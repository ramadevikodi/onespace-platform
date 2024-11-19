package com.philips.onespace.logging;

import org.springframework.stereotype.Service;

@Service
public class LogEmittedDataService {

    private String name;
    private Object data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

