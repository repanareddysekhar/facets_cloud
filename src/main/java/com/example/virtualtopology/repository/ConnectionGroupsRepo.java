package com.example.virtualtopology.repository;

import com.example.virtualtopology.entity.ConnectionGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectionGroupsRepo extends JpaRepository<ConnectionGroupEntity, Long> {
    Optional<ConnectionGroupEntity> findByConnectionGroupName(String connectionGroupName);
}
