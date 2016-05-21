/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon;

import java.util.ArrayList;
import java.util.List;

import jp.kotmw.splatoon.battle.SplatPlayerStatus;
import jp.kotmw.splatoon.battle.TurfBattle;
import jp.kotmw.splatoon.weapons.MainWeaponType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

public class SelectInv implements Listener
{
	public static String weaponmeta = "SplatWeapons";

	public void openGUI(Player player)
	{
		Inventory inv;
		Splatoon.data.Weapons();
		inv = Bukkit.createInventory(null, 9, ChatColor.DARK_PURPLE
				+ "Splat weapon selector");

		ItemStack Shooter = new ItemStack(Material.WOOD_HOE);
		ItemMeta ShooterMeta = Shooter.getItemMeta();
		ShooterMeta.setDisplayName(ChatColor.GREEN + "SplatShooter");
		ShooterMeta.setLore(setLore(MainWeaponType.Shooter));
		Shooter.setItemMeta(ShooterMeta);


		ItemStack Roller = new ItemStack(Material.STICK);
		ItemMeta RollerMeta = Roller.getItemMeta();
		RollerMeta.setDisplayName(ChatColor.BLUE + "SplatRoller");
		RollerMeta.setLore(setLore(MainWeaponType.Roller));
		Roller.setItemMeta(RollerMeta);

		ItemStack Charger = new ItemStack(Material.BOW);
		ItemMeta ChargerMeta = Charger.getItemMeta();
		ChargerMeta.setDisplayName(ChatColor.GOLD + "SplatCharger");
		ChargerMeta.setLore(setLore(MainWeaponType.Charger));
		Charger.setItemMeta(ChargerMeta);

		//012345678
		inv.setItem(1, Shooter);
		inv.setItem(4, Roller);
		inv.setItem(7, Charger);

		player.openInventory(inv);
	}

	public void ShooterSelect(Player player)
	{
		Inventory inv;
		inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN
				+ "Shooter select");

		inv.setItem(4, Splatoon.data.item[0][0]);
		//inv.setItem(4, Splatoon.data.item[0][1]);
		//inv.setItem(7, Splatoon.data.item[0][2]);

