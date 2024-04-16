package dev.kalkafox.wolfutils.client;

import dev.kalkafox.wolfutils.packet.Packets;

public final class WolfUtilsClient {

    public static boolean postWolfTick;
    public static boolean shouldModifySleepFade;


    public static void clientInit() {

        Packets.register();
    }

}
