package com.daisyflemming.restful.spring.boot;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
                setterVisibility = JsonAutoDetect.Visibility.NONE)
public class UID {
    private final String uId;
    private final int input;

    public UID(int input, String uid) {
        this.input = input;
        this.uId = uid;
    }
}
