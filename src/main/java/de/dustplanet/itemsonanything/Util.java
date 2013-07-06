package de.dustplanet.itemsonanything;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Util {
    public static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

    /**
     * Gets the horizontal Block Face from a given yaw angle
     * 
     * @param yaw angle
     * @return The Block Face of the angle
     */
    // Thanks to https://forums.bukkit.org/threads/105314/#post-1372240
    public static BlockFace yawToFace(float yaw) {
	return axis[Math.round(yaw / 90f) & 0x3];
    }

    public static boolean checkEvent(PlayerInteractEvent event) {
	if (event.getAction() == Action.RIGHT_CLICK_AIR && event.hasItem()) {
	    return true;
	}
	if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasBlock() && event.hasItem()) {
	    Material type = event.getItem().getType();
	    if (type == Material.REDSTONE
		    || type == Material.BED
		    || type == Material.REDSTONE_COMPARATOR
		    || type == Material.IRON_DOOR
		    || type == Material.WOOD_DOOR
		    || type == Material.DIODE
		    || type == Material.FLOWER_POT_ITEM
		    || type == Material.SEEDS
		    || type == Material.MELON_SEEDS
		    || type == Material.NETHER_STALK
		    || type == Material.PUMPKIN_SEEDS
		    || type == Material.SUGAR_CANE) {
		return true;
	    }
	}
	return false;
    }

    public static Material translateItemToBlock(Material type) {
	switch (type) {
	case DIODE:
	    type = Material.DIODE_BLOCK_OFF;
	    break;
	case REDSTONE:
	    type = Material.REDSTONE_WIRE;
	    break;
	case BED:
	    type = Material.BED_BLOCK;
	    break;
	case REDSTONE_COMPARATOR:
	    type = Material.REDSTONE_COMPARATOR_OFF;
	    break;
	case IRON_DOOR:
	    type = Material.IRON_DOOR_BLOCK;
	    break;
	case WOOD_DOOR:
	    type = Material.WOODEN_DOOR;
	    break;
	case FLOWER_POT_ITEM:
	    type = Material.FLOWER_POT;
	    break;
	case SEEDS:
	    type = Material.CROPS;
	    break;
	case NETHER_STALK:
	    type = Material.NETHER_WARTS;
	    break;
	case MELON_SEEDS:
	    type = Material.MELON_STEM;
	    break;
	case PUMPKIN_SEEDS:
	    type = Material.PUMPKIN_STEM;
	    break;
	case SUGAR_CANE:
	    type = Material.SUGAR_CANE_BLOCK;
	    break;
	default:
	    break;
	}
	return type;
    }

    public static boolean noRedstoneConflict(Material blockType, Material changedType) {
	// Case 1 our first block is redstone
	if (blockType == Material.REDSTONE_WIRE) {
	    if (changedType == Material.STONE_BUTTON) {
		return false;
	    }
	}
	if (changedType == Material.REDSTONE_WIRE) {
	    if (blockType == Material.STONE_BUTTON) {
		return false;
	    }
	}
	return true;
    }
}
