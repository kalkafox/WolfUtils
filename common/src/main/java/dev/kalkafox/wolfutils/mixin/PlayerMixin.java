package dev.kalkafox.wolfutils.mixin;

import dev.kalkafox.wolfutils.client.event.ClientEventHandler;
import dev.kalkafox.wolfutils.event.EventHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onPrePlayerTick(CallbackInfo ci) {

        if (this.level().isClientSide) {
            ClientEventHandler.onPrePlayerTick((Player)(Object)this);
        } else {
            EventHandler.onPrePlayerTick((Player)(Object)this);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onPostPlayerTick(CallbackInfo ci) {
        EventHandler.onPostPlayerTick((Player)(Object)this);
    }

}
