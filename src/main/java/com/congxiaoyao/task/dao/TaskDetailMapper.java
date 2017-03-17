package com.congxiaoyao.task.dao;

import com.congxiaoyao.task.pojo.Task;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
public interface TaskDetailMapper {
    List<Task> selectByCondition(@Param("userId") Long userId,
                                 @Param("startIndex") Integer startIndex,
                                 @Param("pageSize") Integer pageSize,
                                 @Param("status") Integer status,
                                 @Param("timestamp") Date timestamp,
                                 @Param("createUserId") Long createUserId);

    Long countByCondition(@Param("userId") Long userId,
                          @Param("status") Integer status,
                          @Param("timestamp") Date timestamp,
                          @Param("createUserId") Long createUserId);
}
