/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.battle;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.kotmw.splatoon.ColorSelect;
import jp.kotmw.splatoon.SelectInv;
import jp.kotmw.splatoon.SplatTitle;
import jp.kotmw.splatoon.Splatoon;
import jp.kotmw.splatoon.SplatoonFiles;
import jp.kotmw.splatoon.Squid;
import jp.kotmw.splatoon.arena.ArenaData;
import jp.kotmw.splatoon.arena.ArenaLogs;
import jp.kotmw.splatoon.arena.ArenaSettings;
import jp.kotmw.splatoon.arena.ArenaStatus;
import jp.kotmw.splatoon.arena.ArenaStatusEnum;
import jp.kotmw.splatoon.arena.RoomData;
import jp.kotmw.splatoon.events.ArenaStatusChangeEvent;
import jp.kotmw.splatoon.events.BattleEndEvent;
import jp.kotmw.splatoon.weapons.other.Bomb;

import org.bitbucket.ucchy.undine.tellraw.ClickEventType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

public class TurfBattle
{
	static int x1, y1, z1, x2, y2, z2;
	int count_i = 0;
	Squid squid = new Squid();
	private static Map<String, Integer> Start = new HashMap<>();
	static Map<String, Float> team1 = new HashMap<>();
	static Map<String, Float> team2 = new HashMap<>();
	public static Map<String, Float> allarea = new HashMap<>();
	public static Map<String, Integer> gachiarea = new HashMap<>();
	public static Map<String, Integer> CountDownTaskID = new HashMap<>();
	public static Map<String, Integer> startcount = new HashMap<>();

	public static Map<String, BattleTypeEnum> type = new HashMap<>();



	public enum TimerType {
		BeforeStart, Winner
	}

	/**
	 * チームのメンバーが設定された座標にTPする
	 *
	 * @param arena ステージ名
	 * @param x 何人目
	 * @param world ワールド
	 * @param player プレイヤー
	 *
	 */
	public static boolean teleportteam(String arena, int team, int position, Player player)
	{
		ArenaData arenadata = ArenaSettings.getArenaData(arena);
		return player.teleport(arenadata.getPlayerLocation(team, position), TeleportCause.PLUGIN);
	}

	/*
	 *            teamMenLoc
	 * team_x[0][0] 1  1  x
	 * team_y[0][0] 1  1  y
	 * team_z[0][0] 1  1  z
	 * team_x[0][1] 1  2  x
	 * team_y[0][1] 1  2  y
	 * team_z[0][1] 1  2  z
	 * team_x[0][2] 1  3  x
	 * team_y[0][2] 1  3  y
	 * team_z[0][2] 1  3  z
	 * team_x[0][3] 1  4  x
	 * team_y[0][3] 1  4  y
	 * team_z[0][3] 1  4  z
	 *
	 * team_x[1][0] 2  1  x
	 * team_y[1][0] 2  1  y
	 * team_z[1][0] 2  1  z
	 * team_x[1][1] 2  2  x
	 * team_y[1][1] 2  2  y
	 * team_z[1][1] 2  2  z
	 * team_x[1][2] 2  3  x
	 * team_y[1][2] 2  3  y
	 * team_z[1][2] 2  3  z
	 * team_x[1][3] 2  4  x
	 * team_y[1][3] 2  4  y
	 * team_z[1][3] 2  4  z
	 */

