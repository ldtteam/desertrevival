package com.ldtteam.desertrevival.levelgen.biomemodifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record DRHotRiversBiomeModifier(HolderSet<PlacedFeature> features, GenerationStep.Decoration step) implements BiomeModifier {
    public static final Codec<DRHotRiversBiomeModifier> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(DRHotRiversBiomeModifier::features),
            GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(DRHotRiversBiomeModifier::step)
    ).apply(instance, DRHotRiversBiomeModifier::new));


    @Override
    public Codec<? extends BiomeModifier> codec() {
        return DRBiomeModifierSerializers.HOT_RIVERS_BIOME_MODIFIER.get();
    }

    @Override
    public void modify(Holder<Biome> holder, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            if (holder.is(Tags.Biomes.IS_DESERT) || holder.is(Tags.Biomes.IS_HOT)) {
                for (Holder<PlacedFeature> feature : features) {
                    builder.getGenerationSettings().addFeature(step, feature);
                }
            }
        }
    }
}
