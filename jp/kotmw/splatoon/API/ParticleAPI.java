package jp.kotmw.splatoon.API;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ParticleAPI{
	private static String PackageName;
	private static boolean v1_7 = false;
	private static boolean v1_8 = false;
	static {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		PackageName = "net.minecraft.server."+version;
		if(version.startsWith("v1_8")){
			v1_8 = true;
		}else if(version.startsWith("v1_7")){
			v1_7 = true;
		}
	}
	public static boolean createEffect(EnumParticle effect, Location location, float xd, float yd, float zd, float speed, int amount){
		return createEffect(effect,(float)location.getX(),(float)location.getY(),(float)location.getZ(),xd,yd,zd,speed,amount);
	}
	public static boolean createEffect(String effect, Location location, float xd, float yd, float zd, float speed, int amount){
		return createEffect(effect,(float)location.getBlockX(),(float)location.getBlockY(),(float)location.getBlockZ(),xd,yd,zd,speed,amount);
	}
	public static boolean createEffect(int id, Location location, float xd, float yd, float zd, float speed, int amount){
		return createEffect(id,(float)location.getBlockX(),(float)location.getBlockY(),(float)location.getBlockZ(),xd,yd,zd,speed,amount);
	}
	public static boolean createEffect(Object name, float x, float y, float z, float xd, float yd, float zd, float speed, int amount){
		try{
			Object ParticlesPacket = getCraftClass("PacketPlayOutWorldParticles").newInstance();
			EnumParticle par = convert(name);
			for (Field field : ParticlesPacket.getClass().getDeclaredFields()){
				field.setAccessible(true);
				String fieldName = field.getName();
				if (fieldName.equals("a")) {
					field.set(ParticlesPacket, getEnumParticle(par));
				} else if (fieldName.equals("b")) {
					field.setFloat(ParticlesPacket, x);
				} else if (fieldName.equals("c")) {
					field.setFloat(ParticlesPacket, y);
				} else if (fieldName.equals("d")) {
					field.setFloat(ParticlesPacket, z);
				} else if (fieldName.equals("e")) {
					field.setFloat(ParticlesPacket, xd);
				} else if (fieldName.equals("f")) {
					field.setFloat(ParticlesPacket, yd);
				} else if (fieldName.equals("g")) {
					field.setFloat(ParticlesPacket, zd);
				} else if (fieldName.equals("h")) {
					field.setFloat(ParticlesPacket, speed);
				} else if (fieldName.equals("i")) {
					field.setInt(ParticlesPacket, amount);
				} else if (fieldName.equals("j")) { //since v1.8
					field.setBoolean(ParticlesPacket, false);
				} else if (fieldName.equals("k")) { //since v1.8
					field.set(ParticlesPacket, par.data);
				}
			}
			for(Player p : getOnlinePlayers()){
				sendPacket(p, ParticlesPacket);
			}
			return true;
		}catch(Throwable ex){
			return false;
		}
	}
	private static EnumParticle convert(Object obj) throws Exception{
		EnumParticle par = null;
		if(obj instanceof EnumParticle){
			par = (EnumParticle)obj;
		}else if(obj instanceof String){
			par = EnumParticle.getParticle((String)obj);
		}else if(obj instanceof Integer){
			par = EnumParticle.getParticle((Integer)obj);
		}
		if(par != null){
			if(v1_7 && par.isNewParticle()) throw new Exception();
			return par;
		}else throw new Exception();
	}
	private static Object getEnumParticle(EnumParticle par) throws Exception{
		if(v1_7){
			//Return String on ver1.7
			return par.getName();
		}else if(v1_8){
			//Return EnumParticle on ver1.8
			int i = par.getID();
			if(EnumParticle.getParticle(i) == null) throw new Exception();
			Class<?> EnumParticleClass = getCraftClass("EnumParticle").getEnumConstants()[0].getClass();
			Method a = EnumParticleClass.getDeclaredMethod("a", int.class);
			Object EnumParticle = a.invoke(EnumParticleClass, i);
			return EnumParticle;
		}else{
			//Unknow version
			throw new Exception();
		}
	}
	private static Class<?> getCraftClass(String s) throws Exception {
		Class<?> craftclass = Class.forName(PackageName+"."+s);
		return craftclass;
	}
	private static void sendPacket(Player p, Object packet) throws Exception{
		Method PlayerHandle = p.getClass().getMethod("getHandle");
		Object EntityPlayer = PlayerHandle.invoke(p);

		Object PlayerConnection = null;
		for(Field field : EntityPlayer.getClass().getDeclaredFields()){
			if(field.getName().equals("playerConnection")){
				PlayerConnection = field.get(EntityPlayer);
				break;
			}

		}
		Method PlayerConnectionMethod = null;
		for(Method m : PlayerConnection.getClass().getDeclaredMethods()){
			if(m.getName().equals("sendPacket")){
				PlayerConnectionMethod = m;
				break;
			}
		}
		PlayerConnectionMethod.invoke(PlayerConnection, packet);
	}
	private static Player[] getOnlinePlayers(){
		try{
			return Bukkit.getOnlinePlayers().toArray(new Player[0]);
		}catch(NoSuchMethodError e){
			try{
				Method getOnlinePlayers = null;
				for(Method m : Bukkit.class.getDeclaredMethods()){
					if(m.getName().equals("getOnlinePlayers")){
						getOnlinePlayers = m;
						break;
					}
				}
				if(getOnlinePlayers == null) return new Player[]{};
				Object OnlinePlayers = getOnlinePlayers.invoke(Bukkit.class, new Object[0]);
				if(OnlinePlayers instanceof Player[]) return (Player[])OnlinePlayers;
			}catch (Exception es){}
			return new Player[]{};
		}
	}

	public static enum EnumParticle{
		EXPLOSION("explode",0),
		EXPLOSION_LARGE("largeexplode",1),
		EXPLOSION_HUGE("hugeexplosion",2),
		FIREWORKS_SPARK("fireworksSpark",3),
		WATER_BUBBLE("bubble",4),
		WATER_SPLASH("splash",5),
		WATER_WAKE("wake",6),
		SUSPENDED("suspended",7),
		SUSPENDED_DEPTH("depthsuspend",8),
		CRIT("crit",9),
		CRIT_MAGIC("magicCrit",10),
		SMOKE_NORMAL("smoke",11),
		SMOKE_LARGE("largesmoke",12),
		SPELL("spell",13),
		SPELL_INSTANT("instantSpell",14),
		SPELL_MOB("mobSpell",15),
		SPELL_MOB_AMBIENT("mobSpellAmbient",16),
		SPELL_WITCH("witchMagic",17),
		DRIP_WATER("dripWater",18),
		DRIP_LAVA("dripLava",19),
		VILLAGER_ANGRY("angryVillager",20),
		VILLAGER_HAPPY("happyVillager",21),
		TOWN_AURA("townaura",22),
		NOTE("note",23),
		PORTAL("portal",24),
		ENCHANTMENT_TABLE("enchantmenttable",25),
		FLAME("flame",26),
		LAVA("lava",27),
		FOOTSTEP("footstep",28),
		CLOUD("cloud",29),
		REDSTONE("reddust",30),
		SNOWBALL("snowballpoof",31),
		SNOW_SHOVEL("snowshovel",32),
		SLIME("slime",33),
		HEART("heart",34),
		BARRIER("barrier",35),
		ITEM_CRACK("iconcrack",36),
		BLOCK_CRACK("blockcrack",37),
		BLOCK_DUST("blockdust",38),
		WATER_DROP("droplet",39),
		ITEM_TAKE("take",40),
		MOB_APPEARANCE("mobappearance",41);

		private String name;
		private int id;
		private int[] data;

		private static Map<String, EnumParticle> X;
		private static Map<Integer, EnumParticle> Y;

		static{
			X = new HashMap<String, EnumParticle>();
			Y = new HashMap<Integer, EnumParticle>();
			for(EnumParticle a : values()){
				X.put(a.getName(), a);
				Y.put(a.getID(), a);
			}
		}
		private EnumParticle(String name, int id){
			this.name = name;
			this.id = id;
			if(id == 36){
				data = new int[2];
				data[0] = 256;
				data[1] = 0;
			}else if(id == 37){
				data = new int[2];
				data[0] = 1;
				data[1] = 0;
			}else if(id == 38){
				data = new int[2];
				data[0] = 1;
				data[1] = 0;
			}else data = null;
		}
		public String getName(){
			if(this.equals(ITEM_CRACK)|this.equals(BLOCK_CRACK)|this.equals(BLOCK_DUST)){
				return name+"_"+data[0]+"_"+data[1];
			}
			else return name;
		}
		public int getID(){
			return id;
		}
		public boolean isNewParticle(){
			return (this.equals(BARRIER)|this.equals(BLOCK_CRACK)|
			this.equals(BLOCK_DUST)|this.equals(WATER_DROP)|this.equals(ITEM_TAKE)|this.equals(MOB_APPEARANCE));
		}
		@SuppressWarnings("deprecation")
		public EnumParticle setItem(ItemStack item){
			int id = item.getTypeId();
			int data = (int)item.getDurability();
			return setItemIDandData(id, data);
		}
		@SuppressWarnings("deprecation")
		public EnumParticle setBlock(Block block){
			int id = block.getTypeId();
			int data = (int)block.getData();
			return setItemIDandData(id, data);
		}
		@SuppressWarnings("deprecation")
		public EnumParticle setMaterial(Material material){
			int id = material.getId();
			return setItemID(id);
		}
		public EnumParticle setItemID(int id){
			return setItemIDandData(id, this.data[1]);
		}
		public EnumParticle setItemData(int data){
			return setItemIDandData(this.data[0], data);
		}
		public EnumParticle setItemIDandData(int id, int data){
			if(this.data != null){
				if(this.equals(EnumParticle.BLOCK_CRACK) | this.equals(EnumParticle.BLOCK_DUST)) {
					this.data[0] = data*4096+id;
					this.data[1] = 0;
				} else if(this.equals(EnumParticle.ITEM_CRACK)) {
					this.data[0] = id;
					this.data[1] = data;
				}
			}
			return this;
		}

		public static EnumParticle getParticle(String name){
			if(X.containsKey(name)) return X.get(name);
			else return null;
		}
		public static EnumParticle getParticle(int id){
			if(Y.containsKey(id)) return Y.get(id);
			else return null;
		}
	}
}