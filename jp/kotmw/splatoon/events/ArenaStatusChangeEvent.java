package jp.kotmw.splatoon.events;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import jp.kotmw.splatoon.arena.ArenaStatusEnum;
import jp.kotmw.splatoon.battle.TurfBattle;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaStatusChangeEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	private static String arena;
	private static ArenaStatusEnum status;
	private static List<Player> players;

	public ArenaStatusChangeEvent(String arena, ArenaStatusEnum status)
	{
		ArenaStatusChangeEvent.arena = arena;
		ArenaStatusChangeEvent.status = status;
		ArenaStatusChangeEvent.players = TurfBattle.JoinPlayersList(arena, true);
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public String getArena()
	{
		return arena;
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

	public ArenaStatusEnum getStatus()
	{
		return status;
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
