/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon;

import jp.kotmw.splatoon.battle.SplatTeam;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class SplatRunnable
{
	private static int count = 0;
	private static String[] SBDisplayName = {
			ChatColor.WHITE +""+ ChatColor.BOLD +"S"+ ChatColor.GREEN +""+ ChatColor.BOLD +"platoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"S"+ ChatColor.WHITE +""+ ChatColor.BOLD +"p"+ ChatColor.GREEN + "" + ChatColor.BOLD +"latoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Sp"+ ChatColor.WHITE +""+ ChatColor.BOLD +"l"+ ChatColor.GREEN + "" + ChatColor.BOLD +"atoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Spl"+ ChatColor.WHITE +""+ ChatColor.BOLD +"a"+ ChatColor.GREEN + "" + ChatColor.BOLD +"toon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Spla"+ ChatColor.WHITE +""+ ChatColor.BOLD +"t"+ ChatColor.GREEN + "" + ChatColor.BOLD +"oon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splat"+ ChatColor.WHITE +""+ ChatColor.BOLD +"o"+ ChatColor.GREEN + "" + ChatColor.BOLD +"on"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splato"+ ChatColor.WHITE +""+ ChatColor.BOLD +"o"+ ChatColor.GREEN + "" + ChatColor.BOLD +"n"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoo"+ ChatColor.WHITE +""+ ChatColor.BOLD +"n"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.WHITE +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.WHITE +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.WHITE +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.WHITE +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoon"
			,ChatColor.GREEN +""+ ChatColor.BOLD +"Splatoon"};

	static void AnimationScoreboard()
	{
		count = 0;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Splatoon.instance, new Runnable() {
			public void run()
			{
				if(count == 24)
					count = 0;
				for(String arena : SplatoonFiles.getArenaList())
				{
					if(!SplatTeam.scoreboard.containsKey(arena))
					{
						Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

						Objective obj = sb.registerNewObjective("SplatScoreboard", "dummy");
						obj.setDisplaySlot(DisplaySlot.SIDEBAR);
						obj.setDisplayName(SBDisplayName[count]);

						Team team1 = sb.registerNewTeam("SplatTeam1");
						team1.setPrefix(ColorSelect.color_team1_prefix(arena).toString());
						team1.setSuffix(ChatColor.RESET.toString());
						team1.setAllowFriendlyFire(false);

						Team team2 = sb.registerNewTeam("SplatTeam2");
						team2.setPrefix(ColorSelect.color_team2_prefix(arena).toString());
						team2.setSuffix(ChatColor.RESET.toString());
						team2.setAllowFriendlyFire(false);

						Team spectate = sb.registerNewTeam("SplatDeathPlayer");
						spectate.setPrefix(ChatColor.WHITE.toString());
						spectate.setSuffix(ChatColor.RESET.toString());
						spectate.setAllowFriendlyFire(false);
						SplatTeam.scoreboard.put(arena, sb);
						continue;
					}
					Scoreboard scoreboard = SplatTeam.scoreboard.get(arena);

					Objective obj = scoreboard.getObjective("SplatScoreboard");
					obj.setDisplayName(SBDisplayName[count]);
				}
				count++;
			}
		}, 0L, 2L);
	}

	/*static void PlayParticles()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Splatoon.instance, new Runnable() {
			public void run()
			{
				for(String world : getWorlds())
				{
					for(Entity entity : Bukkit.getWorld(world).getEntities())
					{
						if(entity.getType() == EntityType.ARROW)
						{
							Arrow arrow = (Arrow) entity;
							Player player = (Player) arrow.getShooter();
							if(entity.hasMetadata(Splatoon.data.ChargerArrow))
								ParticleAPI.createEffect(EnumParticle.BLOCK_CRACK.setItem(new ItemStack(Material.WOOL, 1, (short)ColorSelect.ColorWoolItem(player))), entity.getLocation(), 0.0f, 0.0f, 0.0f, 1, 20);
						}
					}
				}
			}
		}, 0L, 2L);
	}

	private static List<String> getWorlds()
	{
		List<String> worlds = new ArrayList<String>();
		for(String arena : Splatoon.files.getArenaList())
		{
			Splatoon.files.fileconf = YamlConfiguration.loadConfiguration(Splatoon.files.ArenaDirFiles(arena));
			worlds.add(Splatoon.files.fileconf.getString(arena + ".World"));
		}
		return worlds;
	}*/
}
