package com.congxiaoyao.auth.dao;

import com.congxiaoyao.auth.pojo.StatelessSession;
import com.congxiaoyao.auth.pojo.StatelessSessionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StatelessSessionMapper {
    long countByExample(StatelessSessionExample example);

    int deleteByExample(StatelessSessionExample example);

    int deleteByPrimaryKey(String clientId);

    int insert(StatelessSession record);

    int insertSelective(StatelessSession record);

    List<StatelessSession> selectByExample(StatelessSessionExample example);

    StatelessSession selectByPrimaryKey(String clientId);

    int updateByExampleSelective(@Param("record") StatelessSession record, @Param("example") StatelessSessionExample example);

    int updateByExample(@Param("record") StatelessSession record, @Param("example") StatelessSessionExample example);

    int updateByPrimaryKeySelective(StatelessSession record);

    int updateByPrimaryKey(StatelessSession record);
}