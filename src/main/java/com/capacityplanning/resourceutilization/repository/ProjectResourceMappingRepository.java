package com.capacityplanning.resourceutilization.repository;

import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDTO;

@Repository
public interface ProjectResourceMappingRepository extends JpaRepository<ProjectResourceMappingEntity, Long> {
    public List<ProjectResourceMappingEntity> findAll();

    @Query("SELECT new com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDTO(prm.resourceId resourceId, ri.resourceName resourceName, :startDate, :endDate, " +
            "(1 - sum(prm.allocationPercentage)/count(prm.resourceId)) availability, sum(prm.allocationPercentage)/count(prm.resourceId) utilized) " +
            "FROM ProjectResourceMappingEntity prm " +
            "JOIN ResourceInfoEntity ri ON prm.resourceId = ri.Id " +
            "WHERE prm.startDate <= :endDate AND prm.endDate >= :startDate " +
            " and ri.resourceName != 'No Name'" +
            "GROUP BY prm.resourceId " +
            "HAVING sum(prm.allocationPercentage) <= 0.5")
    public List<ResourceAvailabilityDTO> findAvailableResourcesForDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}