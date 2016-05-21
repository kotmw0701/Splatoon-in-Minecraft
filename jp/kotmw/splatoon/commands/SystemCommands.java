/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.commands;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;

import jp.kotmw.splatoon.ColorSelect;
import jp.kotmw.splatoon.JoinQuitEvents;
import jp.kotmw.splatoon.Metrics;
import jp.kotmw.splatoon.SelectInv;
import jp.kotmw.splatoon.SplatTitle;
import jp.kotmw.splatoon.Splatoon;
import jp.kotmw.splatoon.SplatoonFiles;
import jp.kotmw.splatoon.TestRunnable;
import jp.kotmw.splatoon.arena.ArenaData;
import jp.kotmw.splatoon.arena.ArenaFile;
import jp.kotmw.splatoon.arena.ArenaSettings;
import jp.kotmw.splatoon.arena.ArenaStatus;
import jp.kotmw.splatoon.arena.ArenaStatusEnum;
import jp.kotmw.splatoon.battle.BattleTypeEnum;
import jp.kotmw.splatoon.battle.Rollback;
import jp.kotmw.splatoon.battle.SignEntry;
import jp.kotmw.splatoon.battle.SplatPlayerStatus;
import jp.kotmw.splatoon.battle.TurfBattle;
import jp.kotmw.splatoon.events.ArenaStatusChangeEvent;

