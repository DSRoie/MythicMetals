package nourl.mythicmetals.blocks;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import io.wispforest.owo.util.Maldenhagen;
import io.wispforest.owo.util.TagInjector;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import nourl.mythicmetals.MythicMetals;
import nourl.mythicmetals.utils.RegistryHelper;

import java.util.*;
import java.util.function.Consumer;

/**
 * This class is a container which is used for the creation for all the blocks in Mythic Metals.
 * For creating blocks using this you want to start with looking at the {@link Builder}, which
 * contains all the methods for creating blocks.
 *
 * @author glisco
 * @author Noaaan
 */
@SuppressWarnings({"unused"})
public class BlockSet {
    private final OreBlock ore;
    private final Block storageBlock;
    private final Block oreStorageBlock;
    private final AnvilBlock anvil;

    private final String name;
    private final boolean fireproof;

    private final Multimap<Block, Identifier> miningLevels;
    private final Multimap<AnvilBlock, Identifier> anvilMap;
    private final Map<String, OreBlock> oreVariants;

    /**
     * This constructor collects the smaller constructors from the {@link Builder} and creates a set of blocks.
     * Use {@link Builder#begin(String, boolean) BlockSet.Builder.begin} to begin,
     * and call {@link Builder#finish()} when you are done.
     *  @param name            Common name for the entire set of blocks, applies to every block created.
     * @param ore             Contains a vanilla {@link OreBlock}.
     * @param storageBlock    Contains a {@link Block} which is used as a storage block.
     * @param oreStorageBlock Contains a {@link Block} which is used as a ore storage block.
     * @param anvil           Contains an {@link AnvilBlock}
     * @param oreVariants     A map of a string and {@link OreBlock} which is used for variant ores.
     * @param fireproof       Boolean for creating fireproof block sets.
     * @param miningLevels    A map containing all the blocks being registered with their corresponding mining levels.
     * @param anvilMap        A map containing all anvils and their levels, so that they can be disabled.
     */
    private BlockSet(String name,
                     OreBlock ore,
                     Block storageBlock,
                     Block oreStorageBlock,
                     AnvilBlock anvil,
                     Map<String, OreBlock> oreVariants,
                     boolean fireproof,
                     Multimap<Block, Identifier> miningLevels,
                     Multimap<AnvilBlock, Identifier> anvilMap) {

        this.name = name;
        this.fireproof = fireproof;

        this.ore = ore;
        this.storageBlock = storageBlock;
        this.oreStorageBlock = oreStorageBlock;
        this.anvil = anvil;

        this.oreVariants = oreVariants;
        this.miningLevels = miningLevels;
        this.anvilMap = anvilMap;
    }

    private void register() {

        if (ore != null) {
            RegistryHelper.block(name + "_ore", ore, fireproof);
        }

        oreVariants.forEach((s, block) -> RegistryHelper.block(s + "_" + name + "_ore", block, fireproof
        ));

        if (oreStorageBlock != null) {
            RegistryHelper.block("raw_" + name + "_block", oreStorageBlock, fireproof);
        }
        if (storageBlock != null) {
            RegistryHelper.block(name + "_block", storageBlock, fireproof);
        }
        if (anvil != null) {
            RegistryHelper.block(name + "_anvil", anvil, fireproof);

        }
        // Inject all the mining levels into their tags.
        if (MythicMetals.CONFIG.enableAnvils) {
            anvilMap.forEach(((anvilBlock, level) -> TagInjector.inject(Registry.BLOCK, RegistryHelper.id("anvils"), anvilBlock)));
            anvilMap.forEach(((anvilBlock, level) -> TagInjector.inject(Registry.BLOCK, level, anvilBlock)));
        }
        miningLevels.forEach((block, level) -> TagInjector.inject(Registry.BLOCK, level, block));
        miningLevels.forEach((block, level) -> TagInjector.inject(Registry.BLOCK, RegistryHelper.id("blocks"), block));
    }

    /**
     * @return Returns the ore block in the set
     */
    public OreBlock getOre() {
        return ore;
    }

    /**
     * @return Returns the storage block from the set
     */
    public Block getStorageBlock() {
        return storageBlock;
    }

    /**
     * @return Returns the ore storage block from the set
     */
    public Block getOreStorageBlock() {
        return oreStorageBlock;
    }

    /**
     * @param variant The string of the ore variants name
     * @return Returns the specified ore variant from the variant map in the blockset
     */
    public OreBlock getOreVariant(String variant) {
        return oreVariants.get(variant);
    }

