package dev.kalkafox.wolfutils.fabric.client;

import dev.kalkafox.wolfutils.client.WolfUtilsClient;
import net.fabricmc.api.ClientModInitializer;

public class WolfUtilsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        WolfUtilsClient.clientInit();
    }
}
