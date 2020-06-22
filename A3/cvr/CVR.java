/* The data structure was implemented with multiple classes. First of all, there is the vehicles class. This one is rather straightforward. Each vehicle
contains a VIN as well as accident history. The VIN basically acts as a key. Each accident history is made up of accidents. All of these vehicles are held inside
the data structure CVR. This dynamic data structure can take on two forms: either a sequence or an AVL tree. Each are separate classes, unrelated, combined with
the CVR class. They both have the same methods and the CVR class decides which type should be used, depending on the amount of cars held inside. If
the threshold for a sequence is passed, the CVR is automatically converted into an AVL tree from a sequence and all the vehicles are transferred. All the required methods
are below, with some additional ones to help supper the CVR data structure. Multiple exceptions can be thrown depending on what the type of mistake is made by the user of
the data structure.
*/

/* Space and time complexity for the CVR-only methods
    1 - setThreshold: O(1) as it only sets a value, never depends on input
    2 - setKeyLength: O(1) as it only sets a value, never depends on input
    3 - generate(n): O(n) as it depends on the desired number of keys only.
 */

/* Space and time complexity for CVR methods when it's acting as a sequence
    4 - allKeys: O(n) as it loops through the sequence and adds all the keys to a new list
    5 - add: O(n) as it loops through the sequence to know where to put the vehicle and shift the rest. Vehicles are sorted as they are added. O(1) for the space complexity as it
             is a constant size.
    6 - remove: O(n) as there is shifting when the car is found. O(1) for the space complexity as it is a constant size.
    7 - getValues: O(logn) as binary searching is done to find the vehicle and remove it
    8 - nextKey: O(logn) as binary searching is done to find the vehicle and it's next key
    9 - prevKey: O(logn) as binary searching is done to find the vehicle and it's previous key
    10 - prevAccids: O(logn) as binary searching is done to find the vehicule and its accident history. The accident are then reverse chronologically sorted which can be ignored.
 */

/* Space and time complexity for CVR methods when it's acting as an AVL
    4 - allKeys: O(n) as it loops through the AVL and adds all the keys to a new list
    5 - add: O(logn) as it depends on the current height of the AVL tree and if balancing is necessary. O(1) for the space complexity as it is constant size
    6 - remove: O(logn) as it depends on the current height of the AVL tree and if balancing is necessary. O(1) for the space complexity as it is a constant size.
    7 - getValues: O(logn) as it depends on the current height of the AVL tree. O(1) for the space complexity as it is a constant size
    8 - nextKey: O(logn) as binary searching is done to find the vehicle and it's next key
    9 - prevKey: O(logn) as binary searching is done to find the vehicle and it's previous key
    10 - prevAccids: O(logn) as it depends on the current height of the AVL tree. The accidents are then reverse chronologically sorted which can be ignored.
 */
package cvr;

import accident.Accident;
import avl.AVL;
import avl.DuplicateVINException;
import avl.Vehicle;
import sequence.NonexistantVINException;
import sequence.Sequence;

import java.util.ArrayList;

public class CVR {
    private long threshold;
    private int keyLength;
    private int size = 0;
    private final AVL avl = new AVL();
    private final Sequence sequence = new Sequence();

    public CVR(){
        threshold = 1000;
        keyLength = 14;
    }

    public CVR(long threshold, int keyLength) throws KeyLengthOutOfBoundsException, ThresholdOutOfBoundsException {
        setKeyLength(keyLength);
        setThreshold(threshold);
    }

    /**
     *
     * @param threshold limit between sequence and AVL
     * @throws ThresholdOutOfBoundsException threshold is too large or too small
     */
    public void setThreshold(long threshold) throws ThresholdOutOfBoundsException {
        if (threshold < 100 || threshold > 900000)
            throw new ThresholdOutOfBoundsException(threshold);
        this.threshold = threshold;
    }

    /**
     *
     * @param keyLength keylength of the vehicles for the data structure
     * @throws KeyLengthOutOfBoundsException the keylength is too short or too long
     */
    public void setKeyLength(int keyLength) throws KeyLengthOutOfBoundsException {
        if (keyLength < 10 || keyLength > 17)
            throw new KeyLengthOutOfBoundsException(keyLength);
        this.keyLength = keyLength;
    }

    /**
     *
     * @param n number of keys
     * @return array of n generated keys
     */
    public ArrayList<String> generate(int n) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        ArrayList<String> keys = new ArrayList<>(n);

