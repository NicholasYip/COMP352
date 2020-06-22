package cvr;

public class InvalidKeyException extends Exception{
    public InvalidKeyException(String VIN, int length) {
        super(String.format("VIN must be alphanumerical and must be of length %d: %s", length, VIN));
    }
}
