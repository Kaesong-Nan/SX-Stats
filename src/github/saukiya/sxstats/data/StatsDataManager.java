package github.saukiya.sxstats.data;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import github.saukiya.sxstats.SXStats;
import github.saukiya.sxstats.api.SXStatsAPI;
import github.saukiya.sxstats.data.StatsData;
import github.saukiya.sxstats.event.UpdateEquipmentStatsEvent;
import github.saukiya.sxstats.event.UpdateHandStatsEvent;
import github.saukiya.sxstats.util.Config;
import github.saukiya.sxstats.util.ItemUtil;
import github.saukiya.sxstats.util.Message;

public class StatsDataManager {
	// 存储玩家属性
	public static Map<UUID,StatsData> equipmentMap = new HashMap<>();
	public static Map<UUID,StatsData> mainHandMap = new HashMap<>();
	public static Map<UUID,StatsData> rpgInventoryMap = new HashMap<>();
	

	public static void healthRegenRunnable(){
		new BukkitRunnable(){
			@Override
			public void run() {
				try{
					for (Player player:Bukkit.getOnlinePlayers()){
						getEntityData(player);
						StatsData statsData = getEntityData(player);
						if(statsData.getHealthRegen() > 0 && !player.isDead()){
							Double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
							if (player.getHealth() < maxHealth){
								player.setHealth(player.getHealth()+statsData.getHealthRegen() >= maxHealth ? maxHealth : player.getHealth()+statsData.getHealthRegen());
							}
						}
					}
				}
				catch (Exception e){
		        	Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §a生命恢复系统崩溃 正在重新启动!");
					this.cancel();
					healthRegenRunnable();
		        	Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §a启动完毕!");
		        	Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §c如果此消息连续刷屏，请通过Yum重载本插件，并报告原作者QQ: 1940208750!");
				}
			}
		}.runTaskTimer(SXStats.getPlugin(), 20, 20);
	}
	
	// 判断护甲
	public static Double getDefense(double d){
		double defense = 0.000D;
		defense = 150/(150+d);
		return defense;
	}
	
	// 判断几率
	public static Boolean probability(double d){
		return d/100D > new Random().nextDouble();
	}
	
	// 读取抛射物数据
	public static StatsData getProjectileData(UUID uuid){
		StatsData data = new StatsData();
		if (equipmentMap.containsKey(uuid)){
			data.add(equipmentMap.remove(uuid));
		}
		return data;
	}
	
	//设置抛射物的数据
	public static void setProjectileData(UUID uuid,StatsData statsData){
		equipmentMap.put(uuid, statsData);
	}
	
	// 获取生物总数据
	public static StatsData getEntityData(LivingEntity entity,StatsData... statsData){
		StatsData data = new StatsData();
		UUID uuid = entity.getUniqueId();
		if (statsData.length > 0){
			data.add(statsData[0]);
		}
		else {
			data.add(mainHandMap.get(uuid));
		}
		data.add(equipmentMap.get(uuid));
		data.add(rpgInventoryMap.get(uuid));
		data.add(SXStatsAPI.getAPIStats(entity.getUniqueId()));
		// 计算点数
		data.value();
		// 生物默认数据
		data.setHealth(data.getHealth()+20D);
		// 纠正数值
		data.correct();
		if (entity instanceof Player){
			// TODO 设置中给予默认属性
			// 玩家默认数据
			data.setSpeed(data.getSpeed()+100D);
			data.setCritDamage(data.getCritDamage()+100D);
		}
		return data;
	}
	
	// 清除生物数据
	public static void clearEntityData(UUID uuid){
		if (equipmentMap.containsKey(uuid)){
			equipmentMap.remove(uuid);
		}
		if (mainHandMap.containsKey(uuid)){
			mainHandMap.remove(uuid);
		}
		SXStatsAPI.removeEntityAllPluginData(uuid);
	}
	
