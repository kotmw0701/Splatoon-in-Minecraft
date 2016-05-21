package jp.kotmw.splatoon.events;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import jp.kotmw.splatoon.arena.ArenaStatusEnum;
import jp.kotmw.splatoon.battle.TurfBattle;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BattleEndEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private static List<Player> players = new ArrayList<>();
	private static String arena;
	private static ArenaStatusEnum status;

	public BattleEndEvent(String arena)
	{
		players = TurfBattle.JoinPlayersList(arena, true);
		BattleEndEvent.arena = arena;
		status = ArenaStatusEnum.ENABLED;
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public String getArena()
	{
		return arena;
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
