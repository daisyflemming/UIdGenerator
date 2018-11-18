package com.daisyflemming.restful.spring.boot;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@RestController
public class UIDService {

    /**
     * alphanumerics contains an array of alphabets and 0-9 in a predefined position.
     */
    private char[] alphanumerics = generateAlphanumericCharacters();
    /**
     * bases represent the no. of alphanumeric character that each character can choose from in each position.
     * e.g. the 1st character can choose from the 21 consonants.
     */
    private final int[] bases = {21, 36, 36, 36, 36, 36};
    /**
     * maxLength is the maximum length of character for a uid
     */
    final int maxLength = 6;
    /**
     * minLength is the minimum length of character for a uid
     */
    private final int minLength = 4;
    /**
     * id will be cached in the pool with the same char length. Ideally this should be stored
     * in a database or a file so when spring reboot, it can be restored.
     */
    private HashMap<Integer,ArrayList<UID>> idProvisioned = new HashMap<Integer,ArrayList<UID>>();

    String LENGTH_OUT_OF_RANGE = "No. of character requested is out of range. " +
        "Please select between " + minLength + " and " + maxLength + ".";
    String INPUT_OUT_OF_RANGE = "Input entered cannot be presented by the selected character length ";
    String NO_UID_AVAILABLE = "No more UId available.";
    /**
     * This method will take a query parameter length= minLength ... maxLength.
     * If no parameter is given, maxLength will be used
     *
     * @param length is the length of character required for the id. If none is given, it is default to the max. length
     * @return UID the contains both the result batch id and the input value
     */
    @RequestMapping(value = {"/getId", "/getId/{length}"}, method = RequestMethod.GET)
    public UID getId(@PathVariable Optional<Integer> length) throws Exception {

        // if no. of character requested is out of range, throw error message
        if (length.isPresent() && (length.get() < minLength || length.get() > maxLength)) {
            throw new RuntimeException(LENGTH_OUT_OF_RANGE);
        }

        // coast clear, proceed
        int idLength = length.orElse(maxLength);

        // there are different pools for different char length, retrieve the pool accordingly
        int maxValue = getMaxValue(idLength);
        ArrayList pool = idProvisioned.get(idLength);
        if (pool.size() == maxValue + 1) {
            throw new RuntimeException(NO_UID_AVAILABLE);
        }

        UID uid;
        do {
            uid = getRandomInt(idLength);
        }
        while (pool.contains(uid) || uid.input > maxValue);
        pool.add(uid);
        // sorting make execution of contains quicker O(logN) instead of O(N).
        Collections.sort(pool);
        return uid;
    }

    @RequestMapping(value = {"/id/{input}"}, method = RequestMethod.GET)
    public UID queryId(@PathVariable int input) throws Exception {
        return queryId(input, maxLength);
    }

    @RequestMapping(value = {"/id/{input}/length/{length}"}, method = RequestMethod.GET)
    public UID queryId(@PathVariable int input, @PathVariable int length) throws Exception {
        if (input > getMaxValue(length)){
            throw new RuntimeException(INPUT_OUT_OF_RANGE);
        }
        return new UID(input, convertToString(input, 0, length - 1));
    }

    private char[] generateAlphanumericCharacters() {
        String consonants = "BCDFGHJKLMNPQRSTVWXYZ";
        String vowels = "AEIOU";
        String numbers = "0123456789";
        return (consonants + vowels + numbers).toCharArray();
    }

    private String convertToString(int input, int count, int length) throws Exception {
        // terminate when count reach id length
        if (count == length) return String.valueOf(alphanumerics[input]);

        // if count = 0, base = 21
        // if count = 1, base = 36
        int base = bases[count];
        // notice that the least significant digit goes to the front, not the end
        return String.valueOf(alphanumerics[input % base]) + convertToString(input / base, count + 1, length);
    }

    // range for 4 char long = 0, 1, 2, ..., 21X36^3
    // range for 5 char long = 0, 1, 2, ..., 21X36^4
    // range for 6 char long = 0, 1, 2, ..., 21X36^5
    private UID getRandomInt(int length) throws Exception {
        int xMax = getMaxValue(length);
        int uid_Int = new Random().nextInt(xMax);
        return new UID(uid_Int, convertToString(uid_Int, 0, length - 1));
    }

    int getMaxValue(int length) {
        return Arrays.stream(Arrays.copyOfRange(bases, 0, length)).reduce(1, (a, b) -> a * b) - 1;
    }

}
