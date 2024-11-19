/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: EncryptionUtil.java
 */

package com.philips.onespace.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {
	private final TextEncryptor encryptor;

	public EncryptionUtil(@Value("${crypto.password}") String password) {
		String salt = KeyGenerators.string().generateKey();
		encryptor = Encryptors.delux(password, salt);
	}

	public String encrypt(String text) {
		return encryptor.encrypt(text);
	}

	public String decrypt(String text) {
		return encryptor.decrypt(text);
	}

}
