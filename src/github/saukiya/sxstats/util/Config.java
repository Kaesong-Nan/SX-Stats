package github.saukiya.sxstats.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import github.saukiya.sxstats.SXStats;
import lombok.Getter;

public class Config {
	@Getter private static YamlConfiguration config;
	final static File configFile = new File("plugins" + File.separator + SXStats.getPlugin().getName() + File.separator + "Config.yml");


	final public static String ITEM_UPDATE_ENABLED = "ItemUpdate.Enabled";
	final public static String HOLOGRAPHIC_ENABLED = "Holographic.Enabled";
	final public static String HOLOGRAPHIC_TICK = "Holographic.Tick";
	final public static String HEALTH_NAME_VISIBLE_ENABLED = "Health.NameVisible.Enabled";
	final public static String HEALTH_NAME_VISIBLE_DISPLAY_TIME = "Health.NameVisible.DisplayTime";
	final public static String HEALTH_NAME_VISIBLE_SIZE = "Health.NameVisible.Size";
	final public static String HEALTH_NAME_VISIBLE_CURRENT = "Health.NameVisible.Current";
	final public static String HEALTH_NAME_VISIBLE_LOSS = "Health.NameVisible.Loss";
	final public static String HEALTH_NAME_VISIBLE_PREFIX = "Health.NameVisible.Prefix";
	final public static String HEALTH_NAME_VISIBLE_SUFFIX = "Health.NameVisible.Suffix";
	final public static String HEALTH_BOSSBAR_ENABLED = "Health.BossBar.Enabled";
	final public static String HEALTH_BOSSBAR_FORMAT = "Health.BossBar.Format";
	final public static String HEALTH_BOSSBAR_DISPLAY_TIME = "Health.BossBar.DisplayTime";
	final public static String HEALTH_SCALED_ENABLED = "HealthScaled.Enabled";
	final public static String HEALTH_SCALED_VALUE = "HealthScaled.Value";
	final public static String ITEM_DISPLAY_NAME = "ItemDisplayName";
	final public static String DAMAGE_CALCULATION_TO_EVE = "DamageCalculationToEVE";
	final public static String DAMAGE_GAUGES = "DamageGauges";
	final public static String BAN_SHIELD_INTERACT = "BanShieldInteract";
	final public static String CLEAR_DEFAULT_ATTRIBUTE_THIS_PLUGIN = "ClearDefaultAttribute.ThisPlugin";
	final public static String CLEAR_DEFAULT_ATTRIBUTE_ALL = "ClearDefaultAttribute.All";
	final public static String CLEAR_DEFAULT_ATTRIBUTE_RESET = "ClearDefaultAttribute.Reset";
	final public static String RPG_INVENTORY_ACTIVE_ITEMS = "RPGInventory.ActiveItems";
	final public static String RPG_INVENTORY_PASSIVE_ITEMS = "RPGInventory.PassiveItems";
	final public static String RANDOM_STRING_NAME = "RandomString.Name";
	final public static String RANDOM_STRING_LORE = "RandomString.Lore";

	final public static String NAME_HAND_MAIN = "Stats.Hand.InMain.Name";//
	final public static String NAME_HAND_OFF = "Stats.Hand.InOff.Name";//
	final public static String NAME_ROLE = "Stats.Role.Name";//
	final public static String NAME_LIMIT_LEVEL = "Stats.LimitLevel.Name";//
	final public static String NAME_DURABILITY = "Stats.Durability.Name";//
	final public static String NAME_EXP_ADDITION = "Stats.ExpAddition.Name";
	final public static String NAME_SPEED = "Stats.Speed.Name";
	final public static String NAME_ATTACKSPEED = "Stats.AttackSpeed.Name";
	
	final public static String NAME_HEALTH = "Stats.Health.Name";
	final public static String NAME_HEALTH_REGEN = "Stats.HealthRegen.Name";
	final public static String NAME_DODGE = "Stats.Dodge.Name";
	final public static String NAME_DEFENSE = "Stats.Defense.Name";
	final public static String NAME_PVP_DEFENSE = "Stats.PVPDefense.Name";
	final public static String NAME_PVE_DEFENSE = "Stats.PVEDefense.Name";
	final public static String NAME_TOUGHNESS = "Stats.Toughness.Name";
	final public static String NAME_REFLECTION_RATE = "Stats.ReflectionRate.Name";
	final public static String NAME_REFLECTION = "Stats.Reflection.Name";
	final public static String NAME_BLOCK_RATE = "Stats.BlockRate.Name";
	final public static String NAME_BLOCK = "Stats.Block.Name";

