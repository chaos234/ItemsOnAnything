package de.dustplanet.itemsonanything;

import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
	if (Util.checkEvent(event)) {
	    Player player = event.getPlayer();
	    List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 5);
	    final Block previousBlock = lastTwoTargetBlocks.get(0);
	    Block targetBlock = lastTwoTargetBlocks.get(1);
	    System.out.println("Block 1: " + previousBlock.getType().name());
	    System.out.println("Block 2: " + targetBlock.getType().name());
	    BlockFace face = targetBlock.getFace(previousBlock);
	    if (plugin.enabledBlocks.contains(targetBlock.getType().name()) && previousBlock.getType() == Material.AIR) {
		ItemStack item = event.getItem();
		Material type = item.getType();
		if (plugin.items.contains(type.name())) {
		    // We need to edit some items to a block
		    type = Util.translateItemToBlock(type);
		    previousBlock.setType(type);
		    
		    // Check for manual fixes
		    if (type == Material.LEVER) {
			// Special case is a lever, we have to adjust the bit values (facing)
			// Reference: http://www.minecraftwiki.net/wiki/Data_values#Levers
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
		    } else if (type == Material.STONE_BUTTON || type == Material.WOOD_BUTTON) {
			// Special case is a button, we have to adjust the bit values (facing)
			// Reference: http://www.minecraftwiki.net/wiki/Data_values#Buttons
			// Buttons can't be placed if the face is UP or DOWN
			if (face == BlockFace.UP || face == BlockFace.DOWN) {
			    previousBlock.setType(Material.AIR);
			    return;
			}
			BlockFace playerDirection = Util.yawToFace(player.getLocation().getYaw());
			switch (playerDirection) {
			case EAST:
			    previousBlock.setData((byte) 0x1);
			    break;
			case NORTH:
			    previousBlock.setData((byte) 0x4);
			    break;
			case SOUTH:
			    previousBlock.setData((byte) 0x3);
			    break;
			case WEST:
			    previousBlock.setData((byte) 0x2);
			    break;
			default:
			    break;
			}
		    } else if (type == Material.SAPLING || type == Material.LONG_GRASS) {
			// We have to respect the different types of saplings/long grass
			previousBlock.setData((byte) item.getDurability());
		    } else if (type == Material.REDSTONE_COMPARATOR_OFF || type == Material.REDSTONE_COMPARATOR_ON
			    || type == Material.DIODE_BLOCK_OFF || type == Material.DIODE_BLOCK_ON) {
			// Special case is a repeater or comparator, we have to adjust the bit values (facing)
			// Reference: http://www.minecraftwiki.net/wiki/Data_values#Redstone_Repeater
			BlockFace playerDirection = Util.yawToFace(player.getLocation().getYaw());
			switch (playerDirection) {
			case EAST:
			    previousBlock.setData((byte) 0x3);
			    break;
			case NORTH:
			    previousBlock.setData((byte) 0x2);
			    break;
			case SOUTH:
			    previousBlock.setData((byte) 0x0);
			    break;
			case WEST:
			    previousBlock.setData((byte) 0x1);
			    break;
			default:
			    break;
			}
		    } else if (type == Material.TRIPWIRE_HOOK) {
			// Special case is tripwire hook, we have to adjust the bit values (facing)
			// Reference: http://www.minecraftwiki.net/wiki/Data_values#Tripwire_Hook
			// Tripwire hook can't be placed if the face is UP or DOWN
			if (face == BlockFace.UP || face == BlockFace.DOWN) {
			    previousBlock.setType(Material.AIR);
			    return;
			}
			BlockFace playerDirection = Util.yawToFace(player.getLocation().getYaw());
			switch (playerDirection) {
			case EAST:
			    previousBlock.setData((byte) 0x3);
			    break;
			case NORTH:
			    previousBlock.setData((byte) 0x2);
			    break;
			case SOUTH:
			    previousBlock.setData((byte) 0x0);
			    break;
			case WEST:
			    previousBlock.setData((byte) 0x1);
			    break;
			default:
			    break;
			}
		    } else if (type == Material.TRAP_DOOR) {
			// Special case is a trap door, we have to adjust the bit values (facing)
			// Reference: http://www.minecraftwiki.net/wiki/Data_values#Trapdoors
			// We need to check for upper or lower half of the block and maybe add the "up" bit 0x8
			BlockFace playerDirection = Util.yawToFace(player.getLocation().getYaw());
			byte data = (byte) (previousBlock.getData() & 0x7);
			switch (playerDirection) {
			case EAST:
			    data = 0x3;
			    break;
			case NORTH:
			    data = 0x0;
			    break;
			case SOUTH:
			    data = 0x1;
			    break;
			case WEST:
			    data = 0x2;
			    break;
			default:
			    break;
			}
			// Player looks up
			if (player.getLocation().getDirection().getY() > 0) {
			    data |= 0x8;
			}
			previousBlock.setData(data);
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