package github.saukiya.sxstats.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;

/**
 * @author Saukiya
 * @since 2018年6月4日
 */

public class ItemData {
	@Getter private ItemStack item;
	
	@Getter private List<String> ids = new ArrayList<>();

	public ItemData(ItemStack item ,List<String> ids){
		this.item = item;
		this.ids = ids;
	}
	
}
