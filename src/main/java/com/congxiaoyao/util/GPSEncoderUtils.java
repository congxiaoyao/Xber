package com.congxiaoyao.util;

import java.util.Base64;

/**
 * Created by Jaycejia on 2017/2/11.
 */
public class GPSEncoderUtils {

    public static byte[] encode(byte[] src) {
        return Base128.encode(src);
    }

    public static byte[] decode(byte[] src) {
        return Base128.decode(src);
    }
}
