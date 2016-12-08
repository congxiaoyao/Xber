package com.congxiaoyao.common.dataBinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
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
				array[i] = random.nextInt(100);
			}
			sort(array);
			Arrays.stream(array).forEach(System.out::println);

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

	private void sort(int[] array) {
//		quickSort(0, array.length - 1, array);
		Arrays.sort(array);
	}

	public static void quickSort(int startIndex, int endIndex, int[] array) {
		if (startIndex >= endIndex) return;
		int startOrg = startIndex;
		int endOrg = endIndex;
		while (startIndex < endIndex) {
			while (startIndex < endIndex) {
				if (array[startIndex] > array[endIndex]) {
					break;
				}
				startIndex++;
			}
			swap(startIndex, endIndex, array);
			while (startIndex < endIndex) {
				if (array[startIndex] > array[endIndex]) {
					break;
				}
				endIndex--;
			}
			swap(startIndex, endIndex, array);
		}
		quickSort(startOrg, startIndex - 1, array);
		quickSort(endIndex + 1, endOrg, array);
	}

	public static void swap(int index1, int index2, int[] array) {
		int temp = array[index1];
		array[index1] = temp;
		array[index1] = array[index2];
		array[index2] = temp;
	}

	public static void main(String[] args) {
		new CustomDateConverter().convert(System.currentTimeMillis() + "");
	}

}
