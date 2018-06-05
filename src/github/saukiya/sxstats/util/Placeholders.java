package github.saukiya.sxstats.util;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import github.saukiya.sxstats.SXStats;
import github.saukiya.sxstats.data.StatsData;
import github.saukiya.sxstats.data.StatsDataManager;
import me.clip.placeholderapi.external.EZPlaceholderHook;

public class Placeholders  extends EZPlaceholderHook{
	
	@SuppressWarnings("unused")
	private SXStats ourPlugin;

	public Placeholders(SXStats ourPlugin) {
		super(ourPlugin, "sx");
		this.ourPlugin = ourPlugin;
	}

	@Override
	public String onPlaceholderRequest(Player player, String string) {
		StatsData data = StatsDataManager.getEntityData(player);
		DecimalFormat df = new DecimalFormat("#.##");
		String str = null;
		Double b = 0D;
		if(string.equalsIgnoreCase("expAddition")) b = data.getExpAddition();
		else if(string.equalsIgnoreCase("speed")) b = data.getSpeed();
		else if(string.equalsIgnoreCase("health")) b = data.getHealth();
		else if(string.equalsIgnoreCase("healthRegen")) b = data.getHealthRegen();
		else if(string.equalsIgnoreCase("dodge")) b = data.getDodge();
		else if(string.equalsIgnoreCase("minDefense")) b = data.getMinDefense();
		else if(string.equalsIgnoreCase("maxDefense")) b = data.getMaxDefense();
		else if(string.equalsIgnoreCase("defense")){
			if(data.getMinDefense() == data.getMaxDefense()){
				b = data.getMinDefense();
			}
			else {
				str = df.format(data.getMinDefense())+" - "+df.format(data.getMaxDefense());
			}
		}
		else if(string.equalsIgnoreCase("pvpMinDefense")) b = data.getPvpMinDefense();
		else if(string.equalsIgnoreCase("pvpMaxDefense")) b = data.getPvpMaxDefense();
		else if(string.equalsIgnoreCase("pvpDefense")){
			if(data.getPvpMinDefense() == data.getPvpMaxDefense()){
				b = data.getPvpMinDefense();
			}
			else {
				str = df.format(data.getPvpMinDefense())+" - "+df.format(data.getPvpMaxDefense());
			}
		}
		else if(string.equalsIgnoreCase("pveMinDefense")) b = data.getPveMinDefense();
		else if(string.equalsIgnoreCase("pveMaxDefense")) b = data.getPveMaxDefense();
		else if(string.equalsIgnoreCase("pveDefense")){
			if(data.getPveMinDefense() == data.getPveMaxDefense()){
				b = data.getPveMinDefense();
			}
			else {
				str = df.format(data.getPveMinDefense())+" - "+df.format(data.getPveMaxDefense());
			}
		}
		else if(string.equalsIgnoreCase("toughness")) b = data.getToughness();
		else if(string.equalsIgnoreCase("reflectionRate")) b = data.getReflectionRate();
		else if(string.equalsIgnoreCase("reflection")) b = data.getReflection();
		else if(string.equalsIgnoreCase("blockRate")) b = data.getBlockRate();
		else if(string.equalsIgnoreCase("block")) b = data.getBlock();
		else if(string.equalsIgnoreCase("minDamage")) b = data.getMinDamage() == 0 ? 1 : data.getMinDamage();
		else if(string.equalsIgnoreCase("maxDamage")) b = data.getMaxDamage() == 0 ? 1 : data.getMaxDamage();
		else if(string.equalsIgnoreCase("damage")){
			if(data.getMinDamage() == data.getMaxDamage()){
				b = data.getMinDamage() == 0 ? 1 : data.getMaxDamage();
			}
			else {
				str = df.format(data.getMinDamage() == 0 ? 1 : data.getMinDamage())+" - "+df.format(data.getMaxDamage() == 0 ? 1 : data.getMaxDamage());
			}
		}
		else if(string.equalsIgnoreCase("pvpMinDamage")) b = data.getPvpMinDamage();
		else if(string.equalsIgnoreCase("pvpMaxDamage")) b = data.getPvpMaxDamage();
		else if(string.equalsIgnoreCase("pvpDamage")){
			if(data.getPvpMinDamage() == data.getPvpMaxDamage()){
				b = data.getPvpMinDamage();
			}
			else {
				str = df.format(data.getPvpMinDamage())+" - "+df.format(data.getPvpMaxDamage());
			}
		}
		else if(string.equalsIgnoreCase("pveMinDamage")) b = data.getPveMinDamage();
		else if(string.equalsIgnoreCase("pveMaxDamage")) b = data.getPveMaxDamage();
		else if(string.equalsIgnoreCase("pveDamage")){
			if(data.getPveMinDamage() == data.getPveMaxDamage()){
				b = data.getPveMinDamage();
			}
			else {
				str = df.format(data.getPveMinDamage())+" - "+df.format(data.getPveMaxDamage());
			}
		}
		else if(string.equalsIgnoreCase("hitRate")) b = data.getHitRate();
		else if(string.equalsIgnoreCase("real")) b = data.getReal();
		else if(string.equalsIgnoreCase("crit")) b = data.getCritRate();
		else if(string.equalsIgnoreCase("critDamage")) b = data.getCritDamage();
		else if(string.equalsIgnoreCase("lifeSteal")) b = data.getLifeSteal();
		else if(string.equalsIgnoreCase("ignition")) b = data.getIgnition();
		else if(string.equalsIgnoreCase("wither")) b = data.getWither();
		else if(string.equalsIgnoreCase("poison")) b = data.getPoison();
		else if(string.equalsIgnoreCase("blindness")) b = data.getBlindness();
		else if(string.equalsIgnoreCase("slowness")) b = data.getSlowness();
		else if(string.equalsIgnoreCase("lightning")) b = data.getLightning();
		else if(string.equalsIgnoreCase("tearing")) b = data.getTearing();
		else if(string.equalsIgnoreCase("value")) b = data.getValue();
		else return "§c变量名不正确: "+string;
		if(str != null){
			return str;
		}
		return df.format(b);
		
	}

}