	final public static String NAME_DAMAGE = "Stats.Damage.Name";
	final public static String NAME_PVE_DAMAGE = "Stats.PVPDamage.Name";
	final public static String NAME_PVP_DAMAGE = "Stats.PVEDamage.Name";
	final public static String NAME_HIT_RATE = "Stats.HitRate.Name";
	final public static String NAME_REAL = "Stats.Real.Name";
	final public static String NAME_CRIT = "Stats.Crit.Name";
	final public static String NAME_CRIT_DAMAGE = "Stats.CritDamage.Name";
	final public static String NAME_LIFE_STEAL = "Stats.LifeSteal.Name";
	final public static String NAME_IGNITION = "Stats.Ignition.Name";
	final public static String NAME_WITHER = "Stats.Wither.Name";
	final public static String NAME_POISON = "Stats.Poison.Name";
	final public static String NAME_BLINDNESS = "Stats.Blindness.Name";
	final public static String NAME_SLOWNESS = "Stats.Slowness.Name";
	final public static String NAME_LIGHTNING = "Stats.Lightning.Name";
	final public static String NAME_TEARING = "Stats.Tearing.Name";

	final public static String VALUE_EXP_ADDITION = "Stats.ExpAddition.Value";
	final public static String VALUE_SPEED = "Stats.Speed.Value";
	
	final public static String VALUE_HEALTH = "Stats.Health.Value";
	final public static String VALUE_HEALTH_REGEN = "Stats.HealthRegen.Value";
	final public static String VALUE_DODGE = "Stats.Dodge.Value";
	final public static String VALUE_DEFENSE = "Stats.Defense.Value";
	final public static String VALUE_PVP_DEFENSE = "Stats.PVPDefense.Value";
	final public static String VALUE_PVE_DEFENSE = "Stats.PVEDefense.Value";
	final public static String VALUE_TOUGHNESS = "Stats.Toughness.Value";
	final public static String VALUE_REFLECTION_RATE = "Stats.ReflectionRate.Value";
	final public static String VALUE_REFLECTION = "Stats.Reflection.Value";
	final public static String VALUE_BLOCK_RATE = "Stats.BlockRate.Value";
	final public static String VALUE_BLOCK = "Stats.Block.Value";

	final public static String VALUE_DAMAGE = "Stats.Damage.Value";
	final public static String VALUE_PVE_DAMAGE = "Stats.PVPDamage.Value";
	final public static String VALUE_PVP_DAMAGE = "Stats.PVEDamage.Value";
	final public static String VALUE_HIT_RATE = "Stats.HitRate.Value";
	final public static String VALUE_REAL = "Stats.Real.Value";
	final public static String VALUE_CRIT_RATE = "Stats.Crit.Value";
	final public static String VALUE_CRIT_DAMAGE = "Stats.CritDamage.Value";
	final public static String VALUE_LIFE_STEAL = "Stats.LifeSteal.Value";
	final public static String VALUE_IGNITION = "Stats.Ignition.Value";
	final public static String VALUE_WITHER = "Stats.Wither.Value";
	final public static String VALUE_POISON = "Stats.Poison.Value";
	final public static String VALUE_BLINDNESS = "Stats.Blindness.Value";
	final public static String VALUE_SLOWNESS = "Stats.Slowness.Value";
	final public static String VALUE_LIGHTNING = "Stats.Lightning.Value";
	final public static String VALUE_TEARING = "Stats.Tearing.Value";

	@Getter private static boolean itemUpdate;
	@Getter private static boolean healthNameVisible;
	@Getter private static boolean healthBossBar;
	@Getter private static boolean healthScaled;
	@Getter private static boolean holographic;
	@Getter private static boolean itemDisplayName;
	@Getter private static boolean damageCalculationToEVE;
	@Getter private static boolean damageGauges;
	@Getter private static boolean banShieldInsteract;
	@Getter private static boolean clearDefaultAttributePlugin;
	@Getter private static boolean clearDefaultAttributeAll;
	@Getter private static boolean clearDefaultAttributeReset = false;
	@Getter private static boolean rpgInventoryActiveItems;
	@Getter private static boolean rpgInventoryPassiveItems;
	@Getter private static boolean randomStringName;
	@Getter private static boolean randomStringLore;

