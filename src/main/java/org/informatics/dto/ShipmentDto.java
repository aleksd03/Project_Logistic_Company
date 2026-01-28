package org.informatics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for Shipment.
 * Used to transfer shipment-related data between layers
 * without exposing the full Shipment entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDto {

    // Unique identifier of the shipment
    private long id;

    // Email of the sender (derived from sender's User entity)
    private String senderEmail;

    // Email of the receiver (derived from receiver's User entity)
    private String receiverEmail;

    // Current shipment status (e.g. CREATED, SENT, DELIVERED)
    private String status;

    // Price of the shipment
    private double price;
}

