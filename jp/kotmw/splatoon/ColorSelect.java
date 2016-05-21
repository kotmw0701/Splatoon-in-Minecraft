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
import java.util.Map;

import jp.kotmw.splatoon.battle.SplatoonScores;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;

public class ColorSelect
{
	private static Map<String, DyeColor> team1color = new HashMap<>();
	private static Map<String, DyeColor> team2color = new HashMap<>();
	private static Map<String, ChatColor> team1chat = new HashMap<>();
	private static Map<String, ChatColor> team2chat = new HashMap<>();
	public static DyeColor[] battle = new DyeColor[8];
	public static ChatColor[] teamprefix = new ChatColor[8];
	//static DyeColor team1;
	//static DyeColor team2;
	//static ChatColor team1_prefix, team2_prefix;

	/**
	 * 指定したステージの色の再取得
	 *
	 * @param arena ステージ名
	 */
	public static void select(String arena)
	{
		DyeColor team1color = select_team1(arena), team2color = select_team2(arena);
		while(team1color == team2color)
			team2color = select_team2(arena);
		/*if(this.Splatoon.instance.DebugMode[0])
		{
			String team1 = "" + this.select_team1();
			String team2 = "" + this.select_team2();
			player.sendMessage(team1);
			player.sendMessage(team2);
		}*/
		ColorSelect.team1color.put(arena, team1color);
		ColorSelect.team2color.put(arena, team2color);
		ColorSelect.team1chat.put(arena, ChangeChatColor(team1color));
		ColorSelect.team2chat.put(arena, ChangeChatColor(team2color));
	}

	public static List<String> AllColors()
	{
		List<String> allcolors = new ArrayList<>();
		allcolors.add(DyeColor.BLACK.toString());
		allcolors.add(DyeColor.BLUE.toString());
		allcolors.add(DyeColor.BROWN.toString());
		allcolors.add(DyeColor.CYAN.toString());
		allcolors.add(DyeColor.GRAY.toString());
		allcolors.add(DyeColor.GREEN.toString());
		allcolors.add(DyeColor.LIGHT_BLUE.toString());
		allcolors.add(DyeColor.LIME.toString());
		allcolors.add(DyeColor.MAGENTA.toString());
		allcolors.add(DyeColor.ORANGE.toString());
		allcolors.add(DyeColor.PINK.toString());
		allcolors.add(DyeColor.PURPLE.toString());
		allcolors.add(DyeColor.RED.toString());
		allcolors.add(DyeColor.SILVER.toString());
		allcolors.add(DyeColor.WHITE.toString());
		allcolors.add(DyeColor.YELLOW.toString());
		return allcolors;
	}

	public static DyeColor color_team1(String arena)
	{
		return ColorSelect.team1color.get(arena);
	}

	public static DyeColor color_team2(String arena)
	{
		return ColorSelect.team2color.get(arena);
	}

	public static ChatColor color_team1_prefix(String arena)
	{

		return ColorSelect.team1chat.get(arena);
	}

	public static ChatColor color_team2_prefix(String arena)
	{
		return ColorSelect.team2chat.get(arena);
	}

	public static String colorname_team1(String arena)
	{
		return color_team1(arena).toString();
	}

	public static String colorname_team2(String arena)
	{
		return color_team2(arena).toString();
	}