    public Set<Block> getOreVariants() {
        return ImmutableSet.copyOf(oreVariants.values());
    }

    public String getName(){
        return this.name;
    }

    /**
     * This is the BlockSet Builder, which is used for constructing new sets of blocks.
     * <p>
     * To begin creating BlockSets you want to call:
     * {@code public static final BlockSet SETNAME = }{@link Builder#begin(String, boolean) BlockSet.Builder.begin()}
     * where you provide a {@code string} for the name/key, and the {@code fireproof} boolean.
     * <p>
     * When creating blocks it's important to call {@link #strength(float)} before creating a block or any set.
     * This is because the values are grabbed from this method. You can call it multiple times if you wish to
     * specifically tailor the values for individual blocks.
     * <p>
     * When you are finished with adding your blocks to the set,
     * call {@link Builder#finish() Builder.finish} when you are done.
     * If you need any examples on how to apply this builder in practice, see {@link MythicBlocks}.
     *
     * @author glisco
     * @author Noaaan
     * @see Builder#begin(String, boolean) Builder.begin
     * @see MythicBlocks
     */
    public static class Builder {

        private static final List<BlockSet> toBeRegistered = new ArrayList<>();

        private final String name;
        private final boolean fireproof;
        private final Map<String, OreBlock> oreVariants = new LinkedHashMap<>();
        private OreBlock ore = null;
        private Block storageBlock = null;
        private Block oreStorageBlock = null;
        private AnvilBlock anvil = null;
        private BlockSoundGroup currentSounds = BlockSoundGroup.STONE;
        private float currentHardness = -1;
        private float currentResistance = -1;
        private final Multimap<Block, Identifier> miningLevels = HashMultimap.create();
        private final Multimap<AnvilBlock, Identifier> anvilMap = HashMultimap.create();
        private final Consumer<FabricBlockSettings> settingsProcessor = fabricBlockSettings -> {
        };

        private final Identifier SHOVEL = new Identifier("mineable/shovel");
        private final Identifier PICKAXE = new Identifier("mineable/pickaxe");

        /**
         * @see #begin(String, boolean)
         */
        private Builder(String name, boolean fireproof) {
            this.name = name;
            this.fireproof = fireproof;
        }

        /**
         * This method begins the creation of a block set.
         * You can add as many blocks as you want in the set
         * Call {@link Builder#finish()} when you are done.
         *
         * @param name      The name of the new block set
         * @param fireproof Boolean of whether or not the entire set should be fireproof
         */
        public static Builder begin(String name, boolean fireproof) {
            return new Builder(name, fireproof);
        }

        public static void register() {
            toBeRegistered.forEach(BlockSet::register);
            toBeRegistered.clear();
        }

        /**
         * Used internally for configuring blocks
         *
         * @param material   Vanilla {@link Material}, determines piston behaviour.
         * @param hardness   Determines the breaking time of the block.
         * @param resistance Determines blast resistance of a block.
         * @param sounds     Determines the sounds that blocks play when interacted with.
         */
        private static FabricBlockSettings blockSettings(Material material, float hardness, float resistance, BlockSoundGroup sounds) {
            return FabricBlockSettings.of(material)
                    .strength(hardness, resistance)
                    .sounds(sounds)
                    .requiresTool();
        }

        /**
         * Puts an ore, a storage block, an ore storage block, and an anvil in the blockset.
         *
         * @param strength    Sets the strength of the blocks in the set.
         * @param miningLevel Mining level of the blocks. The ore sets the raw value,
         *                    while every other block recieves + 1 to their level.
         * @see #strength(float)    Strength
         */
        public Builder createDefaultSet(float strength, Identifier miningLevel, Identifier higherMiningLevel) {
            return strength(strength)
                    .createOre(miningLevel)
                    .strength(strength + 1.0F)
                    .createOreStorageBlock(miningLevel)
                    .createStorageBlock(higherMiningLevel)
                    .createAnvil(miningLevel);
        }

        /**
         * Puts an ore, a storage block, and an ore storage block in the blockset.
         *
         * @param strength           Sets the strength of the blocks in the set.
         * @param miningLevel        The mining level of the ore block
         * @param storageMiningLevel The mining level of both storage blocks
         * @see #strength(float)
         */
        public Builder createBlockSet(float strength, Identifier miningLevel, Identifier storageMiningLevel) {
            return strength(strength)
                    .createOre(miningLevel)
                    .strength(strength + 1.0F)
                    .createStorageBlock(storageMiningLevel)
                    .createOreStorageBlock(storageMiningLevel);
        }

