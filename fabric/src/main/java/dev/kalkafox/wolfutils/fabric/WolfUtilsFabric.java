package dev.kalkafox.wolfutils.fabric;

import dev.kalkafox.wolfutils.WolfUtils;
import net.fabricmc.api.ModInitializer;

public class WolfUtilsFabric implements ModInitializer {
    @Override
    public void onInitialize() {

        WolfUtils.init();
    }
}