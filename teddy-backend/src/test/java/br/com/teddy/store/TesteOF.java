package br.com.teddy.store;

import java.time.DayOfWeek;
import java.time.LocalDate;

class Product{
    public int getDeliveryOnDay(){
        return 3;
    }

    public int getProductShelfLife(){
        return 5;
    }

    public int getDeliveryDaysEarly(){
        return 5;
    }

    public boolean isSupplierOrder(){
        return false;
    }

    public boolean isAvailableSomeDay(){
        return true;
    }

    public boolean isAvailableOnDay(DayOfWeek day){
        return true;
    }

    public String getLineNumber(){
        return "123456";
    }
}

class Slot{
    public LocalDate getStartDateTime(){
        return LocalDate.now();
    }
}

class DeliveryDateNotCalculatedForEntertainingProductConflictException extends RuntimeException{
    public DeliveryDateNotCalculatedForEntertainingProductConflictException(String message){
        super(message);
    }
}

public class TesteOF {

    private LocalDate getDeliveryDate(Product product, Slot slot) {
        Integer shelfLife = product.getProductShelfLife();
        Integer deliveryDaysEarly = product.getDeliveryDaysEarly();
        Integer deliveryOnDay = product.getDeliveryOnDay();
        LocalDate slotDate = slot.getStartDateTime();

        if(!product.isSupplierOrder()){

            return getDateIntoBranchForAllocatedLines(slotDate);
        }

        LocalDate deliveryDateIntoBranch = null;
        if ((null == deliveryDaysEarly) || (null == shelfLife) || !product.isAvailableSomeDay()) {
            throw new DeliveryDateNotCalculatedForEntertainingProductConflictException(product.getLineNumber());
        }
        LocalDate latestDateIntoBranch = slotDate.minusDays(deliveryDaysEarly + 1L);

        while (deliveryDateIntoBranch == null) {
            if (product.isAvailableOnDay(latestDateIntoBranch.getDayOfWeek())) {
                deliveryDateIntoBranch = latestDateIntoBranch;
            } else {
                latestDateIntoBranch = latestDateIntoBranch.minusDays(1);
            }
        }

        LocalDate useByDate = deliveryDateIntoBranch.plusDays(shelfLife);

        if (slotDate.isAfter(useByDate)) {
            if (product.isAvailableOnDay(slotDate.getDayOfWeek()) && deliveryOnDay == 1) {
                deliveryDateIntoBranch = slotDate;
            } else {
                //Here we would be throwing this DeliveryDateNotCalculatedForEntertainingProductConflictException
                // because the deliveryDateIntoBranch would be null, in case if the product is available on the slot
                // day but the delivery on day is not available i.e (0 - Branch can not deliver the items on the day
                // they arrive from the supplier.)
                throw new DeliveryDateNotCalculatedForEntertainingProductConflictException(product.getLineNumber());
            }
        }


        return deliveryDateIntoBranch;
    }

    private LocalDate getDateIntoBranchForAllocatedLines(LocalDate slotDate) {
       return LocalDate.now();
    }

}
