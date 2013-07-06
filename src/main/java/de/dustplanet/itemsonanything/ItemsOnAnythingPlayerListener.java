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

/**
 * ItemsOnAnything for CraftBukkit/Bukkit
 * Handles the permission check for each block/item
 * 
 * Refer to the forum thread:
 * http://bit.ly/bogbukkit
 * 
 * Refer to the dev.bukkit.org page:
 * http://bit.ly/bogbukkitdev
 * 
 * @author xGhOsTkiLLeRx 
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
	    Block previousBlock = lastTwoTargetBlocks.get(0);
	    Block targetBlock = lastTwoTargetBlocks.get(1);
	    System.out.println("Block 1: " + previousBlock.getType().name());
	    System.out.println("Block 2: " + targetBlock.getType().name());
	    BlockFace face = targetBlock.getFace(previousBlock);
	    if (plugin.enabledBlocks.contains(targetBlock.getType().name()) && previousBlock.getType() == Material.AIR) {
		ItemStack item = event.getItem();
		Material type = item.getType();
		if (plugin.items.contains(type.name())) {
		    previousBlock.setType(type);
		    // Special case is a lever, we have to adjust the bit values (facing)
		    // Reference: http://www.minecraftwiki.net/wiki/Data_values#Levers
		    if (type == Material.LEVER) {
			BlockFace playerDirection = Util.yawToFace(player.getLocation().getYaw());
			if (playerDirection == BlockFace.SOUTH || playerDirection == BlockFace.NORTH) {
			    if (face == BlockFace.UP) {
				previousBlock.setData((byte) 0x5);
			    } else if (face == BlockFace.DOWN) {
				previousBlock.setData((byte) 0x7);
			    }
			} else if (playerDirection == BlockFace.EAST  || playerDirection == BlockFace.WEST) {
			    if (face == BlockFace.UP) {
				previousBlock.setData((byte) 0x6);
			    } else if (face == BlockFace.DOWN) {
				previousBlock.setData((byte) 0x0);
			    }
			}
		    } else if (type == Material.LADDER) {
			// Special case is a ladder, we have to adjust the bit values (facing)
			// Reference: http://www.minecraftwiki.net/wiki/Data_values#Ladders.2C_Wall_Signs.2C_Furnaces.2C_and_Chests
			// Ladders can't be placed if the face is UP or DOWN
			if (face == BlockFace.UP || face == BlockFace.DOWN) {
			    previousBlock.setType(Material.AIR);
			    return;
			}
			BlockFace playerDirection = Util.yawToFace(player.getLocation().getYaw());
			switch (playerDirection) {
			case EAST:
			    previousBlock.setData((byte) 0x5);
			    break;
			case NORTH:
			    previousBlock.setData((byte) 0x2);
			    break;
			case SOUTH:
			    previousBlock.setData((byte) 0x3);
			    break;
			case WEST:
			    previousBlock.setData((byte) 0x4);
			    break;
			default:
			    break;
			    
			}
		    }
		    
		}
		decreaseItem(player, item);
	    }
	}
    }

    private void decreaseItem(Player player, ItemStack item) {
	if (player.getGameMode() != GameMode.SURVIVAL) {
	    return;
	}
	if (item.getAmount() == 0) {
	    player.setItemInHand(null);
	} else {
	    item.setAmount((item.getAmount() - 1));
	    player.setItemInHand(item);
	}
    }

    // Check to see if the player has got the permission
    private void cancel(Player player, PlayerInteractEvent event, String block, String item) {
	if (plugin.config.getBoolean("permissions")) {
	    if (!player.hasPermission("itemsonanything.block." + block) || !player.hasPermission("itemsonanything.item." + item)) {
		event.setCancelled(true);
	    }
	}
    }
}