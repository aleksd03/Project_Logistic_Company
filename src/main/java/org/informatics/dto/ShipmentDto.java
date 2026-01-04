package org.informatics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDto {
    private long id;
    private String senderEmail;
    private String receiverEmail;
    private String status;
    private double price;
}
