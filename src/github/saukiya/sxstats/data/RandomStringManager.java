package github.saukiya.sxstats.data;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import github.saukiya.sxstats.SXStats;

/**
 * @author Saukiya
 * @since 2018年5月3日
 */

public class RandomStringManager {
	final static File randomFiles = new File("plugins" + File.separator + SXStats.getPlugin().getName() + File.separator + "Random.yml");

	private static Map<String,List<String>> randomMap = new HashMap<>();
	
	public static List<String> getRandomList(String name){
		return randomMap.get(name);
	}
	
	public static void loadRandom(){
		if (!randomFiles.exists()){
			createRandom();
		}
        YamlConfiguration yaml = new YamlConfiguration();
		try {yaml.load(randomFiles);} catch (IOException | InvalidConfigurationException e) {e.printStackTrace();Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §c读取random时发生错误");}
		randomMap.clear();
		for (String name : yaml.getKeys(false)){
			List<String> list = yaml.getStringList(name);
			if(list != null){
				randomMap.put(name, list);
			}
		}
        Bukkit.getConsoleSender().sendMessage("["+SXStats.getPlugin().getName()+"] §a已读取 §b"+ randomMap.size() + " §a个随机字符串组");
	}
	
	public static void createRandom(){
        YamlConfiguration yaml = new YamlConfiguration();
		yaml.set("DefaultLore", Arrays.asList("&7&o他是由什么材质做成的呢?","&7&o握着它，有不好的预感呢","&7&o据说夜幕曾经带它攻略沙场"));
		yaml.set("DefaultPrefix", Arrays.asList("&c令人兴奋之","&c煞胁之","&e兴趣使然之"));
		yaml.set("DefaultSuffix", Arrays.asList("&e淦","&e武","&e衡"));
		try {
			yaml.save(randomFiles);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
