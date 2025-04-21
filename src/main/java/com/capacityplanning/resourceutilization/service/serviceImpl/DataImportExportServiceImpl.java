package com.capacityplanning.resourceutilization.service.serviceImpl;
import com.capacityplanning.resourceutilization.dto.GlobalResourceAllocationDTO;
import com.capacityplanning.resourceutilization.dto.ProjectInfoDTO;
import com.capacityplanning.resourceutilization.dto.ResourceInfoDTO;
import com.capacityplanning.resourceutilization.entity.ProjectInfoEntity;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import com.capacityplanning.resourceutilization.entity.ResourceInfoEntity;
import com.capacityplanning.resourceutilization.repository.ProjectInfoRepository;
import com.capacityplanning.resourceutilization.repository.ProjectResourceMappingRepository;
import com.capacityplanning.resourceutilization.repository.ResourceInfoRepository;
import com.capacityplanning.resourceutilization.service.DataImportExportService;
import com.capacityplanning.resourceutilization.dto.ResourceAllocationImportDTO;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.SimpleDateFormat;

@Service
public class DataImportExportServiceImpl implements DataImportExportService{
    @Autowired
    private ProjectInfoRepository projectInfoRepository;

    @Autowired
    private ResourceInfoRepository resourceInfoRepository;

    @Autowired
    private ProjectResourceMappingRepository projectResourceMappingRepository;

