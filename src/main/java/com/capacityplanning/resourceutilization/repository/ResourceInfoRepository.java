package com.capacityplanning.resourceutilization.repository;

import com.capacityplanning.resourceutilization.entity.ProjectInfoEntity;
import com.capacityplanning.resourceutilization.entity.ResourceInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResourceInfoRepository extends JpaRepository<ResourceInfoEntity, Long> {
    public List<ResourceInfoEntity> findAll();

    @Query("SELECT ri FROM ResourceInfoEntity ri WHERE ri.resourceName = :resourceName")
    public Optional<ResourceInfoEntity> findByResourceName(@Param("resourceName") String resourceName);
}
