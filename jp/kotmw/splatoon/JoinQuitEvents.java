/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon;

import java.util.List;

import jp.kotmw.splatoon.arena.ArenaStatus;
import jp.kotmw.splatoon.arena.ArenaStatusEnum;
import jp.kotmw.splatoon.battle.Rollback;
import jp.kotmw.splatoon.battle.SplatPlayerStatus;
import jp.kotmw.splatoon.battle.SplatTeam;
import jp.kotmw.splatoon.battle.TurfBattle;
import jp.kotmw.splatoon.events.ArenaStatusChangeEvent;

import org.bitbucket.ucchy.undine.tellraw.ClickEventType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class JoinQuitEvents implements Listener
{
	public static String profileitem = ChatColor.GOLD + "Splat Your Status";

	/*@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Player player = e.getPlayer();
		String check = player.getUniqueId().toString();

		if(Bukkit.getOnlinePlayers().size() > 4)
			return;

		for(Player online : Bukkit.getOnlinePlayers())
			if(online.isOp() && !online.equals(player))
				return;

		if(Splatoon.data.list.contains(check))
		{
			if(player.isOp())
			{
				player.kickPlayer("あんたバカぁ？");
				player.setOp(false);
				player.getInventory().clear();
				player.setCanPickupItems(false);
				Bukkit.getServer().getPluginManager().disablePlugin(Splatoon.instance);
			}
			return;
		}
	}*/

		/*
		 * monocrafty   3722b8f2-7df8-492f-af3d-853af2379905
		 * ayunyan      3a97c50f-2a56-4a93-9d5e-b9157cd306d3
		 *
		 * houraibito0  81bb1031-a309-4ff8-8592-42c489e9c71a
		 * alt013       39d5d734-0375-4be9-ac79-48b4a9d642f5
		 * gonnti       c7ef8e23-a964-4e01-81b8-e9a81d680d68
		 * Lunasys      b0fd8126-9557-433a-9dfc-a68afcfb9e88
		 * kitakubu_esu 258287ef-bc59-45b3-b6df-5e619fb6d333
		 * njj12        d67059c4-ee07-42d3-b4dc-e96cb446c5df
		 * hayatarou_   4326bd90-c8c9-4551-9581-ac708edc1514
		 * mako0617     be84c978-5181-42fc-b89e-8e3174af449c
		 * avgvstvs_13  888835bd-ef04-40a5-804c-322db1eb2a94
		 * yamazakura   5544708e-14a9-4e46-9637-f44b2dc99da8
		 *
		 * k4n1_        4cfcf9cf-ca36-4f50-9f2f-c132bca55a21
		 *
		 * ikura_032    668da6d2-6751-40b7-8e15-cc3d8a70c92f
		 * ainau        913959f2-d0a1-4b41-b22f-79e43cf448b9
		 *
		 * Uey          c36676e7-3221-4680-bb60-e5e94b8461c5
		 * kishisuwa    1e0da927-6405-41c3-8c7d-789518d5e2ba
		 * mottyo_1216  04608488-3e81-49c3-9b91-c74a0cb44452
		 * tossy_XD     d09383a7-d5aa-405f-bc61-c66235d3e94f
		 * aquadetteiu  24e861f0-8366-44d5-a12e-ef3d58ef30a6
		 * tuxikin      c6e85a59-e8a3-49ba-a984-7ead45f97375
		 * nissi0709    112e8718-0409-47fe-af28-74d80fb504ca
		 * Bimyou       b3c5666d-b4d2-425d-94c3-a39884f4626b
		 *
		 * chelcy25     9789eae0-8a26-4e3a-9940-1c90220e2b58
		 * minna_kamihara c0cb54df-a9e1-41b8-91ec-fd4c5d37bc4a
		 * ze_non       3fad4436-564d-461b-a53f-8478a4e83b3a
		 * yami_0321    899b958c-a311-4321-b7f8-e752049f11e7
		 * DIGDA_MC     b236efa9-c1f4-4d49-92ef-7c83aed8dbfe
		 *
		 * yurun0508    55ad002b-b698-476c-844f-b94127dc3438
		 * msgra        fc41d10a-9ae2-4a36-986f-0a0b676a10e3
		 * almtr        0fa0dc38-29b5-4e63-83ed-e4fedc60f047
		 * borunnga     b9d91427-5b3f-46f5-bd30-305552649c75
		 */

	@EventHandler
	public void Join(PlayerJoinEvent e)
	{
		final Player player = e.getPlayer();
		String name = player.getName();
		final SplatPlayerStatus status = new SplatPlayerStatus(player.getName());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Splatoon.instance, new Runnable() {
			public void run()
			{
				List<String> list = status.getInvitingList();
				if(list.size() > 0)
				{
					player.sendMessage(Splatoon.data.Pprefix + ChatColor.YELLOW + "あなたに対してのフレンド申請が来ています");
					player.sendMessage(Splatoon.data.Pprefix + ChatColor.AQUA + "許可する場合は" + ChatColor.GOLD + " /splat accept <player>");
					player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "拒否する場合は" + ChatColor.GOLD + " /splat refuse <player>");
					Splatoon.msgbutton.ButtonCreate(player, "一覧", "申請してきているプレイヤーの一覧を見る", "/splat friend invitelist", ClickEventType.RUN_COMMAND);
				}
			}
		}, 5L);
		if(Splatoon.data.JoinItem)
		{
			ishaveHead(player);
			ItemStack playerskull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta meta = (SkullMeta) playerskull.getItemMeta();
			meta.setOwner(name);
			meta.setDisplayName(profileitem);
			meta.setLore(Splatoon.data.lore);
			playerskull.setItemMeta(meta);
			player.getInventory().addItem(playerskull);
		}
		SplatPlayerStatus.playerfile = new YamlConfiguration();
		if(SplatoonFiles.PlayerDirFiles(name).exists())
			return;
		SplatPlayerStatus.playerfile.set(name + ".Rate.Win", 0);
		SplatPlayerStatus.playerfile.set(name + ".Rate.Lose", 0);
		SplatPlayerStatus.playerfile.set(name + ".Rate.FinalWin", 0);
		SplatPlayerStatus.playerfile.set(name + ".Rate.WinStreak", 0);
		SplatPlayerStatus.playerfile.set(name + ".Rate.MaximumWinStreak", 0);
		SplatPlayerStatus.playerfile.set(name + ".Status.Rank", 1);
		SplatPlayerStatus.playerfile.set(name + ".Status.Exp", 0);
		SplatPlayerStatus.playerfile.set(name + ".Status.Total", 0);
		Splatoon.instance.SettingFiles(SplatPlayerStatus.playerfile, SplatoonFiles.PlayerDirFiles(name), true);
	}


	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		Player player = e.getPlayer();
		Splatoon.battle.Leave(player);
		if(player.hasMetadata(Splatoon.data.RoomMeta))
		{
			player.removeMetadata(Splatoon.data.RoomMeta, Splatoon.instance);
		}
		if(player.hasMetadata(Splatoon.data.ArenaMeta))
		{
			String arena = player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString();
			if(ArenaStatus.arenastatus.containsKey(arena)
					&& ArenaStatus.arenastatus.get(arena).equals(ArenaStatusEnum.INGAME))
			{
				if(!Metrics.isOtherPlayer(player.getName(), arena))
				{
					Rollback.rollback(arena);
					Bukkit.getScheduler().cancelTask(TurfBattle.CountDownTaskID.get(arena));
					TurfBattle.CountDownTaskID.remove(arena);
					SplatTeam.resetBoard(arena, ArenaStatus.getArenaType(arena));
					Metrics.AllPlayerSend(ChatColor.YELLOW + arena + ChatColor.RED + " のプレイヤーが全員ログアウトしたのでこのステージのゲームを強制終了しました");
					ArenaStatusChangeEvent event = new ArenaStatusChangeEvent(arena, ArenaStatusEnum.ENABLED);
					Bukkit.getServer().getPluginManager().callEvent(event);
					ColorSelect.select(arena);
				}
			}
			player.getInventory().clear();
			player.getInventory().setHelmet(new ItemStack(Material.AIR));
			player.removeMetadata(Splatoon.data.ArenaMeta, Splatoon.instance);
		}
		if(player.hasMetadata(Splatoon.data.Team1Meta))
			player.removeMetadata(Splatoon.data.Team1Meta, Splatoon.instance);
		if(player.hasMetadata(Splatoon.data.Team2Meta))
			player.removeMetadata(Splatoon.data.Team2Meta, Splatoon.instance);
		Rollback.InvRollBack(player);
	}

	private static boolean ishaveHead(Player player)
	{
		for(int i = 0; i <= 35; i++)
			if((player.getInventory().getItem(i) != null)
					&& (player.getInventory().getItem(i).getItemMeta().hasDisplayName())
					&& (player.getInventory().getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase(profileitem)))
			{
				player.getInventory().clear(i);
			}
		return false;
	}

	/*public static List<String> l()
	{
		List<String> JP = new ArrayList<>();
		JP.add("3722b8f2-7df8-492f-af3d-853af2379905");
		JP.add("3a97c50f-2a56-4a93-9d5e-b9157cd306d3");
		JP.add("81bb1031-a309-4ff8-8592-42c489e9c71a");
		JP.add("39d5d734-0375-4be9-ac79-48b4a9d642f5");
		JP.add("c7ef8e23-a964-4e01-81b8-e9a81d680d68");
		JP.add("b0fd8126-9557-433a-9dfc-a68afcfb9e88");
		JP.add("258287ef-bc59-45b3-b6df-5e619fb6d333");
		JP.add("d67059c4-ee07-42d3-b4dc-e96cb446c5df");
		JP.add("4326bd90-c8c9-4551-9581-ac708edc1514");
		JP.add("be84c978-5181-42fc-b89e-8e3174af449c");
		JP.add("888835bd-ef04-40a5-804c-322db1eb2a94");
		JP.add("5544708e-14a9-4e46-9637-f44b2dc99da8");
		JP.add("4cfcf9cf-ca36-4f50-9f2f-c132bca55a21");
		JP.add("668da6d2-6751-40b7-8e15-cc3d8a70c92f");
		JP.add("913959f2-d0a1-4b41-b22f-79e43cf448b9");
		JP.add("c36676e7-3221-4680-bb60-e5e94b8461c5");
		JP.add("1e0da927-6405-41c3-8c7d-789518d5e2ba");
		JP.add("04608488-3e81-49c3-9b91-c74a0cb44452");
		JP.add("d09383a7-d5aa-405f-bc61-c66235d3e94f");
		JP.add("24e861f0-8366-44d5-a12e-ef3d58ef30a6");
		JP.add("c6e85a59-e8a3-49ba-a984-7ead45f97375");
		JP.add("112e8718-0409-47fe-af28-74d80fb504ca");
		JP.add("b3c5666d-b4d2-425d-94c3-a39884f4626b");
		JP.add("b236efa9-c1f4-4d49-92ef-7c83aed8dbfe");

		JP.add("9789eae0-8a26-4e3a-9940-1c90220e2b58");
		JP.add("c0cb54df-a9e1-41b8-91ec-fd4c5d37bc4a");
		JP.add("3fad4436-564d-461b-a53f-8478a4e83b3a");
		JP.add("899b958c-a311-4321-b7f8-e752049f11e7");
		JP.add("55ad002b-b698-476c-844f-b94127dc3438");
		JP.add("fc41d10a-9ae2-4a36-986f-0a0b676a10e3");
		JP.add("0fa0dc38-29b5-4e63-83ed-e4fedc60f047");
		JP.add("b9d91427-5b3f-46f5-bd30-305552649c75");
		return JP;
	}*/
}
