package github.saukiya.sxstats.listener;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import github.saukiya.sxstats.SXStats;
import github.saukiya.sxstats.data.ItemDataManager;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

/**
 * @author Saukiya
 * @since 2018年5月2日
 */

public class OnDropListener implements Listener{

	@EventHandler
	void onClickStartGUI(MythicMobDeathEvent  event){
		MythicMob mm = event.getMobType();
		List<String> dropList = mm.getDrops();
		List<ItemStack> drops = event.getDrops();
		for(String str : dropList){
			if(str.contains(" ")  && str.split(" ").length > 1){
				String[] args = str.split(" ");
				int amount = 1;
				if(args.length > 3 && new Random().nextDouble() > Double.valueOf(args[3].replaceAll("[^0-9.]", ""))){// 几率判断
					continue;
				}
				if (args.length > 2){// 数量判断
					if(args[2].contains("-") && args[2].split("-").length > 1){
						int i1 = Integer.valueOf(args[2].split("-")[0].replaceAll("[^0-9]", ""));
						int i2 = Integer.valueOf(args[2].split("-")[1].replaceAll("[^0-9]", ""));
						if(i1 > i2){
							Bukkit.getConsoleSender().sendMessage("[WhiteItem] §c随机数大小不正确!: "+str);
						}
						else {
							amount = new Random().nextInt(i2-i1+1)+i1;
						}
					}
					else {
						amount = Integer.valueOf(args[2].replaceAll("[^0-9]", ""));
					}
				}
				if(args[0].equalsIgnoreCase("sx")){
					
					ItemStack item = null;
					if (event.getKiller() instanceof Player){
						item = ItemDataManager.getItem(args[1],(Player) event.getKiller());
					}
					else {
						item = ItemDataManager.getItem(args[1]);
					}
					if(item != null){
						item.setAmount(amount);
						drops.add(item.clone());
					}
					else {
						Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §cMythicmobs怪物: "+mm.getDisplayName()+"§c 不存在这个掉落物品: "+args[1]);
					}
				}
			}
		}
	}
}
