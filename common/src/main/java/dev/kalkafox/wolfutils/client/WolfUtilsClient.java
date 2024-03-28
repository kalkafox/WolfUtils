package dev.kalkafox.wolfutils.client;

import dev.kalkafox.wolfutils.packet.Packets;

public class WolfUtilsClient {

    public static boolean postWolfTick;


    public static void clientInit() {

        System.out.println("we init the client, yo");

        Packets.register();
    }

}
