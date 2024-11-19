/*
 * Copyright(C) Koninklijke Philips Electronics N.V. 2022
 *
 *    All rights are reserved. Reproduction or transmission in whole or in part, in
 *    any form or by any means, electronic, mechanical or otherwise, is prohibited
 *    without the prior written permission of the copyright owner.
 *
 *
 */
package com.philips.onespace.exception;

public class HttpStatus {
    public static final HttpStatus OK = new HttpStatus(200, "OK");
    public static final HttpStatus CREATED = new HttpStatus(201, "Created");
    public static final HttpStatus NO_CONTENT = new HttpStatus(204, "No Content");
    public static final HttpStatus INTERNAL_SERVER_ERROR = new HttpStatus(500, "System Error");
    public static final HttpStatus BAD_REQUEST = new HttpStatus(400, "Bad Request");
    public static final HttpStatus CONFLICT = new HttpStatus(409, "Conflict");
    public static final HttpStatus FORBIDDEN = new HttpStatus(403, "Forbidden");
    public static final HttpStatus UNAUTHORIZED = new HttpStatus(401, "Unauthorized");
    public static final HttpStatus NOT_FOUND = new HttpStatus(404, "Not Found");
    public static final HttpStatus ACCEPTED = new HttpStatus(202, "ACCEPTED");
    // Add more status codes as needed
    private final int value;
    private final String reasonPhrase;

    private HttpStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    @Override
    public String toString() {
        return value + " " + reasonPhrase;
    }
}
