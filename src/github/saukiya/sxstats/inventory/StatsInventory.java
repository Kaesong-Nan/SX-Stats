package github.saukiya.sxstats.inventory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import github.saukiya.sxstats.SXStats;
import github.saukiya.sxstats.data.StatsData;
import github.saukiya.sxstats.data.StatsDataManager;
import github.saukiya.sxstats.util.Message;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;

/**
 * @author Saukiya
 * @since 2018年3月24日
 */

public class StatsInventory {
	@Getter
	private static List<UUID> hideList = new ArrayList<>();
	
	final private static DecimalFormat df = new DecimalFormat("#.##");

	public static void openStatsInventory(Player player){
		StatsData data = StatsDataManager.getEntityData(player);
		Inventory inv = Bukkit.createInventory(null, 27,Message.getMsg(Message.INVENTORY_STATS_NAME));
	    ItemStack stainedGlass = new ItemStack(Material.STAINED_GLASS_PANE,1,(short) 15);
	    ItemMeta glassMeta = stainedGlass.getItemMeta();
	    glassMeta.setDisplayName("§c");
	    stainedGlass.setItemMeta(glassMeta);
	    ItemStack skull = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
	    ItemMeta skullmeta = skull.getItemMeta();
	    List<String> skullLoreList = new ArrayList<>();
	    if(hideList.contains(player.getUniqueId())){
	    	skullLoreList.add(Message.getMsg(Message.INVENTORY_STATS_HIDE_OFF));
	    }
	    else {
	    	skullLoreList.add(Message.getMsg(Message.INVENTORY_STATS_HIDE_ON));
	    }
	    skullLoreList.addAll(setPlaceholders(data,Message.getList(Message.INVENTORY_STATS_SKULL_LORE)));
		if(SXStats.isPlaceholder()){
			skullLoreList = PlaceholderAPI.setPlaceholders(player, skullLoreList);
		}
		skullmeta.setLore(skullLoreList);
	    skullmeta.setDisplayName(Message.getMsg(Message.INVENTORY_STATS_SKULL_NAME, player.getDisplayName()));
	    ((SkullMeta) skullmeta).setOwner(player.getName());
	    skull.setItemMeta(skullmeta);
	    for (int i=0;i<9;i++){
	    	if (i==4){
		    	inv.setItem(i, skull);
	    	}else {
		    	inv.setItem(i, stainedGlass);
	    	}
	    }
	    for (int i=18;i<27;i++){
	    	inv.setItem(i, stainedGlass);
	    }
	    inv.setItem(10, StatsInventory.getAttackUI(player,data));
	    inv.setItem(13, StatsInventory.getDefenseUI(player,data));
	    inv.setItem(16, StatsInventory.getBaseUI(player,data));
		player.openInventory(inv);
	}
	
	
	

	private static ItemStack getAttackUI(Player player,StatsData data){
			ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
			ItemMeta meta = item.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			meta.setDisplayName(Message.getMsg(Message.INVENTORY_STATS_ATTACK));
			List<String> loreList = setPlaceholders(data,Message.getList(Message.INVENTORY_STATS_ATTACK_LORE));
			if(SXStats.isPlaceholder()){
				loreList = PlaceholderAPI.setPlaceholders(player, loreList);
			}
			if(!hideList.contains(player.getUniqueId())){
				for(int i = loreList.size()-1 ; i >=0 ; i--){
					if(Double.valueOf(StatsDataManager.getStats(StatsDataManager.clearColor(loreList.get(i))).replace("-", "")) == 0D){
						loreList.remove(i);
					}
				}
			}
			meta.setLore(loreList);
			item.setItemMeta(meta);
			return item;
		}

	private static ItemStack getDefenseUI(Player player,StatsData data){
		ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName(Message.getMsg(Message.INVENTORY_STATS_DEFENSE));
		List<String> loreList = setPlaceholders(data,Message.getList(Message.INVENTORY_STATS_DEFENSE_LORE));
		if(SXStats.isPlaceholder()){
			loreList = PlaceholderAPI.setPlaceholders(player, loreList);
		}
		if(!hideList.contains(player.getUniqueId())){
			for(int i = loreList.size()-1 ; i >=0 ; i--){
				if(Double.valueOf(StatsDataManager.getStats(StatsDataManager.clearColor(loreList.get(i))).replace("-", "")) == 0D){
					loreList.remove(i);
				}
			}
		}
		meta.setLore(loreList);
		item.setItemMeta(meta);
		return item;
	}

