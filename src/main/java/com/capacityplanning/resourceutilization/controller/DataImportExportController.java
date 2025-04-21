package com.capacityplanning.resourceutilization.controller;

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
    @Operation(summary = "Update the Service now Data for the project resource mapping", description = "Upload the project resource mapping data")
    public ResponseEntity<String> importGlobalResourceAllocation(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("File name: " + file.getOriginalFilename());
            dataImportExportService.importGlobalResourceAllocation(file);
            return ResponseEntity.ok("Data imported successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import data: " + e.getMessage());
        }
    }

    @PostMapping(value = "/import-new-demand", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import new demand for projects", description = "Upload the new demand data for projects")
    public ResponseEntity<String> importNewDemand(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("File name: " + file.getOriginalFilename());
            dataImportExportService.importNewDemand(file);
            return ResponseEntity.ok("New demand imported successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import new demand: " + e.getMessage());
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
