/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.battle;

import java.util.List;

import jp.kotmw.splatoon.ColorSelect;
import jp.kotmw.splatoon.SplatTitle;
import jp.kotmw.splatoon.Splatoon;
import jp.kotmw.splatoon.arena.ArenaLogs;
import jp.kotmw.splatoon.arena.ArenaStatusEnum;
import jp.kotmw.splatoon.arena.RoomData;
import jp.kotmw.splatoon.events.ArenaStatusChangeEvent;
import jp.kotmw.splatoon.events.BattleStartEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class BeforeStart extends BukkitRunnable {

	private static String name;
	private static int second;

	public BeforeStart(String name, int second)
	{
		BeforeStart.name = name;
		BeforeStart.second = second;
	}

	@Override
	public void run()
	{
		if(second > 0)
		{
			for(Player player : TurfBattle.JoinPlayersList(name, false))
			{
				SplatTitle.sendFullTitle(player, 1, 40, 1, "", ChatColor.BLUE + "ステージに移動します: " + ChatColor.GREEN + second);
				player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
			}
			second--;
		} else {
			this.cancel();
			Splatoon.arenastatus.SetStatus();
			String arena = Splatoon.arenastatus.RandomSelect(name);
			BattleStartEvent event = new BattleStartEvent(name, arena);
			Bukkit.getServer().getPluginManager().callEvent(event);
			List<Player> players = event.getPlayers();
			ArenaLogs logs = new ArenaLogs(arena, name);
			logs.setStartDate(event.getDate());
			logs.setPlayers(players);
			logs.addLogs(event.getRoom() + " Teleported to " + event.getArena());
			//logs.addLogs("Team1 Color : " + event.getTeamDyeColor(1).toString() + " | " + "Team2 Color : " + event.getTeamDyeColor(2).toString());
			SplatEventListeners.setArenaLog(arena, logs);
			if(players.size() == 0)
				return;
			if(arena == null)
			{
				for(Player player : players)
				{
					player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "全てのルームが使用中のため、ゲームを開始できません！");
					player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "今しばらくお待ちください");
				}
				return;
			}
			int jointeam1 = 1, jointeam2 = 1;
			ArenaStatusChangeEvent event2 = new ArenaStatusChangeEvent(arena, ArenaStatusEnum.INGAME);
			Bukkit.getServer().getPluginManager().callEvent(event2);
			RoomData roomdata = new RoomData(name);
			TurfBattle.Start(arena, roomdata.getBattleType(), logs);
			if(!Splatoon.data.SelectTeam)
				Splatoon.teaming.JoinTeam(players, name);
			for(Player player : players)
			{
				player.removeMetadata(Splatoon.data.RoomMeta, Splatoon.instance);
				player.setMetadata(Splatoon.data.ArenaMeta, new FixedMetadataValue(Splatoon.instance, arena));
				player.setMetadata(Splatoon.data.WaitingMeta, new FixedMetadataValue(Splatoon.instance, arena));
				player.getInventory().removeItem(Splatoon.data.item[3][0]);
				player.getInventory().removeItem(Splatoon.data.item[4][1]);
				//player.getInventory().setHelmet(TurfBattle.setPaintedHelm(arena, player));
				player.setExp(0.0f);
				if(Splatoon.data.SelectTeam)
				{
					player.getInventory().removeItem(Splatoon.data.item[3][1]);
					player.getInventory().removeItem(Splatoon.data.item[3][2]);
					if(!player.hasMetadata(Splatoon.data.Team1Meta)
							&& !player.hasMetadata(Splatoon.data.Team2Meta))
					{
						int team1 = TurfBattle.TeamPlayersList(arena, 1).size();
						int team2 = TurfBattle.TeamPlayersList(arena, 2).size();
						if(team1 == team2)
							player.setMetadata(Splatoon.data.Team1Meta, new FixedMetadataValue(Splatoon.instance, name));
						else if(team1 > team2)
							player.setMetadata(Splatoon.data.Team2Meta, new FixedMetadataValue(Splatoon.instance, name));
						else if(team2 < team2)
							player.setMetadata(Splatoon.data.Team1Meta, new FixedMetadataValue(Splatoon.instance, name));
					}
				}

				SplatTeam.UpdateTeamPrefix(arena);
				SplatTeam.ShowScoreboard(player);
				//player.getInventory().addItem(Splatoon.data.item[5][0]);

				if(player.hasMetadata(Splatoon.data.Team1Meta))
				{
					TurfBattle.teleportteam(arena, 1, jointeam1, player);
					player.sendMessage("あなたは [ " + ColorSelect.color_team1_prefix(arena) + "Team1" + ChatColor.WHITE + " ] になりました");
					SplatTeam.setTeam(arena, player, 1);
					player.setBedSpawnLocation(Splatoon.teaming.getSplatRespawnLocation(player,arena));
					jointeam1++;
				} else if(player.hasMetadata(Splatoon.data.Team2Meta)){
					TurfBattle.teleportteam(arena, 2, jointeam2, player);
					player.sendMessage("あなたは [ " + ColorSelect.color_team2_prefix(arena) + "Team2" + ChatColor.WHITE + " ] になりました");
					SplatTeam.setTeam(arena, player, 2);
					player.setBedSpawnLocation(Splatoon.teaming.getSplatRespawnLocation(player,arena));
					jointeam2++;
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void StartMessage(Player player, int i)
	{
		String arena = player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString();
		int ii = i == 1 ? 2 : 1;
		player.sendMessage(ChatColor.GREEN + "" + ChatColor.STRIKETHROUGH + "------------------------");
		player.sendMessage(ChatColor.AQUA + "あなたのチームメンバー");
		for(Player player_i : TurfBattle.TeamPlayersList(arena, i))
			player.sendMessage(player_i.getName());
		player.sendMessage(ChatColor.GREEN + "" + ChatColor.STRIKETHROUGH + "------------------------");
		player.sendMessage(ChatColor.YELLOW + "相手のチームメンバー");
		for(Player player_i : TurfBattle.TeamPlayersList(arena, ii))
			player.sendMessage(player_i.getName());
		player.sendMessage(ChatColor.GREEN + "" + ChatColor.STRIKETHROUGH + "------------------------");
	}
}
