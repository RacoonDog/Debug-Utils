package io.github.racoondog.debugutils.mixin;

import io.github.racoondog.debugutils.ConfigHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @Shadow @Final protected EntityRenderDispatcher dispatcher;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <T extends Entity> void renderUuid(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!ConfigHandler.uuidDisplay) return;

        float f = entity.getHeight() + 0.5F;
        matrices.push();
        matrices.translate(0.0, f, 0.0);
        matrices.multiply(dispatcher.getRotation());
        matrices.scale(-0.0125F, -0.0125F, 0.025F);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        TextRenderer textRenderer = this.getTextRenderer();
        Text text = Text.literal(entity.getUuidAsString());
        float h = (float)(-textRenderer.getWidth(text) / 2);
        textRenderer.draw(text, h, 0, -1, false, matrix4f, vertexConsumers, false, 0, light);

        matrices.pop();

        ci.cancel();
    }
}
