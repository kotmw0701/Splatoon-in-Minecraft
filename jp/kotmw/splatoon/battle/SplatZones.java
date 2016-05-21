package jp.kotmw.splatoon.battle;

import java.util.HashMap;
import java.util.Map;

import jp.kotmw.splatoon.ColorSelect;
import jp.kotmw.splatoon.SplatTitle;
import jp.kotmw.splatoon.Splatoon;
import jp.kotmw.splatoon.arena.ArenaData;
import jp.kotmw.splatoon.arena.ArenaLogs;
import jp.kotmw.splatoon.arena.ArenaSettings;
import jp.kotmw.splatoon.events.ZoneChangeEvent;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class SplatZones
{
	private String name;
	private ArenaLogs log;
	private int allarea;
	private ArenaData arenadata;
	public static Map<String, Boolean> team1 = new HashMap<>();
	public static Map<String, Boolean> team2 = new HashMap<>();

	public static Map<String, Integer> team1_count = new HashMap<>();
	public static Map<String, Integer> team2_count = new HashMap<>();

	public SplatZones(String arena, ArenaLogs logs)
	{
		name = arena;
		log = logs;
		allarea = TurfBattle.gachiarea.get(name);
		arenadata = ArenaSettings.getArenaData(arena);
	}

	public void GachiArenaParticle()
	{
		int x1 = arenadata.getAreaLocation(1).getBlockX();
		int x2 = arenadata.getAreaLocation(2).getBlockX();
		int y = arenadata.getAreaLocation(2).getBlockY();
		int z1 = arenadata.getAreaLocation(1).getBlockZ();
		int z2 = arenadata.getAreaLocation(2).getBlockZ();
		DyeColor dyecolor = EnsureColor();
		Color color = ColorSelect.teamSelect(dyecolor);
		int i = 0;
		if(dyecolor.equals(DyeColor.GREEN) || dyecolor.equals(DyeColor.BLUE))
			i = -1;
		for (int yPoint = y; yPoint <= (y + 20); yPoint++)
		{
			for (int xPoint = x2; xPoint <= x1; xPoint++)
			{
				for (int zPoint = z2; zPoint <= z1; zPoint++)
				{
					if((xPoint == x1 || xPoint == x2)
							|| (zPoint == z1 || zPoint == z2))
					{
						PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
								EnumParticle.REDSTONE
								, false
								, (float)(xPoint + 0.5)
								, (float)(yPoint + 0.5)
								, (float)(zPoint + 0.5)
								, (color.getRed() / 255) + i
								, (color.getGreen() / 255)
								, (color.getBlue() / 255)
								, 10
								, 0
								, 0);
						SplatTitle.sendPlayer(arenadata.getWorld().getName(), packet);
						if(yPoint == (y+2))
						{
							PacketPlayOutWorldParticles packet2 = new PacketPlayOutWorldParticles(
									EnumParticle.REDSTONE
									, true
									, (float)(xPoint + 0.5)
									, (float)(yPoint + 0.5)
									, (float)(zPoint + 0.5)
									, (color.getRed() / 255) + i
									, (color.getGreen() / 255)
									, (color.getBlue() / 255)
									, 10
									, 0
									, 0);
							SplatTitle.sendPlayer(arenadata.getWorld().getName(), packet2);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private DyeColor EnsureColor()
	{
		int team1 = 0, team2 = 0;
		int x1 = arenadata.getAreaLocation(1).getBlockX();
		int x2 = arenadata.getAreaLocation(2).getBlockX();
		int y = arenadata.getAreaLocation(2).getBlockY();
		int z1 = arenadata.getAreaLocation(1).getBlockZ();
		int z2 = arenadata.getAreaLocation(2).getBlockZ();
		for (int xPoint = x2; xPoint <= x1; xPoint++)
		{
			for (int zPoint = z2; zPoint <= z1; zPoint++)
			{
				Block CheckBlock = arenadata.getWorld().getBlockAt(xPoint, y, zPoint);
				Block AbobeBlock = CheckBlock.getLocation().add(0, 1, 0).getBlock();

				if((CheckBlock.getType() != Material.AIR)
						&& (AbobeBlock.getType() == Material.AIR))
				{
					if(CheckBlock.getType() == Material.WOOL)
					{
						DyeColor woolcolor = ColorSelect.itsColor(CheckBlock);
						if(woolcolor == ColorSelect.color_team1(name)) team1 += 1;
						else if(woolcolor == ColorSelect.color_team2(name)) team2 += 1;
					}
					else if(CheckBlock.getType() == Material.GLASS
							|| CheckBlock.getType() == Material.THIN_GLASS
							|| CheckBlock.getType() == Material.HARD_CLAY
							|| CheckBlock.getType() == Material.STAINED_CLAY
							|| CheckBlock.getType() == Material.STAINED_GLASS
							|| CheckBlock.getType() == Material.STAINED_GLASS_PANE
							|| CheckBlock.getType() == Material.CARPET)
					{
						byte id = CheckBlock.getData();
						if(id == ColorSelect.getColorData(ColorSelect.colorname_team1(name))) team1 += 1;
						else if(id == ColorSelect.getColorData(ColorSelect.colorname_team2(name))) team2 += 1;
					}
				}
			}
		}
		if((allarea * 0.5 >= team1) && (allarea * 0.5 >= team2))
			return DyeColor.WHITE;
		if(SplatZones.team2.containsKey(name))
		{
			if(SplatZones.team2.get(name))
			{
				if(allarea * 0.5 <= team1)
				{
					for(Player player : TurfBattle.JoinPlayersList(name, true))
					{
						if(player.hasMetadata(Splatoon.data.Team1Meta))
							SplatTitle.sendFullTitle(player, 60, "", ColorSelect.color_team1_prefix(name).toString() +ChatColor.BOLD + "カウントストップした！");
						else if(player.hasMetadata(Splatoon.data.Team2Meta))
							SplatTitle.sendFullTitle(player, 60, "", ColorSelect.color_team1_prefix(name).toString() +ChatColor.BOLD + "カウントストップされた！");
					}
					SplatZones.team2.put(name, false);
					return DyeColor.WHITE;
				}
				int i = team2_count.get(name);
				i--;
				team2_count.put(name, i);
				return ColorSelect.color_team2(name);
			}
		}
		if(SplatZones.team1.containsKey(name))
		{
			if(SplatZones.team1.get(name))
			{
				if(allarea * 0.5 <= team2)
				{
					for(Player player : TurfBattle.JoinPlayersList(name, true))
					{
						if(player.hasMetadata(Splatoon.data.Team1Meta))
							SplatTitle.sendFullTitle(player, 60, "", ColorSelect.color_team2_prefix(name).toString() +ChatColor.BOLD + "カウントストップされた！");
						else if(player.hasMetadata(Splatoon.data.Team2Meta))
							SplatTitle.sendFullTitle(player, 60, "", ColorSelect.color_team2_prefix(name).toString() +ChatColor.BOLD + "カウントストップした！");
					}
					SplatZones.team1.put(name, false);
					return DyeColor.WHITE;
				}
				int i = team1_count.get(name);
				i--;
				team1_count.put(name, i);
				return ColorSelect.color_team1(name);
			}
		}
		if((allarea * 0.8 <= team1) || (allarea * 0.8 <= team2))
		{
			PluginManager manager = Bukkit.getServer().getPluginManager();
			if(team1 > team2)
			{
				ZoneChangeEvent event = new ZoneChangeEvent(name, 1, team2_count.get(name));
				manager.callEvent(event);
				log.addLogs("Team1 Capped a Zone!");
				log.addLogs("Team2 Count: " + event.getCount());
				SplatZones.team1.put(name, true);
				return event.getEnsureTeamDyeColor();
			} else if(team2 > team1) {
				ZoneChangeEvent event = new ZoneChangeEvent(name, 2, team1_count.get(name));
				manager.callEvent(event);
				log.addLogs("Team2 Capped a Zone!");
				log.addLogs("Team1 Count: " + event.getCount());
				SplatZones.team2.put(name, true);
				return event.getEnsureTeamDyeColor();
			}
		}
		return DyeColor.WHITE;
	}
}
