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
 * 
 */

public class ItemsOnAnythingBlockListener implements Listener {
	private ItemsOnAnything plugin;
	
	public ItemsOnAnythingBlockListener(ItemsOnAnything instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPhysics(BlockPhysicsEvent event) {
		System.out.println("start");
		System.out.println(event.getBlock().getType().name());
		System.out.println(event.getChangedType().name());
		System.out.println("end");
		System.out.println(" ");
		if (plugin.enabledBlocks.contains(event.getBlock().getType().name()) || plugin.items.contains(event.getBlock().getType())) {
			System.out.println("here");
			if (plugin.items.contains(event.getChangedType()) || event.getChangedType() == Material.AIR) {
				System.out.println("her2");
				event.setCancelled(true);
			}
		}
		//		System.out.println();
	}
}