	/**
	 * 待機部屋に入ってるプレイヤーをリストで取得
	 *
	 * @param room 取得するプレイヤーリストの部屋名
	 * @return 指定された待機部屋に居るプレイヤーのリスト
	 */
	public static List<Player> JoinPlayersList(String room, boolean arena)
	{
		List<Player> playerslist = new ArrayList<>();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if((!arena) && (player.hasMetadata(Splatoon.data.RoomMeta))
					&& (player.getMetadata(Splatoon.data.RoomMeta).get(0).asString().equalsIgnoreCase(room)))
				playerslist.add(player);
			if((arena) && (player.hasMetadata(Splatoon.data.ArenaMeta))
					&& (player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString().equalsIgnoreCase(room)))
				playerslist.add(player);
		}
		return playerslist;
	}

	public static List<Player> TeamPlayersList(String arena,int team)
	{
		List<Player> playerslist = new ArrayList<>();
		for(Player player : JoinPlayersList(arena, true))
		{
			if((team == 1) && (player.hasMetadata(Splatoon.data.Team1Meta)))
				playerslist.add(player);
			else if((team == 2) && (player.hasMetadata(Splatoon.data.Team2Meta)))
				playerslist.add(player);
		}
		return playerslist;
	}

	public static List<Player> SpectatePlayerList(String arena)
	{
		List<Player> playerslist = new ArrayList<>();
		for(Player player : Bukkit.getOnlinePlayers())
			if((player.hasMetadata(Splatoon.data.SpectateMeta))
					&& (player.getMetadata(Splatoon.data.SpectateMeta).get(0).asString().equalsIgnoreCase(arena)))
				playerslist.add(player);
		return playerslist;
	}

	/**
	 * 一時待機地点への移動
	 *
	 * @param p 対象のプレイヤー
	 * @param room 部屋名
	 *
	 */
	public void WaitPoint(Player player, final String room)
	{
		if(player.hasMetadata(Splatoon.data.RoomMeta))
			return;
		Splatoon.data.Weapons();
		if(JoinPlayersList(room, false).size() >= 8)
		{
			player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "そのルームは現在満員です");
			return;
		}
		String playername = player.getName();
		Rollback.SaveRollBackInv(player);
		player.getInventory().clear();
		player.setGameMode(GameMode.ADVENTURE);
		if(!Bukkit.getServer().getAllowFlight())
		{
			player.setFlying(false);
			player.setAllowFlight(false);
		}
		RoomData roomdata = new RoomData(room);
		player.teleport(roomdata.getLocation().add(0.5, 0.5, 0.5), TeleportCause.PLUGIN);
		player.getInventory().addItem(Splatoon.data.item[3][0]);
		player.getInventory().setItem(8, Splatoon.data.item[4][1]);
		if(Splatoon.data.SelectTeam)
		{
			player.getInventory().addItem(Splatoon.data.item[3][1]);
			player.getInventory().addItem(Splatoon.data.item[3][2]);
		}
		player.setMetadata(Splatoon.data.RoomMeta, new FixedMetadataValue(Splatoon.instance, room));//プレイヤーにMetaDataを貼り付ける
		if(player.hasMetadata(Splatoon.data.RoomMeta))
		{
			if(JoinPlayersList(room, false).size() < 8)
				Bukkit.broadcastMessage(Splatoon.data.Pprefix
						+ ChatColor.AQUA + playername + ChatColor.GREEN + " が " + ChatColor.AQUA + room + ChatColor.GREEN + " ルームに参加しました"
						+ ChatColor.YELLOW + " [ "+ ChatColor.GREEN + JoinPlayersList(room, false).size() + ChatColor.YELLOW  +"/8 ]");
			else
			{
				LocsetPlayers(room);
				SignEntry.UPdateAllSigns();
			}
		}
	}

	/**
	 * 一時待機地点からの離脱
	 *
	 * @param p 対象のプレイヤー
	 */
	public void Leave(Player p)
	{
		String playername = p.getName();
		if(p.hasMetadata(Splatoon.data.RoomMeta))
		{
			Rollback.InvRollBack(p);
			for(String room : SplatoonFiles.getRoomList())
			{
				if(room.equals(p.getMetadata(Splatoon.data.RoomMeta).get(0).asString()))
				{
					Bukkit.broadcastMessage(Splatoon.data.Pprefix
							+ ChatColor.AQUA + playername + ChatColor.GREEN + " leave");
					p.getInventory().removeItem(Splatoon.data.item[3][0]);
					p.getInventory().removeItem(Splatoon.data.item[4][1]);
					if(Splatoon.data.SelectTeam)
					{
						p.getInventory().removeItem(Splatoon.data.item[3][1]);
						p.getInventory().removeItem(Splatoon.data.item[3][2]);
					}
					backLobby(p);
					SignEntry.UPdateAllSigns();
					p.removeMetadata(Splatoon.data.RoomMeta, Splatoon.instance);
					break;
				}
			}
		}
	}

	public static boolean backLobby(Player player)
	{
		World world = Bukkit.getWorld(Splatoon.files.lobbyconfig.getString("Lobby.World"));
		int x = Splatoon.files.lobbyconfig.getInt("Lobby.x");
		int y = Splatoon.files.lobbyconfig.getInt("Lobby.y");
		int z = Splatoon.files.lobbyconfig.getInt("Lobby.z");
		Location loc = new Location(world, x, y, z);
		return player.teleport(loc, TeleportCause.PLUGIN);
	}

	/**
	 * ステージに移動させる
	 *
	 * @param room 待機部屋名
	 */
	public void LocsetPlayers(String room)
	{
		new BeforeStart(room, Splatoon.instance.StartCountDown).runTaskTimer(Splatoon.instance, 1, 20);
	}

	/*public static ItemStack setPaintedHelm(String arena, Player player)
	{
		ItemStack ColoredHelm = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta leather = (LeatherArmorMeta) ColoredHelm.getItemMeta();
		Color color = player.hasMetadata(Splatoon.data.Team1Meta)
				? ColorSelect.teamSelect(ColorSelect.color_team1(arena))
						: ColorSelect.teamSelect(ColorSelect.color_team2(arena));
		String DisplayName = player.hasMetadata(Splatoon.data.Team1Meta)
				? ColorSelect.color_team1_prefix(arena) +"Your Team Color Armor"+ ChatColor.WHITE
						: ColorSelect.color_team2_prefix(arena)+"Your Team Color Armor"+ ChatColor.WHITE;
		leather.setColor(color);
		leather.setDisplayName(DisplayName);
		leather.setLore(Splatoon.data.lore);
		ColoredHelm.setItemMeta(leather);
		return ColoredHelm;
	}*/

	public static void SetWeapons(Player player)
	{
		if(!player.hasMetadata(SelectInv.weaponmeta))
			player.setMetadata(SelectInv.weaponmeta, new FixedMetadataValue(Splatoon.instance, Splatoon.data.SHOOTER1));
		String weapon = player.getMetadata(SelectInv.weaponmeta).get(0).asString();
		Splatoon.instance.giveWeapon(player, weapon);
		Bomb.setBomb(player);
		if(!Splatoon.data.SneakSquid)
			for(int x = 3; x <= 8; x++)
				player.getInventory().setItem(x, Splatoon.data.item[4][0]);
	}

	public static void Start(final String arena, final BattleTypeEnum type, final ArenaLogs log)
	{
		TurfBattle.type.put(arena, type);
		ArenaSettings.SaveAllArea(arena);
		startcount.put(arena, Splatoon.instance.StartCountDown);
		Start.put(arena, Splatoon.instance.getServer().getScheduler()
				.scheduleSyncRepeatingTask(Splatoon.instance, new Runnable() {
			public void run()
			{
				int i = startcount.get(arena);
				if(i > 0)
				{
					if(i == 1)
					{
						for(Player player : JoinPlayersList(arena, true))
						{
							SetWeapons(player);
						}
					}
					for(Player player : JoinPlayersList(arena, true))
					{
						SplatTitle.sendFullTitle(player, 40, "", ChatColor.BLUE + "開始まで: " + ChatColor.GREEN + i);
						player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1);
					}
					i--;
					startcount.put(arena, i);
				} else {
					for(Player player : JoinPlayersList(arena, true))
						SplatTitle.sendFullTitle(player, 0, 20, 60, ChatColor.GOLD + "" + ChatColor.BOLD + "開始！", "");
					Bukkit.getScheduler().cancelTask(Start.get(arena));
					int ii = 0;
					if(type.equals(BattleTypeEnum.Turf_War))
					{
						ii = Splatoon.instance.time;
						if(ii > 600)
							ii = 600;
					}
					else if(type.equals(BattleTypeEnum.Splat_Zones))
					{
						Splatoon.arenastatus.SaveGachiAreaCount();
						ii = Splatoon.instance.gachi_area_time;
						if(ii > 600)
							ii = 600;
					}
					CountDownTaskID.put(arena, Splatoon.instance.getServer().getScheduler()
							.scheduleSyncRepeatingTask(Splatoon.instance, new BattleTimer(arena, ii*4, type, log), 0, 5));
					for(Player player : JoinPlayersList(arena, true))
					{
						player.removeMetadata(Splatoon.data.WaitingMeta, Splatoon.instance);
						player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
						player.setExp(1.0f);
					}
				}
			}
		}, 0L, 20L));
	}

	public void FinishBattle(final String arena, boolean command, BattleTypeEnum type, final ArenaLogs log)
	{
		TurfBattle.team1.put(arena, 0f);
		TurfBattle.team2.put(arena, 0f);
		final List<Player> playerlist = JoinPlayersList(arena, true);
		if(command)
		{
			Bukkit.getScheduler().cancelTask(TurfBattle.CountDownTaskID.get(arena));
			Squid.FinishSquid(TurfBattle.JoinPlayersList(arena, true));
			for(Player player : TurfBattle.JoinPlayersList(arena, true))
			{
				player.setGameMode(GameMode.SPECTATOR);
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
				player.removePotionEffect(PotionEffectType.SPEED);
				player.getInventory().setHeldItemSlot(1);
				player.getInventory().clear();
			}
		}
		CheckWinner(arena);
		final List<String> result = WhichWinner(arena);
		Splatoon.instance.getServer().getScheduler()
				.scheduleSyncDelayedTask(Splatoon.instance, new Runnable() {
			public void run()
			{
				BattleEndEvent event = new BattleEndEvent(arena);
				Bukkit.getServer().getPluginManager().callEvent(event);
				log.setEndDate(event.getDate());
				log.setResult(result);
				log.setWinnerTeam(Integer.valueOf(result.get(2)));
				log.addLogs(event.getArena() + " End Battle");
				SplatoonFiles.BattleLogs(arena, log);
				/*SplatTwitter twitter = new SplatTwitter(log);
				twitter.Tweet();*/
				TurfBattle.type.remove(arena);
				CountDownTaskID.remove(arena);
				ArenaStatusChangeEvent event2 = new ArenaStatusChangeEvent(arena, ArenaStatusEnum.ENABLED);
				Bukkit.getServer().getPluginManager().callEvent(event2);
				SplatoonScores.ShowScores(playerlist);
				ColorSelect.select(arena);
				Splatoon.files.lobbyconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.lobbyfile);
				World world = Bukkit.getWorld(Splatoon.files.lobbyconfig.getString("Lobby.World"));
				int x = Splatoon.files.lobbyconfig.getInt("Lobby.x");
				int y = Splatoon.files.lobbyconfig.getInt("Lobby.y");
				int z = Splatoon.files.lobbyconfig.getInt("Lobby.z");
				for(Player player : TurfBattle.SpectatePlayerList(arena))
				{
					for(Player joinplayers : TurfBattle.JoinPlayersList(arena, true))
						joinplayers.showPlayer(player);
					player.setGameMode(Bukkit.getServer().getDefaultGameMode());
					player.teleport(new Location(world, x, y, z).add(0.5, 0.5, 0.5), TeleportCause.PLUGIN);
					player.removeMetadata(Splatoon.data.SpectateMeta, Splatoon.instance);
				}
				for(Player player : playerlist)
				{
					SplatTeam.hideScoreboard(player);
					player.getInventory().clear();
					if(player.hasMetadata(Splatoon.data.Team1Meta))
						player.removeMetadata(Splatoon.data.Team1Meta, Splatoon.instance);
					else if(player.hasMetadata(Splatoon.data.Team2Meta))
						player.removeMetadata(Splatoon.data.Team2Meta, Splatoon.instance);
					player.setGameMode(Bukkit.getServer().getDefaultGameMode());
					player.teleport(new Location(world, x, y, z).add(0.5, 0.5, 0.5), TeleportCause.PLUGIN);
					player.removeMetadata(Splatoon.data.ArenaMeta, Splatoon.instance);
					Rollback.InvRollBack(player);
				}
				Rollback.rollback(arena);
			}
		}, 200L);
	}

	/**
	 * プラグイン無効化の際に戦闘が行われてた時の処理
	 *
	 */
	public static void End()
	{
		Splatoon.files.lobbyconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.lobbyfile);
		World world = Bukkit.getWorld(Splatoon.files.lobbyconfig.getString("Lobby.World"));
		int x = Splatoon.files.lobbyconfig.getInt("Lobby.x");
		int y = Splatoon.files.lobbyconfig.getInt("Lobby.y");
		int z = Splatoon.files.lobbyconfig.getInt("Lobby.z");
		for(String arena : SplatoonFiles.getArenaList())
		{
			if(ArenaStatus.arenastatus.containsKey(arena)
					&& ArenaStatus.arenastatus.get(arena).equals(ArenaStatusEnum.INGAME))
			{
				for(Player player : SpectatePlayerList(arena))
				{
					for(Player joinplayers : TurfBattle.JoinPlayersList(arena, true))
						joinplayers.showPlayer(player);
					if(player.hasMetadata(Splatoon.data.SpectateMeta))
						player.removeMetadata(Splatoon.data.SpectateMeta, Splatoon.instance);
					player.setGameMode(Bukkit.getServer().getDefaultGameMode());
					player.teleport(new Location(world, x, y, z).add(0.5, 0.5, 0.5), TeleportCause.PLUGIN);
					Rollback.InvRollBack(player);
				}
				for(Player player : JoinPlayersList(arena, true))
				{
					if(player.hasMetadata(Splatoon.data.ArenaMeta))
						player.removeMetadata(Splatoon.data.ArenaMeta, Splatoon.instance);
					if(player.hasMetadata(Splatoon.data.Team1Meta))
						player.removeMetadata(Splatoon.data.Team1Meta, Splatoon.instance);
					if(player.hasMetadata(Splatoon.data.Team2Meta))
						player.removeMetadata(Splatoon.data.Team2Meta, Splatoon.instance);
					player.setGameMode(Bukkit.getServer().getDefaultGameMode());
					player.teleport(new Location(world, x, y, z).add(0.5, 0.5, 0.5), TeleportCause.PLUGIN);
					SplatTeam.hideScoreboard(player);
					Rollback.InvRollBack(player);
				}
			}
		}
		for(String room : SplatoonFiles.getRoomList())
		{
			for(Player player : JoinPlayersList(room, false))
			{
				if(player.hasMetadata(Splatoon.data.RoomMeta))
					player.removeMetadata(Splatoon.data.RoomMeta, Splatoon.instance);
				player.setGameMode(Bukkit.getServer().getDefaultGameMode());
				player.teleport(new Location(world, x, y, z).add(0.5, 0.5, 0.5), TeleportCause.PLUGIN);
				Rollback.InvRollBack(player);
			}
		}
	}
	/**
	 * 羊毛を取得する
	 *
	 * @param arena 取得対象のステージ名
	 */
	public static void CheckWinner(String arena)
	{
		ArenaData arenadata = ArenaSettings.getArenaData(arena);
		x1 = arenadata.getLocation(1).getBlockX();
		y1 = arenadata.getLocation(1).getBlockY();
		z1 = arenadata.getLocation(1).getBlockZ();
		x2 = arenadata.getLocation(2).getBlockX();
		y2 = arenadata.getLocation(2).getBlockY();
		z2 = arenadata.getLocation(2).getBlockZ();
		{
			for (int xPoint = x2; xPoint <= x1; xPoint++)
			{
				for (int yPoint = y2; yPoint <= y1; yPoint++)
				{
					for (int zPoint = z2; zPoint <= z1; zPoint++)
					{
						Block CheckBlock = arenadata.getWorld().getBlockAt(xPoint, yPoint, zPoint);
						Block CheckAboveBlock = arenadata.getWorld().getBlockAt(xPoint, yPoint+1, zPoint);

						if(CheckBlock.getType() != Material.AIR)
						{
							if(CheckAboveBlock.getType() == Material.AIR)
							{
								CheckColor(CheckBlock, arena);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 取得した羊毛の色がチーム1、若しくはチーム2の色の羊毛かを調べる
	 *
	 * @param block 取得したブロック(羊毛)
	 */
	@SuppressWarnings("deprecation")
	private static void CheckColor(Block block, String arena)
	{
		if(block.getType() == Material.WOOL)
		{
			DyeColor woolcolor = ColorSelect.itsColor(block);
			if(woolcolor == ColorSelect.color_team1(arena)) team1.put(arena, team1.get(arena) + 0.1f);
			else if(woolcolor == ColorSelect.color_team2(arena)) team2.put(arena, team2.get(arena) + 0.1f);
		}
		else if(block.getType() == Material.GLASS
				|| block.getType() == Material.THIN_GLASS
				|| block.getType() == Material.HARD_CLAY
				|| block.getType() == Material.STAINED_CLAY
				|| block.getType() == Material.STAINED_GLASS
				|| block.getType() == Material.STAINED_GLASS_PANE
				|| block.getType() == Material.CARPET)
		{
			byte id = block.getData();
			if(id == ColorSelect.getColorData(ColorSelect.colorname_team1(arena)))team1.put(arena, team1.get(arena) + 0.1f);
			else if(id == ColorSelect.getColorData(ColorSelect.colorname_team2(arena)))team2.put(arena, team2.get(arena) + 0.1f);
		}
	}

	public static void CheckAllArea(String arena)
	{
		ArenaData arenadata = ArenaSettings.getArenaData(arena);
		x1 = arenadata.getLocation(1).getBlockX();
		y1 = arenadata.getLocation(1).getBlockY();
		z1 = arenadata.getLocation(1).getBlockZ();
		x2 = arenadata.getLocation(2).getBlockX();
		y2 = arenadata.getLocation(2).getBlockY();
		z2 = arenadata.getLocation(2).getBlockZ();
		{
			for (int xPoint = x2; xPoint <= x1; xPoint++)
			{
				for (int yPoint = y2; yPoint <= y1; yPoint++)
				{
					for (int zPoint = z2; zPoint <= z1; zPoint++)
					{
						Block CheckBlock = arenadata.getWorld().getBlockAt(xPoint, yPoint, zPoint);
						Block CheckAboveBlock = arenadata.getWorld().getBlockAt(xPoint, (yPoint+1), zPoint);

						if(CheckBlock.getType() != Material.AIR)
						{
							if(CheckAboveBlock.getType() == Material.AIR)
							{
								if(CheckBlock.getType() == Material.WOOL)
								{
									setAllArea(arena);
								}
								else if(CheckBlock.getType() == Material.GLASS
										|| CheckBlock.getType() == Material.THIN_GLASS
										|| CheckBlock.getType() == Material.HARD_CLAY
										|| CheckBlock.getType() == Material.STAINED_CLAY
										|| CheckBlock.getType() == Material.STAINED_GLASS
										|| CheckBlock.getType() == Material.STAINED_GLASS_PANE
										|| CheckBlock.getType() == Material.CARPET)
								{
									setAllArea(arena);
								}
							}
						}
					}
				}
			}
		}
	}

	private static void setAllArea(String arena)
	{
		if(!allarea.containsKey(arena))
		{
			allarea.put(arena, 0.1f);
			return;
		}
		float i = allarea.get(arena);
		i = i + 0.1f;
		allarea.put(arena, i);
	}

	/**
	 * 勝利チームを計算、取得する
	 */
	public static List<String> WhichWinner(String arena)
	{
		List<String> result = new ArrayList<>();
		ArenaData arenadata = ArenaSettings.getArenaData(arena);
		DecimalFormat df = new DecimalFormat("##0.0%");
		float total = arenadata.getAllArea();
		float parce_team1 = (team1.get(arena) / total);
		float parce_team2 = (team2.get(arena) / total);
		if(Splatoon.data.DebugMode)
		{
			System.out.println("team1: "+ team1.get(arena));
			System.out.println("team2: "+ team2.get(arena));
			System.out.println("total: "+ total);
			System.out.println("Pteam1: "+ parce_team1);
			System.out.println("Pteam2: "+ parce_team2);
		}
		String win = null, lose = null;
		int win_i = 0;
		boolean draw = false;
		if(parce_team1 != parce_team2)
		{
			win = parce_team1 > parce_team2 ? Splatoon.data.Team1Meta : Splatoon.data.Team2Meta;
			win_i = parce_team1 > parce_team2 ? 1 : 2;
			lose = parce_team1 > parce_team2 ? Splatoon.data.Team2Meta : Splatoon.data.Team1Meta;
		}
		else
			draw = true;
		result.add("Team1: " + df.format(parce_team1));
		result.add("Team2: " + df.format(parce_team2));
		result.add(Integer.toString(win_i));
		for(Player player : JoinPlayersList(arena, true))
		{
			player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------------------------------");
			player.sendMessage("");
			player.sendMessage("" + ChatColor.GREEN + "このステージが良いと思ったら下の投票ボタンを押してください");
			Splatoon.msgbutton.ButtonCreate_Vote(player, "投票", "このステージに投票します", "/splat vote", ClickEventType.RUN_COMMAND);
			player.sendMessage("");
			player.sendMessage("");
			player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------------------------------");
			if(draw)
			{
				SplatTitle.sendFullTitle(player, 0, 60, 60, ChatColor.RED + "" + ChatColor.UNDERLINE + "Draw!"
						,ChatColor.WHITE + "  [" +ColorSelect.color_team1_prefix(arena) + "Team1" + ChatColor.WHITE + "]" + df.format(parce_team1)
						+ ChatColor.WHITE + "  [" + ColorSelect.color_team2_prefix(arena) + "Team2" + ChatColor.WHITE + "]" + df.format(parce_team2));
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "引き分けの場合はレートに加算されません");
			}
			else if(player.hasMetadata(win))
			{
				for(int i = 1; i <= 4; i++)
				{
					Firework firework = arenadata.getPlayerLocation(win_i, i).getWorld().spawn(arenadata.getPlayerLocation(win_i, i), Firework.class);
					FireworkMeta meta = firework.getFireworkMeta();
					meta.addEffect(FireworkEffect.builder().withColor(ColorSelect.PlayerTeamColor(player)).flicker(false).with(Type.BALL_LARGE).build());
					firework.setFireworkMeta(meta);
				}
				SplatTitle.sendFullTitle(player, 0, 60, 60, ChatColor.GOLD + "" + ChatColor.BOLD + "You win!"
						,ChatColor.WHITE + "  [" +ColorSelect.color_team1_prefix(arena) + "Team1" + ChatColor.WHITE + "]" + df.format(parce_team1)
						+ ChatColor.WHITE + "  [" + ColorSelect.color_team2_prefix(arena) + "Team2" + ChatColor.WHITE + "]" + df.format(parce_team2));
				SplatoonScores.setWinnerScore(player);
				SplatPlayerStatus rate = new SplatPlayerStatus(player.getName());
				rate.setRate(true);
			}
			else if(player.hasMetadata(lose))
			{
				SplatTitle.sendFullTitle(player, 0, 60, 60, ChatColor.BLUE + "" + ChatColor.ITALIC + "You lose!"
						,ChatColor.WHITE + "  [" +ColorSelect.color_team1_prefix(arena) + "Team1" + ChatColor.WHITE + "]" + df.format(parce_team1)
						+ ChatColor.WHITE + "  [" + ColorSelect.color_team2_prefix(arena) + "Team2" + ChatColor.WHITE + "]" + df.format(parce_team2));
				SplatPlayerStatus rate = new SplatPlayerStatus(player.getName());
				rate.setRate(false);
			}
		}
		for(Player player : SpectatePlayerList(arena))
			SplatTitle.sendFullTitle(player, 0, 60, 60, "",
					ChatColor.WHITE + "  [" +ColorSelect.color_team1_prefix(arena) + "Team1" + ChatColor.WHITE + "]" + df.format(parce_team1)
			+ ChatColor.WHITE + "  [" + ColorSelect.color_team2_prefix(arena) + "Team2" + ChatColor.WHITE + "]" + df.format(parce_team2));
		return result;
	}

	/**
	 * ダメージを与える
	 *
	 * @param player ダメージを"与える"プレイヤー
	 * @param world どこのワールドか
	 * @param x 塗られる羊毛のx座標
	 * @param y 塗られる羊毛のy座標
	 * @param z 塗られる羊毛のz座標
	 * @param damage ダメージ量(ハート1個で2)
	 */
	public Player Damager(Player player, int x, int y, int z, int damage)
	{
		Player target = null;
		for(Player players : JoinPlayersList(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString(), true))
		{
			if(players == player)
				continue;
			boolean playerteam = player.hasMetadata(Splatoon.data.Team1Meta) ? true : false;
			boolean otherteam = players.hasMetadata(Splatoon.data.Team1Meta) ? true : false;
			if((playerteam && otherteam) || (!playerteam && !otherteam))
				continue;
			Location otherloc = players.getLocation();
			int o_X = otherloc.getBlockX();
			int o_Y = otherloc.getBlockY();
			int o_Z = otherloc.getBlockZ();
			if((x == o_X)&&(y == o_Y)&&(z == o_Z))
			{
				if(target.hasMetadata(Splatoon.data.InvincibleMeta))
					break;
				players.damage(damage);
				target = players;
			}
		}
		return target;
	}
}
