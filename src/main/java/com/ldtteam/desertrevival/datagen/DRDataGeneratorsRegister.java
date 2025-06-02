package com.ldtteam.desertrevival.datagen;

import com.ldtteam.desertrevival.DesertRevival;
import com.ldtteam.desertrevival.levelgen.biomemodifier.DRBiomeModifiers;
import com.ldtteam.desertrevival.levelgen.placedfeature.DRPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = DesertRevival.MOD_ID)
public class DRDataGeneratorsRegister {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.PLACED_FEATURE, pContext -> DRPlacedFeatures.PLACED_FEATURE_FACTORIES.forEach((resourceKey, factory) -> pContext.register(resourceKey, factory.generate(pContext.lookup(Registries.CONFIGURED_FEATURE)))))
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, pContext -> DRBiomeModifiers.BIOME_MODIFIERS_FACTORIES.forEach((key, modifier) -> pContext.register(key, modifier.generate(pContext.lookup(Registries.PLACED_FEATURE)))));

    @SubscribeEvent
    protected static void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        DatapackBuiltinEntriesProvider datapackBuiltinEntriesProvider = new DatapackBuiltinEntriesProvider(output, lookupProvider, BUILDER, Set.of(DesertRevival.MOD_ID));
        generator.addProvider(event.includeServer(), datapackBuiltinEntriesProvider);
    }

}
