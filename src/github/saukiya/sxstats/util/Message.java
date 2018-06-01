package github.saukiya.sxstats.util;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import github.saukiya.sxstats.SXStats;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Message {
	private static YamlConfiguration messages = new YamlConfiguration();
	final static File messageFile = new File("plugins" + File.separator + SXStats.getPlugin().getName() + File.separator + "Message.yml");

	final public static String PLAYER_NO_LEVEL_USE = "Player.NoLevelUse";
	final public static String PLAYER_NO_ROLE = "Player.NoRole";
	final public static String PLAYER_NO_HAND = "Player.NoHand";
	final public static String PLAYER_OVERDUE_ITEM = "Player.OverdueItem";
	final public static String PLAYER_EXP_ADDITION = "Player.ExpAddition";
	
	final public static String PLAYER_BATTLE_CRIT = "Player.Battle.Crit";
	final public static String PLAYER_BATTLE_IGNITION = "Player.Battle.Ignition";
	final public static String PLAYER_BATTLE_WITHER = "Player.Battle.Wither";
	final public static String PLAYER_BATTLE_POISON = "Player.Battle.Poison";
	final public static String PLAYER_BATTLE_BLINDNESS = "Player.Battle.Blindness";
	final public static String PLAYER_BATTLE_SLOWNESS = "Player.Battle.Slowness";
	final public static String PLAYER_BATTLE_LIGHTNING = "Player.Battle.Lightning";
	final public static String PLAYER_BATTLE_REAL = "Player.Battle.Real";
	final public static String PLAYER_BATTLE_TEARING = "Player.Battle.Tearing";
	final public static String PLAYER_BATTLE_REFLECTION = "Player.Battle.Reflection";
	final public static String PLAYER_BATTLE_BLOCK = "Player.Battle.Block";
	final public static String PLAYER_BATTLE_DODGE = "Player.Battle.Dodge";
	final public static String PLAYER_BATTLE_LIFE_STEAL = "Player.Battle.LifeSteal";
	final public static String PLAYER_BATTLE_NULL_DAMAGE = "Player.Battle.NullDamage";

	final public static String PLAYER_HOLOGRAPHIC_CRIT = "Player.Holographic.Crit";
	final public static String PLAYER_HOLOGRAPHIC_IGNITION = "Player.Holographic.Ignition";
	final public static String PLAYER_HOLOGRAPHIC_WITHER = "Player.Holographic.Wither";
	final public static String PLAYER_HOLOGRAPHIC_POISON = "Player.Holographic.Poison";
	final public static String PLAYER_HOLOGRAPHIC_BLINDNESS = "Player.Holographic.Blindness";
	final public static String PLAYER_HOLOGRAPHIC_SLOWNESS = "Player.Holographic.Slowness";
	final public static String PLAYER_HOLOGRAPHIC_LIGHTNING = "Player.Holographic.Lightning";
	final public static String PLAYER_HOLOGRAPHIC_REAL = "Player.Holographic.Real";
	final public static String PLAYER_HOLOGRAPHIC_TEARING = "Player.Holographic.Tearing";
	final public static String PLAYER_HOLOGRAPHIC_REFLECTION = "Player.Holographic.Reflection";
	final public static String PLAYER_HOLOGRAPHIC_BLOCK = "Player.Holographic.Block";
	final public static String PLAYER_HOLOGRAPHIC_DODGE = "Player.Holographic.Dodge";
	final public static String PLAYER_HOLOGRAPHIC_LIFE_STEAL = "Player.Holographic.LifeSteal";
	final public static String PLAYER_HOLOGRAPHIC_NULL_DAMAGE = "Player.Holographic.NullDamage";
	final public static String PLAYER_HOLOGRAPHIC_DAMAGE = "Player.Holographic.Damage";
	
	final public static String INVENTORY_STATS_NAME = "Inventory.Stats.Name";
	final public static String INVENTORY_STATS_HIDE_ON = "Inventory.Stats.HideOn";
	final public static String INVENTORY_STATS_HIDE_OFF = "Inventory.Stats.HideOff";
	final public static String INVENTORY_STATS_SKULL_NAME = "Inventory.Stats.SkullName";
	final public static String INVENTORY_STATS_SKULL_LORE = "Inventory.Stats.SkullLore";
	final public static String INVENTORY_STATS_ATTACK = "Inventory.Stats.Attack";
	final public static String INVENTORY_STATS_ATTACK_LORE = "Inventory.Stats.AttackLore";
	final public static String INVENTORY_STATS_DEFENSE= "Inventory.Stats.Defense";
	final public static String INVENTORY_STATS_DEFENSE_LORE= "Inventory.Stats.DefenseLore";
	final public static String INVENTORY_STATS_BASE= "Inventory.Stats.Base";
	final public static String INVENTORY_STATS_BASE_LORE= "Inventory.Stats.BaseeLore";

	final public static String ADMIN_CLEAR_ENTITY_DATA = "Admin.ClearEntityData";
	final public static String ADMIN_NO_ITEM = "Admin.NoItem";
	final public static String ADMIN_HAS_ITEM = "Admin.HasItem";
	final public static String ADMIN_GIVE_ITEM = "Admin.GiveItem";
	final public static String ADMIN_SAVE_ITEM = "Admin.SaveItem";
	final public static String ADMIN_NO_PER_CMD = "Admin.NoPermissionCommand";
	final public static String ADMIN_NO_CMD = "Admin.NoCommand";
	final public static String ADMIN_NO_FORMAT = "Admin.NoFormat";
	final public static String ADMIN_NO_ONLINE = "Admin.NoOnline";
	final public static String ADMIN_NO_CONSOLE = "Admin.NoConsole";
	final public static String ADMIN_PLUGIN_RELOAD = "Admin.PluginReload";

	final public static String COMMAND_STATS = "Command.stats";
	final public static String COMMAND_GIVE = "Command.give";
	final public static String COMMAND_SAVE = "Command.save";
	final public static String COMMAND_RELOAD = "Command.reload";
	
	final public static String REPLACE_LIST = "ReplaceList";
	
	public static void createMessage(){
        Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §cCreate Message.yml");
        
		messages.set(PLAYER_NO_LEVEL_USE, "&8[&dSX-Stats&8] &c你没有达到使用 &a{0} &c的等级要求!");
		messages.set(PLAYER_NO_ROLE, "&8[&dSX-Stats&8] &c你没有达到使用 &a{0} &c的职业要求!");
		messages.set(PLAYER_NO_HAND, "&8[&dSX-Stats&8] &7物品 &a{0} &7只适合装在 &a{1}.");
		messages.set(PLAYER_OVERDUE_ITEM, "&8[&dSX-Stats&8] &c物品 &a{0}&c 已经过期了!");
		messages.set(PLAYER_EXP_ADDITION, "&8[&dSX-Stats&8] &7你的经验额外增加了 &6{0}&7! [&a{1}%&7]");

		List<String> attackLoreList = new ArrayList<String>();
		
		attackLoreList.add("&e攻击力:&b %sx_mindamage% - %sx_maxdamage%");
		attackLoreList.add("&ePVP攻击力:&b %sx_pvpmindamage% - %sx_pvpmaxdamage%");
		attackLoreList.add("&ePVE攻击力:&b %sx_pvemindamage% - %sx_pvemaxdamage%");
		attackLoreList.add("&e命中几率:&b %sx_hitRate%%");
		attackLoreList.add("&e破甲几率:&b %sx_real%%");
		attackLoreList.add("&e暴击几率:&b %sx_crit%%");
		attackLoreList.add("&e暴击伤害:&b %sx_critDamage%%");
		attackLoreList.add("&e生命吸取:&b %sx_lifeSteal%%");
		attackLoreList.add("&e点燃几率:&b %sx_ignition%%");
		attackLoreList.add("&e凋零几率:&b %sx_wither%%");
		attackLoreList.add("&e中毒几率:&b %sx_poison%%");
		attackLoreList.add("&e失明几率:&b %sx_blindness%%");
		attackLoreList.add("&e缓慢几率:&b %sx_slowness%%");
		attackLoreList.add("&e雷霆几率:&b %sx_lightning%%");
		attackLoreList.add("&e撕裂几率:&b %sx_tearing%%");
		
		List<String> defenseLoreList = new ArrayList<String>();
		
		defenseLoreList.add("&e防御力:&b %sx_minDefense% - %sx_maxDefense%");
		defenseLoreList.add("&ePVP防御力:&b %sx_pvpMinDefense% - %sx_pvpMaxDefense%");
		defenseLoreList.add("&ePVE防御力:&b %sx_pveMinDefense% - %sx_pveMaxDefense%");
		defenseLoreList.add("&e生命上限:&b %sx_health%");
		defenseLoreList.add("&e生命恢复:&b %sx_healthRegen%");
		defenseLoreList.add("&e闪避几率:&b %sx_dodge%%");
		defenseLoreList.add("&e韧性:&b %sx_toughness%%");
		defenseLoreList.add("&e反射几率:&b %sx_reflectionRate%%");
		defenseLoreList.add("&e反射伤害:&b %sx_reflection%%");
		defenseLoreList.add("&e格挡几率:&b %sx_blockRate%%");
		defenseLoreList.add("&e格挡伤害:&b %sx_block%%");
		
		List<String> baseLoreList = new ArrayList<String>();
		
		baseLoreList.add("&e经验加成:&b %sx_expAddition%%");
		baseLoreList.add("&e速度:&b %sx_speed%%");
		messages.set(INVENTORY_STATS_NAME, "&d&l&oSX-Stats");
		messages.set(INVENTORY_STATS_HIDE_ON, "&a点击显示更多属性");
		messages.set(INVENTORY_STATS_HIDE_OFF, "&c点击隐藏更多属性");
		messages.set(INVENTORY_STATS_SKULL_NAME, "&6&l&o{0} 的属性");
		messages.set(INVENTORY_STATS_SKULL_LORE, Arrays.asList("&e战斗力:&b %sx_value%"));
		messages.set(INVENTORY_STATS_ATTACK, "&a&l&o攻击属性");
		messages.set(INVENTORY_STATS_ATTACK_LORE, attackLoreList);
		messages.set(INVENTORY_STATS_DEFENSE, "&9&l&o防御属性");
		messages.set(INVENTORY_STATS_DEFENSE_LORE, defenseLoreList);
		messages.set(INVENTORY_STATS_BASE, "&9&l&o其他属性");
		messages.set(INVENTORY_STATS_BASE_LORE, baseLoreList);

		messages.set(PLAYER_BATTLE_CRIT, "[ACTIONBAR]&c{0}&6 对 &c{1}&6 造成了暴击!");
		messages.set(PLAYER_BATTLE_IGNITION, "[ACTIONBAR]&c{0}&6 被 &c{1}&6 点燃了!");
		messages.set(PLAYER_BATTLE_WITHER, "[ACTIONBAR]]&c{0}&6 被 &c{1}&6 凋零了!");
		messages.set(PLAYER_BATTLE_POISON, "[ACTIONBAR]&c{0}&6 被 &c{1}&6 中毒了!");
		messages.set(PLAYER_BATTLE_BLINDNESS, "[ACTIONBAR]&c{0}&6 被 &c{1}&6 致盲了!");
		messages.set(PLAYER_BATTLE_SLOWNESS, "[ACTIONBAR]&c{0}&6 被 &c{1}&6 减速了!");
		messages.set(PLAYER_BATTLE_LIGHTNING, "[ACTIONBAR]&c{0}&6 被 &c{1}&6 用雷电击中了!");
		messages.set(PLAYER_BATTLE_REAL, "[ACTIONBAR]&c{0}&6 被 &c{1}&6 破甲了!");
		messages.set(PLAYER_BATTLE_TEARING, "[ACTIONBAR]&c{0}&6 被 &c{1}&6 撕裂了!");
		messages.set(PLAYER_BATTLE_REFLECTION, "[ACTIONBAR]&c{0}&6 被 &c{1}&6 反弹伤害了!");
		messages.set(PLAYER_BATTLE_BLOCK, "[ACTIONBAR]&c{0}&6 格挡了 &c{1}&6 的部分伤害!");
		messages.set(PLAYER_BATTLE_DODGE, "[ACTIONBAR]&c{0}&6 躲开了 &c{1}&6 的攻击!");
		messages.set(PLAYER_BATTLE_NULL_DAMAGE, "[ACTIONBAR]&c{0}&6 无法对 &c{1}&6 造成伤害!");

		messages.set(PLAYER_HOLOGRAPHIC_CRIT, "&a&o暴击: &b&o+{0}");
		messages.set(PLAYER_HOLOGRAPHIC_IGNITION, "&c&o点燃: &b&o{0}s");
		messages.set(PLAYER_HOLOGRAPHIC_WITHER, "&7&o凋零: &b&o{0}s");
		messages.set(PLAYER_HOLOGRAPHIC_POISON, "&5&o中毒: &b&o{0}s");
		messages.set(PLAYER_HOLOGRAPHIC_BLINDNESS, "&8&o致盲: &b&o{0}s");
		messages.set(PLAYER_HOLOGRAPHIC_SLOWNESS, "&b&o减速: &b&o{0}s");
		messages.set(PLAYER_HOLOGRAPHIC_LIGHTNING, "&e&o雷霆");
		messages.set(PLAYER_HOLOGRAPHIC_REAL, "&c&o破甲");
		messages.set(PLAYER_HOLOGRAPHIC_TEARING, "&c&o撕裂");
		messages.set(PLAYER_HOLOGRAPHIC_REFLECTION, "&6&o反伤: &b&o{0}%");
		messages.set(PLAYER_HOLOGRAPHIC_BLOCK, "&2&o格挡: &b&o{0}%");
		messages.set(PLAYER_HOLOGRAPHIC_DODGE, "&a&o闪避");
		messages.set(PLAYER_HOLOGRAPHIC_LIFE_STEAL, "&c&o吸取: &b&o{0}");
		messages.set(PLAYER_HOLOGRAPHIC_NULL_DAMAGE, "&c&o无伤");
		messages.set(PLAYER_HOLOGRAPHIC_DAMAGE, "&c&o伤害: &b&o{0}");

		messages.set(ADMIN_CLEAR_ENTITY_DATA, "&8[&dSX-Stats&8] &c清理了 &6{0}&c 个多余的生物属性数据!");
		messages.set(ADMIN_NO_ITEM, "&8[&dSX-Stats&8] &c物品不存在!");
		messages.set(ADMIN_HAS_ITEM, "&8[&dSX-Stats&8] &c已经存在名字为  &6{0}&c的物品!");
		messages.set(ADMIN_GIVE_ITEM, "&8[&dSX-Stats&8] &c给予 &6{0} &a{1}&c个 &6{2}&c 物品!");
		messages.set(ADMIN_SAVE_ITEM, "&8[&dSX-Stats&8] &a物品 &6{0} &a成功保存! 编号为: &6{1}&a!");
		messages.set(ADMIN_NO_PER_CMD, "&8[&dSX-Stats&8] &c你没有权限执行此指令");
		messages.set(ADMIN_NO_CMD, "&8[&dSX-Stats&8] &c未找到此子指令:{0}");
		messages.set(ADMIN_NO_FORMAT, "&8[&dSX-Stats&8] &c格式错误!");
		messages.set(ADMIN_NO_ONLINE, "&8[&dSX-Stats&8] &c玩家不在线或玩家不存在!");
		messages.set(ADMIN_NO_CONSOLE, "&8[&dSX-Stats&8] &c控制台不允许执行此指令!");
		messages.set(ADMIN_PLUGIN_RELOAD, "&8[&dSX-Stats&8] §c插件已重载");

		messages.set(COMMAND_STATS, "查看属性");
		messages.set(COMMAND_GIVE, "给予玩家RPG物品");
		messages.set(COMMAND_SAVE, "保存当前的物品到配置文件");
		messages.set(COMMAND_RELOAD, "重新加载这个插件的配置");

		messages.set(REPLACE_LIST+".Pig", "猪猪");
		messages.set(REPLACE_LIST+".Sheep", "羊羊");
		messages.set(REPLACE_LIST+".Rabbit", "兔兔");
		messages.set(REPLACE_LIST+".Mule", "骡骡");
		messages.set(REPLACE_LIST+".Skeleton", "骷髅");
		messages.set(REPLACE_LIST+".Zombie", "僵尸");
		messages.set(REPLACE_LIST+".Silverfish", "蠢虫");
		messages.set(REPLACE_LIST+".Horse", "马马");
		messages.set(REPLACE_LIST+".Cow", "牛牛");
		messages.set(REPLACE_LIST+".Chicken", "鸡鸡");
		try {messages.save(messageFile);} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void loadMessage(){
		if (!messageFile.exists()){
				createMessage();
		}else {
	        Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §aFind Message.yml");
		}
		messages = new YamlConfiguration();
		try {messages.load(messageFile);} catch (IOException | InvalidConfigurationException e) {e.printStackTrace();Bukkit.getConsoleSender().sendMessage("§8[§6HorseChair§8] §a读取message时发生错误");}
	}

	public static String replace(String str){
		if(messages.contains(REPLACE_LIST)){
			for(String replaceName : messages.getConfigurationSection(REPLACE_LIST).getKeys(false)){
				str = str.replace(replaceName, messages.getString(REPLACE_LIST+"."+replaceName));
			}
		}
		return str.replace("&", "§");
	}

    public static String getMsg(String loc, Object... args) {
        String message = ChatColor.translateAlternateColorCodes('&',messages.getString(loc,"Null Message: " + loc));
        return args.length >= 0 ? MessageFormat.format(message, args) : message;
    }

	public static List<String> getList(String loc,Object... args){
		List<String> list = messages.getStringList(loc);
		if (list == null || list.isEmpty()) {
			list.add("Null Message: " + loc);
			return list;
		}
		//循环lore
		for(int e= 0;e <list.size();e++){
			String lore = list.get(e).replace("&", "§");
			for (int i= 0; i < args.length;i++){
				lore = lore.replace("{" + i + "}", args[i]==null ? "null" : args[i].toString());
			}
			list.set(e,lore);
		}
		return list;
	}
	
	public static void send(LivingEntity entity,String loc,Object... args){
		if(!(entity instanceof Player))return;
		Player player = (Player) entity;
		String message = getMsg(loc,args);
		if(message.equals("Null Message: " + loc)) return;
		if (message.contains("[ACTIONBAR]")){
			message = message.replace("[ACTIONBAR]", "");
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
		}
		else if (message.contains("[TITLE]")){
			message = message.replace("[TITLE]", "");
			if(message.contains(":")){
				String title = message.split(":")[0];
				String subTitle = message.split(":")[1];
				player.sendTitle(title, subTitle,5,20,3);
			}
			else {
				player.sendTitle(message, null,5,20,3);
			}
		}
		else {
			player.sendMessage(message);
		}
	}
}
