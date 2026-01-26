package org.informatics.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.informatics.entity.enums.ShipmentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shipments")
public class Shipment extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id")
    private Client sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id")
    private Client receiver;

    @ManyToOne(optional = false)
    @JoinColumn(name = "registered_by_employee_id")
    private Employee registeredBy;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Double price;

    @Column(name = "delivery_to_office", nullable = false)
    private Boolean deliveryToOffice;

    @ManyToOne(optional = true)
    @JoinColumn(name = "delivery_office_id", nullable = true)
    private Office deliveryOffice;

    @Column(name = "delivery_address", length = 500, nullable = true)
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "delivery_date", nullable = true)
    private LocalDateTime deliveryDate;
}