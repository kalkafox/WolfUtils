package dev.kalkafox.wolfutils.mixin;


import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LeashFenceKnotEntity.class)
public abstract class LeashFenceKnotEntityMixin extends HangingEntity {

    protected LeashFenceKnotEntityMixin(EntityType<? extends HangingEntity> entityType, Level level, BlockPos pos) {
        super(entityType, level, pos);
    }

}
