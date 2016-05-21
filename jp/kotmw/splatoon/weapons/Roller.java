/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.weapons;

import jp.kotmw.splatoon.ColorSelect;
import jp.kotmw.splatoon.SplatTitle;
import jp.kotmw.splatoon.Splatoon;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class Roller implements Listener
{
	int PD;

	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		Player player = e.getPlayer();
		float ink = player.getExp();
		ItemStack SplatRoller = player.getItemInHand();
		if(!player.hasMetadata(Splatoon.data.ArenaMeta))
			return;
		if ((SplatRoller == null)
				|| (SplatRoller.getType() == Material.AIR)
				|| (!SplatRoller.getItemMeta().hasDisplayName())
				|| (!SplatRoller.getItemMeta().getDisplayName().equals(Splatoon.data.ROLLER1))
				|| (!SplatRoller.getItemMeta().hasLore()))
			return;
		if(!Splatoon.data.Rooler.containsKey(player.getName()))
			Splatoon.data.Rooler.put(player.getName(), true);
		if(!Splatoon.data.Rooler.get(player.getName()))
			return;
		if(ink <= 0.05)
		{
			SplatTitle.sendFullTitle(player, 1, 20, 1, "", ChatColor.RED + "インクがありません！");
			return;
		}
		Location loc = player.getLocation();
		float dir = loc.getYaw();
		PD = PlayerDirection(dir);
		if(PD == 5)
			PD = PlayerDirection(dir);
		//player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
		player.setExp(ink - 0.005f);
		World world = loc.getWorld();
		int Player_x = loc.getBlockX();
		int Player_y = loc.getBlockY();
		int Player_z = loc.getBlockZ();
		int Center_x, Center_z;
		if(PD == 0)
		{
			Center_z = Player_z + 2;
			this.Splatroller(PD, Player_x, Player_y, Center_z, player, world);
		}
		else if(PD == 1)
		{
			Center_x = Player_x - 2;
			this.Splatroller(PD, Center_x, Player_y, Player_z, player, world);
		}
		else if(PD == 2)
		{
			Center_z = Player_z - 2;
			this.Splatroller(PD, Player_x, Player_y, Center_z, player, world);
		}
		else if(PD == 3)
		{
			Center_x = Player_x + 2;
			this.Splatroller(PD, Center_x, Player_y, Player_z, player, world);
		}
		return;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		ItemStack SplatRoller = player.getItemInHand();
		if(Action.PHYSICAL == e.getAction())
			return;
		if ((SplatRoller == null)
				|| (SplatRoller.getType() == Material.AIR)
				|| (!SplatRoller.getItemMeta().hasDisplayName())
				|| (!SplatRoller.getItemMeta().getDisplayName().equals(Splatoon.data.ROLLER1))
				|| (!SplatRoller.getItemMeta().hasLore()))
			return;
		if(Splatoon.data.Rooler.containsKey(player.getName()))
		{
			if(Splatoon.data.Rooler.get(player.getName()))
				Splatoon.data.Rooler.put(player.getName(), false);
			else
			{
				Splatoon.data.Rooler.put(player.getName(), true);
			}
			String msg = Splatoon.data.Rooler.get(player.getName()) ? ChatColor.YELLOW + "ON" : ChatColor.RED + "OFF";
			player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "塗りを " + msg + ChatColor.GREEN + " にしました");
			//ばしゃっ
			if(Splatoon.data.Rooler.get(player.getName()))
			{
				Location loc = player.getLocation().clone();
				int PD = PlayerDirection(loc.getYaw());
				if(PD == 0)
				{
					loc.add(0, 0, 1);
					Arrow arrow=loc.getWorld().spawnArrow(loc.add(loc.getDirection()),loc.getDirection(),0.1f,12);
					arrow.setShooter(player);
					arrow.setMetadata(Splatoon.data.ChargerArrow, new FixedMetadataValue(Splatoon.instance, player.getName()));
					//new RemoveEntities(arrow).runTaskLater(Splatoon.instance, 1*20);
				}
				else if(PD == 1)
				{
					loc.add(-1, 0, 0);
					Arrow arrow=loc.getWorld().spawnArrow(loc.add(loc.getDirection()),loc.getDirection(),0.1f,12);
					arrow.setShooter(player);
					arrow.setMetadata(Splatoon.data.ChargerArrow, new FixedMetadataValue(Splatoon.instance, player.getName()));
					//new RemoveEntities(arrow).runTaskLater(Splatoon.instance, 1*20);
				}
				else if(PD == 2)
				{
					loc.add(0, 0, -1);
					Arrow arrow=loc.getWorld().spawnArrow(loc.add(loc.getDirection()),loc.getDirection(),0.1f,12);
					arrow.setShooter(player);
					arrow.setMetadata(Splatoon.data.ChargerArrow, new FixedMetadataValue(Splatoon.instance, player.getName()));
					//new RemoveEntities(arrow).runTaskLater(Splatoon.instance, 1*20);
				}
				else if(PD == 3)
				{
					loc.add(-1, 0, 0);
					Arrow arrow=loc.getWorld().spawnArrow(loc.add(loc.getDirection()),loc.getDirection(),0.1f,12);
					arrow.setShooter(player);
					arrow.setMetadata(Splatoon.data.ChargerArrow, new FixedMetadataValue(Splatoon.instance, player.getName()));
					//new RemoveEntities(arrow).runTaskLater(Splatoon.instance, 1*20);
				}

			}
		}
	}

	public static int PlayerDirection(Float dir)
	{
		int Direction = 5;
		if((((dir >= 0.0)&&(dir <= 44.5))||((dir >= 314.6)&&(dir <= 360.0)))||(((dir <= -0.0)&&(dir >= -44.5))||((dir <= -314.6)&&(dir >= -360.0))))//0
		{
			Direction = 0;
		}
		else if (((dir >= 44.6)&&(dir <= 134.5))||((dir <= -224.6)&&(dir >= -314.5)))//1
		{
			Direction = 1;
		}
		else if (((dir >= 134.6)&&(dir <= 224.5))||((dir <= -134.6)&&(dir >= -224.5)))//2
		{
			Direction = 2;
		}
		else if (((dir >= 224.6)&&(dir <= 314.5))||((dir <= -44.6)&&(dir >= -134.5)))//3
		{
			Direction = 3;
		}
		return Direction;
	}

	public void Splatroller(int PD ,int Center_x, int Center_y, int Center_z, Player player, World world)
	{
		int x1, x2, y1, y2, z1, z2;
		if(PD == 0 || PD == 2)
		{
			x1 = Center_x - 2; x2 = Center_x + 2;
			y1 = Center_y - 1; y2 = Center_y;
			for (int xPoint = x1; xPoint <= x2; xPoint++)//530 ～ 534の5つの幅(SOUTH)
			{
				for (int yPoint = y1; yPoint <= y2; yPoint++)
				{
					Block CheckBlock = world.getBlockAt(xPoint, yPoint, Center_z);
					ColorSelect.ColorChange(CheckBlock, player);
					Splatoon.battle.Damager(player, xPoint, yPoint, Center_z, 20);
				}
			}
			return;
		}
		else if (PD == 1 || PD == 3)
		{
			y1 = Center_y - 1; y2 = Center_y;
			z1 = Center_z - 2; z2 = Center_z + 2;
			for (int zPoint = z1; zPoint <= z2; zPoint++)//12 ～ 16の5つの幅(WEST)
			{
				for (int yPoint = y1; yPoint <= y2; yPoint++)
				{
					Block CheckBlock = world.getBlockAt(Center_x, yPoint, zPoint);
					ColorSelect.ColorChange(CheckBlock, player);
					Splatoon.battle.Damager(player, Center_x, yPoint, zPoint, 20);
				}
			}
			return;
		}
	}
}