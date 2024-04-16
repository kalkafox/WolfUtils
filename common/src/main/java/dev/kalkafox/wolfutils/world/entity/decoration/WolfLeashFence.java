package dev.kalkafox.wolfutils.world.entity.decoration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class WolfLeashFence {

    public static LeashFenceKnotEntity getOrCreateKnot(Level level, BlockPos pos, Mob mob) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        List<LeashFenceKnotEntity> list = level.getEntitiesOfClass(LeashFenceKnotEntity.class, new AABB((double)i - 1.0, (double)j - 1.0, (double)k - 1.0, (double)i + 1.0, (double)j + 1.0, (double)k + 1.0));
        for (LeashFenceKnotEntity leashFenceKnotEntity : list) {
            if (!leashFenceKnotEntity.getPos().equals(pos)) continue;
            return leashFenceKnotEntity;
        }
        LeashFenceKnotEntity leashFenceKnotEntity2 = new LeashFenceKnotEntity(level, pos);
        level.addFreshEntity(leashFenceKnotEntity2);
        return leashFenceKnotEntity2;
    }

}
