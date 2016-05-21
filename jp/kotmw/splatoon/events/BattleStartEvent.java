package jp.kotmw.splatoon.events;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import jp.kotmw.splatoon.ColorSelect;
import jp.kotmw.splatoon.arena.ArenaStatusEnum;
import jp.kotmw.splatoon.battle.TurfBattle;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BattleStartEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private static List<Player> players = new ArrayList<>();
	private static String arena, room;
	private static ArenaStatusEnum status;

	public BattleStartEvent(String room, String arena)
	{
		players = TurfBattle.JoinPlayersList(room, false);
		BattleStartEvent.room = room;
		BattleStartEvent.arena = arena;
		status = ArenaStatusEnum.INGAME;
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public String getArena()
	{
		return arena;
	}

	public String getRoom()
	{
		return room;
	}
	public ArenaStatusEnum getStatus()
	{
		return status;
	}

	public String getDate()
	{
		Calendar cal = new GregorianCalendar();
		String date = cal.get(Calendar.YEAR)
				+ "/" + (cal.get(Calendar.MONTH) + 1)
				+ "/" + cal.get(Calendar.DAY_OF_MONTH)
				+ " " + cal.get(Calendar.HOUR_OF_DAY)
				+ ":" + cal.get(Calendar.MINUTE)
				+ ":" + cal.get(Calendar.SECOND);
		return date;
	}

	public Color getTeamColor(int i)
	{
		return ColorSelect.teamSelect(getTeamDyeColor(i));
	}

	public DyeColor getTeamDyeColor(int i)
	{
		DyeColor color = i == 1 ? ColorSelect.color_team1(arena) : ColorSelect.color_team2(arena);
		return color;
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
