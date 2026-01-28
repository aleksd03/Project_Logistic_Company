package org.informatics.service;

/**
 * Service responsible for calculating shipment prices
 * and validating delivery details.
 */
public class PricingService {

    // Base price per kilogram when delivery is to an office
    private static final double BASE_PRICE_TO_OFFICE = 1.50;

    // Base price per kilogram when delivery is to an address
    private static final double BASE_PRICE_TO_ADDRESS = 2.50;

    /**
     * Calculates the final shipment price based on weight and delivery type.
     */
    public double calculatePrice(double weight, boolean deliveryToOffice) {

        // Validate weight
        if (weight <= 0) {
            throw new IllegalArgumentException("Теглото трябва да е положително число");
        }

        // Select base price depending on delivery type
        double basePrice = deliveryToOffice
                ? BASE_PRICE_TO_OFFICE
                : BASE_PRICE_TO_ADDRESS;

        // Calculate base cost
        double price = weight * basePrice;

        // Add surcharge for heavy shipments
        if (weight > 10) {
            price += 5.0;
        }
        if (weight > 20) {
            price += 10.0;
        }

        // Round price to two decimal places
        return Math.round(price * 100.0) / 100.0;
    }

    /**
     * Validates delivery information depending on delivery type.
     */
    public boolean validateDelivery(
            boolean deliveryToOffice,
            Long officeId,
            String address
    ) {
        // Delivery to office requires a valid office ID
        if (deliveryToOffice) {
            return officeId != null;
        }
        // Delivery to address requires a non-empty address
        else {
            return address != null && !address.trim().isEmpty();
        }
    }
}

