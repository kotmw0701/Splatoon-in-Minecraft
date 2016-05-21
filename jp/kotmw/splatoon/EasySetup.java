/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon;

import jp.kotmw.splatoon.arena.ArenaSettings;
import jp.kotmw.splatoon.arena.ArenaStatus;
import jp.kotmw.splatoon.arena.ArenaStatusEnum;
import jp.kotmw.splatoon.battle.BattleTypeEnum;
import jp.kotmw.splatoon.commands.SystemCommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class EasySetup implements Listener
{
	private static String D_setlobby = ChatColor.GREEN + "ロビー設定";
	private static String D_setroom = ChatColor.GREEN + "待機部屋設定";
	private static String D_create = ChatColor.GOLD + "ステージ作成";
	private static String D_setspawn = ChatColor.GOLD + "スポーンポイント設置";
	private static String D_othermenu = ChatColor.AQUA + "その他のメニュー";
	private static String D_eixt = ChatColor.RED + "閉じる";

	private static String D_yes = ChatColor.GREEN + "Yes";
	private static String D_no = ChatColor.RED + "No";

	public static String Q_es = "簡単セットアップを使用しますか？";
	public static String Q_sl = "現在地をロビーに設定しますか？";
	public static String Q_sr = "現在地を待機部屋に設定しますか？";
	public static String Q_ca = "現在の選択範囲をステージに設定しますか？";
	public static String Q_sp = "どのチームの何人目を設定しますか？";

	public static Inventory Use(String question)
	{
		ItemStack yes = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemMeta yes_meta = yes.getItemMeta();
		yes_meta.setDisplayName(D_yes);
		yes.setItemMeta(yes_meta);

		ItemStack no = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemMeta no_meta = no.getItemMeta();
		no_meta.setDisplayName(D_no);
		no.setItemMeta(no_meta);

		Inventory inv = Bukkit.createInventory(null, 9*1, question);

		inv.setItem(1, yes);
		inv.setItem(7, no);

		return inv;
	}

	private static Inventory Menu()
	{
		ItemStack setlobby = new ItemStack(Material.COMPASS);
		ItemMeta setlobby_meta = setlobby.getItemMeta();
		setlobby_meta.setDisplayName(D_setlobby);

		setlobby.setItemMeta(setlobby_meta);

		ItemStack setroom = new ItemStack(Material.EYE_OF_ENDER);
		ItemMeta setroom_meta = setroom.getItemMeta();
		setroom_meta.setDisplayName(D_setroom);

		setroom.setItemMeta(setroom_meta);

		ItemStack create = new ItemStack(Material.WOOD_AXE);
		ItemMeta create_meta = create.getItemMeta();
		create_meta.setDisplayName(D_create);

		create.setItemMeta(create_meta);

		ItemStack setspawn = new ItemStack(Material.BEACON);
		ItemMeta setspawn_meta = setspawn.getItemMeta();
		setspawn_meta.setDisplayName(D_setspawn);

		setspawn.setItemMeta(setspawn_meta);

		ItemStack othermenu = new ItemStack(Material.BOOK);
		ItemMeta othermenu_meta = othermenu.getItemMeta();
		othermenu_meta.setDisplayName(D_othermenu);

		othermenu.setItemMeta(othermenu_meta);

		ItemStack exit = new ItemStack(Material.WOOD_DOOR);
		ItemMeta exit_meta = exit.getItemMeta();
		exit_meta.setDisplayName(D_eixt);

		exit.setItemMeta(exit_meta);

		Inventory inv = Bukkit.createInventory(null, 9*1, "何する？");

		inv.setItem(0, setlobby);
		inv.setItem(1, setroom);
		inv.setItem(3, create);
		inv.setItem(4, setspawn);
		inv.setItem(6, othermenu);
		inv.setItem(8, exit);

		return inv;
	}



	@EventHandler
	public void onMenuClick(InventoryClickEvent e)
	{
		Player player = (Player)e.getWhoClicked();
		if(e.getInventory().getName().equalsIgnoreCase("何する？"))//メインメニュー
		{
			e.setCancelled(true);

			if(e.getCurrentItem() == null
					|| e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
				return;
			String itemname = e.getCurrentItem().getItemMeta().getDisplayName();
			if(itemname.equalsIgnoreCase(D_setlobby))
				player.openInventory(Use(Q_sl));
			else if(itemname.equalsIgnoreCase(D_setroom))
				player.openInventory(Use(Q_sr));
			else if(itemname.equalsIgnoreCase(D_create))
				player.openInventory(Use(Q_ca));
			else if(itemname.equalsIgnoreCase(D_setspawn))
			{

			}
			else if(itemname.equalsIgnoreCase(D_othermenu))
			{

			}
			else if(itemname.equalsIgnoreCase(D_eixt))
			{
				player.closeInventory();
				return;
			}
		}

		if(e.getInventory().getName().equalsIgnoreCase(Q_es))//簡単セットアップの使用有無の選択
		{
			e.setCancelled(true);

			if(e.getCurrentItem() == null
					|| e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
				return;
			if(getYN(e.getCurrentItem().getItemMeta().getDisplayName()))
			{
				player.openInventory(Menu());
				return;
			}
			else if(getYN(e.getCurrentItem().getItemMeta().getDisplayName()))
			{
				SystemCommands.AdminCommandList(player);
				player.closeInventory();
			}
		}

		if(e.getInventory().getName().equalsIgnoreCase(Q_sl))//ロビーの設定をしますか？
		{
			e.setCancelled(true);

			if(e.getCurrentItem() == null
					|| e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
				return;
			if(getYN(e.getCurrentItem().getItemMeta().getDisplayName()))
			{
				Location loc = player.getLocation();
				ArenaSettings.SaveLobby(loc.getWorld().getName(),
						loc.getBlockX(),
						loc.getBlockY(),
						loc.getBlockZ());
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "ロビーの設定をしました！");
				player.closeInventory();
			}
			else if(getYN(e.getCurrentItem().getItemMeta().getDisplayName()))
				player.closeInventory();
		}

		if(e.getInventory().getName().equalsIgnoreCase(Q_sr))//待機部屋の設定をしますか？
		{
			e.setCancelled(true);

			if(e.getCurrentItem() == null
					|| e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
				return;
			if(getYN(e.getCurrentItem().getItemMeta().getDisplayName()))
			{
				Location loc = player.getLocation();
				int i = 1;
				String waitroom = "waitroom";
				String waitroom_x = waitroom;
				if(!Splatoon.arenastatus.CheckArena(waitroom, false))
					do
					{
						waitroom_x = waitroom + "_" + i;
						i++;
					}
					while(!Splatoon.arenastatus.CheckArena(waitroom_x, false));
				ArenaSettings.SaveRoomLocation(player, waitroom_x,
						loc.getWorld().getName(),
						loc.getBlockX(),
						loc.getBlockY(),
						loc.getBlockZ(),
						BattleTypeEnum.Turf_War);
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "待機部屋の設定をしました！ "
						+ ChatColor.WHITE +"【"+ ChatColor.AQUA +waitroom_x+ ChatColor.WHITE +"】");
				player.closeInventory();
			}
			else if(getYN(e.getCurrentItem().getItemMeta().getDisplayName()))
				player.closeInventory();
		}

		if(e.getInventory().getName().equalsIgnoreCase(Q_ca))//ステージ作成しますか？
		{
			e.setCancelled(true);

			if(e.getCurrentItem() == null
					|| e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
				return;
			if(getYN(e.getCurrentItem().getItemMeta().getDisplayName()))
			{
				WorldEditPlugin worldEdit = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
				Selection selection = worldEdit.getSelection(player);
				if(selection != null)
				{
					int i = 1;
					String arena = "splat_arena";
					String arena_x = arena;
					if(!Splatoon.arenastatus.CheckArena(arena, false))
						do
						{
							arena_x = arena + "_" + i;
							i++;
						}
						while(!Splatoon.arenastatus.CheckArena(arena_x, false));
					String worldName = selection.getWorld().getName();
					int x1 = selection.getMinimumPoint().getBlockX();
					int y1 = selection.getMinimumPoint().getBlockY();
					int z1 = selection.getMinimumPoint().getBlockZ();
					int x2 = selection.getMaximumPoint().getBlockX();
					int y2 = selection.getMaximumPoint().getBlockY();
					int z2 = selection.getMaximumPoint().getBlockZ();
					ArenaStatus.arenastatus.put(arena, ArenaStatusEnum.DISABLED);
					if(!ArenaSettings.SaveArenaSetting(player, arena_x, worldName, x1, x2, y1, y2, z1, z2))
					{
						player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "着色可能ブロックを1つ以上構築してください");
						SplatoonFiles.ArenaDirFiles(arena).delete();
					}
				}
				player.closeInventory();
			}
			else if(getYN(e.getCurrentItem().getItemMeta().getDisplayName()))
				player.closeInventory();
		}
	}

	private boolean getYN(String name)
	{
		return name.equalsIgnoreCase(D_yes);
	}

	/*
	 * 0  1  2  3  4  5  6  7  8
	 * 9  10 11 12 13 14 15 16 17
	 * 18 19 20 21 22 23 24 25 26
	 * 27 28 29 30 31 32 33 34 35
	 * 36 37 38 39 40 41 42 43 44
	 * 45 46 47 48 49 50 51 52 53
	 */

}
