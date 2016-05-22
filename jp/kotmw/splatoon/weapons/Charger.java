/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.weapons;

import jp.kotmw.splatoon.ColorSelect;
import jp.kotmw.splatoon.Metrics;
import jp.kotmw.splatoon.SplatTitle;
import jp.kotmw.splatoon.Splatoon;
import jp.kotmw.splatoon.API.ParticleAPI;
import jp.kotmw.splatoon.API.ParticleAPI.EnumParticle;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class Charger implements Listener
{
	private Metrics data = new Metrics();
	int r = 1;

	@EventHandler
	public void onLaunch(EntityShootBowEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player player = (Player) e.getEntity();
			float ink = player.getExp();
			ItemStack SplatCharger = player.getItemInHand();
			if(!player.hasMetadata(Splatoon.data.ArenaMeta))
				return;
			if ((SplatCharger == null)
					|| (SplatCharger.getType() == Material.AIR)
					|| (!SplatCharger.getItemMeta().hasDisplayName())
					|| (!SplatCharger.getItemMeta().getDisplayName().equals(data.CHARGER))
					|| (!SplatCharger.getItemMeta().hasLore()))
				return;
			if(ink <= 0.45f)
			{
				SplatTitle.sendFullTitle(player, 1, 20, 1, "", ChatColor.RED + "インクがありません！");
				e.setCancelled(true);
				return;
			}
			int shootlength = 0;
			if(e.getForce() >= 1.0)
				shootlength = 25;
			else if(e.getForce() < 1.0 && e.getForce() >= 0.9)
				shootlength = 25;
			else if(e.getForce() < 0.9 && e.getForce() >= 0.8)
				shootlength = 23;
			else if(e.getForce() < 0.8 && e.getForce() >= 0.7)
				shootlength = 21;
			else if(e.getForce() < 0.7 && e.getForce() >= 0.6)
				shootlength = 19;
			else if(e.getForce() < 0.6 && e.getForce() >= 0.5)
				shootlength = 17;
			else if(e.getForce() < 0.5 && e.getForce() >= 0.4)
				shootlength = 15;
			else if(e.getForce() < 0.4 && e.getForce() >= 0.3)
				shootlength = 13;
			else if(e.getForce() < 0.3 && e.getForce() >= 0.2)
				shootlength = 11;
			else if(e.getForce() < 0.2 && e.getForce() >= 0.1)
				shootlength = 9;
			else if(e.getForce() < 0.1)
				shootlength = 7;
			Snowball ball = (Snowball)player.launchProjectile(Snowball.class);
			Vector vec = player.getLocation().getDirection();
			ball.setVelocity(new Vector(vec.getX()*10,vec.getY()*10,vec.getZ()*10));
			player.setExp(ink - 0.45f);
			BlockIterator seeblock = new BlockIterator(player, shootlength);
			while(seeblock.hasNext())
			{
				Block block = seeblock.next();
				Location loc = block.getLocation().clone();
				if(block.getType() != Material.AIR)
				{
					ChargerPaint(player, loc);
					return;
				}
				while(loc.getBlock().getType() == Material.AIR)
				{
					if(loc.getBlockY() <= 1)
						break;
					loc.add(0, -1, 0);
				}
				ChargerPaint(player, loc);
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		final Player player = e.getPlayer();
		@SuppressWarnings("unused")
		float ink = player.getExp();
		ItemStack SplatCharger = player.getItemInHand();
		if(!player.hasMetadata(Splatoon.data.ArenaMeta))
			return;
		if((e.getAction().equals(Action.RIGHT_CLICK_AIR))
				|| e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		if((SplatCharger == null)
				|| (SplatCharger.getType() == Material.AIR)
				|| (!SplatCharger.getItemMeta().hasDisplayName())
				|| (!SplatCharger.getItemMeta().getDisplayName().equals(data.CHARGER))
				|| (!SplatCharger.getItemMeta().hasLore()))
			return;
	}

	public void ChargerPaint(Player player, Location loc)
	{
		for(int x = loc.getBlockX(); x < loc.getBlockX() + r * 2; x++)
		{
			for(int y = loc.getBlockY(); y < loc.getBlockY() + r * 2; y++)
			{
				for(int z = loc.getBlockZ(); z < loc.getBlockZ() + r * 2; z++)
				{
					Location bulletloc = new Location(loc.getWorld(), x, y, z);
					Block block = bulletloc.getBlock();
					ColorSelect.ColorChange(block, player);
				}
			}
		}
		for(int x = loc.getBlockX(); x < loc.getBlockX() + r + 1; x++)
		{
			for(int y = loc.getBlockY(); y < loc.getBlockY() + r + 1; y++)
			{
				for(int z = loc.getBlockZ(); z < loc.getBlockZ() + r + 1; z++)
				{
					Splatoon.battle.Damager(player, x, y, z, 7);
				}
			}
		}
	}

	@EventHandler
	public void onLaunch(ProjectileHitEvent e)
	{
		Projectile proj = e.getEntity();

		if(proj instanceof Arrow)
		{
			Arrow ball = (Arrow) proj;
			if(ball.getShooter() instanceof Player)
			{
				Player player = (Player)ball.getShooter();
				if(!ball.hasMetadata(Splatoon.data.ChargerArrow))
					return;
				ParticleAPI.createEffect(EnumParticle.BLOCK_CRACK.setItem(new ItemStack(Material.WOOL, 1, (short)ColorSelect.ColorWoolItem(player))), ball.getLocation(), 0.5f, 0.5f, 0.5f, 0.0f, 10);
				int r = 1;
				Location startLoc = ball.getLocation().subtract(r, r, r);

				for(int x=startLoc.getBlockX(); x<startLoc.getBlockX()+r*3; x++)
				{
					for(int y=startLoc.getBlockY(); y<startLoc.getBlockY()+r*3; y++)
					{
						for(int z=startLoc.getBlockZ(); z<startLoc.getBlockZ()+r*3; z++)
						{
							Location loc = new Location(startLoc.getWorld(), x, y, z);
							Block b = loc.getBlock();
							ColorSelect.ColorChange(b, player);
						}
					}
				}
			}
		}
	}
}
