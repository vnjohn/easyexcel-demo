package com.vnjohn.easyexcel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vnjohn.easyexcel.entity.ActResultLogDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author <a href="https://www.vnjohn.com">vnjohn</a>
 * @since 2022-07-07
 */
@Repository
public interface ActResultLogMapper extends BaseMapper<ActResultLogDO> {

    void importToDb(List<ActResultLogDO> actResultLogList);
}
