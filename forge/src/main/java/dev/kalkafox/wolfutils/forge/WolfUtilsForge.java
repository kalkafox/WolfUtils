package dev.kalkafox.wolfutils.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.kalkafox.wolfutils.WolfUtils;
import dev.kalkafox.wolfutils.client.WolfUtilsClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import static dev.kalkafox.wolfutils.WolfUtils.MOD_ID;

@Mod(MOD_ID)
public class WolfUtilsForge {
    public WolfUtilsForge() {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        EventBuses.registerModEventBus(MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        WolfUtils.init();

        bus.<FMLClientSetupEvent>addListener(fmlClientSetupEvent -> WolfUtilsClient.clientInit());



    }
}