package nourl.mythicmetals.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import nourl.mythicmetals.MythicMetals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static nourl.mythicmetals.models.PlayerEnergySwirlFeatureRenderer.SWIRL_TEXTURE;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(method = "renderArm", at = @At("TAIL"))
    private void renderShieldArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
        if (player.getComponent(MythicMetals.CARMOT_SHIELD).isShieldActive()) {
            final var client = MinecraftClient.getInstance();
            float f = player.age + (client.isPaused() ? 0 : client.getTickDelta());

            var shield = player.getComponent(MythicMetals.CARMOT_SHIELD);

            int pieces = (int) (shield.getMaxHealth() % 4 + 1);
            float health = pieces < 3 ? shield.health / 80f : shield.health / 110f;

            var consumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(SWIRL_TEXTURE, (f * .005f) % 1f, f * .005f % 1f));

            if (shield.cooldown > 0) {
                matrices.scale(1.0625f, 1.0625f, 1.0625f);
                sleeve.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV, .9f, .025f, .025f, 1);
            } else // Regular animation
                sleeve.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV, .8f, .1f + health, .05f, 1);
        }
    }

}
