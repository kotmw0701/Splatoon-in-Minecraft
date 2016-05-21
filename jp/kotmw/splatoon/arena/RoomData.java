/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.arena;

import java.util.List;

import jp.kotmw.splatoon.SplatoonFiles;
import jp.kotmw.splatoon.battle.BattleTypeEnum;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class RoomData
{
	String name;
	FileConfiguration roomyaml = new YamlConfiguration();

	public RoomData(String name)
	{
		roomyaml = YamlConfiguration.loadConfiguration(SplatoonFiles.RoomDirFiles(name));
		this.name = name;
	}

	public World getWorld()
	{
		return Bukkit.getWorld(roomyaml.getString(name + ".World"));
	}

	public Location getLocation()
	{
		int x = roomyaml.getInt(name + ".x");
		int y = roomyaml.getInt(name + ".y");
		int z = roomyaml.getInt(name + ".z");
		return new Location(getWorld(), x,y,z);
	}

	public BattleTypeEnum getBattleType()
	{
		BattleTypeEnum type = BattleTypeEnum.Turf_War;
		for(BattleTypeEnum types : BattleTypeEnum.values())
		{
			if(types.toString().equalsIgnoreCase(roomyaml.getString(name + ".BattleType")))
			{
				type = types;
				break;
			}
		}
		return type;
	}

	public List<String> getSelectArenaList()
	{
		return roomyaml.getStringList(name + ".SelectArenas");
	}

	public int getMinPlayers()
	{
		return roomyaml.getInt(name + ".MinPlayers");
	}

	public int getMaxPlayers()
	{
		return roomyaml.getInt(name + ".MaxPlayers");
	}
}
