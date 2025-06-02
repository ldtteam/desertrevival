package com.ldtteam.desertrevival.levelgen.biomemodifier;

import com.ldtteam.desertrevival.DesertRevival;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DRBiomeModifierSerializers {

    public static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, DesertRevival.MOD_ID);

    public static RegistryObject<Codec<DRHotRiversBiomeModifier>> HOT_RIVERS_BIOME_MODIFIER = BIOME_MODIFIER_SERIALIZERS.register("hot_rivers_biome_modifier", () -> DRHotRiversBiomeModifier.CODEC);
}
