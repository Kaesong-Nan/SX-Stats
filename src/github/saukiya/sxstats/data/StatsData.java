package github.saukiya.sxstats.data;

import lombok.Setter;

import java.util.Random;

import github.saukiya.sxstats.util.Config;
import lombok.Getter;

public class StatsData{
	@Getter @Setter private double expAddition = 0D;//经验加成
	@Getter @Setter private double speed = 0D;//速度
	
	@Getter @Setter private double health = 0D;//血量
	@Getter @Setter private double healthRegen = 0D;//每秒回血
	@Getter @Setter private double dodge = 0D;//闪避几率
	@Getter @Setter private double minDefense = 0D;//最低防御力
	@Getter @Setter private double maxDefense = 0D;//最高防御力
	@Getter @Setter private double pvpMinDefense = 0D;//PVP最低防御力
	@Getter @Setter private double pvpMaxDefense = 0D;//PVP最高防御力
	@Getter @Setter private double pveMinDefense = 0D;//PVE最低防御力
	@Getter @Setter private double pveMaxDefense = 0D;//PVE最高防御力
	@Getter @Setter private double toughness = 0D;//韧性 抵消效果
	@Getter @Setter private double reflectionRate = 0D;//反射几率
	@Getter @Setter private double reflection = 0D;//反射伤害
	@Getter @Setter private double blockRate = 0D;//格挡几率
	@Getter @Setter private double block = 0D;//格挡伤害
	@Getter @Setter private double minDamage = 0D;//最低攻击力
	@Getter @Setter private double maxDamage = 0D;//最高攻击力
	@Getter @Setter private double pvpMinDamage = 0D;//PVP最低攻击力
	@Getter @Setter private double pvpMaxDamage = 0D;//PVP最高攻击力
	@Getter @Setter private double pveMinDamage = 0D;//PVE最低攻击力
	@Getter @Setter private double pveMaxDamage = 0D;//PVE最高攻击力
	@Getter @Setter private double hitRate = 0D;//命中几率 抵消闪避几率
	@Getter @Setter private double real = 0D;//破甲几率 无视对放所有护甲效果
	@Getter @Setter private double critRate = 0D;//暴击几率
	@Getter @Setter private double critDamage = 0D;//暴击伤害
	@Getter @Setter private double lifeSteal = 0D;//生命吸取
	@Getter @Setter private double ignition = 0D;//点燃几率
	@Getter @Setter private double wither = 0D;//凋零几率
	@Getter @Setter private double poison = 0D;//中毒几率
	@Getter @Setter private double blindness = 0D;//失明几率
	@Getter @Setter private double slowness = 0D;//缓慢几率
	@Getter @Setter private double lightning = 0D;//雷霆几率
	@Getter @Setter private double tearing = 0D;//撕裂几率
	@Getter private double value = 0D;//点数
	
	public double getDamage(){
		return this.minDamage + new Random().nextDouble()*(this.maxDamage - this.minDamage);
	}
	public double getPVPDamage(){
		return this.pvpMinDamage + new Random().nextDouble()*(this.pvpMaxDamage - this.pvpMinDamage);
	}
	public double getPVEDamage(){
		return this.pveMinDamage + new Random().nextDouble()*(this.pveMaxDamage - this.pveMinDamage);
	}

	public double getDefense(){
		return this.minDefense + new Random().nextDouble()*(this.maxDefense - this.minDefense);
	}
	public double getPVPDefense(){
		return this.pvpMinDefense + new Random().nextDouble()*(this.pvpMaxDefense - this.pvpMinDefense);
	}
	public double getPVEDefense(){
		return this.pveMinDefense + new Random().nextDouble()*(this.pveMaxDefense - this.pveMinDefense);
	}
	
