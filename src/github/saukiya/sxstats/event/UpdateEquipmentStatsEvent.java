package github.saukiya.sxstats.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import github.saukiya.sxstats.data.StatsData;
import lombok.Getter;
import lombok.Setter;
/**
 * @author Saukiya
 * @since 2018年5月2日
 */

public class UpdateEquipmentStatsEvent extends Event{

	@Getter LivingEntity entity;
	@Setter @Getter StatsData statsData;
	@Setter @Getter ItemStack[] items;
	
    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public UpdateEquipmentStatsEvent (LivingEntity entity,StatsData statsData,ItemStack... items){
		this.entity = entity;
		this.statsData = statsData;
		this.items = items;
    }

}
