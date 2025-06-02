package com.ldtteam.desertrevival.levelgen.placementmodifier;

import com.ldtteam.desertrevival.DesertRevival;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DRPlacementModifierTypes {

    public static final DeferredRegister<PlacementModifierType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, DesertRevival.MOD_ID);

    public static final RegistryObject<PlacementModifierType<NearBiomePlacementModifier2D>> NEAR_BIOME_2D = DEFERRED_REGISTER.register("near_biome_2d", () -> () -> NearBiomePlacementModifier2D.CODEC);
}