        /**
         * Puts an ore, a storage block and an ore in the blockset, with slightly more configurable settings.
         *
         * @param oreStrength        The strength of the ore block.
         * @param oreMiningLevel     The mining level of the ore block.
         * @param storageStrength    The strength of the storage block and ore storage block.
         * @param storageMiningLevel The mining level of the storage block and ore storage block.
         * @see #strength(float)        oreStrength and storageStrength
         */
        public Builder createDefaultSet(float oreStrength, Identifier oreMiningLevel, float storageStrength, Identifier storageMiningLevel) {
            return strength(oreStrength)
                    .createOre(oreMiningLevel)
                    .strength(storageStrength)
                    .createStorageBlock(storageMiningLevel)
                    .createOreStorageBlock(storageMiningLevel);
        }

        /**
         * Puts a storage block and an anvil in the blockset.
         *
         * @param miningLevel The mining level of the anvil and the storage block
         * @see #strength(float)
         */
        public Builder createAnvilSet(float strength, Identifier miningLevel) {
            return strength(strength)
                    .sounds(BlockSoundGroup.METAL)
                    .createStorageBlock(miningLevel)
                    .createAnvil(miningLevel);
        }

        /**
         * Puts a storage block and an anvil in the blockset, where the storage block is configurable.
         *
         * @param hardness    The hardness of the storage block.
         * @param resistance  The blast resistance of the storage block.
         * @param miningLevel The mining level of the anvil and the storage block.
         * @see #createAnvil(Identifier)  createAnvil
         */
        public Builder createAnvilSet(float hardness, float resistance, Identifier miningLevel) {
            return strength(hardness, resistance)
                    .createStorageBlock(this.currentSounds, miningLevel)
                    .createAnvil(miningLevel);
        }

        /**
         * Applies sounds to the block(s) in the set.
         *
         * @param sounds The {@link BlockSoundGroup} which should be played.
         */
        public Builder sounds(BlockSoundGroup sounds) {
            this.currentSounds = sounds;
            return this;
        }

        /**
         * A simplified method to create a hardness and resistance value from a single int.
         *
         * @param strength The base int value for the blocks strength.
         * @return hardness, resistance (strength + 1)
         */
        public Builder strength(float strength) {
            return strength(strength, strength + 1);
        }

        /**
         * Gives the block(s) in the set the specified strength.
         *
         * @param hardness   Hardness of the block, determines breaking speed.
         * @param resistance Blast resistance of the block.
         */
        public Builder strength(float hardness, float resistance) {
            this.currentHardness = hardness;
            this.currentResistance = resistance;
            return this;
        }

        /**
         * Creates an ore block.
         *
         * @param miningLevel The mining level of the ore block.
         * @see Builder
         */
        public Builder createOre(Identifier miningLevel) {
            final var settings = blockSettings(Material.STONE, currentHardness, currentResistance, currentSounds);
            settingsProcessor.accept(settings);
            this.ore = new OreBlock(settings);
            miningLevels.put(ore, miningLevel);
            miningLevels.put(ore, PICKAXE);
            return this;
        }

        /**
         * Creates an ore block, which drops experience.
         *
         * @param miningLevel The mining level of the ore block.
         * @param experience  An {@link UniformIntProvider}, which holds the range of xp that can drop.
         * @see Builder
         */
        public Builder createOre(Identifier miningLevel, UniformIntProvider experience) {
            final var settings = blockSettings(Material.STONE, currentHardness, currentResistance, currentSounds);
            settingsProcessor.accept(settings);
            this.ore = new OreBlock(settings, experience);
            miningLevels.put(ore, miningLevel);
            miningLevels.put(ore, PICKAXE);
            return this;
        }

        /**
         * Creates an ore block, which drops experience.
         *
         * @param miningLevel The mining level of the ore block.
         * @param experience  An {@link UniformIntProvider}, which holds the range of xp that can drop.
         * @see Builder
         */
        public Builder createLuminantOre(Identifier miningLevel, UniformIntProvider experience, int luminance) {
            final var settings = blockSettings(Material.STONE, currentHardness, currentResistance, currentSounds).luminance(luminance);
            settingsProcessor.accept(settings);
            this.ore = new OreBlock(settings, experience);
            miningLevels.put(ore, miningLevel);
            miningLevels.put(ore, PICKAXE);
            Maldenhagen.injectCopium(this.ore);
            return this;
        }

