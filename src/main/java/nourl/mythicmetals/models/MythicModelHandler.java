package nourl.mythicmetals.models;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;
import nourl.mythicmetals.utils.RegistryHelper;

import java.util.function.BiConsumer;

public class MythicModelHandler {
    public static final EntityModelLayer BANGLUM = RegistryHelper.model("banglum_armor");
    public static final EntityModelLayer CARMOT = RegistryHelper.model("carmot_armor");
    public static final EntityModelLayer CARMOT_SWIRL = RegistryHelper.model("carmot_swirl");
    public static final EntityModelLayer HALLOWED_ARMOR = RegistryHelper.model("hallowed_armor");
    public static final Identifier HALLOWED_CAPE = RegistryHelper.id("textures/models/hallowed_cape.png");
    public static final EntityModelLayer METALLURGIUM = RegistryHelper.model("metallurgium_armor");
    public static final EntityModelLayer RUNITE = RegistryHelper.model("runite_armor");

    public static void init(BiConsumer<EntityModelLayer, TexturedModelData> consumer) {
        consumer.accept(BANGLUM, TexturedModelData.of(BanglumArmorModel.getModelData(), 32, 32));
        consumer.accept(CARMOT, TexturedModelData.of(CarmotArmorModel.getModelData(), 64, 64));
        consumer.accept(CARMOT_SWIRL, TexturedModelData.of(PlayerEntityModel.getTexturedModelData(new Dilation(1.15f), false), 64, 32));
        consumer.accept(HALLOWED_ARMOR, TexturedModelData.of(HallowedArmorModel.getModelData(), 64, 64));
        consumer.accept(METALLURGIUM, TexturedModelData.of(MetallurgiumArmorModel.getModelData(), 32, 16));
        consumer.accept(RUNITE, TexturedModelData.of(RuniteArmorModel.getModelData(), 32, 32));
    }
}
