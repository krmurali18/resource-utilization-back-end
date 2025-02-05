package com.capacityplanning.resourceutilization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalResourceAllocationController {
    @GetMapping("/api/global-resource-allocations")
    // write a rest api to get all global resource allocations
    public String getGlobalResourceAllocations() {
        return "All global resource allocations";
    }

}
