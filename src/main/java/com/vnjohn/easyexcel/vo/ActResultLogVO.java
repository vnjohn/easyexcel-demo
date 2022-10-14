package com.vnjohn.easyexcel.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 *  excel 导入/导出演示实体
 * </p>
 *
 * @author <a href="https://www.vnjohn.com">vnjohn</a>
 * @since 2022-07-07
 */
@Data
public class ActResultLogVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "onlineseqid",index = 0)
    private String onlineseqid;

    @ExcelProperty(value = "businessid",index = 1)
    private String businessid;

    @ExcelProperty(value = "becifno",index = 2)
    private String becifno;

    @ExcelProperty(value = "ivisresult",index = 3)
    private String ivisresult;

    @ExcelProperty(value = "createdby",index = 4)
    private String createdby;

    @ExcelProperty(value = "createddate",index = 5)
    private LocalDate createddate;

    @ExcelProperty(value = "updateby",index = 6)
    private String updateby;

    @ExcelProperty(value = "updateddate",index = 7)
    private LocalDate updateddate;

    @ExcelProperty(value = "risklevel",index = 8)
    private String risklevel;
}
