package github.saukiya.sxstats.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import github.saukiya.sxstats.inventory.StatsInventory;
import github.saukiya.sxstats.util.Message;

/**
 * @author Saukiya
 * @since 2018年3月16日
 */

public class OnInventoryClickListener implements Listener {
	@EventHandler
	void onInventoryClickEvent(InventoryClickEvent event){
		if(event.getInventory().getName().equals(Message.getMsg(Message.INVENTORY_STATS_NAME))){
			event.setCancelled(true);
			if(event.getRawSlot() == 4){
				Player player = (Player) event.getView().getPlayer();
				if(StatsInventory.getHideList().contains(player.getUniqueId())){
					StatsInventory.getHideList().remove(player.getUniqueId());
				}
				else {
					StatsInventory.getHideList().add(player.getUniqueId());
				}
				StatsInventory.openStatsInventory(player);
			}
		}
	}
}
