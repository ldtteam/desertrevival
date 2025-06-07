package com.ldtteam.desertrevival.levelgen.biomemodifier;

import com.ldtteam.desertrevival.DesertRevival;
import com.ldtteam.desertrevival.levelgen.placedfeature.DRPlacedFeatures;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DRBiomeModifiers {
    public static final Map<ResourceKey<BiomeModifier>, BiomeModifierFactory> BIOME_MODIFIERS_FACTORIES = new ConcurrentHashMap<>();

    public static final ResourceKey<DRHotRiversBiomeModifier> JUNGLE_HOT_RIVERS_BIOME_MODIFIER = createBiomeModifier("jungle_hot_rivers_biome_modifier", placedFeatureHolderGetter -> new DRHotRiversBiomeModifier(HolderSet.direct(placedFeatureHolderGetter.getOrThrow(DRPlacedFeatures.NEAR_RIVER_JUNGLE_TREES)), GenerationStep.Decoration.VEGETAL_DECORATION));


    protected static <T extends BiomeModifier> ResourceKey<T> createBiomeModifier(String id, BiomeModifierFactory factory) {
        ResourceKey<T> key = registerKey(id);
        BIOME_MODIFIERS_FACTORIES.put((ResourceKey<BiomeModifier>) key, factory);
        return key;
    }

    public static void init() {
    }


    private static <T extends BiomeModifier> ResourceKey<T> registerKey(String name) {
        return (ResourceKey<T>) DesertRevival.key(ForgeRegistries.Keys.BIOME_MODIFIERS, name);
    }
    @FunctionalInterface
    public interface BiomeModifierFactory {
        BiomeModifier generate(HolderGetter<PlacedFeature> placedFeatureHolderGetter);
    }

}
