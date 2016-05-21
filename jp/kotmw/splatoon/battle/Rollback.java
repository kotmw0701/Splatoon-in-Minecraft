/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.kotmw.splatoon.Splatoon;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.Wool;

public class Rollback
{
	public static Map<String, List<BlockState>> Blocks = new HashMap<>();
	public static Map<String, Map<Integer, ItemStack>> BeforeInv = new HashMap<>();
	private static Map<String, ItemStack> H = new HashMap<>();
	private static Map<String, ItemStack> C = new HashMap<>();
	private static Map<String, ItemStack> L = new HashMap<>();
	private static Map<String, ItemStack> B = new HashMap<>();

	public void setPaintedBlock(Block block, String arena)
	{
		List<BlockState> listblocks = new ArrayList<>();
		if(!Blocks.containsKey(arena) || Blocks.get(arena).size() == 0)
		{
			listblocks.add(block.getState());
			Blocks.put(arena, listblocks);
			return;
		}
		listblocks = Blocks.get(arena);
		for(BlockState blocks : listblocks)
		{
			Location loc = block.getLocation();
			if(loc.equals(blocks.getLocation()))
				return;
		}
		listblocks.add(block.getState());
		Blocks.put(arena, listblocks);
		if(Splatoon.data.DebugMode)
			System.out.println("" + block);
	}

	public static void rollback(String arena)
	{
		if(!Blocks.containsKey(arena))
			return;
		Block block;
		for(BlockState state : Blocks.get(arena))
		{
			block = state.getBlock();
			if(state.getType() == Material.WOOL)
			{
				Wool wool = (Wool)state.getData();
				wool.setColor(wool.getColor());
				state.update();
			}
			else if(state.getType() == Material.STAINED_CLAY
					|| state.getType() == Material.STAINED_GLASS
					|| state.getType() == Material.STAINED_GLASS_PANE
					|| state.getType() == Material.CARPET)
			{
				state.setData(state.getData());
				state.update();
			}
			else if(state.getType() == Material.GLASS)
				block.setType(Material.GLASS);
			else if(state.getType() == Material.THIN_GLASS)
				block.setType(Material.THIN_GLASS);
			else if(state.getType() == Material.HARD_CLAY)
				block.setType(Material.HARD_CLAY);
		}
		Blocks.remove(arena);
	}

	public static void SaveRollBackInv(Player player)
	{
		String name = player.getName();
		Map<Integer, ItemStack> inv = new HashMap<>();
		for(int i = 0; i <= 35; i++)
		{
			ItemStack item = player.getInventory().getItem(i);
			if(item == null)
				item = new ItemStack(Material.AIR);
			inv.put(i, item);
		}
		BeforeInv.put(name, inv);
		H.put(name, player.getInventory().getHelmet());
		C.put(name, player.getInventory().getChestplate());
		L.put(name, player.getInventory().getLeggings());
		B.put(name, player.getInventory().getBoots());
	}

	/*
	 * 9  10 11 12 13 14 15 16 17
	 * 18 19 20 21 22 23 24 25 26
	 * 27 28 29 30 31 32 33 34 35
	 * --------------------------
	 * 0  1  2  3  4  5  6  7  8
	 */
	public static boolean InvRollBack(Player player)
	{
		PlayerInventory inv = player.getInventory();
		if(BeforeInv.containsKey(player.getName())
				&& H.containsKey(player.getName())
				&& C.containsKey(player.getName())
				&& L.containsKey(player.getName())
				&& B.containsKey(player.getName()))
		{
			for(int i = 0; i <= 35; i++)
				inv.setItem(i, BeforeInv.get(player.getName()).get(i));
			inv.setHelmet(H.get(player.getName()));
			inv.setChestplate(C.get(player.getName()));
			inv.setLeggings(L.get(player.getName()));
			inv.setBoots(B.get(player.getName()));

			BeforeInv.remove(player.getName());
			H.remove(player.getName());
			C.remove(player.getName());
			L.remove(player.getName());
			B.remove(player.getName());
			return true;
		}
		return false;
	}
}
