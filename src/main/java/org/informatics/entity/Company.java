package org.informatics.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a Company.
 * A Company may have multiple Offices and multiple Clients.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company extends BaseEntity {

    /**
     * Name of the company.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String name;

    /**
     * One-to-many relationship with Office.
     * - A Company can have multiple Offices
     * - CascadeType.ALL propagates persistence operations
     * - orphanRemoval removes Offices when they are detached from the Company
     */
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Office> offices = new ArrayList<>();

    /**
     * One-to-many relationship with Client.
     * - A Company can have multiple Clients
     * - No cascading to avoid accidental deletion of clients
     */
    @OneToMany(mappedBy = "company")
    private List<Client> clients = new ArrayList<>();
}

