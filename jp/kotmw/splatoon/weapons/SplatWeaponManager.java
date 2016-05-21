/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.weapons;

import java.io.File;

import jp.kotmw.splatoon.SplatoonFiles;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SplatWeaponManager
{
	private String name;
	private Material item;
	private MainWeaponType type;
	private float cost;

	public SplatWeaponManager(File file)
	{
		FileConfiguration weapon = YamlConfiguration.loadConfiguration(file);
		name = weapon.getString(SplatoonFiles.getName(file.getName()) + ".ItemInfo.ItemName");
		item = Material.getMaterial(weapon.getString(SplatoonFiles.getName(file.getName()) + ".ItemInfo.ItemType"));
		cost = Float.valueOf(weapon.getString(SplatoonFiles.getName(file.getName()) + ".WeaponInfo.InkCost"));
	}

	public String getName()
	{
		return this.name;
	}

	public Material getItem()
	{
		return this.item;
	}

	public MainWeaponType getType()
	{
		return this.type;
	}

	public Float getCost()
	{
		return this.cost;
	}
}
