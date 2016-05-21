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

import jp.kotmw.splatoon.API.ParticleAPI;
import jp.kotmw.splatoon.API.ParticleAPI.EnumParticle;
import jp.kotmw.splatoon.arena.ArenaStatus;
import jp.kotmw.splatoon.arena.ArenaStatusEnum;
import jp.kotmw.splatoon.battle.Charge;
import jp.kotmw.splatoon.weapons.Roller;

import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Squid implements Listener
{
	//public List<LivingEntity> Squids = new ArrayList<>();
	static public Map<String, List<LivingEntity>> Squids = new HashMap<>();
	private Map<String, Boolean> wallclimb = new HashMap<>();
	static PotionEffectType Speed = PotionEffectType.SPEED;
	static PotionEffectType Invisible = PotionEffectType.INVISIBILITY;

	@EventHandler
	public void ChangeSquid(PlayerItemHeldEvent e)
	{
		Player player = e.getPlayer();
		String name = player.getName();
		if(Splatoon.data.SneakSquid)
			return;
		if(!player.hasMetadata(Splatoon.data.ArenaMeta))
			return;
		String Metakey = player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString();
		List<LivingEntity> Squidlist = new ArrayList<>();
		int Nslot = e.getNewSlot();
		if(Nslot >= 3)//イカ呼び出し処理
		{
			for(int x = 3; x <= 8; x++)
			{
				if(x == Nslot)
				{
					player.getInventory().setItem(Nslot, new ItemStack(Material.AIR));
					continue;
				}
				player.getInventory().setItem(x, Splatoon.data.item[4][0]);
			}
			if(!Squids.containsKey(Metakey))//対象ステージのイカデータが一切ない場合の例外処理
			{
				player.addPotionEffect(new PotionEffect(Invisible, 360000, 1));
				LivingEntity Squid = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.SQUID);
				Squid.setCustomName(name);
				Squid.setMetadata(Splatoon.data.SquidMeta, new FixedMetadataValue(Splatoon.instance, name));
				Squidlist.add(Squid);
				Squids.put(Metakey, Squidlist);
				return;
			}
			Squidlist = Squids.get(Metakey);
			for(LivingEntity Squid : Squidlist)
			{
				if(Squid.getCustomName().equalsIgnoreCase(name))//プレイヤーと同じ名前のイカが既に居れば処理終了
					return;
			}
			player.addPotionEffect(new PotionEffect(Invisible, 360000, 1));
			player.playSound(player.getLocation(), Sound.SWIM, 1, 1);
			LivingEntity Squid = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.SQUID);
			Squid.setCustomName(name);
			Squid.setMetadata(Splatoon.data.SquidMeta, new FixedMetadataValue(Splatoon.instance, name));
			Squidlist.add(Squid);
			Squids.put(Metakey, Squidlist);
			new Charge(player).runTaskTimer(Splatoon.instance, 0, 1);
			if(this.isBelowPaintWool(player))
			{
				player.addPotionEffect(new PotionEffect(Speed, 360000, 4));
				Squid.addPotionEffect(new PotionEffect(Invisible, 360000, 1));
				Splatoon.data.Squid.put(player.getName(), true);
			} else if(!this.isBelowPaintWool(player)){
				player.removePotionEffect(Speed);
				Squid.removePotionEffect(Invisible);
				Splatoon.data.Squid.put(player.getName(), false);
			}
		} else {//イカ呼び戻し処理
			for(int x = 3; x <= 8; x++)
				player.getInventory().setItem(x, Splatoon.data.item[4][0]);
			if(!Squids.containsKey(Metakey))
				return;
			Squidlist = Squids.get(Metakey);
			for(LivingEntity Squid : Squidlist)
			{
				if(Squid.getMetadata(Splatoon.data.SquidMeta).get(0).asString().equalsIgnoreCase(name))
				{
					player.removePotionEffect(Invisible);
					player.removePotionEffect(Speed);
					if(!player.getGameMode().equals(GameMode.SPECTATOR))
					{
						player.setAllowFlight(false);
						player.setFlying(false);
					}
					player.playSound(player.getLocation(), Sound.SWIM, 1, 1);
					Splatoon.data.Squid.put(name, false);
					Squid.removeMetadata(Splatoon.data.SquidMeta, Splatoon.instance);
					Squid.remove();//そのイカを消す
					for(int i = 0; i <= Squids.get(Metakey).size() -1; i++)
						if(Squids.get(Metakey).get(i).getCustomName().equalsIgnoreCase(Squid.getCustomName()))
						{
							Squids.get(Metakey).remove(i);
							break;
						}
					Squid = null;//イカの初期化
					break;
				}
			}
		}
	}

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e)
	{
		Player player = e.getPlayer();
		String name = player.getName();
		if(!Splatoon.data.SneakSquid)
			return;
		if(!player.hasMetadata(Splatoon.data.ArenaMeta))
			return;
		if(ArenaStatus.getArenaStatus(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()).equals(ArenaStatusEnum.RESULT))
			return;
		String Metakey = player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString();
		List<LivingEntity> Squidlist = new ArrayList<>();
		if(e.isSneaking())
		{
			if(!player.hasMetadata(Splatoon.data.SquidPlayerMeta))//イカ呼び出し処理
			{
				if(!Squids.containsKey(Metakey))//対象ステージのイカデータが一切ない場合の例外処理
				{
					player.setMetadata(Splatoon.data.SquidPlayerMeta, new FixedMetadataValue(Splatoon.instance, player.getName()));
					player.addPotionEffect(new PotionEffect(Invisible, 360000, 1));
					player.getInventory().setHeldItemSlot(3);
					player.playSound(player.getLocation(), Sound.SWIM, 1, 1);
					LivingEntity Squid = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.SQUID);
					Squid.setCustomName(name);
					Squid.setMetadata(Splatoon.data.SquidMeta, new FixedMetadataValue(Splatoon.instance, name));
					Squidlist.add(Squid);
					Squids.put(Metakey, Squidlist);
					return;
				}
				Squidlist = Squids.get(Metakey);
				for(LivingEntity Squid : Squidlist)
				{
					if(Squid.getCustomName().equalsIgnoreCase(name))//プレイヤーと同じ名前のイカが既に居れば処理終了
						return;
				}
				player.setMetadata(Splatoon.data.SquidPlayerMeta, new FixedMetadataValue(Splatoon.instance, player.getName()));
				player.addPotionEffect(new PotionEffect(Invisible, 360000, 1));
				player.getInventory().setHeldItemSlot(3);
				player.playSound(player.getLocation(), Sound.SWIM, 1, 1);
				LivingEntity Squid = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.SQUID);
				Squid.setCustomName(name);
				Squid.setMetadata(Splatoon.data.SquidMeta, new FixedMetadataValue(Splatoon.instance, name));
				Squidlist.add(Squid);
				Squids.put(Metakey, Squidlist);
				new Charge(player).runTaskTimer(Splatoon.instance, 0, 1);
				if(this.isBelowPaintWool(player))
				{
					player.addPotionEffect(new PotionEffect(Speed, 360000, 4));
					Squid.addPotionEffect(new PotionEffect(Invisible, 360000, 1));
					Splatoon.data.Squid.put(player.getName(), true);
				} else if(!this.isBelowPaintWool(player)){
					player.removePotionEffect(Speed);
					Squid.removePotionEffect(Invisible);
					Splatoon.data.Squid.put(player.getName(), false);
				}
			} else {
				Squidlist = Squids.get(Metakey);
				for(LivingEntity Squid : Squidlist)
				{
					if(Squid.getMetadata(Splatoon.data.SquidMeta).get(0).asString().equalsIgnoreCase(name))
					{
						player.removeMetadata(Splatoon.data.SquidPlayerMeta, Splatoon.instance);
						player.getInventory().setHeldItemSlot(0);
						player.removePotionEffect(Invisible);
						player.removePotionEffect(Speed);
						if(!player.getGameMode().equals(GameMode.SPECTATOR))
						{
							player.setAllowFlight(false);
							player.setFlying(false);
						}
						player.playSound(player.getLocation(), Sound.SWIM, 1, 1);
						Splatoon.data.Squid.put(name, false);
						Squid.removeMetadata(Splatoon.data.SquidMeta, Splatoon.instance);
						Squid.remove();//そのイカを消す
						for(int i = 0; i <= Squids.get(Metakey).size() -1; i++)
							if(Squids.get(Metakey).get(i).getCustomName().equalsIgnoreCase(Squid.getCustomName()))
							{
								Squids.get(Metakey).remove(i);
								break;
							}
						Squid = null;//イカの初期化
						break;
					}
				}
			}
		}
	}

	public static void SquidInitialization(Player player)
	{
		String arena = player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString();
		List<LivingEntity> Squidlist = Squids.get(arena);
		for(LivingEntity Squid : Squidlist)
		{
			if(Squid.getMetadata(Splatoon.data.SquidMeta).get(0).asString().equalsIgnoreCase(player.getName()))
			{
				player.removeMetadata(Splatoon.data.SquidPlayerMeta, Splatoon.instance);
				player.removePotionEffect(Invisible);
				player.removePotionEffect(Speed);
				player.setAllowFlight(true);
				player.setFlying(true);
				Squid.removeMetadata(Splatoon.data.SquidMeta, Splatoon.instance);
				Squid.remove();//そのイカを消す
				for(int i = 0; i <= Squids.get(arena).size() -1; i++)
					if(Squids.get(arena).get(i).getCustomName().equalsIgnoreCase(Squid.getCustomName()))
					{
						Squids.get(arena).remove(i);
						break;
					}
				Squid = null;//イカの初期化
				break;
			}
		}
	}


	public static void FinishSquid(List<Player> players)
	{
		for(Player player : players)
		{
			player.removePotionEffect(Invisible);
			player.removePotionEffect(Speed);
			Splatoon.data.Squid.put(player.getName(), false);
			if(player.hasMetadata(Splatoon.data.SquidPlayerMeta))
			{
				player.removeMetadata(Splatoon.data.SquidPlayerMeta, Splatoon.instance);
				player.setSneaking(true);
			}
			if(!Squids.containsKey(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()))
				return;
			if(Squids.get(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()).size() == 0)
				return;
			if(Squids.containsKey(player.getMetadata(Splatoon.data.ArenaMeta)))
				Squids.remove(player.getMetadata(Splatoon.data.ArenaMeta));
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		Player player = e.getPlayer();
		if(!player.hasMetadata(Splatoon.data.ArenaMeta))
			return;
		if(!Squids.containsKey(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()))
			return;
		if(!player.hasPotionEffect(Invisible))//応急処置(/・ω・)/
			return;
		if(!wallclimb.containsKey(player.getName()))
			wallclimb.put(player.getName(), false);
		Location loc = player.getLocation().clone();
		if(getTarget(player))
		{
			player.setAllowFlight(true);
			player.setFlying(true);
			wallclimb.put(player.getName(), true);
		}
		else if(!getTarget(player))
		{
			if(loc.add(0, -1, 0).getBlock().getType() == Material.AIR
					&& wallclimb.get(player.getName()))
			{
				if(getTargetBlock(player) != Material.AIR)
					player.teleport(loc.add(0, 0, 0));
			}
			player.setAllowFlight(false);
			player.setFlying(false);
			wallclimb.put(player.getName(), false);
		}
		for(LivingEntity Squid : Squids.get(player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString()))
		{
			if(Squid.getMetadata(Splatoon.data.SquidMeta).get(0).asString().equalsIgnoreCase(player.getName()))
			{
				Squid.teleport(player);
				if(this.isBelowPaintWool(player))
				{
					player.addPotionEffect(new PotionEffect(Speed, 360000, 4));
					Squid.addPotionEffect(new PotionEffect(Invisible, 360000, 1));
					if(!Splatoon.data.Squid.containsKey(player.getName()))
						Splatoon.data.Squid.put(player.getName(), false);
					if(!Splatoon.data.Squid.get(player.getName()))
						new Charge(player).runTaskTimer(Splatoon.instance, 1, 1);
					Splatoon.data.Squid.put(player.getName(), true);
					ParticleAPI.createEffect(EnumParticle.BLOCK_CRACK.setItem(new ItemStack(Material.WOOL, 1, (short)ColorSelect.ColorWoolItem(player))), player.getLocation(), 0.0f, 0.0f, 0.0f, 0.0f, 10);
				} else if(!this.isBelowPaintWool(player)){
					if(wallclimb.get(player.getName()))
					{
						Squid.addPotionEffect(new PotionEffect(Invisible, 360000, 1));
						break;
					}
					player.removePotionEffect(Speed);
					Squid.removePotionEffect(Invisible);
					Splatoon.data.Squid.put(player.getName(), false);
				}
				break;
			}
		}

	}
	/**
	 * 足元ブロックが自分の色かどうかを調べる
	 *
	 * @param player 対象のプレイヤー
	 *
	 * @return プレイヤーが属しているチームの色だった場合true
	 */
	@SuppressWarnings("deprecation")
	public boolean isBelowPaintWool(Player player)
	{
		boolean is = false;
		Location loc = player.getLocation();
		if(loc.getBlock().getType() != Material.CARPET)
			loc.add(0, -1, 0);
		Block block = loc.getBlock();
		String arena = player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString();
		if(block.getType() == Material.WOOL)
		{
			DyeColor color = ColorSelect.itsColor(block);
			if(player.hasMetadata(Splatoon.data.Team1Meta))
				is = color.equals(ColorSelect.color_team1(arena)) ? true : false;
			else if (player.hasMetadata(Splatoon.data.Team2Meta))
				is = color.equals(ColorSelect.color_team2(arena)) ? true : false;
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
			if(player.hasMetadata(Splatoon.data.Team1Meta))
				is = id ==
						ColorSelect.getColorData(ColorSelect.colorname_team1(arena))
						? true : false;
			else if (player.hasMetadata(Splatoon.data.Team2Meta))
				is = id ==
						ColorSelect.getColorData(ColorSelect.colorname_team2(arena))
						? true : false;
		}
		return is;
	}

	/**
	 * 見てるブロックが自チームの色かどうかを調べる
	 *
	 * @param player 対象のプレイヤー
	 *
	 * @return 自チームの色だったらtrue
	 */
	@SuppressWarnings("deprecation")
	private boolean isTargetBlockTeamColor(Player player, Block block)
	{
		boolean is = false;
		if(block.getType() == null)
			return false;
		String arena = player.getMetadata(Splatoon.data.ArenaMeta).get(0).asString();
		if(block.getType() == Material.WOOL)
		{
			DyeColor color = ColorSelect.itsColor(block);
			if(player.hasMetadata(Splatoon.data.Team1Meta))
				is = color.equals(ColorSelect.color_team1(arena)) ? true : false;
			else if (player.hasMetadata(Splatoon.data.Team2Meta))
				is = color.equals(ColorSelect.color_team2(arena)) ? true : false;
			return is;
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
			if(player.hasMetadata(Splatoon.data.Team1Meta))
				is = id ==
						ColorSelect.getColorData(ColorSelect.colorname_team1(arena))
						? true : false;
			else if (player.hasMetadata(Splatoon.data.Team2Meta))
				is = id ==
						ColorSelect.getColorData(ColorSelect.colorname_team2(arena))
						? true : false;
			return is;
		}
		return is;
	}

	private boolean getTarget(Player player)
	{
		Location loc = player.getLocation().clone();
		float dir = loc.getYaw();
		int PD = Roller.PlayerDirection(dir);
		if(PD == 5)
			PD = Roller.PlayerDirection(dir);
		if(PD == 0)
		{
			loc.add(0, 0.5, 1);
			return isTargetBlockTeamColor(player, loc.getBlock());
		}
		else if(PD == 1)
		{
			loc.add(-1, 0.5, 0);
			return isTargetBlockTeamColor(player, loc.getBlock());
		}
		else if(PD == 2)
		{
			loc.add(0, 0.5, -1);
			return isTargetBlockTeamColor(player, loc.getBlock());
		}
		else if(PD == 3)
		{
			loc.add(1, 0.5, 0);
			return isTargetBlockTeamColor(player, loc.getBlock());
		}
		return false;
	}

	private Material getTargetBlock(Player player)
	{
		Location loc = player.getLocation().clone();
		float dir = loc.getYaw();
		int PD = Roller.PlayerDirection(dir);
		if(PD == 5)
			PD = Roller.PlayerDirection(dir);
		if(PD == 0)
			loc.add(0, 0.5, 1);
		else if(PD == 1)
			loc.add(-1, 0.5, 0);
		else if(PD == 2)
			loc.add(0, 0.5, -1);
		else if(PD == 3)
			loc.add(1, 0.5, 0);
		return loc.getBlock().getType();
	}
}
