package dev.kalkafox.wolfutils.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.MobRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MobRenderer.class)
public class MobRendererInvoker {

    @Invoker("addVertexPair")
    public static void invokeAddVertexPair(VertexConsumer consumer, Matrix4f matrix, float f, float g, float h, int entityBlockLightLevel, int leashHolderBlockLightLevel, int entitySkyLightLevel, int leashHolderSkyLightLevel, float i, float j, float k, float l, int index, boolean bl) {
        throw new AssertionError();
    }

}
