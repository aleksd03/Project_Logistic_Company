package org.informatics.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a Client in the system.
 * Each Client is associated with exactly one User
 * and may optionally belong to a Company.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends BaseEntity {

    /**
     * One-to-one relationship with User.
     * - Each Client must have exactly one User
     * - CascadeType.ALL propagates all operations (persist, remove, etc.)
     * - orphanRemoval ensures the User is deleted if the Client is removed
     */
    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    /**
     * Many Clients may belong to one Company.
     * - Association is optional (client may exist without a company)
     * - ON DELETE SET NULL ensures company_id becomes NULL if Company is deleted
     */
    @ManyToOne(optional = true)
    @JoinColumn(
            name = "company_id",
            nullable = true,
            foreignKey = @ForeignKey(
                    name = "fk_client_company",
                    foreignKeyDefinition =
                            "FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE SET NULL"
            )
    )
    private Company company;
}

