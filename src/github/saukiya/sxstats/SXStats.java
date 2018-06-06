package github.saukiya.sxstats;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import github.saukiya.sxstats.data.ItemDataManager;
import github.saukiya.sxstats.data.RandomStringManager;
import github.saukiya.sxstats.data.StatsDataManager;
import github.saukiya.sxstats.inventory.StatsInventory;
import github.saukiya.sxstats.listener.OnBanShieldInteractListener;
import github.saukiya.sxstats.listener.OnDamageListener;
import github.saukiya.sxstats.listener.OnMythicmobsDropOrSpawnListener;
import github.saukiya.sxstats.listener.OnExpChangeListener;
import github.saukiya.sxstats.listener.OnInventoryClickListener;
import github.saukiya.sxstats.listener.OnItemDurabilityListener;
import github.saukiya.sxstats.listener.OnItemSpawnListener;
import github.saukiya.sxstats.listener.OnUpdateStatsListener;
import github.saukiya.sxstats.util.CommandType;
import github.saukiya.sxstats.util.Config;
import github.saukiya.sxstats.util.ItemUtil;
import github.saukiya.sxstats.util.Message;
import github.saukiya.sxstats.util.Placeholders;
import github.saukiya.sxstats.util.PlayerCommand;
import lombok.Getter;


public class SXStats extends JavaPlugin implements Listener{

	@Getter private static boolean placeholder = true;
	@Getter private static boolean holographic = true;
	@Getter private static boolean rpgInventory = true;
	
	public void onEnable()
	{
		Long oldTimes = System.currentTimeMillis();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new OnBanShieldInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new OnDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new OnExpChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new OnInventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new OnItemDurabilityListener(), this);
        Bukkit.getPluginManager().registerEvents(new OnItemSpawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new OnUpdateStatsListener(), this);
    	Bukkit.getConsoleSender().sendMessage("["+this.getName()+"] ServerPacket: "+Bukkit.getServer().getClass().getPackage().getName().replace(".", "-").split("-")[3]);
        
