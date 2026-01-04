package org.informatics.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "companies")
public class Company extends BaseEntity {

    @Column(nullable = false)
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
