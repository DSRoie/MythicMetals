package nourl.mythicmetals.abilities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import nourl.mythicmetals.armor.ArmorSet;
import nourl.mythicmetals.item.tools.ToolSet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An ability is a container of items which have specific behaviour.
 * These are put into a set, and queried for later when needed.
 * Most of these are implemented in {@link nourl.mythicmetals.mixin.EnchantmentHelperMixin EnchantmentHelperMixin}
 * <br>
 * Other than that, this class also handles tooltips for the items.
 */
public class Ability {

    private final String tooltip;
    private final int level;
    private final Set<Item> items;
    private boolean showLevel = true;

    public Ability(String translationKey, int level) {
        this.tooltip = translationKey;
        this.level = level;
        this.items = new HashSet<>();
    }

    public Ability(String translationKey, int level, boolean showLevel) {
        this.tooltip = translationKey;
        this.level = level;
        this.items = new HashSet<>();
        this.showLevel = showLevel;
    }

    public int getLevel() {
        return level;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void addItem(Item item, Style style) {
        items.add(item);
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT))
            addTooltip(item, style);

    }

    public void addArmorSet(ArmorSet armorSet, Style style) {
        items.add(armorSet.getHelmet());
        items.add(armorSet.getChestplate());
        items.add(armorSet.getLeggings());
        items.add(armorSet.getBoots());
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
            addTooltip(armorSet.getHelmet(), style);
            addTooltip(armorSet.getChestplate(), style);
            addTooltip(armorSet.getLeggings(), style);
            addTooltip(armorSet.getBoots(), style);
        }

    }

    public void addToolSet(ToolSet toolSet, Style style) {
        items.add(toolSet.getSword());
        items.add(toolSet.getAxe());
        items.add(toolSet.getHoe());
        items.add(toolSet.getPickaxe());
        items.add(toolSet.getShovel());
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
            addTooltip(toolSet.getSword(), style);
            addTooltip(toolSet.getAxe(), style);
            addTooltip(toolSet.getHoe(), style);
            addTooltip(toolSet.getPickaxe(), style);
            addTooltip(toolSet.getShovel(), style);
        }
    }

    @Environment(EnvType.CLIENT)
    public void addTooltip(Item item, Style style) {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            MutableText text = Text.literal("");
            if (stack.isOf(item)) {
                text.append(Text.translatable("abilities.mythicmetals." + tooltip));
                text.setStyle(style);
                if (showLevel) {
                    text.append(" ").append(Text.translatable("enchantment.level." + level));
                }
                if (lines.size() > 2) {
                    var enchantCount = stack.getEnchantments().size();
                    lines.add(enchantCount + 1, text);
                } else lines.add(text);
            }
        });
    }

   public static void addTooltipOnStack(ItemStack stack, List<Text> lines, Style style, String translationKey) {
        MutableText text = Text.literal("");
        text.append(Text.translatable(translationKey));
        text.setStyle(style);
        if (lines.size() > 2) {
            var enchantCount = stack.getEnchantments().size();
            lines.add(enchantCount + 1, text);
        } else lines.add(text);
    }
}