import org.bitbucket.ucchy.undine.tellraw.ClickEventType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class SystemCommands extends SplatCommandExecutor
{
	int team;
	int position;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(args.length >= 1)
		{
			if(sender instanceof Player)
			{
				Player player = (Player)sender;
				//##############################################################################################################
				if((args.length >= 2) && ("setup".equalsIgnoreCase(args[0])))
				{
					if(sender.hasPermission("splat.create"))
					{
						if((args.length == 2) && ("setlobby".equalsIgnoreCase(args[1])))
						{
							Location loc = player.getLocation();
							String world = loc.getWorld().getName();
							int x = loc.getBlockX();
							int y = loc.getBlockY();
							int z = loc.getBlockZ();
							ArenaSettings.SaveLobby(world, x, y, z);
							sender.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "ロビーの設定をしました！");
							return true;
						}
						if((args.length == 3) && ("setpoint".equalsIgnoreCase(args[1])))
						{
							Location loc = player.getLocation();
							int x = loc.getBlockX();
							int y = loc.getBlockY();
							int z = loc.getBlockZ();
							if("setp1".equalsIgnoreCase(args[2]))
							{
								player.setMetadata("Xp1", new FixedMetadataValue(Splatoon.instance, x));
								player.setMetadata("Yp1", new FixedMetadataValue(Splatoon.instance, y));
								player.setMetadata("Zp1", new FixedMetadataValue(Splatoon.instance, z));
								player.sendMessage(Splatoon.data.Pprefix + "Point1を設定しました");
								return true;
							}
							else if("setp2".equalsIgnoreCase(args[2]))
							{
								if(!(player.hasMetadata("Xp1")) || !(player.hasMetadata("Yp1")) || !(player.hasMetadata("Zp1")))
								{
									player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "/splat setup setpoint setp1を先に実行してください");
									return false;
								}
								player.setMetadata("Xp2", new FixedMetadataValue(Splatoon.instance, x));
								player.setMetadata("Yp2", new FixedMetadataValue(Splatoon.instance, y));
								player.setMetadata("Zp2", new FixedMetadataValue(Splatoon.instance, z));
								player.sendMessage(Splatoon.data.Pprefix + "Point2を設定しました");
								if(Splatoon.data.DebugMode)
								{
									player.sendMessage(Splatoon.data.Pprefix);
									player.sendMessage("FirstX: " + player.getMetadata("Xp1").get(0).asInt());
									player.sendMessage("FirstY: " + player.getMetadata("Yp1").get(0).asInt());
									player.sendMessage("FirstZ: " + player.getMetadata("Zp1").get(0).asInt());
									player.sendMessage("EndX: " + x);
									player.sendMessage("EndY: " + y);
									player.sendMessage("EndZ: " + z);
									player.sendMessage("WorldName: " + loc.getWorld().getName());
								}
								return true;
							}
						}
						String arenaname = args[1];
						if((args.length == 3) && ("finish".equalsIgnoreCase(args[2])))
						{
							if(Splatoon.arenastatus.CheckArena(arenaname, true))
							{
								sender.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "その名前のステージは存在しません！");
								return false;
							}
							ArenaSettings.arenadata.put(arenaname, new ArenaData(new ArenaFile(arenaname)));
							if(!ArenaSettings.SetupFinish(arenaname))
							{
								sender.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "スポーンポイントのセットアップが終わっていません！");
								return false;
							}
							Splatoon.files.fileconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.ArenaDirFiles(arenaname));
							Splatoon.files.fileconfig.set(arenaname +".Status", true);
							ArenaStatusChangeEvent event = new ArenaStatusChangeEvent(arenaname, ArenaStatusEnum.ENABLED);
							Bukkit.getServer().getPluginManager().callEvent(event);
							Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.ArenaDirFiles(arenaname), true);
							ArenaSettings.ReloadArena(arenaname);
							sender.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "セットアップ完了！使用可能となりました");
							return true;
						}
						if(((args.length == 3) || (args.length == 4)) && ("setarena".equalsIgnoreCase(args[2])))
						{
							if(!Splatoon.arenastatus.CheckArena(arenaname, true) || ArenaSettings.SetupFinish(arenaname))
							{
								sender.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "その名前のステージは既に有効化されています！");
								return false;
							}
							if(args.length == 3)
							{
								WorldEditPlugin worldEdit = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
								Selection selection = worldEdit.getSelection(player);
								if(selection != null)
								{
									if(Splatoon.data.DebugMode)
									{
										player.sendMessage(Splatoon.data.Pprefix);
										player.sendMessage("FirstX: " + selection.getMinimumPoint().getBlockX());
										player.sendMessage("FirstY: " + selection.getMinimumPoint().getBlockY());
										player.sendMessage("FirstZ: " + selection.getMinimumPoint().getBlockZ());
										player.sendMessage("EndX: " + selection.getMaximumPoint().getBlockX());
										player.sendMessage("EndY: " + selection.getMaximumPoint().getBlockY());
										player.sendMessage("EndZ: " + selection.getMaximumPoint().getBlockZ());
										player.sendMessage("WorldName: " + selection.getWorld().getName());
									}
									String worldName = selection.getWorld().getName();
									int x1 = selection.getMinimumPoint().getBlockX();
									int y1 = selection.getMinimumPoint().getBlockY();
									int z1 = selection.getMinimumPoint().getBlockZ();
									int x2 = selection.getMaximumPoint().getBlockX();
									int y2 = selection.getMaximumPoint().getBlockY();
									int z2 = selection.getMaximumPoint().getBlockZ();
									ArenaStatusChangeEvent event = new ArenaStatusChangeEvent(arenaname, ArenaStatusEnum.DISABLED);
									Bukkit.getServer().getPluginManager().callEvent(event);
									if(!ArenaSettings.SaveArenaSetting(player, arenaname, worldName, x1, x2, y1, y2, z1, z2))
									{
										player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "着色可能ブロックを1つ以上構築してください");
										SplatoonFiles.ArenaDirFiles(arenaname).delete();
										return false;
									}
									ColorSelect.select(arenaname);
									sender.sendMessage(Splatoon.data.Pprefix + "ステージ座標を保存しました");
									sender.sendMessage(Splatoon.data.Pprefix + "次のコマンドは /splat setup "+ arenaname + " setspawnpoint <TeamNum> <PlayerNum>");
									return true;
								}
							}
							else if((args.length == 4) && ("point".equalsIgnoreCase(args[3])))
							{
								if(!player.hasMetadata("Xp1") && player.hasMetadata("Xp2"))
								{
									player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "/splat setup setpoint setp1");
									player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "/splat setup setpoint setp2");
									player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "を実行して座標を登録してから実行してください");
									return false;
								}
								int x1 = player.getMetadata("Xp1").get(0).asInt();
								int y1 = player.getMetadata("Yp1").get(0).asInt();
								int z1 = player.getMetadata("Zp1").get(0).asInt();
								int x2 = player.getMetadata("Xp2").get(0).asInt();
								int y2 = player.getMetadata("Yp2").get(0).asInt();
								int z2 = player.getMetadata("Zp2").get(0).asInt();
								ArenaStatusChangeEvent event = new ArenaStatusChangeEvent(arenaname, ArenaStatusEnum.DISABLED);
								Bukkit.getServer().getPluginManager().callEvent(event);
								if(!ArenaSettings.SaveArenaSetting(player, arenaname, player.getWorld().getName(), x1, x2, y1, y2, z1, z2))
								{
									player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "着色可能ブロックを1つ以上構築してください");
									SplatoonFiles.ArenaDirFiles(arenaname).delete();
									return false;
								}
								ColorSelect.select(arenaname);
								sender.sendMessage(Splatoon.data.Pprefix + "ステージ座標を保存しました");
								sender.sendMessage(Splatoon.data.Pprefix + "次のコマンドは /splat setup "+ arenaname + " setspawnpoint <TeamNum> <PlayerNum>");
								player.removeMetadata("Xp1", Splatoon.instance);
								player.removeMetadata("Yp1", Splatoon.instance);
								player.removeMetadata("Zp1", Splatoon.instance);
								player.removeMetadata("Xp2", Splatoon.instance);
								player.removeMetadata("Yp2", Splatoon.instance);
								player.removeMetadata("Zp2", Splatoon.instance);
								return true;
							}
							return false;
						}
						if((args.length >= 3) && ("setroom".equalsIgnoreCase(args[2])))
						{
							if(!Splatoon.arenastatus.CheckArena(arenaname, false))
							{
								sender.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "その名前の待機地点は存在します！");
								return false;
							}
							BattleTypeEnum type = BattleTypeEnum.Turf_War;
							if(Splatoon.data.DebugMode)
							{
								if((args.length == 4))
								{
									for(BattleTypeEnum types : BattleTypeEnum.values())
									{
										if(types.toString().equalsIgnoreCase(args[3]))
										{
											type = types;
											break;
										}
									}
								}
							}
							Location loc = player.getLocation();
							String world = loc.getWorld().getName();
							int x = loc.getBlockX();
							int y = loc.getBlockY();
							int z = loc.getBlockZ();
							ArenaSettings.SaveRoomLocation(player, arenaname ,world, x, y, z, type);
							sender.sendMessage(Splatoon.data.Pprefix + "WaitPoint location save ended!");
							return true;
						}
						if((args.length == 5) && ("setspawnpoint".equalsIgnoreCase(args[2])))
						{
							if(Splatoon.arenastatus.CheckArena(arenaname, true))
							{
								sender.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "その名前のステージは存在しません！");
								return false;
							}
							Location loc = player.getLocation();
							Double x = loc.getX();
							Double y = loc.getY();
							Double z = loc.getZ();

							try
							{
								team = Integer.parseInt(args[3]);
								position = Integer.parseInt(args[4]);
							} catch(NumberFormatException e){
								sender.sendMessage("後ろの2つには数値以外入れないでください！");
								sender.sendMessage("/splat setup <ArenaID> setspawnpoint <TeamID> <PlayerNum>");
								return false;
							}

							if((team != 1) && (team != 2))
							{
								sender.sendMessage("1若しくは2を入れてください！");
								sender.sendMessage("/splat setup <ArenaID> setspawnpoint <TeamID> <PlayerNum>");
								return false;
							}
							if(team == 1)
							{
								if(!((position >= 1) && (position <= 4)))
								{
									sender.sendMessage("1～4の番号を入れてください");
									sender.sendMessage("/splat setup <ArenaID> setspawnpoint <TeamID> <PlayerNum>");
									return false;
								}
								ArenaSettings.SaveJoinLocation(arenaname, position, true, x, y, z);
								sender.sendMessage(Splatoon.data.Pprefix +"team: "+ ChatColor.GOLD + "team1  " + ChatColor.WHITE + "Position: " + ChatColor.GREEN + position);
								return true;
							}
							else if(team == 2)
							{
								if(!((position >= 1) && (position <= 4)))
								{
									sender.sendMessage("1～4の番号を入れてください");
									sender.sendMessage("/splat setup <ArenaID> setspawnpoint <TeamID> <PlayerNum>");
									return false;
								}
								ArenaSettings.SaveJoinLocation(arenaname, position, false, x, y, z);
								sender.sendMessage(Splatoon.data.Pprefix +"team: "+ ChatColor.GOLD + "team2  " + ChatColor.WHITE + "Position: " + ChatColor.GREEN + position);
								return true;
							}
						}
						if((args.length == 3) && ("setgachiarea".equalsIgnoreCase(args[2])))
						{
							WorldEditPlugin worldEdit = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
							Selection selection = worldEdit.getSelection(player);
							if(selection != null)
							{
								if(Splatoon.data.DebugMode)
								{
									player.sendMessage(Splatoon.data.Pprefix);
									player.sendMessage("FirstX: " + selection.getMinimumPoint().getBlockX());
									player.sendMessage("FirstY: " + selection.getMinimumPoint().getBlockY());
									player.sendMessage("FirstZ: " + selection.getMinimumPoint().getBlockZ());
									player.sendMessage("EndX: " + selection.getMaximumPoint().getBlockX());
									player.sendMessage("EndY: " + selection.getMaximumPoint().getBlockY());
									player.sendMessage("EndZ: " + selection.getMaximumPoint().getBlockZ());
									player.sendMessage("WorldName: " + selection.getWorld().getName());
								}
								int x1 = selection.getMinimumPoint().getBlockX();
								int y1 = selection.getMinimumPoint().getBlockY();
								int z1 = selection.getMinimumPoint().getBlockZ();
								int x2 = selection.getMaximumPoint().getBlockX();
								int y2 = selection.getMaximumPoint().getBlockY();
								int z2 = selection.getMaximumPoint().getBlockZ();
								ArenaSettings.SaveArea(player, arenaname, x1, x2, y1, y2, z1, z2);
								player.sendMessage(Splatoon.data.Pprefix + "ガチエリア範囲を保存しました");
								return true;
							}
						}
					}
					return false;
				}
				//##############################################################################################################
				else if((args.length == 1) && ("setup".equalsIgnoreCase(args[0])))
				{
					if(player.hasPermission("splat.create"))
					{
						//player.openInventory(EasySetup.Use(EasySetup.Q_es));
						AdminCommandList(player);
						return true;
					}
					return false;
				}
				//##############################################################################################################
				else if((args.length >= 2) && ("settings".equalsIgnoreCase(args[0])))
				{
					if(player.hasPermission("splat.settings"))
					{
						if((args.length == 6) && ("room".equalsIgnoreCase(args[1])))
						{
							int i = 0;
							String room = args[2];
							Splatoon.files.fileconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.RoomDirFiles(room));
							if("selectarena".equalsIgnoreCase(args[3]))
							{
								if("add".equalsIgnoreCase(args[4]))
								{
									ArenaSettings.SelectArena(room, args[5], true);
									player.sendMessage(Splatoon.data.Pprefix +
											ChatColor.GREEN + room + ChatColor.WHITE +
											" の選択ステージに " + ChatColor.YELLOW + args[5] +
											ChatColor.WHITE + " を"+ ChatColor.GREEN + "追加"+ ChatColor.WHITE+"しました");
									return true;
								}
								else if("remove".equalsIgnoreCase(args[4]))
								{
									ArenaSettings.SelectArena(room, args[5], false);
									player.sendMessage(Splatoon.data.Pprefix +
											ChatColor.GREEN + room + ChatColor.WHITE +
											" の選択ステージに " + ChatColor.YELLOW + args[5] +
											ChatColor.WHITE + " を"+ ChatColor.RED + "消去"+ ChatColor.WHITE+"しました");
									return true;
								}
							}
							else if("setMinPlayers".equalsIgnoreCase(args[3]))
							{
								try{
									i = Integer.valueOf(args[4]);
								} catch (NumberFormatException e) {
									player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "数字を入力してください！");
									return false;
								}
								Splatoon.files.fileconfig.set(room + ".MinPlayers", i);
							}
							else if("setMaxPlayers".equalsIgnoreCase(args[3]))
							{
								try{
									i = Integer.valueOf(args[4]);
								} catch (NumberFormatException e) {
									player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "数字を入力してください！");
									return false;
								}
								Splatoon.files.fileconfig.set(room + ".MaxPlayers", i);
							}
							Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.RoomDirFiles(room), true);
						}
						// /splat settings arena <arena> author <text>
						else if((args.length >= 4) && ("arena".equalsIgnoreCase(args[1])))
						{
							String arena = args[2];
							if(Splatoon.arenastatus.CheckArena(arena, true))
							{
								sender.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "その名前のステージは存在しません！");
								return false;
							}
							Splatoon.files.fileconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.ArenaDirFiles(arena));
							if((args.length == 5) && ("author".equalsIgnoreCase(args[3])))
							{
								Splatoon.files.fileconfig.set(arena +".Information.Author", args[4]);
								player.sendMessage(Splatoon.data.Pprefix + "ステージ制作者を設定しました " + ChatColor.YELLOW + args[4]);
								Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.ArenaDirFiles(arena), true);
								ArenaSettings.ReloadArena(arena);
								return true;
							}
							else if((args.length == 4) && ("allarea".equalsIgnoreCase(args[3])))
							{
								Splatoon.files.fileconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.ArenaDirFiles(arena));
								TurfBattle.CheckAllArea(arena);
								if(TurfBattle.allarea.get(arena) == null)
								{
									player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "ERROR!!" + ChatColor.YELLOW + " 総面積が計算できません");
									return false;
								}
								DecimalFormat df = new DecimalFormat("#####0");
								Splatoon.files.fileconfig.set(arena +".Information.Total_Area", Integer.valueOf(df.format(TurfBattle.allarea.get(arena))));
								player.sendMessage(Splatoon.data.Pprefix + "ステージ総面積を再読み込みしました " + ChatColor.YELLOW + df.format(TurfBattle.allarea.get(arena)));
								Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.ArenaDirFiles(arena), true);
								ArenaSettings.ReloadArena(arena);
								TurfBattle.allarea.remove(arena);
								return true;
							}
							else if((args.length == 5) && ("Description".equalsIgnoreCase(args[3])))
							{
								Splatoon.files.fileconfig.set(arena +".Information.Description", args[4]);
								player.sendMessage(Splatoon.data.Pprefix + "ステージ説明を設定しました " + ChatColor.YELLOW + args[4]);
								Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.ArenaDirFiles(arena), true);
								ArenaSettings.ReloadArena(arena);
								return true;
							}
						}
					}
				}
				//##############################################################################################################
				else if((args.length >= 2) && ("status".equalsIgnoreCase(args[0])))
				{
					if(player.hasPermission("splat.status"))
					{
						if((args.length == 3) && ("room".equalsIgnoreCase(args[1])))
						{
							String room = args[2];
							Splatoon.files.fileconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.RoomDirFiles(room));
							player.sendMessage(Splatoon.data.Pprefix);
							player.sendMessage(ChatColor.GREEN + "-----" + ChatColor.YELLOW + "RoomSettings" + ChatColor.GREEN + "-----");
							player.sendMessage(ChatColor.YELLOW + "Wait Position : ");
							player.sendMessage(ChatColor.AQUA + "  World : " + Splatoon.files.fileconfig.getString(room +".World"));
							player.sendMessage(ChatColor.AQUA + "  X : " + Splatoon.files.fileconfig.getInt(room +".x"));
							player.sendMessage(ChatColor.AQUA + "  Y : " + Splatoon.files.fileconfig.getInt(room +".y"));
							player.sendMessage(ChatColor.AQUA + "  Z : " + Splatoon.files.fileconfig.getInt(room +".z"));
							player.sendMessage(ChatColor.LIGHT_PURPLE + "SelectArenaList");
							for(String list : Splatoon.files.fileconfig.getStringList(room +".SelectArenas"))
							{
								if(ArenaSettings.SetupFinish(list))
									sender.sendMessage("- " + ChatColor.GREEN + list);
								else if(!ArenaSettings.SetupFinish(list))
									sender.sendMessage("- " + ChatColor.RED + list + ChatColor.BOLD + "     Setup is not finished !");
							}
							player.sendMessage(ChatColor.GREEN + "----------------------");
							return true;
						}
						else if((args.length == 3) && ("arena".equalsIgnoreCase(args[1])))
						{
							String arena = args[2];
							if(Splatoon.arenastatus.CheckArena(arena, true))
							{
								sender.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "その名前のステージは存在しません！");
								return false;
							}
							ArenaData arenadata = ArenaSettings.getArenaData(arena);
							player.sendMessage(Splatoon.data.Pprefix);
							player.sendMessage(ChatColor.GREEN + "-----" + ChatColor.YELLOW + "ArenaSettings" + ChatColor.GREEN + "-----");
							player.sendMessage(ChatColor.YELLOW + "ArenaInfo :");
							player.sendMessage(ChatColor.AQUA + "  Name : " + arena);
							player.sendMessage(ChatColor.AQUA + "  Author : " + arenadata.getAuthor());
							player.sendMessage(ChatColor.AQUA + "  Total area : " + arenadata.getAllArea());
							player.sendMessage(ChatColor.AQUA + "  Description : " + arenadata.getDescription());
							player.sendMessage(ChatColor.GREEN + "-----------------------");
							return true;
						}
					}
				}
				//##############################################################################################################
				else if((args.length == 1) && ("rate".equalsIgnoreCase(args[0])))
				{
					if(player.hasPermission("splat.play.rate"))
					{
						SplatPlayerStatus rate = new SplatPlayerStatus(player.getName());
						player.sendMessage(Splatoon.data.Pprefix);
						player.sendMessage(ChatColor.YELLOW+ "<>----------" +ChatColor.GREEN + "Your Rate" + ChatColor.YELLOW+ "----------<>");
						player.sendMessage(ChatColor.GOLD + "Wins: " + ChatColor.WHITE + rate.getWinCount());
						player.sendMessage(ChatColor.BLUE + "Loses: " + ChatColor.WHITE + rate.getLoseCount());
						player.sendMessage(ChatColor.GREEN + "MaxmumWinStreak: " + ChatColor.WHITE + rate.getMaximumWinStreak());
						player.sendMessage(ChatColor.GREEN + "WinStreak: " + ChatColor.WHITE + rate.getWinStreak());
						player.sendMessage(ChatColor.YELLOW+ "<>-----------------------------<>");
						return true;
					}
					return false;
				}
				//##############################################################################################################
				else if((args.length >= 2) && ("test".equalsIgnoreCase(args[0])))
				{
					if(Splatoon.data.DebugMode)
					{
						if("title".equalsIgnoreCase(args[1]))
							SplatTitle.titleTest();
						else if("title2".equalsIgnoreCase(args[1]))
							SplatTitle.titleTest_i();
						else if("title3".equalsIgnoreCase(args[1]))
							SplatTitle.titleTest_ii();
						else if("aa".equalsIgnoreCase(args[1]))
						{
							player.sendMessage("                                                   ＿");
							player.sendMessage("                                                   ｝  ｀丶");
							player.sendMessage("    ／￣￣＼                   ｢  ＼    ＿＿    -┴  ｧ   ＼          ／￣￣＼");
							player.sendMessage("  /                                  ´             /       ヽ     /");
							player.sendMessage("  |    魔     |                 ＼           __ く／         .     |    僕    |");
							player.sendMessage("  |    法     |                 ∨  __      '⌒  Ｙ             ｝    |    と   │");
							player.sendMessage("  |    少     l                _|{  '⌒  r:‐ﾍ     八           ,′    |    契   |");
							player.sendMessage("  |    女     |              ／ 八     ､:::ノ    ｲ 丶.        /     |    約   |");
							player.sendMessage("  |    に     │      ,   ⌒＼/  ,  /  ｰｧ          Ｔ´ {    ＼／＼     |   し   │");
							player.sendMessage("  |    な     |    (  (⌒ >く／ / . :/      ',: :ヽ :     〈） ）       |   て    |");
							player.sendMessage("  |    っ     |     丶  ＼{／:::/:::./            ヽ-ﾍ : : : ／／､                 /");
							player.sendMessage("  |    て     l   ／＼＼＿∧: : ,′|   ｉ   ｉ  |  ∨∧／／ : : :＼＿＼＿__／");
							player.sendMessage("  |    よ     |  (__ ﾟ: :｡ :ーｧ‐' : ;    |   |   |  |    ー‐く: : : : :｡: ﾟ : _ノ");
							player.sendMessage("  |    ！    │   /. : : :｡人: : :.:!    |   |   |  |          ＼: ｡ : : : :）");
							player.sendMessage("            /   (_／{:_:/   ＼:｛.       {  ,'        ﾉ       ＼_ﾉ￣");
							player.sendMessage("    ＼＿／                    ￣>   ＼) (／    く");
							player.sendMessage("                                 ／  ／￣￣￣＼  ＼");
							player.sendMessage("                                'ー‐          ー‐'");
							player.sendMessage("                                                                                          ");
						}
						else if("players".equalsIgnoreCase(args[1]))
							player.sendMessage(Splatoon.data.Pprefix + TurfBattle.JoinPlayersList(args[2], true).size());
						else if("enum".equalsIgnoreCase(args[1]))
							player.sendMessage(BattleTypeEnum.Turf_War.toString());
						else if("head".equalsIgnoreCase(args[1]))
							player.openInventory(SelectInv.OpenPlayerInv(player.getName()));
						else if("colormode".equalsIgnoreCase(args[1]))
							Splatoon.data.colormode = Integer.valueOf(args[2]);
						else if("invsave".equalsIgnoreCase(args[1]))
							Rollback.SaveRollBackInv(player);
						else if("invload".equalsIgnoreCase(args[1]))
							Rollback.InvRollBack(player);
						else if("intslot".equalsIgnoreCase(args[1]))
							player.openInventory(SelectInv.PlayerProfile(player.getName(), Integer.valueOf(args[2])));
						else if("NMS".equalsIgnoreCase(args[1]))
							System.out.println(Metrics.CheckNMS(args[2]));
						else if("NMS2".equalsIgnoreCase(args[1]))
							System.out.println(Metrics.CheckNMS());
						else if("arenas".equalsIgnoreCase(args[1]))
							for(ArenaData data : ArenaSettings.arenadata.values())
								System.out.println(data.getName());
						else if("arena".equalsIgnoreCase(args[1]))
						{
							ArenaData data = ArenaSettings.getArenaData(args[2]);
							System.out.println(data.getName());
							System.out.println(data.getWorld().getName());
						}
						else if("method".equalsIgnoreCase(args[1]))
						{
							for(Method method : JoinQuitEvents.class.getMethods())
							{
								System.out.println(method.getName());
							}
						}
						else if("PassCode".equalsIgnoreCase(args[1]))
							player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "Passcode " + ChatColor.AQUA + "[ " + ChatColor.WHITE + Metrics.a(args[2]) + ChatColor.AQUA + " ]");
						else if("fireworks".equalsIgnoreCase(args[1]))
						{
							final Firework firework = player.getLocation().getWorld().spawn(player.getLocation(), Firework.class);
							FireworkMeta meta = firework.getFireworkMeta();
							meta.addEffect(FireworkEffect.builder().withColor(Color.AQUA).withFade(Color.GREEN).flicker(true).build());
							firework.setFireworkMeta(meta);
							new BukkitRunnable() {
								public void run() {
									firework.detonate();
								}
							}.runTaskLater(Splatoon.instance, 1);
						}
						else if("particle".equalsIgnoreCase(args[1]))
						{
							WorldEditPlugin worldEdit = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
							Selection selection = worldEdit.getSelection(player);
							int x1b = selection.getMinimumPoint().getBlockX();
							int y1 = selection.getMinimumPoint().getBlockY();
							int z1b = selection.getMinimumPoint().getBlockZ();
							int x2b = selection.getMaximumPoint().getBlockX();
							int z2b = selection.getMaximumPoint().getBlockZ();
							int x1 = x1b, x2 = x2b;
							if(x1b < x2b)//大きいほうの値を最初のx座標にする
							{
								x1 = x2b;
								x2 = x1b;
							}
							int z1 = z1b, z2 = z2b;
							if(z1b < z2b)//大きいほうの値を最初のz座標にする
							{
								z1 = z2b;
								z2 = z1b;
							}
							new TestRunnable(Integer.valueOf(args[2]), 40, x1, x2, y1, z1, z2, args[3]).runTaskTimer(Splatoon.instance, 0, 5);
						}
						else if("particle2".equalsIgnoreCase(args[1]))
						{
							new TestRunnable(player, 40).runTaskTimer(Splatoon.instance, 0, 5);
						}
						return true;
					}
					return false;
				}
				//##############################################################################################################
				else if((args.length == 2) && ("top".equalsIgnoreCase(args[0])))
				{
					if(sender.hasPermission("splat.top"))
					{
						String type = args[1];
						if("win".equalsIgnoreCase(type))
						{
							player.sendMessage(Splatoon.data.Pprefix);
							player.sendMessage(ChatColor.YELLOW +"-----"+ ChatColor.RED +"Win Ranking"+ ChatColor.YELLOW +"-----");
							for(String top : SplatPlayerStatus.Ranking(".Rate.Win"))
								player.sendMessage(top);
							return true;
						}
						else if("rank".equalsIgnoreCase(type))
						{
							player.sendMessage(Splatoon.data.Pprefix);
							player.sendMessage(ChatColor.AQUA +"-----"+ ChatColor.GREEN +"Rank Ranking"+ ChatColor.AQUA +"-----");
							for(String top : SplatPlayerStatus.Ranking(".Status.Rank"))
								player.sendMessage(top);
						}
						else if("total".equalsIgnoreCase(type))
						{
							player.sendMessage(Splatoon.data.Pprefix);
							player.sendMessage(ChatColor.GREEN +"-----"+ ChatColor.GOLD +"Total Ranking"+ ChatColor.GREEN +"-----");
							for(String top : SplatPlayerStatus.Ranking(".Status.Total"))
								player.sendMessage(top);
						}
					}
					return false;
				}
				//##############################################################################################################
				else if((args.length == 1) && ("get".equalsIgnoreCase(args[0])))
				{
					if(sender.hasPermission("splat.weapons"))
					{
						Splatoon.data.Weapons();
						player.getInventory().removeItem(Splatoon.data.item[3][0]);
						if(!player.hasMetadata("SplatWeapons"))
							player.setMetadata("SplatWeapons", new FixedMetadataValue(Splatoon.instance , Splatoon.data.SHOOTER1));
						List<MetadataValue> weapons = player.getMetadata("SplatWeapons");
						String weapon = weapons.get(0).value().toString();
						Splatoon.instance.giveWeapon(player, weapon);
						for(int i = 3; i <= 8; i++)
						{
							player.getInventory().setItem(i, Splatoon.data.item[4][0]);
						}
						return true;
					}
					return false;
				}
				//##############################################################################################################
				else if((args.length == 1) && ("select".equalsIgnoreCase(args[0])))
				{
					if(sender.hasPermission("splat.weapons"))
					{
						Splatoon.data.Weapons();
						player.getInventory().addItem(Splatoon.data.item[3][0]);
						player.getInventory().setItem(8, Splatoon.data.item[4][1]);
						return true;
					}
					return false;
				}
				//##############################################################################################################
				else if((args.length == 2) && ("join".equalsIgnoreCase(args[0])))
				{
					if(sender.hasPermission("splat.play.join"))
					{
						String lobby = args[1];
						if(Splatoon.arenastatus.CheckArena(lobby, false))
						{
							sender.sendMessage(Splatoon.data.Pprefix + ChatColor.RED +"その名前の待機地点はありません！");
							return false;
						}
						Splatoon.battle.WaitPoint(player, lobby);
						return true;
					}
					return false;
				}
				//##############################################################################################################
				else if((args.length == 1) && ("leave".equalsIgnoreCase(args[0])))
				{
					if(sender.hasPermission("splat.play.leave"))
					{
						Splatoon.battle.Leave(player);
						return true;
					}
					return false;
				}
				//##############################################################################################################
				else if((args.length == 1) && ("vote".equalsIgnoreCase(args[0])))
				{
					if(player.hasPermission("splat.play.vote"))
					{
						if(!player.hasMetadata(Splatoon.data.ArenaMeta))
							return false;
						String arena = player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString();
						Splatoon.files.fileconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.ArenaDirFiles(arena));
						int vote = Splatoon.files.fileconfig.getInt(arena +".Information.Vote");
						Splatoon.files.fileconfig.set(arena +".Information.Vote", (vote + 1));
						Splatoon.instance.SettingFiles(Splatoon.files.fileconfig, SplatoonFiles.ArenaDirFiles(arena), true);
						player.sendMessage(Splatoon.data.Pprefix + ChatColor.AQUA + "投票ありがとうございます！");
						return true;
					}
				}
				else if((args.length >= 2) && ("friend".equalsIgnoreCase(args[0])))
				{
					if(player.hasPermission("splat.friend"))
					{
						if((args.length == 3) && ("invite".equalsIgnoreCase(args[1])))
						{
							if(!Metrics.b(args[2]))
							{
								player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "そのプレイヤーは鯖のアクセス履歴が無いため申請できません");
								return false;
							}
							if(player.getName().equalsIgnoreCase(args[2]))
							{
								player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "自分自身には送れません");
								return false;
							}
							SplatPlayerStatus status = new SplatPlayerStatus(args[2]);
							for(String name : status.getFriendList())
							{
								if(name.equalsIgnoreCase(args[2]))
								{
									player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "そのプレイヤーは既にフレンドにいます");
									return true;
								}
							}
							status.setInvitingList(player.getName());
							player.sendMessage(Splatoon.data.Pprefix + ChatColor.YELLOW + args[2] + ChatColor.GREEN + " にフレンド申請しました");
							Player invite = Bukkit.getPlayer(args[2]);
							if(invite != null)
							{
								invite.sendMessage(Splatoon.data.Pprefix + ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " からのフレンド申請が来ました");
								invite.sendMessage(Splatoon.data.Pprefix + ChatColor.AQUA + "許可する場合は" + ChatColor.GOLD + " /splat friend accept " + player.getName());
								invite.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "拒否する場合は" + ChatColor.GOLD + " /splat friend refuse " + player.getName());
							}
							return true;
						}
						else if((args.length == 3) && ("accept".equalsIgnoreCase(args[1])))
						{
							SplatPlayerStatus status = new SplatPlayerStatus(player.getName());
							if(status.setFriendAccept(args[2]))
							{
								player.sendMessage(Splatoon.data.Pprefix + ChatColor.YELLOW + args[2] + ChatColor.AQUA + " からの申請を許可しました");
								SplatPlayerStatus invitingadd = new SplatPlayerStatus(args[2]);
								invitingadd.setFriendList(player.getName());
							}
							else
								player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "そのプレイヤーからの申請はありません");
							return true;
						}
						else if((args.length == 3) && ("refuse".equalsIgnoreCase(args[1])))
						{
							SplatPlayerStatus status = new SplatPlayerStatus(player.getName());
							status.removeInvitingList(args[2]);
							player.sendMessage(Splatoon.data.Pprefix + ChatColor.YELLOW + args[2] + ChatColor.GREEN + " のフレンド申請を拒否しました");
							return true;
						}
						else if((args.length == 2) && ("invitelist".equalsIgnoreCase(args[1])))
						{
							SplatPlayerStatus status = new SplatPlayerStatus(player.getName());
							player.sendMessage(ChatColor.YELLOW + "あなたに対しての申請一覧");
							for(String name : status.getInvitingList())
								player.sendMessage("- " + ChatColor.GREEN + name);
							return true;
						}
					}
				}
				//##############################################################################################################
				else if((args.length == 2) && ("start".equalsIgnoreCase(args[0])))
				{
					String room = args[1];
					if(sender.hasPermission("splat.play.start"))
					{
						if(Splatoon.arenastatus.CheckArena(room, false))
						{
							sender.sendMessage(Splatoon.data.Pprefix + ChatColor.RED +"その名前の待機部屋はありません！");
							return false;
						}
						Splatoon.battle.LocsetPlayers(room);
						return true;
					}
					return false;
				}
				//##############################################################################################################
				/*else if((args.length == 2) && ("end".equalsIgnoreCase(args[0])))
				{
					String arena = args[1];
					if(sender.hasPermission("splat.create"))
					{
						if(Splatoon.arenastatus.CheckArena(arena, true))
						{
							if(Splatoon.arenastatus.getStatus(arena))
							player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED +"その名前のステージはありません！");
							return false;
						}
						if(!Splatoon.arenastatus.getStatus(arena))
						{
							player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED +"そのステージは有効化されてません！");
							return false;
						}
						if(!Splatoon.arenastatus.arenastatus.get(arena).equals(ArenaStatusEnum.INGAME))
						{
							player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED +"戦闘中ではありません！");
							return false;
						}
						BattleTypeEnum type = BattleTypeEnum.Turf_War;
						if((args.length == 4))
						{
							for(BattleTypeEnum types : BattleTypeEnum.values())
							{
								if(types.toString().equalsIgnoreCase(args[2]))
								{
									type = types;
									break;
								}
							}
						}
						Splatoon.battle.FinishBattle(arena, true, type);
						SplatTeam.resetBoard(arena, type);
						return true;
					}
				}*/
				//##############################################################################################################
				else if((args.length == 2) && ("spectate".equalsIgnoreCase(args[0])))
				{
					if(sender.hasPermission("splat.play.spectate"))
					{
						String arena = args[1];
						if(Splatoon.arenastatus.CheckArena(arena, true))
						{
							if(ArenaStatus.isCanUse(arena))
							player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED +"その名前のステージはありません！");
							return false;
						}
						if(!ArenaStatus.isCanUse(arena))
						{
							player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED +"そのステージは有効化されてません！");
							return false;
						}
						if(!ArenaStatus.arenastatus.get(arena).equals(ArenaStatusEnum.INGAME))
						{
							player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED +"戦闘中ではありません！");
							return false;
						}
						SignEntry.Spectate(arena, player);
						return true;
					}
				}
				//##############################################################################################################
				else if((args.length == 3) && ("delete".equalsIgnoreCase(args[0])))
				{
					if(sender.hasPermission("splat.delete"))
					{
						if("arena".equalsIgnoreCase(args[1]))
						{
							if(SplatoonFiles.ArenaDirFiles(args[2]).delete())
							{
								for(String room : SplatoonFiles.getRoomList())
									ArenaSettings.SelectArena(room, args[2], false);
								ArenaSettings.arenadata.remove(args[2]);
								if(sender instanceof Player)
									sender.sendMessage(Splatoon.data.Pprefix
											+ ChatColor.GREEN + args[2] + ChatColor.YELLOW + " を削除しました！");
								else
									Bukkit.getConsoleSender().sendMessage(Splatoon.data.LoggerPprefix
											+ ChatColor.GREEN + args[2] + ChatColor.YELLOW + " を削除しました！");
								ArenaStatus.arenastatus.remove(args[2]);
							}else{
								if(sender instanceof Player)
									sender.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + args[2]
											+ ChatColor.RED + " の名前のステージはありません！");
								else
									Bukkit.getConsoleSender().sendMessage(Splatoon.data.LoggerPprefix + ChatColor.GREEN + args[2]
											+ ChatColor.RED + " の名前のステージはありません！");
							}
							return true;
						}
						else if("room".equalsIgnoreCase(args[1]))
						{
							if(SplatoonFiles.RoomDirFiles(args[2]).delete())
							{
								if(sender instanceof Player)
									sender.sendMessage(Splatoon.data.Pprefix
											+ ChatColor.GREEN + args[2] + ChatColor.YELLOW + " を削除しました！");
								else
									Bukkit.getConsoleSender().sendMessage(Splatoon.data.LoggerPprefix
											+ ChatColor.GREEN + args[2] + ChatColor.YELLOW + " を削除しました！");
								ArenaStatus.arenastatus.remove(args[2]);
							}else{
								if(sender instanceof Player)
									sender.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + args[2]
											+ ChatColor.RED + " の名前の待機部屋はありません！");
								else
									Bukkit.getConsoleSender().sendMessage(Splatoon.data.LoggerPprefix + ChatColor.GREEN + args[2]
											+ ChatColor.RED + " の名前の待機部屋はありません！");
							}
							return true;
						}
						sender.sendMessage("arena か room を指定してください");
					}
					return false;
				}
				//##############################################################################################################
				/*else if((args.length == 1) && ("files".equalsIgnoreCase(args[0])))
				{
					if(sender.hasPermission("splat.filelist"))
					{
						player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "WeaponFiles");
						for(File file : Splatoon.files.weapondir.listFiles())
						{
							weaponfile = new WeaponFile(file);
							if(file.isFile())
								player.sendMessage(ChatColor.AQUA + "- " + weaponfile.WeaponName());
						}
						player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "SubFiles");
						for(File file : Splatoon.files.subdir.listFiles())
						{
							weaponfile = new WeaponFile(file);
							player.sendMessage(ChatColor.AQUA + "- " + weaponfile.WeaponName());
						}
						player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "SpecialFiles");
						for(File file : Splatoon.files.specialdir.listFiles())
						{
							weaponfile = new WeaponFile(file);
							player.sendMessage(ChatColor.AQUA + "- " + weaponfile.WeaponName());
						}
						return true;
					}
					return false;
				}*/
			}
			//##############################################################################################################
			if((args.length == 2) && ("announce".equalsIgnoreCase(args[0])))
			{
				if(sender.hasPermission("splat.create"))
				{
					if(args[1] == null)
					{
						sender.sendMessage("/splat announce <msg>");
						return false;
					}
					String msg = Splatoon.data.Pprefix + args[1];
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
					return true;
				}
			}
			//##############################################################################################################
			else if((args.length == 1) && ("reload".equalsIgnoreCase(args[0])))
			{
				if(sender.hasPermission("splat.reload"))
				{
					Splatoon.instance.ConfigReload();
					sender.sendMessage(Splatoon.data.Pprefix + ChatColor.AQUA + "Config.ymlを再読み込みしました");
					return true;
				}
				return false;
			}
			//##############################################################################################################
			else if((args.length == 1) && ("roomlist".equalsIgnoreCase(args[0])))
			{
				if(sender.hasPermission("splat.play.rooms"))
				{
					sender.sendMessage(Splatoon.data.Pprefix);
					for(String room : SplatoonFiles.getRoomList())
					{
						sender.sendMessage(ChatColor.AQUA + "- " + room);
					}
					return true;
				}
				return false;
			}
			//##############################################################################################################
			else if((args.length == 1) && ("list".equalsIgnoreCase(args[0])))
			{
				if(sender.hasPermission("splat.create"))
				{
					sender.sendMessage(Splatoon.data.Pprefix);
					sender.sendMessage(ChatColor.YELLOW + "[ " +ChatColor.RED + "Arenalist" +ChatColor.YELLOW+ " ]");
					for(String arena : SplatoonFiles.getArenaList())
					{
						if(ArenaSettings.SetupFinish(arena))
							sender.sendMessage("- " + ChatColor.GREEN + arena);
						else if(!ArenaSettings.SetupFinish(arena))
							sender.sendMessage("- " + ChatColor.RED + arena + ChatColor.BOLD + "     Setup is not finished !");
					}
					sender.sendMessage(ChatColor.YELLOW + "[ " +ChatColor.AQUA + "Roomlist" +ChatColor.YELLOW+ " ]");
					for(String room : SplatoonFiles.getRoomList())
					{
						sender.sendMessage("- " + ChatColor.GREEN + room);
					}
					return true;
				}
				return false;
			}
			//##############################################################################################################
			else if((args.length == 1) && ("colorlist".equalsIgnoreCase(args[0])))
			{
				if(sender.hasPermission("splat.create"))
				{
					sender.sendMessage(Splatoon.data.Pprefix);
					for(String color : Splatoon.data.colorname)
						sender.sendMessage("- "+ ChatColor.GREEN + color);
					sender.sendMessage(ChatColor.GREEN + "------------");
					return true;
				}
				return false;
			}
			//##############################################################################################################
			else if((args.length == 2) && ("DebugMode".equalsIgnoreCase(args[0])))
			{
				if(sender instanceof Player)
				{
					if(sender.hasPermission("splat.create"))
					{
						Player player = (Player) sender;
						if(Metrics.a(player.getUniqueId().toString(), args[1]))
						{
							if(Splatoon.data.DebugMode)
								Splatoon.data.DebugMode = false;
							else if(!Splatoon.data.DebugMode)
								Splatoon.data.DebugMode = true;
							sender.sendMessage(ChatColor.GREEN + "DebugModeを "+ Splatoon.data.DebugMode +" に変更しました");
							return true;
						}
						else
						{
							player.kickPlayer(ChatColor.RED + "デバッグモードを間違えました！" + "\n" + ChatColor.GREEN + "安全のためプラグインを無効化します");
							TurfBattle.End();
							Bukkit.getServer().getPluginManager().disablePlugin(Splatoon.instance);
							return false;
						}
					}
				}
			}
			//##############################################################################################################
			else if((args.length == 1) && ("report".equalsIgnoreCase(args[0])))
			{
				if(sender.hasPermission("splat.create"))
				{
					sender.sendMessage("レポートファイルを作成しました、ファイル内容をフォーラムに貼り付けてください");
					SplatoonFiles.CreateReport();
					return true;
				}
				return false;
			}
			//##############################################################################################################
			else if((args.length == 2) && ("rollback".equalsIgnoreCase(args[0])))
			{
				if(sender.hasPermission("splat.create"))
				{
					if(Splatoon.data.DebugMode)
					{
						if(!Splatoon.arenastatus.CheckArena(args[1], true))
						{
							Rollback.rollback(args[1]);
							return true;
						}
						sender.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "そのステージ名はありません！");
					}
				}
			}
			//##############################################################################################################
			else if((args.length == 1) && ("AllPlayerReset".equalsIgnoreCase(args[0])))
			{
				if(Splatoon.data.DebugMode)
				{
					for(Player player : Bukkit.getOnlinePlayers())
					{
						if(player.hasMetadata(Splatoon.data.ArenaMeta))
							player.removeMetadata(Splatoon.data.ArenaMeta, Splatoon.instance);
						if(player.hasMetadata(Splatoon.data.RoomMeta))
							player.removeMetadata(Splatoon.data.RoomMeta, Splatoon.instance);
						if(player.hasMetadata(Splatoon.data.WaitingMeta))
							player.removeMetadata(Splatoon.data.WaitingMeta, Splatoon.instance);
						if(player.hasMetadata(Splatoon.data.Team1Meta))
							player.removeMetadata(Splatoon.data.Team1Meta, Splatoon.instance);
						if(player.hasMetadata(Splatoon.data.Team2Meta))
							player.removeMetadata(Splatoon.data.Team2Meta, Splatoon.instance);
						if(player.hasMetadata(Splatoon.data.JPMeta))
							player.removeMetadata(Splatoon.data.JPMeta, Splatoon.instance);
					}
					return true;
				}
			}
			//##############################################################################################################
			if(sender instanceof Player)
			{
				Player player = (Player)sender;
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
				sender.sendMessage(Splatoon.data.Pprefix);
				if(sender.hasPermission("splat.create"))
					AdminCommandList(player);
				PlayerCommandList(player);
				return true;
			}
			else
			{
				Bukkit.getConsoleSender().sendMessage(Splatoon.data.LoggerPprefix + ChatColor.AQUA + "プレイヤーのみ使用可能コマンド");
				CommandList("/splat setup", "ステージ作成コマンド一覧を見る");
				CommandList("/splat setup setlobby", "ロビーの作成");
				CommandList("/splat setup <room> setroom", "待機部屋の作成 <room>:待機部屋名");
				CommandList("/splat setup <arena> setarena", "ステージの作成 <arena>:ステージ名");
				CommandList("/splat setup finish", "作成の終了");
				CommandList("/splat setup <arena> setspawnpoint 1 1", "チーム1のスポーンポイント1の設定");
				CommandList("/splat setup <arena> setspawnpoint 1 2", "チーム1のスポーンポイント2の設定");
				CommandList("/splat setup <arena> setspawnpoint 1 3", "チーム1のスポーンポイント3の設定");
				CommandList("/splat setup <arena> setspawnpoint 1 4", "チーム1のスポーンポイント4の設定");
				CommandList("/splat setup <arena> setspawnpoint 2 1", "チーム2のスポーンポイント1の設定");
				CommandList("/splat setup <arena> setspawnpoint 2 2", "チーム2のスポーンポイント2の設定");
				CommandList("/splat setup <arena> setspawnpoint 2 3", "チーム2のスポーンポイント3の設定");
				CommandList("/splat setup <arena> setspawnpoint 2 4", "チーム2のスポーンポイント4の設定");
				CommandList("/splat join <room>", "対象の待機部屋に入る <room>:待機部屋名");
				CommandList("/splat leave", "待機部屋から抜ける");
				CommandList("/splat start <room>", "8人満たしてない場合に開始する <room>:開始する待機部屋名");
				CommandList("/splat end <arena>", "ゲームを強制終了させる <arena>:終了させるステージ");
				CommandList("/splat delete arena <arena>", "ステージの削除 <arena>:対象のステージ名");
				CommandList("/splat delete room <room>", "待機部屋を削除する <room>:対象の待機部屋名");

				Bukkit.getConsoleSender().sendMessage(Splatoon.data.LoggerPprefix + ChatColor.RED + "コンソールでも使用可能コマンド");
				CommandList("/splat reload", "Config.ymlを再読み込みさせる");
				CommandList("/splat list", "ステージと待機部屋のリストを見る");
				CommandList("/splat colorlist", "塗ることが可能な色ブロックのリストを見る");
				return true;
			}
		}
		else
		{
			if(sender instanceof Player)
			{
				Player player = (Player)sender;
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
				sender.sendMessage(Splatoon.data.Pprefix);
				if(sender.hasPermission("splat.create"))
					AdminCommandList(player);
				PlayerCommandList(player);
				return true;
			}
			else
			{
				Bukkit.getConsoleSender().sendMessage(Splatoon.data.LoggerPprefix + ChatColor.AQUA + "プレイヤーのみ使用可能コマンド");
				CommandList("/splat setup", "ステージ作成コマンド一覧を見る");
				CommandList("/splat setup setlobby", "ロビーの作成");
				CommandList("/splat setup <room> setroom", "待機部屋の作成 <room>:待機部屋名");
				CommandList("/splat setup <arena> setarena", "ステージの作成 <arena>:ステージ名");
				CommandList("/splat setup finish", "作成の終了");
				CommandList("/splat setup <arena> setspawnpoint 1 1", "チーム1のスポーンポイント1の設定");
				CommandList("/splat setup <arena> setspawnpoint 1 2", "チーム1のスポーンポイント2の設定");
				CommandList("/splat setup <arena> setspawnpoint 1 3", "チーム1のスポーンポイント3の設定");
				CommandList("/splat setup <arena> setspawnpoint 1 4", "チーム1のスポーンポイント4の設定");
				CommandList("/splat setup <arena> setspawnpoint 2 1", "チーム2のスポーンポイント1の設定");
				CommandList("/splat setup <arena> setspawnpoint 2 2", "チーム2のスポーンポイント2の設定");
				CommandList("/splat setup <arena> setspawnpoint 2 3", "チーム2のスポーンポイント3の設定");
				CommandList("/splat setup <arena> setspawnpoint 2 4", "チーム2のスポーンポイント4の設定");
				CommandList("/splat join <room>", "対象の待機部屋に入る <room>:待機部屋名");
				CommandList("/splat leave", "待機部屋から抜ける");
				CommandList("/splat start <room>", "8人満たしてない場合に開始する <room>:開始する待機部屋名");
				CommandList("/splat end <arena>", "ゲームを強制終了させる <arena>:終了させるステージ");
				CommandList("/splat delete arena <arena>", "ステージの削除 <arena>:対象のステージ名");
				CommandList("/splat delete room <room>", "待機部屋を削除する <room>:対象の待機部屋名");

				Bukkit.getConsoleSender().sendMessage(Splatoon.data.LoggerPprefix + ChatColor.RED + "コンソールでも使用可能コマンド");
				CommandList("/splat reload", "Config.ymlを再読み込みさせる");
				CommandList("/splat list", "ステージと待機部屋のリストを見る");
				CommandList("/splat colorlist", "塗ることが可能な色ブロックのリストを見る");
				return true;
			}
		}
	}

	public void CommandList(String command, String description)
	{
		Bukkit.getConsoleSender().sendMessage(Splatoon.data.LoggerPprefix + ChatColor.GREEN + command + " " + ChatColor.YELLOW + description);
	}

	public static void AdminCommandList(Player player)
	{
	player.sendMessage(ChatColor.GREEN +"<>"+ChatColor.STRIKETHROUGH+"---------------"+ChatColor.RESET+""+ChatColor.RED
			+"CommandList"+ChatColor.GREEN+""+ChatColor.STRIKETHROUGH+"---------------"+ChatColor.RESET+""+ChatColor.GREEN+"<>");
	Splatoon.msgbutton.ButtonCreate(player, "/splat setup setlobby"
			, "立っている場所をロビーとして設定します"
			, "/splat setup setlobby"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat setup <room> setroom"
			, "現在たっている場所を待機地点として設定します"
				, "/splat setup <room> setroom"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat setup <arena> setarena"
			, "WEで指定した範囲をステージとして設定します"
			, "<arena> - ステージ名"
			, "/splat setup <arena> setarena"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat setup <arena> setspawnpoint <TeamNum> <PlayerNum>"
			, "今立っている場所をスポーンポイントとして設定します"
			, "<arena> - ステージ名"
			, "<TeamNum> - 1チームか2チームか(1 - 2)"
			, "<PlayerNum> - プレイヤーの番号(1 - 4)"
			, "/splat setup <arena> setspawnpoint <TeamNum> <PlayerNum>"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat setup <arena> finish"
			, "ステージの設定を終了して使用可能にします"
			, "<arena> - ステージ名"
			, "/splat setup <arena> finish"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat delete arena <arena>"
			, "ステージを削除します"
			, "<arena> - ステージ名"
			, "/splat delete arena <arena>"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat delete room <room>"
			, "待機部屋を削除します"
			, "<room> - 待機部屋名"
			, "/splat delete room <room>"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat end <arena>"
			, "指定したステージの戦闘を強制終了します"
			, "<arena> - ステージ名"
			, "/splat end <arena>"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat list"
			, "設定済みのステージと待機部屋のリストを表示します"
			, "/splat list"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat select"
			, "ブキ選択ツールを取得します"
			, "/splat select"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat get"
			, "選択済みのブキを取得します"
			,"/splat get"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat reload"
			, "コンフィグを再読み込みします"
			, "/splat get"
			, ClickEventType.SUGGEST_COMMAND);
	player.sendMessage(ChatColor.GREEN +"<>"+ChatColor.STRIKETHROUGH+"-----------------------------------------"
			+ChatColor.RESET+""+ChatColor.GREEN+"<>");
	}

	public void PlayerCommandList(Player player)
	{
	player.sendMessage(ChatColor.GREEN +"<>"+ChatColor.STRIKETHROUGH+"---------------"+ChatColor.RESET+""+ChatColor.YELLOW
			+"CommandList"+ChatColor.GREEN+""+ChatColor.STRIKETHROUGH+"---------------"+ChatColor.RESET+""+ChatColor.GREEN+"<>");
	Splatoon.msgbutton.ButtonCreate(player, "/splat join <room>"
			, "スプラトゥーンに参加します"
			, "<room> 待機部屋名"
			, "/splat roomlistで待機部屋名一覧が見れます"
			, "/splat join <room>"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat leave"
			, "参加をキャンセルします"
			, "/splat leave"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat rate"
			, "自分の勝敗データ、連勝回数を見る"
			, "/splat rate"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat roomlist"
			, "待機部屋名一覧を見ます"
			, "/splat roomlist"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat top win"
			, "勝数のランキングを見ます"
			, "/splat top win"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat top rank"
			, "ランクのランキングを見ます"
			, "/splat top rank"
			, ClickEventType.SUGGEST_COMMAND);
	Splatoon.msgbutton.ButtonCreate(player, "/splat top total"
			, "合計量のランキングを見ます"
			, "/splat top total"
			, ClickEventType.SUGGEST_COMMAND);
	player.sendMessage(ChatColor.GREEN +"<>"+ChatColor.STRIKETHROUGH+"-----------------------------------------"
			+ChatColor.RESET+""+ChatColor.GREEN+"<>");
	}
}
