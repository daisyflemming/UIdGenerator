package com.daisyflemming.restful.spring.boot;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;

/**
 * This represent the UID generated, it contains the alphanumeric UID as the input value used to generate it.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
                setterVisibility = JsonAutoDetect.Visibility.NONE)
public class UID implements Comparable<UID> {
    final int input;
    final String uid;

    UID(int input, String uid) {
        this.input = input;
        this.uid = uid;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(uid).toHashCode();
    }

    @Override
    public int compareTo(@NotNull UID u) {
        return Integer.compare(input, u.input);
    }
}
