package com.vnjohn.easyexcel.controller;

import com.vnjohn.easyexcel.util.EasyExcelUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vnjohn
 * @date 2022/7/7
 */
@RequestMapping("/excel")
@RestController
public class ExcelController {

    @Resource
    private EasyExcelUtil easyExcelUtil;

    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) {
        easyExcelUtil.dataExport300w(response);
    }

    @GetMapping("/import")
    public void importExcel(HttpServletResponse response) {
        easyExcelUtil.dataImport300w(response);
    }
}
