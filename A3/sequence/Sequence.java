package sequence;

import avl.DuplicateVINException;
import avl.Vehicle;
import java.util.ArrayList;

public class Sequence {
    public ArrayList<Vehicle> vehicles;

    public Sequence() {
        vehicles = new ArrayList<>();
    }

    public void clear() {
        vehicles.clear();
    }

    /**
     *
     * @param key key we want to add to the sequence
     * @param car car we want to add to the sequence
     * @throws DuplicateVINException if the car is already in the sequence
     */
    public void add(String key, Vehicle car) throws DuplicateVINException {
        if (binarySearch(key, 0, this.vehicles.size() - 1) != -1)
            throw new DuplicateVINException(key);
        if (vehicles.isEmpty())
            vehicles.add(car);
        else {
            int i = vehicles.size();
            for (; i > 0; i--) {
                if (key.compareToIgnoreCase(vehicles.get(i-1).getVIN()) > 0)
                    break;
            }
            vehicles.add(i, car);
        }
    }

    /**
     *
     * @param key removes the vehicle with this key
     */
    public boolean remove(String key){
        int result = binarySearch(key, 0, this.vehicles.size() - 1) ;
        if (result == -1)
            return false;
        else {
            this.vehicles.remove(result);
            return true;
        }
    }

    /**
     *
     * @param key which we want the vehicule
     * @return vehicule with the key
     */
    public Vehicle getValues(String key) throws NonexistantVINException {
        int result = binarySearch(key, 0, this.vehicles.size() - 1);
        if (result == -1)
            throw new NonexistantVINException(key);
        return this.vehicles.get(result);
    }

    /**
     *
     * @param key, we want the next key of this one
     * @return next key
     */
    public String nextKey(String key) throws NonexistantVINException {
        int result = binarySearch(key, 0, this.vehicles.size()-1);
        if (result == -1 || result == vehicles.size() - 1)
            throw new NonexistantVINException(key);
        return this.vehicles.get(result + 1).getVIN();
    }

    /**
     *
     * @param key, we want the previous key of this one
     * @return previous key
     */
    public String prevKey(String key) throws NonexistantVINException {
        int result = binarySearch(key, 0, this.vehicles.size() -1);
        if (result == -1 || result == 0)
            throw new NonexistantVINException(key);
        return this.vehicles.get(result - 1).getVIN();
    }

    /**
     *
     * @param key key we are searching for
     * @param bot start of the search area
     * @param top end of the search area
     * @return index of the key, or -1 if not found
     */
    public int binarySearch(String key, int bot, int top) {
        if (top >= bot) {
            int mid = bot + (top - bot) / 2;
            if (this.vehicles.get(mid).getVIN().compareToIgnoreCase(key) == 0)
                return mid;
            else if (this.vehicles.get(mid).getVIN().compareToIgnoreCase(key) > 0)
                return binarySearch(key, bot, mid - 1);
            else
                return binarySearch(key, mid + 1, top);
        }
        return -1;
    }

    /**
     *
     * @return a list of all the keys for all vehicles inside the sequence
     */
    public ArrayList<String> allKeys(){
        ArrayList<String> keys = new ArrayList<>();
        for (int i = 0; i < this.vehicles.size(); i++)
            keys.add(vehicles.get(i).getVIN());
        return keys;
    }

    /**
     *
     * @return a list of all the Vehicle inside the sequence ordered by keys
     */
    public ArrayList<Vehicle> allVehicles(){
        ArrayList<Vehicle> v = new ArrayList<>();
        for (int i = 0; i < this.vehicles.size(); i++)
            v.add(vehicles.get(i));
        return v;

    }
}
