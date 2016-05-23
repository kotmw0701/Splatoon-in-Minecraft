/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.battle;

import jp.kotmw.splatoon.Splatoon;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Respawn extends BukkitRunnable{

	private int tick;
	private Player player;

	public Respawn(int i, Player player)
	{
		this.tick = i + (5*20);
		this.player = player;
	}

	@Override
	public void run()
	{
		if(tick > 100)
		{
			if(tick%20 == 0)
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.WHITE + "復活まで " + ChatColor.AQUA + ((tick/20)-5) + ChatColor.WHITE + " 秒");
		}
		else if(tick == 100)
		{
			Location loc = Splatoon.teaming.getSplatRespawnLocation(player,player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString());
			player.teleport(loc, TeleportCause.PLUGIN);
			TurfBattle.SetWeapons(player);
			SplatEventListeners.death.remove(player.getName());
			player.getInventory().setHeldItemSlot(0);

			player.setVelocity(new Vector());
			player.setExp(1.0f);
		}
		else if(tick > 0 && tick < 100)
		{
			if(tick%20 == 0)
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.WHITE + "無敵時間無効化まで " + ChatColor.GREEN.toString() + ChatColor.BOLD + (tick/20) + ChatColor.WHITE + " 秒");
		}
		else if(tick < 0)
		{
			Location loc = Splatoon.teaming.getSplatRespawnLocation(player,player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString());
			player.teleport(loc, TeleportCause.PLUGIN);
			player.setGameMode(GameMode.ADVENTURE);
			player.removeMetadata(Splatoon.data.InvincibleMeta, Splatoon.instance);
			this.cancel();
		}
		tick--;
	}
}