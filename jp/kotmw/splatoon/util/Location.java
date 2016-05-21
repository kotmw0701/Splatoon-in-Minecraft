package jp.kotmw.splatoon.util;

import org.bukkit.Bukkit;

public class Location
{
	String world;
	double x,y,z;
	float yaw,pitch;

	public Location(String world, double x, double y, double z, float yaw, float pitch)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public org.bukkit.Location getLocation()
	{
		return new org.bukkit.Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
	}

	/*
	 * ワールド読み込み前にWorld型が読み込まれるとnullになってしまう
	 * そのために一時的数値&文字列での格納
	 *
	 *
	 *
	 *
	 *
	 *
	 *
	 * /
	 */
}
