package nourl.mythicmetals.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import nourl.mythicmetals.MythicMetals;

import java.util.ArrayList;

@Config(name = MythicMetals.MOD_ID + "_config")
public class MythicConfig implements ConfigData {
    @ConfigEntry.Gui.RequiresRestart
    public boolean enableDusts = false;
    @ConfigEntry.Gui.RequiresRestart
    public boolean enableNuggets = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean enableAnvils = true;

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig adamantite = new OreConfig(true, 5, 1, -54, -20, .125f, false, true);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig aquarium = new OreConfig(true, 9, 4, 41, 63, .0f, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public VariantConfig banglum = new VariantConfig(true, 6, 8, 3, 4, 50, 69, 69, 110, .125f, false, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig carmot = new OreConfig(true,4, 1, -24 ,10, .25f, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
     public VariantConfig kyber = new VariantConfig(true, 3, 15, 1, 40, 12, -62, 52, 40,.5f, false, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig manganese = new OreConfig(true, 9, 2, 28, 48,.25f, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig morkite = new OreConfig(true,11, 3, 27, 32, .25f, true);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig midas_gold = new OreConfig(true,7, 4, 12, 125,.25f, false, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig mythril = new OreConfig(true, 5, 1, -24, 4, .125f, false, true);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig orichalcum = new OreConfig(true, 5, 1, 8, 8, .0f, true);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig osmium = new OreConfig(true, 6, 5, 50, 120, .25f, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig overworld_nether_ores = new OreConfig(true, 4, 2, 40, 70, .125f, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig palladium = new OreConfig(true, 5, 2, 14, 36, .0f, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig platinum = new OreConfig(true, 6, 2, 8, 32, .0f, false, true);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig prometheum = new OreConfig(true, 6, 4, 30, 48, .0f, true);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig quadrillum = new OreConfig(true,7, 2, 12, 40, .25f, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public VariantConfig runite = new VariantConfig(true, 3, 4, 1, 2, 39, -55, 53, -37, .5f, false, true);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig silver = new OreConfig(true,8, 4, 25, 42, .25f, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig starrite = new OreConfig(true, 4,  1,  70,  260,  .125f, false, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig end_starrite = new OreConfig(true, 7, 2, 30, 90, .0f, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig stormyx = new OreConfig(true,8, 2, 4, 60, .0f, false);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig unobtainium = new OreConfig(true,3, 1, -54, 5, .0f, false, true);
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public OreConfig tin = new OreConfig(true,8, 4, 64, 92, .25f, false);
    @ConfigEntry.Gui.Excluded
    public int configVersion = MythicMetals.CONFIG_VERSION;
    @ConfigEntry.Gui.RequiresRestart
    public boolean disableFunny = false;
    public int banglumNukeCoreRadius = 24;

    public ArrayList<String> blacklist = new ArrayList<>();
}
