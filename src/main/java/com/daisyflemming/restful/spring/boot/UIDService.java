package com.daisyflemming.restful.spring.boot;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

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
    private int nextId = 0;

    /**
     * This method will take a query parameter length= minLength ... maxLength.
     * If no parameter is given, maxLength will be used
     *
     * @param length is the length of character required for the id. If none is given, it is default to the max. length
     * @return UID the contains both the result batch id and the input value
     */
    @RequestMapping(value = {"/getNextId", "/getNextId/{length}"}, method = RequestMethod.GET)
    public UID getNextId(@PathVariable Optional<Integer> length) {
        // if nextId reach xMax, display warning
        if (nextId > xMax) {
            throw new RuntimeException("No more new Id available.");
        }

        // if length of id requested is out of range, throw error message
        if (length.isPresent() && (length.get() < minLength || length.get() > maxLength)) {
            throw new RuntimeException(
                "Id length required is not supported. Please select between " + minLength + " and " + maxLength + ".");
        }

        // coast clear, proceed
        int idLength = length.orElse(maxLength);
        UID uid = new UID(nextId, convertByBase(nextId, 0, idLength - 1));
        nextId++;
        return uid;
    }

    @RequestMapping(value = {"/reset", "/reset/{input}"}, method = RequestMethod.GET)
    public String resetId(@PathVariable Optional<Long> input) {
        if (input.isPresent() && input.get() > xMax) {
            throw new RuntimeException("The maximum input value accepted is " + xMax);
        }
        nextId = input.map(Long::intValue).orElse(0);
        return "Input is reset to " + nextId;
    }

    private char[] generateAlphanumericCharters() {
        String consonants = "BCDFGHJKLMNPQRSTVWXYZ";
        String vowels = "AEIOU";
        String numbers = "0123456789";
        return (consonants + vowels + numbers).toCharArray();
    }

    private String convertByBase(int input, int count, int length) {
        // terminate when count reach id length
        if (count == length) return String.valueOf(alphanumerics[input]);

        // if count = 0, base = 21
        // if count = 1, base = 36
        int base = bases[count];
        return String.valueOf(alphanumerics[input % base]) + convertByBase(input / base, count + 1, length);
    }
}
