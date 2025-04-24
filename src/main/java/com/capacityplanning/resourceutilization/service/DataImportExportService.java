package com.capacityplanning.resourceutilization.service;
import com.capacityplanning.resourceutilization.dto.NewDemandImportResultDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface DataImportExportService {
    public String importGlobalResourceAllocation(MultipartFile file) throws IOException;
    public List<NewDemandImportResultDTO> importNewDemand(MultipartFile file) throws IOException;
    public ByteArrayInputStream exportData() throws IOException;
}
