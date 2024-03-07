package com.maxencemacia.collectionSaver.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column
    private String name;
    @Column
    private String stringValue;
    @Column
    private Integer numberValue;
    @ManyToOne
    @JoinColumn(name = "collection_id")
    @JsonBackReference
    private Collection collection;
}
