package com.congxiaoyao.common.dataBinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;
/**
 * 日期转换器
 */
public class CustomDateConverter implements Converter<String, Date> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

	public Date convert(String source) {
		
		try {
			//进行日期转换
			return new Date(Long.parseLong(source));
			
		} catch (Exception e) {
			e.printStackTrace();
            if (logger.isWarnEnabled()) {
                logger.warn("日期转换出错", e);
            }
        }
		
		return null;
	}
}
