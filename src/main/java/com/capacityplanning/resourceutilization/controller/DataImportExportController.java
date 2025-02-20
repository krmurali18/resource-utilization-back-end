package com.capacityplanning.resourceutilization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.capacityplanning.resourceutilization.service.DataImportExportService;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/data")

public class DataImportExportController {
    @Autowired
    private DataImportExportService dataImportExportService;

    @PostMapping("/import")
    public ResponseEntity<String> importData(@RequestParam("file") MultipartFile file) {
        try {
            dataImportExportService.importData(file);
            return ResponseEntity.ok("Data imported successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import data: " + e.getMessage());
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
