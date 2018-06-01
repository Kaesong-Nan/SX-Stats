package github.saukiya.sxstats.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import github.saukiya.sxstats.data.StatsDataManager;
import github.saukiya.sxstats.util.Message;

/**
 * @author Saukiya
 * @since 2018年3月25日
 */

public class OnExpChangeListener implements Listener {
	@EventHandler
	void onExpChangeEvent(PlayerExpChangeEvent event){
		Player player = event.getPlayer();
		Double expAddition = StatsDataManager.getEntityData(player).getExpAddition();
		if(event.getAmount() > 0 && expAddition >0){
			int exp = (int) (event.getAmount()*expAddition/100);
			Message.send(player, Message.PLAYER_EXP_ADDITION, exp,expAddition);
			event.setAmount(exp+event.getAmount());
		}
	}
}
