/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SplatTitle
{
	/**
	 * タイトルの表示
	 *
	 * @param player 対象プレイヤー
	 * @param fadein フェードイン
	 * @param stay 表示時間
	 * @param fadeout フェードアウト
	 * @param MainTitle メインタイトル
	 * @param Subtitle サブタイトル
	 *
	 */
	public static void sendFullTitle(Player player, int fadein, int stay, int fadeout, String MainTitle, String SubTitle)
	{
		sendTitle(player, fadein, stay, fadeout, MainTitle, SubTitle);
	}

	/**
	 * タイトルの表示
	 *
	 * @param player 対象プレイヤー
	 * @param stay 表示時間
	 * @param MainTitle メインタイトル
	 * @param Subtitle サブタイトル
	 *
	 */
	public static void sendFullTitle(Player player, int stay, String MainTitle, String SubTitle)
	{
		sendTitle(player, 0, stay, 0, MainTitle, SubTitle);
	}

	/**
	 * タイトルの表示
	 *
	 * @param player 対象プレイヤー
	 * @param fadein フェードイン
	 * @param stay 表示時間
	 * @param fadeout フェードアウト
	 * @param MainTitle メインタイトル
	 * @param Subtitle サブタイトル
	 *
	 */
	private static void sendTitle(Player player, int fadein, int stay, int fadeout, String MainTitle, String SubTitle)
	{
		if(!Metrics.CheckNMS())
		{
			sendTitle_Command(player, fadein, stay, fadeout, MainTitle, SubTitle);
			return;
		}
		net.minecraft.server.v1_8_R3.PacketPlayOutTitle titletime = new net.minecraft.server.v1_8_R3.PacketPlayOutTitle(
				net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadein, stay, fadeout);
		sendPlayer(player, titletime);

		if(SubTitle != null)
		{
			SubTitle = ChatColor.translateAlternateColorCodes('&', SubTitle);
			net.minecraft.server.v1_8_R3.IChatBaseComponent subtitle = net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + SubTitle + "\"}");
			net.minecraft.server.v1_8_R3.PacketPlayOutTitle sendsubtitle = new net.minecraft.server.v1_8_R3.PacketPlayOutTitle(
					net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitle);
			sendPlayer(player, sendsubtitle);
		}

		if(MainTitle != null)
		{
			MainTitle = ChatColor.translateAlternateColorCodes('&', MainTitle);
			net.minecraft.server.v1_8_R3.IChatBaseComponent maintitle = net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + MainTitle + "\"}");
			net.minecraft.server.v1_8_R3.PacketPlayOutTitle sendmaintitle = new net.minecraft.server.v1_8_R3.PacketPlayOutTitle(
					net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction.TITLE, maintitle);
			sendPlayer(player, sendmaintitle);
		}
	}


	/**
	 * パケットを送信
	 *
	 * @param player 対象
	 * @param packet パケット
	 */
	@SuppressWarnings("rawtypes")
	public static void sendPlayer(Player player, net.minecraft.server.v1_8_R3.Packet packet)
	{
		((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}



	/**
	 * パーティクルのパケットを送信(対象ワールドのプレイヤーだけに送信)
	 * (v_1_8_R3)
	 *
	 * @param world 対象ワールド
	 * @param packet パーティクル
	 */
	@SuppressWarnings("rawtypes")
	public static void sendPlayer(String world, net.minecraft.server.v1_8_R3.Packet packet)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		if(player.getWorld().getName() == world)
			((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}


	public static void titleTest()
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			sendFullTitle(player, 20, 100, 0, "／人"+ChatColor.LIGHT_PURPLE+"◕"+ChatColor.WHITE+" ‿‿ "+ChatColor.LIGHT_PURPLE+"◕"+ChatColor.WHITE+"人＼", "僕と契約して魔法少女になってよ");
		}
	}

	public static void titleTest_i()
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			sendTitle_Command(player, 20, 100, 0, "／人"+ChatColor.LIGHT_PURPLE+"◕"+ChatColor.WHITE+" ‿‿ "+ChatColor.LIGHT_PURPLE+"◕"+ChatColor.WHITE+"人＼", "僕と契約して魔法少女になってよ！");
		}
	}

	public static void titleTest_ii()
	{
		String BR = "/n";
		for(Player player : Bukkit.getOnlinePlayers())
		{
			sendFullTitle(player, 20, 100, 0, "／人"+ChatColor.LIGHT_PURPLE+"◕"+ChatColor.WHITE+" ‿‿ "+ChatColor.LIGHT_PURPLE+"◕"+ChatColor.WHITE+"人＼", "僕と契約して魔法少女になってよ" + BR + "まどか！");
		}
	}

	/**
	 * もしNMSのバージョンがv1_8_R3じゃなかった場合にコマンドでタイトルの送信
	 *
	 * @param player 対象プレイヤー
	 * @param fadein フェードイン
	 * @param stay 表示時間
	 * @param fadeout フェードアウト
	 * @param MainTitle メインタイトル
	 * @param SubTitle サブタイトル
	 */
	private static void sendTitle_Command(Player player, int fadein, int stay, int fadeout, String MainTitle, String SubTitle)
	{
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "title "+ player.getName() +" times "+ fadein +" "+ stay +" "+ fadeout);
		if(SubTitle != null)
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "title "+ player.getName() +" subtitle "+ "{\"text\":\"" + SubTitle + "\"}");

		if(MainTitle != null)
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "title "+ player.getName() +" title "+ "{\"text\":\"" + MainTitle + "\"}");
	}
}