	/**
	 * 色の塗り替え
	 *
	 * @param block 塗る対象のブロック
	 * @param player 塗ったプレイヤー
	 */
	@SuppressWarnings("deprecation")
	public static void ColorChange(Block block, Player player)
	{
		if(!CheckPaint(block, player))
			return;
		if(isAboveBlock(block))
		{
			if(CheckOpponentColor(block, player))
				SplatoonScores.setScore(player, true);
			else
				SplatoonScores.setScore(player, false);
		}
		Splatoon.rollback.setPaintedBlock(block, player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString());
		if(block.getType() == Material.WOOL)
		{
			if(player.hasMetadata(Splatoon.data.Team1Meta))
			{
				BlockState state = block.getState();
				Wool wool = (Wool)state.getData();
				wool.setColor(color_team1(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()));
				state.update();
			}
			else if(player.hasMetadata(Splatoon.data.Team2Meta))
			{
				BlockState state = block.getState();
				Wool wool = (Wool)state.getData();
				wool.setColor(color_team2(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()));
				state.update();
			}
		}
		else if(block.getType() == Material.GLASS)
		{
			block.setType(Material.STAINED_GLASS);
			block.setData((byte)ColorWoolItem(player));
		}
		else if(block.getType() == Material.THIN_GLASS)
		{
			block.setType(Material.STAINED_GLASS_PANE);
			block.setData((byte)ColorWoolItem(player));
		}
		else if(block.getType() == Material.HARD_CLAY)
		{
			block.setType(Material.STAINED_CLAY);
			block.setData((byte)ColorWoolItem(player));
		}
		else if(block.getType() == Material.STAINED_CLAY)
		{
			block.setType(Material.STAINED_CLAY);
			block.setData((byte)ColorWoolItem(player));
		}
		else if(block.getType() == Material.STAINED_GLASS)
		{
			block.setType(Material.STAINED_GLASS);
			block.setData((byte)ColorWoolItem(player));
		}
		else if(block.getType() == Material.STAINED_GLASS_PANE)
		{
			block.setType(Material.STAINED_GLASS_PANE);
			block.setData((byte)ColorWoolItem(player));
		}
		else if(block.getType() == Material.CARPET)
		{
			block.setType(Material.CARPET);
			block.setData((byte)ColorWoolItem(player));
		}
	}

	public static boolean isAboveBlock(Block block)
	{
		Location loc = block.getLocation().clone();
		loc.add(0, 1, 0);
		if(loc.getBlock().getType() == Material.AIR)
			return true;
		return false;
	}

	public static int RandomColor()
	{
		int randomselect = Splatoon.random.nextInt(8);
		return randomselect;
	}

	/**
	 * 取得したブロックが本当に羊毛なら色名を取得し、返す
	 *
	 * @param block 取得したブロック
	 *
	 * @return DyeColor
	 */
	public static DyeColor itsColor(Block block)
	{
		BlockState state = block.getState();
		MaterialData data = state.getData();
		if(data instanceof Wool)
		{
			Wool wool = (Wool) data;
			return wool.getColor();
		}
		return null;
	}

	/**
	 * 塗ったブロックが敵チームの色が付いたブロックかを調べる
	 *
	 * @param block 対象のブロック
	 */
	@SuppressWarnings("deprecation")
	public static boolean CheckOpponentColor(Block block, Player player)
	{
		byte id = 0;
		if(block.getType() == Material.WOOL)
		{
			String color = itsColor(block).toString();
			if(player.hasMetadata(Splatoon.data.Team1Meta)
					&& color.equalsIgnoreCase(colorname_team2(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()).toString()))
				return true;
			else if(player.hasMetadata(Splatoon.data.Team2Meta)
					&& color.equalsIgnoreCase(colorname_team1(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()).toString()))
				return true;
		}
		else if(block.getType() == Material.GLASS
				|| block.getType() == Material.THIN_GLASS
				|| block.getType() == Material.HARD_CLAY
				|| block.getType() == Material.STAINED_CLAY
				|| block.getType() == Material.STAINED_GLASS
				|| block.getType() == Material.STAINED_GLASS_PANE
				|| block.getType() == Material.CARPET)
		{
			id = block.getState().getBlock().getData();
			if(player.hasMetadata(Splatoon.data.Team1Meta) && id == ColorWoolItem(player))
				return true;
			else if(player.hasMetadata(Splatoon.data.Team2Meta) && id == ColorWoolItem(player))
				return true;
		}
		return false;
	}

