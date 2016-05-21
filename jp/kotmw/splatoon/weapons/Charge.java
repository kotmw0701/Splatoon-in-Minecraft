/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.weapons;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Charge extends BukkitRunnable
{
	static Map<String, Boolean> charge = new HashMap<>();
	static Player player;

	public Charge(Player player)
	{
		Charge.player = player;
	}

	public void run()
	{

	}
}
