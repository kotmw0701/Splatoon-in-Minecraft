/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.battle;

import jp.kotmw.splatoon.SplatTitle;
import jp.kotmw.splatoon.Splatoon;
import jp.kotmw.splatoon.arena.ArenaLogs;
import jp.kotmw.splatoon.arena.ArenaStatusEnum;
import jp.kotmw.splatoon.events.ArenaStatusChangeEvent;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BattleEnd extends BukkitRunnable
{
	/**
	 * 最後のメーターのやつ
	 */
	private static String name;
	private static int second;
	private static BattleTypeEnum type;
	private static boolean knockout;
	private static ArenaLogs log;
	private static int winteam;
	private static float pitch;

	public BattleEnd(String arena, int second, BattleTypeEnum type, boolean knockout, ArenaLogs log)
	{
		BattleEnd.pitch = 0.5f;
		BattleEnd.name = arena;
		BattleEnd.second = second;
		BattleEnd.type = type;
		BattleEnd.knockout = knockout;
		if(knockout)
			winteam = SplatZones.team1_count.get(arena) == 0 ? 1 : 2;
		BattleEnd.log = log;
		ArenaStatusChangeEvent event = new ArenaStatusChangeEvent(arena, ArenaStatusEnum.RESULT);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	@Override
	public void run()
	{
		if(second >= 0)
		{
			for(Player player : TurfBattle.JoinPlayersList(name, true))
			{
				if(second%5 == 0)
					player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, pitch);
				SplatTitle.sendFullTitle(player, 100, "", Meter.sendMeter(name, second));
			}
			if(second%5 == 0)
				pitch += 0.02f;
			second--;
		}
		else
		{
			this.cancel();
			if(type.equals(BattleTypeEnum.Turf_War))
			{
				if(!TurfBattle.team1.containsKey(name))
					TurfBattle.team1.put(name, 0f);
				if(!TurfBattle.team2.containsKey(name))
					TurfBattle.team2.put(name, 0f);
				TurfBattle.CheckWinner(name);
				double total = TurfBattle.team1.get(name) + TurfBattle.team2.get(name);
				double parce_team1 = TurfBattle.team1.get(name) / total;
				for(Player player : TurfBattle.JoinPlayersList(name, true))
					SplatTitle.sendFullTitle(player, 100, "", Meter.ParceMeter(name, parce_team1 * 100));
				Bukkit.getScheduler().scheduleSyncDelayedTask(Splatoon.instance, new Runnable() {
					public void run()
					{
						Splatoon.battle.FinishBattle(name, false, type, log);
					}
				}, 60);
			} else if(type.equals(BattleTypeEnum.Splat_Zones)) {
				if(knockout)
					for(Player player : TurfBattle.JoinPlayersList(name, true))
						SplatTitle.sendFullTitle(player, 100, "", Meter.KnockOut(name, winteam));
				Bukkit.getScheduler().scheduleSyncDelayedTask(Splatoon.instance, new Runnable() {
					public void run()
					{
						Splatoon.battle.FinishBattle(name, false, type, log);
					}
				}, 60);
			}
		}
	}
}
