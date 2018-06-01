package github.saukiya.sxstats.listener;

import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import github.saukiya.sxstats.data.ItemDataManager;
import github.saukiya.sxstats.data.StatsDataManager;
import github.saukiya.sxstats.util.Config;

public class OnItemDurabilityListener implements Listener {
	// 获取当前耐久值
	public static int getDurability (String lore){
		int durability = 0;
		if(lore.contains("/")){
			durability = Integer.valueOf(Pattern.compile("[^0-9]").matcher(StatsDataManager.clearColor(lore).split("/")[0]).replaceAll("").trim());
		}
		return durability;
	}
	// 获取最大耐久值
	public static int getMaxDurability (String lore){
		int maxDurability = 0;
		if(lore.contains("/")){
			maxDurability = Integer.valueOf(Pattern.compile("[^0-9]").matcher(StatsDataManager.clearColor(lore).split("/")[1]).replaceAll("").trim());
		}
		return maxDurability;
	}
	public static Boolean takeDurability(LivingEntity entity,ItemStack item,int takeDurability,Boolean strip){
		if (item.hasItemMeta() && item.getItemMeta().hasLore()){
			ItemMeta meta = item.getItemMeta();
			List<String> loreList = meta.getLore();
			takeDurability = item.getType().toString().contains("_") && "SPADE|PICKAXE|AXE|HDE".contains(item.getType().toString().split("_")[1]) ? 1: takeDurability;
			for (int i = 0 ; i < loreList.size() ; i++){
				String lore = loreList.get(i);
				if (lore.contains(Config.getConfig().getString(Config.NAME_DURABILITY))){
					// 扣取耐久值 设定耐久条耐久
					int durability = getDurability(lore)-takeDurability;
					int maxDurability = getMaxDurability(lore);
					// 扣取耐久时免疫颜色代码
					lore = ItemDataManager.replaceColor(ItemDataManager.clearColor(lore).replaceFirst(String.valueOf(getDurability(lore)), String.valueOf(durability)));
					loreList.set(i, lore);
					meta.setLore(loreList);
					item.setItemMeta(meta);
					// 禁止修复
					Repairable repairable = (Repairable) meta;
					if(repairable.getRepairCost() != 999){
						repairable.setRepairCost(999);
						item.setItemMeta((ItemMeta) repairable);
					}
					// 物品是否消失
					if (durability <= 0){
						// TODO 当耐久为0时物品是否消失 并取消属性
						item.setAmount(0);
						// 重新加载装备属性
						if (entity instanceof Player){
							OnUpdateStatsListener.loadEquipmentData((Player) entity);
						}
						// 跳过耐久条设定
						return true;
					}
					// 设定耐久条
					if(strip){
						int maxDefaultDurability = item.getType().getMaxDurability();
						int defaultDurability = (int) ((Double.valueOf(durability)/maxDurability)*maxDefaultDurability);
						item.setDurability((short) (maxDefaultDurability - defaultDurability));
					}
					return true;
				}
			}
		}
		return false;
		
	}
	
	@EventHandler
	void onItemDurabilityEvent(PlayerItemDamageEvent event){
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		// 如果是 取消原版事件
		if(takeDurability(player,item,event.getDamage(),true)){
			event.setCancelled(true);
		}
	}

}
