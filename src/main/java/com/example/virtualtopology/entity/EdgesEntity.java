package com.example.virtualtopology.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EdgesEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;

    @ManyToOne
    @JoinColumn
    private NodeEntity toNode;

    @ManyToOne
    @JoinColumn
    private NodeEntity fromNode;

    @ManyToOne
    @JoinColumn
    private ConnectionGroupEntity connectionGroup;
}
