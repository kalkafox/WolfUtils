package dev.kalkafox.wolfutils.mixin;

import dev.kalkafox.wolfutils.event.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {


    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "startSleeping", at = @At("TAIL"))
    private void onSleep(BlockPos pos, CallbackInfo ci) {

        EventHandler.onSleep(pos, (LivingEntity) (Object) this);

    }

    @Inject(method = "stopSleeping", at = @At("TAIL"))
    private void onStopSleep(CallbackInfo ci) {
        EventHandler.onStopSleep((LivingEntity) (Object) this);
    }

}