	public static void loadConfig(){
		//检测Config.yml是否存在
		if (!configFile.exists()){
			//创建Config.yml
			createConfig();
		}else {
	        Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §aFind Config.yml");
			config = new YamlConfiguration();
			//读取config
			try {config.load(configFile);} catch (IOException | InvalidConfigurationException e) {e.printStackTrace();Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §c读取config时发生错误");}
		}
		
		itemUpdate = config.getBoolean(ITEM_UPDATE_ENABLED);
		healthNameVisible = config.getBoolean(HEALTH_NAME_VISIBLE_ENABLED);
		healthBossBar = config.getBoolean(HEALTH_BOSSBAR_ENABLED);
		healthScaled = config.getBoolean(HEALTH_SCALED_ENABLED);
		holographic = config.getBoolean(HOLOGRAPHIC_ENABLED);
		itemDisplayName = config.getBoolean(ITEM_DISPLAY_NAME);
		damageCalculationToEVE = config.getBoolean(DAMAGE_CALCULATION_TO_EVE);
		damageGauges = config.getBoolean(DAMAGE_GAUGES);
		clearDefaultAttributePlugin = config.getBoolean(CLEAR_DEFAULT_ATTRIBUTE_THIS_PLUGIN);
		clearDefaultAttributeAll = config.getBoolean(CLEAR_DEFAULT_ATTRIBUTE_ALL);
		// 当 All Plugin 开启时 重置为false，否则读取配置文件
		// 意思是，开启消除默认标签时，该功能不启用
		clearDefaultAttributeReset = clearDefaultAttributePlugin && clearDefaultAttributeAll ? false : config.getBoolean(CLEAR_DEFAULT_ATTRIBUTE_RESET);
		rpgInventoryActiveItems = config.getBoolean(RPG_INVENTORY_ACTIVE_ITEMS);
		rpgInventoryPassiveItems = config.getBoolean(RPG_INVENTORY_PASSIVE_ITEMS);
		banShieldInsteract = config.getBoolean(BAN_SHIELD_INTERACT);
		randomStringName = config.getBoolean(RANDOM_STRING_NAME);
		randomStringLore = config.getBoolean(RANDOM_STRING_LORE);
	}
	
	public static void createConfig(){
        Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §cCreate Config.yml");
		config = new YamlConfiguration();
		// 物品更新机制
		config.set(ITEM_UPDATE_ENABLED, false);
		// 全息显示
		config.set(HOLOGRAPHIC_ENABLED, true);
		config.set(HOLOGRAPHIC_TICK, 40);
		// 血量头顶显示
		config.set(HEALTH_NAME_VISIBLE_ENABLED, true);
		config.set(HEALTH_NAME_VISIBLE_DISPLAY_TIME, 40);
		config.set(HEALTH_NAME_VISIBLE_SIZE, 10);
		config.set(HEALTH_NAME_VISIBLE_CURRENT, "❤");
		config.set(HEALTH_NAME_VISIBLE_LOSS, "&7❤");
		config.set(HEALTH_NAME_VISIBLE_PREFIX, "&8[&c");
		config.set(HEALTH_NAME_VISIBLE_SUFFIX, "&8]");
		// 血量显示
		config.set(HEALTH_BOSSBAR_ENABLED, true);
		config.set(HEALTH_BOSSBAR_FORMAT, "&a&l{0}:&8&l[&a&l{1}&7&l/&c&l{2}&8&l]");
		config.set(HEALTH_BOSSBAR_DISPLAY_TIME, 100);
		// 血条压缩
		config.set(HEALTH_SCALED_ENABLED, true);
		config.set(HEALTH_SCALED_VALUE, 40);
		// 怪V怪的属性计算
		config.set(DAMAGE_CALCULATION_TO_EVE, false);
		// 伤害计量器
		config.set(DAMAGE_GAUGES, true);
		// 伤害计量器
		config.set(DAMAGE_GAUGES, true);
		// 禁止盾牌右键
		config.set(BAN_SHIELD_INTERACT, false);
		// 清除默认属性标签
		config.set(CLEAR_DEFAULT_ATTRIBUTE_THIS_PLUGIN, true);
		config.set(CLEAR_DEFAULT_ATTRIBUTE_ALL, false);
		config.set(CLEAR_DEFAULT_ATTRIBUTE_RESET, false);
		// 支持RPGInventory
		config.set(RPG_INVENTORY_ACTIVE_ITEMS, false);
		config.set(RPG_INVENTORY_PASSIVE_ITEMS, false);
		// 随机字符串
		config.set(RANDOM_STRING_NAME, true);
		config.set(RANDOM_STRING_LORE, true);

		config.set(NAME_HAND_MAIN, "主手");
		config.set(NAME_HAND_OFF, "副手");
		config.set(NAME_ROLE, "限制职业");
		config.set(NAME_LIMIT_LEVEL, "限制等级");
		config.set(NAME_EXP_ADDITION, "经验加成");
		config.set(NAME_DURABILITY, "耐久度");
		config.set(NAME_SPEED, "速度");
		config.set(NAME_ATTACKSPEED, "攻击速度");
		
		config.set(NAME_HEALTH, "生命上限");
		config.set(NAME_HEALTH_REGEN, "生命恢复");
		config.set(NAME_DODGE, "闪避几率");
		config.set(NAME_DEFENSE, "防御力");
		config.set(NAME_PVP_DEFENSE, "PVP防御力");
		config.set(NAME_PVE_DEFENSE, "PVE防御力");
		config.set(NAME_TOUGHNESS, "韧性");
		config.set(NAME_REFLECTION_RATE, "反射几率");
		config.set(NAME_REFLECTION, "反射伤害");
		config.set(NAME_BLOCK_RATE, "格挡几率");
		config.set(NAME_BLOCK, "格挡伤害");
		
		config.set(NAME_DAMAGE, "攻击力");
		config.set(NAME_PVP_DAMAGE, "PVP攻击力");
		config.set(NAME_PVE_DAMAGE, "PVE攻击力");
		config.set(NAME_HIT_RATE, "命中几率");
		config.set(NAME_REAL, "破甲几率");
		config.set(NAME_CRIT, "暴击几率");
		config.set(NAME_CRIT_DAMAGE, "暴击伤害");
		config.set(NAME_LIFE_STEAL, "生命吸取");
		config.set(NAME_IGNITION, "点燃几率");
		config.set(NAME_WITHER, "凋零几率");
		config.set(NAME_POISON, "中毒几率");
		config.set(NAME_BLINDNESS, "失明几率");
		config.set(NAME_SLOWNESS, "缓慢几率");
		config.set(NAME_LIGHTNING, "雷霆几率");
		config.set(NAME_TEARING, "撕裂几率");

		
		config.set(VALUE_EXP_ADDITION, 1);
		config.set(VALUE_SPEED, 1);
		
		config.set(VALUE_HEALTH, 1);
		config.set(VALUE_HEALTH_REGEN, 1);
		config.set(VALUE_DODGE, 1);
		config.set(VALUE_DEFENSE, 1);
		config.set(VALUE_PVP_DEFENSE, 1);
		config.set(VALUE_PVE_DEFENSE, 1);
		config.set(VALUE_TOUGHNESS, 1);
		config.set(VALUE_REFLECTION_RATE, 1);
		config.set(VALUE_REFLECTION, 1);
		config.set(VALUE_BLOCK_RATE, 1);
		config.set(VALUE_BLOCK, 1);
		
		config.set(VALUE_DAMAGE, 1);
		config.set(VALUE_PVP_DAMAGE, 1);
		config.set(VALUE_PVE_DAMAGE, 1);
		config.set(VALUE_HIT_RATE, 1);
		config.set(VALUE_REAL, 1);
		config.set(VALUE_CRIT_RATE, 1);
		config.set(VALUE_CRIT_DAMAGE, 1);
		config.set(VALUE_LIFE_STEAL, 1);
		config.set(VALUE_IGNITION, 1);
		config.set(VALUE_WITHER, 1);
		config.set(VALUE_POISON, 1);
		config.set(VALUE_BLINDNESS, 1);
		config.set(VALUE_SLOWNESS, 1);
		config.set(VALUE_LIGHTNING, 1);
		config.set(VALUE_TEARING, 1);

		try {config.save(configFile);} catch (IOException e) {e.printStackTrace();}
	}

	
	public static void setConfig(String loc , Object arg){
		config.set(loc, arg);
		try {config.save(configFile);} catch (IOException e) {e.printStackTrace();}
	}
}
