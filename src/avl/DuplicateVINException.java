package avl;

public class DuplicateVINException extends Exception {
    public DuplicateVINException(String VIN) {
        super(String.format("Duplicate VIN insertion is not permitted: %s", VIN));
    }
}
