package nourl.mythicmetals.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import nourl.mythicmetals.blocks.AquariumResonatorBlockEntity;
import nourl.mythicmetals.blocks.AquariumStewardBlockEntity;
import nourl.mythicmetals.blocks.MythicBlocks;
import nourl.mythicmetals.misc.RegistryHelper;

public class RegisterBlockEntityTypes {


    public static final BlockEntityType<AquariumStewardBlockEntity> AQUARIUM_STEWARD_BLOCK_ENTITY_TYPE =
            FabricBlockEntityTypeBuilder.create(AquariumStewardBlockEntity::new, MythicBlocks.AQUARIUM_STEWARD).build();
    public static final BlockEntityType<AquariumResonatorBlockEntity> AQUARIUM_RESONATOR_BLOCK_ENTITY_TYPE =
            FabricBlockEntityTypeBuilder.create(AquariumResonatorBlockEntity::new, MythicBlocks.AQUARIUM_RESONATOR).build();

    public static void init() {
        RegistryHelper.blockEntity("aquarium_sentry", RegisterBlockEntityTypes.AQUARIUM_STEWARD_BLOCK_ENTITY_TYPE);
    }
}
