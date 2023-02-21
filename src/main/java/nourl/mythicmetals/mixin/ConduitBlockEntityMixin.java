package nourl.mythicmetals.mixin;

import net.minecraft.block.entity.ConduitBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestStorage;
import nourl.mythicmetals.blocks.AquariumSentryBlockEntity;
import nourl.mythicmetals.registry.RegisterPointOfInterests;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ConduitBlockEntity.class)
public class ConduitBlockEntityMixin {

    @Inject(method = "givePlayersEffects", at = @At("TAIL"))
    private static void mythicmetals$invokeNearbySentries(World world, BlockPos pos, List<BlockPos> activatingBlocks, CallbackInfo ci) {
        if (world.isClient) return;
        int radius = activatingBlocks.size() / 7 * 16;
        ((ServerWorld)world).getPointOfInterestStorage()
            .getInCircle(type -> type.value() == RegisterPointOfInterests.AQUARIUM_SENTRY_POI, pos, radius, PointOfInterestStorage.OccupationStatus.ANY)
            .forEach(pointOfInterest -> {
                var blockEntity = world.getBlockEntity(pointOfInterest.getPos());
                if (blockEntity instanceof AquariumSentryBlockEntity entity) {
                    entity.activateSentry();
                }
            });
    }
}
