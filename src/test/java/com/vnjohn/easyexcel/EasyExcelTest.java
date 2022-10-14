package com.vnjohn.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.vnjohn.easyexcel.listener.EasyExcelGeneralDataJDBCListener;
import com.vnjohn.easyexcel.service.IActResultLogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;

/**
 * @author vnjohn
 * @date 2022/7/7
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasyexcelDemoApplication.class)
public class EasyExcelTest {
    @Resource
    private IActResultLogService actResultLogService;

    // EasyExcel的读取Excel数据的API
    @Test
    public void import2DBFromExcel10wTest() {
        String fileName = "/Users/vnjohn/Downloads/export-excel.xlsx";
        // 记录开始读取Excel时间,也是导入程序开始时间
        long startReadTime = System.currentTimeMillis();
        log.info("------开始读取Excel的Sheet时间(包括导入数据过程):" + startReadTime + " ms------");
        // 读取所有Sheet的数据.每次读完一个Sheet就会调用这个方法
//        EasyExcel.read(fileName, ActResultLogVO.class, new EasyExcelGeneralDataMybatisListener(actResultLogService))
//                .registerConverter(new EasyExcelLocalDateConvert()).doReadAll();
        EasyExcel.read(fileName, new EasyExcelGeneralDataJDBCListener(actResultLogService)).doReadAll();
        long endReadTime = System.currentTimeMillis();
        log.info("------结束读取Excel的Sheet时间(包括导入数据过程):" + endReadTime + " ms------");
        log.info("------导入总花费时长：{}", ((endReadTime - startReadTime) / 1000) + "s------");
    }

    public static void main(String[] args) {
        System.out.println(1657257794753L - 378000L);
    }

}
