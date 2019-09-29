package primat;

import java.io.OutputStream;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ArithmeticDecrypter {

    private OutputStream output;
    private NavigableMap<Character, CharacterAttribute> mapCharacterAttributes = new TreeMap<>();
    private int fileLength;
    private String inputStream;
    private double code;

    public ArithmeticDecrypter(OutputStream output, double code) {
        this.output = output;
        this.code = code;
    }

}
