package com.congxiaoyao.location.dao;

/**
 * Created by congxiaoyao on 2017/4/2.
 */
public interface LatLngMapper<T> {

    double getLat(T t);

    double getLng(T t);

    T toObject(double lat, double lng);
}
