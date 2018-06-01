package github.saukiya.sxstats.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;

import github.saukiya.sxstats.SXStats;
import github.saukiya.sxstats.listener.OnItemDurabilityListener;
import github.saukiya.sxstats.util.Config;
import github.saukiya.sxstats.util.ItemUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ItemDataManager {
	private static Map<String,ItemStack> itemMap = new HashMap<>();
	final static File itemFiles = new File("plugins" + File.separator + SXStats.getPlugin().getName() + File.separator + "Item");
	final static File itemDefaultFile = new File("plugins" + File.separator + SXStats.getPlugin().getName() + File.separator + "Item" + File.separator + "Default" + File.separator  + "Default.yml");

	static List<String> ColorList = Arrays.asList("§0","§1","§2","§3","§4","§5","§6","§7","§8","§9");
	static List<String> ColorReplaceList = Arrays.asList("%零%","%一%","%二%","%三%","%四%","%五%","%六%","%七%","%八%","%九%");
	
	public static void loadItemData() {
		loadItemMap();
		Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §a已读取 §b"+itemMap.size()+" §a个物品");
	}
	
	static public String clearColor(String lore){
		for (int i=0;i<10;i++){
			lore = lore.replace(ColorList.get(i), ColorReplaceList.get(i));
		}
		return lore;
	}
	static public String replaceColor(String lore){
		for (int i=0;i<10;i++){
			lore = lore.replace(ColorReplaceList.get(i), ColorList.get(i));
		}
		return lore;
	}

	// 快速处理ItemStack
	@SuppressWarnings("deprecation")
	public static ItemStack getItemStack(String itemName,String id,List<String> itemLore,List<String> enchantList,List<String> itemFlagList,Boolean unbreakable,String color,String skullName){
        int itemMaterial = 260;
        int itemDurability = 0;
        if(id != null){
            if (id.contains(":")){
            	itemMaterial = Integer.valueOf(id.split(":")[0]);
            	itemDurability = Integer.valueOf(id.split(":")[1]);
            }else{
            	itemMaterial = Integer.valueOf(id);
            }
        }
		ItemStack item = new ItemStack(itemMaterial,1,(short) itemDurability);
		ItemMeta meta = item.getItemMeta();
		if (itemName != null) {
			meta.setDisplayName(itemName.replace("&", "§"));
		}
		if (itemLore != null) {
			for(int i=0;i < itemLore.size();i++){
				itemLore.set(i, itemLore.get(i).replace("&", "§"));
			}
			meta.setLore(itemLore);
		}
		if(enchantList !=  null && enchantList.size() > 0){
			//TODO 附魔
			for(String enchantName : enchantList){
				if(enchantName.contains(":") && enchantName.split(":").length > 1){
					Enchantment enchant = Enchantment.getByName(enchantName.split(":")[0]);
					int level = Integer.valueOf(enchantName.split(":")[1].replaceAll("[^0-9]", ""));
					if(enchant != null){
						meta.addEnchant(enchant, level, true);
					}
					else {
				        Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §a物品: "+itemName+" 的附魔: "+enchantName.split(":")[0]+"不是正常的附魔名称！");
					}
				}
			}
		}
		if(itemFlagList !=  null && itemFlagList.size() > 0){
			for(String flagName : itemFlagList){
				ItemFlag itemFlag = ItemFlag.valueOf(flagName);
				if(itemFlag != null){
					meta.addItemFlags(itemFlag);
				}else {
			        Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §a物品: "+itemName+" 的Flag: "+flagName+"不是正常的Flag名称！");
				}
			}
		}
		if (unbreakable != null){
			meta.setUnbreakable(unbreakable);
		}
		if (color != null && item.getType().name().contains("LEATHER_")){
			Color c = Color.fromRGB(Integer.valueOf(color.split(",")[0]),Integer.valueOf(color.split(",")[1]),Integer.valueOf(color.split(",")[2]));
			((LeatherArmorMeta)meta).setColor(c);
		}
		if (skullName != null){
			((SkullMeta)meta).setOwner(skullName);
		}
		item.setItemMeta(meta);
		return item;
	}
	
	public static void loadItem(File files){
		for (File file: files.listFiles()){
			if (file.isDirectory()){
				loadItem(file);
			}
			else {
				YamlConfiguration itemData = new YamlConfiguration();
				try {
					itemData.load(file);
				} catch (IOException | InvalidConfigurationException e) {
					//
				}
				for (String name:itemData.getKeys(false)) {
					if (itemMap.containsKey(name)){
						Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §c不要重复物品名: §6"+file.toString().replace("plugins"+File.separator+SXStats.getPlugin().getName()+File.separator, "")+File.separator+name +" §c!");
					}
					String itemName = itemData.getString(name+".Name");
					String id = itemData.getString(name+".ID");
					List<String> itemLore = itemData.getStringList(name+".Lore");
					List<String> enchantList = itemData.getStringList(name+".EnchantList");
					List<String> itemFlagList = itemData.getStringList(name+".ItemFlagList");
					Boolean unbreakable = itemData.getBoolean(name+".Unbreakable");
					String color = itemData.getString(name+".Color");
					String skullName = itemData.getString(name+".SkullName");
					
					ItemStack item = getItemStack(itemName, id, itemLore, enchantList, itemFlagList , unbreakable , color , skullName);
					int hashCode = item.getType().name().hashCode()/100 + item.getDurability();
					
					if (itemName != null) {
						hashCode += itemName.hashCode()/100;
					}
					
					if (itemLore != null) {
						hashCode += itemLore.hashCode()/100;
					}
					
					if (item.getEnchantments().size() > 0){
						hashCode += item.getEnchantments().hashCode()/100;
					}
					
					if(item.getItemMeta().getItemFlags().size() > 0){
						hashCode += item.getItemMeta().getItemFlags().hashCode()/100;
					}

					if (color != null){
						Color c = Color.fromRGB(Integer.valueOf(color.split(",")[0]),Integer.valueOf(color.split(",")[1]),Integer.valueOf(color.split(",")[2]));
						hashCode += c.hashCode()/100;
					}
					
					// 记录物品名 HashCode
					if(Config.isClearDefaultAttributePlugin() && item.getItemMeta().hasLore()){
						ItemUtil.clearAttribute(item);
					}

					if(hashCode != item.getType().name().hashCode()/100 + item.getDurability()){
						item = ItemUtil.setNBT(ItemUtil.setNBT(item,"Name",name),"HasCode",String.valueOf(hashCode));
					}
					
					itemMap.put(name, item);
				}
			}
		}
	}
	
	public static void loadItemMap() {
		itemMap.clear();
		if (!itemFiles.exists() || itemFiles.listFiles().length == 0) {
			createItemData();
		}
		loadItem(itemFiles);
	}
	
	public static void saveItemData(YamlConfiguration itemData){
		try {itemData.save(itemDefaultFile);} catch (IOException e) {e.printStackTrace();}
	}
	
	static void createItemData() {
        Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §aCreate Item/Default.yml");
        YamlConfiguration itemData = new YamlConfiguration();
        itemData.set("默认一.Name", "<s:DefaultPrefix>&c炎之洗礼 <s:DefaultSuffix>");
        itemData.set("默认一.ID", 278);
        itemData.set("默认一.Lore", Arrays.asList("&6限制职业: 剑士","&6限制手持: 主手","&c攻击力: +20 - 60","&b攻击速度: +50%","&d速度: +50%","&d点燃几率: +33%","&d撕裂几率: +10%","&6限制等级: <r:100_200>级","&7耐久度: <r:10_200>/200","&r","<s:DefaultLore>"));
        itemData.set("默认一.Unbreakable", false);
        itemData.set("默认二.Name", "&c机械轻羽之靴");
        itemData.set("默认二.ID", 301);
        itemData.set("默认二.Lore", Arrays.asList("&b防御力: +15","&c生命上限: +2000","&d速度: +100%","&d闪避几率: +20%","&2生命恢复: +10","&e经验加成: +20%","&6限制等级: <r:50_100>级","&r","<s:DefaultLore>"));
        itemData.set("默认二.EnchantList", Arrays.asList("DURABILITY:5"));
        itemData.set("默认二.ItemFlagList", Arrays.asList("HIDE_ENCHANTS","HIDE_UNBREAKABLE"));
        itemData.set("默认二.Unbreakable", true);
        itemData.set("默认二.Color", "128,128,128");
		try {itemData.save(itemDefaultFile);} catch (IOException e) {e.printStackTrace();}
	}
	
	public static Boolean isItem(String itemName) {
		return itemMap.containsKey(itemName);
	}
	
	public static ItemStack getItem(String itemName,Player... players) {
		if (itemMap.containsKey(itemName)) {
			ItemStack item = itemMap.get(itemName).clone();
			if (item.hasItemMeta() && item.getItemMeta().hasLore()){
				ItemMeta meta = item.getItemMeta();
				if(meta.hasDisplayName() && Config.isRandomStringName()){
					String name = meta.getDisplayName();
					if(name.contains("<s:") && name.split("<s:").length > 1&& name.split("<s:")[1].contains(">")){
						String replaceName = name;
						for (int r = 1 ; r < name.split("<s:").length && name.split("<s:")[r].contains(">") ; r++){
							String str1 = name.split("<s:")[r].split(">")[0];
							List<String> randomList = RandomStringManager.getRandomList(str1);
							if (randomList != null){
								replaceName = replaceName.replace("<s:"+str1+">", randomList.get(new Random().nextInt(randomList.size())).replace("&", "§"));
							}
						}
						meta.setDisplayName(replaceName);
					}
				}
				List<String> loreList = meta.getLore();
				for (int i = 0 ; i < loreList.size() ; i++){
					String lore = loreList.get(i);
					// 计算随机数值
					if (lore.contains("<r:") && lore.split("<r:").length > 1&& lore.split("<r:")[1].contains(">")){
						String replaceLore = lore;
						for (int r = 1 ; r < lore.split("<r:").length && lore.split("<r:")[r].contains(">") ; r++){
							String str1 = lore.split("<r:")[r].split(">")[0];
							if (str1.contains("_") && str1.split("_").length > 1){
								int i1 = Integer.valueOf(str1.split("_")[0].replace("[^0-9]", ""));
								int i2 = Integer.valueOf(str1.split("_")[1].replace("[^0-9]", ""))+1;
								replaceLore = replaceLore.replace("<r:"+str1+">", String.valueOf(new Random().nextInt(i2-i1)+i1));
							}
						}
						lore = replaceLore;
					}
					// 计算随机字符串
					if(Config.isRandomStringLore() && lore.contains("<s:") && lore.split("<s:").length > 1&& lore.split("<s:")[1].contains(">")){
						String replaceLore = lore;
						for (int r = 1 ; r < lore.split("<s:").length && lore.split("<s:")[r].contains(">") ; r++){
							String str1 = lore.split("<s:")[r].split(">")[0];
							List<String> randomList = RandomStringManager.getRandomList(str1);
							if (randomList != null){
								replaceLore = replaceLore.replace("<s:"+str1+">", randomList.get(new Random().nextInt(randomList.size())).replace("&", "§"));
							}
						}
						lore = replaceLore;
					}
					// 计算耐久值
					if (lore.contains(Config.getConfig().getString(Config.NAME_DURABILITY))){
						// 识别物品是否为工具
						if (item.getType().getMaxDurability() > 0){
							if (meta.isUnbreakable()){
								meta.setUnbreakable(false);
							}
							Repairable repairable = (Repairable) meta;
							repairable.setRepairCost(999);
							meta = (ItemMeta) repairable;
							int durability = OnItemDurabilityListener.getDurability(lore);
							int maxDurability = OnItemDurabilityListener.getMaxDurability(lore);
							int maxDefaultDurability = item.getType().getMaxDurability();
							int defaultDurability = (int) ((Double.valueOf(durability)/maxDurability)*maxDefaultDurability);
							item.setDurability((short) (maxDefaultDurability - defaultDurability));
						}
					}
					loreList.set(i, lore);
				}
				// 如果 players 数据存在，并且 Placeholder 开启的情况下，可以代入Placeholder 变量
				if(SXStats.isPlaceholder() && players.length > 0){
					PlaceholderAPI.setPlaceholders(players[0], loreList);
				}
				meta.setLore(loreList);
				item.setItemMeta(meta);
			}
			return item;
		}else {
			return null;
		}
	}
	
	// 更新物品
	public static void updateItem(ItemStack item){
		//判断物品是否有这个nbt
		if (item != null && ItemUtil.isNBT(item, "Name")){
			String dataName = ItemUtil.getNBT(item, "Name");
			// 判断配置内的名称是否相同
			if (ItemDataManager.isItem(dataName)){
				// 获取物品的HashCode
				int hasCode = Integer.valueOf(ItemUtil.getNBT(item, "HasCode"));
				ItemStack dataItem = ItemDataManager.getItem(dataName);
				ItemMeta dataMeta = dataItem.getItemMeta();
				// 获取ItemMap物品的HashCode
				int dataHasCode = Integer.valueOf(ItemUtil.getNBT(dataItem, "HasCode"));
				// 如果两者的原始Lore数据相同
				if (dataHasCode != hasCode){
					// 将物品的HasCode更新到现在的版本
					ItemUtil.setNBT(item, "HasCode", String.valueOf(dataHasCode));
					// 更新时耐久度百分比不变
					List<String> dataItemLore = dataMeta.getLore();
					for (int i = 0;i<dataItemLore.size();i++){
						// 判断是否有耐久度
						if (dataItemLore.get(i).contains(Config.getConfig().getString(Config.NAME_DURABILITY))){
							List<String> itemLoreList = item.getItemMeta().getLore();
							for (String itemLote : itemLoreList){
								//判断原来是否有耐久lore
								if (itemLote.contains(Config.getConfig().getString(Config.NAME_DURABILITY))){
									double maxDefaultDurability = OnItemDurabilityListener.getMaxDurability(itemLote);
									double defaultDurability = OnItemDurabilityListener.getDurability(itemLote);
									String lore = dataItemLore.get(i);
									double maxDurability = OnItemDurabilityListener.getMaxDurability(lore);
									// 根据当前默认耐久百分比，乘以当前RPG最大耐久条得出目前RPG耐久值
									lore = replaceColor(clearColor(lore).replaceFirst(String.valueOf(OnItemDurabilityListener.getDurability(lore)), String.valueOf((int) (defaultDurability/maxDefaultDurability*maxDurability))));
									dataItemLore.set(i, lore);
									dataMeta.setLore(dataItemLore);
									break;
								}
							}
							break;
						}
					}

					// 设置lore及名字
					item.setItemMeta(dataMeta);
					// 设置物品类型
					item.setType(dataItem.getType());
					// 设置物品子ID
					if (dataItem.getType().getMaxDurability() == 0){
						item.setDurability(dataItem.getDurability());
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void saveItem(String itemName,ItemStack itemStack) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemStack.getType();
		String name = null;
		if(itemMeta.hasDisplayName()){
			name = itemMeta.getDisplayName().replace("§", "&");
		}
		String id = String.valueOf(itemStack.getTypeId());
		if (itemStack.getType().getMaxDurability() == 0){
			id += ":"+itemStack.getDurability();
		}
		List<String> lore = itemMeta.getLore();
		if (lore !=null) {
			for (int i=0;i<lore.size();i++){
				lore.set(i, lore.get(i).replace("§", "&"));
			}
		}
		YamlConfiguration itemData = new YamlConfiguration();
		if (itemDefaultFile.exists() && !itemDefaultFile.isDirectory()) {
			try {
				itemData.load(itemDefaultFile);
			} catch (IOException | InvalidConfigurationException e) {
				//
			}
		}
		List<String> enchantList = new ArrayList<>();
		if(itemMeta.hasEnchants()){
			for(Enchantment enchant : itemMeta.getEnchants().keySet()){
				enchantList.add(enchant.getName()+":"+itemMeta.getEnchants().get(enchant));
			}
		}
		List<String> itemFlagList = new ArrayList<>();
		if(itemMeta.getItemFlags().size() > 0){
			for(ItemFlag itemFlag : itemMeta.getItemFlags()){
				itemFlagList.add(itemFlag.name());
			}
		}
		String color = null;
		if(itemMeta instanceof LeatherArmorMeta){
			Color c = ((LeatherArmorMeta) itemMeta).getColor();
			color = c.getRed()+","+c.getGreen()+","+c.getBlue();
		}
		String skullName = null;
		if(itemMeta instanceof SkullMeta){
			skullName = ((SkullMeta) itemMeta).getOwner();
		}
		
		Boolean unbreakable = itemMeta.isUnbreakable();
		itemData.set(itemName+".Name", name);
		itemData.set(itemName+".ID", id);
		if (lore !=null) itemData.set(itemName+".Lore", lore);
		if (enchantList.size() > 0) itemData.set(itemName+".EnchantList", enchantList);
		if (itemFlagList.size() > 0) itemData.set(itemName+".ItemFlagList", itemFlagList);
		itemData.set(itemName+".Unbreakable", unbreakable);
		itemData.set(itemName+".Color", color);
		itemData.set(itemName+".SkullName", skullName);
		saveItemData(itemData);
		loadItemMap();
	}

	//发送物品列表给指令者
	@SuppressWarnings("deprecation")
	public static void sendItemMapToPlayer(CommandSender sender,String... searchs) {
		if (sender instanceof Player){
			sender.sendMessage("§e物品列表§b - §e点击获取");
		}else {
			sender.sendMessage("§e物品列表");
		}
		String search = "";
		if (searchs.length>0){
			search = searchs[0];
			sender.sendMessage("§c正在搜索关键词: "+search);
		}
		int z=1;
		for (String key:itemMap.keySet()) {
			ItemStack item = itemMap.get(key);
			ItemMeta itemMeta = item.getItemMeta();
			String itemName = item.getType().name();
			if (itemMeta.hasDisplayName()) {
				itemName = itemMeta.getDisplayName();
			}
			//搜索功能！
			String str = "§b"+z+" - §a"+key+" §7("+itemName+"§7)";
			if (!str.contains(search)) {
				continue;
			}
			z++;
			List<String> itemLore = itemMeta.getLore();
			if (sender instanceof Player){
				TextComponent message = new TextComponent(str);
				ComponentBuilder bc = new ComponentBuilder(itemName+"§b - "+item.getTypeId()+":"+item.getDurability());
				if (itemLore!=null) {
					for (String lore:itemLore) {
						bc.append("\n"+lore);
					}
				}else {
					bc.append("\n§cN/A");
				}
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , bc.create()) );
				message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sx give "+key));
				((Player) sender).spigot().sendMessage(message);
				
			}else {
				sender.sendMessage(str);
			}
		}
		if (z == 1 && searchs.length > 0){
			sender.sendMessage("§c搜索失败! 请核对关键词!");
		}
	}
}
