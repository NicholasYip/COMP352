package accident;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * AccidentHistory will work as a priority queue storing the history of accidents of a specific car
 */
public class AccidentHistory {
    Node head;

    public AccidentHistory(){};

    private static class Node {
        Node next;
        Accident accident;

        private Node(Accident accident, Node next) {
            this.accident = accident;
            this.next = next;
        }
    }

    public ArrayList<Accident> getAccidentHistory() {
        ArrayList<Accident> accidents = new ArrayList<>();
        for(Node current = head; current != null; current = current.next) {
            accidents.add(current.accident.clone());
        }
        return accidents;
    }

    /**
     * Adds accident to history given description and date
     * @param description description of an accident
     * @param date date that an accident occurred
     */
    // Linear time insertion is fine since accidents are usually added in reverse chronological order
    public void addAccident(String description, LocalDate date) {
        Accident newAccident = new Accident(description, date);

        // Add element as head if history is empty or it is more recent than the head
        if (this.isEmpty() || newAccident.isMoreRecentThan(head.accident)) {
            head = new Node(newAccident, head);
            return;
        }

        // Find accident less recent than newAccident and insert before it
        Node current;
        for (current = head; current.next != null; current = current.next) {
            if(newAccident.isMoreRecentThan(current.next.accident)) {
                current.next = new Node(newAccident, current.next);
                break;
            }
        }
        // Add accident as last entry if all other accidents are not less recent
        if(current.next == null) {
            current.next = new Node(newAccident, null);
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void printHistory() {
        for (Node current = head; current != null; current = current.next) {
            System.out.printf("[ %s ] ", current.accident.toString());
        }
        System.out.println();
    }
}
