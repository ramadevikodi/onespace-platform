/*
 * Copyright(C) Koninklijke Philips Electronics N.V. 2019
 *
 *  All rights are reserved. Reproduction or transmission in whole or in part, in
 *  any form or by any means, electronic, mechanical or otherwise, is prohibited
 *  without the prior written permission of the copyright owner.
 *
 */

package com.philips.onespace.exception;

public class GenericException extends RuntimeException
{
    private static final long serialVersionUID = 2990090782677212216L;
    private String text;
    private String display;
    private String diagnostics;
    public String getText() {
        return text;
    }

    public String getDisplay() {
        return display;
    }

    public String getDiagnostics() {
        return diagnostics;
    }

    public GenericException(String text, String display, String diagnostics) {
        this.text = text;
        this.display = display;
        this.diagnostics = diagnostics;
    }
}