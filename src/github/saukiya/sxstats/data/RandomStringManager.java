package github.saukiya.sxstats.data;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import github.saukiya.sxstats.SXStats;

/**
 * @author Saukiya
 * @since 2018年5月3日
 */

public class RandomStringManager {
	final static File randomFiles = new File("plugins" + File.separator + SXStats.getPlugin().getName() + File.separator + "Random");
	final static File randomFile1 = new File("plugins" + File.separator + SXStats.getPlugin().getName() + File.separator + "Random"+ File.separator + "普通随机.yml");
	final static File randomFile2 = new File("plugins" + File.separator + SXStats.getPlugin().getName() + File.separator + "Random"+ File.separator + "10级"+ File.separator+"普通-优秀-史诗.yml");

	private static Map<String,List<String>> randomMap = new HashMap<>();
	
	public static List<String> getRandomList(String name){
		return randomMap.get(name);
	}
	
	
	public static String getRandomString(String name){
		List<String> randomList = randomMap.get(name);
		if(randomList != null){
			String str1 = randomList.get(new Random().nextInt(randomList.size()));
			List<String> replaceStringList = ItemDataManager.getStringList("<s:",">",str1);
			for(String str2 : replaceStringList){
				if(str1.equals(str2)){
			        Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §c请不要造成无限循环 本插件不承担相应责任!");
					continue;
				}
				str1 = str1.replace("<s:"+str2+">", getRandomString(str2));
			}
			return str1;
		}
		return null;
	}
	
	public static void loadRandomMap() {
		randomMap.clear();
		if (!randomFiles.exists() || randomFiles.listFiles().length == 0) {
			createRandom();
		}
		loadRandom(randomFiles);
        Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §a已读取 §b"+ randomMap.size() + " §a个随机字符串组");
	}
	
	public static void loadRandom(File files){
		for (File file: files.listFiles()){
			if (file.isDirectory()){
				loadRandom(file);
			}
			else {
		        YamlConfiguration yaml = new YamlConfiguration();
				try {yaml.load(file);} catch (IOException | InvalidConfigurationException e) {e.printStackTrace();Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §c读取 "+file.getName().replace("plugins"+File.separator+SXStats.getPlugin().getName()+File.separator,"")+" 随机字符串时发生错误");}
				for (String name : yaml.getKeys(false)){
					if (randomMap.containsKey(name)){
				        Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §c不要随机字符串组名:§b "+ file.getName().replace("plugins"+File.separator+SXStats.getPlugin().getName()+File.separator,"")+File.separator+name+ "§c !");
					}
					if(yaml.get(name) instanceof String){
						randomMap.put(name, Arrays.asList(yaml.getString(name)));
					}else if(yaml.get(name) instanceof List) {
						List<String> list = yaml.getStringList(name);
						randomMap.put(name, list);
					}
				}
			}
		}
	}
	
	public static void createRandom(){
        YamlConfiguration yaml = new YamlConfiguration();
		yaml.set("DefaultLore", Arrays.asList("&7&o他是由什么材质做成的呢?","&7&o握着它，有不好的预感呢","&7&o据说夜幕曾经带它攻略沙场"));
		yaml.set("DefaultPrefix", Arrays.asList("&c令人兴奋之","&c煞胁之","&e兴趣使然之"));
		yaml.set("DefaultSuffix", Arrays.asList("&e淦","&e武","&e衡"));
		yaml.set("Quality", Arrays.asList("普通","优秀","史诗"));
		yaml.set("普通Color", "&7");
		yaml.set("优秀Color", "&a");
		yaml.set("史诗Color", "&5");
		yaml.set("好看Color", Arrays.asList("&a","&b","&c","&4","&d","&1","&3","&9"));
		yaml.set("好丑Color", Arrays.asList("&1","&8","&7","&5","&3","&2"));
		yaml.set("攻随一", Arrays.asList("攻击速度","暴击几率"));
		yaml.set("攻随二", Arrays.asList("点燃几率","暴击伤害","暴击伤害"));
		yaml.set("攻随三", Arrays.asList("命中几率","失明几率","缓慢几率","凋零几率"));
		yaml.set("攻随四", Arrays.asList("雷霆几率","破甲几率","撕裂几率"));
		yaml.set("防随一", Arrays.asList("反射几率","格挡几率","韧性","速度"));
		yaml.set("防随二", Arrays.asList("反射伤害","格挡伤害","闪避几率"));
		yaml.set("防随三", Arrays.asList("生命恢复","生命上限","PVP防御力","PVE防御力"));
		try {
			yaml.save(randomFile1);
			yaml = new YamlConfiguration();
		} catch (IOException e) {
			e.printStackTrace();
		}
		yaml.set("普通攻击-10", "20 - 30");
		yaml.set("优秀攻击-10", "25 - 35");
		yaml.set("史诗攻击-10", "30 - 40");
		yaml.set("普通等级-10", "10");
		yaml.set("优秀等级-10", "13");
		yaml.set("史诗等级-10", "15");
		yaml.set("普通攻一-10", "<s:好丑Color><s:攻随一>: +<r:10_30>%");
		yaml.set("普通攻二-10", "<s:好丑Color><s:攻随二>: +<r:0_7>.<r:0_99>%");
		yaml.set("普通攻三-10", Arrays.asList("<s:好丑Color><s:攻随三>: +<r:0_7>.<r:0_99>%","%DeleteLore%"));
		yaml.set("普通攻四-10", Arrays.asList("<s:好丑Color><s:攻随四>: +<r:0_4>.<r:0_99>%","%DeleteLore%","%DeleteLore%"));
		yaml.set("优秀攻一-10", "<s:好丑Color><s:攻随一>: +<r:20_40>%");
		yaml.set("优秀攻二-10", "<s:好看Color><s:攻随二>: +<r:8_15>.<r:0_99>%");
		yaml.set("优秀攻三-10", "<s:好丑Color><s:攻随三>: +<r:8_15>.<r:0_99>%");
		yaml.set("优秀攻四-10", Arrays.asList("<s:好看Color><s:攻随四>: +<r:5_9>.<r:0_99>%","%DeleteLore%"));
		yaml.set("史诗攻一-10", "<s:好看Color><s:攻随一>: +<r:30_50>%");
		yaml.set("史诗攻二-10", "<s:好看Color><s:攻随二>: +<r:16_23>.<r:0_99>%");
		yaml.set("史诗攻三-10", "<s:好看Color><s:攻随三>: +<r:16_23>.<r:0_99>%");
		yaml.set("史诗攻四-10", "<s:好看Color><s:攻随四>: +<r:10_14>.<r:0_99>%");
		yaml.set("普通防御-10", "10 - 15");
		yaml.set("优秀防御-10", "13 - 18");
		yaml.set("史诗防御-10", "16 - 21");
		try {
			yaml.save(randomFile2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
