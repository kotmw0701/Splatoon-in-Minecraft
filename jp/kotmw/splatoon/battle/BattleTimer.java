/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.battle;

import java.util.HashMap;
import java.util.Map;

import jp.kotmw.splatoon.SplatTitle;
import jp.kotmw.splatoon.Splatoon;
import jp.kotmw.splatoon.Squid;
import jp.kotmw.splatoon.arena.ArenaLogs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class BattleTimer implements Runnable
{
	static Map<String, Integer> count = new HashMap<>();
	private static String name;
	private static BattleTypeEnum type;
	private static ArenaLogs logs;

	public BattleTimer(String name, int time, BattleTypeEnum type, ArenaLogs log)
	{
		BattleTimer.name = name;
		BattleTimer.type = type;
		BattleTimer.logs = log;
		count.put(name, time);
		if(type.equals(BattleTypeEnum.Splat_Zones))
		{
			int i = 100;
			SplatZones.team1_count.put(name, i);
			SplatZones.team2_count.put(name, i);
		}
	}

	public void run()
	{
		CountDown(count.get(name));
	}

	public void CountDown(int i)
	{
		if(i%2 == 0)
			SplatTeam.ChangeScoreboard(name, i, type);
		if(i > 0)
		{
			if(i == (600*4))
				PlayersSendMessage(" 残り10分", false);
			if(i == (540*4))
				PlayersSendMessage(" 残り9分", false);
			if(i == (480*4))
				PlayersSendMessage(" 残り8分", false);
			if(i == (420*4))
				PlayersSendMessage(" 残り7分", false);
			if(i == (360*4))
				PlayersSendMessage(" 残り6分", false);
			if(i == (300*4))
				PlayersSendMessage(" 残り5分", false);
			if(i == (240*4))
				PlayersSendMessage(" 残り4分", false);
			if(i == (180*4))
				PlayersSendMessage(" 残り3分", false);
			if(i == (120*4))
				PlayersSendMessage(" 残り2分", false);
			if(i == (60*4))
				PlayersSendMessage(" 残り1分", true);
			if(i == (30*4))
				PlayersSendMessage(" 残り30秒", false);
			if(i == (10*4))
				PlayersSendMessage(" 終了まで 10", false);
			if(i < (6*4))
				if(i%4 == 0)
					PlayersSendMessage(" " + i/4, false);
			i--;
			count.put(name, i);
			if(type.equals(BattleTypeEnum.Splat_Zones) && i%2 == 0)
			{
				SplatZones zones = new SplatZones(name, logs);
				zones.GachiArenaParticle();
				if((SplatZones.team1_count.get(name) == 0) || (SplatZones.team2_count.get(name) == 0))
				{
					SplatTeam.ChangeScoreboard(name, i, type);
					Bukkit.getScheduler().cancelTask(TurfBattle.CountDownTaskID.get(name));
					SplatTeam.resetBoard(name, type);
					SplatZones.team1.remove(name);
					SplatZones.team2.remove(name);
					for(Player player : TurfBattle.JoinPlayersList(name, true))
					{
						Squid.SquidInitialization(player);
						player.setGameMode(GameMode.SPECTATOR);
						player.removePotionEffect(PotionEffectType.INVISIBILITY);
						player.removePotionEffect(PotionEffectType.SPEED);
						player.getInventory().setHeldItemSlot(1);
						player.getInventory().clear();
						player.setAllowFlight(true);
						player.setFlying(true);
						SplatTitle.sendFullTitle(player, 60, ChatColor.AQUA.toString() + ChatColor.BOLD + "Finish!", "");
					}
					Bukkit.getScheduler().scheduleSyncDelayedTask(Splatoon.instance, new Runnable(){ public void run() {
						new BattleEnd(name, 130, type, true, logs).runTaskTimer(Splatoon.instance, 0, 1);
					}}, 60);
				}
			}
		} else {
			Bukkit.getScheduler().cancelTask(TurfBattle.CountDownTaskID.get(name));
			SplatTeam.resetBoard(name, type);
			SplatZones.team1.remove(name);
			SplatZones.team2.remove(name);
			for(Player player : TurfBattle.JoinPlayersList(name, true))
			{
				if(SplatEventListeners.death.containsKey(player.getName()))
				{
					SplatEventListeners.death.get(player.getName()).cancel();
					SplatEventListeners.death.remove(player.getName());
				}
				player.removeMetadata(Splatoon.data.SquidPlayerMeta, Splatoon.instance);
				Squid.SquidInitialization(player);
				player.setGameMode(GameMode.SPECTATOR);
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
				player.removePotionEffect(PotionEffectType.SPEED);
				player.getInventory().setHeldItemSlot(1);
				player.getInventory().clear();
				player.setAllowFlight(true);
				player.setFlying(true);
				SplatTitle.sendFullTitle(player, 60, ChatColor.AQUA.toString() + ChatColor.BOLD + "Finish!", "");
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(Splatoon.instance, new Runnable(){ public void run() {
				new BattleEnd(name, 130, type, false, logs).runTaskTimer(Splatoon.instance, 0, 1);
			}}, 60);
		}
	}

	private void PlayersSendMessage(String msg, boolean title)
	{
		if(title)
		{
			for(Player player : TurfBattle.JoinPlayersList(name, true))
				SplatTitle.sendFullTitle(player, 0, 40, 20, "", ChatColor.WHITE + "" + ChatColor.BOLD + msg);
		}
		else
		{
			for(Player player : TurfBattle.JoinPlayersList(name, true))
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN +msg);
		}
	}
}
