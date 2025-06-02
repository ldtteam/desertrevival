package com.ldtteam.desertrevival;

import com.ldtteam.desertrevival.levelgen.biomemodifier.DRBiomeModifierSerializers;
import com.ldtteam.desertrevival.levelgen.placementmodifier.DRPlacementModifierTypes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DesertRevival.MOD_ID)
public class DesertRevival {

    public static final String MOD_ID = "desertrevival";
    public DesertRevival() {
        DRPlacementModifierTypes.DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        DRBiomeModifierSerializers.BIOME_MODIFIER_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name); // 1.21: ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registryKey, String name) {
        return ResourceKey.create(registryKey, id(name));
    }
}
