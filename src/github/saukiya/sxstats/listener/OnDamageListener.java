package github.saukiya.sxstats.listener;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import github.saukiya.sxstats.SXStats;
import github.saukiya.sxstats.data.NameData;
import github.saukiya.sxstats.data.StatsData;
import github.saukiya.sxstats.data.StatsDataManager;
import github.saukiya.sxstats.listener.OnItemDurabilityListener;
import github.saukiya.sxstats.util.Config;
import github.saukiya.sxstats.util.Message;
import lombok.Getter;

/**
 * @author Saukiya
 * @since 2018年3月25日
 */

public class OnDamageListener implements Listener {

	@Getter private static Map<UUID,NameData> nameMap = new HashMap<>();
	@Getter private static Map<UUID,BukkitRunnable> runNameMap = new HashMap<>();
	
	 @Getter private static List<Hologram> hologramsList = new ArrayList<>();
	 
	 @Getter static public HashMap<UUID,BossBar> bossMap = new HashMap<>();
	 static public HashMap<UUID,BukkitRunnable> runBossMap = new HashMap<>();
	
	void sendHolo(String message,Hologram holograms){
		if(message.contains("Null Message: ")) return;
		if(holograms != null){
			holograms.appendTextLine(message);
		}
	}

	@EventHandler
	void onProjectileHitEvent(EntityShootBowEvent event){
		if(event.isCancelled())return;
		Entity projectile = event.getProjectile();
		LivingEntity entity = event.getEntity();
		int level = StatsDataManager.getLevel(entity);
		ItemStack item = event.getBow();
		StatsData data = StatsDataManager.getEntityData(entity,StatsDataManager.getItemData(entity, level, item));
		StatsDataManager.setProjectileData(projectile.getUniqueId(), data);
	}
	
