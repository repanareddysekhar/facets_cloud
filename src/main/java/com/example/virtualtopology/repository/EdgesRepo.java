package com.example.virtualtopology.repository;

import com.example.virtualtopology.entity.EdgesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EdgesRepo extends JpaRepository<EdgesEntity, Long> {
    @Query("SELECT edge from EdgesEntity edge WHERE edge.connectionGroup.connectionGroupName =:connectionGroupName")
    List<EdgesEntity> findAllByConnectionGroup(String connectionGroupName);
}
