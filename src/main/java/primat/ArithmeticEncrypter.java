package primat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

public class ArithmeticEncrypter {
    private InputStream input;
    private OutputStream output;
    private NavigableMap<Character, CharacterAttribute> mapCharacterAttributes = new TreeMap<>();
    private int fileLength;
    private String inputStream;
    private BigDecimal code;
    private double customLeft, customRight;

    public ArithmeticEncrypter(InputStream input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    public void readTextAndFillCounts() throws IOException {
        inputStream = new String(input.readAllBytes());
        fileLength = inputStream.length();
        inputStream.chars()
                .mapToObj(character -> (char) character)
                .forEach(character -> {
                    if (!mapCharacterAttributes.containsKey(character))
                        mapCharacterAttributes.put(character, new CharacterAttribute(character, 1));
                    else {
                        CharacterAttribute characterAttribute = mapCharacterAttributes.get(character);
                        characterAttribute.setCount(characterAttribute.getCount() + 1);
                    }
                });
    }

    public void fillProbabilities() {
        AtomicReference<Double> sumProbability = new AtomicReference<>((double) 0);
        mapCharacterAttributes.values()
                .forEach(characterAttribute -> {
                    double currentProbability = ((double) characterAttribute.getCount()) / fileLength;
                    sumProbability.updateAndGet(v -> v + currentProbability);
                    characterAttribute.setProbability(sumProbability.get());
                });
    }

    public BigDecimal getCode() {
        final BigDecimal[] left = {new BigDecimal("0")};
        final BigDecimal[] right = {new BigDecimal("1")};
        inputStream.chars()
                .mapToObj(character -> (char) character)
                .forEach(character -> {
                    BigDecimal diff = right[0].subtract(left[0]);
                    double rightProbability = mapCharacterAttributes.get(character).getProbability();
                    double leftProbability = mapCharacterAttributes.lowerEntry(character) == null ? 0 : mapCharacterAttributes.lowerEntry(character).getValue().getProbability();
                    right[0] = left[0].add(diff.multiply(new BigDecimal(rightProbability)));
                    left[0] = left[0].add(diff.multiply(new BigDecimal(leftProbability)));
                });
        code = right[0].subtract(left[0]).divide(new BigDecimal("2")).add(left[0]);
        return code;
    }

    public void decode() throws IOException {
        BigDecimal currentCode = code;
        customLeft = 0;
        customRight = 1;
        for (int i = 0; i < fileLength; ++i) {
            output.write(getCharForCode(currentCode));
            currentCode = (currentCode.subtract(new BigDecimal(customLeft))).divide(new BigDecimal(customRight).subtract(new BigDecimal(customLeft)));
        }
        output.flush();
    }

    private char getCharForCode(BigDecimal code) {
        customLeft = 0;
        for (CharacterAttribute characterAttribute : mapCharacterAttributes.values()) {
            if (code.compareTo(new BigDecimal(characterAttribute.getProbability())) < 0) {
                customRight = characterAttribute.getProbability();
                return characterAttribute.getCharacter();
            }
            customRight = characterAttribute.getProbability();
            customLeft = customRight;
        }
        return 0;
    }

}
