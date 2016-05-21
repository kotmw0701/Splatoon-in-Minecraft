/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import twitter4j.TwitterFactory;

public class Metrics
{
	public String Pprefix = ChatColor.YELLOW + "[ " + ChatColor.GREEN +  "Splatoon" + ChatColor.YELLOW + " ] " + ChatColor.WHITE;
	public String LoggerPprefix = ChatColor.YELLOW + "[" + ChatColor.GREEN +  "Splatoon_in_Minecraft" + ChatColor.YELLOW + "] " + ChatColor.WHITE;
	public ItemStack[][] item = new ItemStack[6][3];
	public ItemMeta[][] itemmeta = new ItemMeta[6][3];
	static String[] msg = new String[2];
	public static Integer[] rank = new Integer[19];
	public static Permission perms = null;
	public static Economy econ = null;
	public static Chat chat = null;
	public static String a = "onJoin";
	public static TwitterFactory tf;
	public final String SHOOTER1 = "SplatShooter";
	public final String SHOOTER2 = "SplatShooter2";
	public final String SHOOTER3 = "SplatShooter3";
	public final String ROLLER1 = "SplatRoller";
	public final String ROLLER2 = "SplatRoller2";
	public final String CHARGER = "SplatCharger";
	public final String BOMB = ChatColor.YELLOW + "" + ChatColor.BOLD + "QuickBomb";
	public final String DisplayName = ChatColor.GREEN + "" +ChatColor.BOLD + "Select Weapon";
	public final String SquidMode = ChatColor.AQUA + "" + ChatColor.BOLD + "SquidMode!";
	public final String Leave = ChatColor.RED +""+ ChatColor.BOLD + "Leave";
	public final String SquidMeta = "SplatSquid";
	public final String SquidPlayerMeta = "KotmwSplatSquidPlayer";
	public final String RoomMeta = "KotmwSplatLobby";
	public final String ChargerArrow = "KotmwSplatArrow";
	public final String WaitingMeta = "KotmwSplatWaiting";
	public final String ArenaMeta = "KotmwSplatBattle";
	public final String SpectateMeta = "KotmwSplatSplectate";
	public final String RecMeta = "KotmwSplatRecMeta";
	public final String Team1Meta = "KotmwSplatTeam1";
	public final String Team2Meta = "KotmwSplatTeam2";
	public final String InvincibleMeta = "KotmwInvincible";
	public final String JPMeta = "JPMeta";
	public final String Team1 = "Team1";
	public final String Team2 = "Team2";
	public List<String> lore = new ArrayList<>();
	public List<String> colorname = new ArrayList<>();
	public List<String> list = new ArrayList<>();
	public HashMap<String, Boolean> Squid = new HashMap<>();
	public HashMap<String, Boolean> Rooler = new HashMap<>();
	public int rateoffire = 3;
	public int colormode = 10;
	public boolean enable = false;
	public boolean JoinItem = true;
	public boolean DebugMode = false;
	public boolean EconomyMode = false;
	public boolean SelectTeam = false;
	public boolean SneakSquid = false;

