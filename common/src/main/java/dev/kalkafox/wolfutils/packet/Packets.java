package dev.kalkafox.wolfutils.packet;

import dev.architectury.networking.NetworkManager;
import dev.kalkafox.wolfutils.WolfUtils;
import dev.kalkafox.wolfutils.client.WolfUtilsClient;
import net.minecraft.resources.ResourceLocation;

public final class Packets {

    public static final ResourceLocation UPDATE_WOLF_STATE = new ResourceLocation(WolfUtils.MOD_ID, "check_wolf_status");
    public static final ResourceLocation MODIFY_SLEEP_FADE = new ResourceLocation(WolfUtils.MOD_ID, "modify_sleep_fade");


    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, UPDATE_WOLF_STATE, (buf, context) -> {

            WolfUtilsClient.postWolfTick = buf.readBoolean();
        });

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, MODIFY_SLEEP_FADE, (buf, context) -> {

            WolfUtilsClient.shouldModifySleepFade = buf.readBoolean();
        });

        WolfUtils.getLogger().info("Registered network handler");
    }


}
