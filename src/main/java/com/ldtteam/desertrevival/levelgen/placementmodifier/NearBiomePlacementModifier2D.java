package com.ldtteam.desertrevival.levelgen.placementmodifier;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.EitherCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class NearBiomePlacementModifier2D extends PlacementModifier {

    private static final Int2ObjectOpenHashMap<int[][][]> SPIRAL_CACHE = new Int2ObjectOpenHashMap<>();



    private final int blockStep;
    private final int maxDistance;
    private boolean atSurface;
    private final Either<ResourceKey<Biome>, TagKey<Biome>> biome;

    public NearBiomePlacementModifier2D(int blockStep, int maxDistance, boolean atSurface, Either<ResourceKey<Biome>, TagKey<Biome>> biome) {
        this.blockStep = blockStep;
        this.maxDistance = maxDistance;
        this.atSurface = atSurface;
        this.biome = biome;
    }

    public static final Codec<NearBiomePlacementModifier2D> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.INT.fieldOf("block_step").forGetter(modifier -> modifier.blockStep),
                    Codec.INT.fieldOf("max_distance").forGetter(modifier -> modifier.maxDistance),
                    Codec.BOOL.fieldOf("at_surface").forGetter(modifier -> modifier.atSurface),
                    new EitherCodec<>(ResourceKey.codec(Registries.BIOME), TagKey.hashedCodec(Registries.BIOME)).fieldOf("biome").forGetter(modifier -> modifier.biome)
            ).apply(instance, NearBiomePlacementModifier2D::new)
    );


    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource randomSource, BlockPos blockPos) {
        WorldGenLevel level = placementContext.getLevel();

        int spiralIteratorSize = this.maxDistance / this.blockStep;
        int[][][] spiral = SPIRAL_CACHE.computeIfAbsent(spiralIteratorSize, k -> spiral2D(spiralIteratorSize)); // This iterates from the center outwards in a spiral pattern, allowing us to find the nearest biome efficiently.

        if (!level.getBiome(blockPos).is(BiomeTags.HAS_DESERT_PYRAMID))
        {
            return Stream.empty();
        }
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int[][] xzOffsets : spiral) {
            for (int[] offset : xzOffsets) {
                int offsetX = offset[0] * blockStep;
                int offsetZ = offset[1] * blockStep;
                int worldX = blockPos.getX() + offsetX;
                int worldZ = blockPos.getZ() + offsetZ;
                int worldY = atSurface ? level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, worldX, worldZ) : blockPos.getY();
                mutable.set(worldX, worldY, worldZ);
                if (this.biome.left().isPresent()) {
                    ResourceKey<Biome> biomeKey = this.biome.left().orElseThrow();
                    if (level.getBiome(mutable).is(biomeKey)) {
                        if (!FMLLoader.isProduction()) {
                            // Debugging output to verify the biome match
                            System.out.println("Found nearby matching biome: " + biomeKey.location() + " at " + mutable + "for position " + blockPos);
                        }
                        return Stream.of(mutable);
                    }
                } else {
                    TagKey<Biome> biomeTag = this.biome.right().orElseThrow();
                    if (level.getBiome(mutable).is(biomeTag)) {
                        if (!FMLLoader.isProduction()) {
                            // Debugging output to verify the biome match
                            System.out.println("Found nearby matching biome tag: " + biomeTag.location() + " at " + mutable + "for position " + blockPos);
                        }
                        return Stream.of(mutable);
                    }
                }

            }
        }
        return Stream.empty(); // No matching biome found within the specified distance.
    }





    @Override
    public PlacementModifierType<?> type() {
        return DRPlacementModifierTypes.NEAR_BIOME_2D.get();
    }


    static int[][][] spiral2D(int size) {
        Map<Integer, List<int[]>> distanceMap = new TreeMap<>();
        for (int x = -size; x <= size; x++) {
            for (int z = -size; z <= size; z++) {
                int distance = x * x + z * z;
                distanceMap.computeIfAbsent(distance, dist -> new ArrayList<>()).add(new int[]{x, z});
            }
        }

        List<int[][]> offsets = new ArrayList<>();

        for (List<int[]> value : distanceMap.values()) {
            offsets.add(value.toArray(int[][]::new));
        }
        return offsets.toArray(new int[offsets.size()][][]);
    }
}
