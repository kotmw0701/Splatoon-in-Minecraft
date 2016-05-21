/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.arena;

import org.bukkit.ChatColor;

public enum ArenaStatusEnum
{
	ENABLED(ChatColor.GREEN + "ENABLED"),
	DISABLED(ChatColor.DARK_RED + "DISABLED"),
	INGAME(ChatColor.RED + "INGAME"),
	RESULT(ChatColor.LIGHT_PURPLE + "RESULT NOW");

	private final String stats;

	private ArenaStatusEnum(final String stats)
	{
		this.stats = stats;
	}

	public String getStats()
	{
		return stats;
	}
}