	private static ItemStack getBaseUI(Player player,StatsData data){
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Message.getMsg(Message.INVENTORY_STATS_BASE));
		List<String> loreList = setPlaceholders(data,Message.getList(Message.INVENTORY_STATS_BASE_LORE));
		if(SXStats.isPlaceholder()){
			loreList = PlaceholderAPI.setPlaceholders(player, loreList);
		}
		if(!hideList.contains(player.getUniqueId())){
			for(int i = loreList.size()-1 ; i >=0 ; i--){
				if(Double.valueOf(StatsDataManager.getStats(StatsDataManager.clearColor(loreList.get(i))).replace("-", "")) == 0D){
					loreList.remove(i);
				}
			}
		}
		meta.setLore(loreList);
		item.setItemMeta(meta);
		return item;
	}

	static List<String> setPlaceholders(StatsData data,List<String> list){
		for(int i = 0 ; i < list.size() ; i++){
			String lore = list.get(i);
			while(lore.contains("%") && lore.split("%").length > 1 && lore.split("%")[1].contains("sx_") && lore.split("%")[1].split("_").length > 1){
				String string = lore.split("%")[1].split("_")[1];
				Double b = -1D;
				if(string.equalsIgnoreCase("expAddition")) b = data.getExpAddition();
				else if(string.equalsIgnoreCase("speed")) b = data.getSpeed();
				else if(string.equalsIgnoreCase("health")) b = data.getHealth();
				else if(string.equalsIgnoreCase("healthRegen")) b = data.getHealthRegen();
				else if(string.equalsIgnoreCase("dodge")) b = data.getDodge();
				else if(string.equalsIgnoreCase("minDefense")) b = data.getMinDefense();
				else if(string.equalsIgnoreCase("maxDefense")) b = data.getMaxDefense();
				else if(string.equalsIgnoreCase("pvpMinDefense")) b = data.getPvpMinDefense();
				else if(string.equalsIgnoreCase("pvpMaxDefense")) b = data.getPvpMaxDefense();
				else if(string.equalsIgnoreCase("pveMinDefense")) b = data.getPveMinDefense();
				else if(string.equalsIgnoreCase("pveMaxDefense")) b = data.getPveMaxDefense();
				else if(string.equalsIgnoreCase("toughness")) b = data.getToughness();
				else if(string.equalsIgnoreCase("reflectionRate")) b = data.getReflectionRate();
				else if(string.equalsIgnoreCase("reflection")) b = data.getReflection();
				else if(string.equalsIgnoreCase("blockRate")) b = data.getBlockRate();
				else if(string.equalsIgnoreCase("block")) b = data.getBlock();
				else if(string.equalsIgnoreCase("minDamage")) b = data.getMinDamage() == 0 ? 1 : data.getMinDamage();
				else if(string.equalsIgnoreCase("maxDamage")) b = data.getMaxDamage() == 0 ? 1 : data.getMinDamage();
				else if(string.equalsIgnoreCase("pvpMinDamage")) b = data.getPvpMinDamage();
				else if(string.equalsIgnoreCase("pvpMaxDamage")) b = data.getPvpMaxDamage();
				else if(string.equalsIgnoreCase("pveMinDamage")) b = data.getPveMinDamage();
				else if(string.equalsIgnoreCase("pveMaxDamage")) b = data.getPveMaxDamage();
				else if(string.equalsIgnoreCase("hitRate")) b = data.getHitRate();
				else if(string.equalsIgnoreCase("real")) b = data.getReal();
				else if(string.equalsIgnoreCase("crit")) b = data.getCritRate();
				else if(string.equalsIgnoreCase("critDamage")) b = data.getCritDamage();
				else if(string.equalsIgnoreCase("lifeSteal")) b = data.getLifeSteal();
				else if(string.equalsIgnoreCase("ignition")) b = data.getIgnition();
				else if(string.equalsIgnoreCase("wither")) b = data.getWither();
				else if(string.equalsIgnoreCase("poison")) b = data.getPoison();
				else if(string.equalsIgnoreCase("blindness")) b = data.getBlindness();
				else if(string.equalsIgnoreCase("slowness")) b = data.getSlowness();
				else if(string.equalsIgnoreCase("lightning")) b = data.getLightning();
				else if(string.equalsIgnoreCase("tearing")) b = data.getTearing();
				else if(string.equalsIgnoreCase("value")) b = data.getValue();
				if (b == -1D){
					lore = lore.replaceFirst("%"+lore.split("%")[1]+"%", "变量不正确");
				}
				else {
					lore = lore.replaceFirst("%"+lore.split("%")[1]+"%", df.format(b));
				}
			}
			list.set(i, lore);
		}
		return list;
	}
}
