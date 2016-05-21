/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.battle;

import java.io.File;

import jp.kotmw.splatoon.Splatoon;
import jp.kotmw.splatoon.SplatoonFiles;
import jp.kotmw.splatoon.arena.ArenaStatus;
import jp.kotmw.splatoon.arena.ArenaStatusEnum;
import jp.kotmw.splatoon.arena.RoomData;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SignEntry implements Listener
{
	String splatsign = ChatColor.DARK_AQUA + "["+ ChatColor.DARK_GREEN +"Splatoon"+ ChatColor.DARK_AQUA +"]";
	String splatstatussign = ChatColor.DARK_AQUA + "["+ ChatColor.GREEN + "SplatoonStatus" + ChatColor.DARK_AQUA +"]";
	String TurfBattleText = ChatColor.BOLD + "Turf Battle";
	String Gachi = ChatColor.RED + "" + ChatColor.BOLD + "Gachi Match";

	@EventHandler
	public void ClickSign(PlayerInteractEvent e)
	{
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		final Player player = e.getPlayer();

		Block clickdblock = e.getClickedBlock();
		if ((clickdblock.getType() != Material.SIGN)
				&& (clickdblock.getType() != Material.SIGN_POST)
				&& (clickdblock.getType() != Material.WALL_SIGN))
			return;
		Sign thisSign = (Sign)clickdblock.getState();
		if (thisSign.getLines().length <= 3)
			return;
		if(thisSign.getLine(0).equalsIgnoreCase(splatsign))
		{
			e.setCancelled(true);
			for(String room : SplatoonFiles.getRoomList())
			{
				if(thisSign.getLine(1).equalsIgnoreCase(room))
				{
					Splatoon.battle.WaitPoint(player, room);
					break;
				}
			}
			thisSign.setLine(2, TurfBattle.JoinPlayersList(thisSign.getLine(1), false).size() + " / 8");
			thisSign.update();
		}
		else if(thisSign.getLine(0).equalsIgnoreCase(splatstatussign))
		{
			e.setCancelled(true);
			for(final String arena : SplatoonFiles.getArenaList())
			{
				if(thisSign.getLine(1).equalsIgnoreCase(arena))
				{
					if(ArenaStatus.arenastatus.get(arena).equals(ArenaStatusEnum.DISABLED))
					{
						player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "そのステージは有効化されていません");
						return;
					}
					if(!ArenaStatus.arenastatus.get(arena).equals(ArenaStatusEnum.INGAME))
					{
						player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "そのステージでゲームが実行されてないため観戦はできません");
						return;
					}
					Spectate(arena, player);
					break;
				}
			}
		}
	}

	public static void Spectate(final String arena, final Player player)
	{
		TurfBattle.teleportteam(arena, 1, 1, player);
		player.setMetadata(Splatoon.data.SpectateMeta, new FixedMetadataValue(Splatoon.instance, arena));
		Bukkit.getScheduler().scheduleSyncDelayedTask(Splatoon.instance, new Runnable(){
			public void run()
			{
				for(Player joinplayers : TurfBattle.JoinPlayersList(arena, true))
					joinplayers.hidePlayer(player);
				player.setGameMode(GameMode.ADVENTURE);
				player.setAllowFlight(true);
				player.setFlying(true);
				player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 360000, 1));
				player.getInventory().setItem(8, Splatoon.data.item[4][1]);
			}
		}, 5L);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{
		Block block = e.getBlock();
		if ((block.getType() != Material.SIGN)
				&& (block.getType() != Material.SIGN_POST)
				&& (block.getType() != Material.WALL_SIGN))
			return;
		Sign sign = (Sign)block.getState();
		String world = block.getLocation().getWorld().getName();
		int x = block.getLocation().getBlockX();
		int y = block.getLocation().getBlockY();
		int z = block.getLocation().getBlockZ();
		for(int i = 1; i <= 500; i++)
		{
			if(sign.getLine(0).equalsIgnoreCase(this.splatsign))
			{
				if(SplatoonFiles.SignDirFiles("Join_" + sign.getLine(1)).exists());
					if(this.checklocation(SplatoonFiles.SignDirFiles("Join_" + sign.getLine(1)),
							x, y, z, world))
					{
						SplatoonFiles.SignDirFiles("Join_" + sign.getLine(1)).delete();
						return;
					}
				else if(SplatoonFiles.SignDirFiles("Join_" + sign.getLine(1) + "_" + i).exists());
					if(this.checklocation(SplatoonFiles.SignDirFiles("Join_" + sign.getLine(1) + "_" + i),
							x, y, z, world))
					{
						SplatoonFiles.SignDirFiles("Join_" + sign.getLine(1) + "_" + i).delete();
						return;
					}
			}
			else if(sign.getLine(0).equalsIgnoreCase(this.splatstatussign))
			{
				if(SplatoonFiles.SignDirFiles("Status_" + sign.getLine(1)).exists());
					if(this.checklocation(SplatoonFiles.SignDirFiles("Status_" + sign.getLine(1)),
							x, y, z, world))
					{
						SplatoonFiles.SignDirFiles("Status_" + sign.getLine(1)).delete();
						return;
					}
				else if(SplatoonFiles.SignDirFiles("Status_" + sign.getLine(1) + "_" + i).exists());
					if(this.checklocation(SplatoonFiles.SignDirFiles("Status_" + sign.getLine(1) + "_" + i),
							x, y, z, world))
					{
						SplatoonFiles.SignDirFiles("Status_" + sign.getLine(1) + "_" + i).delete();
						return;
					}
			}
		}
	}

	@EventHandler
	public void PlaceSplatSign(SignChangeEvent e)
	{
		Player player = e.getPlayer();
		if(e.getLine(0).equalsIgnoreCase("[Splatoon]")
				|| e.getLine(0).equalsIgnoreCase("splat")
				|| e.getLine(0).equalsIgnoreCase("Splatoon"))
		{
			if(e.getLine(1) == null)
			{
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "待機部屋名を指定してください");
				e.setCancelled(true);
				e.getBlock().breakNaturally();
				return;
			}
			if(Splatoon.arenastatus.CheckArena(e.getLine(1), false))
			{
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "その待機部屋は存在しません");
				e.setCancelled(true);
				e.getBlock().breakNaturally();
				return;
			}
			if(e.getLine(3).equalsIgnoreCase(""))
			{
				RoomData roomdata = new RoomData(e.getLine(1));
				e.setLine(0, splatsign);
				e.setLine(2, TurfBattle.JoinPlayersList(e.getLine(1), false).size() + " / 8");
				e.setLine(3, roomdata.getBattleType().getType());
			}
			this.SaveSignLocation(e.getBlock(), e.getLine(1), "Join_"+ e.getLine(1), "PLAY");
			UPdateAllSigns();
			player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "参加看板設置完了(/・ω・)/");
			return;
		}
		if(e.getLine(0).equalsIgnoreCase("[SplatStatus]")
				|| e.getLine(0).equalsIgnoreCase("splatstatus")
				|| e.getLine(0).equalsIgnoreCase("splatoonstatus"))
		{
			if(e.getLine(1) == null)
			{
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "ステージを指定してください");
				e.setCancelled(true);
				e.getBlock().breakNaturally();
				return;
			}
			if(Splatoon.arenastatus.CheckArena(e.getLine(1), true))
			{
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "そのステージは存在しません");
				e.setCancelled(true);
				e.getBlock().breakNaturally();
				return;
			}
			e.setLine(0, splatstatussign);
			e.setLine(2, ArenaStatus.arenastatus.get(e.getLine(1)).getStats());
			this.SaveSignLocation(e.getBlock(), e.getLine(1), "Status_"+ e.getLine(1),"STATUS");
			UPdateAllSigns();
			player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN +"ステージステータス看板設置完了(-ω-)/");
			return;
		}
	}

	public void SaveSignLocation(Block block, String name, String Filename, String Type)
	{
		String Filename_x = Filename;
		if(SplatoonFiles.SignDirFiles(Filename).exists())
			for(int i = 1; i <= 500; i++)
			{
				if(!SplatoonFiles.SignDirFiles(Filename + "_" + i).exists())
				{
					Filename_x = Filename + "_" + i;
					break;
				}
			}
		Splatoon.files.signconfig = new YamlConfiguration();
		Splatoon.files.signconfig.set("Name", name);
		Splatoon.files.signconfig.set("Type", Type);
		Splatoon.files.signconfig.set("World", block.getLocation().getWorld().getName());
		Splatoon.files.signconfig.set("LocX", block.getLocation().getBlockX());
		Splatoon.files.signconfig.set("LocY", block.getLocation().getBlockY());
		Splatoon.files.signconfig.set("LocZ", block.getLocation().getBlockZ());
		Splatoon.instance.SettingFiles(Splatoon.files.signconfig, SplatoonFiles.SignDirFiles(Filename_x), true);
	}

	public static void UPdateAllSigns()
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Splatoon.instance, new Runnable() {
			public void run()
			{
				Location loc;
				Splatoon.arenastatus.SetStatus();
				for(String signs : SplatoonFiles.getSignsList())
				{
					Splatoon.files.signconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.SignDirFiles(signs));
					if("PLAY".equalsIgnoreCase(Splatoon.files.signconfig.getString("Type")))
					{
						loc = new Location(Bukkit.getWorld(Splatoon.files.signconfig.getString("World")),
								Splatoon.files.signconfig.getInt("LocX"),
								Splatoon.files.signconfig.getInt("LocY"),
								Splatoon.files.signconfig.getInt("LocZ"));
						Block block = loc.getBlock();
						if ((block.getType() != Material.SIGN)
								&& (block.getType() != Material.SIGN_POST)
								&& (block.getType() != Material.WALL_SIGN))
							return;
						Sign sign = (Sign) block.getState();
						sign.setLine(2, TurfBattle.JoinPlayersList(sign.getLine(1), false).size() + " / 8");
						sign.update();
						continue;
					}
					else if("STATUS".equalsIgnoreCase(Splatoon.files.signconfig.getString("Type")))
					{
						loc = new Location(Bukkit.getWorld(Splatoon.files.signconfig.getString("World")),
								Splatoon.files.signconfig.getInt("LocX"),
								Splatoon.files.signconfig.getInt("LocY"),
								Splatoon.files.signconfig.getInt("LocZ"));
						Block block = loc.getBlock();
						if ((block.getType() != Material.SIGN)
								&& (block.getType() != Material.SIGN_POST)
								&& (block.getType() != Material.WALL_SIGN))
							return;
						Sign sign = (Sign) block.getState();
						if(Splatoon.arenastatus.CheckArena(sign.getLine(1), true))
						{
							sign.setLine(0, "");
							sign.setLine(1, "It does not exist,");
							sign.setLine(2, "The listed stage!");
							continue;
						}
						sign.setLine(2, ArenaStatus.arenastatus.get(sign.getLine(1)).getStats());
						sign.update();
						continue;
					}
				}
			}
		}, 10L);
	}
	/**
	 * 座標データが同じ看板のフォルダがあるかチェック
	 *
	 * @param file 対象のファイル
	 * @param x 対象のX座標
	 * @param y 対象のY座標
	 * @param z 対象のZ座標
	 *
	 * @return データが同じだったらtrue
	 */
	public boolean checklocation(File file, int x, int y, int z, String world)
	{
		Splatoon.files.signconfig = YamlConfiguration.loadConfiguration(file);
		String signworld = Splatoon.files.signconfig.getString("World");
		int signx = Splatoon.files.signconfig.getInt("LocX");
		int signy = Splatoon.files.signconfig.getInt("LocY");
		int signz = Splatoon.files.signconfig.getInt("LocZ");
		if(signworld.equalsIgnoreCase(world)
				&& signx == x
				&& signy == y
				&& signz == z)
			return true;
		return false;
	}
}
