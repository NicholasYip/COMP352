package sequence;

public class NonexistantVINException extends Exception {
    public NonexistantVINException(String VIN) {
        super("There doesn't exist a vehicle with the VIN " + VIN);
    }
}