        /**
         * Creates an ore variant.
         *
         * @param name        The name/key for the variant.
         * @param miningLevel The mining level of the ore variant.
         * @see Builder
         */
        public Builder createOreVariant(String name, Identifier miningLevel) {
            final var settings = blockSettings(Material.STONE, currentHardness, currentResistance, currentSounds);
            settingsProcessor.accept(settings);
            this.oreVariants.put(name, new OreBlock(settings));
            miningLevels.put(oreVariants.get(name), miningLevel);
            miningLevels.put(oreVariants.get(name), PICKAXE);
            return this;
        }

        /**
         * Creates an ore variant, which drops experience.
         *
         * @param name        The name/key for the variant.
         * @param miningLevel The mining level of the variant ore block.
         * @param experience  An {@link UniformIntProvider}, which holds the range of xp that can drop.
         */
        public Builder createOreVariant(String name, Identifier miningLevel, UniformIntProvider experience) {
            final var settings = blockSettings(Material.STONE, currentHardness, currentResistance, currentSounds);
            settingsProcessor.accept(settings);
            this.oreVariants.put(name, new OreBlock(settings, experience));
            miningLevels.put(oreVariants.get(name), miningLevel);
            miningLevels.put(oreVariants.get(name), PICKAXE);
            return this;
        }

        /**
         * Creates an ore variant, which drops experience.
         *
         * @param name        The name/key for the variant.
         * @param miningLevel The mining level of the variant ore block.
         * @param experience  An {@link UniformIntProvider}, which holds the range of xp that can drop.
         */
        public Builder createOreVariant(String name, Identifier miningLevel, UniformIntProvider experience, int luminance) {
            final var settings = blockSettings(Material.STONE, currentHardness, currentResistance, currentSounds).luminance(luminance);
            settingsProcessor.accept(settings);
            this.oreVariants.put(name, new OreBlock(settings, experience));
            miningLevels.put(oreVariants.get(name), miningLevel);
            miningLevels.put(oreVariants.get(name), PICKAXE);
            Maldenhagen.injectCopium(this.oreVariants.get(name));
            return this;
        }

        /**
         * A special ore creator for the creation of a {@link StarriteOreBlock}.
         *
         * @param miningLevel The mining level of the block.
         * @param experience  An {@link UniformIntProvider}, which holds the range of xp that can drop.
         */
        public Builder createStarriteOre(Identifier miningLevel, UniformIntProvider experience) {
            final var settings = blockSettings(Material.STONE, currentHardness, currentResistance, currentSounds);
            settingsProcessor.accept(settings);
            this.ore = new StarriteOreBlock(settings, experience);
            miningLevels.put(ore, miningLevel);
            miningLevels.put(ore, PICKAXE);
            return this;
        }

        /**
         * A special ore creator for the creation of a {@link BanglumOreBlock}.
         *
         * @param miningLevel The mining level of the block.
         */
        public Builder createBanglumOre(Identifier miningLevel) {
            final var settings = blockSettings(Material.STONE, currentHardness, currentResistance, currentSounds);
            settingsProcessor.accept(settings);
            this.ore = new BanglumOreBlock(settings);
            miningLevels.put(ore, miningLevel);
            miningLevels.put(ore, PICKAXE);
            return this;
        }

        /**
         * A special method for the creation of variants from {@link StarriteOreBlock}.
         *
         * @param name        The name/key for the variant.
         * @param miningLevel The mining level of the block.
         * @param experience  An {@link UniformIntProvider}, which holds the range of xp that can drop.
         */
        public Builder createStarriteOreVariant(String name, Identifier miningLevel, UniformIntProvider experience) {
            final var settings = blockSettings(Material.STONE, currentHardness, currentResistance, currentSounds);
            settingsProcessor.accept(settings);
            this.oreVariants.put(name, new StarriteOreBlock(settings, experience));
            miningLevels.put(oreVariants.get(name), miningLevel);
            miningLevels.put(oreVariants.get(name), PICKAXE);
            return this;
        }

        /**
         * A special method for the creation of variants from {@link BanglumOreBlock}.
         *
         * @param name        The name/key for the variant.
         * @param miningLevel The mining level of the block.
         */
        public Builder createBanglumOreVariant(String name, Identifier miningLevel) {
            final var settings = blockSettings(Material.STONE, currentHardness, currentResistance, currentSounds);
            settingsProcessor.accept(settings);
            this.oreVariants.put(name, new BanglumOreBlock(settings));
            miningLevels.put(oreVariants.get(name), miningLevel);
            miningLevels.put(oreVariants.get(name), PICKAXE);
            return this;
        }

