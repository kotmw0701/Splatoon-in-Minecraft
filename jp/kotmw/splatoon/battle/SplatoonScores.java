/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.battle;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.kotmw.splatoon.ColorSelect;
import jp.kotmw.splatoon.Metrics;
import jp.kotmw.splatoon.Splatoon;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SplatoonScores
{
	private static Map<String, Double> score = new HashMap<>();
	//private HashMap<String, Integer> kill = new HashMap<>();
	//private HashMap<String, Integer> death = new HashMap<>();

	public static void setScore(Player player, boolean bonus)
	{
		String name = player.getName();
		if(!score.containsKey(name))
		{
			score.put(name, 0.1);
			return;
		}
		Double playerscore = score.get(name);
		if(bonus)
		{
			score.put(name, playerscore + 0.2);
			return;
		}
		score.put(name, playerscore + 0.1);
	}

	public static void setWinnerScore(Player player)
	{
		String name = player.getName();
		if(!score.containsKey(name))
			score.put(name, 30.0);
		Double playerscore = score.get(name);
		score.put(name, playerscore + 30.0);
	}

	public static void resetAllPlayerScore(List<Player> playerlist)
	{
		for(Player player : playerlist)
		{
			String name = player.getName();
			if(score.containsKey(name))
				score.remove(name);
		}
	}

	public static int getScores(Player player)
	{
		DecimalFormat df = new DecimalFormat("###0");
		return Integer.valueOf(df.format(score.get(player.getName())));
	}

	public static void ShowScores(List<Player> playerlist)
	{
		DecimalFormat df = new DecimalFormat("###0");
		for(Player player : playerlist)
		{
			player.sendMessage(ChatColor.AQUA +"------"+ ChatColor.YELLOW +"[ "+ ChatColor.GREEN +"SplatScores"+ ChatColor.YELLOW +" ]"+ ChatColor.AQUA + "------");
			player.sendMessage("         [ "+ ColorSelect.color_team1_prefix(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()) +"Team1"+ ChatColor.WHITE +" ]");
			for(Player players : TurfBattle.TeamPlayersList(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString(), 1))
			{
				SplatPlayerStatus status = new SplatPlayerStatus(players.getName());
				player.sendMessage("Rank " + status.getRank() + Metrics.setSpace2(String.valueOf(status.getRank()))+ " : " + ColorSelect.color_team1_prefix(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString())
						+ players.getName() + ChatColor.WHITE + Metrics.setSpace(players.getName()) + ChatColor.GREEN +": " + ChatColor.WHITE + df.format(score.get(players.getName())));
			}
			player.sendMessage(ChatColor.AQUA +"--------------------------");
			player.sendMessage("         [ "+ ColorSelect.color_team2_prefix(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()) +"Team2"+ ChatColor.WHITE +" ]");
			for(Player players : TurfBattle.TeamPlayersList(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString(), 2))
			{
				SplatPlayerStatus status = new SplatPlayerStatus(players.getName());
				player.sendMessage("Rank " + status.getRank() + Metrics.setSpace2(String.valueOf(status.getRank()))+ " : " + ColorSelect.color_team2_prefix(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString())
						+ players.getName() + ChatColor.WHITE + Metrics.setSpace(players.getName()) + ChatColor.GREEN +": " + ChatColor.WHITE + df.format(score.get(players.getName())));
			}
			player.sendMessage(ChatColor.AQUA +"--------------------------");
			SplatPlayerStatus rate = new SplatPlayerStatus(player.getName());
			if(rate.setStatus(SplatoonScores.getScores(player)))
			{
				player.sendMessage(Splatoon.data.Pprefix);
				player.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
				player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "       Rank UP!");
				player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "      You rank " + rate.getRank());
				player.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			}
		}
		resetAllPlayerScore(playerlist);
	}
}