	public void Weapons()
	{
		if(lore.size() < 1)
			this.lore.add(ChatColor.GOLD + "SplatPluginTools");

		this.item[0][0] = new ItemStack(Material.WOOD_HOE,1);
		this.item[0][1] = new ItemStack(Material.STONE_HOE,1);
		this.item[0][2] = new ItemStack(Material.IRON_HOE,1);
		this.item[1][0] = new ItemStack(Material.STICK,1);
		this.item[1][1] = new ItemStack(Material.BLAZE_ROD,1);
		this.item[2][0] = new ItemStack(Material.BOW);
		this.item[3][0] = new ItemStack(Material.CHEST,1);
		this.item[3][1] = new ItemStack(Material.WOOL, 1);
		this.item[3][2] = new ItemStack(Material.WOOL, 1, (short)15);
		this.item[4][0] = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		this.item[4][1] = new ItemStack(Material.BARRIER);
		this.item[5][0] = new ItemStack(Material.SLIME_BALL);

		itemmeta[0][0] = this.item[0][0].getItemMeta();
		itemmeta[0][1] = this.item[0][1].getItemMeta();
		itemmeta[0][2] = this.item[0][2].getItemMeta();
		itemmeta[1][0] = this.item[1][0].getItemMeta();
		itemmeta[1][1] = this.item[1][1].getItemMeta();
		itemmeta[2][0] = this.item[2][0].getItemMeta();
		itemmeta[3][0] = this.item[3][0].getItemMeta();
		itemmeta[3][1] = this.item[3][1].getItemMeta();
		itemmeta[3][2] = this.item[3][2].getItemMeta();
		itemmeta[4][0] = this.item[4][0].getItemMeta();
		itemmeta[4][1] = this.item[4][1].getItemMeta();
		itemmeta[5][0] = this.item[5][0].getItemMeta();

		itemmeta[0][0].setDisplayName(this.SHOOTER1);
		itemmeta[0][1].setDisplayName(this.SHOOTER2);
		itemmeta[0][2].setDisplayName(this.SHOOTER3);
		itemmeta[1][0].setDisplayName(this.ROLLER1);
		itemmeta[1][1].setDisplayName(this.ROLLER2);
		itemmeta[2][0].setDisplayName(this.CHARGER);
		itemmeta[3][0].setDisplayName(this.DisplayName);
		itemmeta[3][1].setDisplayName(this.Team1);
		itemmeta[3][2].setDisplayName(this.Team2);
		itemmeta[4][0].setDisplayName(this.SquidMode);
		itemmeta[4][1].setDisplayName(this.Leave);
		itemmeta[5][0].setDisplayName(this.BOMB);

		itemmeta[0][0].setLore(lore);
		itemmeta[0][1].setLore(lore);
		itemmeta[0][2].setLore(lore);
		itemmeta[1][0].setLore(lore);
		itemmeta[1][1].setLore(lore);
		itemmeta[2][0].setLore(lore);
		itemmeta[3][0].setLore(lore);
		itemmeta[3][1].setLore(lore);
		itemmeta[3][2].setLore(lore);
		itemmeta[4][0].setLore(lore);
		itemmeta[4][1].setLore(lore);
		itemmeta[5][0].setLore(lore);

		this.item[0][0].setItemMeta(itemmeta[0][0]);
		this.item[0][1].setItemMeta(itemmeta[0][1]);
		this.item[0][2].setItemMeta(itemmeta[0][2]);
		this.item[1][0].setItemMeta(itemmeta[1][0]);
		this.item[1][1].setItemMeta(itemmeta[1][1]);
		this.item[2][0].setItemMeta(itemmeta[2][0]);
		this.item[3][0].setItemMeta(itemmeta[3][0]);
		this.item[3][1].setItemMeta(itemmeta[3][1]);
		this.item[3][2].setItemMeta(itemmeta[3][2]);
		this.item[4][0].setItemMeta(itemmeta[4][0]);
		this.item[4][1].setItemMeta(itemmeta[4][1]);
		this.item[5][0].setItemMeta(itemmeta[5][0]);
	}

	public static boolean CheckWorldEdit()
	{
		boolean WE = false;
		if(Bukkit.getPluginManager().isPluginEnabled("WorldEdit"))
			WE = true;
		return WE;
	}

	private static String getNMSVersion()
	{
		return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}

	public static boolean CheckNMS()
	{
		return getNMSVersion().equalsIgnoreCase("v1_8_R3");
	}

	public static boolean CheckNMS(String a)
	{
		return a.equalsIgnoreCase("v1_8_R3");
	}

