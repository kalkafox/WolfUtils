package dev.kalkafox.wolfutils.fabric;

import dev.kalkafox.wolfutils.WolfUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class WolfUtilsFabric implements ModInitializer {
    @Override
    public void onInitialize() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

        });

        WolfUtils.init();
    }
}