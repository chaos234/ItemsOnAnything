package de.dustplanet.itemsonanything;

import org.bukkit.block.BlockFace;

public class Util {
    public static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

    /**
     * Gets the horizontal Block Face from a given yaw angle
     *
     * @param yaw angle
     * @param useSubCardinalDirections setting, True to allow NORTH_WEST to be returned
     * @return The Block Face of the angle
     */
    // Thanks to https://forums.bukkit.org/threads/get-the-direction-a-player-is-facing.105314/#post-1372240
    public static BlockFace yawToFace(float yaw) {
	return axis[Math.round(yaw / 90f) & 0x3];
    }
}
