package github.saukiya.sxstats.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import github.saukiya.sxstats.data.StatsData;
import github.saukiya.sxstats.data.StatsDataManager;

/**
 * @author Saukiya
 * @since 2018年3月25日
 */

public class SXStatsAPI {
	
	public static Map<UUID,Map<Class<?>,StatsData>> apiMap = new HashMap<>();
	
	/**
	 * 获取实体总API属性数据
	 * 
	 * @param uuid
	 * @return 来自所有附属插件提供的 StatsData
	 */
	public static StatsData getAPIStats(UUID uuid){
		StatsData statsData = new StatsData();
		if(apiMap.containsKey(uuid)){
			for(Class<?> c :apiMap.get(uuid).keySet()){
				statsData.add(apiMap.get(uuid).get(c));
			}
		}
		return statsData;
	}
	
	/**
	 * 获取实体属性数据
	 * 
	 * @param uuid
	 * @return 实体当前的总 StatsData
	 */
	public static StatsData getEntityAllData(LivingEntity livingEntity){
		return StatsDataManager.getEntityData(livingEntity);
	}
	
	/**
	 * 获取实体与插件关联的属性数据
	 * 
	 * @param class
	 * @param uuid
	 * @return 与Class关联的 StatsData 没有则返回null
	 */
	public static StatsData getEntityAPIData(Class<?> c,UUID uuid){
		return apiMap.containsKey(uuid)  ? apiMap.get(uuid).get(c) : null;
	}
	
	/**
	 * 判断插件是否有注册该实体的属性
	 * 
	 * @param class
	 * @param uuid
	 * @return 注册了为true 没注册为false
	 */
	public static Boolean isEntityAPIData(Class<?> c,UUID uuid){
		return apiMap.containsKey(uuid) && apiMap.get(uuid).containsKey(c) ? true : false;
	}
	
	/**
	 * 设置插件关联的实体属性数据
	 * 
	 * @param class
	 * @param uuid
	 * @param statsData
	 */
	public static void setEntityAPIData(Class<?> c,UUID uuid,StatsData statsData){
		Map<Class<?>,StatsData> statsMap = new HashMap<>();
		if (apiMap.containsKey(uuid)) {
			statsMap = apiMap.get(uuid);
		}
		else {
			apiMap.put(uuid, statsMap);
		}
		statsMap.put( c , statsData);
	}
	
	/**
	 * 清除插件关联的实体属性数据
	 * 
	 * @param c
	 * @param uuid
	 * @return 清除前的数据 没有返回null
	 */
	public static StatsData removeEntityAPIData(Class<?> c,UUID uuid){
		StatsData statsData = null;
		if(apiMap.containsKey(uuid) && apiMap.get(uuid).containsKey(c)){
			statsData = apiMap.get(uuid).remove(c);
		}
		return statsData;
	}
	
	/**
	 * 清除 插件 关联的 所有实体 属性数据
	 * 
	 * @param c
	 */
	public static void removePluginAllEntityData(Class<?> c){
		for(Map<Class<?>,StatsData> statsMap : apiMap.values()){
			if(statsMap.containsKey(c)){
				statsMap.remove(c);
			}
		}
	}

	/**
	 * 清除插件所有关联的实体属性数据
	 * 
	 * @param uuid
	 */
	public static void removeEntityAllPluginData(UUID uuid){
		if(apiMap.containsKey(uuid)){
			apiMap.remove(uuid);
		}
	}
	
	/**
	 * 获取物品的StatsData数据
	 * 
	 * @param livingEntity 可以为null 
	 * @param item 可以为多个
	 * @return 总 StatsData
	 */
	public static StatsData getItemData(LivingEntity livingEntity, ItemStack... item){
		return StatsDataManager.getItemData(livingEntity, StatsDataManager.getLevel(livingEntity), item);
	}
	
	// 
	/**
	 * 判断玩家等级、职业是否达到要求
	 * 
	 * @param player
	 * @param item
	 * @param hand 主手为true 副手为false 可不填
	 * @return 达到则返回true 未达成则返回false
	 */
	public static Boolean isUse(Player player,ItemStack item,Boolean... hand){
		return StatsDataManager.isUse(player, item, hand);
	}
}
