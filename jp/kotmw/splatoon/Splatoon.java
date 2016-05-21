/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import jp.kotmw.splatoon.arena.ArenaSettings;
import jp.kotmw.splatoon.arena.ArenaStatus;
import jp.kotmw.splatoon.battle.BattleEventListeners;
import jp.kotmw.splatoon.battle.Rollback;
import jp.kotmw.splatoon.battle.SignEntry;
import jp.kotmw.splatoon.battle.SplatEventListeners;
import jp.kotmw.splatoon.battle.SplatTeam;
import jp.kotmw.splatoon.battle.TurfBattle;
import jp.kotmw.splatoon.commands.SystemCommands;
import jp.kotmw.splatoon.weapons.Charger;
import jp.kotmw.splatoon.weapons.Roller;
import jp.kotmw.splatoon.weapons.Shooter;
import jp.kotmw.splatoon.weapons.other.Bomb;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Splatoon extends JavaPlugin
{
	public File datafolder = getDataFolder();
	public String separator = File.separator;
	public String filepath = getDataFolder() + separator;
	public String Pluginname = "[" + getDescription().getName() + "]";
	public static Splatoon instance;
	public static Random random = new Random();
	public static ArenaSettings settings;
	public static TurfBattle battle;
	public static MessageButton msgbutton;
	public static ArenaStatus arenastatus;
	public static SplatTeam teaming;
	public static SplatoonFiles files;
	public static Metrics data;
	public static Rollback rollback;

	public int StartCountDown;
	public int time;
	public int gachi_area_time;
	public float charge;

	private double configver;

	public void ConfigReload()//コンフィグの指定範囲のみの再読み込み
	{
		this.reloadConfig();
		configver = this.getConfig().getDouble("ConfigVersion");
		StartCountDown = this.getConfig().getInt("Splat.StartCount");
		time = this.getConfig().getInt("Splat.time");
		gachi_area_time = this.getConfig().getInt("Splat.GachiTime");
		charge = (float) this.getConfig().getDouble("Splat.Charge");
		data.SelectTeam = this.getConfig().getBoolean("Splat.SelectTeam");
		data.JoinItem = this.getConfig().getBoolean("Splat.JoinItem");
		Metrics.rank[0] = this.getConfig().getInt("Splat.Rank.Rank2");
		Metrics.rank[1] = this.getConfig().getInt("Splat.Rank.Rank3");
		Metrics.rank[2] = this.getConfig().getInt("Splat.Rank.Rank4");
		Metrics.rank[3] = this.getConfig().getInt("Splat.Rank.Rank5");
		Metrics.rank[4] = this.getConfig().getInt("Splat.Rank.Rank6");
		Metrics.rank[5] = this.getConfig().getInt("Splat.Rank.Rank7");
		Metrics.rank[6] = this.getConfig().getInt("Splat.Rank.Rank8");
		Metrics.rank[7] = this.getConfig().getInt("Splat.Rank.Rank9");
		Metrics.rank[8] = this.getConfig().getInt("Splat.Rank.Rank10");
		Metrics.rank[9] = this.getConfig().getInt("Splat.Rank.Rank11");
		Metrics.rank[10] = this.getConfig().getInt("Splat.Rank.Rank12");
		Metrics.rank[11] = this.getConfig().getInt("Splat.Rank.Rank13");
		Metrics.rank[12] = this.getConfig().getInt("Splat.Rank.Rank14");
		Metrics.rank[13] = this.getConfig().getInt("Splat.Rank.Rank15");
		Metrics.rank[14] = this.getConfig().getInt("Splat.Rank.Rank16");
		Metrics.rank[15] = this.getConfig().getInt("Splat.Rank.Rank17");
		Metrics.rank[16] = this.getConfig().getInt("Splat.Rank.Rank18");
		Metrics.rank[17] = this.getConfig().getInt("Splat.Rank.Rank19");
		Metrics.rank[18] = this.getConfig().getInt("Splat.Rank.Rank20");
		data.SneakSquid = this.getConfig().getBoolean("Settings.SneakSquid");
		data.EconomyMode = this.getConfig().getBoolean("Settings.Economy");
		data.colorname = this.getConfig().getStringList("CanPaintWoolColors");
	}

	public void ConfigSet()//コンフィグを設定する
	{
		this.getConfig().addDefault("ConfigVersion", 2.4);
		this.getConfig().addDefault("Splat.StartCount", 3);
		this.getConfig().addDefault("Splat.time", 180);
		this.getConfig().addDefault("Splat.GachiTime", 300);
		this.getConfig().addDefault("Splat.Charge", 0.02f);
		this.getConfig().addDefault("Splat.SelectTeam", false);
		this.getConfig().addDefault("Splat.JoinItem", true);
		this.getConfig().addDefault("Splat.Rank.Rank2", 700);
		this.getConfig().addDefault("Splat.Rank.Rank3", 1600);
		this.getConfig().addDefault("Splat.Rank.Rank4", 2600);
		this.getConfig().addDefault("Splat.Rank.Rank5", 3700);
		this.getConfig().addDefault("Splat.Rank.Rank6", 4800);
		this.getConfig().addDefault("Splat.Rank.Rank7", 6000);
		this.getConfig().addDefault("Splat.Rank.Rank8", 7200);
		this.getConfig().addDefault("Splat.Rank.Rank9", 8600);
		this.getConfig().addDefault("Splat.Rank.Rank10", 10000);
		this.getConfig().addDefault("Splat.Rank.Rank11", 11500);
		this.getConfig().addDefault("Splat.Rank.Rank12", 13100);
		this.getConfig().addDefault("Splat.Rank.Rank13", 14800);
		this.getConfig().addDefault("Splat.Rank.Rank14", 16600);
		this.getConfig().addDefault("Splat.Rank.Rank15", 18600);
		this.getConfig().addDefault("Splat.Rank.Rank16", 20600);
		this.getConfig().addDefault("Splat.Rank.Rank17", 22700);
		this.getConfig().addDefault("Splat.Rank.Rank18", 25000);
		this.getConfig().addDefault("Splat.Rank.Rank19", 27400);
		this.getConfig().addDefault("Splat.Rank.Rank20", 30000);
		this.getConfig().addDefault("Settings.Economy", false);
		this.getConfig().addDefault("Settings.SneakSquid", false);
		this.getConfig().addDefault("CanPaintWoolColors", ColorSelect.AllColors());
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		this.reloadConfig();
		return;
	}

	public void ConfigUpdater()
	{
		this.getConfig().set("ConfigVersion", 2.4);
		this.getConfig().addDefault("Settings.SneakSquid", false);
		if(!this.getConfig().contains("Splat.GachiTime"))
			this.getConfig().addDefault("Splat.GachiTime", 300);
		if(!this.getConfig().contains("Splat.Rank.Rank2"))
		{
			this.getConfig().addDefault("Splat.Rank.Rank2", 700);
			this.getConfig().addDefault("Splat.Rank.Rank3", 1600);
			this.getConfig().addDefault("Splat.Rank.Rank4", 2600);
			this.getConfig().addDefault("Splat.Rank.Rank5", 3700);
			this.getConfig().addDefault("Splat.Rank.Rank6", 4800);
			this.getConfig().addDefault("Splat.Rank.Rank7", 6000);
			this.getConfig().addDefault("Splat.Rank.Rank8", 7200);
			this.getConfig().addDefault("Splat.Rank.Rank9", 8600);
			this.getConfig().addDefault("Splat.Rank.Rank10", 10000);
			this.getConfig().addDefault("Splat.Rank.Rank11", 11500);
			this.getConfig().addDefault("Splat.Rank.Rank12", 13100);
			this.getConfig().addDefault("Splat.Rank.Rank13", 14800);
			this.getConfig().addDefault("Splat.Rank.Rank14", 16600);
			this.getConfig().addDefault("Splat.Rank.Rank15", 18600);
			this.getConfig().addDefault("Splat.Rank.Rank16", 20600);
			this.getConfig().addDefault("Splat.Rank.Rank17", 22700);
			this.getConfig().addDefault("Splat.Rank.Rank18", 25000);
			this.getConfig().addDefault("Splat.Rank.Rank19", 27400);
			this.getConfig().addDefault("Splat.Rank.Rank20", 30000);
		}
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		this.reloadConfig();
		return;
	}

	/*private boolean setupEconomy()
	{
		if (getServer().getPluginManager().getPlugin("Vault") == null)
			return false;
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null)
			return false;
		Metrics.econ = rsp.getProvider();
		return Metrics.econ != null;
	}

	private boolean setupChat()
	{
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		Metrics.chat = rsp.getProvider();
		return Metrics.chat != null;
	}

	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		Metrics.perms = rsp.getProvider();
		return Metrics.perms != null;
	}*/

	public void onEnable()//起動時処理
	{
		instance = this;
		settings = new ArenaSettings();
		battle = new TurfBattle();
		msgbutton = new MessageButton();
		arenastatus = new ArenaStatus();
		teaming = new SplatTeam();
		files = new SplatoonFiles();
		data = new Metrics();
		rollback = new Rollback();
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new Shooter(), this);
		pm.registerEvents(new Roller(), this);
		pm.registerEvents(new Charger(), this);
		pm.registerEvents(new Squid(), this);
		pm.registerEvents(new SelectInv(), this);
		pm.registerEvents(new EasySetup(), this);
		pm.registerEvents(new JoinQuitEvents(), this);
		pm.registerEvents(new SignEntry(), this);
		pm.registerEvents(new SplatEventListeners(), this);
		pm.registerEvents(new BattleEventListeners(), this);
		pm.registerEvents(new Bomb(), this);
		this.loadYAMLFiles();
		data.Weapons();
		//if(!Arrays.asList(JoinQuitEvents.class.getMethods()).contains(Metrics.a))
		//	Bukkit.getServer().getPluginManager().disablePlugin(Splatoon.instance);
		if(!SplatoonFiles.config.exists())
		{
			Bukkit.getConsoleSender().sendMessage(data.LoggerPprefix + ChatColor.YELLOW + "初回起動なのでConfig.ymlを作成します");
			ConfigSet();
			ConfigReload();
		}
		ConfigReload();
		if(configver != 2.4)
		{
			Bukkit.getConsoleSender().sendMessage(data.LoggerPprefix + ChatColor.YELLOW + "旧バージョンのコンフィグなので更新します");
			ConfigUpdater();
			ConfigReload();
		}
		/*if(data.EconomyMode)
		{
			if(!setupEconomy())//setupEconomy()のメソッドが無かった際(=Vaultが入ってない)時にプラグインが無効化される
			{
				Bukkit.getConsoleSender().sendMessage(data.LoggerPprefix + ChatColor.RED + "経済プラグインが入っていません！");
				Bukkit.getConsoleSender().sendMessage(data.LoggerPprefix + ChatColor.RED + "経済仕様を無効化したい場合はConfig.ymlのEconomyをfalseにしてください！");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
			setupPermissions();
			setupChat();
		}*/
		this.SettingFiles(files.lobbyconfig, SplatoonFiles.lobbyfile, false);
		this.CreateDirctory(SplatoonFiles.weapondir);
		this.CreateDirctory(SplatoonFiles.subdir);
		this.CreateDirctory(SplatoonFiles.specialdir);
		this.CreateDirctory(SplatoonFiles.arenadir);
		this.CreateDirctory(SplatoonFiles.signsdir);
		this.CreateDirctory(SplatoonFiles.roomdir);
		this.CreateDirctory(SplatoonFiles.battlelogs);
		FileShiftChange.CheckPlayersDIR();
		FileShiftChange.CheckRoomsDIR();
		for(String arena : SplatoonFiles.getArenaList())
			ColorSelect.select(arena);
		//Splatoon.data.list = JoinQuitEvents.l();
		Splatoon.arenastatus.SetStatus();
		ArenaSettings.ReloadAllArena();
		getCommand("splatoon").setExecutor(new SystemCommands());
		Splatoon.arenastatus.OutputArenaLists();
		SplatTeam.createScoreBoard();
		//SplatRunnable.AnimationScoreboard();
		SignEntry.UPdateAllSigns();
		//Metrics.setTF();
	}

	public void onDisable()
	{

	}

	public static Splatoon getInstance()
	{
		return instance;
	}

	public ItemStack shooter(int i)
	{
		return data.item[0][i].clone();
	}

	public ItemStack roller(int i)
	{
		return data.item[1][i].clone();
	}

	public ItemStack charger()
	{
		return data.item[2][0].clone();
	}

	/**
	 * ブキを与える
	 *
	 * @param player 与えるプレイヤー名
	 * @param weapon 与えるブキ名
	 */
	public void giveWeapon(Player player, String weapon)
	{

		if(weapon.equalsIgnoreCase(data.SHOOTER1))
		{
			ItemStack Shooter = this.shooter(0);
			player.getInventory().addItem(Shooter);
		}
		else if(weapon.equalsIgnoreCase(data.SHOOTER2))
		{
			ItemStack Shooter = this.shooter(1);
			player.getInventory().addItem(Shooter);
		}
		else if(weapon.equalsIgnoreCase(data.SHOOTER3))
		{
			ItemStack Shooter = this.shooter(2);
			player.getInventory().addItem(Shooter);
		}
		else if(weapon.equalsIgnoreCase(data.ROLLER1))
		{
			ItemStack Roller = this.roller(0);
			player.getInventory().addItem(Roller);
		}
		else if(weapon.equalsIgnoreCase(data.ROLLER2))
		{
			ItemStack Roller = this.roller(1);
			player.getInventory().addItem(Roller);
		}
		else if(weapon.equalsIgnoreCase(data.CHARGER))
		{
			ItemStack Charger = this.charger();
			Charger.addEnchantment(Enchantment.ARROW_INFINITE, 1);
			Charger.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
			Charger.addEnchantment(Enchantment.DURABILITY, 3);
			player.getInventory().setItem(9, new ItemStack(Material.ARROW));
			player.getInventory().addItem(Charger);
		}
	}

	/**
	 * ファイルの保存
	 *
	 * @param fileconfiguration ファイルコンフィグを指定
	 * @param file ファイル指定
	 * @param save 上書きをするかリセットするか
	 */
	public void SettingFiles(FileConfiguration fileconfiguration, File file, boolean save)
	{
		if(!file.exists() || save)
		{
			try {
				fileconfiguration.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * フォルダの作成
	 *
	 * @param file フォルダのパス
	 */
	public void CreateDirctory(File file)
	{
		if(!file.exists())
		{
			file.mkdir();
		}
	}

	/**
	 * ロビーファイルの読み込み、何で作ったか覚えてない(おい
	 *
	 */
	public void loadYAMLFiles()
	{
		files.lobbyconfig = YamlConfiguration.loadConfiguration(SplatoonFiles.lobbyfile);
	}
}
