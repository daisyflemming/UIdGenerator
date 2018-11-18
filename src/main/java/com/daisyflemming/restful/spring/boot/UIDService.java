package com.daisyflemming.restful.spring.boot;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
public class UIDService {

    private char[] alphanumerics = generateAlphanumericCharters();
    private int[] bases = {21, 36, 36, 36, 36, 36};
    private int xMax = Arrays.stream(bases).reduce(1, (a, b) -> a * b) - 1;
    private int maxLength = 6;
    private int minLength = 4;
    /**
     * nextId will be cached. Ideally this should be stored in a database or a file
     * so when spring reboot, it can be restored.
     */
    private ArrayList<UID> idProvisioned = new ArrayList<UID>();

    /**
     * This method will take a query parameter length= minLength ... maxLength.
     * If no parameter is given, maxLength will be used
     *
     * @param length is the length of character required for the id. If none is given, it is default to the max. length
     * @return UID the contains both the result batch id and the input value
     */
    @RequestMapping(value = {"/getId", "/getId/{length}"}, method = RequestMethod.GET)
    public UID getId(@PathVariable Optional<Integer> length) {
        // if all idea are provisioned, display error message
        if (idProvisioned.size() == xMax + 1) {
            throw new RuntimeException("No more new Id available.");
        }

        // if no. of character requested is out of range, throw error message
        if (length.isPresent() && (length.get() < minLength || length.get() > maxLength)) {
            throw new RuntimeException(
                "No. of character requested is out of range. Please select between " + minLength + " and " + maxLength + ".");
        }

        // coast clear, proceed
        int idLength = length.orElse(maxLength);
        UID uid = null;
        do {
            uid = getRandomIntInRange(idLength);
        }
        while (idProvisioned.contains(uid));
        idProvisioned.add(uid);
        Collections.sort(idProvisioned);
        return uid;
    }

    @RequestMapping(value = {"/id/{input}"}, method = RequestMethod.GET)
    public UID queryId(@RequestParam(value="input") Integer input) {
        return queryId(input, maxLength);
    }
    @RequestMapping(value = {"/id/{input}/length/{length}"}, method = RequestMethod.GET)
    public UID queryId(@RequestParam(value="input") Integer input, @RequestParam(value="length") Integer length) {
        return new UID(input, convertToString(input, 0, length - 1));
    }

    private char[] generateAlphanumericCharters() {
        String consonants = "BCDFGHJKLMNPQRSTVWXYZ";
        String vowels = "AEIOU";
        String numbers = "0123456789";
        return (consonants + vowels + numbers).toCharArray();
    }

    private String convertToString(int input, int count, int length) {
        // terminate when count reach id length
        if (count == length) return String.valueOf(alphanumerics[input]);

        // if count = 0, base = 21
        // if count = 1, base = 36
        int base = bases[count];
        // notice that the least significant digit goes to the front, not the end
        // this avoid collision between different length of id
        // BBBB = 0 (length = 4)
        // CBBBBB = 1 (length = 6)
        // DBBBB = 1 (length = 5)
        return String.valueOf(alphanumerics[input % base]) + convertToString(input / base, count + 1, length);
    }

    private UID getRandomIntInRange(int idLength){
        int uid_Int = new Random().nextInt(xMax);
        return new UID(uid_Int, convertToString(uid_Int, 0, idLength - 1));
    }
}
