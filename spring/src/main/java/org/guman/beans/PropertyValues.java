package org.guman.beans;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 封装一个对象所有的PropertyValue,以集合方式存储
 * @author duanhaoran
 * @since 2019/3/10 2:29 PM
 */
@NoArgsConstructor
@Getter
public class PropertyValues {

	private final List<PropertyValue> propertyValueList = Lists.newArrayList();

	public void addPropertyValue(PropertyValue pv) {
		this.propertyValueList.add(pv);
	}

}