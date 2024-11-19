/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: UndeliveredMailException.java
 */

package com.philips.onespace.exception;

public class UndeliveredMailException extends Exception {
    private static final long serialVersionUID = 115L;

    public UndeliveredMailException(String message) {
        super(message);
    }
}
