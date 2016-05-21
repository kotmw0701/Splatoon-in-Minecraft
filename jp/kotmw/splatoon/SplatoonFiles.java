/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import jp.kotmw.splatoon.arena.ArenaLogs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class SplatoonFiles
{
	public static File config = new File(Splatoon.instance.filepath + "Config.yml");
	public static File arenafile = new File(Splatoon.instance.filepath + "Arena.yml");
	public static File ratefile = new File(Splatoon.instance.filepath + "Rate.yml");
	public static File lobbyfile = new File(Splatoon.instance.filepath + "Lobby.yml");
	public static File playersdir = new File(Splatoon.instance.filepath + "Players");
	public static File arenadir = new File(Splatoon.instance.filepath + "Arenas");
	public static File roomdir = new File(Splatoon.instance.filepath + "Rooms");
	public static File battlelogs = new File(Splatoon.instance.filepath + "BattleLog");
	public static File signsdir = new File(Splatoon.instance.filepath + "Arenas" + Splatoon.instance.separator + "Signs");
	public static File weapondir = new File(Splatoon.instance.filepath + "Weapons");
	public static File subdir = new File(Splatoon.instance.filepath + "Weapons" + Splatoon.instance.separator + "Sub");
	public static File specialdir = new File(Splatoon.instance.filepath + "Weapons" + Splatoon.instance.separator + "Special");
	public FileConfiguration fileconfig = new YamlConfiguration();
	public FileConfiguration arenaconfig = new YamlConfiguration();
	public FileConfiguration signconfig = new YamlConfiguration();
	public FileConfiguration rateconfig = new YamlConfiguration();
	public FileConfiguration lobbyconfig = new YamlConfiguration();


	public static File ArenaDirFiles(String name)
	{
		File file = new File(Splatoon.instance.filepath + "Arenas" + File.separator + name +".yml");
		return file;
	}

	public static File RoomDirFiles(String name)
	{
		File file = new File(Splatoon.instance.filepath + "Rooms" + File.separator + name +".yml");
		return file;
	}

	public static File SignDirFiles(String name)
	{
		File file = new File(Splatoon.instance.filepath + "Arenas" + File.separator + "Signs" + File.separator + name +".yml");
		return file;
	}

	public static File PlayerDirFiles(String name)
	{
		File file = new File(Splatoon.instance.filepath + "Players" + File.separator + name +".yml");
		return file;
	}

	public static String getName(String name)
	{
		if (name == null)
			return null;
		int point = name.lastIndexOf(".");
		if (point != -1)
			return name.substring(0, point);
		return name;
	}

	public static List<String> getArenaList()
	{
		List<String> names = new ArrayList<>();
		for(File file : Arrays.asList(arenadir.listFiles()))
		{
			if(file.isDirectory())
				continue;
			names.add(getName(file.getName()));
		}
		return names;
	}

	public static List<String> getRoomList()
	{
		List<String> names = new ArrayList<>();
		for(File file : Arrays.asList(roomdir.listFiles()))
		{
			if(file.isDirectory())
				continue;
			names.add(getName(file.getName()));
		}
		return names;
	}

	public static List<String> getSignsList()
	{
		List<String> names = new ArrayList<>();
		for(File file : Arrays.asList(signsdir.listFiles()))
		{
			if(file.isDirectory())
				continue;
			names.add(getName(file.getName()));
		}
		return names;
	}

	public static List<String> getPlayersList()
	{
		List<String> names = new ArrayList<>();
		for(File file : Arrays.asList(playersdir.listFiles()))
		{
			if(file.isDirectory())
				continue;
			names.add(getName(file.getName()));
		}
		return names;
	}

	public static void CreateReport()
	{
		Calendar cal = new GregorianCalendar();
		String date = (cal.get(Calendar.MONTH) + 1)
				+ "-" + cal.get(Calendar.DAY_OF_MONTH)
				+ "-" + cal.get(Calendar.YEAR)
				+ "_" + cal.get(Calendar.HOUR_OF_DAY)
				+ "-" + cal.get(Calendar.MINUTE)
				+ "-" + cal.get(Calendar.SECOND);
		File reportfile = new File(Splatoon.instance.filepath + "report-" + date + ".txt");
		String separator = "\r\n";
		try {
			reportfile.createNewFile();
		} catch (IOException e) {
			System.out.println("ファイルが生成できません");
			System.out.println(e);
		}
		try {
			FileWriter writer = new FileWriter(reportfile);
			writer.write("Java Version : " + System.getProperty("java.version") + separator);
			writer.write("Server OS : " + System.getProperty("os.name") + separator);
			writer.write(separator);
			writer.write("Bukkit Version : " + Bukkit.getServer().getVersion() + separator);
			writer.write("---Other Plugins---" + separator);
			for(Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins())
				writer.write(plugin.getName() + " (" + plugin.getDescription().getVersion() + ")" + separator);
			writer.close();
		} catch (IOException e) {
			System.out.println("レポートファイルの作成が出来ませんでした");
			System.out.println(e);
		}
	}

	public static void BattleLogs(String arena, ArenaLogs log)
	{
		Calendar cal = new GregorianCalendar();
		String date = cal.get(Calendar.YEAR)
				+ "-" + (cal.get(Calendar.MONTH) + 1)
				+ "-" + cal.get(Calendar.DAY_OF_MONTH)
				+ "_" + cal.get(Calendar.HOUR_OF_DAY)
				+ "-" + cal.get(Calendar.MINUTE)
				+ "-" + cal.get(Calendar.SECOND);
		File file = new File(Splatoon.instance.filepath + "BattleLog" + Splatoon.instance.separator + date + ".log");
		String separator = "\r\n";
		try {
			file.createNewFile();
		} catch (IOException e) {
			System.out.println("ログファイルが生成できませんでした");
			System.out.println(e);
		}
		try {
			FileWriter writer = new FileWriter(file);
			for(String logtext : LogTexts(log))
				writer.write(logtext + separator);
			writer.close();
		} catch (IOException e) {
			System.out.println("書き込みが出来ませんでした");
			System.out.println(e);
		}
	}

	private static List<String> LogTexts(ArenaLogs log)
	{
		List<String> temp = new ArrayList<>();
		temp.add("Start : " + log.getStartDate());
		temp.add("End : " + log.getEndDate());
		for(String text : log.getResult())
			temp.add(text);
		temp.add("Winner : " + log.getWinnerTeam());
		temp.add("-----Room&Arenas-----");
		temp.add("Room : " + log.getRoom());
		temp.add("Arena : " + log.getArena());
		temp.add("-----Players-----");
		for(String players : log.getPlayers())
			temp.add(players);
		temp.add("---------------");
		for(String logtext : log.getLogs())
			temp.add(logtext);
		return temp;
	}
}
