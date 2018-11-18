package com.daisyflemming.restful.spring.boot;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
                setterVisibility = JsonAutoDetect.Visibility.NONE)
public class UID implements Comparable<UID>{
    final String uid;
    final int input;

    public UID(int input, String uid) {
        this.input = input;
        this.uid = uid;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(uid).toHashCode();
    }

    @Override
    public int compareTo(UID u) {
        return uid.compareTo(u.uid);
    }
}
