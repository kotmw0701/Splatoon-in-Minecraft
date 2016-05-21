/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.battle;

import org.bukkit.ChatColor;

public enum BattleTypeEnum
{
	Turf_War(ChatColor.GREEN.toString() + ChatColor.BOLD + "Turf War"),
	Splat_Zones(ChatColor.RED.toString() + ChatColor.BOLD + "Splat Zones"),
	Rain_Maker(ChatColor.BLUE.toString() + ChatColor.BOLD + "Rain Maker");

	private final String type;

	private BattleTypeEnum(final String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}
}
