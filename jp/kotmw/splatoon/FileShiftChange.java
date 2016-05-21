/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon;

import jp.kotmw.splatoon.battle.BattleTypeEnum;
import jp.kotmw.splatoon.battle.SplatPlayerStatus;
import jp.kotmw.splatoon.battle.SplatRate;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileShiftChange
{

	/*public static void CheckArenaYAML()
	{
		if((SplatoonFiles.arenafile.exists())
				&& (!SplatoonFiles.arenadir.exists())
				&& (!SplatoonFiles.lobbyfile.exists()))
		{
			Splatoon.files.arenaconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.arenafile);
			Bukkit.getConsoleSender().sendMessage(Splatoon.data.LoggerPprefix + ChatColor.GREEN + "Chenge now!");
			SplatoonFiles.arenadir.mkdir();
			Splatoon.files.lobbyconfig.set("Room.Rooms", Splatoon.files.arenaconfig.getStringList("Room.Rooms"));
			for(String arena : Splatoon.files.arenaconfig.getStringList("Arena.Arenas"))
				setArenaData(arena);
			for(String room : Splatoon.files.arenaconfig.getStringList("Room.Rooms"))
				setRoomData(room);
			setLobbyData();
			Splatoon.instance.SettingFiles(Splatoon.files.lobbyconfig, SplatoonFiles.lobbyfile, true);
			SplatoonFiles.arenafile.delete();
		}
	}

	private static void setArenaData(String arena)
	{
		Splatoon.files.fileconfig = new YamlConfiguration();
		Splatoon.files.fileconfig.set(arena +".Stats", Splatoon.files.arenaconfig.getBoolean("Arena."+ arena +".Stats"));
		Splatoon.files.fileconfig.set(arena +".World", Splatoon.files.arenaconfig.getString("Arena."+ arena +".World"));
		Splatoon.files.fileconfig.set(arena +".Point1.x", Splatoon.files.arenaconfig.getInt("Arena."+ arena +".Point1.x"));
		Splatoon.files.fileconfig.set(arena +".Point1.y", Splatoon.files.arenaconfig.getInt("Arena."+ arena +".Point1.y"));
		Splatoon.files.fileconfig.set(arena +".Point1.z", Splatoon.files.arenaconfig.getInt("Arena."+ arena +".Point1.z"));
		Splatoon.files.fileconfig.set(arena +".Point2.x", Splatoon.files.arenaconfig.getInt("Arena."+ arena +".Point2.x"));
		Splatoon.files.fileconfig.set(arena +".Point2.y", Splatoon.files.arenaconfig.getInt("Arena."+ arena +".Point2.y"));
		Splatoon.files.fileconfig.set(arena +".Point2.z", Splatoon.files.arenaconfig.getInt("Arena."+ arena +".Point2.z"));
		for(int i = 0; i <= 1; i++)
		{
			int x = i+1;
			for(int j = 0; j <= 3; j++)
			{
				int y = j+1;
				Splatoon.files.fileconfig.set(arena +".Joinloc.team"+ x +".p"+ y +".x", Splatoon.files.arenaconfig.getDouble("Arena."+ arena +".Joinloc.team"+ x +".p"+ y +".x"));
				Splatoon.files.fileconfig.set(arena +".Joinloc.team"+ x +".p"+ y +".y", Splatoon.files.arenaconfig.getDouble("Arena."+ arena +".Joinloc.team"+ x +".p"+ y +".y"));
				Splatoon.files.fileconfig.set(arena +".Joinloc.team"+ x +".p"+ y +".z", Splatoon.files.arenaconfig.getDouble("Arena."+ arena +".Joinloc.team"+ x +".p"+ y +".z"));
			}
		}
		Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.ArenaDirFiles(arena), true);
	}

	private static void setRoomData(String room)
	{
		Splatoon.files.fileconfig = new YamlConfiguration();
		Splatoon.files.fileconfig.set("Room."+ room +".World", Splatoon.files.arenaconfig.getString("Room."+ room +".World"));
		Splatoon.files.fileconfig.set("Room."+ room +".x", Splatoon.files.arenaconfig.getInt("Room."+ room +".x"));
		Splatoon.files.fileconfig.set("Room."+ room +".y", Splatoon.files.arenaconfig.getInt("Room."+ room +".y"));
		Splatoon.files.fileconfig.set("Room."+ room +".z", Splatoon.files.arenaconfig.getInt("Room."+ room +".z"));
		Splatoon.files.fileconfig.set("Room."+ room +".BattleType", BattleTypeEnum.TurfBattle.toString());
		Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.RoomDirFiles(room), true);
	}

	private static void setLobbyData()
	{
		Splatoon.files.lobbyconfig.set("Lobby.World", Splatoon.files.arenaconfig.getString("Lobby.World"));
		Splatoon.files.lobbyconfig.set("Lobby.x", Splatoon.files.arenaconfig.getInt("Lobby.x"));
		Splatoon.files.lobbyconfig.set("Lobby.y", Splatoon.files.arenaconfig.getInt("Lobby.y"));
		Splatoon.files.lobbyconfig.set("Lobby.z", Splatoon.files.arenaconfig.getInt("Lobby.z"));
	}*/

	public static void CheckPlayersDIR()
	{
		if(SplatoonFiles.ratefile.exists()
				&& !SplatoonFiles.playersdir.exists())
		{
			Splatoon.files.rateconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.ratefile);
			for(OfflinePlayer player : Bukkit.getOfflinePlayers())
			{
				SplatPlayerStatus.playerfile = new YamlConfiguration();
				String name = player.getName();
				if(SplatoonFiles.PlayerDirFiles(name).exists())
					continue;
				SplatRate rate = new SplatRate(name);
				int win = rate.getWinCount();
				int lose = rate.getLoseCount();
				int winstreak = rate.getWinStreak();
				int maxstreak = rate.getMaximumWinStreak();
				boolean finalwin = rate.getfinalwin();
				SplatPlayerStatus.playerfile.set(name + ".Rate.Win", win);
				SplatPlayerStatus.playerfile.set(name + ".Rate.Lose", lose);
				SplatPlayerStatus.playerfile.set(name + ".Rate.FinalWin", finalwin);
				SplatPlayerStatus.playerfile.set(name + ".Rate.WinStreak", winstreak);
				SplatPlayerStatus.playerfile.set(name + ".Rate.MaximumWinStreak", maxstreak);
				SplatPlayerStatus.playerfile.set(name + ".Status.Rank", 1);
				SplatPlayerStatus.playerfile.set(name + ".Status.Exp", 0);
				SplatPlayerStatus.playerfile.set(name + ".Status.Total", 0);
				Splatoon.instance.SettingFiles(SplatPlayerStatus.playerfile, SplatoonFiles.PlayerDirFiles(player.getName()), true);
			}
			SplatoonFiles.ratefile.delete();
		}
	}

	public static void CheckRoomsDIR()
	{
		if(SplatoonFiles.lobbyfile.exists()
				&& !SplatoonFiles.roomdir.exists())
		{
			Splatoon.files.lobbyconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.lobbyfile);
			for(String room : Splatoon.files.lobbyconfig.getStringList("Room.Rooms"))
			{
				Splatoon.files.fileconfig = new YamlConfiguration();
				Splatoon.files.fileconfig.set(room +".World", Splatoon.files.lobbyconfig.getString("Room."+ room +".World"));
				Splatoon.files.fileconfig.set(room +".x", Splatoon.files.lobbyconfig.getInt("Room."+ room +".x"));
				Splatoon.files.fileconfig.set(room +".y", Splatoon.files.lobbyconfig.getInt("Room."+ room +".y"));
				Splatoon.files.fileconfig.set(room +".z", Splatoon.files.lobbyconfig.getInt("Room."+ room +".z"));
				Splatoon.files.fileconfig.set(room +".BattleType", BattleTypeEnum.Turf_War.toString());
				Splatoon.files.fileconfig.set(room +".SelectArenas", SplatoonFiles.getArenaList());
				Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.RoomDirFiles(room), true);
			}
			String world = Splatoon.files.lobbyconfig.getString("Lobby.World");
			int x = Splatoon.files.lobbyconfig.getInt("Lobby.x");
			int y = Splatoon.files.lobbyconfig.getInt("Lobby.y");
			int z = Splatoon.files.lobbyconfig.getInt("Lobby.z");
			Splatoon.files.lobbyconfig = new YamlConfiguration();
			Splatoon.files.lobbyconfig.set("Lobby.World", world);
			Splatoon.files.lobbyconfig.set("Lobby.x", x);
			Splatoon.files.lobbyconfig.set("Lobby.y", y);
			Splatoon.files.lobbyconfig.set("Lobby.z", z);
			Splatoon.instance.SettingFiles(Splatoon.files.lobbyconfig, SplatoonFiles.lobbyfile, true);
		}
	}
}
