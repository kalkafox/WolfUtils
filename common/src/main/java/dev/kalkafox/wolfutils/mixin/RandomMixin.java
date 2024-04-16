package dev.kalkafox.wolfutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(Random.class)
public class RandomMixin {

    @Inject(method = "nextInt()I", at = @At(value = "RETURN", remap = false), remap = false, cancellable = true)
    private void onNextInt(CallbackInfoReturnable<Integer> cir) {
        System.out.println("nextint is now 0");
        cir.setReturnValue(0);
    }

    @Inject(method = "nextInt(I)I", at = @At(value = "RETURN", remap = false), remap = false, cancellable = true)
    private void onNextInt(int bounds, CallbackInfoReturnable<Integer> cir) {
        System.out.println("nextint is now 0");
        cir.setReturnValue(0);
    }

    @Inject(method = "nextBytes([B)V", at = @At("HEAD"), require = 1, remap = false, cancellable = true)
    private void onNextBytes(byte[] bytes, CallbackInfo ci) {
        System.out.println("nextBytes is now modified");
        // You can add your modification logic here if needed
    }

    @Inject(method = "nextDouble()D", at = @At("HEAD"), require = 1, remap = false, cancellable = true)
    private void onNextDouble(CallbackInfoReturnable<Double> cir) {
        System.out.println("nextDouble is now modified");
        // You can add your modification logic here if needed
    }

    @Inject(method = "nextFloat()F", at = @At("HEAD"), require = 1, remap = false, cancellable = true)
    private void onNextFloat(CallbackInfoReturnable<Float> cir) {
        System.out.println("nextFloat is now modified");
        // You can add your modification logic here if needed
    }

    @Inject(method = "nextGaussian()D", at = @At("HEAD"), require = 1, remap = false, cancellable = true)
    private void onNextGaussian(CallbackInfoReturnable<Double> cir) {
        System.out.println("nextGaussian is now modified");
        // You can add your modification logic here if needed
    }

    @Inject(method = "nextBoolean()Z", at = @At("HEAD"), require = 1, remap = false, cancellable = true)
    private void onNextBoolean(CallbackInfoReturnable<Boolean> cir) {
        System.out.println("nextBoolean is now modified");
        // You can add your modification logic here if needed
    }


}
