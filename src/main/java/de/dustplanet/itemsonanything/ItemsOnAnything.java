package de.dustplanet.itemsonanything;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.*;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;
import org.mcstats.Metrics.Plotter;

/**
 * ItemsOnAnything for CraftBukkit/Bukkit
 * Handles some general stuff.
 * 
 * @author xGhOsTkiLLeRx
 */

public class ItemsOnAnything extends JavaPlugin {
    private final ItemsOnAnythingPlayerListener playerListener = new ItemsOnAnythingPlayerListener(this);
    private final ItemsOnAnythingBlockListener blockListener = new ItemsOnAnythingBlockListener(this);
    public List<String> items = new ArrayList<String>();
    private List<String> stats = new ArrayList<String>();
    public List<String> enabledBlocks = new ArrayList<String>();
    public FileConfiguration config;
    private File configFile;

    // Shutdown
    public void onDisable() {
	stats.clear();
	items.clear();
    }

    // Start
    public void onEnable() {
	// Events
	PluginManager pm = getServer().getPluginManager();
	pm.registerEvents(blockListener, this);
	pm.registerEvents(playerListener, this);

	// Config
	configFile = new File(getDataFolder(), "config.yml");
	if (!configFile.exists()) {
	    if (configFile.getParentFile().mkdirs()) {
		copy(getResource("config.yml"), configFile);
	    } else {
		getLogger().severe("The config folder could NOT be created, make sure it's writable!");
		getLogger().severe("Disabling now!");
		setEnabled(false);
		return;
	    }
	}
	
	config = getConfig();
	loadConfig();

	// Blocks modification
	setupBlockLists();

	// Stats
	try {
	    Metrics metrics = new Metrics(this);
	    // Custom plotter for each item
	    Graph graph = metrics.createGraph("Enabled items");
	    for (final String name: stats) {
		graph.addPlotter(new Plotter(name) {
		    public int getValue() {
			return 1;
		    }
		});
	    }
	    metrics.start();
	} catch (IOException e) {
	    getLogger().info("Couldn't start Metrics, please report this!");
	    e.printStackTrace();
	}
    }

    private void setupBlockLists() {
	items.add("POWERED_RAIL");
	items.add("DETECTOR_RAIL");
	items.add("TORCH");
	items.add("LADDER");
	items.add("RAILS");
	items.add("LEVER");
	items.add("STONE_PLATE");
	items.add("WOOD_PLATE");
	items.add("REDSTONE_TORCH_ON");
	items.add("REDSTONE_TORCH_OFF");
	items.add("REDSTONE_WIRE");
	items.add("STONE_BUTTON");
	items.add("WOODEN_DOOR");
	items.add("WOOD_DOOR");
	items.add("IRON_DOOR_BLOCK");
	items.add("IRON_DOOR");
	items.add("REDSTONE");
	items.add("BED");
	items.add("DIODE");
	items.add("DIODE_BLOCK_OFF");
	items.add("DIODE_BLOCK_ON");
	items.add("TRAP_DOOR");
	items.add("SNOW");
	items.add("WOOD_BUTTON");
	items.add("FLOWER_POT");
	items.add("FLOWER_POT_ITEM");
	items.add("REDSTONE_COMPARATOR");
	items.add("REDSTONE_COMPARATOR_ON");
	items.add("REDSTONE_COMPARATOR_OFF");
	items.add("TRIPWIRE_HOOK");
	items.add("GOLD_PLATE");
	items.add("IRON_PLATE");
	if (config.getBoolean("botanical")) {
	    items.add("RED_MUSHROOM");
	    items.add("BROWN_MUSHROOM");
	    items.add("YELLOW_FLOWER");
	    items.add("RED_ROSE");
	    items.add("CACTUS");
	    items.add("WATER_LILY");
	    items.add("DEAD_BUSH");
	    items.add("LONG_GRASS");
	    items.add("SAPLING");
	    items.add("MELON_SEEDS");
	    items.add("MELON_STEM");
	    items.add("PUMPKIN_SEEDS");
	    items.add("PUMPKIN_STEM");
	    items.add("NETHER_WARTS");
	    items.add("NETHER_STALK");
	    items.add("SUGAR_CANE");
	    items.add("SUGAR_CANE_BLOCK");
	}
    }

    public void loadConfig() {
	config.options().header("For help and support visit please: http://bit.ly/bogbukkitdev or http://bit.ly/bogbukkit");
	config.options().copyDefaults(true);
	saveConfig();
	enabledBlocks = config.getStringList("enabledBlocks");
    }

    // If no config is found, copy the default one(s)!
    private void copy(InputStream in, File file) {
	OutputStream out = null;
	try {
	    out = new FileOutputStream(file);
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		out.write(buf, 0, len);
	    }
	} catch (IOException e) {
	    getLogger().warning("Failed to copy the default config! (I/O)");
	    e.printStackTrace();
	} finally {
	    try {
		if (out != null) {
		    out.close();
		}
	    } catch (IOException e) {
		getLogger().warning("Failed to close the streams! (I/O -> Output)");
		e.printStackTrace();
	    }
	    try {
		if (in != null) {
		    in.close();
		}
	    } catch (IOException e) {
		getLogger().warning("Failed to close the streams! (I/O -> Input)");
		e.printStackTrace();
	    }
	}
    }
}