/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.arena;

import jp.kotmw.splatoon.SplatoonFiles;
import jp.kotmw.splatoon.util.Location;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ArenaFile
{
	String name;
	FileConfiguration arenayaml = new YamlConfiguration();

	public ArenaFile(String name)
	{
		arenayaml = YamlConfiguration.loadConfiguration(SplatoonFiles.ArenaDirFiles(name));
		this.name = name;
	}

	public boolean getstatus()
	{
		return arenayaml.getBoolean(name + ".Status");
	}

	public String getWorld()
	{
		return arenayaml.getString(name + ".World");
	}

	public Location getLocation(int i)
	{
		int x = arenayaml.getInt(name + ".Point"+i+".x");
		int y = arenayaml.getInt(name + ".Point"+i+".y");
		int z = arenayaml.getInt(name + ".Point"+i+".z");
		return new Location(getWorld(), x, y, z, 0, 0);
	}

	public Location getAreaLocation(int i)
	{
		int x = arenayaml.getInt(name + ".Area.Point"+i+".x");
		int y = arenayaml.getInt(name + ".Area.Point"+i+".y");
		int z = arenayaml.getInt(name + ".Area.Point"+i+".z");
		return new Location(getWorld(), x, y, z, 0, 0);
	}

	public Location getTeam(int team,int position)
	{
		double x = arenayaml.getDouble(name + ".Joinloc.team"+team+".p"+position+".x");
		double y = arenayaml.getDouble(name + ".Joinloc.team"+team+".p"+position+".y");
		double z = arenayaml.getDouble(name + ".Joinloc.team"+team+".p"+position+".z");
		return new Location(getWorld(), x, y, z, 0, 0);
	}

	public String getAuthor()
	{
		return arenayaml.getString(name +".Information.Author");
	}

	public int getTotal()
	{
		return arenayaml.getInt(name +".Information.Total_Area");
	}

	public String getDescription()
	{
		return arenayaml.getString(name +".Information.Description");
	}
}