        /**
         * A special method for the creation of storage blocks that copies Amethyst Blocks.
         *
         * @param miningLevel The mining level of the block.
         * @see Blocks#AMETHYST_BLOCK
         * @see AmethystBlock
         */
        public Builder createAmethystStorageBlock(Identifier miningLevel) {
            this.storageBlock = new AmethystBlock(blockSettings(Material.AMETHYST, currentHardness, currentResistance, BlockSoundGroup.AMETHYST_BLOCK));
            miningLevels.put(storageBlock, miningLevel);
            miningLevels.put(storageBlock, PICKAXE);
            return this;
        }

        /**
         * Create a storage block for metals.
         *
         * @param miningLevel The mining level of the storage block.
         */
        public Builder createStorageBlock(Identifier miningLevel) {
            return createStorageBlock(BlockSoundGroup.METAL, miningLevel);
        }

        /**
         * Create a storage block, with a specific material in mind.
         *
         * @param material    Vanilla {@link Material}, determines piston behaviour.
         * @param miningLevel The mining level of the storage block.
         */
        public Builder createStorageBlock(Material material, Identifier miningLevel) {
            final var settings = blockSettings(Material.METAL, currentHardness, currentResistance, currentSounds);
            settingsProcessor.accept(settings);
            this.storageBlock = new Block(settings);
            miningLevels.put(storageBlock, miningLevel);
            miningLevels.put(storageBlock, PICKAXE);
            return this;
        }

        /**
         * Create a storage block, with a specific sound in mind.
         *
         * @param sounds      A {@link BlockSoundGroup}, which determines block sounds.
         * @param miningLevel The mining level of the storage block.
         */
        public Builder createStorageBlock(BlockSoundGroup sounds, Identifier miningLevel) {
            final var settings = blockSettings(Material.METAL, currentHardness, currentResistance, sounds);
            settingsProcessor.accept(settings);
            this.storageBlock = new Block(settings);
            miningLevels.put(storageBlock, miningLevel);
            miningLevels.put(storageBlock, PICKAXE);
            return this;
        }

        /**
         * Create a falling storage block, which is exclusively used for gravel-like storage blocks.
         *
         * @param material    Vanilla {@link Material}, determines piston behaviour.
         * @param miningLevel The mining level of the storage block.
         */
        public Builder createFallingStorageBlock(Material material, Identifier miningLevel) {
            this.storageBlock = new FallingBlock(FabricBlockSettings.of(material).strength(currentHardness, currentResistance).sounds(currentSounds));
            miningLevels.put(storageBlock, miningLevel);
            miningLevels.put(storageBlock, SHOVEL);
            return this;
        }

        /**
         * Create a raw ore storage block.
         *
         * @param miningLevel The mining level of the raw storage block.
         */
        public Builder createOreStorageBlock(Identifier miningLevel) {
            final var settings = blockSettings(Material.STONE, currentHardness, currentResistance, currentSounds);
            settingsProcessor.accept(settings);
            this.oreStorageBlock = new Block(settings);
            miningLevels.put(oreStorageBlock, miningLevel);
            miningLevels.put(oreStorageBlock, PICKAXE);
            return this;
        }

        /**
         * Creates an anvil for a blockset.
         * Only requires a mining level, since hardness and resistance match vanilla values.
         *
         * @param miningLevel Mining level of the anvil.
         */
        public Builder createAnvil(Identifier miningLevel) {
            if (MythicMetals.CONFIG.enableAnvils) {
                final var settings = blockSettings(Material.REPAIR_STATION, 5.0f, 15000f, BlockSoundGroup.ANVIL);
                settingsProcessor.accept(settings);
                this.anvil = new AnvilBlock(settings);
                anvilMap.put(anvil, miningLevel);
                anvilMap.put(anvil, PICKAXE);
            }
            return this;
        }

        /**
         * Finishes the creation of the block set, and returns the entire set using the settings declared.
         * For registering the blocks call {@link Builder#register() Builder.register} during mod initialization.
         *
         * @return BlockSet
         */
        public BlockSet finish() {
            final var set = new BlockSet(this.name, this.ore,
                    this.storageBlock, this.oreStorageBlock, this.anvil,
                    this.oreVariants, this.fireproof, this.miningLevels, this.anvilMap);
            Builder.toBeRegistered.add(set);
            return set;
        }

    }
}
