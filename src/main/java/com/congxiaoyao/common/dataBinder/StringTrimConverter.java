package com.congxiaoyao.common.dataBinder;

import org.springframework.core.convert.converter.Converter;

/**
 * 数据去除掉两边的空格(Trim)
 */
public class StringTrimConverter implements Converter<String, String> {

	public String convert(String source) {
		
		try {
			//去掉字符串两边空格，如果去除后为空设置为null
			if(source!=null){
				source = source.trim();
				if(source.equals("")){
					return null;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return source;
	}

}
