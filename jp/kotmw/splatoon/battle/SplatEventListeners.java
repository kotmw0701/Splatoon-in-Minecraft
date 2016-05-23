/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 */
package jp.kotmw.splatoon.battle;

import java.util.HashMap;
import java.util.Map;

import jp.kotmw.splatoon.*;
import jp.kotmw.splatoon.arena.ArenaData;
import jp.kotmw.splatoon.arena.ArenaLogs;
import jp.kotmw.splatoon.arena.ArenaSettings;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

public class SplatEventListeners implements Listener
{
	private static Map<String, ArenaLogs> log = new HashMap<>();
	public static Map<String, BukkitTask> death = new HashMap<>();

	public static void setArenaLog(String arena, ArenaLogs logs)
	{
		log.put(arena, logs);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e)
	{
		Player player = e.getPlayer();
		if((player.hasMetadata(Splatoon.data.RoomMeta))
				|| (player.hasMetadata(Splatoon.data.ArenaMeta))
				|| (player.hasMetadata(Splatoon.data.SpectateMeta)))
			e.setCancelled(true);
		if(player.getItemInHand().getItemMeta().hasDisplayName()
				&& player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(JoinQuitEvents.profileitem))
			e.setCancelled(true);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{
		Player player = e.getPlayer();
		if((player.hasMetadata(Splatoon.data.RoomMeta))
				|| (player.hasMetadata(Splatoon.data.ArenaMeta))
				|| (player.hasMetadata(Splatoon.data.SpectateMeta)))
			e.setCancelled(true);
	}

	@EventHandler
	public void onChangeFoodLevel(FoodLevelChangeEvent e)
	{
		Player player = (Player) e.getEntity();
		if((player.hasMetadata(Splatoon.data.RoomMeta))
				|| (player.hasMetadata(Splatoon.data.ArenaMeta)))
			e.setCancelled(true);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e)
	{
		Player player = e.getPlayer();
		if((player.hasMetadata(Splatoon.data.RoomMeta))
				|| (player.hasMetadata(Splatoon.data.ArenaMeta)))
			e.setCancelled(true);
	}

	@EventHandler
	public void onItemHeld(PlayerItemHeldEvent e)
	{
		Player player = e.getPlayer();
		if(player.hasMetadata(Splatoon.data.SquidPlayerMeta))
		{
			if(e.getNewSlot() <= 3)
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e)
	{
		if((e.getCause() == DamageCause.VOID))
			return;
		Entity entity = e.getEntity();
		if(entity.hasMetadata(Splatoon.data.SquidMeta))
			e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player player = (Player) e.getEntity();
			if(player.hasMetadata(Splatoon.data.InvincibleMeta))
			{
				e.setCancelled(true);
				return;
			}
			if((player.hasMetadata(Splatoon.data.RoomMeta))
					|| (player.hasMetadata(Splatoon.data.ArenaMeta)))
			{
				if((e.getCause() == DamageCause.FALL)
						|| (e.getCause() == DamageCause.ENTITY_ATTACK))
					e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerInventoryClick(InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();
		if(player.hasMetadata(Splatoon.data.RoomMeta)
				|| player.hasMetadata(Splatoon.data.ArenaMeta))
			e.setCancelled(true);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		Player player = e.getPlayer();
		if(player.hasMetadata(Splatoon.data.WaitingMeta))
			if((e.getFrom().getX() != e.getTo().getX())
					|| (e.getFrom().getZ() != e.getTo().getZ()))
				e.setCancelled(true);
		if(player.hasMetadata(Splatoon.data.ArenaMeta))
		{
			ArenaData arenadata = ArenaSettings.getArenaData(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString());
			if(player.getLocation().getBlockY() <= arenadata.getLocation(2).getBlockY()-1)
			{
				player.damage(50);
				return;
			}
			Location loc = player.getLocation().clone();
			loc.add(0, 0.1, 0);
			if(loc.getBlock().getType().equals(Material.WATER_LILY)
					|| loc.getBlock().equals(Material.WATER))
				player.damage(50);
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		Player player = e.getPlayer();
		if(!player.hasMetadata(Splatoon.data.ArenaMeta))
			return;
		String arena = player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString();
		String team = player.hasMetadata(Splatoon.data.Team1Meta) ? "[TEAM1] " : "[TEAM2] ";
		ChatColor color = player.hasMetadata(Splatoon.data.Team1Meta)
				? ColorSelect.color_team1_prefix(arena)
						: ColorSelect.color_team2_prefix(arena);
		log.get(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()).addLogs(team+player.getName()+":"+e.getMessage());
		e.setCancelled(true);
		for(Player players : TurfBattle.JoinPlayersList(arena, true))
			players.sendMessage(color+team+ChatColor.WHITE+player.getName()+ChatColor.GREEN+":"+ChatColor.WHITE+e.getMessage());
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		Player player = e.getEntity();
		if(!player.hasMetadata(Splatoon.data.ArenaMeta))
			return;

		String arena = player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString();
		String team = player.hasMetadata(Splatoon.data.Team1Meta) ? "[TEAM1] " : "[TEAM2] ";
		ChatColor color = player.hasMetadata(Splatoon.data.Team1Meta)
				? ColorSelect.color_team1_prefix(arena)
				: ColorSelect.color_team2_prefix(arena);

		Metrics.AllPlayerSend(color + player.getDisplayName() + ChatColor.YELLOW + "が倒された！");
		e.setDeathMessage("");
		e.getDrops().clear();
		if(player.getKiller() instanceof Player)
		{
			SplatTitle.sendFullTitle(player, 0, 60, 40, "", player.getKiller().getMetadata(SelectInv.weaponmeta).get(0).asString() + " で倒された！");
			player.teleport(player.getKiller().getLocation());
		}
		if(player.hasMetadata(Splatoon.data.SquidPlayerMeta))
		{
			player.removeMetadata(Splatoon.data.SquidPlayerMeta, Splatoon.instance);
			Squid.SquidInitialization(player);
		}
		log.get(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()).addLogs(player.getName() + " Death!");
		player.getInventory().setHeldItemSlot(1);
		player.setHealth(player.getMaxHealth());
		player.setRemainingAir(player.getMaximumAir());
		player.setFoodLevel(20);
		player.setMetadata(Splatoon.data.InvincibleMeta, new FixedMetadataValue(Splatoon.instance, player.getName()));
		player.setGameMode(GameMode.SPECTATOR);
		player.setAllowFlight(true);
		player.setFlying(true);
		death.put(player.getName(), new Respawn(3*20, player).runTaskTimer(Splatoon.instance, 1, 1));
	}
}