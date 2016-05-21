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
import jp.kotmw.splatoon.API.ParticleAPI;
import jp.kotmw.splatoon.API.ParticleAPI.EnumParticle;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Shooter implements Listener
{
	float useink;

	@EventHandler
	public void onInteract(final PlayerInteractEvent e)
	{
		final Player player = e.getPlayer();
		final float ink = player.getExp();
		ItemStack SplatShooter = player.getItemInHand();
		Action action = e.getAction();
		if(!player.hasMetadata(Splatoon.data.ArenaMeta))
			return;
		if((SplatShooter == null)
				|| (SplatShooter.getType() == Material.AIR)
				|| (!SplatShooter.getItemMeta().hasDisplayName())
				|| (!SplatShooter.getItemMeta().getDisplayName().equals(Splatoon.data.SHOOTER1))
				|| (!SplatShooter.getItemMeta().hasLore()))
			return;
		if((action == Action.LEFT_CLICK_AIR)
				|| (action == Action.LEFT_CLICK_BLOCK)
				|| (action == Action.PHYSICAL))
			return;
		if(ink <= 0.05f)
		{
			SplatTitle.sendFullTitle(player, 1, 20, 1, "", ChatColor.RED + "インクがありません！");
			return;
		}
		Vector vec = player.getLocation().getDirection();
		Snowball ball = player.launchProjectile(Snowball.class);
		ball.setVelocity(new Vector(vec.getX()*0.7,vec.getY()*0.7,vec.getZ()*0.7));
		ball.setShooter(player);
		player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
		player.setExp(ink - 0.02f);
		new Rate_of_fire(player).runTaskLater(Splatoon.instance, Splatoon.data.rateoffire);
		ParticleAPI.createEffect(EnumParticle.BLOCK_CRACK.setItem(new ItemStack(Material.WOOL, 1, (short)ColorSelect.ColorWoolItem(player))), player.getLocation(), 0.5f, 0.5f, 0.5f, 0.0f, 10);
		int r = 1;
		Location startLoc = player.getLocation().subtract(r, r, r);

		for(int x=startLoc.getBlockX(); x<startLoc.getBlockX()+r*3; x++)
		{
			for(int y=startLoc.getBlockY(); y<startLoc.getBlockY()+r*2; y++)
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

	@EventHandler
	public void onLaunch(ProjectileHitEvent e)
	{
		Projectile proj = e.getEntity();

		if(proj instanceof Snowball)
		{
			Snowball snowball = (Snowball) proj;
			if(snowball.getShooter() instanceof Player)
			{
				Player player = (Player)snowball.getShooter();
				if(!player.hasMetadata(Splatoon.data.ArenaMeta))
					return;
				ParticleAPI.createEffect(EnumParticle.BLOCK_CRACK.setItem(new ItemStack(Material.WOOL, 1, (short)ColorSelect.ColorWoolItem(player))), snowball.getLocation(), 0.5f, 0.5f, 0.5f, 0.0f, 10);
				int r = 1;
				Location startLoc = snowball.getLocation().subtract(r, r, r);

				for(int x=startLoc.getBlockX(); x<startLoc.getBlockX()+r*3; x++)
				{
					for(int y=startLoc.getBlockY(); y<startLoc.getBlockY()+r*2; y++)
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

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player player = (Player)e.getEntity();
			if(!player.hasMetadata(Splatoon.data.ArenaMeta))
				return;
			if(e.getDamager() instanceof Projectile
					&& e.getDamager() instanceof Snowball)
			{
				Snowball snowball = (Snowball)e.getDamager();
				Player damager = (Player)snowball.getShooter();
				if(player == damager)
				{
					e.setCancelled(true);
					return;
				}
				boolean playerteam = player.hasMetadata(Splatoon.data.Team1Meta) ? true : false;
				boolean otherteam = damager.hasMetadata(Splatoon.data.Team1Meta) ? true : false;
				if((playerteam && otherteam) || (!playerteam && !otherteam))
				{
					e.setCancelled(true);
					return;
				}
			}
		}
	}
}
