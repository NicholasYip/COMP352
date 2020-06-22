package cvr;

public class KeyLengthOutOfBoundsException extends Exception {
    public KeyLengthOutOfBoundsException(int keyLength){
        super(String.format(keyLength + " is an invalid key length"));
    }
}
