package dev.kalkafox.wolfutils.mixin;

import dev.kalkafox.wolfutils.WolfUtils;
import dev.kalkafox.wolfutils.event.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal implements NeutralMob {

    protected WolfMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }


    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    private void onGetAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
//        for (Wolf wolfy : WolfUtils.sleepingWolves) {
//            if (wolfy.getId() == this.getId()) {
//                cir.setReturnValue(null);
//            }
//        }

        if (WolfUtils.sleepingWolves.contains((Wolf)(Object)this)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "playStepSound", at = @At("HEAD"), cancellable = true)
    private void onPlayStepSound(BlockPos pos, BlockState state, CallbackInfo ci) {
        ci.cancel();
        EventHandler.playWolfStepSound((Wolf)(Object)this);
    }

//    @ModifyArg(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Wolf;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), index = 2)
//    private void onPlayStepSound(float pitch) {
//
//    }

}
