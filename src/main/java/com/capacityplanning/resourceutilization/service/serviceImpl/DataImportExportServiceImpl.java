package com.capacityplanning.resourceutilization.service.serviceImpl;
import com.capacityplanning.resourceutilization.dto.*;
import com.capacityplanning.resourceutilization.entity.ProjectInfoEntity;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import com.capacityplanning.resourceutilization.entity.ResourceInfoEntity;
import com.capacityplanning.resourceutilization.repository.ProjectInfoRepository;
import com.capacityplanning.resourceutilization.repository.ProjectResourceMappingRepository;
import com.capacityplanning.resourceutilization.repository.ResourceInfoRepository;
import com.capacityplanning.resourceutilization.service.DataImportExportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DataImportExportServiceImpl implements DataImportExportService{
    @Autowired
    private ProjectInfoRepository projectInfoRepository;

    @Autowired
    private ResourceInfoRepository resourceInfoRepository;

    @Autowired
    private ProjectResourceMappingRepository projectResourceMappingRepository;

    @Override
    public List<ResourceAllocationImportResultDTO> importGlobalResourceAllocation(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        boolean isHeader = true;
        Map<String, Integer> recordCountMap = new HashMap<>();
        Map<String, LocalDate[]> dateRangeMap = new HashMap<>();
        Map<String, ResourceAllocationImportDTO> resourceInfoMap = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<ResourceAllocationImportResultDTO> resourceAllocationImportResultDTOList = new ArrayList<ResourceAllocationImportResultDTO>();
        try {
            for (Row row : sheet) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip the header row
                }

                String resourceName = row.getCell(2).getStringCellValue();
                String task = row.getCell(1).getStringCellValue();
                String groupName = row.getCell(7).getStringCellValue();

                String key = resourceName + "," + task + "," + groupName;
                //System.out.println("key:"+key);
                recordCountMap.put(key, recordCountMap.getOrDefault(key, 0) + 1);
                ResourceAllocationImportDTO resourceAllocationImportDTO = new ResourceAllocationImportDTO();
                Cell startDateCell = row.getCell(0);
                resourceAllocationImportDTO.setResourceName(resourceName);
                resourceAllocationImportDTO.setTask(task);
                resourceAllocationImportDTO.setGroupName(groupName);
                resourceAllocationImportDTO.setProjectManager(row.getCell(3).getStringCellValue());
                resourceAllocationImportDTO.setFte(row.getCell(4).getNumericCellValue());
                resourceAllocationImportDTO.setShortDescription(row.getCell(5).getStringCellValue());
                resourceAllocationImportDTO.setCountry(row.getCell(6).getStringCellValue());
                resourceAllocationImportDTO.setEmploymentType(row.getCell(8).getStringCellValue());
                resourceAllocationImportDTO.setState(row.getCell(9).getStringCellValue());
                //&& DateUtil.isCellDateFormatted(startDateCell)
                if (startDateCell != null && startDateCell.getCellType() == CellType.NUMERIC ) {
                    LocalDate startDate = startDateCell.getLocalDateTimeCellValue().toLocalDate();
                    LocalDate[] dateRange = dateRangeMap.getOrDefault(key, new LocalDate[]{null, null});
                    if (dateRange[0] == null || startDate.isBefore(dateRange[0])) {
                        dateRange[0] = startDate; // Update first start date
                    }
                    if (dateRange[1] == null || startDate.isAfter(dateRange[1])) {
                        dateRange[1] = startDate; // Update last start date
                    }
                    dateRangeMap.put(key, dateRange);
                }

                dateRangeMap.forEach((entryKey, dateRange) -> {
                    if (entryKey.equals(key)) {
                        resourceAllocationImportDTO.setStartDate(dateRange[0]);
                        resourceAllocationImportDTO.setEndDate(dateRange[1]);
                    }
                });

                    resourceInfoMap.put(key, resourceAllocationImportDTO);
                }
            workbook.close();
            return parseAndSaveAllocationData(resourceInfoMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }

        return resourceAllocationImportResultDTOList;
    }

    public List<ResourceAllocationImportResultDTO> parseAndSaveAllocationData(Map<String, ResourceAllocationImportDTO> resourceInfoMap) {

        List<ResourceAllocationImportResultDTO> resourceAllocationImportResultDTOList = new ArrayList<ResourceAllocationImportResultDTO>();
        // Delete all records in the ProjectResourceMappingEntity table
        projectResourceMappingRepository.deleteAll();
        AtomicInteger rowNum = new AtomicInteger();
        resourceInfoMap.forEach((key, resourceAllocationImportDTO) -> {
            rowNum.getAndIncrement();
            ProjectInfoDTO projectInfoDTO;
            Optional<ProjectInfoEntity> existingProjectInfo = projectInfoRepository.findByGroupNameAndTask(
                    resourceAllocationImportDTO.getGroupName(), resourceAllocationImportDTO.getTask());
            if (!existingProjectInfo.isPresent()) {
                ProjectInfoEntity newProjectInfo = new ProjectInfoEntity();
                newProjectInfo.setGroupName(resourceAllocationImportDTO.getGroupName());
                newProjectInfo.setTask(resourceAllocationImportDTO.getTask());
                newProjectInfo.setRequiredAllocation(BigDecimal.valueOf(resourceAllocationImportDTO.getFte()));
                newProjectInfo.setDescription(resourceAllocationImportDTO.getShortDescription());
                //newProjectInfo.setCountry(globalResourceAllocationDTO.getCountry());
                //newProjectInfo.setEmployementType(globalResourceAllocationDTO.getEmployementType());
                newProjectInfo.setStatus(resourceAllocationImportDTO.getState());
                newProjectInfo.setProjectManager(resourceAllocationImportDTO.getProjectManager());
                newProjectInfo.setDemandManager("");
                newProjectInfo.setStartDate(resourceAllocationImportDTO.getStartDate());
                newProjectInfo.setEndDate(resourceAllocationImportDTO.getEndDate());
                newProjectInfo.setCreatedBy("System");
                newProjectInfo.setCreatedAt(LocalDateTime.now());
                newProjectInfo.setUpdatedBy("System");
                newProjectInfo.setUpdatedAt(LocalDateTime.now());
                //newProjectInfo.setProjectManager(globalResourceAllocationDTO.getProjectManager());
                projectInfoDTO = new ProjectInfoDTO(projectInfoRepository.save(newProjectInfo));
                //projectInfoRepository.save(newProjectInfo);
            } else {
                projectInfoDTO = new ProjectInfoDTO(existingProjectInfo.get());
            }

            //Check and create new resource
            ResourceInfoDTO resourceInfoDTO;
            Optional<ResourceInfoEntity> existingResource = resourceInfoRepository.findByResourceName(resourceAllocationImportDTO.getResourceName());
            if (!existingResource.isPresent()) {
                ResourceInfoEntity newResourceInfo = new ResourceInfoEntity();
                newResourceInfo.setResourceName(resourceAllocationImportDTO.getResourceName());
                newResourceInfo.setCompany("");
                newResourceInfo.setCreatedBy("System");
                newResourceInfo.setCreatedAt(LocalDateTime.now());
                newResourceInfo.setUpdatedBy("System");
                newResourceInfo.setUpdatedAt(LocalDateTime.now());
                resourceInfoDTO = new ResourceInfoDTO(resourceInfoRepository.save(newResourceInfo));
            } else {
                resourceInfoDTO = new ResourceInfoDTO(existingResource.get());
            }

            //check and create if the project manager is not present
            ResourceInfoDTO projectManagerResourceInfoDTO;
            if(resourceAllocationImportDTO.getProjectManager()!=null && !resourceAllocationImportDTO.getProjectManager().isEmpty()){
                Optional<ResourceInfoEntity> existingResource1 = resourceInfoRepository.findByResourceName(resourceAllocationImportDTO.getProjectManager());
                if (!existingResource1.isPresent()) {
                    ResourceInfoEntity newResourceInfo = new ResourceInfoEntity();
                    newResourceInfo.setResourceName(resourceAllocationImportDTO.getProjectManager());
                    newResourceInfo.setCompany("");
                    newResourceInfo.setCreatedBy("System");
                    newResourceInfo.setCreatedAt(LocalDateTime.now());
                    newResourceInfo.setUpdatedBy("System");
                    newResourceInfo.setUpdatedAt(LocalDateTime.now());
                    projectManagerResourceInfoDTO = new ResourceInfoDTO(resourceInfoRepository.save(newResourceInfo));
                } else {
                    projectManagerResourceInfoDTO = new ResourceInfoDTO(existingResource1.get());
                }
            }

            // Delete all existing ProjectResourceMappingEntity for the given project and resource
//            projectResourceMappingRepository.deleteByProjectIdAndResourceId(
//                projectInfoDTO.getProjectId(), resourceInfoDTO.getResourceId());

            ProjectResourceMappingEntity projectResourceMappingEntity = new ProjectResourceMappingEntity();
            projectResourceMappingEntity.setResourceId(resourceInfoDTO.getResourceId());
            projectResourceMappingEntity.setProjectId(projectInfoDTO.getProjectId());
            projectResourceMappingEntity.setStartDate(resourceAllocationImportDTO.getStartDate());
            projectResourceMappingEntity.setEndDate(resourceAllocationImportDTO.getEndDate());
            projectResourceMappingEntity.setAllocationPercentage(BigDecimal.valueOf(resourceAllocationImportDTO.getFte()));
            projectResourceMappingEntity.setSource("IMPORT");
            projectResourceMappingEntity.setStatus(resourceAllocationImportDTO.getState());
            projectResourceMappingEntity.setCreatedAt(LocalDateTime.now());
            projectResourceMappingEntity.setCreatedBy("System");
            projectResourceMappingEntity.setUpdatedAt(LocalDateTime.now());
            projectResourceMappingEntity.setUpdatedBy("System");
            projectResourceMappingEntity.setComments("Imported from Global Resource Allocation Excel");
            projectResourceMappingRepository.save(projectResourceMappingEntity);


            ResourceAllocationImportResultDTO resourceAllocationImportResultDTO = new ResourceAllocationImportResultDTO();
            resourceAllocationImportResultDTO.setRowNum(rowNum.get());
            resourceAllocationImportResultDTO.setResourceName(resourceAllocationImportDTO.getResourceName());
            resourceAllocationImportResultDTO.setTask(resourceAllocationImportDTO.getTask());
            resourceAllocationImportResultDTO.setGroupName(resourceAllocationImportDTO.getGroupName());
            resourceAllocationImportResultDTO.setFte(resourceAllocationImportDTO.getFte());
            resourceAllocationImportResultDTO.setShortDescription(resourceAllocationImportDTO.getShortDescription());
            resourceAllocationImportResultDTO.setCountry(resourceAllocationImportDTO.getCountry());
            resourceAllocationImportResultDTO.setEmploymentType(resourceAllocationImportDTO.getEmploymentType());
            resourceAllocationImportResultDTO.setState(resourceAllocationImportDTO.getState());
            resourceAllocationImportResultDTO.setStartDate(resourceAllocationImportDTO.getStartDate());
            resourceAllocationImportResultDTO.setEndDate(resourceAllocationImportDTO.getEndDate());
            resourceAllocationImportResultDTO.setProjectManager(resourceAllocationImportDTO.getProjectManager());
            resourceAllocationImportResultDTO.setImportStatus("Success");
            resourceAllocationImportResultDTO.setMessage("Inserted successfully");
            resourceAllocationImportResultDTOList.add(resourceAllocationImportResultDTO);
        });

        System.out.println("Global Resource Allocation DTO List: " + resourceAllocationImportResultDTOList.size() + " records");
        return resourceAllocationImportResultDTOList;
    }

    @Override
    public List<NewDemandImportResultDTO> importNewDemand(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        boolean isHeader = true;
        List<ProjectInfoEntity> newDemandList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<NewDemandImportResultDTO> newDemandImportResultDTOList = new ArrayList<>();
        Map<String, NewDemandImportDataDTO> newDemandImportMap = new HashMap<>();

        try {
            for (Row row : sheet) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip the header row
                }
                NewDemandImportDataDTO newDemandImportDataDTO = new NewDemandImportDataDTO();
                String requestNumber = row.getCell(2).getStringCellValue();
                String resourcePlan = row.getCell(5).getStringCellValue();
                String portfolio = row.getCell(11).getStringCellValue();
                String task = row.getCell(4).getStringCellValue();
                String groupName = row.getCell(9).getStringCellValue();
                BigDecimal fte = BigDecimal.valueOf(row.getCell(1).getNumericCellValue());
                String description = row.getCell(3).getStringCellValue();
                String demandManager = row.getCell(6).getStringCellValue();
                String projectManager = row.getCell(8).getStringCellValue();
                String status = row.getCell(10).getStringCellValue();
                //String startDate = row.getCell(12).getStringCellValue();
                //String endDate = row.getCell(13).getStringCellValue();
                Cell startDateCell = row.getCell(12); // Assuming start date is in the 6th column
                if (startDateCell != null && startDateCell.getCellType() == CellType.NUMERIC) {
                    newDemandImportDataDTO.setStartDate(startDateCell.getLocalDateTimeCellValue().toLocalDate());
                }

                Cell endDateCell = row.getCell(13); // Assuming end date is in the 7th column
                if (endDateCell != null && endDateCell.getCellType() == CellType.NUMERIC) {
                    newDemandImportDataDTO.setEndDate(endDateCell.getLocalDateTimeCellValue().toLocalDate());
                }
                String key = task + "," + groupName;

                newDemandImportDataDTO.setShortDescription(description);
                newDemandImportDataDTO.setTask(task);
                newDemandImportDataDTO.setResourcePlan(resourcePlan);
                newDemandImportDataDTO.setDemandManager(demandManager);
                newDemandImportDataDTO.setProjectManager(projectManager);
                newDemandImportDataDTO.setGroupName(groupName);
                newDemandImportDataDTO.setState(status);
                newDemandImportDataDTO.setPortfolio(portfolio);

                newDemandImportDataDTO.setFte(BigDecimal.valueOf(fte.doubleValue()));
                newDemandImportMap.put(key, newDemandImportDataDTO);
            }

            AtomicInteger rowNum = new AtomicInteger();
            newDemandImportMap.forEach((key, newDemandImportDataDTO) -> {
                NewDemandImportResultDTO newDemandImportResultDTO = new NewDemandImportResultDTO();
                rowNum.getAndIncrement();
                newDemandImportResultDTO.setRowNum(rowNum.get());
                newDemandImportResultDTO.setFte(newDemandImportDataDTO.getFte());
                newDemandImportResultDTO.setShortDescription(newDemandImportDataDTO.getShortDescription());
                newDemandImportResultDTO.setTask(newDemandImportDataDTO.getTask());
                newDemandImportResultDTO.setResourcePlan(newDemandImportDataDTO.getResourcePlan());
                newDemandImportResultDTO.setDemandManager(newDemandImportDataDTO.getDemandManager());
                newDemandImportResultDTO.setProjectManager(newDemandImportDataDTO.getProjectManager());
                newDemandImportResultDTO.setGroupName(newDemandImportDataDTO.getGroupName());
                newDemandImportResultDTO.setState(newDemandImportDataDTO.getState());
                newDemandImportResultDTO.setPortfolio(newDemandImportDataDTO.getPortfolio());
                newDemandImportResultDTO.setStartDate(newDemandImportDataDTO.getStartDate());
                newDemandImportResultDTO.setEndDate(newDemandImportDataDTO.getEndDate());

                Optional<ProjectInfoEntity> existingProjectInfo = projectInfoRepository.findByGroupNameAndTask(
                        newDemandImportDataDTO.getGroupName(), newDemandImportDataDTO.getTask());

                if (!existingProjectInfo.isPresent()) {
                    ProjectInfoEntity newDemand = new ProjectInfoEntity();
                    newDemand.setRequiredAllocation(newDemandImportDataDTO.getFte());
                    newDemand.setDescription(newDemandImportDataDTO.getShortDescription());
                    newDemand.setTask(newDemandImportDataDTO.getTask());
                    newDemand.setDemandManager(newDemandImportDataDTO.getDemandManager());
                    newDemand.setProjectManager(newDemandImportDataDTO.getProjectManager());
                    newDemand.setGroupName(newDemandImportDataDTO.getGroupName());
                    newDemand.setStatus(newDemandImportDataDTO.getState()); // Assuming status is in the 5th column
                    newDemand.setStartDate(newDemandImportDataDTO.getStartDate());
                    newDemand.setEndDate(newDemandImportDataDTO.getEndDate());
                    newDemand.setCreatedBy("System");
                    newDemand.setCreatedAt(LocalDateTime.now());
                    newDemand.setUpdatedBy("System");
                    newDemand.setUpdatedAt(LocalDateTime.now());
                    newDemandList.add(newDemand);
                    projectInfoRepository.save(newDemand);
                    newDemandImportResultDTO.setImportStatus("Success");
                    newDemandImportResultDTO.setMessage("Inserted successfully");
                }  else {
                    newDemandImportResultDTO.setImportStatus("Failed");
                    newDemandImportResultDTO.setMessage("Project already exists");
                }
                newDemandImportResultDTOList.add(newDemandImportResultDTO);
            });
            // if(newDemandList.size > 1) {
            //     projectInfoRepository.saveAll(newDemandList);
            // }
        } catch (Exception e) {
            e.printStackTrace();
            //return "Error occurred while importing new demand: " + e.getMessage();
        } finally {
            workbook.close();
        }


        return newDemandImportResultDTOList;
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
