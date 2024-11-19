/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: InvalidTokenException.java
 */

package com.philips.onespace.exception;

public class InvalidTokenException extends Exception {
    private static final long serialVersionUID = 105L;

    public InvalidTokenException(String message) {
        super(message);
    }
}
