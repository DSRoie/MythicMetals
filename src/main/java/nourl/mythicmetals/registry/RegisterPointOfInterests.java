package nourl.mythicmetals.registry;

import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.world.poi.PointOfInterestType;
import nourl.mythicmetals.blocks.MythicBlocks;
import nourl.mythicmetals.misc.RegistryHelper;

public class RegisterPointOfInterests {
    public static final PointOfInterestType AQUARIUM_SENTRY_POI = PointOfInterestHelper.register(RegistryHelper.id("aquarium_sentry"), 0, 1, MythicBlocks.AQUARIUM_SENTRY);

    public static void init() {}
}
