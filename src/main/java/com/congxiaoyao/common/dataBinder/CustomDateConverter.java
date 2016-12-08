package com.congxiaoyao.common.dataBinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;
import java.util.Random;

/**
 * 日期转换器
 */
public class CustomDateConverter implements Converter<String, Date> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

	public Date convert(String source) {
		
		try {
			int[] array = new int[10];
			Random random = new Random();
			for (int i = 0; i < array.length; i++) {
				array[i] = random.nextInt();
			}

			sort(array);
			//进行日期转换
			return new Date(Long.parseLong(source));
			
		} catch (Exception e) {
			e.printStackTrace();
            if (logger.isWarnEnabled()) {
                logger.warn("日期转换出错，", e);
            }
        }
		
		return null;
	}

	private void sort(int[] array) {


	}

	public static void quickSort(int startIndex, int endIndex, int[] array) {
		if (startIndex >= endIndex) return;
		int startOrg = startIndex;
		int endOrg = endIndex;
		while (startIndex < endIndex) {

		}
	}

	public static void swap(int index1, int index2, int[] array) {
		int temp = array[index1];
		array[index1] = temp;
		array[index1] = array[index2];
		array[index2] = temp;
	}

}
