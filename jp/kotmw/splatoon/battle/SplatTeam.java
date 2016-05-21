/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.kotmw.splatoon.ColorSelect;
import jp.kotmw.splatoon.Metrics;
import jp.kotmw.splatoon.Splatoon;
import jp.kotmw.splatoon.SplatoonFiles;
import jp.kotmw.splatoon.arena.ArenaData;
import jp.kotmw.splatoon.arena.ArenaSettings;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class SplatTeam
{
	public static Map<String, Scoreboard> scoreboard = new HashMap<>();
	static String splat = ChatColor.GREEN + "" + ChatColor.BOLD + "Splatoon";

	private static Metrics data = new Metrics();
	//private HashMap<String, Scoreboard> scoreboard = new HashMap<>();

	public void JoinTeam(List<Player> players, String arena)
	{
		boolean team1 = true;
		Collections.shuffle(players);
		Collections.shuffle(players);
		for(Player player : players)
		{
			if(team1)
			{
				//player.setPlayerListName(Splatoon.color.color_team1_prefix(arena) + player.getName());
				player.setMetadata(data.Team1Meta, new FixedMetadataValue(Splatoon.instance, arena));
				team1 = false;
			} else {
				//player.setPlayerListName(Splatoon.color.color_team2_prefix(arena) + player.getName());
				player.setMetadata(data.Team2Meta, new FixedMetadataValue(Splatoon.instance, arena));
				team1 = true;
			}
		}
	}

	public Location getSplatRespawnLocation(Player player, String arena)
	{
		ArenaData arenadata = ArenaSettings.getArenaData(arena);
		if(player.hasMetadata(data.Team1Meta))
		{
			return arenadata.getPlayerLocation(1, 1);
		} else if(player.hasMetadata(data.Team2Meta)){
			return arenadata.getPlayerLocation(2, 1);
		}
		return null;
	}

	public static void createScoreBoard()
	{
		//取りあえずステージ1つづつにスコアボードを生成
		//ステージ量によっちゃ正直メモリリークする
		//将来的に弄る予定
		for(String arena : SplatoonFiles.getArenaList())
		{
			Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

			Objective obj = sb.registerNewObjective("SplatScoreboard", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Splatoon");

			Team team1 = sb.registerNewTeam("SplatTeam1");
			team1.setPrefix(ColorSelect.color_team1_prefix(arena).toString());
			team1.setSuffix(ChatColor.RESET.toString());
			team1.setAllowFriendlyFire(false);
			team1.setCanSeeFriendlyInvisibles(false);
			team1.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

			Team team2 = sb.registerNewTeam("SplatTeam2");
			team2.setPrefix(ColorSelect.color_team2_prefix(arena).toString());
			team2.setSuffix(ChatColor.RESET.toString());
			team2.setAllowFriendlyFire(false);
			team2.setCanSeeFriendlyInvisibles(false);
			team2.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

			Team spectate = sb.registerNewTeam("SplatDeathPlayer");
			spectate.setPrefix(ChatColor.WHITE.toString());
			spectate.setSuffix(ChatColor.RESET.toString());
			spectate.setAllowFriendlyFire(false);
			spectate.setNameTagVisibility(NameTagVisibility.ALWAYS);
			scoreboard.put(arena, sb);
		}
	}

	public static void setTeam(String arena, Player player, int i)
	{
		Scoreboard sb = scoreboard.get(arena);
		Team team = i == 1 ? sb.getTeam("SplatTeam1") : sb.getTeam("SplatTeam2");
		team.addEntry(player.getName());
	}

	public static void setDeathTeam(String arena, Player player, boolean death)
	{
		Scoreboard sb = scoreboard.get(arena);
		Team team;
		if(death)
			team = sb.getTeam("SplatDeathPlayer");
		else
		{
			String teamdata = "SplatTeam1";
			if(player.hasMetadata(Splatoon.data.Team2Meta))
				teamdata = "SplatTeam2";
			team = sb.getTeam(teamdata);
		}
		team.addEntry(player.getName());
	}

	public static void UpdateTeamPrefix(String arena)
	{
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		if(scoreboard.containsKey(arena))
			sb = scoreboard.get(arena);
		Team team1 = sb.getTeam("SplatTeam1");
		team1.setPrefix(ColorSelect.color_team1_prefix(arena).toString());
		team1.setSuffix(ChatColor.RESET.toString());
		team1.setAllowFriendlyFire(false);
		team1.setCanSeeFriendlyInvisibles(false);
		team1.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

		Team team2 = sb.getTeam("SplatTeam2");
		team2.setPrefix(ColorSelect.color_team2_prefix(arena).toString());
		team2.setSuffix(ChatColor.RESET.toString());
		team2.setAllowFriendlyFire(false);
		team2.setCanSeeFriendlyInvisibles(false);
		team2.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

		Team spectate = sb.getTeam("SplatDeathPlayer");
		spectate.setPrefix(ChatColor.WHITE.toString());
		spectate.setSuffix(ChatColor.RESET.toString());
		spectate.setAllowFriendlyFire(false);
		scoreboard.put(arena, sb);
	}

	public static void ShowScoreboard(Player player)
	{
		player.setScoreboard(scoreboard.get(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()));
	}

	public static boolean hideScoreboard(Player player)
	{
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		Scoreboard sb = scoreboard.get(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString());
		Team team = player.hasMetadata(Splatoon.data.Team1Meta) ? sb.getTeam("SplatTeam1") : sb.getTeam("SplatTeam2");
		return team.removeEntry(player.getName());
	}

	private static String replaceTime(int time)
	{
		int second = time%60;
		int minut = (time/60)%60;
		if(String.valueOf(second).length() == 2)
			return minut +" : "+ second;
		return minut +" : 0" + second;
	}

	/*private static String replaceTime2(int time)
	{
		int second = time%60;
		int minut = (time/60)%60;
		if(String.valueOf(second).length() == 2)
			return minut +" : "+ second;
		return minut +" : 0" + second;
	}*/

	public static List<String> SplatScoreboard(String name, BattleTypeEnum type)
	{
		List<String> board = new ArrayList<>();
		int i = BattleTimer.count.get(name);
		while(i%4 != 0)
			i--;
		i = i/4;
		board.add(ChatColor.GREEN + "-Time left-");
		board.add(ChatColor.YELLOW + replaceTime(i));
		board.add(ChatColor.RESET.toString());
		board.add(ChatColor.YELLOW + "This stage : " + ChatColor.AQUA + name);
		board.add(ChatColor.RESET.toString() + ChatColor.RESET.toString());
		board.add(ColorSelect.color_team1_prefix(name) + "■ ■ ■ ■");
		board.add(ColorSelect.color_team2_prefix(name) + "■ ■ ■ ■");
		if(type.equals(BattleTypeEnum.Splat_Zones))
		{
			board.add(ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
			board.add(ChatColor.GREEN + "-Counts-");
			board.add("Team1 : " + ColorSelect.color_team1_prefix(name).toString() + SplatZones.team1_count.get(name));
			board.add("Team2 : " + ColorSelect.color_team2_prefix(name).toString() + SplatZones.team2_count.get(name));
		}
		return board;
	}

	public static void ChangeScoreboard(String arena, int time, BattleTypeEnum type)
	{
		Scoreboard board = scoreboard.get(arena);
		Objective obj = board.getObjective("SplatScoreboard");
		if(time%2 == 0)
		{
			int time_i = (time/4) + 1;
			Score scoretime = obj.getScore(ChatColor.YELLOW + replaceTime(time_i));
			scoretime.setScore(0);
			board.resetScores(ChatColor.YELLOW + replaceTime(time_i));
		}
		if(type.equals(BattleTypeEnum.Splat_Zones))
		{
			int team1 = SplatZones.team1_count.get(arena) + 1;
			Score score = obj.getScore("Team1 : " + ColorSelect.color_team1_prefix(arena).toString() + team1);
			score.setScore(0);
			board.resetScores("Team1 : " + ColorSelect.color_team1_prefix(arena).toString() + team1);
			int team2 = SplatZones.team2_count.get(arena) + 1;
			Score score_i = obj.getScore("Team2 : " + ColorSelect.color_team2_prefix(arena).toString() + team2);
			score_i.setScore(0);
			board.resetScores("Team2 : " + ColorSelect.color_team2_prefix(arena).toString() + team2);
		}
		int i = SplatScoreboard(arena, type).size() - 1;
		for(String sb : SplatScoreboard(arena, type))
		{
			Score score_i = obj.getScore(sb);
			score_i.setScore(i);
			i--;
		}
	}

	public static void resetBoard(String arena, BattleTypeEnum type)
	{
		Scoreboard board = scoreboard.get(arena);
		Objective obj = board.getObjective("SplatScoreboard");
		int i = BattleTimer.count.get(arena) * 4;
		while(i%4 != 0)
			i--;
		BattleTimer.count.put(arena, i);
		for(String sb : SplatScoreboard(arena, type))
		{
			Score score = obj.getScore(sb);
			score.setScore(0);
			board.resetScores(sb);
		}
	}
}
