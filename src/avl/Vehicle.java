package avl;

import accident.Accident;
import accident.AccidentHistory;

import java.time.LocalDate;
import java.util.ArrayList;

public class Vehicle implements Comparable<String>{
    private final String VIN;
    private AccidentHistory accidentHistory;

    public Vehicle(String VIN) {
        this.VIN = VIN.toUpperCase();
        accidentHistory = new AccidentHistory();
    }

    public void addAccident(String description, LocalDate date) {
        accidentHistory.addAccident(description, date);
    }

    @Override
    public int compareTo(String VIN) {
        return this.VIN.compareTo(VIN);
    }

    public String getVIN() {
        return VIN;
    }


    public void printVehicle(){
        System.out.println("The vehicle with the VIN " + this.VIN + " has the following accident history: ");
        this.accidentHistory.printHistory();
    }


    public ArrayList<Accident> getAccidentHistory() {
        return accidentHistory.getAccidentHistory();
    }

}
