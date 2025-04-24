package com.capacityplanning.resourceutilization.controller;

import com.capacityplanning.resourceutilization.dto.NewDemandImportResultDTO;
import com.capacityplanning.resourceutilization.dto.ResourceAllocationImportResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.capacityplanning.resourceutilization.service.DataImportExportService;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
@Tag(name = "data-import", description = "Data Import & Export API")
public class DataImportExportController {
    @Autowired
    private DataImportExportService dataImportExportService;

    @PostMapping(value = "/import-global-resource-allocation",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Insert the Service now Data for the project resource mapping", description = "Upload the project resource mapping data")
    public ResponseEntity<List<ResourceAllocationImportResultDTO>> importGlobalResourceAllocation(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("File name: " + file.getOriginalFilename());
            List<ResourceAllocationImportResultDTO> result = dataImportExportService.importGlobalResourceAllocation(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            List<ResourceAllocationImportResultDTO> errorResponse = List.of(new ResourceAllocationImportResultDTO("Error", "Failed to import resource allocation data: " + e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(value = "/import-new-demand", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import new demand for projects", description = "Upload the new demand data for projects")
    public ResponseEntity<List<NewDemandImportResultDTO>> importNewDemand(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("File name: " + file.getOriginalFilename());
            List<NewDemandImportResultDTO> result = dataImportExportService.importNewDemand(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            List<NewDemandImportResultDTO> errorResponse = List.of(new NewDemandImportResultDTO("Error", "Failed to import new demand: " + e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/export")
    public ResponseEntity<ByteArrayInputStream> exportData() {
        try {
            ByteArrayInputStream data = dataImportExportService.exportData();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=data.xlsx")
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
