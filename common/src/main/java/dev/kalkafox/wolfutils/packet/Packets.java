package dev.kalkafox.wolfutils.packet;

import dev.architectury.networking.NetworkManager;
import dev.kalkafox.wolfutils.WolfUtils;
import dev.kalkafox.wolfutils.client.WolfUtilsClient;
import net.minecraft.resources.ResourceLocation;

public abstract class Packets {

    public static final ResourceLocation UPDATE_WOLF_STATE = new ResourceLocation(WolfUtils.MOD_ID, "check_wolf_status");


    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, UPDATE_WOLF_STATE, (buf, context) -> {

            System.out.println("received packet postwolftick");

            WolfUtilsClient.postWolfTick = buf.readBoolean();
        });

        WolfUtils.getLogger().info("Registered network handler");
    }


}
