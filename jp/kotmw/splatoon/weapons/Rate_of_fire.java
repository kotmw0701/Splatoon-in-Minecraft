/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.weapons;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Rate_of_fire extends BukkitRunnable
{
	private static Player player;

	public Rate_of_fire(Player player)
	{
		Rate_of_fire.player = player;
	}

	public void run()
	{
		float ink = player.getExp();
		Vector vec = player.getLocation().getDirection();
		Snowball ball = player.launchProjectile(Snowball.class);
		ball.setVelocity(new Vector(vec.getX()*0.7,vec.getY()*0.7,vec.getZ()*0.7));
		ball.setShooter(player);
		player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
		player.setExp(ink - 0.02f);
		this.cancel();
	}
}