	/**
	 * 塗ったブロックが塗るのを許可されているブロックかを調べる
	 *
	 * @param block 対象のブロック
	 * @param player ぬったプレイヤー
	 *
	 * @return 塗ることが出来るならtrueを返す(自分のチームカラーはfalseで返す)
	 */
	public static boolean CheckPaint(Block block, Player player)
	{
		for(String colorname : Splatoon.data.colorname)
		{
			if(block.getType() == Material.WOOL)
			{
				String color = itsColor(block).toString();
				if(color.equalsIgnoreCase(colorname))
					return true;
				if(color.equalsIgnoreCase(
						colorname_team1(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()))
						&& player.hasMetadata(Splatoon.data.Team2Meta))
					return true;
				else if(color.equalsIgnoreCase(
						colorname_team2(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()))
						&& player.hasMetadata(Splatoon.data.Team1Meta))
					return true;
			}
			else if(block.getType() == Material.GLASS
					|| block.getType() == Material.THIN_GLASS
					|| block.getType() == Material.HARD_CLAY
					|| block.getType() == Material.STAINED_CLAY
					|| block.getType() == Material.STAINED_GLASS
					|| block.getType() == Material.STAINED_GLASS_PANE
					|| block.getType() == Material.CARPET)
			{
				if(ColorWoolItem(player) == getColorData(colorname))
					return true;
			}
		}
		return false;
	}
	private static DyeColor select_team1(String arena)
	{
		int i = RandomColor();
		DyeColor team1 = select_team1_wool(i);
		return team1;
	}

	private static DyeColor select_team2(String arena)
	{
		int i = RandomColor();
		DyeColor team2 = select_team2_wool(i);
		return team2;
	}

	private static DyeColor select_team1_wool(int i)
	{
		battle[0] = DyeColor.BLUE;
		battle[1] = DyeColor.LIGHT_BLUE;
		battle[2] = DyeColor.GREEN;
		battle[3] = DyeColor.LIME;
		battle[4] = DyeColor.YELLOW;
		battle[5] = DyeColor.ORANGE;
		battle[6] = DyeColor.PURPLE;
		battle[7] = DyeColor.PINK;
		return battle[i];
	}

	private static DyeColor select_team2_wool(int i)
	{
		battle[0] = DyeColor.BLUE;
		battle[1] = DyeColor.LIGHT_BLUE;
		battle[2] = DyeColor.GREEN;
		battle[3] = DyeColor.LIME;
		battle[4] = DyeColor.YELLOW;
		battle[5] = DyeColor.ORANGE;
		battle[6] = DyeColor.PURPLE;
		battle[7] = DyeColor.PINK;
		return battle[i];
	}

	/**
	 * 入力されたDyeColorをColorに変換
	 *
	 * @param dyecolor 変換するDyeColor
	 */
	public static Color teamSelect(DyeColor dyecolor)
	{
		Color color = null;
		if(dyecolor.equals(DyeColor.BLUE))color = Color.fromRGB(0, 0, 255);//BLUE
		else if(dyecolor.equals(DyeColor.LIGHT_BLUE))color = Color.AQUA;// ok
		else if(dyecolor.equals(DyeColor.GREEN))color = Color.fromRGB(0, 255, 0);//GREEN
		else if(dyecolor.equals(DyeColor.LIME))color = Color.LIME;// ok
		else if(dyecolor.equals(DyeColor.YELLOW))color = Color.YELLOW;// ok
		else if(dyecolor.equals(DyeColor.ORANGE))color = Color.fromRGB(255, 170, 0);// ok
		else if(dyecolor.equals(DyeColor.PURPLE))color = Color.BLUE;//PURLE ok
		else if(dyecolor.equals(DyeColor.PINK))color = Color.FUCHSIA;// ok
		else if(dyecolor.equals(DyeColor.WHITE))color = Color.WHITE;
		return color;
	}

