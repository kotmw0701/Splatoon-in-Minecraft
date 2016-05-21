package jp.kotmw.splatoon.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.kotmw.splatoon.ColorSelect;
import jp.kotmw.splatoon.arena.ArenaSettings;
import jp.kotmw.splatoon.battle.TurfBattle;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ZoneChangeEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	private static Map<Integer, List<Player>> players = new HashMap<>();
	private static String arena;
	private static int getteam;
	private static int getcount;

	public ZoneChangeEvent(String arena, int team, int count)
	{
		players.put(team, TurfBattle.TeamPlayersList(arena, team));
		ZoneChangeEvent.arena = arena;
		getteam = team;
		getcount = count;
	}

	public List<Player> getTeamPlayers()
	{
		return players.get(getteam);
	}

	public Color getEnsureTeamColor()
	{
		return ColorSelect.teamSelect(getEnsureTeamDyeColor());
	}

	public DyeColor getEnsureTeamDyeColor()
	{
		return getteam == 1 ? ColorSelect.color_team1(arena) : ColorSelect.color_team2(arena);
	}

	public int getEnsureTeam()
	{
		return getteam;
	}

	public int getCount()
	{
		return getcount;
	}

	public String getArena()
	{
		return arena;
	}

	public Location getZonesPosition1()
	{
		return ArenaSettings.getArenaData(arena).getAreaLocation(1);
	}

	public Location getZonesPosition2()
	{
		return ArenaSettings.getArenaData(arena).getAreaLocation(2);
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

}
