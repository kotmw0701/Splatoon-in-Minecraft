package jp.kotmw.splatoon.arena;

import java.util.HashMap;
import java.util.Map;

import jp.kotmw.splatoon.util.Location;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class ArenaData
{
	private String a;
	private boolean b;
	private String c;
	private Location d;
	private Location e;
	private Location f;
	private Location g;
	private Map<Integer,Location> h = new HashMap<>();
	private Map<Integer,Location> i = new HashMap<>();
	private String j;
	private int k;
	private String l;

	public ArenaData(ArenaFile file)
	{
		a = file.name;
		b = file.getstatus();
		c = file.getWorld();
		d = file.getLocation(1);
		e = file.getLocation(2);
		f = file.getAreaLocation(1);
		g = file.getAreaLocation(2);
		j = file.getAuthor();
		k = file.getTotal();
		l = file.getDescription();
		for(int ii = 1 ; ii <= 4 ; ii++)
		{
			h.put(ii, file.getTeam(1, ii));
			i.put(ii, file.getTeam(2, ii));
		}
	}

	public String getName()
	{
		return this.a;
	}

	public boolean getStatus()
	{
		return this.b;
	}

	public World getWorld()
	{
		return Bukkit.getWorld(this.c);
	}

	public org.bukkit.Location getLocation(int param)
	{
		if(param == 1)
			return d.getLocation();
		else if(param == 2)
			return e.getLocation();
		return d.getLocation();
	}

	public org.bukkit.Location getAreaLocation(int param)
	{
		if(param == 1)
			return f.getLocation();
		else if(param == 2)
			return g.getLocation();
		return f.getLocation();
	}

	public org.bukkit.Location getPlayerLocation(int param1, int param2)
	{
		if(param1 == 1)
			return h.get(param2).getLocation();
		else if(param1 == 2)
			return i.get(param2).getLocation();
		return null;
	}

	public String getAuthor()
	{
		return this.j;
	}

	public int getAllArea()
	{
		return this.k;
	}

	public String getDescription()
	{
		return this.l;
	}
}
