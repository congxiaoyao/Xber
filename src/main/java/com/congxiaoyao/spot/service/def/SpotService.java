package com.congxiaoyao.spot.service.def;

import com.congxiaoyao.spot.pojo.Spot;

import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
public interface SpotService {
    /**
     * 根据spot Id 获取spot
     * @param spotId
     * @return
     */
    Spot getSpotById(Long spotId);

    /**
     * 获取所有的地点
     * @return
     */
    List<Spot> getSpots();
}