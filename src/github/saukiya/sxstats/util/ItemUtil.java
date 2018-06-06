package github.saukiya.sxstats.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import github.saukiya.sxstats.SXStats;

/**
 * @author Saukiya
 * @since 2018年3月22日
 */

public class ItemUtil {
	
	static Class<?> xCraftItemStack = null;
	static Class<?> xNMSItemStack;
	static Class<?> xNBTTagCompound;
	static Class<?> xNBTTagList;
	static Class<?> xNBTTagString;
	static Class<?> xNBTTagDouble;
	static Class<?> xNBTTagInt;
	static Class<?> xNBTBase;
	static Constructor<?> xNewNBTTagString;
	static Constructor<?> xNewNBTTagDouble;
	static Constructor<?> xNewNBTTagInt;
	
	static Method xAsNMSCopay;
	static Method xGetTag;
	static Method xHasTag;
	static Method xSetString;
	static Method xSet;
	static Method xAdd;
	static Method xRemove;
	static Method xSetTag;
	static Method xAsBukkitCopy;

	static Method xHasKey;
	static Method xGetString;

	public static void clearAttribute(Player player){
		 EntityEquipment eq = player.getEquipment();
		 for(ItemStack item : eq.getArmorContents()){
			 if(item != null && item.hasItemMeta() && item.getItemMeta().hasLore()){
				 if (item.getType().getMaxDurability() != 0){
					 clearAttribute(item);
				 }
			 }
		 }
		 ItemStack mainItem = eq.getItemInMainHand();
		 if(mainItem != null && mainItem.hasItemMeta() && mainItem.getItemMeta().hasLore()){
			 if (mainItem.getType().getMaxDurability() != 0){
				 clearAttribute(mainItem);
			 }
		 }
		 ItemStack offItem = eq.getItemInOffHand();
		 if(offItem != null && offItem.hasItemMeta() && offItem.getItemMeta().hasLore()){
			 if (offItem.getType().getMaxDurability() != 0){
				 clearAttribute(offItem);
			 }
		 }
	}
	
	public static double getAttackSpeed(ItemStack item,double... addSpeed){
		double attackSpeed = -0.30D;
		String itemType = item.getType().name();
		if(itemType.contains("_PICKAXE")){
			attackSpeed = -0.70D;
		}
		else if (itemType.contains("_AXE")){
			attackSpeed = -0.80D;
		}
		else if (itemType.contains("_HOE")){
			attackSpeed = -0.50D;
		}
		else if (itemType.contains("_SPADE")){
			attackSpeed = -0.77D;
		}
		else if (itemType.contains("_SWORD")){
			attackSpeed = -0.60D;
		}
		if(addSpeed.length > 0){
			attackSpeed += addSpeed[0]/500D;
		}
		return attackSpeed <= -1D ? -0.99D : attackSpeed;
	}
	
	public static ItemStack setAttackSpeed(ItemStack item, double... speed){
		if(item != null && !item.getType().name().equals("AIR")){
			try {
				Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack,ItemUtil.setNBT(item, "Clear", "yes"+Config.isDamageGauges()));
				Object compound = ((Boolean)xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem): xNBTTagCompound.newInstance();
				Object modifiers = xNBTTagList.newInstance();
				Object attackSpeed = xNBTTagCompound.newInstance();
				xSet.invoke(attackSpeed, "AttributeName",xNewNBTTagString.newInstance("generic.attackSpeed"));
				xSet.invoke(attackSpeed, "Name",xNewNBTTagString.newInstance("AttackSpeed"));
				xSet.invoke(attackSpeed, "Amount",xNewNBTTagDouble.newInstance(getAttackSpeed(item,speed)));
				xSet.invoke(attackSpeed, "Operation",xNewNBTTagInt.newInstance(1));
				xSet.invoke(attackSpeed, "UUIDLeast",xNewNBTTagInt.newInstance(20000));
				xSet.invoke(attackSpeed, "UUIDMost",xNewNBTTagInt.newInstance(1000));
				xSet.invoke(attackSpeed, "Slot",xNewNBTTagString.newInstance("mainhand"));
				xAdd.invoke(modifiers, attackSpeed);
				xSet.invoke(compound, "AttributeModifiers" ,modifiers);
				xSetTag.invoke(nmsItem, compound);
				ItemMeta meta = ((ItemStack) xAsBukkitCopy.invoke(xCraftItemStack, nmsItem)).getItemMeta();
				meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				item.setItemMeta(meta);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
			
		}
		return item;
	}
	
