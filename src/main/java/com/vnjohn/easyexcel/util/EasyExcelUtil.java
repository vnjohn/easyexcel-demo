package com.vnjohn.easyexcel.util;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import com.vnjohn.easyexcel.entity.ActResultLogDO;
import com.vnjohn.easyexcel.service.IActResultLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author vnjohn
 * @date 2022/7/7
 */
@Slf4j
@Component
public class EasyExcelUtil {
    @Resource
    private IActResultLogService actResultLogService;

    private static final Integer totalCount = 20000;

    /**
     * 导出逻辑代码
     *
     * @param response
     */
    public void dataExport300w(HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            long startTime = System.currentTimeMillis();
            log.info("导出开始时间:{}", startTime);
            outputStream = response.getOutputStream();
            WriteWorkbook writeWorkbook = new WriteWorkbook();
            writeWorkbook.setOutputStream(outputStream);
            writeWorkbook.setExcelType(ExcelTypeEnum.XLSX);
            ExcelWriter writer = new ExcelWriter(writeWorkbook);
            String fileName = new String(("export-excel").getBytes(), StandardCharsets.UTF_8);
            // TODO WriteTable 标题这块可以作为公共的封装起来，基于反射获取属性名称
            WriteTable table = new WriteTable();
            List<List<String>> titles = new ArrayList<List<String>>();
            titles.add(Collections.singletonList("onlineseqid"));
            titles.add(Collections.singletonList("businessid"));
            titles.add(Collections.singletonList("becifno"));
            titles.add(Collections.singletonList("ivisresult"));
            titles.add(Collections.singletonList("createdby"));
            titles.add(Collections.singletonList("createddate"));
            titles.add(Collections.singletonList("updateby"));
            titles.add(Collections.singletonList("updateddate"));
            titles.add(Collections.singletonList("risklevel"));
            table.setHead(titles);
            // 记录总数：实际中需要根据查询条件（过滤数据）进行统计即可，
            // TODO 此处写入限定的条数进行自测
//            Integer totalCount = actResultLogService.count();
//            Integer totalCount = 200 * 10000;
//            Integer totalCount = 20000;
            // 每一个Sheet存放100w条数据
//            Integer sheetDataRows = ExcelConstants.PER_SHEET_ROW_COUNT;
            Integer sheetDataRows = 200000;
            // 每次写入的数据量20w
//            Integer writeDataRows = ExcelConstants.PER_WRITE_ROW_COUNT;
            Integer writeDataRows = 10000;
            // 计算需要的Sheet数量
            int sheetNum = totalCount % sheetDataRows == 0 ? (totalCount / sheetDataRows) : (totalCount / sheetDataRows + 1);
            // 计算一般情况下每一个Sheet需要写入的次数(一般情况不包含最后一个sheet,因为最后一个sheet不确定会写入多少条数据)
            int oneSheetWriteCount = totalCount > sheetDataRows ? sheetDataRows / writeDataRows : totalCount % writeDataRows > 0 ? totalCount / writeDataRows + 1 : totalCount / writeDataRows;
            // 计算最后一个sheet需要写入的次数
            int lastSheetWriteCount = totalCount % sheetDataRows == 0 ? oneSheetWriteCount : (totalCount % sheetDataRows % writeDataRows == 0 ? (totalCount / sheetDataRows / writeDataRows) : (totalCount / sheetDataRows / writeDataRows + 1));

            // 开始分批查询分次写入
            // 注意这次的循环就需要进行嵌套循环了,外层循环是Sheet数目,内层循环是写入次数
            List<List<String>> dataList = new ArrayList<>();
            for (int i = 0; i < sheetNum; i++) {
                //创建Sheet
                WriteSheet sheet = new WriteSheet();
                sheet.setSheetNo(i);
                sheet.setSheetName(fileName + i);
                // 循环写入次数: j的自增条件是当不是最后一个Sheet的时候写入次数为正常的每个Sheet写入的次数,如果是最后一个就需要使用计算的次数lastSheetWriteCount
                for (int j = 0; j < (i != sheetNum - 1 || i==0 ? oneSheetWriteCount : lastSheetWriteCount); j++) {
                    // 集合复用,便于GC清理
                    dataList.clear();
                    // 分页查询一次20w
                    List<ActResultLogDO> resultList = actResultLogService.findByPage100w(j + 1 + oneSheetWriteCount * i, writeDataRows);
                    if (!CollectionUtils.isEmpty(resultList)) {
                        resultList.forEach(item -> {
                            dataList.add(Arrays.asList(item.getOnlineseqid(), item.getBusinessid(), item.getBecifno(), item.getIvisresult(), item.getCreatedby(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), item.getUpdateby(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), item.getRisklevel()));
                        });
                    }
                    // 写数据
                    writer.write(dataList, sheet, table);
                }
            }

            // 下载EXCEL 以下代码可以作为公共的进行封装.
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes("gb2312"), StandardCharsets.ISO_8859_1) + ".xlsx");
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            writer.finish();
            outputStream.flush();
            // 导出时间结束
            long endTime = System.currentTimeMillis();
            log.info("导出结束时间:{}", endTime + "ms");
            log.info("导出所用时间:{}", (endTime - startTime) / 1000 + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void dataImport300w(HttpServletResponse response) {

    }

    public static void main(String[] args) {
        Integer totalCount = 20000;
        // 每一个Sheet存放100w条数据
        Integer sheetDataRows = 200000;
        // 每次写入的数据量20w
        Integer writeDataRows = 10000;
        // 计算需要的Sheet数量
        int sheetNum = 1;
        // 计算一般情况下每一个Sheet需要写入的次数(一般情况不包含最后一个sheet,因为最后一个sheet不确定会写入多少条数据)
        int oneSheetWriteCount = sheetDataRows / writeDataRows;
        // 计算最后一个sheet需要写入的次数
        int lastSheetWriteCount = totalCount % sheetDataRows % writeDataRows == 0 ? totalCount / sheetDataRows / writeDataRows : totalCount / sheetDataRows / writeDataRows + 1;
        System.out.println(oneSheetWriteCount);
        System.out.println(lastSheetWriteCount);
    }
}
