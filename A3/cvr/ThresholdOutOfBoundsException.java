package cvr;

public class ThresholdOutOfBoundsException extends Exception{
    public ThresholdOutOfBoundsException(Long Threshold) {
        super(String.format(Threshold + " is an invalid threshold"));
    }
}
