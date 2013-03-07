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

/**
 * ItemsOnAnything for CraftBukkit/Bukkit
 * Handles some general stuff.
 * 
 * @author xGhOsTkiLLeRx
 */

public class ItemsOnAnything extends JavaPlugin {
    private final ItemsOnAnythingPlayerListener playerListener = new ItemsOnAnythingPlayerListener(this);
    private final ItemsOnAnythingBlockListener blockListener = new ItemsOnAnythingBlockListener(this);
    public List<org.bukkit.Material> items = new ArrayList<org.bukkit.Material>();
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
	    for (int i = 0; i < stats.size(); i++) {
		final String name = stats.get(i);
		metrics.addCustomData(new Metrics.Plotter() {
		    @Override
		    public String getColumnName() {
			return name;
		    }

		    @Override
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
	items.add(org.bukkit.Material.POWERED_RAIL);
	items.add(org.bukkit.Material.DETECTOR_RAIL);
	items.add(org.bukkit.Material.TORCH);
	items.add(org.bukkit.Material.LADDER);
	items.add(org.bukkit.Material.RAILS);
	items.add(org.bukkit.Material.LEVER);
	items.add(org.bukkit.Material.STONE_PLATE);
	items.add(org.bukkit.Material.WOOD_PLATE);
	items.add(org.bukkit.Material.REDSTONE_TORCH_ON);
	items.add(org.bukkit.Material.REDSTONE_TORCH_OFF);
	items.add(org.bukkit.Material.REDSTONE_WIRE);
	items.add(org.bukkit.Material.STONE_BUTTON);
	items.add(org.bukkit.Material.WOODEN_DOOR);
	items.add(org.bukkit.Material.WOOD_DOOR);
	items.add(org.bukkit.Material.IRON_DOOR_BLOCK);
	items.add(org.bukkit.Material.IRON_DOOR);
	items.add(org.bukkit.Material.REDSTONE);
	items.add(org.bukkit.Material.BED);
	items.add(org.bukkit.Material.DIODE);
	items.add(org.bukkit.Material.DIODE_BLOCK_OFF);
	items.add(org.bukkit.Material.DIODE_BLOCK_ON);
	items.add(org.bukkit.Material.TRAP_DOOR);
	items.add(org.bukkit.Material.SNOW);
	items.add(org.bukkit.Material.THIN_GLASS);
	items.add(org.bukkit.Material.IRON_FENCE);
	items.add(org.bukkit.Material.LADDER);
	if (config.getBoolean("botanical")) {
	    items.add(org.bukkit.Material.RED_MUSHROOM);
	    items.add(org.bukkit.Material.BROWN_MUSHROOM);
	    items.add(org.bukkit.Material.YELLOW_FLOWER);
	    items.add(org.bukkit.Material.RED_ROSE);
	    items.add(org.bukkit.Material.CACTUS);
	    items.add(org.bukkit.Material.WATER_LILY);
	    items.add(org.bukkit.Material.DEAD_BUSH);
	}
    }

    public void loadConfig() {
	config.options().header("For help and support visit please: http://bit.ly/bogbukkitdev or http://bit.ly/bogbukkit");
	config.options().copyDefaults(true);
	saveConfig();
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