package jp.kotmw.splatoon.weapons;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class Bullet
{
	public static boolean setBullet(Player player) {
		return setBullet(player
				, player.getLocation().getDirection().getX()
				, player.getLocation().getDirection().getY()
				, player.getLocation().getDirection().getZ()
				, 0.1f);
	}

	public static boolean setBullet(Player player, float speed) {
		return setBullet(player
				, player.getLocation().getDirection().getX()*speed
				, player.getLocation().getDirection().getY()*speed
				, player.getLocation().getDirection().getZ()*speed
				, 0.1f);
	}

	public static boolean setBullet(Player player, double y, float speed, float spread) {
		return setBullet(player
				, player.getLocation().getDirection().getX()*speed
				, y
				, player.getLocation().getDirection().getZ()*speed
				, spread);
	}

	public static boolean setBullet(Player player, double x, double y, double z, float spread)
	{
		Vector vec = new Vector(x*Math.cos(30)
				, y*Math.sin(30)
				, z*Math.tan(30));
		Snowball ball = player.launchProjectile(Snowball.class, vec);
		ball.setShooter(player);
		return true;
	}
}
