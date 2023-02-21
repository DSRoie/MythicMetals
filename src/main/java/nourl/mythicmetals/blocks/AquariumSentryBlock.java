package nourl.mythicmetals.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nourl.mythicmetals.MythicMetals;
import org.jetbrains.annotations.Nullable;

public class AquariumSentryBlock extends BlockWithEntity {

    protected AquariumSentryBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AquariumSentryBlockEntity(MythicMetals.AQUARIUM_SENTRY_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, MythicMetals.AQUARIUM_SENTRY_BLOCK_ENTITY_TYPE, AquariumSentryBlockEntity::tick);
    }


}
