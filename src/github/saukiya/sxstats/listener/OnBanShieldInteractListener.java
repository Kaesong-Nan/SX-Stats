package github.saukiya.sxstats.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import github.saukiya.sxstats.util.Config;

/**
 * @author Saukiya
 * @since 2018年5月3日
 */

public class OnBanShieldInteractListener implements Listener {

	@EventHandler
	void onPlayerClickEvent(PlayerInteractEvent event){
		if(Config.isBanShieldInsteract() && event.getItem() != null && event.getItem().getType().equals(Material.SHIELD)){
			event.setCancelled(true);
		}
	}

}
