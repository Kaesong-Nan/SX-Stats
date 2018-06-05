package github.saukiya.sxstats.data;

import lombok.Getter;

/**
 * @author Saukiya
 * @since 2018年6月4日
 */

public class NameData {
	
	@Getter private String name;
	@Getter private boolean visible;
	
	/**
	 * 头顶血条显示数据
	 * @param name
	 * @param visible
	 */
	public NameData (String name, boolean visible){
		this.name = name;
		this.visible = visible;
	}
}
