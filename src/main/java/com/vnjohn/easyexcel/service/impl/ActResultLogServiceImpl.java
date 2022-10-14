package com.vnjohn.easyexcel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vnjohn.easyexcel.entity.ActResultLogDO;
import com.vnjohn.easyexcel.mapper.ActResultLogMapper;
import com.vnjohn.easyexcel.service.IActResultLogService;
import com.vnjohn.easyexcel.util.JDBCDruidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author <a href="https://www.vnjohn.com">vnjohn</a>
 * @since 2022-07-07
 */
@Slf4j
@Service
public class ActResultLogServiceImpl extends ServiceImpl<ActResultLogMapper, ActResultLogDO> implements IActResultLogService {

    @Override
    public List<ActResultLogDO> findByPage100w(Integer pageNum, Integer pageSize) {
        PageInfo<ActResultLogDO> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            QueryWrapper<ActResultLogDO> wrapper = new QueryWrapper<>();
            // TODO 此处可以进行添加条件过滤
            baseMapper.selectList(wrapper);
        });
        return pageInfo.getList();
    }


    // Service中具体业务逻辑

    /**
     * 测试用Excel导入超过10w条数据,经过测试发现,使用Mybatis的批量插入速度非常慢,所以这里可以使用 数据分批+JDBC分批插入+事务来继续插入速度会非常快
     */
    @Override
    @Transactional
    public Map<String, Object> import2DBFromExcel10wByJDBC(List<Map<Integer, String>> dataList) {
        Map<String, Object> result = new HashMap<>();
        // 结果集中数据为0时,结束方法.进行下一次调用
        if (dataList.size() == 0) {
            result.put("empty", "0000");
            return result;
        }
        // JDBC分批插入+事务操作完成对10w数据的插入
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long startTime = System.currentTimeMillis();
            log.info("{} 条,开始导入到数据库时间:{}", dataList.size(), startTime + "ms");
            conn = JDBCDruidUtils.getConnection();
            // 控制事务:默认不提交
            conn.setAutoCommit(false);
            String sql = "insert into ACT_RESULT_LOG (onlineseqid,businessid,becifno,ivisresult,createdby,createddate,updateby,updateddate,risklevel) values";
            sql += "(?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            // 循环结果集:这里循环不支持"烂布袋"表达式
            for (int i = 0; i < dataList.size(); i++) {
                Map<Integer, String> item = dataList.get(i);
                ps.setString(1, item.get(0));
                ps.setString(2, item.get(1));
                ps.setString(3, item.get(2));
                ps.setString(4, item.get(3));
                ps.setString(5, item.get(4));
                ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                ps.setString(7, item.get(6));
                ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
                ps.setString(9, item.get(8));
                // 将一组参数添加到此 PreparedStatement 对象的批处理命令中。
                ps.addBatch();
            }
            // 执行批处理
            ps.executeBatch();
            // 手动提交事务
            conn.commit();
            long endTime = System.currentTimeMillis();
            log.info("{} 条,结束导入到数据库时间:{}", dataList.size(), endTime + "ms");
            log.info("{} 条,导入用时:{}", dataList.size(), (endTime - startTime) + "ms");
            result.put("success", "1111");
        } catch (Exception e) {
            result.put("exception", "0000");
            e.printStackTrace();
        } finally {
            // 关连接
            JDBCDruidUtils.close(conn, ps);
        }
        return result;
    }


    // 采用 mapper 编写 SQL 语句进行测试，效率明显比原生 JDBC 要高
    @Override
    public void import2DBFromExcel10wByMybatis(List<ActResultLogDO> actResultLogList) {
        baseMapper.importToDb(actResultLogList);
    }
}