	// 加载生物装备槽的数据
	public static void loadEquipmentData(LivingEntity entity){
		int level = getLevel(entity);
		// 更新物品
		if (Config.isItemUpdate() && entity instanceof Player){
			for (ItemStack item : entity.getEquipment().getArmorContents()){
				ItemDataManager.updateItem(item);
			}
		}
		ItemStack[] itemList = entity.getEquipment().getArmorContents();
		StatsData statsData = getItemData(entity,level,itemList);// 更新属性 并从 itemList 中删减不符合的物品
		if(statsData.value() <= 0){
			if(equipmentMap.containsKey(entity.getUniqueId())){
				equipmentMap.remove(entity.getUniqueId());
			}
			return;
		}
		Bukkit.getPluginManager().callEvent(new UpdateEquipmentStatsEvent(entity,statsData,itemList)); 
		equipmentMap.put(entity.getUniqueId(), statsData);
	}
	
	// 加载生物手中的数据
	public static void loadHandData(LivingEntity entity){
		int level = getLevel(entity);
		// 更新物品
		if (Config.isItemUpdate() && entity instanceof Player){
			ItemDataManager.updateItem(entity.getEquipment().getItemInMainHand());
			ItemDataManager.updateItem(entity.getEquipment().getItemInOffHand());
		}
		ItemStack mainItem = entity.getEquipment().getItemInMainHand();
		ItemStack offItem = entity.getEquipment().getItemInOffHand();
		// 判断主手 手持
		if(mainItem != null && mainItem.hasItemMeta() && mainItem.getItemMeta().hasLore()){
			for (String lore : mainItem.getItemMeta().getLore()){
				if (getText(lore).contains(Config.getConfig().getString(Config.NAME_HAND_OFF))){
					Message.send(entity, Message.PLAYER_NO_HAND, mainItem.getItemMeta().getDisplayName() , getText(lore));
					mainItem = null;
					break;
				}
			}
		}
		// 判断副手 手持
		if(offItem != null && offItem.hasItemMeta() && offItem.getItemMeta().hasLore()){
			for (String lore : offItem.getItemMeta().getLore()){
				if (getText(lore).contains(Config.getConfig().getString(Config.NAME_HAND_MAIN))){
					Message.send(entity, Message.PLAYER_NO_HAND, offItem.getItemMeta().getDisplayName() , getText(lore));
					offItem = null;
					break;
				}
			}
		}
		StatsData statsData = getItemData(entity,level,offItem,mainItem);
		if(statsData.value() <= 0){
			if(mainHandMap.containsKey(entity.getUniqueId())){
				mainHandMap.remove(entity.getUniqueId());
			}
			return;
		}
		Bukkit.getPluginManager().callEvent(new UpdateHandStatsEvent(entity,statsData,offItem,mainItem)); 
		mainHandMap.put(entity.getUniqueId(), statsData);
	}
	
	// 清除与数字有关的颜色
	public static String clearColor(String lore){
		List<String> ColorList = new ArrayList<String>();
		for (int i=0;i<10;i++){
			ColorList.add("§"+i);
		}
		for (int i=0;i<ColorList.size();i++){
			lore = lore.replace(ColorList.get(i), "");
		}
		return lore;
	}
	