    @Override
    public String importGlobalResourceAllocation(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        boolean isHeader = true;
        Map<String, Integer> recordCountMap = new HashMap<>();
        Map<String, Date[]> dateRangeMap = new HashMap<>();
        Map<String, ResourceAllocationImportDTO> resourceInfoMap = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
                Cell startDateCell = row.getCell(0); // Assuming the start date is in the 5th column (index 4)
                resourceAllocationImportDTO.setProjectManager(row.getCell(3).getStringCellValue()); // Assuming project manager is in the 6th column
                resourceAllocationImportDTO.setFte(row.getCell(4).getNumericCellValue()); // Assuming FTE is in the 7th column
                resourceAllocationImportDTO.setShortDescription(row.getCell(5).getStringCellValue()); // Assuming short description is in the 8th column
                resourceAllocationImportDTO.setCountry(row.getCell(6).getStringCellValue()); // Assuming country is in the 9th column
                resourceAllocationImportDTO.setEmploymentType(row.getCell(8).getStringCellValue()); // Assuming employment type is in the 10th column
                resourceAllocationImportDTO.setState(row.getCell(9).getStringCellValue()); // Assuming state is in the 11th column
                if (startDateCell != null && startDateCell.getCellType() == CellType.NUMERIC) {
                    if (DateUtil.isCellDateFormatted(startDateCell)) {
                        Date startDate = startDateCell.getDateCellValue();

                        Date[] dateRange = dateRangeMap.getOrDefault(key, new Date[]{null, null});
                        if (dateRange[0] == null || startDate.before(dateRange[0])) {
                            dateRange[0] = startDate; // Update first start date
                            //String formattedStartDate = dateFormat.format(dateRange[0]) ;
                            //resourceAllocationImportDTO.setStartDate(formattedStartDate);
                        }
                        if (dateRange[1] == null || startDate.after(dateRange[1])) {
                            dateRange[1] = startDate; // Update last start date
                            //String formattedEndDate = dateFormat.format(dateRange[1]);
                            //resourceAllocationImportDTO.setEndDate(formattedEndDate);
                        }
                        dateRangeMap.put(key, dateRange);
                    }

                    dateRangeMap.forEach((entryKey, dateRange) -> {
                        if (entryKey.equals(key)) {
                            resourceAllocationImportDTO.setStartDate(dateFormat.format(dateRange[0]));
                            resourceAllocationImportDTO.setEndDate(dateFormat.format(dateRange[1]));
                        }
                    });

                    resourceInfoMap.put(key, resourceAllocationImportDTO);
                }
            }
            workbook.close();
            parseDTO(resourceInfoMap);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while importing data: " + e.getMessage();
        } finally {
            workbook.close();
        }

        return "File imported successfully";
    }

    public void parseDTO(Map<String, ResourceAllocationImportDTO> resourceInfoMap) {
        // Populate the DTO with data from the entity
        List<GlobalResourceAllocationDTO> globalResourceAllocationDTOList = new ArrayList<GlobalResourceAllocationDTO>();
        resourceInfoMap.forEach((key, resourceAllocationImportDTO) -> {
            GlobalResourceAllocationDTO globalResourceAllocationDTO = new GlobalResourceAllocationDTO();
            String[] parts = key.split(",");
            String resourceName = parts[0]!=null ? parts[0] : "N/A";
            String task = "";
            if (parts.length > 1 && parts[1] != null && !parts[1].isEmpty()) {
                task = parts[1];
            }
            String groupName = "";
            if (parts.length > 2 && parts[2] != null && !parts[2].isEmpty()) {
                groupName = parts[2];
            }
            globalResourceAllocationDTO.setResourceName(resourceName);
            globalResourceAllocationDTO.setTask(task);
            globalResourceAllocationDTO.setGroupName(groupName);
            globalResourceAllocationDTO.setFte(resourceAllocationImportDTO.getFte());
            globalResourceAllocationDTO.setShortDescription(resourceAllocationImportDTO.getShortDescription());
            globalResourceAllocationDTO.setCountry(resourceAllocationImportDTO.getCountry());
            globalResourceAllocationDTO.setEmployementType(resourceAllocationImportDTO.getEmploymentType());
            globalResourceAllocationDTO.setState(resourceAllocationImportDTO.getState());
            globalResourceAllocationDTO.setStartDate(resourceAllocationImportDTO.getStartDate());
            globalResourceAllocationDTO.setEndDate(resourceAllocationImportDTO.getEndDate());
            globalResourceAllocationDTO.setProjectManager(resourceAllocationImportDTO.getProjectManager());
            globalResourceAllocationDTOList.add(globalResourceAllocationDTO);
        });


        globalResourceAllocationDTOList.forEach(globalResourceAllocationDTO -> {
            ProjectInfoDTO projectInfoDTO;
            Optional<ProjectInfoEntity> existingProjectInfo = projectInfoRepository.findByGroupNameAndTask(
                    globalResourceAllocationDTO.getGroupName(), globalResourceAllocationDTO.getTask());
            if (!existingProjectInfo.isPresent()) {
                ProjectInfoEntity newProjectInfo = new ProjectInfoEntity();
                newProjectInfo.setGroupName(globalResourceAllocationDTO.getGroupName());
                newProjectInfo.setTask(globalResourceAllocationDTO.getTask());
                newProjectInfo.setRequiredAllocation(BigDecimal.valueOf(globalResourceAllocationDTO.getFte()));
                newProjectInfo.setDescription(globalResourceAllocationDTO.getShortDescription());
                //newProjectInfo.setCountry(globalResourceAllocationDTO.getCountry());
                //newProjectInfo.setEmployementType(globalResourceAllocationDTO.getEmployementType());
                newProjectInfo.setStatus(globalResourceAllocationDTO.getState());
                newProjectInfo.setStartDate(LocalDate.parse(globalResourceAllocationDTO.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                newProjectInfo.setEndDate(LocalDate.parse(globalResourceAllocationDTO.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                newProjectInfo.setCreatedBy("Murali");
                newProjectInfo.setCreatedAt(LocalDateTime.now());
                newProjectInfo.setUpdatedBy("Murali");
                newProjectInfo.setUpdatedAt(LocalDateTime.now());
                //newProjectInfo.setProjectManager(globalResourceAllocationDTO.getProjectManager());
                projectInfoDTO = new ProjectInfoDTO(projectInfoRepository.save(newProjectInfo));
                //projectInfoRepository.save(newProjectInfo);
            } else {
                projectInfoDTO = new ProjectInfoDTO(existingProjectInfo.get());
            }

            //Check and create new resource
            ResourceInfoDTO resourceInfoDTO;
            Optional<ResourceInfoEntity> existingResource = resourceInfoRepository.findByResourceName(globalResourceAllocationDTO.getResourceName());
            if (!existingResource.isPresent()) {
                ResourceInfoEntity newResourceInfo = new ResourceInfoEntity();
                newResourceInfo.setResourceName(globalResourceAllocationDTO.getResourceName());
                newResourceInfo.setSkills("");
                newResourceInfo.setCompany("");
                newResourceInfo.setCreatedBy("Murali");
                newResourceInfo.setCreatedAt(LocalDateTime.now());
                newResourceInfo.setUpdatedBy("Murali");
                newResourceInfo.setUpdatedAt(LocalDateTime.now());
                resourceInfoDTO = new ResourceInfoDTO(resourceInfoRepository.save(newResourceInfo));
            } else {
                resourceInfoDTO = new ResourceInfoDTO(existingResource.get());
            }

            //check and create if the project manager is not present
            ResourceInfoDTO projectManagerResourceInfoDTO;
            if(globalResourceAllocationDTO.getProjectManager()!=null && !globalResourceAllocationDTO.getProjectManager().isEmpty()){
                Optional<ResourceInfoEntity> existingResource1 = resourceInfoRepository.findByResourceName(globalResourceAllocationDTO.getProjectManager());
                if (!existingResource1.isPresent()) {
                    ResourceInfoEntity newResourceInfo = new ResourceInfoEntity();
                    newResourceInfo.setResourceName(globalResourceAllocationDTO.getProjectManager());
                    newResourceInfo.setSkills("");
                    newResourceInfo.setCompany("");
                    newResourceInfo.setCreatedBy("Murali");
                    newResourceInfo.setCreatedAt(LocalDateTime.now());
                    newResourceInfo.setUpdatedBy("Murali");
                    newResourceInfo.setUpdatedAt(LocalDateTime.now());
                    projectManagerResourceInfoDTO = new ResourceInfoDTO(resourceInfoRepository.save(newResourceInfo));
                } else {
                    projectManagerResourceInfoDTO = new ResourceInfoDTO(existingResource1.get());
                }
            }

            ProjectResourceMappingEntity projectResourceMappingEntity = new ProjectResourceMappingEntity();
            projectResourceMappingEntity.setResourceId(resourceInfoDTO.getResourceId());
            projectResourceMappingEntity.setProjectId(projectInfoDTO.getProjectId());
            projectResourceMappingEntity.setStartDate(LocalDate.parse(globalResourceAllocationDTO.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            projectResourceMappingEntity.setEndDate(LocalDate.parse(globalResourceAllocationDTO.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            projectResourceMappingEntity.setAllocationPercentage(BigDecimal.valueOf(globalResourceAllocationDTO.getFte()));
            projectResourceMappingEntity.setSource("IMPORT");
            projectResourceMappingEntity.setCreatedAt(LocalDateTime.now());
            projectResourceMappingEntity.setCreatedBy("Murali");
            projectResourceMappingEntity.setUpdatedAt(LocalDateTime.now());
            projectResourceMappingEntity.setUpdatedBy("Murali");
            projectResourceMappingEntity.setComments("Imported from Excel");
            projectResourceMappingRepository.save(projectResourceMappingEntity);

        });
        // save the project info into the database
        //checkAndCreateResourceInfo(globalResourceAllocationDTOList);
        //checkAndCreateProjectInfo(globalResourceAllocationDTOList);

        //projectInfoRepository.saveAll(projectInfoList);
        System.out.println("Global Resource Allocation DTO List: " + globalResourceAllocationDTOList.size() + " records");
    }


    public void checkAndCreateProjectInfo(List<GlobalResourceAllocationDTO> globalResourceAllocationDTOList) {
        Set<String> uniqueGroupNames = new HashSet<>();

        globalResourceAllocationDTOList.forEach(globalResourceAllocationDTO -> {
            Optional<ProjectInfoEntity> existingProjectInfo = projectInfoRepository.findByGroupNameAndTask(
                    globalResourceAllocationDTO.getGroupName(), globalResourceAllocationDTO.getTask());
            if (!existingProjectInfo.isPresent()) {
                ProjectInfoEntity newProjectInfo = new ProjectInfoEntity();
                newProjectInfo.setGroupName(globalResourceAllocationDTO.getGroupName());
                newProjectInfo.setTask(globalResourceAllocationDTO.getTask());
                newProjectInfo.setRequiredAllocation(BigDecimal.valueOf(globalResourceAllocationDTO.getFte()));
                newProjectInfo.setDescription(globalResourceAllocationDTO.getShortDescription());
                //newProjectInfo.setCountry(globalResourceAllocationDTO.getCountry());
                //newProjectInfo.setEmployementType(globalResourceAllocationDTO.getEmployementType());
                newProjectInfo.setStatus(globalResourceAllocationDTO.getState());
                newProjectInfo.setStartDate(LocalDate.parse(globalResourceAllocationDTO.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                newProjectInfo.setEndDate(LocalDate.parse(globalResourceAllocationDTO.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                newProjectInfo.setCreatedBy("Murali");
                newProjectInfo.setCreatedAt(LocalDateTime.now());
                newProjectInfo.setUpdatedBy("Murali");
                newProjectInfo.setUpdatedAt(LocalDateTime.now());
                //newProjectInfo.setProjectManager(globalResourceAllocationDTO.getProjectManager());
                projectInfoRepository.save(newProjectInfo);
            }
        });
    }

    @Override
    public String importNewDemand(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        boolean isHeader = true;
        List<ProjectInfoEntity> newDemandList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            for (Row row : sheet) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip the header row
                }

                ProjectInfoEntity newDemand = new ProjectInfoEntity();
                newDemand.setRequiredAllocation(BigDecimal.valueOf(row.getCell(1).getNumericCellValue())); 
                newDemand.setDescription(row.getCell(3).getStringCellValue()); 
                newDemand.setTask(row.getCell(4).getStringCellValue());
                newDemand.setDemandManager(row.getCell(6).getStringCellValue()); 
                newDemand.setProjectManager(row.getCell(8).getStringCellValue()); 
                newDemand.setGroupName(row.getCell(9).getStringCellValue());                 
                newDemand.setStatus(row.getCell(10).getStringCellValue()); // Assuming status is in the 5th column
                
                Cell startDateCell = row.getCell(12); // Assuming start date is in the 6th column
                if (startDateCell != null && startDateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(startDateCell)) {
                    newDemand.setStartDate(startDateCell.getLocalDateTimeCellValue().toLocalDate());
                }

                Cell endDateCell = row.getCell(13); // Assuming end date is in the 7th column
                if (endDateCell != null && endDateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(endDateCell)) {
                    newDemand.setEndDate(endDateCell.getLocalDateTimeCellValue().toLocalDate());
                }

                newDemand.setCreatedBy("System");
                newDemand.setCreatedAt(LocalDateTime.now());
                newDemand.setUpdatedBy("System");
                newDemand.setUpdatedAt(LocalDateTime.now());
                

                newDemandList.add(newDemand);
            }

            projectInfoRepository.saveAll(newDemandList);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while importing new demand: " + e.getMessage();
        } finally {
            workbook.close();
        }

        return "New demand imported successfully";
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
