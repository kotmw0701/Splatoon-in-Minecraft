/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.arena;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.kotmw.splatoon.Metrics;
import jp.kotmw.splatoon.SplatoonFiles;
import jp.kotmw.splatoon.battle.BattleTypeEnum;
import jp.kotmw.splatoon.battle.TurfBattle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class ArenaStatus
{
	private Metrics data = new Metrics();
	public static Map<String, ArenaStatusEnum> arenastatus = new HashMap<>();

	int x1, y1, z1, x2, y2, z2;

	public void SetStatus()
	{
		for(String name : SplatoonFiles.getArenaList())
		{
			if(arenastatus.containsKey(name))
			{
				continue;
			} else if(!arenastatus.containsKey(name)) {
				if(isCanUse(name))
					arenastatus.put(name, ArenaStatusEnum.ENABLED);
				else
					arenastatus.put(name, ArenaStatusEnum.DISABLED);
				if(!ArenaSettings.SetupFinish(name))
					arenastatus.put(name, ArenaStatusEnum.DISABLED);
			}
		}
	}

	public static boolean isCanUse(String arena)
	{
		ArenaData arenadata = ArenaSettings.getArenaData(arena);
		return arenadata.getStatus();
	}

	public static ArenaStatusEnum getArenaStatus(String arena)
	{
		return ArenaStatus.arenastatus.get(arena);
	}

	/**
	 * ステージとロビーのリストを出力する
	 *
	 */
	public void OutputArenaLists()
	{
		Bukkit.getConsoleSender().sendMessage(data.LoggerPprefix + ChatColor.YELLOW + "[ " +ChatColor.RED + "Arenalist" +ChatColor.YELLOW+ " ]");
		for(String arena : SplatoonFiles.getArenaList())
		{
			if(ArenaSettings.SetupFinish(arena))
				Bukkit.getConsoleSender().sendMessage(data.LoggerPprefix + ChatColor.GREEN +"- "+ arena);
			else if(!ArenaSettings.SetupFinish(arena))
			Bukkit.getConsoleSender().sendMessage(data.LoggerPprefix + ChatColor.GREEN +"- "+ ChatColor.RED + arena + "     Setup is not finished !");
		}
		Bukkit.getConsoleSender().sendMessage(data.LoggerPprefix + ChatColor.YELLOW + "[ " +ChatColor.AQUA + "Roomlist" +ChatColor.YELLOW+ " ]");
		for(String room : SplatoonFiles.getRoomList())
		{
			Bukkit.getConsoleSender().sendMessage(data.LoggerPprefix + ChatColor.GREEN +"- "+ room);
		}
	}

	public String RandomSelect(String room)
	{
		RoomData roomdata = new RoomData(room);
		List<String> list = roomdata.getSelectArenaList();
		Collections.shuffle(list);
		for(String arenas : list)
		{
			if(arenastatus.get(arenas).equals(ArenaStatusEnum.INGAME)
					|| arenastatus.get(arenas).equals(ArenaStatusEnum.DISABLED))
				continue;
			return arenas;
		}
		return null;
	}

	/**
	 * 同じ名前のステージが存在しないかのチェック
	 *
	 * @param arena ステージ名
	 *
	 * @return ある場合はfalse、無い場合はtrue
	 */
	public boolean CheckArena(String name, boolean arena)
	{
		if(arena)
		{
			for(String arenaname : SplatoonFiles.getArenaList())
			{
				if(data.DebugMode)
				{
					System.out.println("Arena " + arenaname);
					System.out.println(name);
				}
				if(arenaname == null)
					break;
				if(arenaname.equalsIgnoreCase(name))
					return false;
			}
		} else {
			for(String roomname : SplatoonFiles.getRoomList())
			{
				if(data.DebugMode)
				{
					System.out.println("Room " + roomname);
					System.out.println(name);
				}
				if(roomname == null)
					break;
				if(roomname.equalsIgnoreCase(name))
					return false;
			}
		}
		return true;
	}

	public void SaveGachiAreaCount()
	{
		for(String arena : SplatoonFiles.getArenaList())
		{
			if(TurfBattle.gachiarea.containsKey(arena))
				continue;
			ArenaData arenadata = ArenaSettings.getArenaData(arena);
			int x1 = arenadata.getAreaLocation(1).getBlockX();
			int x2 = arenadata.getAreaLocation(2).getBlockX();
			int y = arenadata.getAreaLocation(2).getBlockY();
			int z1 = arenadata.getAreaLocation(1).getBlockZ();
			int z2 = arenadata.getAreaLocation(2).getBlockZ();
			if((x1 + x2 + y + z1 + z2) == 0)
			{
				TurfBattle.gachiarea.put(arena, 1);
				continue;
			}
			for (int xPoint = x2; xPoint <= x1; xPoint++)
			{
				for (int zPoint = z2; zPoint <= z1; zPoint++)
				{
					Block CheckBlock = arenadata.getWorld().getBlockAt(xPoint, y, zPoint);

					if(CheckBlock.getType() != Material.AIR)
					{
						if(CheckBlock.getType() == Material.WOOL)
						{
							setAllArea(arena);
						}
						else if(CheckBlock.getType() == Material.GLASS
								|| CheckBlock.getType() == Material.THIN_GLASS
								|| CheckBlock.getType() == Material.HARD_CLAY
								|| CheckBlock.getType() == Material.STAINED_CLAY
								|| CheckBlock.getType() == Material.STAINED_GLASS
								|| CheckBlock.getType() == Material.STAINED_GLASS_PANE
								|| CheckBlock.getType() == Material.CARPET)
						{
							setAllArea(arena);
						}
					}
				}
			}
		}
	}

	private static void setAllArea(String arena)
	{
		if(!TurfBattle.gachiarea.containsKey(arena))
		{
			TurfBattle.gachiarea.put(arena, 1);
			return;
		}
		int i = TurfBattle.gachiarea.get(arena);
		i += 1;
		TurfBattle.gachiarea.put(arena, i);
	}

	public static BattleTypeEnum getArenaType(String arena)
	{
		if(ArenaStatus.arenastatus.containsKey(arena)
				&& ArenaStatus.arenastatus.get(arena).equals(ArenaStatusEnum.INGAME))
			if(TurfBattle.type.containsKey(arena))
				return TurfBattle.type.get(arena);
		return null;
	}
}