    	if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
        	new Placeholders(this).hook();
        	Bukkit.getConsoleSender().sendMessage("["+this.getName()+"] Find PlacholderAPI!");
        }
        else {
        	placeholder = false;
        	Bukkit.getConsoleSender().sendMessage("["+this.getName()+"] §cNo Find PlacholderAPI!");
        }
        
        if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
        	Bukkit.getConsoleSender().sendMessage("["+this.getName()+"] Find HolographicDisplays!");
        }
        else {
        	holographic = false;
        	Bukkit.getConsoleSender().sendMessage("["+this.getName()+"] §cNo Find HolographicDisplays!");
        }
        
        if (Bukkit.getPluginManager().isPluginEnabled("Mythicmobs")) {
            Bukkit.getPluginManager().registerEvents(new OnMythicmobsDropOrSpawnListener(), this);
        	Bukkit.getConsoleSender().sendMessage("["+this.getName()+"] Find Mythicmobs!");
        }
        else {
        	Bukkit.getConsoleSender().sendMessage("["+this.getName()+"] §cNo Find Mythicmobs!");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("RPGInventory")) {
        	Bukkit.getConsoleSender().sendMessage("["+this.getName()+"] Find RPGInventory!");
        }
        else {
        	rpgInventory = false;
        	Bukkit.getConsoleSender().sendMessage("["+this.getName()+"] §cNo Find RPGInventory!");
        }
        
        Config.loadConfig();
		Message.loadMessage();
		ItemUtil.loadItemUtil();
		RandomStringManager.loadRandomMap();
        ItemDataManager.loadItemData();
        StatsDataManager.healthRegenRunnable();
		Bukkit.getConsoleSender().sendMessage("["+this.getName()+"] 加载用时: §c"+(System.currentTimeMillis()-oldTimes)+"§r 毫秒");
        Bukkit.getConsoleSender().sendMessage("["+this.getName()+"] §c本插件首发于MCBBS! §4未经允许 严禁转载!");
        Bukkit.getConsoleSender().sendMessage("["+this.getName()+"] §c加载成功! 插件作者: Saukiya 插件联系: 1940208750");
	}

	public void onDisable(){
		OnDamageListener.getHologramsList().forEach(Hologram::delete);
		OnDamageListener.getBossMap().values().forEach(BossBar::removeAll);
		OnDamageListener.getNameMap().forEach((key,nameData) -> {
			Entity entity = Bukkit.getEntity(key);
			if(entity != null && !entity.isDead()){
				entity.setCustomName(nameData.getName());
				entity.setCustomNameVisible(nameData.isVisible());
			}
		});
	}
	
	public static Plugin getPlugin(){
		return SXStats.getPlugin(SXStats.class);
	}
	
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
        if ("sx".equalsIgnoreCase(label) || label.equalsIgnoreCase(this.getName())) {
            CommandType type = CommandType.CONSOLE;
            //判断是否是玩家
            if (sender instanceof Player) {
                //判断是否有权限
                if (!sender.hasPermission(this.getName() + ".use")) {
                    sender.sendMessage(Message.getMsg(Message.ADMIN_NO_PER_CMD));
                    return true;
                }
                type = CommandType.PLAYER;
            }
            //无参数
            if (args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&0-&8 --&7 ---&c ----&4 -----&b " + this.getName() + "&4 -----&c ----&7 ---&8 --&0 - &0By Saukiya"));
                String color = "&7";
                for (java.lang.reflect.Method method : this.getClass().getDeclaredMethods()) {
                    if (!method.isAnnotationPresent(PlayerCommand.class)) {
                        continue;
                    }
                    PlayerCommand sub = method.getAnnotation(PlayerCommand.class);
                    if (contains(sub.type(), type) && sender.hasPermission(this.getName() + "." + sub.cmd())) {
                        color = color.equals("&7") ? "" : "&7";
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(color+"/{0} {1}{2}&7 -&c {3}", label, sub.cmd(), sub.arg(), Message.getMsg("Command." + sub.cmd()))));
                    }
                }
                return true;
            }
            for (java.lang.reflect.Method method : this.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(PlayerCommand.class)) {
                    continue;
                }
                PlayerCommand sub = method.getAnnotation(PlayerCommand.class);
                if (!sub.cmd().equalsIgnoreCase(args[0])) {
                    continue;
                }
                if (!contains(sub.type(), type) || !sender.hasPermission(this.getName() + "." + args[0])) {
                    sender.sendMessage(Message.getMsg(Message.ADMIN_NO_PER_CMD));
                    return true;
                }
                try {
                    method.invoke(this, sender, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return true;
            }
            sender.sendMessage(Message.getMsg(Message.ADMIN_NO_CMD, args[0]));
            return true;
        }
        return false;
    }

    public static boolean contains(CommandType[] type1, CommandType type2) {
        return IntStream.range(0, type1.length).anyMatch(i -> type1[i].equals(CommandType.ALL) || type1[i].equals(type2));
    }
    
	@PlayerCommand(cmd="stats",type=CommandType.PLAYER)
	public void onStatsCommand(CommandSender sender,String args[]){
        StatsInventory.openStatsInventory((Player) sender);
	}
	
	// 给予指令执行方法
	@PlayerCommand(cmd="give",arg=" <itemName> [player] [amount]")
	public void onGiveCommand(CommandSender sender,String args[]){
		if (args.length < 2) {
			ItemDataManager.sendItemMapToPlayer(sender);
			return;
		}
		Player player = null;
		int amount = 1;
		if (ItemDataManager.getItem(args[1]) == null) {
			ItemDataManager.sendItemMapToPlayer(sender, args[1]);
			return;
		}
		if(args.length >= 3){
			player = Bukkit.getPlayerExact(args[2]);
			if (player != null){
				if (args.length >= 4){
					if (Pattern.compile("[0-9]*").matcher(args[3]).matches()) {
						amount = Integer.valueOf(args[3]);
					}
				}
			}
			else if (Pattern.compile("[0-9]*").matcher(args[2]).matches()){
				amount = Integer.valueOf(args[2]);
			}
		}
		if (player == null){
			if(sender instanceof Player){
				player = (Player) sender;
			}
			else {
				sender.sendMessage(Message.getMsg(Message.ADMIN_NO_CONSOLE));
				return;
			}
		}
		
		for (int i=0;i<amount;i++) {
			player.getInventory().addItem(ItemDataManager.getItem(args[1],player));
		}
		sender.sendMessage(Message.getMsg(Message.ADMIN_GIVE_ITEM, player.getName(),String.valueOf(amount),args[1]));
	}
	
	// 保存指令执行方法
	@PlayerCommand(cmd="save",arg=" <itemName>",type=CommandType.PLAYER)
	public void onSaveCommand(CommandSender sender,String args[]){
		if (args.length < 2) {
			sender.sendMessage(Message.getMsg(Message.ADMIN_NO_FORMAT));
			return;
		}
		String itemName = args[1];
		Player player = (Player) sender;
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if (itemStack.getType().toString().contains("AIR")) {
			player.sendMessage(Message.getMsg(Message.ADMIN_NO_ITEM));
			return;
		}
		if (ItemDataManager.isItem(itemName)) {
			player.sendMessage(Message.getMsg(Message.ADMIN_HAS_ITEM,itemName));
			return;
		}
		ItemDataManager.saveItem(itemName, itemStack);
		sender.sendMessage(Message.getMsg(Message.ADMIN_SAVE_ITEM, itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : itemStack.getType().name(),args[1]));
	}
	
	@PlayerCommand(cmd="reload")
	public void onReloadCommand(CommandSender sender,String args[]){
		Long oldTimes = System.currentTimeMillis();
        Config.loadConfig();
		Message.loadMessage();
		RandomStringManager.loadRandomMap();
        ItemDataManager.loadItemData();
        int size = 0;
        for (UUID uuid : StatsDataManager.equipmentMap.keySet()){
        	Entity entity = Bukkit.getEntity(uuid);
        	if(entity == null){
        		StatsDataManager.clearEntityData(uuid);
        		size++;
        	}
        }
        for (UUID uuid : StatsDataManager.mainHandMap.keySet()){
        	Entity entity = Bukkit.getEntity(uuid);
        	if(entity == null){
        		StatsDataManager.clearEntityData(uuid);
        		size++;
        	}
        }
        if(size >0){
        	sender.sendMessage(Message.getMsg(Message.ADMIN_CLEAR_ENTITY_DATA, String.valueOf(size)));
        }
		sender.sendMessage(Message.getMsg(Message.ADMIN_PLUGIN_RELOAD));
		Bukkit.getConsoleSender().sendMessage("["+this.getName()+"] §a重载用时:"+(System.currentTimeMillis()-oldTimes)+"毫秒");
	}
	
}
