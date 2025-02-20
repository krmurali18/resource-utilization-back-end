package com.capacityplanning.resourceutilization.service.serviceImpl;
import com.capacityplanning.resourceutilization.entity.ProjectInfoEntity;
import com.capacityplanning.resourceutilization.repository.ProjectInfoRepository;
import com.capacityplanning.resourceutilization.service.DataImportExportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class DataImportExportServiceImpl implements DataImportExportService{
    @Autowired
    private ProjectInfoRepository projectInfoRepository;

    @Override
    public String importData(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            System.out.println(row.getCell(0));
            //ProjectInfoEntity projectInfo = new ProjectInfoEntity();
            //projectInfo.setId((long) row.getCell(0).getNumericCellValue());
            //projectInfo.setName(row.getCell(1).getStringCellValue());
            // Set other fields as needed
            //projectInfoRepository.save(projectInfo);
        }
        workbook.close();
        return "File imported successfully";
    }

    @Override
    public ByteArrayInputStream exportData() throws IOException {
        List<ProjectInfoEntity> projectInfoList = projectInfoRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Project Info");

        int rowNum = 0;
        for (ProjectInfoEntity projectInfo : projectInfoList) {
            Row row = sheet.createRow(rowNum++);
            System.out.println("Row created:"+ row);
            //row.createCell(0).setCellValue(projectInfo.getId());
            //row.createCell(1).setCellValue(projectInfo.getName());
            // Set other fields as needed
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
