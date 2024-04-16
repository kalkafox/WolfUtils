package dev.kalkafox.wolfutils.mixin.client;


import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kalkafox.wolfutils.mixin.MobRendererInvoker;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MobRenderer.class)
public abstract class MobRendererMixin<T extends Mob, M extends EntityModel<T>>
        extends LivingEntityRenderer<T, M> {

    public MobRendererMixin(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

//    @Inject(method = "renderLeash", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/MobRenderer;addVertexPair(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lorg/joml/Matrix4f;FFFIIIIFFFFIZ)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
//    private <E extends Entity> void onRenderLeash(T entityLiving, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, E leashHolder, CallbackInfo ci, Vec3 vec3, double d, Vec3 vec32, double e, double f, double g, double h, double i, float j, float k, float l, float m, VertexConsumer vertexConsumer, Matrix4f matrix4f, float n, float o, float p, BlockPos blockPos, BlockPos blockPos2, int q, int r, int s, int t) {
//        if (entityLiving instanceof Wolf && leashHolder instanceof Player) {
//            ci.cancel();
//
//            float[] colors = ((Wolf) entityLiving).getCollarColor().getTextureDiffuseColors();
//
//            int u;
//            for (u = 0; u <= 24; ++u) {
//                MobRendererInvoker.invokeAddVertexPair(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025f, 0.025f, o, p, u, false);
//            }
//            for (u = 24; u >= 0; --u) {
//                MobRendererInvoker.invokeAddVertexPair(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025f, 0.025f, o, p, u, false);
//            }
//            poseStack.popPose();
//
//        }
//    }

//
//    @Inject(method = "addVertexPair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;vertex(Lorg/joml/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", shift = At.Shift.BEFORE))
//    private static void onAddVertexPair(VertexConsumer consumer, Matrix4f matrix, float f, float g, float h, int entityBlockLightLevel, int leashHolderBlockLightLevel, int entitySkyLightLevel, int leashHolderSkyLightLevel, float i, float j, float k, float l, int index, boolean bl, CallbackInfo ci, @Local(ordinal = 5) LocalRef<Float> localRef) {
//
//        localRef.get();
//
//    }
}