        for (int i = 0; i < n;) {
            String generatedKey = "";
            for (int j = 0; j < keyLength; j++) {
                int randomNum = (int) (Math.random() * (characters.length()));
                generatedKey = generatedKey + characters.substring(randomNum, randomNum + 1);
            }
            if (this.allKeys().contains(generatedKey) || keys.contains(generatedKey))
                continue;
            keys.add(generatedKey);
            i++;
        }
        return keys;
    }

    /**
     *
     * @return if the current structure is a sequence, meaning we are under the threshold
     */
    public boolean usingSequence() {
        return size < threshold;
    }

    /**
     *
     * @return if the current structure is an AVL, meaning we are over the threshold
     */
    public boolean usingAVL() {
        return size >= threshold;
    }

    /**
     *
     * @return a list of all the keys
     */
    public ArrayList<String> allKeys() {
        if(usingSequence())
            return sequence.allKeys();
        else
            return avl.allKeys();
    }

    /**
     *
     * @param key the car's key that we want to add to the CVR
     * @param value the car we want to add to the CVR
     * @throws DuplicateVINException if the car is already inside the CVR
     * @throws InvalidKeyException if the key entered isn't valid
     */
    public void add(String key, Vehicle value) throws DuplicateVINException, InvalidKeyException {
        if(key.length() != keyLength || !isAlphaNumerical(key))
            throw new InvalidKeyException(key, keyLength);
        if (allKeys().contains(key))
            throw new DuplicateVINException(key);
        size++;
        if(this.usingAVL()) {
            convertToAVL();
            avl.insert(value);
        } else {
            sequence.add(key, value);
        }
    }

    /**
     *
     * @param key key of the car we want to remove
     */
    public void remove(String key) {
        if (usingAVL()) {
            if (avl.delete(key))
                size--;
        } else {
            if (sequence.remove(key))
                size--;
        }
    }

    /**
     *
     * @param key key that we want the previous vehicule
     * @return previous key
     * @throws NonexistantVINException if the key entered doesn't exist
     */
    public String prevKey(String key) throws NonexistantVINException {
        if(usingAVL())
            return avl.prevKey(key);
        else
            return sequence.prevKey(key);
    }

    /**
     *
     * @param key key of the original vehicule
     * @return the next key of that first vehicule
     * @throws NonexistantVINException if the key entered doesn't exist
     */
    public String nextKey(String key) throws NonexistantVINException {
        if(usingAVL())
            return avl.nextKey(key);
        else
            return sequence.nextKey(key);
    }

    /**
     *
     * @param key key of the car which we want all the accidents.
     * @return a list of all the accidents for that key, in a reverse chronological order
     * @throws NonexistantVINException if the key entered doesn't exist
     */
    public ArrayList<Accident> prevAccids(String key) throws NonexistantVINException {
        Vehicle found;
        if(usingAVL()) {
            found = avl.find(key);
            if (found == null)
                throw new NonexistantVINException(key);
        } else {
            found = sequence.getValues(key);
        }
        return found.getAccidentHistory();
    }

    /**
     * when we go over to threshold, we convert the structure to an AVL
     */
    public void convertToAVL() {
        ArrayList<Vehicle> vehicles = sequence.allVehicles();
        for(Vehicle v: vehicles) {
            try {
                avl.insert(v);
            } catch (DuplicateVINException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        sequence.clear();
    }

    /**
     * when we go under the threshold, we convert the structure to a sequence
     */
    public void convertToSequence() {
        ArrayList<Vehicle> vehicles = avl.allVehicles();
        for(Vehicle v: vehicles) {
            try {
                sequence.add(v.getVIN(), v);
            } catch (DuplicateVINException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        avl.clear();
    }

    /**
     *
     * @param s key which we want to check if valid
     * @return if the key is valid or not
     */
    private boolean isAlphaNumerical(String s) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < s.length(); i++) {
            if(!characters.contains(s.subSequence(i, i+1)))
                return false;
        }
        return true;
    }

    /**
     *
     * @param key key of the car we want
     * @return vehicule of the key
     * @throws NonexistantVINException if there exists no vehicule with that particular key
     */
    public Vehicle getValues(String key) throws NonexistantVINException {
        if (usingSequence())
            return sequence.getValues(key);
        else if (avl.find(key) == null)
            throw new NonexistantVINException(key);
        return avl.find(key);
    }

    /**
     *
     * @return if the structure is an AVL or a sequence
     */
    public String isWhat(){
        if (usingSequence())
            return "Sequence";
        return "AVL";
    }

    /**
     *
     * @return size of the structure
     */
    public int getSize(){
        return size;
    }


}
