/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TestRunnable extends BukkitRunnable
{
	int second = 0;
	int mode = 0;
	int x1,x2,y1,z1,z2;
	String colormode = null;
	Player player;

	public TestRunnable(int mode, int second, int param1, int param2, int param3, int param4, int param5, String param6)
	{
		this.mode = mode;
		this.second = second;
		this.x1 = param1;
		this.x2 = param2;
		this.y1 = param3;
		this.z1 = param4;
		this.z2 = param5;
		this.colormode = param6;
	}

	public TestRunnable(Player player, int second)
	{
		this.mode = 10;
		this.second = second;
		this.player = player;
	}

	@Override
	public void run()
	{
		if(mode == 0)
		{
			if(second > 0)
			{
				for (int yPoint = (y1 -1); yPoint <= (y1 + 20); yPoint++)
				{
					for (int xPoint = x2; xPoint <= x1; xPoint++)
					{
						for (int zPoint = z2; zPoint <= z1; zPoint++)
						{
							if((xPoint == x1 || xPoint == x2) || (zPoint == z1 || zPoint == z2))
							{
								PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
										EnumParticle.REDSTONE
										, true
										, (float)(xPoint + 0.5)
										, (float)(yPoint + 0.5)
										, (float)(zPoint + 0.5)
										, (Color.FUCHSIA.getRed() / 255)
										, (Color.FUCHSIA.getGreen() / 255)
										, (Color.FUCHSIA.getBlue() / 255)
										, 10
										, 0
										, 0);
								for(Player online : Bukkit.getOnlinePlayers())
									SplatTitle.sendPlayer(online, packet);
							}
						}
					}
				}
				second--;
			}
			else
			{
				this.cancel();
			}
		}
		else if(mode == 1)
		{
			if(second > 0)
			{
				for (int yPoint = (y1 -1); yPoint <= (y1 + 20); yPoint++)
				{
					for (int xPoint = x2; xPoint <= x1; xPoint++)
					{
						for (int zPoint = z2; zPoint <= z1; zPoint++)
						{
							if((xPoint == x1 || xPoint == x2) || (zPoint == z1 || zPoint == z2))
							{
								PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
										EnumParticle.FIREWORKS_SPARK
										, true
										, (float)(xPoint + 0.5)
										, (float)(yPoint + 0.5)
										, (float)(zPoint + 0.5)
										, 0
										, 0
										, 0
										, 0
										, 1
										, 0);
								for(Player online : Bukkit.getOnlinePlayers())
									SplatTitle.sendPlayer(online, packet);
							}
						}
					}
				}
				second--;
			}
			else
			{
				this.cancel();
			}
		}
		else if(mode == 2)
		{
			if(second > 0)
			{
				for (int yPoint = (y1 -1); yPoint <= (y1 + 20); yPoint++)
				{
					for (int xPoint = x2; xPoint <= x1; xPoint++)
					{
						for (int zPoint = z2; zPoint <= z1; zPoint++)
						{
							if((xPoint == x1 || xPoint == x2) || (zPoint == z1 || zPoint == z2))
							{
								PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
										EnumParticle.SPELL_MOB
										, true
										, (float)(xPoint + 0.5)
										, (float)(yPoint + 0.5)
										, (float)(zPoint + 0.5)
										, (Color.FUCHSIA.getRed() / 255)
										, (Color.FUCHSIA.getGreen() / 255)
										, (Color.FUCHSIA.getBlue() / 255)
										, 10
										, 0
										, 0);
								for(Player online : Bukkit.getOnlinePlayers())
									SplatTitle.sendPlayer(online, packet);
							}
						}
					}
				}
				second--;
			}
			else
			{
				this.cancel();
			}
		}
		else if(mode == 3)
		{
			if(second > 0)
			{
				Color color = Color.WHITE;
				DyeColor type = DyeColor.WHITE;
				for(DyeColor types : DyeColor.values())
				{
					if(types.toString().equalsIgnoreCase(colormode))
					{
						type = types;
						break;
					}
				}
				color = ColorSelect.teamSelect(type);
				int i = 0;
				if(type.equals(DyeColor.GREEN) || type.equals(DyeColor.BLUE))
					i = -1;
				for (int yPoint = (y1 -1); yPoint <= (y1 + 20); yPoint++)
				{
					for (int xPoint = x2; xPoint <= x1; xPoint++)
					{
						for (int zPoint = z2; zPoint <= z1; zPoint++)
						{
							if((xPoint == x1 || xPoint == x2) || (zPoint == z1 || zPoint == z2))
							{
								PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
										EnumParticle.REDSTONE
										, false
										, (float)(xPoint + 0.5)
										, (float)(yPoint + 0.5)
										, (float)(zPoint + 0.5)
										, (color.getRed() / 255) + i
										, (color.getGreen() / 255)
										, (color.getBlue() / 255)
										, Splatoon.data.colormode
										, 0
										, 0);
								for(Player online : Bukkit.getOnlinePlayers())
									SplatTitle.sendPlayer(online, packet);
							}
						}
					}
				}
				second--;
			}
			else
			{
				this.cancel();
			}
		}
		else if(mode == 10)
		{
			if(second > 0)
			{
				Location l = player.getLocation();
				for(double i = 0 ; i < 50 ; i++)
				{
					double x = Math.sin(i * 3);
					double z = Math.cos(i * 3);
					PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
							EnumParticle.REDSTONE
							, true
							, (float)(l.getX() + x)
							, (float)(l.getY() + 2)
							, (float)(l.getZ() + z)
							, (Color.AQUA.getRed() / 255)
							, (Color.AQUA.getGreen() / 255)
							, (Color.AQUA.getBlue() / 255)
							, 10
							, 0
							, 0);
					for(Player online : Bukkit.getOnlinePlayers())
						SplatTitle.sendPlayer(online, packet);
				}
				second--;
			}
			else
			{
				this.cancel();
			}
		}
	}
}
