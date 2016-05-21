package jp.kotmw.splatoon.arena;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.bukkit.entity.Player;

public class ArenaLogs
{
	private String arena, room, start, end, winner;
	private List<String> log = new ArrayList<>();
	private List<String> players = new ArrayList<>();
	private List<String> result = new ArrayList<>();

	public ArenaLogs(String arena, String room)
	{
		this.arena = arena;
		this.room = room;
	}
	public void setStartDate(String date)
	{
		this.start = date;
	}

	public void setEndDate(String date)
	{
		this.end = date;
	}

	public void setPlayers(List<Player> players)
	{
		for(Player player : players)
			this.players.add(player.getName());
	}

	public void setResult(List<String> result)
	{
		this.result = result.subList(0, 2);
	}

	public void setWinnerTeam(int i)
	{
		this.winner = "Team" + i;
	}

	public void addLogs(String log)
	{
		DecimalFormat df = new DecimalFormat("00");
		Calendar cal = new GregorianCalendar();
		String date = "["+ df.format(cal.get(Calendar.HOUR_OF_DAY))
				+":"+ df.format(cal.get(Calendar.MINUTE))
				+":"+ df.format(cal.get(Calendar.SECOND)) +"]";
		this.log.add(date + ": " + log);
	}

	public String getArena()
	{
		return arena;
	}

	public String getRoom()
	{
		return room;
	}

	public String getStartDate()
	{
		return start;
	}

	public String getEndDate()
	{
		return end;
	}

	public String getWinnerTeam()
	{
		return winner;
	}

	public List<String> getPlayers()
	{
		return players;
	}

	public List<String> getResult()
	{
		return result;
	}

	public List<String> getLogs()
	{
		return log;
	}
}