	/**
	 * 入力されたDyeColorをChatColorに変換
	 *
	 *
	 */
	public static ChatColor ChangeChatColor(DyeColor dyecolor)
	{
		ChatColor color = null;
		if(dyecolor.equals(DyeColor.BLUE))color = ChatColor.BLUE;
		else if(dyecolor.equals(DyeColor.LIGHT_BLUE))color =  ChatColor.AQUA;
		else if(dyecolor.equals(DyeColor.GREEN))color = ChatColor.DARK_GREEN;
		else if(dyecolor.equals(DyeColor.LIME))color = ChatColor.GREEN;
		else if(dyecolor.equals(DyeColor.YELLOW))color = ChatColor.YELLOW;
		else if(dyecolor.equals(DyeColor.ORANGE))color = ChatColor.GOLD;
		else if(dyecolor.equals(DyeColor.PURPLE))color = ChatColor.DARK_PURPLE;
		else if(dyecolor.equals(DyeColor.PINK))color = ChatColor.LIGHT_PURPLE;
		else if(dyecolor.equals(DyeColor.WHITE))color = ChatColor.WHITE;
		return color;
	}

	/**
	 * プレイヤー名を入れるとColorで帰ってくる
	 *
	 */
	public static Color PlayerTeamColor(Player player)
	{
		if(player.hasMetadata(Splatoon.data.Team1Meta))
			return teamSelect(color_team1(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()));
		else if(player.hasMetadata(Splatoon.data.Team2Meta))
			return teamSelect(color_team2(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()));
		return null;
	}

	public static byte ColorWoolItem(Player player)
	{
		byte color = 0;
		DyeColor dyecolor = null;
		if(player.hasMetadata(Splatoon.data.Team1Meta))
			dyecolor = color_team1(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString());
		else if(player.hasMetadata(Splatoon.data.Team2Meta))
			dyecolor = color_team2(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString());
		if(dyecolor == null)
			return 0;
		if(dyecolor.equals(DyeColor.BLUE))color = 11;
		else if(dyecolor.equals(DyeColor.LIGHT_BLUE))color = 3;
		else if(dyecolor.equals(DyeColor.GREEN))color = 13;
		else if(dyecolor.equals(DyeColor.LIME))color = 5;
		else if(dyecolor.equals(DyeColor.YELLOW))color = 4;
		else if(dyecolor.equals(DyeColor.ORANGE))color = 1;
		else if(dyecolor.equals(DyeColor.PURPLE))color = 10;
		else if(dyecolor.equals(DyeColor.PINK))color = 6;
		return color;
	}

	public static byte getColorData(String color)
	{
		byte i = 0;
		if(color.equalsIgnoreCase(DyeColor.BLACK.toString()))i=15;
		else if(color.equalsIgnoreCase(DyeColor.BLUE.toString()))i=11;
		else if(color.equalsIgnoreCase(DyeColor.BROWN.toString()))i=12;
		else if(color.equalsIgnoreCase(DyeColor.CYAN.toString()))i=9;
		else if(color.equalsIgnoreCase(DyeColor.GRAY.toString()))i=7;
		else if(color.equalsIgnoreCase(DyeColor.GREEN.toString()))i=13;
		else if(color.equalsIgnoreCase(DyeColor.LIGHT_BLUE.toString()))i=3;
		else if(color.equalsIgnoreCase(DyeColor.LIME.toString()))i=5;
		else if(color.equalsIgnoreCase(DyeColor.MAGENTA.toString()))i=2;
		else if(color.equalsIgnoreCase(DyeColor.ORANGE.toString()))i=1;
		else if(color.equalsIgnoreCase(DyeColor.PINK.toString()))i=6;
		else if(color.equalsIgnoreCase(DyeColor.PURPLE.toString()))i=10;
		else if(color.equalsIgnoreCase(DyeColor.RED.toString()))i=14;
		else if(color.equalsIgnoreCase(DyeColor.SILVER.toString()))i=8;
		else if(color.equalsIgnoreCase(DyeColor.WHITE.toString()))i=0;
		else if(color.equalsIgnoreCase(DyeColor.YELLOW.toString()))i=4;
		return i;
	}
}
