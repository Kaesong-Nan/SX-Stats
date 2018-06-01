package github.saukiya.sxstats.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import github.saukiya.sxstats.SXStats;
import github.saukiya.sxstats.data.StatsData;
import github.saukiya.sxstats.data.StatsDataManager;
import github.saukiya.sxstats.util.Config;
import github.saukiya.sxstats.util.ItemUtil;
import ru.endlesscode.rpginventory.api.InventoryAPI;

// 处理数据更新事件
public class OnUpdateStatsListener implements Listener {
	// 更新玩家血量、移动速度、血量压缩值
	static void updateStats(Player player){
		new BukkitRunnable(){
			@Override
			public void run() {
				if(Config.isClearDefaultAttributeAll()){
					ItemUtil.clearAttribute(player);
				}
				else if (Config.isClearDefaultAttributeReset()){
					ItemUtil.removeAttribute(player);
				}
				StatsData data = StatsDataManager.getEntityData(player);
				player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(data.getHealth());
				if(Config.isDamageGauges()){
					player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(data.getMinDamage());
				}
				else if(data.getDamage() == 0D){
					player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
				}
				else {
					player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0.1);
				}
				if(player.getHealth() > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()){
					player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				}
				player.setWalkSpeed((float) (data.getSpeed()/500D));
				if(Config.isHealthScaled()){
					int healthScale = Config.getConfig().getInt(Config.HEALTH_SCALED_VALUE);
					if(healthScale > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()){
						player.setHealthScaled(false);
					}else{
						player.setHealthScaled(true);
						player.setHealthScale(Config.getConfig().getInt(Config.HEALTH_SCALED_VALUE));
					}
				}
			}
		}.runTask(SXStats.getPlugin());
	}
	
	// 更新手中的物品
	void loadMainData(Player player,ItemStack... itemList){
		if (itemList.length>0){
			int i=0;
			for (ItemStack item: itemList){
				if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()){
					i++;
				}
			}
			if (i==itemList.length)return;
		}
		new BukkitRunnable(){
			@Override
			public void run() {
				StatsDataManager.loadHandData(player);//加载玩家手持
				updateStats(player);
			}
		}.runTaskLaterAsynchronously(SXStats.getPlugin(), 5);
	}
	
	// 更新装备栏物品、更新手中的物品
	static void loadEquipmentData(Player player){
		new BukkitRunnable(){
			@Override
			public void run() {
				StatsDataManager.loadEquipmentData(player);//加载玩家装备
				StatsDataManager.loadHandData(player);//加载玩家双持
				updateStats(player);
			}
		}.runTaskLaterAsynchronously(SXStats.getPlugin(), 5);
	}
	
	// 切换物品栏
	@EventHandler
	void onPlayerItemHeldEvent(PlayerItemHeldEvent event){
		if(event.isCancelled()){
			return;
		}
		Player player = event.getPlayer();
		Inventory inv = player.getInventory();
		ItemStack oldItem = inv.getItem(event.getPreviousSlot());
		ItemStack newItem = inv.getItem(event.getNewSlot());
		loadMainData(player,oldItem,newItem);
	}

	// 切换主副手
	@EventHandler
	void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event){
		if(event.isCancelled()){
			return;
		}
		Player player = event.getPlayer();
		ItemStack oldItem = event.getMainHandItem();
		ItemStack newItem = event.getOffHandItem();
		loadMainData(player,oldItem,newItem);
	}
	
	// 关闭背包
	@EventHandler
	void onInventoryCloseEvent(InventoryCloseEvent event){
		Player player = (Player) event.getPlayer();
		Inventory inv = event.getInventory();
		
		if (SXStats.isRpgInventory()){
			if(InventoryAPI.isRPGInventory(inv)){
				List<ItemStack> itemList = new ArrayList<>();
				if(Config.isRpgInventoryActiveItems()){
					itemList.addAll(InventoryAPI.getActiveItems(player));
				}
				if(Config.isRpgInventoryPassiveItems()){
					itemList.addAll(InventoryAPI.getPassiveItems(player));
				}
				if(itemList.size() == 0){
					return;
				}
				StatsData data = StatsDataManager.getItemData(player, StatsDataManager.getLevel(player), itemList.toArray(new ItemStack[itemList.size()]));
				if(data.value() > 0){
					StatsDataManager.rpgInventoryMap.put(player.getUniqueId(), data);
				}
			}
			return;
		}
		if (inv.getName().contains("container") || inv.getName().contains("Repair")){
			loadEquipmentData(player);
		}
	}
	
	// 拾取物品
	@EventHandler
	void onPlayerDropEvent(PlayerDropItemEvent event){
		if(event.isCancelled()){
			return;
		}
		Player player = event.getPlayer();
		ItemStack item = event.getItemDrop().getItemStack();
		loadMainData(player,item);
	}
	
	// 丢弃物品
	@EventHandler
	void onPlayerPickupItemEvent(PlayerPickupItemEvent event){
		if(event.isCancelled()){
			return;
		}
		Player player = event.getPlayer();
		ItemStack item = event.getItem().getItemStack();
		loadMainData(player,item);
	}
	
	// 点击事件
	@EventHandler
	void onPlayerInteractEvent(PlayerInteractEvent event){
		if(event.isCancelled()){
			return;
		}
		Player player = event.getPlayer();
		if ((event.getAction()+"").contains("RIGHT"))
			if (event.getItem() != null){
				String name = event.getItem().getType().toString();
				if (name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS")){
					loadEquipmentData(player);
				}
			}
	}
	
	// 进入服务器
	@EventHandler
	void onPlayerJoinEvent(PlayerJoinEvent event){
		Player player = event.getPlayer();
		loadEquipmentData(player);
	}

	// 退出服务器
	@EventHandler
	void onPlayerQuitEvent(PlayerQuitEvent event){
		Player player = event.getPlayer();
		StatsDataManager.clearEntityData(player.getUniqueId());
	}

	// 怪物生成事件
	@EventHandler
	void onEntitySpawnEvent(CreatureSpawnEvent event){
		if(event.isCancelled()){
			return;
		}
		LivingEntity entity = event.getEntity();
		new BukkitRunnable(){
			@Override
			public void run() {
				StatsDataManager.loadHandData(entity);
				StatsDataManager.loadEquipmentData(entity);
			}
		}.runTaskLaterAsynchronously(SXStats.getPlugin(), 10);
	}
	// 怪物死亡事件
	@EventHandler
	void onEntityDeathEvent(EntityDeathEvent event){
		if (!(event.getEntity() instanceof Player)){
			StatsDataManager.clearEntityData(event.getEntity().getUniqueId());
		}
	}
}
