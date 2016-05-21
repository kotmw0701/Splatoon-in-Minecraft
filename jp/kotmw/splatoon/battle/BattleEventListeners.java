package jp.kotmw.splatoon.battle;

import jp.kotmw.splatoon.ColorSelect;
import jp.kotmw.splatoon.Metrics;
import jp.kotmw.splatoon.SplatTitle;
import jp.kotmw.splatoon.Splatoon;
import jp.kotmw.splatoon.arena.ArenaStatus;
import jp.kotmw.splatoon.arena.ArenaStatusEnum;
import jp.kotmw.splatoon.events.ArenaStatusChangeEvent;
import jp.kotmw.splatoon.events.BattleEndEvent;
import jp.kotmw.splatoon.events.BattleStartEvent;
import jp.kotmw.splatoon.events.ZoneChangeEvent;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.material.Wool;

public class BattleEventListeners implements Listener
{
	@SuppressWarnings("deprecation")
	@EventHandler
	public void EnsureZones(ZoneChangeEvent e)
	{
		String name = e.getArena();
		if(e.getEnsureTeam() == 1)
		{
			for(Player player : e.getTeamPlayers())
			{
				if(player.hasMetadata(Splatoon.data.Team1Meta))
					SplatTitle.sendFullTitle(player, 60, "", ColorSelect.color_team1_prefix(name).toString() +ChatColor.BOLD + "ガチエリア確保した！");
				else if(player.hasMetadata(Splatoon.data.Team2Meta))
					SplatTitle.sendFullTitle(player, 60, "", ColorSelect.color_team1_prefix(name).toString() +ChatColor.BOLD + "ガチエリア確保された！");
			}
		}
		else if(e.getEnsureTeam() == 2)
		{
			for(Player player : e.getTeamPlayers())
			{
				if(player.hasMetadata(Splatoon.data.Team1Meta))
					SplatTitle.sendFullTitle(player, 60, "", ColorSelect.color_team2_prefix(name).toString() +ChatColor.BOLD + "ガチエリア確保された！");
				else if(player.hasMetadata(Splatoon.data.Team2Meta))
					SplatTitle.sendFullTitle(player, 60, "", ColorSelect.color_team2_prefix(name).toString() +ChatColor.BOLD + "ガチエリア確保した！");
			}
		}
		DyeColor dyecolor = e.getEnsureTeamDyeColor();
		int x1 = e.getZonesPosition1().getBlockX();
		int x2 = e.getZonesPosition2().getBlockX();
		int y = e.getZonesPosition2().getBlockY();
		int z1 = e.getZonesPosition1().getBlockZ();
		int z2 = e.getZonesPosition2().getBlockZ();
		for (int xPoint = x2; xPoint <= x1; xPoint++)
		{
			for (int zPoint = z2; zPoint <= z1; zPoint++)
			{
				Block CheckBlock = e.getZonesPosition1().getWorld().getBlockAt(xPoint, y, zPoint);

				if(CheckBlock.getType() != Material.AIR)
				{
					Splatoon.rollback.setPaintedBlock(CheckBlock, name);
					if(CheckBlock.getType() == Material.WOOL)
					{
						BlockState state = CheckBlock.getState();
						Wool wool = (Wool)state.getData();
						wool.setColor(dyecolor);
						state.update();
					}
					else if(CheckBlock.getType() == Material.GLASS)
					{
						CheckBlock.setType(Material.STAINED_GLASS);
						CheckBlock.setData(ColorSelect.getColorData(dyecolor.toString()));
					}
					else if(CheckBlock.getType() == Material.THIN_GLASS)
					{
						CheckBlock.setType(Material.STAINED_GLASS_PANE);
						CheckBlock.setData(ColorSelect.getColorData(dyecolor.toString()));
					}
					else if(CheckBlock.getType() == Material.HARD_CLAY)
					{
						CheckBlock.setType(Material.STAINED_CLAY);
						CheckBlock.setData(ColorSelect.getColorData(dyecolor.toString()));
					}
					else if(CheckBlock.getType() == Material.STAINED_CLAY)
					{
						CheckBlock.setType(Material.STAINED_CLAY);
						CheckBlock.setData(ColorSelect.getColorData(dyecolor.toString()));
					}
					else if(CheckBlock.getType() == Material.STAINED_GLASS)
					{
						CheckBlock.setType(Material.STAINED_GLASS);
						CheckBlock.setData(ColorSelect.getColorData(dyecolor.toString()));
					}
					else if(CheckBlock.getType() == Material.STAINED_GLASS_PANE)
					{
						CheckBlock.setType(Material.STAINED_GLASS_PANE);
						CheckBlock.setData(ColorSelect.getColorData(dyecolor.toString()));
					}
					else if(CheckBlock.getType() == Material.CARPET)
					{
						CheckBlock.setType(Material.CARPET);
						CheckBlock.setData(ColorSelect.getColorData(dyecolor.toString()));
					}
				}
			}
		}
	}

	@EventHandler
	public void BattleStart(BattleStartEvent e)
	{
		SignEntry.UPdateAllSigns();
		Metrics.AllPlayerSend(
				ChatColor.GREEN + e.getRoom() + ChatColor.YELLOW + " ルームが "
				+ ChatColor.GREEN + e.getArena() + ChatColor.YELLOW + " で、ゲームを開始しました", Splatoon.data.RoomMeta);
		SplatoonScores.resetAllPlayerScore(e.getPlayers());
	}

	@EventHandler
	public void BattleEnd(BattleEndEvent e)
	{
		SignEntry.UPdateAllSigns();
	}

	@EventHandler
	public void ChangeStatus(ArenaStatusChangeEvent e)
	{
		String arena = e.getArena();
		ArenaStatusEnum status = e.getStatus();
		ArenaStatus.arenastatus.put(arena, status);
		SignEntry.UPdateAllSigns();
	}
}