	// 清除物品默认属性标签
	public static ItemStack clearAttribute(ItemStack item){
		if (item == null || (ItemUtil.isNBT(item, "Clear") && ItemUtil.getNBT(item, "Clear").equals("yes"+Config.isDamageGauges()))){
			return item;
		}
		else {
			try {
				Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack,ItemUtil.setNBT(item, "Clear", "yes"+Config.isDamageGauges()));
				Object compound = ((Boolean)xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem): xNBTTagCompound.newInstance();
				Object modifiers = xNBTTagList.newInstance();
				xSet.invoke(compound, "AttributeModifiers" ,modifiers);
				xSetTag.invoke(nmsItem, compound);
				ItemMeta meta = ((ItemStack) xAsBukkitCopy.invoke(xCraftItemStack, nmsItem)).getItemMeta();
				if(Config.isDamageGauges()){
					meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				}
				item.setItemMeta(meta);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
			return item;
		}
	}
	
	public static void removeAttribute(Player player){
		 EntityEquipment eq = player.getEquipment();
		 for(ItemStack item : eq.getArmorContents()){
			 if(item != null && item.hasItemMeta() && item.getItemMeta().hasLore()){
				 if (item.getType().getMaxDurability() != 0){
					 removeAttribute(item);
				 }
			 }
		 }
		 ItemStack mainItem = eq.getItemInMainHand();
		 if(mainItem != null && mainItem.hasItemMeta() && mainItem.getItemMeta().hasLore()){
			 if (mainItem.getType().getMaxDurability() != 0){
				 removeAttribute(mainItem);
			 }
		 }
		 ItemStack offItem = eq.getItemInOffHand();
		 if(offItem != null && offItem.hasItemMeta() && offItem.getItemMeta().hasLore()){
			 if (offItem.getType().getMaxDurability() != 0){
				 removeAttribute(offItem);
			 }
		 }
	}
	