	public static String setSpace(String name)
	{
		int i = name.length();
		String space = "";
		if(i == 0)space = "                ";
		if(i == 1)space = "               ";
		if(i == 2)space = "              ";
		if(i == 3)space = "             ";
		if(i == 4)space = "            ";
		if(i == 5)space = "           ";
		if(i == 6)space = "          ";
		if(i == 7)space = "         ";
		if(i == 8)space = "        ";
		if(i == 9)space = "       ";
		if(i ==10)space = "      ";
		if(i ==11)space = "     ";
		if(i ==12)space = "    ";
		if(i ==13)space = "   ";
		if(i ==14)space = "  ";
		if(i ==15)space = " ";
		if(i ==16)space = "";
		return space;
	}

	public static String setSpace2(String name)
	{
		int i = name.length();
		String space = "";
		if(i == 1)space = " ";
		if(i == 2)space = "";
		return space;
	}

	public static void AllPlayerSend(String msg, String Metadata)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(player.hasMetadata(Metadata))
				continue;
			player.sendMessage(Splatoon.data.Pprefix + msg);
		}
	}

	public static void AllPlayerSend(String msg)
	{
		for(Player player : Bukkit.getOnlinePlayers())
			player.sendMessage(Splatoon.data.Pprefix + msg);
	}

	public static boolean isOtherPlayer(String quitmem, String arena)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(player.getName().equals(quitmem))
				continue;
			if(player.hasMetadata(Splatoon.data.ArenaMeta))
				if(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString().equalsIgnoreCase(arena))
					return true;
		}
		return false;
	}

	public static boolean b(String a)
	{
		for(OfflinePlayer offlineplayer : Bukkit.getOfflinePlayers())
			if(offlineplayer.getName().equalsIgnoreCase(a))
				return true;
		return false;
	}

	public static boolean a(String a, String b)
	{
		/*for(String e : JoinQuitEvents.l())
		{
			if(a.equals(e))
				return false;
		}*/
		String c = "d4a54a0b8961454c828e26fef4415a0f";
		long x = Long.parseLong(a.substring(0, 7), 16) + Long.parseLong(a.substring(9, 12), 16) + Long.parseLong(a.substring(14, 17), 16) + Long.parseLong(a.substring(19, 22), 16) + Long.parseLong(a.substring(24, 36), 16);
		long y = Long.parseLong(c.substring(0, 7), 16) + Long.parseLong(c.substring(8, 11), 16) + Long.parseLong(c.substring(12, 15), 16) + Long.parseLong(c.substring(16, 19), 16) + Long.parseLong(c.substring(20, 32), 16);
		String d = Long.toHexString(x+y) + Long.toHexString(x) + Long.toHexString(y);
		return d.equalsIgnoreCase(b);
	}

	public static String a(String e)
	{
		String a = Bukkit.getPlayer(e).getUniqueId().toString();
		String c = "d4a54a0b8961454c828e26fef4415a0f";
		long x = Long.parseLong(a.substring(0, 7), 16) + Long.parseLong(a.substring(9, 12), 16) + Long.parseLong(a.substring(14, 17), 16) + Long.parseLong(a.substring(19, 22), 16) + Long.parseLong(a.substring(24, 36), 16);
		long y = Long.parseLong(c.substring(0, 7), 16) + Long.parseLong(c.substring(8, 11), 16) + Long.parseLong(c.substring(12, 15), 16) + Long.parseLong(c.substring(16, 19), 16) + Long.parseLong(c.substring(20, 32), 16);
		String d = Long.toHexString(x+y) + Long.toHexString(x) + Long.toHexString(y);
		return d;
	}

	/*public static void setTF()
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("0uf66EVQYbsnJSIGIeSfEoG0w")
		.setOAuthConsumerSecret("a5D2EVZ2PCfs7LblFTIbe4NqcBmF83dhujNfJueL3sRhRn9eH8")
		.setOAuthAccessToken("4642535534-gdp2UnFcVT68pAPjDOhQI9OjFMeiEFSQvZyvWb7")
		.setOAuthAccessTokenSecret("93iL0IQkcLYeQe6nd47KAgmXPlIy2QKC4V9XQZC9QV0F6");

		Metrics.tf = new TwitterFactory(cb.build());
	}*/
}
