package accident;

import java.time.LocalDate;

/**
 * Accident class stores description and date of a single accident
 */
public class Accident implements Comparable<Accident>{
    private String description;
    private final LocalDate dateOfAccident;

    /**
     * Constructor for Accident
     * @param description description of an accident
     * @param dateOfAccident date that the accident occured
     */
    Accident(String description, LocalDate dateOfAccident) {
        this.description = description;
        this.dateOfAccident = dateOfAccident;
    }

    Accident(Accident other) {
        this.description = other.description;
        this.dateOfAccident = LocalDate.of(other.dateOfAccident.getYear(), other.dateOfAccident.getMonthValue(), other.dateOfAccident.getDayOfMonth());
    }

    public Accident clone() {
        return new Accident(this);
    }

    /**
     * Allows Accidents to be compared based on the date of an accident for Accident History
     * @param o Accident to be compared to
     * @return the value 0 if the argument Date is equal to this Date; a value less than 0 if this Date is before the Date argument; and a value greater than 0 if this Date is after the Date argument.
     */
    @Override
    public int compareTo(Accident o) {
        return dateOfAccident.compareTo(o.dateOfAccident);
    }

    public boolean isMoreRecentThan(Accident o) {
        return  dateOfAccident.compareTo(o.dateOfAccident) > 0;
    }

    public String toString() {
        return String.format("Description: %s, Date: %s", description, dateOfAccident.toString());
    }
}
