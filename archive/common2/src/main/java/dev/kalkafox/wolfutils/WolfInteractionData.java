package dev.kalkafox.wolfutils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class WolfInteractionData {
    private final Player player;
    private final Vec3 wolfPos;
    private final Vec3 wolfLookPos;

    public int interactTicks;

    public WolfInteractionData(Player player, Vec3 wolfPos, Vec3 wolfLookPos) {
        this.player = player;
        this.wolfPos = wolfPos;
        this.wolfLookPos = wolfLookPos;
    }


    public Player getPlayer() {
        return player;
    }

    public Vec3 getWolfPos() {
        return wolfPos;
    }

    public Vec3 getWolfLookPos() {
        return wolfLookPos;
    }
}
