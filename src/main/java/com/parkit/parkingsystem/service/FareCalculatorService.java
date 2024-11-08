package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.FideliteDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    private FideliteDAO fideliteDAO;

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        int inHour = ticket.getInTime().getHours();
        int outHour = ticket.getOutTime().getHours();
        int inMin = ticket.getInTime().getMinutes();
        int outMin = ticket.getOutTime().getMinutes();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        int hduration = outHour - inHour;
        int minduration;
        if (inMin > outMin){
            hduration--;
            outMin = outMin + 60;
        }
        minduration = outMin - inMin;
        minduration = (minduration*100)/60;

        double duration = hduration + minduration;

        int fidelite = fideliteDAO.getFidelite(ticket.getVehicleRegNumber());
        double price;
        if (minduration > 30){
            switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                    price = duration * Fare.CAR_RATE_PER_HOUR;
                    if (fidelite >= 5){
                        price = price*0.95;
                    }
                    ticket.setPrice(price);
                    break;
                }
                case BIKE: {
                    price = duration * Fare.BIKE_RATE_PER_HOUR;
                    if (fidelite >= 5){
                        price = price*0.95;
                    }
                    ticket.setPrice(price);
                    break;
                }
                default: throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
        else {
            ticket.setPrice(0);
        }
    }
}