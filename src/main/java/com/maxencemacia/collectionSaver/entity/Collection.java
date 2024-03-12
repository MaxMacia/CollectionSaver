package com.maxencemacia.collectionSaver.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.maxencemacia.collectionSaver.entity.authentication.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@Table(name = "collections")
@NoArgsConstructor
@AllArgsConstructor
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column
    private String type;
    @LastModifiedDate
    @Column
    private LocalDateTime modificationDate;
    @LastModifiedBy
    @Column
    private String modificationUser;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creationUser;
    @CreatedDate
    @Column
    private LocalDateTime creationDate;
    @OneToMany(mappedBy = "collection", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Attribute> attributes;
}
