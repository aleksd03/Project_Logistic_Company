package org.informatics.service;

public class PricingService {

    private static final double BASE_PRICE_TO_OFFICE = 1.50;

    private static final double BASE_PRICE_TO_ADDRESS = 2.50;

    public double calculatePrice(double weight, boolean deliveryToOffice) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Теглото трябва да е положително число");
        }

        double basePrice = deliveryToOffice ? BASE_PRICE_TO_OFFICE : BASE_PRICE_TO_ADDRESS;
        double price = weight * basePrice;

        if (weight > 10) {
            price += 5.0;
        }
        if (weight > 20) {
            price += 10.0;
        }

        return Math.round(price * 100.0) / 100.0;
    }

    public boolean validateDelivery(boolean deliveryToOffice, Long officeId, String address) {
        if (deliveryToOffice) {
            return officeId != null;
        } else {
            return address != null && !address.trim().isEmpty();
        }
    }
}
