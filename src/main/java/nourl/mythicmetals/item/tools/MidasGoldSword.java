package nourl.mythicmetals.item.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MidasGoldSword extends SwordItem {

    public static final NbtKey<Integer> GOLD_FOLDED = new NbtKey<>("GoldFolded", NbtKey.Type.INT);
    public static final NbtKey<Boolean> IS_GILDED = new NbtKey<>("IsGilded", NbtKey.Type.BOOLEAN);
    public static final NbtKey<Boolean> IS_ROYAL = new NbtKey<>("IsRoyal", NbtKey.Type.BOOLEAN);

    public MidasGoldSword(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {

        Multimap<EntityAttribute, EntityAttributeModifier> mapnite = this.getAttributeModifiers(slot);

        int goldCount = stack.get(GOLD_FOLDED);
        if (goldCount > 0) {

            mapnite = HashMultimap.create(mapnite);

            mapnite.removeAll(EntityAttributes.GENERIC_ATTACK_DAMAGE);

            float baseDamage = getAttackDamage();

            int bonus = MathHelper.clamp(MathHelper.floor((float) goldCount / 64), 0, 6);

            if (goldCount >= 1280) {
                bonus += 1;
            }

            mapnite.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(Item.ATTACK_DAMAGE_MODIFIER_ID, "Damage modifier", baseDamage + bonus, EntityAttributeModifier.Operation.ADDITION));

        }
        return slot == EquipmentSlot.MAINHAND ? mapnite : super.getAttributeModifiers(slot);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> lines, TooltipContext context) {
        int lineIndex = 1;

        if (lines.size() > 2) {
            var enchantCount = stack.getEnchantments().size();
            lineIndex = enchantCount + 1;
        }

        int goldCount = stack.get(MidasGoldSword.GOLD_FOLDED);
        int level = calculateSwordLevel(goldCount);

        if (level > 20) {
            level = 20 + level / 6;
        }

        // Spout fun facts and lore while leveling up the sword
        lines.add(lineIndex, Text.translatable("tooltip.midas_gold.level." + level).formatted(Formatting.GOLD));
        if (goldCount == 0) {
            return;
        }

        // Remove the cap from tooltip when maxed
        if (goldCount >= 1280) {
            if (goldCount == 10000) {
                // e.g. Folds: 10000 **MAXED**
                lines.add(lineIndex + 1, Text.translatable("tooltip.midas_gold.maxed", goldCount).formatted(Formatting.GOLD, Formatting.BOLD));
            } else {
                // e.g. Folds: 2500
                lines.add(lineIndex + 1, Text.translatable("tooltip.midas_gold.fold_counter", goldCount).formatted(Formatting.GOLD));
            }
            return;
        }

        // Handle the cap format
        if (stack.has(IS_ROYAL)) {
            // e.g. 63/128
            lines.add(lineIndex + 1, Text.literal(goldCount + " / " + 1280).formatted(Formatting.GOLD));
        } else if (stack.has(IS_GILDED)) {
            // e.g. 63/128
            lines.add(lineIndex + 1, Text.literal(goldCount + " / " + 640).formatted(Formatting.GOLD));
        } else {
            // e.g. 63/128
            lines.add(lineIndex + 1, Text.literal(goldCount + " / " + (64 + level * 64)).formatted(Formatting.GOLD));
        }

    }

    public static float countGold(int goldCount) {
        if (goldCount >= 1280) return 1.0f;
        return switch (goldCount / 64) {
            case 1 -> 0.1f;
            case 2, 3 -> 0.2f;
            case 4 -> 0.3f;
            case 5, 6, 7, 8, 9 -> 0.4f;
            case 10,11 -> 0.5f;
            case 12,13 -> 0.6f;
            case 14,15 -> 0.7f;
            case 16,17 -> 0.8f;
            case 18 -> 0.9f;
            case 19 -> 1.0f;
            default -> 0.0f;
        };
    }

    /**
     * Calculates a level from intervals of 64.
     * Used for appending specific text to a Midas Gold Sword tooltip
     * @param goldCount The amount of gold that is currently applied on this stack
     * @return amount of gold divided by 64, or 0 if less than 64 gold
     */
    public static int calculateSwordLevel(int goldCount) {
        if (goldCount < 64) return 0;
        return (goldCount / 64);
    }
}
