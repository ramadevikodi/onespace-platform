/*
 * (C) Koninklijke Philips Electronics N.V. 2023
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: CommonException.java
*/

package com.philips.onespace.exception;

import org.owasp.encoder.Encode;
import org.springframework.http.HttpStatus;

public class CommonException extends GenericException {

	private static final long serialVersionUID = -1460179998141149216L;
	private HttpStatus status;

	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public CommonException(String text, String display, String diagnostics, HttpStatus status) {
		super(text,display,Encode.forHtmlContent(diagnostics));
		this.status = status;
	}

}
