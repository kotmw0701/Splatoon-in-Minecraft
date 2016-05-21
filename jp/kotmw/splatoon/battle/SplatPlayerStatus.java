/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.battle;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.kotmw.splatoon.Metrics;
import jp.kotmw.splatoon.Splatoon;
import jp.kotmw.splatoon.SplatoonFiles;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SplatPlayerStatus
{
	String name;
	public static FileConfiguration playerfile = new YamlConfiguration();
	private int win = 0, lose = 0, maxwinstreak = 0, winstreak = 0;
	private boolean finalwin = true;

	public SplatPlayerStatus(String name)
	{
		playerfile = YamlConfiguration.loadConfiguration(SplatoonFiles.PlayerDirFiles(name));
		this.name = name;
	}

	public int getWinCount()
	{
		return playerfile.getInt(name + ".Rate.Win");
	}

	public int getLoseCount()
	{
		return playerfile.getInt(name + ".Rate.Lose");
	}

	public int getWinStreak()
	{
		return playerfile.getInt(name + ".Rate.WinStreak");
	}

	public int getMaximumWinStreak()
	{
		return playerfile.getInt(name + ".Rate.MaximumWinStreak");
	}

	public boolean getfinalwin()
	{
		return playerfile.getBoolean(name + ".Rate.FinalWin");
	}

	public int getExp()
	{
		return playerfile.getInt(name + ".Status.Exp");
	}

	public int getTotal()
	{
		return playerfile.getInt(name + ".Status.Total");
	}

	public int getRank()
	{
		return playerfile.getInt(name + ".Status.Rank");
	}

	public Udemae getUdemae()
	{
		return Udemae.valueOf(playerfile.getString(name + ".Status.Udemae"));
	}

	public List<String> getFriendList()
	{
		return playerfile.getStringList(name + ".Friends");
	}

	public List<String> getInvitingList()
	{
		return playerfile.getStringList(name + ".Invites");
	}

	public String Winning_percentage()
	{
		int win = getWinCount();
		int total = win + getLoseCount();
		float parcentage = (float) win / total;
		if(Splatoon.data.DebugMode)
		{
			System.out.println(win);
			System.out.println(total);
			System.out.println(parcentage);
		}
		DecimalFormat df = new DecimalFormat("##0.00%");
		return df.format(parcentage);
	}

	private void saveRate()
	{
		playerfile.set(name + ".Rate.Win", win);
		playerfile.set(name + ".Rate.Lose", lose);
		playerfile.set(name + ".Rate.FinalWin", finalwin);
		playerfile.set(name + ".Rate.WinStreak", winstreak);
		playerfile.set(name + ".Rate.MaximumWinStreak", maxwinstreak);
		Splatoon.instance.SettingFiles(playerfile, SplatoonFiles.PlayerDirFiles(name), true);
	}

	private void saveStatus(int rank, int point, int total)
	{
		playerfile.set(name + ".Status.Rank" , rank);
		playerfile.set(name + ".Status.Exp", point);
		playerfile.set(name + ".Status.Total", total);
		Splatoon.instance.SettingFiles(playerfile, SplatoonFiles.PlayerDirFiles(name), true);
	}

	private void saveFirends(List<String> list)
	{
		playerfile.set(name + ".Friends", list);
		Splatoon.instance.SettingFiles(playerfile, SplatoonFiles.PlayerDirFiles(name), true);
	}

	private void saveInvitings(List<String> list)
	{
		playerfile.set(name + ".Invites", list);
		Splatoon.instance.SettingFiles(playerfile, SplatoonFiles.PlayerDirFiles(name), true);
	}

	public void setRate(boolean battle)
	{
		win = getWinCount();
		lose = getLoseCount();
		winstreak = getWinStreak();
		maxwinstreak = getMaximumWinStreak();
		finalwin = getfinalwin();
		if(battle)
		{
			win = win + 1;
			if(finalwin)
			{
				winstreak = winstreak + 1;
				if(winstreak > maxwinstreak)
					maxwinstreak = maxwinstreak + 1;
			}
			finalwin = true;
		}
		else
		{
			lose = lose + 1;
			if(finalwin)
			{
				finalwin = false;
			}
			winstreak = 0;
		}
		saveRate();
	}

	public boolean setStatus(int point)
	{
		int exp = getExp() + point;
		int rank = getRank();
		int total = getTotal() + point;
		for(int i = 18; i >= 0; i--)
		{
			if(exp > Metrics.rank[i])
			{
				if((i+2) <= rank)
					continue;
				rank = 2 + i;
				exp = exp - Metrics.rank[i];
				break;
			}
		}
		boolean rankup = rank > getRank();
		saveStatus(rank, exp, total);
		return rankup;
	}

	public void setFriendList(String player)
	{
		List<String> list = getFriendList();
		list.add(player);
		saveFirends(list);
	}

	public void setInvitingList(String player)
	{
		List<String> list = getInvitingList();
		list.add(player);
		saveInvitings(list);
	}

	public void removeInvitingList(String player)
	{
		List<String> list = new ArrayList<>();
		for(String inviting : getInvitingList())
		{
			if(inviting.equals(player))
				continue;
			list.add(inviting);
		}
		saveInvitings(list);
	}

	public boolean setFriendAccept(String player)
	{
		for(String inviting : getInvitingList())
		{
			if(inviting.equals(player))
			{
				removeInvitingList(player);
				setFriendList(player);
				return true;
			}
		}
		return false;
	}

	/*private int setPoint(int point, boolean win)
	{
		int point_i = 0;
		int point_ii = point;
		while(point_ii > 200)
		{
			point_i = point_i + 1;
			point_ii = point_ii - 200;
		}
		if(win)
			point_i = point_i + 3;
		return point_i;
	}*/

	public static List<String> Ranking(String path)
	{
		List<String> list = new ArrayList<>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		int i = 1;
		for(String players : SplatoonFiles.getPlayersList())
		{
			playerfile = YamlConfiguration.loadConfiguration(SplatoonFiles.PlayerDirFiles(players));
			map.put(players, playerfile.getInt(players + path));
		}
		List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(
					Entry<String, Integer> entry1, Entry<String, Integer> entry2) {
				return ((Integer)entry2.getValue().compareTo((Integer)entry1.getValue()));
			}
		});
		for(Entry<String, Integer> top : entries)
		{
			if(i >= 10)
				break;
			String msg = ChatColor.AQUA + top.getKey() + Metrics.setSpace(top.getKey()) +": " + ChatColor.WHITE + top.getValue();
			list.add(msg);
			i++;
		}
		return list;
	}

	public enum Udemae
	{
		S_plus("S+"),
		S("S"),
		A_plus("A+"),
		A("A"),
		A_minus("A-"),
		B_plus("B+"),
		B("B"),
		B_minus("B-"),
		C_plus("C+"),
		C("C"),
		C_minus("C-");

		private String type;

		Udemae(final String type)
		{
			this.type = type;
		}

		String getType()
		{
			return type;
		}
	}
}