	//纠正错误的属性 几率不能为负数 有些几率不能大于100%
	public StatsData correct(){
		if (this.expAddition <= 0) this.expAddition = 0D;
		if (this.speed <= 0) this.speed = 0D;
		if (this.health <= 0) this.health = 1D;
		if (this.healthRegen < 0) this.healthRegen = 0D;
		if (this.dodge < 0) this.dodge = 0D;
		if (this.maxDefense < this.minDefense) this.maxDefense = this.minDefense;
		if (this.pvpMaxDefense < this.pvpMinDefense) this.pvpMaxDefense = this.pvpMinDefense;
		if (this.pveMaxDefense < this.pveMinDefense) this.pveMaxDefense = this.pveMinDefense;
		if (this.toughness < 0) this.toughness = 0D;
		if (this.reflectionRate < 0) this.reflectionRate = 0D;
		if (this.reflection < 0) this.reflection = 0D;
		if (this.blockRate < 0) this.blockRate = 0D;
		if (this.block < 0) this.block = 0D;
		if (Config.isDamageGauges() && this.minDamage <= 0){
			this.minDamage = 1;
		}
		if (this.maxDamage < this.minDamage) this.maxDamage = this.minDamage;
		if (this.pvpMaxDamage < this.pvpMinDamage) this.pvpMaxDamage = this.pvpMinDamage;
		if (this.pveMaxDamage < this.pveMinDamage) this.pveMaxDamage = this.pveMinDamage;
		if (this.hitRate < 0) this.hitRate = 0D;
		if (this.real < 0) this.real = 0D;
		if (this.critRate < 0) this.critRate = 0D;
		if (this.critDamage < 0) this.critDamage = 0D;
		if (this.lifeSteal < 0) this.lifeSteal = 0D;
		if (this.ignition < 0) this.ignition = 0D;
		if (this.wither < 0) this.wither = 0D;
		if (this.poison < 0) this.poison = 0D;
		if (this.blindness < 0) this.blindness = 0D;
		if (this.slowness < 0) this.slowness = 0D;
		if (this.lightning < 0) this.lightning = 0D;
		if (this.tearing < 0) this.tearing = 0D;
		return this;
	}
	
	
	public StatsData add(StatsData data){
		if(data == null){
			return this;
		}
		this.expAddition += data.expAddition;
		this.speed += data.speed;

		this.health += data.health;
		this.healthRegen += data.healthRegen;
		this.dodge += data.dodge;
		this.minDefense += data.minDefense;
		this.maxDefense += data.maxDefense;
		this.pvpMinDefense += data.pvpMinDefense;
		this.pvpMaxDefense += data.pvpMaxDefense;
		this.pveMinDefense += data.pveMinDefense;
		this.pveMaxDefense += data.pveMaxDefense;
		this.toughness += data.toughness;
		this.reflectionRate += data.reflectionRate;
		this.reflection += data.reflection;
		this.blockRate += data.blockRate;
		this.block += data.block;
		
		this.minDamage += data.minDamage;
		this.maxDamage += data.maxDamage;
		this.pvpMinDamage += data.pvpMinDamage;
		this.pvpMaxDamage += data.pvpMaxDamage;
		this.pveMinDamage += data.pveMinDamage;
		this.pveMaxDamage += data.pveMaxDamage;
		this.hitRate += data.hitRate;
		this.real += data.real;
		this.critRate += data.critRate;
		this.critDamage += data.critDamage;
		this.lifeSteal += data.lifeSteal;
		this.ignition += data.ignition;
		this.wither += data.wither;
		this.poison += data.poison;
		this.blindness += data.blindness;
		this.slowness += data.slowness;
		this.lightning += data.lightning;
		this.tearing += data.tearing;

		return this;
	}

	
	public double value(){
		this.value = 0D;
		this.value += this.expAddition*Config.getConfig().getInt(Config.VALUE_EXP_ADDITION);
		this.value += this.speed*Config.getConfig().getInt(Config.VALUE_SPEED);
		this.value += this.health*Config.getConfig().getInt(Config.VALUE_HEALTH);
		this.value += this.healthRegen*Config.getConfig().getInt(Config.VALUE_HEALTH_REGEN);
		this.value += this.dodge*Config.getConfig().getInt(Config.VALUE_DODGE);
		this.value += (this.minDefense+this.maxDefense)/2*Config.getConfig().getInt(Config.VALUE_DEFENSE);
		this.value += (this.pvpMinDefense+this.pvpMaxDefense)/2*Config.getConfig().getInt(Config.VALUE_PVP_DEFENSE);
		this.value += (this.pveMinDefense+pveMaxDefense)/2*Config.getConfig().getInt(Config.VALUE_PVE_DEFENSE);
		this.value += this.toughness*Config.getConfig().getInt(Config.VALUE_TOUGHNESS);
		this.value += this.reflectionRate*Config.getConfig().getInt(Config.VALUE_REFLECTION_RATE);
		this.value += this.reflection*Config.getConfig().getInt(Config.VALUE_REFLECTION);
		this.value += this.blockRate*Config.getConfig().getInt(Config.VALUE_BLOCK_RATE);
		this.value += this.block*Config.getConfig().getInt(Config.VALUE_BLOCK);
		this.value += (this.minDamage+this.maxDamage)/2*Config.getConfig().getInt(Config.VALUE_DAMAGE);
		this.value += (this.pvpMinDamage+this.pvpMaxDamage)/2*Config.getConfig().getInt(Config.VALUE_PVP_DAMAGE);
		this.value += (this.pveMinDamage+this.pveMaxDamage)/2*Config.getConfig().getInt(Config.VALUE_PVE_DAMAGE);
		this.value += this.hitRate*Config.getConfig().getInt(Config.VALUE_HIT_RATE);
		this.value += this.real*Config.getConfig().getInt(Config.VALUE_REAL);
		this.value += this.critRate*Config.getConfig().getInt(Config.VALUE_CRIT_RATE);
		this.value += this.critDamage*Config.getConfig().getInt(Config.VALUE_CRIT_DAMAGE);
		this.value += this.lifeSteal*Config.getConfig().getInt(Config.VALUE_LIFE_STEAL);
		this.value += this.ignition*Config.getConfig().getInt(Config.VALUE_IGNITION);
		this.value += this.wither*Config.getConfig().getInt(Config.VALUE_WITHER);
		this.value += this.poison*Config.getConfig().getInt(Config.VALUE_POISON);
		this.value += this.blindness*Config.getConfig().getInt(Config.VALUE_BLINDNESS);
		this.value += this.slowness*Config.getConfig().getInt(Config.VALUE_SLOWNESS);
		this.value += this.lightning*Config.getConfig().getInt(Config.VALUE_LIGHTNING);
		this.value += this.tearing*Config.getConfig().getInt(Config.VALUE_TEARING);
		return this.value;
	}
	
}
