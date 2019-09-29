package primat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

public class ArithmeticEncrypter {
    private InputStream input;
    private NavigableMap<Character, CharacterAttribute> mapCharacterAttributes = new TreeMap<>();
    private int fileLength;
    private String inputStream;

    public ArithmeticEncrypter(InputStream input) {
        this.input = input;
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

    public double getCode() {
        final double[] left = {0};
        final double[] right = {1};
        inputStream.chars()
                .mapToObj(character -> (char) character)
                .forEach(character -> {
                    double diff = right[0] - left[0];
                    double rightProbability = mapCharacterAttributes.get(character).getProbability();
                    double leftProbability = mapCharacterAttributes.lowerEntry(character) == null ? 0 : mapCharacterAttributes.lowerEntry(character).getValue().getProbability();
                    right[0] = left[0] + diff * rightProbability;
                    left[0] = left[0] + diff * leftProbability;
                });
        return  (right[0] - left[0]) / 2;
    }

}
