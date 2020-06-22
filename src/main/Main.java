package main;

import accident.Accident;
import avl.DuplicateVINException;
import avl.Vehicle;
import cvr.CVR;
import cvr.InvalidKeyException;
import cvr.KeyLengthOutOfBoundsException;
import cvr.ThresholdOutOfBoundsException;
import sequence.NonexistantVINException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws DuplicateVINException, ThresholdOutOfBoundsException, KeyLengthOutOfBoundsException, InvalidKeyException, NonexistantVINException {

        CVR structure = new CVR(1000, 11);
        ArrayList<String> keys = structure.generate(999);

        for (int i = 0; i < 999; i++) {
            structure.add(keys.get(i), new Vehicle(keys.get(i)));
            structure.getValues(keys.get(i)).addAccident("Shit went wrong", LocalDate.of(1999, 11, 25));
            structure.getValues(keys.get(i)).addAccident("Car went boom", LocalDate.of(2000, 10, 11));
            structure.getValues(keys.get(i)).addAccident("Big crash", LocalDate.of(2000, 10, 10));
            structure.getValues(keys.get(i)).addAccident("Very old accident", LocalDate.of(0, 1, 1));
        }
        System.out.println("Testing for sequence\n");
        System.out.println("The current structure has " + structure.getSize() + " vehicules and is a " + structure.isWhat());
        structure.getValues(keys.get(750)).printVehicle();

        String testkey = structure.prevKey(keys.get(750));
        System.out.println(testkey);
        System.out.println(structure.nextKey(testkey));

        ArrayList<Accident> history = structure.prevAccids(structure.nextKey(testkey));
        for (int i = 0; i < history.size(); i++)
            System.out.println(history.get(i));

        structure.remove(keys.get(100));
        System.out.println("The current structure has " + structure.getSize() + " vehicules and is a " + structure.isWhat());

        ArrayList<String> keys2 = structure.generate(3);
        keys.add(keys2.get(0));
        keys.add(keys2.get(1));
        keys.add(keys2.get(2));

        structure.add(keys.get(999), new Vehicle(keys.get(999)));
        structure.add(keys.get(1000), new Vehicle(keys.get(1000)));
        structure.add(keys.get(1001), new Vehicle(keys.get(1001)));

        System.out.println("\ntesting for AVL\n");
        System.out.println("The current structure has " + structure.getSize() + " vehicules and is an " + structure.isWhat());

        structure.getValues(keys.get(750)).printVehicle();
        testkey = structure.prevKey(keys.get(750));
        System.out.println(testkey);
        System.out.println(structure.nextKey(testkey));

       history = structure.prevAccids(structure.nextKey(testkey));
        for (int i = 0; i < history.size(); i++)
            System.out.println(history.get(i));

        System.out.println(Arrays.toString(structure.allKeys().toArray()));
    }
}
