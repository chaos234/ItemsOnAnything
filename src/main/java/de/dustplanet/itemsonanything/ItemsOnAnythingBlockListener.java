package de.dustplanet.itemsonanything;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

/**
 * ItemsOnAnything for CraftBukkit/Bukkit
 * Handles the physics event which needs to be blocked
 * 
 * @author xGhOsTkiLLeRx
 */

public class ItemsOnAnythingBlockListener implements Listener {
    private ItemsOnAnything plugin;

    public ItemsOnAnythingBlockListener(ItemsOnAnything instance) {
	plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPhysics(BlockPhysicsEvent event) {
	// Get our blocks and items
	String blockName = event.getBlock().getType().name();
	String changedTypeName = event.getChangedType().name();
	System.out.println(blockName +  " " + changedTypeName);
	// Our "base" block is enabled
	if (plugin.enabledBlocks.contains(blockName) || plugin.items.contains(blockName)) {
	    // Is our item on the list?
	    if (plugin.items.contains(changedTypeName) || event.getChangedType() == Material.AIR) {
		System.out.println("cancel");
		event.setCancelled(true);
	    }
	}
    }
}