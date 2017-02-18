package com.congxiaoyao.spot.service.impl;

import com.congxiaoyao.spot.dao.SpotMapper;
import com.congxiaoyao.spot.pojo.Spot;
import com.congxiaoyao.spot.pojo.SpotExample;
import com.congxiaoyao.spot.service.def.SpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
@Transactional
@Service
public class SpotServiceImpl implements SpotService{
    @Autowired
    private SpotMapper spotMapper;
    /**
     * 根据spot Id 获取spot
     *
     * @param spotId
     * @return
     */
    @Override
    public Spot getSpotById(Long spotId) {
        return spotMapper.selectByPrimaryKey(spotId);
    }

    /**
     * 获取所有的地点
     *
     * @return
     */
    @Override
    public List<Spot> getSpots() {
        return spotMapper.selectByExample(new SpotExample());
    }
}