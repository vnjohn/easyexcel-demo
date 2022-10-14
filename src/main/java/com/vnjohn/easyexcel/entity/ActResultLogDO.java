package com.vnjohn.easyexcel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * excel 导入/导出演示实体
 * </p>
 *
 * @author <a href="https://www.vnjohn.com">vnjohn</a>
 * @since 2022-07-07
 */
@Data
@Accessors(chain = true)
@TableName("act_result_log")
public class ActResultLogDO implements Serializable {

    private static final long serialVersionUID = 6130087252261636070L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("onlineseqid")
    private String onlineseqid;

    @TableField("businessid")
    private String businessid;

    @TableField("becifno")
    private String becifno;

    @TableField("ivisresult")
    private String ivisresult;

    @TableField("createdby")
    private String createdby;

    @TableField("createddate")
    private LocalDate createddate;

    @TableField("updateby")
    private String updateby;

    @TableField("updateddate")
    private LocalDate updateddate;

    @TableField("risklevel")
    private String risklevel;

}