	//判断物品是否符合条件 true为主手 false为副手 -- API专用
	public static Boolean isUse(LivingEntity entity,ItemStack item,Boolean... hand){
		if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()){
			List<String> loreList = item.getItemMeta().getLore();
			for(String lore : loreList){
				if (lore.contains(Config.getConfig().getString(Config.NAME_LIMIT_LEVEL))){
					// 等级不够 返回否
					if (entity == null || Integer.valueOf(getStats(lore))>getLevel(entity)){
						return false;
					}
				}
				else if(lore.contains(Config.getConfig().getString(Config.NAME_ROLE))){
					// 不符合职业 返回否
					if (entity == null || !entity.hasPermission(SXStats.getPlugin().getName()+"."+getText(lore))){
						return false;
					}
				}
				// 不符合手持判断 返回否
				if(hand.length > 0){
					if(hand[0]){
						if(getText(lore).contains(Config.getConfig().getString(Config.NAME_HAND_OFF))){
							return false;
						}
					}
					else {
						if(getText(lore).contains(Config.getConfig().getString(Config.NAME_HAND_MAIN))){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	// 获取物品的属性
	public static StatsData getItemData(LivingEntity entity,int level,ItemStack... itemList){
		StatsData dataList = new StatsData();
		for (int i = 0 ; i< itemList.length ; i++){
			ItemStack item = itemList[i];
			if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()){
				StatsData data = new StatsData();
				String itemName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name();
				List<String> loreList = item.getItemMeta().getLore();
				for (String lore:loreList){
					// 如果出现§X 则跳过该行识别
					if (lore.contains("§X") || !(lore.contains(":") || lore.contains("："))){
						continue;
					}// 循环每条lore是否有所需属性
					if (lore.contains(Config.getConfig().getString(Config.NAME_LIMIT_LEVEL))){
						// 等级不够，给予空的数据
						if (entity != null && entity instanceof Player && Integer.valueOf(getStats(lore))>level){
							Message.send(entity, Message.PLAYER_NO_LEVEL_USE, itemName);
							data = new StatsData();
							itemList[i] = null;
							break;
						}
					}
					else if(lore.contains(Config.getConfig().getString(Config.NAME_ROLE))){
						// 不符合职业，给予空的数据
						if (entity != null && entity instanceof Player && !entity.hasPermission(SXStats.getPlugin().getName()+"."+getText(lore))){
							Message.send(entity, Message.PLAYER_NO_ROLE, itemName);
							data = new StatsData();
							itemList[i] = null;
							break;
						}
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_EXP_ADDITION))){
						data.setExpAddition(data.getExpAddition() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_ATTACKSPEED))){
						ItemUtil.setAttackSpeed(item, Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_SPEED))){
						data.setSpeed(data.getSpeed() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_HEALTH)) && !lore.contains(Config.getConfig().getString(Config.NAME_LIFE_STEAL))){
						data.setHealth(data.getHealth() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_HEALTH_REGEN))){
						data.setHealthRegen(data.getHealthRegen() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_PVP_DEFENSE))){
						if (lore.contains("-")){
							data.setPvpMinDefense(data.getPvpMinDefense() + Double.valueOf(getStats(lore.split("-")[0])));
							data.setPvpMaxDefense(data.getPvpMaxDefense() + Double.valueOf(getStats(lore.split("-")[1])));
						}
						else{
							data.setPvpMinDefense(data.getPvpMinDefense() + Double.valueOf(getStats(lore)));
							data.setPvpMaxDefense(data.getPvpMaxDefense() + Double.valueOf(getStats(lore)));
						}
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_PVE_DEFENSE))){
						if (lore.contains("-")){
							data.setPveMinDefense(data.getPveMinDefense() + Double.valueOf(getStats(lore.split("-")[0])));
							data.setPveMaxDefense(data.getPveMaxDefense() + Double.valueOf(getStats(lore.split("-")[1])));
						}
						else{
							data.setPveMinDefense(data.getPveMinDefense() + Double.valueOf(getStats(lore)));
							data.setPveMaxDefense(data.getPveMaxDefense() + Double.valueOf(getStats(lore)));
						}
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_DEFENSE))){
						if (lore.contains("-")){
							data.setMinDefense(data.getMinDefense() + Double.valueOf(getStats(lore.split("-")[0])));
							data.setMaxDefense(data.getMaxDefense() + Double.valueOf(getStats(lore.split("-")[1])));
						}
						else{
							data.setMinDefense(data.getMinDefense() + Double.valueOf(getStats(lore)));
							data.setMaxDefense(data.getMaxDefense() + Double.valueOf(getStats(lore)));
						}
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_DODGE))){
						data.setDodge(data.getDodge() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_TOUGHNESS))){
						data.setToughness(data.getToughness() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_REFLECTION_RATE))){
						data.setReflectionRate(data.getReflectionRate() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_REFLECTION))){
						data.setReflection(data.getReflection() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_BLOCK_RATE))){
						data.setBlockRate(data.getBlockRate() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_BLOCK))){
						data.setBlock(data.getBlock() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_PVP_DAMAGE))){
						if (lore.contains("-")){
							data.setPvpMinDamage(data.getPvpMinDamage() + Double.valueOf(getStats(lore.split("-")[0])));
							data.setPvpMaxDamage(data.getPvpMaxDamage() + Double.valueOf(getStats(lore.split("-")[1])));
						}
						else{
							data.setPvpMinDamage(data.getPvpMinDamage() + Double.valueOf(getStats(lore)));
							data.setPvpMaxDamage(data.getPvpMaxDamage() + Double.valueOf(getStats(lore)));
						}
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_PVE_DAMAGE))){
						if (lore.contains("-")){
							data.setPveMinDamage(data.getPveMinDamage() + Double.valueOf(getStats(lore.split("-")[0])));
							data.setPveMaxDamage(data.getPveMaxDamage() + Double.valueOf(getStats(lore.split("-")[1])));
						}
						else{
							data.setPveMinDamage(data.getPveMinDamage() + Double.valueOf(getStats(lore)));
							data.setPveMaxDamage(data.getPveMaxDamage() + Double.valueOf(getStats(lore)));
						}
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_DAMAGE))){
						if (lore.contains("-")){
							data.setMinDamage(data.getMinDamage() + Double.valueOf(getStats(lore.split("-")[0])));
							data.setMaxDamage(data.getMaxDamage() + Double.valueOf(getStats(lore.split("-")[1])));
						}
						else{
							data.setMinDamage(data.getMinDamage() + Double.valueOf(getStats(lore)));
							data.setMaxDamage(data.getMaxDamage() + Double.valueOf(getStats(lore)));
						}
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_HIT_RATE))){
						data.setHitRate(data.getHitRate() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_REAL))){
						data.setReal(data.getReal() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_CRIT))){
						data.setCritRate(data.getCritRate() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_CRIT_DAMAGE))){
						if(lore.contains("x")){
							data.setCritDamage(data.getCritDamage() + (Double.valueOf(getStats(lore))*100D));
						}else{
							data.setCritDamage(data.getCritDamage() + Double.valueOf(getStats(lore)));
						}
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_LIFE_STEAL))){
						data.setLifeSteal(data.getLifeSteal() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_IGNITION))){
						data.setIgnition(data.getIgnition() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_WITHER))){
						data.setWither(data.getWither() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_POISON))){
						data.setPoison(data.getPoison() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_BLINDNESS))){
						data.setBlindness(data.getBlindness() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_SLOWNESS))){
						data.setSlowness(data.getSlowness() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_LIGHTNING))){
						data.setLightning(data.getLightning() + Double.valueOf(getStats(lore)));
					}
					else if (lore.contains(Config.getConfig().getString(Config.NAME_TEARING))){
						data.setTearing(data.getTearing() + Double.valueOf(getStats(lore)));
					}
				}
				dataList.add(data);
			}
		}
		return dataList;
	}
	
	// 获取生物等级
	public static int getLevel(LivingEntity entity){
		int level = 0;
		if (entity != null && entity instanceof Player){
			level = ((Player) entity).getLevel();
		}else {
			level = 10000;
		}
		return level;
	}
	
	// getlore内的属性值
	public static String getStats(String lore){
		String str = clearColor(lore).replaceAll("[^-0-9.]", "");
		if(str.length() == 0){
			str = "0";
		}
		return str;
	}
	
	// 获取lore内的中文 (职业)
	public static String getText(String lore){
		lore = clearColor(lore);
		String str = lore.replaceAll("[^\u0391-\uFFE5]", "");
		if (lore.contains(":") || lore.contains("：")){
			str = lore.replace("：", "/").replace(":", "/").split("/")[1].replaceAll("[^\u0391-\uFFE5]", "");
		}
		return str;
	}

}
