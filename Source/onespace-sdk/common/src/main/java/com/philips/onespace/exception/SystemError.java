/*
  * (C) Koninklijke Philips Electronics N.V. 2023
  *
  * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
  * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
  * the copyright owner.
  *
  * File name: SystemError.java
*/

package com.philips.onespace.exception;

public class SystemError {

    private HttpStatus status;
    private String message;

    public SystemError() {
    	
    }

    public SystemError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