	@EventHandler ( priority = EventPriority.HIGHEST)
	void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event){
		if (event.isCancelled()){
			return;
		}
		double damage = event.getDamage();
		LivingEntity entity = null;// = event.getEntity();
		LivingEntity damager = null;// = event.getDamager();
		StatsData entityData = new StatsData();
		StatsData damagerData = null;
		// 当攻击者为投抛物时，
		if (event.getDamager() instanceof Projectile){
			Projectile arrow = (Projectile) event.getDamager();
			if (arrow.getShooter() instanceof LivingEntity){
				damagerData = StatsDataManager.getProjectileData(arrow.getUniqueId());
				damager = (LivingEntity) arrow.getShooter();
			}else {
				System.out.println("[DEBUG] 但凡事都有例外，攻击者去援交了");
			}
		}
		else if (event.getDamager() instanceof LivingEntity){
			damager = (LivingEntity) event.getDamager();
		}
		if (event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof ArmorStand)){
			entity = (LivingEntity) event.getEntity();
		}
		// 若有一方为null 则取消
		if (entity == null || damager ==null) {
			return;
		}
		// 取消怪v怪的属性计算
		if (Config.isDamageCalculationToEVE() && !(entity instanceof Player || damager instanceof Player)){
			return;
		}
		
		entityData = StatsDataManager.getEntityData(entity);
		if (damagerData == null) {
			damagerData = StatsDataManager.getEntityData(damager);
		}

		// 主手持弓左键判断
		if (event.getCause().equals(DamageCause.ENTITY_ATTACK)){
			EntityEquipment eq = damager.getEquipment();
			ItemStack mainHand = eq.getItemInMainHand();
			if (mainHand.getType().equals(Material.BOW)){
				damagerData = StatsDataManager.getEntityData(damager,new StatsData());
			}
			// 是否为默认工具 并且是否为创造模式
			if (damager instanceof Player && !((HumanEntity) damager).getGameMode().equals(GameMode.CREATIVE) && mainHand.getType().getMaxDurability() == 0){
				// 尝试耐久度扣取
				OnItemDurabilityListener.takeDurability(damager, mainHand, 1, false);
			}
		}
		
		// 处理双方名字
		String entityName = entity.getName();
		if(entity instanceof Player){
			entityName =((Player) entity).getDisplayName();
		}
		else{
			// 获取原来的名字
			NameData nameData = nameMap.get(entity.getUniqueId());
			if(nameData != null){
				if(nameData.getName() != null){
					entityName = Message.replace(nameData.getName());
				}
				else {
					String tempName = entityName;
					entity.setCustomName(null);
					entityName = Message.replace(entity.getName());
					entity.setCustomName(tempName);
				}
			}
			else {
				entityName = Message.replace(entityName);
			}
		}
		String damagerName = damager.getName();
		if(damager instanceof Player){
			damagerName =((Player) damager).getDisplayName();
		}
		else{
			// 获取原来的名字
			NameData nameData = nameMap.get(damager.getUniqueId());
			if(nameData != null){
				if(nameData.getName() != null){
					damagerName = Message.replace(nameData.getName());
				}
				else {
					String tempName = damagerName;
					damager.setCustomName(null);
					damagerName = Message.replace(damager.getName());
					damager.setCustomName(tempName);
				}
			}
			else {
				damagerName = Message.replace(damagerName);
			}
		}
		
		// 全息部分
		Hologram hologram = null ;
		DecimalFormat df = new DecimalFormat("#.#");
		if(Config.isHolographic() && SXStats.isHolographic()){
			Location loc = entity.getEyeLocation().clone().add(0,0.6-new Random().nextDouble()/2,0);
			loc.setYaw(damager.getLocation().getYaw()+90);
			loc.add(loc.getDirection().multiply(0.8D));
			hologram = HologramsAPI.createHologram(SXStats.getPlugin(), loc);
			hologramsList.add(hologram);
			Hologram h = hologram;
			new BukkitRunnable(){
				@Override
				public void run() {
						h.delete();
						hologramsList.remove(h);
				}
			}.runTaskLater(SXStats.getPlugin(), Config.getConfig().getInt(Config.HOLOGRAPHIC_DISPLAY_TIME));
		}
		// 闪避+命中
		if (StatsDataManager.probability(entityData.getDodge()-damagerData.getHitRate())){
			event.setCancelled(true);
			Location loc = damager.getLocation().clone();
			loc.setYaw(loc.getYaw()+new Random().nextInt(80)-40);
			entity.setVelocity(loc.getDirection().setY(0.1).multiply(0.7));
			Message.send(damager, Message.PLAYER_BATTLE_DODGE, entityName,"你");
			Message.send(entity, Message.PLAYER_BATTLE_DODGE,"你", damagerName);
			sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_DODGE), hologram);
			return;
		}
		// 如果不是攻击缓冲模式 或 是抛射物
		if(!Config.isDamageGauges() || event.getDamager() instanceof Projectile){
			// 随机伤害
			damage += damagerData.getDamage();
		}
		else {
			// 随机伤害-最小伤害
			damage += damagerData.getDamage()-damagerData.getMinDamage();
		}
		if (entity instanceof Player){
			damage += damagerData.getPVPDamage();// PVP攻击力
		}
		else {
			damage += damagerData.getPVEDamage();// PVE攻击力
		}
		// 暴击
		Boolean crit = false;
		if (StatsDataManager.probability(damagerData.getCritRate())){
			crit = true;
			damage *= (damagerData.getCritDamage() < 100 ? 100 : damagerData.getCritDamage())/100;
		}
		//非穿甲伤害
		if(!event.getCause().equals(DamageCause.CUSTOM)){
			// 破甲+坚定
			if (!StatsDataManager.probability(damagerData.getReal())){
				// 防御力
				Double defense = entityData.getDefense();
				if (damager instanceof Player){
					defense += entityData.getPVPDefense();
				}
				else {
					defense += entityData.getPVEDefense();
				}
				damage -= defense;
				// 打不动
				if (damage < 0){
					event.setCancelled(true);
					entity.playEffect(EntityEffect.HURT);
					entity.setHealth(entity.getHealth()-1 < 0 ? 0.1D:entity.getHealth()-1);
					 sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_DAMAGE,df.format(1.0D)), hologram);
					return;
				}
				// 反射
				if (StatsDataManager.probability(entityData.getReflectionRate())){
					damager.playEffect(EntityEffect.HURT);
					damager.setHealth(damager.getHealth()-(damage*entityData.getReflection()/100));
					// 反射消息
					Message.send(damager, Message.PLAYER_BATTLE_REFLECTION, "你",entityName);
					Message.send(entity, Message.PLAYER_BATTLE_REFLECTION,damagerName,"你");
					sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_REFLECTION,df.format(entityData.getReflection())), hologram);
				}
				// 格挡
				else if (StatsDataManager.probability(entityData.getBlockRate())){
					damage *= 1-(entityData.getBlock()/100);
					// 格挡消息
					Message.send(damager, Message.PLAYER_BATTLE_BLOCK, entityName,"你");
					Message.send(entity, Message.PLAYER_BATTLE_BLOCK,"你",damagerName);
					sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_BLOCK,df.format(entityData.getBlock())), hologram);
				}
			}else {
				// 破甲消息
				Message.send(damager, Message.PLAYER_BATTLE_REAL,entityName, "你");
				Message.send(entity, Message.PLAYER_BATTLE_REAL,"你",damagerName);
				 sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_REAL), hologram);
			}
		}
		
		// 生命吸取
		if(damagerData.getLifeSteal() > 0){
			 Double maxHealth = damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			 Double lifeHealth = damager.getHealth() + ((damage > entity.getHealth() ? entity.getHealth() : damage)*damagerData.getLifeSteal()/100);
			 damager.setHealth(lifeHealth > maxHealth ? maxHealth : lifeHealth);
			 sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_LIFE_STEAL,df.format(lifeHealth-maxHealth)), hologram);
		}
		// 点燃+韧性
		 if (StatsDataManager.probability(damagerData.getIgnition()-entityData.getToughness())){
			 entity.setFireTicks(40+new Random().nextInt(60));
			 // 点燃消息
			 Message.send(damager, Message.PLAYER_BATTLE_IGNITION,entityName, "你");
			 Message.send(entity, Message.PLAYER_BATTLE_IGNITION,"你",damagerName);
			 sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_IGNITION,df.format(entity.getFireTicks()/20)), hologram);
		 }
		// 凋零+韧性
		 if (StatsDataManager.probability(damagerData.getWither()-entityData.getToughness())){
			 entity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40+new Random().nextInt(60), new Random().nextInt(2)+1));
			 // 凋零消息
			 Message.send(damager, Message.PLAYER_BATTLE_WITHER,entityName, "你");
			 Message.send(entity, Message.PLAYER_BATTLE_WITHER,"你",damagerName);
			 sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_WITHER,df.format(entity.getPotionEffect(PotionEffectType.WITHER).getDuration()/20D)), hologram);
		 }
		// 中毒+韧性
		 if (StatsDataManager.probability(damagerData.getPoison()-entityData.getToughness())){
			 int tick = 40+new Random().nextInt(60);
			 entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, tick, new Random().nextInt(2)+1));
			 // 中毒消息
			 Message.send(damager, Message.PLAYER_BATTLE_POISON,entityName, "你");
			 Message.send(entity, Message.PLAYER_BATTLE_POISON,"你",damagerName);
			 sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_POISON,df.format(tick/20D)), hologram);
		 }
		// 失明+韧性
		 if (StatsDataManager.probability(damagerData.getPoison()-entityData.getToughness())){
			 entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40+new Random().nextInt(60), new Random().nextInt(2)+1));
			 // 失明消息
			 Message.send(damager, Message.PLAYER_BATTLE_BLINDNESS,entityName, "你");
			 Message.send(entity, Message.PLAYER_BATTLE_BLINDNESS,"你",damagerName);
			 sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_BLINDNESS,df.format(entity.getPotionEffect(PotionEffectType.BLINDNESS).getDuration()/20D)), hologram);
		 }
		// 缓慢+韧性
		 if (StatsDataManager.probability(damagerData.getSlowness()-entityData.getToughness())){
			 entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40+new Random().nextInt(60), new Random().nextInt(2)+1));
			 // 缓慢消息
			 Message.send(damager, Message.PLAYER_BATTLE_SLOWNESS,entityName, "你");
			 Message.send(entity, Message.PLAYER_BATTLE_SLOWNESS,"你",damagerName);
			 sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_SLOWNESS,df.format(entity.getPotionEffect(PotionEffectType.SLOW).getDuration()/20D)), hologram);
		 }
		// 雷霆+韧性
		if (StatsDataManager.probability(damagerData.getLightning()-entityData.getToughness())){
			entity.getWorld().strikeLightningEffect(entity.getLocation());
			entity.setHealth(entity.getHealth()*(1-new Random().nextDouble()/10));
			// 雷霆消息
			Message.send(damager, Message.PLAYER_BATTLE_LIGHTNING,entityName, "你");
			Message.send(entity, Message.PLAYER_BATTLE_LIGHTNING,"你",damagerName);
			 sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_LIGHTNING), hologram);
		}
		// 撕裂+韧性
		if (StatsDataManager.probability(damagerData.getTearing()-entityData.getToughness())){
			LivingEntity runnableEntity = entity;
			LivingEntity runnableDamager = damager;
			int size = new Random().nextInt(3)+1; //1-3
			new BukkitRunnable(){
				int i=0;
				@Override
				public void run() {
					i++;
					if (i >= 12/size || runnableEntity.isDead() || event.isCancelled()) cancel();
					runnableEntity.playEffect(EntityEffect.HURT);
					runnableEntity.setHealth(runnableEntity.getHealth()-runnableEntity.getHealth()/100);
					runnableEntity.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, runnableEntity.getEyeLocation().add(0,-1,0), 2,0.2D,0.2D,0.2D,0.1f);
					if(runnableDamager instanceof Player){
						((Player)runnableDamager).playSound(runnableEntity.getEyeLocation(), Sound.valueOf("ENTITY_"+runnableEntity.getType().toString()+"_HURT"), 1, 1);
					}
				}
			}.runTaskTimer(SXStats.getPlugin(),5,size);
			// 撕裂消息
			Message.send(damager, Message.PLAYER_BATTLE_TEARING,entityName, "你");
			Message.send(entity, Message.PLAYER_BATTLE_TEARING,"你",damagerName);
			 sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_TEARING), hologram);
		}
		event.setDamage(damage);
		if(crit){
			Message.send(damager, Message.PLAYER_BATTLE_CRIT, "你",entityName);
			Message.send(entity, Message.PLAYER_BATTLE_CRIT, damagerName,"你");
			sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_CRIT,df.format(event.getFinalDamage())), hologram);
		}
		else{
			 sendHolo(Message.getMsg(Message.PLAYER_HOLOGRAPHIC_DAMAGE,df.format(event.getFinalDamage())), hologram);
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onDamageEvent(EntityDamageByEntityEvent event){
		if(event.isCancelled()){
			return;
		}
		LivingEntity damager = null;
		LivingEntity entity = (LivingEntity) event.getEntity();
		if(event.getDamager() instanceof LivingEntity){
			//攻击者
			damager =  (LivingEntity) event.getDamager();
		}
		else if(event.getDamager() instanceof Projectile){
			Projectile pro = (Projectile) event.getDamager();
			if(pro.getShooter() instanceof LivingEntity){
				//攻击者
				damager = (LivingEntity) pro.getShooter();
			}
		}
		if(damager ==null) return;
		
		// 获取名字
		String name = entity.getName();
		if(entity instanceof Player){
			name =((Player) entity).getDisplayName();
		}
		else{
			// 获取原来的名字
			NameData nameData = nameMap.get(entity.getUniqueId());
			if(nameData != null){
				if(nameData.getName() != null){
					name = Message.replace(nameData.getName());
				}
				else {
					String temp = name;
					entity.setCustomName(null);
					name = Message.replace(entity.getName());
					entity.setCustomName(temp);
				}
			}
			else {
				name = Message.replace(name);
			}
		}
		
		// 获取血量
		double health = entity.getHealth()-event.getFinalDamage();
		if(health<0)health=0;
		double maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
		double progress = health/maxHealth;


		// 处理bossbar
		if(Config.isHealthBossBar() && damager instanceof Player){
			BarColor barColor = null;
			if(progress > 0.66){
				barColor = BarColor.GREEN;
			}else if(progress > 0.33){
				barColor = BarColor.YELLOW;
			}else{
				barColor = BarColor.RED;
			}
			if(runBossMap.containsKey(damager.getUniqueId()))runBossMap.remove(damager.getUniqueId()).cancel();
			DecimalFormat df = new DecimalFormat("#.#");
			if(bossMap.containsKey(damager.getUniqueId()))bossMap.remove(damager.getUniqueId()).removeAll();
			BossBar bossBar = Bukkit.createBossBar(MessageFormat.format(Config.getConfig().getString(Config.HEALTH_BOSSBAR_FORMAT), name,df.format(health),df.format(maxHealth)).replace("&", "§"), barColor, BarStyle.SEGMENTED_20);
			bossBar.setProgress(progress);
			bossBar.addPlayer((Player) damager);
			bossMap.put(damager.getUniqueId(), bossBar);
			BukkitRunnable runnable = new BukkitRunnable(){
				@Override
				public void run() {
					bossBar.removeAll();
				}
			};
			runnable.runTaskLater(SXStats.getPlugin(), Config.getConfig().getInt(Config.HEALTH_BOSSBAR_DISPLAY_TIME));
			runBossMap.put(damager.getUniqueId(), runnable);
		}
		// 头顶血条
		if(Config.isHealthNameVisible()){
			if(entity instanceof Player && health == 0){
				NameData nameData = nameMap.remove(damager.getUniqueId());
				if(nameData != null){
					damager.setCustomName(nameData.getName());
					damager.setCustomNameVisible(nameData.isVisible());
				}
			}
			else if (damager instanceof Player){
				// 处理死亡
				if(health == 0){
					BukkitRunnable run = runNameMap.get(entity.getUniqueId());
					NameData nameData = nameMap.remove(entity.getUniqueId());
					if (run != null){
						run.cancel();
					}
					if(nameData != null){
						entity.setCustomName(nameData.getName());
						entity.setCustomNameVisible(false);
					}
					return;
				}
				// 处理名字
				String healthName = Config.getConfig().getString(Config.HEALTH_NAME_VISIBLE_PREFIX);
				int maxSize = Config.getConfig().getInt(Config.HEALTH_NAME_VISIBLE_SIZE);
				int size = (int) (maxSize*progress);
				String current = Config.getConfig().getString(Config.HEALTH_NAME_VISIBLE_CURRENT);
				String loss = Config.getConfig().getString(Config.HEALTH_NAME_VISIBLE_LOSS);
				for(int i = 0 ; i < size ; i++){
					healthName += current;
				}
				for(int i = size ; i < maxSize; i++){
					healthName += loss;
				}
				healthName = (healthName+Config.getConfig().getString(Config.HEALTH_NAME_VISIBLE_SUFFIX)).replace("&", "§");
				
				// 输出
				String entityName = entity.getCustomName();
				Boolean visible = entity.isCustomNameVisible();
				NameData nameData = nameMap.containsKey(entity.getUniqueId()) ? nameMap.get(entity.getUniqueId()) : new NameData(entityName,visible);
				nameMap.put(entity.getUniqueId(), nameData);
				
				BukkitRunnable run = runNameMap.get(entity.getUniqueId());
				if(run != null){
					run.cancel();
				}
				
				entity.setCustomName(healthName);
				entity.setCustomNameVisible(true);

				BukkitRunnable runnable = new BukkitRunnable(){
					@Override
					public void run() {
						if(!entity.isDead()){
							entity.setCustomName(nameData.getName());
							entity.setCustomNameVisible(nameData.isVisible());
							nameMap.remove(entity.getUniqueId());
							runNameMap.remove(entity.getUniqueId());
						}
					}
				};
				runNameMap.put(entity.getUniqueId(), runnable);
				runnable.runTaskLater(SXStats.getPlugin(), Config.getConfig().getInt(Config.HEALTH_NAME_VISIBLE_DISPLAY_TIME));
			}
		}
	}
}
