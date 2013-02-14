package de.dustplanet.itemsonanything;

import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Ladder;
import org.bukkit.material.Lever;

/**
 * BlocksOnGlass for CraftBukkit/Bukkit
 * Handles the permission check for each block/item
 * 
 * Refer to the forum thread:
 * http://bit.ly/bogbukkit
 * Refer to the dev.bukkit.org page:
 * http://bit.ly/bogbukkitdev
 *
 * @author xGhOsTkiLLeRx
 * @thanks to FrozenBrain for the original BlocksOnGlass plugin!
 * @thanks to mooman219 for help with bug fixing and NMS code!
 * 
 */

public class ItemsOnAnythingPlayerListener implements Listener {
	private ItemsOnAnything plugin;
	
	public ItemsOnAnythingPlayerListener(ItemsOnAnything instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if ((event.getAction() == Action.RIGHT_CLICK_AIR) && event.hasItem()) {
			Player player = event.getPlayer();
			List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 5);
			System.out.println(lastTwoTargetBlocks.toString());
			Block previousBlock = lastTwoTargetBlocks.get(0);
			Block targetBlock = lastTwoTargetBlocks.get(1);
			BlockFace face = targetBlock.getFace(previousBlock);
			if (plugin.enabledBlocks.contains(targetBlock.getType().name()) && previousBlock.getType() == Material.AIR) {
				ItemStack item = event.getItem();
				Material type = item.getType();
				if (plugin.items.contains(type) && face == BlockFace.UP) {
					previousBlock.setType(type);
					if (type == Material.LEVER) {
						Lever lever = new Lever();
						System.out.println(face.getOppositeFace().name());
						lever.setFacingDirection(face);
						previousBlock.setType(lever.getItemType());
						previousBlock.setData(lever.getData());
					}
				}
				//reduceItemAmountIfNotCreativeMode(player, item);
				else if (type == Material.LADDER && face != BlockFace.DOWN && face != BlockFace.UP) {
					Ladder ladder = new Ladder();
					ladder.setFacingDirection(face.getOppositeFace());
					previousBlock.setType(ladder.getItemType());
					previousBlock.setData(ladder.getData());
					// reduceItemAmountIfNotCreativeMode(player, item);
					// etc
				}
			}
		}
	}

	private void decreaseItem(Player player, ItemStack item) {
		if (player.getGameMode() != GameMode.SURVIVAL) return;
		if (item.getAmount() == 0) player.setItemInHand(null);
		else {
			item.setAmount((item.getAmount() - 1));
			player.setItemInHand(item);
		}
	}

	// Check to see if the player has got the permission
	private void cancel (Player player, PlayerInteractEvent event, String block, String item) {
		if (plugin.config.getBoolean("permissions")) {
			if (!player.hasPermission("itemsonanything.block." + block) || !player.hasPermission("itemsonanything.item." + item)) {
				event.setCancelled(true);
			}
		}
	}
}