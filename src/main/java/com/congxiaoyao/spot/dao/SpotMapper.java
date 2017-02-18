package com.congxiaoyao.spot.dao;

import com.congxiaoyao.spot.pojo.Spot;
import com.congxiaoyao.spot.pojo.SpotExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SpotMapper {
    long countByExample(SpotExample example);

    int deleteByExample(SpotExample example);

    int deleteByPrimaryKey(Long spotId);

    int insert(Spot record);

    int insertSelective(Spot record);

    List<Spot> selectByExample(SpotExample example);

    Spot selectByPrimaryKey(Long spotId);

    int updateByExampleSelective(@Param("record") Spot record, @Param("example") SpotExample example);

    int updateByExample(@Param("record") Spot record, @Param("example") SpotExample example);

    int updateByPrimaryKeySelective(Spot record);

    int updateByPrimaryKey(Spot record);
}