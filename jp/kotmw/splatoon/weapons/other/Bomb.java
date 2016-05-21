/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.weapons.other;

import java.util.HashMap;
import java.util.Map;

import jp.kotmw.splatoon.ColorSelect;
import jp.kotmw.splatoon.SplatTitle;
import jp.kotmw.splatoon.Splatoon;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Bomb implements Listener
{
	public static Map<String, Boolean> FullCharge = new HashMap<>();

	@EventHandler
	public void ThrowBom(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		float ink = player.getExp();
		ItemStack item = player.getItemInHand();
		Action action = e.getAction();
		if(!player.hasMetadata(Splatoon.data.ArenaMeta))
			return;
		if ((item == null)
				|| (item.getType() == Material.AIR)
				|| (!item.getItemMeta().hasDisplayName())
				|| (!item.getItemMeta().getDisplayName().equals(Splatoon.data.BOMB))
				|| (!item.getItemMeta().hasLore()))
			return;
		if((action == Action.LEFT_CLICK_AIR)
				|| (action == Action.LEFT_CLICK_BLOCK)
				|| (action == Action.PHYSICAL))
			return;
		if(ink < 0.25f)
		{
			SplatTitle.sendFullTitle(player, 1, 20, 1, "", ChatColor.RED + "インクがありません！");
			return;
		}
		player.playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
		ThrownExpBottle bottle = player.launchProjectile(ThrownExpBottle.class);
		bottle.setShooter(player);
		player.setExp(ink - 0.25f);
		e.setCancelled(true);
	}

	@EventHandler
	public void ExplodeExpBottle(ExpBottleEvent e)
	{
		ThrownExpBottle bottle = e.getEntity();
		Player player = (Player) bottle.getShooter();
		if(!player.hasMetadata(Splatoon.data.ArenaMeta))
			return;
		e.setExperience(0);
		Location loc = bottle.getLocation().clone();
		loc.getWorld().createExplosion(bottle.getLocation(), 0, false);
		int r = 1;
		Location startLoc = loc.subtract(r, r, r);

		for(int x=startLoc.getBlockX(); x<startLoc.getBlockX()+r*4; x++)
		{
			for(int y=startLoc.getBlockY(); y<startLoc.getBlockY()+r*3; y++)
			{
				for(int z=startLoc.getBlockZ(); z<startLoc.getBlockZ()+r*4; z++)
				{
					Location loc_i = new Location(startLoc.getWorld(), x, y, z);
					Block b = loc_i.getBlock();
					ColorSelect.ColorChange(b, player);
					Splatoon.battle.Damager(player, x, y, z, 5);
				}
			}
		}
	}

	public static void setBomb(Player player)
	{
		player.getInventory().setItem(1, Splatoon.data.item[5][0]);
		/*ItemStack item = Splatoon.data.item[5][0].clone();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Bomb Rush!");
		item.setItemMeta(meta);
		player.getInventory().setItem(2, item);*/
	}
}
