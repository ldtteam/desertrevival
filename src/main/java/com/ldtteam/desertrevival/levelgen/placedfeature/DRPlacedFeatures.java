package com.ldtteam.desertrevival.levelgen.placedfeature;

import com.ldtteam.desertrevival.DesertRevival;
import com.ldtteam.desertrevival.levelgen.placementmodifier.NearBiomePlacementModifier2D;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DRPlacedFeatures {
    public static final Map<ResourceKey<PlacedFeature>, PlacedFeatureFactory> PLACED_FEATURE_FACTORIES = new Reference2ObjectOpenHashMap<>();


    public static final ResourceKey<PlacedFeature> NEAR_RIVER_JUNGLE_TREES = createPlacedFeature("near_river_trees", VegetationFeatures.TREES_BIRCH_AND_OAK, () -> Util.make(new ArrayList<>(), list -> {
        list.addAll(VegetationPlacements.treePlacement(PlacementUtils.countExtra(500, 0.1F, 1)));
        list.add(new NearBiomePlacementModifier2D(2, 32, true, Either.right(BiomeTags.IS_RIVER))); // Look for rivers within 32 blocks with 2 block steps on the horizontal plane at the world surface
        list.add(BiomeFilter.biome());
    }));

    protected static <FC extends FeatureConfiguration> ResourceKey<PlacedFeature> createPlacedFeature(String id, ResourceKey<ConfiguredFeature<?, ?>> feature, Supplier<List<PlacementModifier>> placementModifiers) {
        ResourceKey<PlacedFeature> placedFeatureKey = registerKey(id);
        PLACED_FEATURE_FACTORIES.put(placedFeatureKey, configuredFeatureHolderGetter -> new PlacedFeature(configuredFeatureHolderGetter.getOrThrow(feature), placementModifiers.get()));
        return placedFeatureKey;
    }


    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return DesertRevival.key(Registries.PLACED_FEATURE, name);
    }


    @FunctionalInterface
    public interface PlacedFeatureFactory {
        PlacedFeature generate(HolderGetter<ConfiguredFeature<?, ?>> configuredFeatureHolderGetter);
    }
}
