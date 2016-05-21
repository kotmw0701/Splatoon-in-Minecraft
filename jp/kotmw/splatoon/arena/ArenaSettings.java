/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.arena;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.kotmw.splatoon.Splatoon;
import jp.kotmw.splatoon.SplatoonFiles;
import jp.kotmw.splatoon.battle.BattleTypeEnum;
import jp.kotmw.splatoon.battle.TurfBattle;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ArenaSettings
{

	public static Map<String, ArenaData> arenadata = new HashMap<>();

	/**
	 * ステージの範囲を保存
	 *
	 * @param player コマンドを実行したプレイヤー
	 * @param arena ステージ名
	 * @param world ステージが存在するワールド名
	 * @param x1b 最初のx
	 * @param y1b 最初のy
	 * @param z1b 最初のz
	 * @param x2b 最後のx
	 * @param y2b 最後のy
	 * @param z2b 最後のz
	 *
	 */
	public static boolean SaveArenaSetting(Player player, String arena, String world, int x1b, int x2b, int y1b, int y2b, int z1b, int z2b)
	{
		Splatoon.files.fileconfig = new YamlConfiguration();
		int x1 = x1b, x2 = x2b;
		if(x1b < x2b)//大きいほうの値を最初のx座標にする
		{
			x1 = x2b;
			x2 = x1b;
		}
		int y1 = y1b, y2 = y2b;
		if(y1b < y2b)//大きいほうの値を最初のy座標にする
		{
			y1 = y2b;
			y2 = y1b;
		}
		int z1 = z1b, z2 = z2b;
		if(z1b < z2b)//大きいほうの値を最初のz座標にする
		{
			z1 = z2b;
			z2 = z1b;
		}
		Splatoon.files.fileconfig.set(arena +".Status", false);
		Splatoon.files.fileconfig.set(arena +".World", world);
		Splatoon.files.fileconfig.set(arena +".Point1.x", x1);
		Splatoon.files.fileconfig.set(arena +".Point1.y", y1);
		Splatoon.files.fileconfig.set(arena +".Point1.z", z1);
		Splatoon.files.fileconfig.set(arena +".Point2.x", x2);
		Splatoon.files.fileconfig.set(arena +".Point2.y", y2);
		Splatoon.files.fileconfig.set(arena +".Point2.z", z2);
		Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.ArenaDirFiles(arena), true);
		TurfBattle.CheckAllArea(arena);
		if(TurfBattle.allarea.get(arena) == null)
		{
			player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "ERROR!!" + ChatColor.YELLOW + " 総面積が計算できません");
			return false;
		}
		DecimalFormat df = new DecimalFormat("#####0");
		Splatoon.files.fileconfig.set(arena +".Information.Total_Area", Integer.valueOf(df.format(TurfBattle.allarea.get(arena))));
		player.sendMessage(Splatoon.data.Pprefix + "ステージ総面積 " + ChatColor.YELLOW + df.format(TurfBattle.allarea.get(arena)));
		Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.ArenaDirFiles(arena), true);
		ArenaSettings.arenadata.put(arena, new ArenaData(new ArenaFile(arena)));
		TurfBattle.allarea.remove(arena);
		return true;
	}

	/**
	 * ガチエリアのエリアを設定する
	 *
	 *
	 */
	public static void SaveArea(Player player, String arena, int x1b, int x2b, int y1b, int y2b, int z1b, int z2b)
	{
		Splatoon.files.fileconfig = new YamlConfiguration();
		Splatoon.files.fileconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.ArenaDirFiles(arena));
		int x1 = x1b, x2 = x2b;
		if(x1b < x2b)//大きいほうの値を最初のx座標にする
		{
			x1 = x2b;
			x2 = x1b;
		}
		int y1 = y1b, y2 = y2b;
		int z1 = z1b, z2 = z2b;
		if(z1b < z2b)//大きいほうの値を最初のz座標にする
		{
			z1 = z2b;
			z2 = z1b;
		}
		Splatoon.files.fileconfig.set(arena +".Area.Point1.x", x1);
		Splatoon.files.fileconfig.set(arena +".Area.Point1.y", y1);
		Splatoon.files.fileconfig.set(arena +".Area.Point1.z", z1);
		Splatoon.files.fileconfig.set(arena +".Area.Point2.x", x2);
		Splatoon.files.fileconfig.set(arena +".Area.Point2.y", y2);
		Splatoon.files.fileconfig.set(arena +".Area.Point2.z", z2);
		Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.ArenaDirFiles(arena), true);
	}

	/**
	 * ステージの登録が終わっているかどうかをチェックする
	 *
	 * @param arena 調べるステージ名
	 *
	 * @return 終わっていればtrue、終わっていなければfalse
	 */
	public static boolean SetupFinish(String arena)
	{
		if(Splatoon.arenastatus.CheckArena(arena, true))
			return false;
		boolean setup = true;//初期値がtrueってのもちょっと怖い・・・
		ArenaData arenadata = ArenaSettings.getArenaData(arena);
		for(int i = 1; i <= 4; i++)
		{
			double team1_y = arenadata.getPlayerLocation(1, i).getY();
			double team2_y = arenadata.getPlayerLocation(2, i).getY();
			if((team1_y == 0.0) || (team2_y == 0.0))
			{
				setup = false;
				break;
			}
		}
		return setup;
	}


	/**
	 * 待機ロビーの保存
	 *
	 * @param player コマンドを実行したプレイヤー
	 * @param room 待ち部屋名
	 * @param world 待ち部屋が存在するワールド名
	 * @param x テレポートしてくるx座標
	 * @param y テレポートしてくるy座標
	 * @param z テレポートしてくるz座標
	 * @param type バトルタイプ
	 *
	 */
	public static void SaveRoomLocation(Player player, String room ,String world, int x, int y, int z, BattleTypeEnum type)
	{
		Splatoon.files.fileconfig = new YamlConfiguration();
		Splatoon.files.fileconfig.set(room +".World", world);
		Splatoon.files.fileconfig.set(room +".x", x);
		Splatoon.files.fileconfig.set(room +".y", y);
		Splatoon.files.fileconfig.set(room +".z", z);
		Splatoon.files.fileconfig.set(room +".BattleType", type.toString());
		Splatoon.files.fileconfig.set(room +".SelectArenas", SplatoonFiles.getArenaList());
		Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.RoomDirFiles(room), true);
	}

	/**
	 * ステージにTPした際のプレイヤーの座標
	 *
	 * @param arena ステージ名
	 * @param locnum 何人目
	 * @param team1 チーム1かどうか
	 * @param x テレポートするx
	 * @param y テレポートするy
	 * @param z テレポートするz
	 */
	public static void SaveJoinLocation(String arena, int locnum, boolean team1, Double x, Double y, Double z)
	{
		Splatoon.files.fileconfig = new YamlConfiguration();
		Splatoon.files.fileconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.ArenaDirFiles(arena));
		String playernum = "p" + locnum;
		if(team1)
		{
			Splatoon.files.fileconfig.set(arena +".Joinloc.team1."+ playernum +".x" , x);
			Splatoon.files.fileconfig.set(arena +".Joinloc.team1."+ playernum +".y" , y);
			Splatoon.files.fileconfig.set(arena +".Joinloc.team1."+ playernum +".z" , z);
			Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.ArenaDirFiles(arena), true);
		}
		else if(!team1)
		{
			Splatoon.files.fileconfig.set(arena +".Joinloc.team2."+ playernum +".x" , x);
			Splatoon.files.fileconfig.set(arena +".Joinloc.team2."+ playernum +".y" , y);
			Splatoon.files.fileconfig.set(arena +".Joinloc.team2."+ playernum +".z" , z);
			Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.ArenaDirFiles(arena), true);
		}
	}

	public static void SaveLobby(String world, int x, int y, int z)
	{
		Splatoon.files.lobbyconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.lobbyfile);
		Splatoon.files.lobbyconfig.set("Lobby.World", world);
		Splatoon.files.lobbyconfig.set("Lobby.x", x);
		Splatoon.files.lobbyconfig.set("Lobby.y", y);
		Splatoon.files.lobbyconfig.set("Lobby.z", z);
		Splatoon.instance.SettingFiles(Splatoon.files.lobbyconfig, SplatoonFiles.lobbyfile, true);
	}

	public static void SelectArena(String room, String arena, boolean add)
	{
		Splatoon.files.fileconfig = new YamlConfiguration();
		Splatoon.files.fileconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.RoomDirFiles(room));
		if(add)
		{
			List<String> arenalist = Splatoon.files.fileconfig.getStringList(room +".SelectArenas");
			arenalist.add(arena);
			Splatoon.files.fileconfig.set(room +".SelectArenas", arenalist);
		}
		else
		{
			List<String> arenalist = new ArrayList<>();
			for(String arenas : Splatoon.files.fileconfig.getStringList(room +".SelectArenas"))
			{
				if(arenas.equalsIgnoreCase(arena))
					continue;
				arenalist.add(arenas);
			}
			Splatoon.files.fileconfig.set(room +".SelectArenas", arenalist);
		}
		Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.RoomDirFiles(room), true);
	}

	public static void SaveAllArea(String arena)
	{
		Splatoon.files.fileconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.ArenaDirFiles(arena));
		TurfBattle.CheckAllArea(arena);
		if(TurfBattle.allarea.get(arena) == null)
			return;
		DecimalFormat df = new DecimalFormat("#####0");
		Splatoon.files.fileconfig.set(arena +".Information.Total_Area", Integer.valueOf(df.format(TurfBattle.allarea.get(arena))));
		Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.ArenaDirFiles(arena), true);
		TurfBattle.allarea.remove(arena);
	}

	public static void ReloadAllArena()
	{
		for(String arena : SplatoonFiles.getArenaList())
		{
			ArenaSettings.arenadata.put(arena, new ArenaData(new ArenaFile(arena)));
		}
	}

	public static boolean ReloadArena(String arena)
	{
		if(SplatoonFiles.getArenaList().contains(arena))
		{
			ArenaSettings.arenadata.put(arena, new ArenaData(new ArenaFile(arena)));
			return true;
		}
		return false;
	}

	public static ArenaData getArenaData(String arena)
	{
		if(!ArenaSettings.arenadata.containsKey(arena))
			ArenaSettings.arenadata.put(arena, new ArenaData(new ArenaFile(arena)));
		return ArenaSettings.arenadata.get(arena);
	}
}
