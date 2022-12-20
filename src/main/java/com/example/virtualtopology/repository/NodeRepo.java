package com.example.virtualtopology.repository;

import com.example.virtualtopology.entity.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NodeRepo extends JpaRepository<NodeEntity, Long> {
    Optional<NodeEntity> findByNodeName(String nodeName);
}
