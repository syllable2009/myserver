package com.xkcoding.export;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * 2020-07-09 11:14
 **/

@Slf4j
@RestController
@RequestMapping("/jxp/export")
public class TestExportController {


  //  @ApiOperation(value = "理财明细模板下载")
  @RequestMapping(value = "/downloadItemTemplate", method = RequestMethod.GET)
  public void downloadTemplate(HttpServletResponse response) {
    BufferedInputStream inputStream = null;
    BufferedOutputStream outputStream = null;

    try {
      InputStream is = this.getClass().getClassLoader().getResourceAsStream("template/financialbenefit.xlsx");
      if (is == null) {
        throw new RuntimeException("模板文件不存在");
      }
      File file = new File("template/financialbenefit.xlsx");
      response.setHeader("content-disposition",
        "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
      OutputStream os = response.getOutputStream();
      inputStream = new BufferedInputStream(is);
      outputStream = new BufferedOutputStream(os);
      final int buffSize = 2048;
      byte[] buff = new byte[buffSize];
      int byteRead = 0;
      while ((byteRead = inputStream.read(buff)) != -1) {
        outputStream.write(buff, 0, byteRead);
      }
    } catch (Exception e) {
      log.error("export downloadTemplate error. case:{}", e.getMessage(), e);
    } finally {
      IOUtils.closeQuietly(inputStream);
      IOUtils.closeQuietly(outputStream);
    }
    return;
  }

  @RequestMapping(value = "/downloadDataTemplate", method = RequestMethod.GET)
  public void downloadDataTemplate(HttpServletResponse response) {

    try {
      InputStream is = this.getClass().getClassLoader().getResourceAsStream("template/financialbenefit.xlsx");
      if (is == null) {
        throw new RuntimeException("模板文件不存在");
      }
      //      File file = new File("template/financialbenefit.xlsx");
      response.setHeader("content-disposition",
        "attachment;filename=" + URLEncoder.encode("下载模板.xlsx", "UTF-8"));

      Workbook workbook = WorkbookFactory.create(is);
      Sheet sheet = workbook.getSheetAt(0);
      Row row = sheet.createRow(4);
      row.createCell(0).setCellValue("1");
      row.createCell(1).setCellValue("NO00001");
      row.createCell(2).setCellValue("1929-5-7");
      workbook.write(response.getOutputStream());
      response.flushBuffer();

    } catch (Exception e) {
      log.error("export downloadTemplate error. case:{}", e.getMessage(), e);
    }
    return;
  }

  private void appendTemplateData(InputStream inputStream, HttpServletResponse response) throws Exception {
    Workbook workbook = WorkbookFactory.create(inputStream);
    Sheet sheet = workbook.getSheetAt(0);

    workbook.write(response.getOutputStream());
    response.flushBuffer();
  }

}