	// 恢复物品默认属性标签 暂时无效
	public static ItemStack removeAttribute(ItemStack item){
		if (item == null || (ItemUtil.isNBT(item, "Clear") && ItemUtil.getNBT(item, "Clear").equals("no"))){
			return item;
		}
		else {
			try {
				Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack,ItemUtil.setNBT(item, "Clear", "no"));
				Object compound = ((Boolean)xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem): xNBTTagCompound.newInstance();
				xRemove.invoke(compound, "AttributeModifiers");
				xSetTag.invoke(nmsItem, compound);
				item.setItemMeta(((ItemStack) xAsBukkitCopy.invoke(xCraftItemStack, nmsItem)).getItemMeta());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
			ItemMeta meta = item.getItemMeta();
			meta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(meta);
			return item;
		}
	}
	
	
	//设置物品NBT数据
	public static ItemStack setNBT(ItemStack item,String key,String value){
		try {
			Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack,item);
			Object itemTag = ((Boolean)xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem): xNBTTagCompound.newInstance();
			Object tagString = xNewNBTTagString.newInstance(value);
			xSet.invoke(itemTag, SXStats.getPlugin().getName()+"-"+key ,tagString);
			xSetTag.invoke(nmsItem, itemTag);
			item = (ItemStack) xAsBukkitCopy.invoke(xCraftItemStack, nmsItem);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			//
		}
		return item;
	}
	
	//获取物品NBT数据
	public static String getNBT(ItemStack item,String key){
		try {
			Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack,item);
			Object itemTag = ((Boolean)xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem): xNBTTagCompound.newInstance();
			if((boolean) xHasKey.invoke(itemTag, SXStats.getPlugin().getName()+"-"+key))return (String) xGetString.invoke(itemTag, SXStats.getPlugin().getName()+"-"+key);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			//
		}
		return null;
	}
	
	//判断是否有物品NBT数据
	public static Boolean isNBT(ItemStack item,String key){
		try {
			Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack,item);
			Object itemTag = ((Boolean)xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem): xNBTTagCompound.newInstance();
			if((boolean) xHasKey.invoke(itemTag, SXStats.getPlugin().getName()+"-"+key))return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			//
		}
		return false;
	}
	
	// 快速处理ItemStack
	@SuppressWarnings("deprecation")
	public static ItemStack getItemStack(String itemName,String id,List<String> itemLore,Boolean enchant,Boolean unbreakable){
        int itemMaterial = 351;
        int itemDurability = 0;
        if(id != null){
            if (id.contains(":")){
            	itemMaterial = Integer.valueOf(id.split(":")[0]);
            	itemDurability = Integer.valueOf(id.split(":")[1]);
            }else{
            	itemMaterial = Integer.valueOf(id);
            }
        }
		ItemStack item = new ItemStack(itemMaterial,1,(short) itemDurability);
		ItemMeta meta = item.getItemMeta();
		if (itemName != null) meta.setDisplayName(itemName.replace("&", "§"));
		if (itemLore != null) {
			for(int i=0;i < itemLore.size();i++){
				itemLore.set(i, itemLore.get(i).replace("&", "§"));
			}
			meta.setLore(itemLore);
		}
		if (enchant!=null && enchant){
			meta.addEnchant(Enchantment.DURABILITY, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		if (unbreakable != null && unbreakable){
			meta.setUnbreakable(true);
			meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		}
		item.setItemMeta(meta);
		return item;
	}
	
	public static void loadItemUtil(){
		if (xCraftItemStack == null){
			//反射预处理
			String packet = Bukkit.getServer().getClass().getPackage().getName();
			String nmsversion = packet.substring(packet.lastIndexOf('.') + 1);
			String NMSname = "net.minecraft.server." + nmsversion;
        	try {
				xCraftItemStack = Class.forName(packet+".inventory.CraftItemStack");
				xNMSItemStack = Class.forName(NMSname+".ItemStack");
				xNBTTagCompound = Class.forName(NMSname+".NBTTagCompound");
				xNBTTagString = Class.forName(NMSname+".NBTTagString");
				xNBTTagDouble = Class.forName(NMSname+".NBTTagDouble");
				xNBTTagInt = Class.forName(NMSname+".NBTTagInt");
				xNBTTagList = Class.forName(NMSname+".NBTTagList");
				xNBTBase = Class.forName(NMSname+".NBTBase");
				xNewNBTTagString = xNBTTagString.getConstructor(String.class);
				xNewNBTTagDouble = xNBTTagDouble.getConstructor(double.class);
				xNewNBTTagInt = xNBTTagInt.getConstructor(int.class);
				
				xAsNMSCopay = xCraftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
				xGetTag = xNMSItemStack.getDeclaredMethod("getTag");
				xHasTag = xNMSItemStack.getDeclaredMethod("hasTag");
				xSet = xNBTTagCompound.getDeclaredMethod("set", new Class[]{String.class,xNBTBase});
				xAdd = xNBTTagList.getDeclaredMethod("add", xNBTBase);
				xRemove = xNBTTagCompound.getDeclaredMethod("remove",String.class);
				xSetTag = xNMSItemStack.getDeclaredMethod("setTag", xNBTTagCompound);
				xAsBukkitCopy = xCraftItemStack.getDeclaredMethod("asBukkitCopy", xNMSItemStack);
				
				xHasKey = xNBTTagCompound.getDeclaredMethod("hasKey", String.class);
				xGetString = xNBTTagCompound.getDeclaredMethod("getString", String.class);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
			}
        	Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] Load ItemUtil! ");
		}
	}
}
