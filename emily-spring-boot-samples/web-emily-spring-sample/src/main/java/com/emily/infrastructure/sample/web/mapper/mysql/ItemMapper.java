package com.emily.infrastructure.sample.web.mapper.mysql;

import com.emily.infrastructure.sample.web.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Emily
 * @program: spring-parent
 * @since 2021/09/08
 */
@Mapper
public interface ItemMapper {
    int inertByBatch(@Param("list") List<Item> list);

    int insertItem(@Param("scheName") String scheName, @Param("lockName") String lockName);
}