		player.openInventory(inv);
	}

	public void RoolerSelect(Player player)
	{
		Inventory inv;
		inv = Bukkit.createInventory(null, 9, ChatColor.DARK_AQUA
				+ "Roller select");

		inv.setItem(4, Splatoon.data.item[1][0]);
		//inv.setItem(7, Splatoon.data.item[1][1]);

		player.openInventory(inv);
	}

	public void ChargerSelect(Player player)
	{
		Inventory inv;
		inv = Bukkit.createInventory(null, 9, ChatColor.GOLD
				+ "Charger select");

		inv.setItem(4, Splatoon.data.item[2][0]);

		player.openInventory(inv);
	}

	private static List<String> setLore(MainWeaponType type)
	{
		List<String> lore = new ArrayList<String>();
		switch(type)
		{
		case Shooter:
			lore.add(ChatColor.GREEN + "右クリックで玉発射！");
			lore.add(ChatColor.YELLOW + "連射可能");
			break;
		case Roller:
			lore.add(ChatColor.GREEN + "持って動き回るだけで塗れます");
			lore.add(ChatColor.YELLOW + "クリックで塗る、塗らないの切り替え可能");
			break;
		case Charger:
			lore.add(ChatColor.GREEN + "普通に矢を打つだけ");
			lore.add(ChatColor.YELLOW + "援護射撃向き");
			break;
		}
		return lore;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		if(!player.hasMetadata(Splatoon.data.RoomMeta))
			return;
		ItemStack menu = player.getItemInHand();
		Action action = e.getAction();
		if((action == Action.LEFT_CLICK_AIR)
				|| (action == Action.LEFT_CLICK_BLOCK)
				|| (action == Action.PHYSICAL))
			return;
		if ((menu == null)
				|| (menu.getType() == Material.AIR)
				|| (!menu.getItemMeta().hasDisplayName())
				|| (!menu.getItemMeta().getDisplayName().equals(Splatoon.data.DisplayName))
				|| (!menu.getItemMeta().hasLore()))
			return;
		player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
		this.openGUI(player);
	}

	@EventHandler
	public void onInteract_Leave(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		if(!player.hasMetadata(Splatoon.data.RoomMeta) && !player.hasMetadata(Splatoon.data.SpectateMeta))
			return;
		ItemStack item = player.getItemInHand();
		Action action = e.getAction();
		if((action == Action.LEFT_CLICK_AIR)
				|| (action == Action.LEFT_CLICK_BLOCK)
				|| (action == Action.PHYSICAL))
			return;
		if ((item == null)
				|| (item.getType() == Material.AIR)
				|| (!item.getItemMeta().hasDisplayName())
				|| (!item.getItemMeta().getDisplayName().equals(Splatoon.data.Leave))
				|| (!item.getItemMeta().hasLore()))
			return;
		e.setCancelled(true);
		Splatoon.battle.Leave(player);
		if(player.hasMetadata(Splatoon.data.SpectateMeta))
		{
			String arena = player.getMetadata(Splatoon.data.SpectateMeta).get(0).asString();
			for(Player joinplayers : TurfBattle.JoinPlayersList(arena, true))
				joinplayers.showPlayer(player);
			player.getInventory().remove(Splatoon.data.item[4][1]);
			player.removeMetadata(Splatoon.data.SpectateMeta, Splatoon.instance);
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			player.setAllowFlight(false);
			player.setFlying(false);
			TurfBattle.backLobby(player);
		}
	}

	@EventHandler
	public void onTeamSelect(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		if(!player.hasMetadata(Splatoon.data.RoomMeta))
			return;
		ItemStack selector = player.getItemInHand();
		Action action = e.getAction();
		if((action == Action.LEFT_CLICK_AIR)
				|| (action == Action.LEFT_CLICK_BLOCK)
				|| (action == Action.PHYSICAL))
			return;
		if ((selector == null)
				|| (selector.getType() == Material.AIR)
				|| (!selector.getItemMeta().hasDisplayName())
				|| (!selector.getItemMeta().hasLore()))
			return;
		e.setCancelled(true);
		if(selector.getItemMeta().getDisplayName().equals(Splatoon.data.Team1))
		{
			if(player.hasMetadata(Splatoon.data.Team1Meta))
			{
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "そのチームに既に入っています！");
			}
			/*if(Splatoon.battle.TeamPlayersList(player.getMetadata(Splatoon.data.RoomMeta).get(0).asString(), 1).size()
					>= Splatoon.battle.JoinPlayersList(player.getMetadata(Splatoon.data.RoomMeta).get(0).asString(), false).size() / 2)
			{
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "そのチームは満員です!");
				return;
			}*/
			player.setMetadata(Splatoon.data.Team1Meta, new FixedMetadataValue(Splatoon.instance, player.getMetadata(Splatoon.data.RoomMeta).get(0).asString()));
			player.removeMetadata(Splatoon.data.Team2Meta, Splatoon.instance);
			player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "Team1 を選びました");
		}
		else if (selector.getItemMeta().getDisplayName().equals(Splatoon.data.Team2))
		{
			if(player.hasMetadata(Splatoon.data.Team2Meta))
			{
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "そのチームに既に入っています！");
			}
			/*if(Splatoon.battle.TeamPlayersList(player.getMetadata(Splatoon.data.RoomMeta).get(0).asString(), 2).size()
					>= Splatoon.battle.JoinPlayersList(player.getMetadata(Splatoon.data.RoomMeta).get(0).asString(), false).size() / 2)
			{
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "そのチームは満員です!");
				return;
			}*/
			player.setMetadata(Splatoon.data.Team2Meta, new FixedMetadataValue(Splatoon.instance, player.getMetadata(Splatoon.data.RoomMeta).get(0).asString()));
			player.removeMetadata(Splatoon.data.Team1Meta, Splatoon.instance);
			player.sendMessage(Splatoon.data.Pprefix + ChatColor.GREEN + "Team2 を選びました");
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();
		if(ChatColor.stripColor(e.getInventory().getName())//最初に開かれるGUI
				.equalsIgnoreCase("Splat weapon selector"))
		{

			e.setCancelled(true);

			if(e.getCurrentItem() == null
					|| e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
			{
				return;
			}

			switch (e.getCurrentItem().getType())
			{
			case WOOD_HOE:
				this.ShooterSelect(player);
				break;

			case STICK:
				this.RoolerSelect(player);
				break;

			case BOW:
				this.ChargerSelect(player);
				break;

			default:
				player.closeInventory();
				break;
			}

		}

		else if(ChatColor.stripColor(e.getInventory().getName())
				.equalsIgnoreCase("Shooter select"))//シューターを選んだ場合のGUI
		{
			e.setCancelled(true);

			if(e.getCurrentItem() == null
					|| e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
			{
				return;
			}

			switch (e.getCurrentItem().getType())
			{
			case WOOD_HOE:
				player.setMetadata(weaponmeta, new FixedMetadataValue(Splatoon.instance, Splatoon.data.SHOOTER1));
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.AQUA + "SplatShooter " +ChatColor.GREEN + "を選択しました");
				player.closeInventory();
				break;

			case STONE_HOE:
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "そのブキは使用不可能です！");
				//player.setMetadata("SplatWeapons", new FixedMetadataValue(this.plugin, this.plugin.SHOOTER2));
				player.closeInventory();
				break;

			case IRON_HOE:
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "そのブキは使用不可能です！");
				//player.setMetadata("SplatWeapons", new FixedMetadataValue(this.plugin, this.plugin.SHOOTER3));
				player.closeInventory();
				break;

			default:
				player.closeInventory();
				break;
			}

		}

		else if(ChatColor.stripColor(e.getInventory().getName())//ローラーを選んだ場合のGUI
				.equalsIgnoreCase("Roller select"))
		{
			e.setCancelled(true);

			if(e.getCurrentItem() == null
					|| e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
			{
				return;
			}

			switch (e.getCurrentItem().getType())
			{
			case STICK:
				player.setMetadata(weaponmeta, new FixedMetadataValue(Splatoon.instance, Splatoon.data.ROLLER1));
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.AQUA + "SplatRoller " +ChatColor.GREEN + "を選択しました");
				player.closeInventory();
				break;

			case BLAZE_ROD:
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.RED + "そのブキは使用不可能です！");
				//player.setMetadata("SplatWeapons", new FixedMetadataValue(this.plugin, this.plugin.ROLLER2));
				player.closeInventory();
				break;
			default:
				player.closeInventory();
				break;
			}

		}

		else if(ChatColor.stripColor(e.getInventory().getName())//チャージャーを選んだ場合のGUI
				.equalsIgnoreCase("Charger select"))
		{
			e.setCancelled(true);

			if(e.getCurrentItem() == null
					|| e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
			{
				return;
			}

			switch (e.getCurrentItem().getType())
			{
			case BOW:
				player.setMetadata(weaponmeta, new FixedMetadataValue(Splatoon.instance, Splatoon.data.CHARGER));
				player.sendMessage(Splatoon.data.Pprefix + ChatColor.AQUA + "SplatCharger " +ChatColor.GREEN + "を選択しました");
				player.closeInventory();
				break;

			default:
				player.closeInventory();
				break;
			}

		}
	}

	@EventHandler
	public void onInteract_StatusItem(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		ItemStack statusitem = player.getItemInHand();
		Action action = e.getAction();
		if((action == Action.LEFT_CLICK_AIR)
				|| (action == Action.LEFT_CLICK_BLOCK)
				|| (action == Action.PHYSICAL))
			return;
		if ((statusitem == null)
				|| (statusitem.getType() == Material.AIR)
				|| (!statusitem.getItemMeta().hasDisplayName())
				|| (!statusitem.getItemMeta().getDisplayName().equals(JoinQuitEvents.profileitem))
				|| (!statusitem.getItemMeta().hasLore()))
			return;
		player.openInventory(OpenPlayerInv(player.getName()));
	}

	public static Inventory OpenPlayerInv(String name)
	{
		Inventory inv;

		ItemStack playerskull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) playerskull.getItemMeta();
		meta.setOwner(name);
		meta.setDisplayName(ChatColor.GREEN + "Your Profile");
		playerskull.setItemMeta(meta);

		ItemStack otherskull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemMeta othermeta = otherskull.getItemMeta();
		othermeta.setDisplayName(ChatColor.YELLOW + "Friend Players");
		otherskull.setItemMeta(othermeta);

		inv = Bukkit.createInventory(null, 9, "Select");

		inv.setItem(1, playerskull);
		inv.setItem(7, otherskull);

		return inv;
	}

	/*
	 * 0  1  2  3  4  5  6  7  8
	 * 9  10 11 12 13 14 15 16 17
	 * 18 19 20 21 22 23 24 25 26
	 * 27 28 29 30 31 32 33 34 35
	 * 36 37 38 39 40 41 42 43 44
	 * 45 46 47 48 49 50 51 52 53
	 */

	@EventHandler
	public void onSelectPlayer(InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();
		if(e.getInventory().getName().equalsIgnoreCase("Select"))
		{
			e.setCancelled(true);

			if(e.getCurrentItem() == null
					|| e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
				return;

			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Your Profile"))
			{
				sendStatus(player, player.getName());
				player.closeInventory();
			}
			else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Friend Players"))
				player.openInventory(OtherProfile(player.getName()));
		}
		else if(e.getInventory().getName().equalsIgnoreCase("Friend Players"))
		{
			e.setCancelled(true);

			if(e.getCurrentItem() == null
					|| e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
				return;
			ItemStack item = e.getCurrentItem();
			String name = s(player.getName(), item.getItemMeta().getDisplayName());
			sendStatus(player, name);
			player.closeInventory();
		}
	}

	public static Inventory PlayerProfile(String name, int slot)
	{
		Inventory inv;

		inv = Bukkit.createInventory(null, 9*slot, name + "'s Profile");
		for(int i = 0; i <= 9*slot-1 ; i++)
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1 , (short) 15));
		return inv;
	}

	private static Inventory OtherProfile(String name)
	{
		Inventory inv;

		inv = Bukkit.createInventory(null, 9*6, "Friend Players");
		SplatPlayerStatus status = new SplatPlayerStatus(name);

		int i = 0;
		for(String player : status.getFriendList())
		{
			ItemStack playerskull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta meta = (SkullMeta) playerskull.getItemMeta();
			meta.setOwner(player);
			meta.setDisplayName(ChatColor.GREEN + player);
			playerskull.setItemMeta(meta);
			inv.setItem(i, playerskull);
			i++;
		}
		return inv;
	}

	private static void sendStatus(Player player, String target)
	{
		SplatPlayerStatus status = new SplatPlayerStatus(target);
		player.sendMessage(Splatoon.data.Pprefix);
		player.sendMessage(ChatColor.GREEN + "<>" + ChatColor.STRIKETHROUGH + "--------------------" + ChatColor.RESET + ChatColor.GREEN + "<>");
		player.sendMessage(ChatColor.AQUA + "名前 : " + ChatColor.YELLOW + target);
		player.sendMessage(ChatColor.AQUA + "ランク : " + ChatColor.YELLOW + status.getRank());
		player.sendMessage(ChatColor.AQUA + "勝数 : " + ChatColor.YELLOW + status.getWinCount());
		player.sendMessage(ChatColor.AQUA + "総試合 : " + ChatColor.YELLOW + (status.getWinCount() + status.getLoseCount()));
		player.sendMessage(ChatColor.AQUA + "勝率 : " + ChatColor.YELLOW + status.Winning_percentage());
		player.sendMessage(ChatColor.AQUA + "総塗り面積 : " + ChatColor.YELLOW + status.getTotal());
		player.sendMessage(ChatColor.GREEN + "<>" + ChatColor.STRIKETHROUGH + "--------------------" + ChatColor.RESET + ChatColor.GREEN + "<>");
	}

	private String s(String name, String target)
	{
		SplatPlayerStatus status = new SplatPlayerStatus(name);
		for(String player : status.getFriendList())
		{
			if(target.equalsIgnoreCase(ChatColor.GREEN + player))
				return player;
		}
		return null;
	}
}
