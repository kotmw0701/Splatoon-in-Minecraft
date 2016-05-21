package jp.kotmw.splatoon.battle;

import jp.kotmw.splatoon.Splatoon;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Charge extends BukkitRunnable{

	private Player player;

	public Charge(Player player)
	{
		this.player = player;
	}

	@Override
	public void run()
	{
		float ink = player.getExp();
		if((!player.hasMetadata(Splatoon.data.WaitingMeta))
				&& (Splatoon.data.Squid.containsKey(player.getName())))
		{
			if(Splatoon.data.Squid.get(player.getName()))
			{
				if(ink < 1.0)
				{
					player.setExp(ink + Splatoon.instance.charge);
				} else {
					this.cancel();
				}
			} else {
				this.cancel();
			}
		}
	}

}
