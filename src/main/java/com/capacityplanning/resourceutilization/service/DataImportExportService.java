package com.capacityplanning.resourceutilization.service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface DataImportExportService {
    public String importGlobalResourceAllocation(MultipartFile file) throws IOException;
    public String importNewDemand(MultipartFile file) throws IOException;
    public ByteArrayInputStream exportData() throws IOException;
}
