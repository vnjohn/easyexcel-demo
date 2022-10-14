package com.vnjohn.easyexcel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vnjohn.easyexcel.entity.ActResultLogDO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author <a href="https://www.vnjohn.com">vnjohn</a>
 * @since 2022-07-07
 */
public interface IActResultLogService extends IService<ActResultLogDO> {
    /**
     * 通过分页参数查询一百w数据
     * @return
     */
    List<ActResultLogDO> findByPage100w(Integer pageNum,Integer pageSize);

    /**
     * 从 Excel 导入数据，批次为 10w，通过 JDBC
     * @param dataList
     * @return
     */
    Map<String, Object> import2DBFromExcel10wByJDBC(List<Map<Integer, String>> dataList);

    /**
     * 从 Excel 导入数据，批次为 10W，通过 MyBatis
     * @param actResultLogList
     */
    void import2DBFromExcel10wByMybatis(List<ActResultLogDO> actResultLogList);
}
