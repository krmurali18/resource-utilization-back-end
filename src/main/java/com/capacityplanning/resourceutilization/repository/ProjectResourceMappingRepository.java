package com.capacityplanning.resourceutilization.repository;

import com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDetailDTO;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDTO;
@Repository
public interface ProjectResourceMappingRepository extends JpaRepository<ProjectResourceMappingEntity, Long> {
    public List<ProjectResourceMappingEntity> findAll();

    @Query("SELECT new com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDTO(prm.resourceId, ri.resourceName, :startDate, :endDate, sum(prm.allocationPercentage), " +
            "(1 - sum(prm.allocationPercentage))) " +
            "FROM ProjectResourceMappingEntity prm " +
            "JOIN ResourceInfoEntity ri ON prm.resourceId = ri.Id " +
            "WHERE prm.startDate <= :endDate AND prm.endDate >= :startDate  and prm.status='Allocated' " +
            "GROUP BY prm.resourceId " +
            "HAVING sum(prm.allocationPercentage) <= 0.5")
    public List<ResourceAvailabilityDTO> findAvailableResourcesForDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

//    @Query("SELECT new com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDTO(prm.resourceId, " +
//            "ri.resourceName, " +
//            "DATE_FORMAT(prm.startDate, '%Y-%m'), " +
//            ":startDate, " +
//            ":endDate, " +
//            "sum(prm.allocationPercentage)/count(prm.resourceId), " +
//            "(1 - sum(prm.allocationPercentage)/count(prm.resourceId)))" +
//            " FROM ProjectResourceMappingEntity prm " +
//            "JOIN ResourceInfoEntity ri ON prm.resourceId = ri.Id " +
//            "WHERE prm.startDate <= :endDate AND prm.endDate >= :startDate " +
//            " and ri.resourceName != 'No Name'" +
//            " GROUP BY prm.resourceId, DATE_FORMAT(prm.startDate, '%Y-%m') " +
//            " HAVING sum(prm.allocationPercentage) <= 0.5")
//    public List<ResourceAvailabilityDTO> findMonthlyResourceAvailability(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT pr.resource_id as resourceId, " +
            "       ri.resource_name as resourceName, " +
            "       DATE_FORMAT(m.month, '%Y-%m') as yearMonth, " +
            "       COALESCE(SUM(pr.allocation_percentage), 0) as total_allocation " +
            "FROM ( " +
            "    SELECT DATE_ADD(:startDate, INTERVAL (a.a + (10 * b.a) + (100 * c.a)) MONTH) as month " +
            "    FROM (SELECT 0 as a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) as a " +
            "    CROSS JOIN (SELECT 0 as a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) as b " +
            "    CROSS JOIN (SELECT 0 as a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) as c " +
            ") as m " +
            "LEFT JOIN project_resource_mapping pr ON m.month BETWEEN pr.start_date AND LAST_DAY(pr.end_date) " +
            "LEFT JOIN resource_info ri ON pr.resource_id = ri.resource_id " +
            "WHERE m.month BETWEEN :startDate AND LAST_DAY(:endDate) " +
            "GROUP BY pr.resource_id, ri.resource_name, yearMonth " +
            "ORDER BY pr.resource_id, yearMonth", nativeQuery = true)
    List<ResourceAvailabilityDetailDTO> findMonthlyResourceAllocationTotals(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);



    @Query(value = "SELECT pr.resource_id as resourceId, " +
            "       ri.resource_name as resourceName, " +
            "       DATE_FORMAT(m.month, '%Y-%m') as yearMonth, " +
            "       COALESCE(SUM(pr.allocation_percentage), 0) as total_allocation " +
            "FROM ( " +
            "    SELECT DATE_ADD(:startDate, INTERVAL (a.a + (10 * b.a) + (100 * c.a)) MONTH) as month " +
            "    FROM (SELECT 0 as a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) as a " +
            "    CROSS JOIN (SELECT 0 as a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) as b " +
            "    CROSS JOIN (SELECT 0 as a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) as c " +
            ") as m " +
            "LEFT JOIN project_resource_mapping pr ON m.month BETWEEN pr.start_date AND LAST_DAY(pr.end_date) " +
            "LEFT JOIN resource_info ri ON pr.resource_id = ri.resource_id " +
            "WHERE m.month BETWEEN :startDate AND LAST_DAY(:endDate) " +
            "  AND ri.resource_name = :resourceName " +
            "GROUP BY pr.resource_id, ri.resource_name, yearMonth " +
            "HAVING ri.resource_name = :resourceName " +
            "ORDER BY pr.resource_id, yearMonth", nativeQuery = true)
    List<ResourceAvailabilityDetailDTO> findMonthlyResourceAllocationTotalsByResourceName(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("resourceName") String resourceName);
    @Query("SELECT prm FROM ProjectResourceMappingEntity prm WHERE prm.mappingId = :mappingId")
    ProjectResourceMappingEntity findByMappingId(@Param("mappingId") Long mappingId);

    @Query("SELECT prm FROM ProjectResourceMappingEntity prm WHERE prm.status = :status")
    List<ProjectResourceMappingEntity> findByStatus(@Param("status") String status);

    @Query("SELECT prm FROM ProjectResourceMappingEntity prm " +
            "JOIN ResourceInfoEntity ri ON prm.resourceId = ri.resourceId " +
            "WHERE prm.status = 'Allocated' AND ri.active = true")
    List<ProjectResourceMappingEntity> findAllocatedResourcesWithActiveFlag();

    @Modifying
    @Transactional
    @Query("DELETE FROM ProjectResourceMappingEntity prm WHERE prm.projectId = :projectId AND prm.resourceId = :resourceId")
    void deleteByProjectIdAndResourceId(@Param("projectId") Integer projectId, @Param("resourceId") Integer resourceId);

    @Query("SELECT prm FROM ProjectResourceMappingEntity prm WHERE prm.resourceId = :resourceId AND prm.projectId = :projectId AND prm.startDate = :startDate AND prm.endDate = :endDate")
    ProjectResourceMappingEntity findByResourceIdAndProjectIdAndStartDateAndEndDate(@Param("resourceId") Integer resourceId, @Param("projectId") Integer projectId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}