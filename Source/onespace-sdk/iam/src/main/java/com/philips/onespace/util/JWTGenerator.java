/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: JWTGenerator.java
 */

package com.philips.onespace.util;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.extern.slf4j.Slf4j;

import static com.philips.onespace.util.IamConstants.TOKEN_EXPIRY;

@Slf4j
@Component
public class JWTGenerator {
    private String issuer;
    private String audience;
    private String serviceIdentityPrivateKey;

    public JWTGenerator(@Value("${iam.issuer}") String issuer, @Value("${iam.audience}") String audience, @Value("${iam.serviceidentityprivatekey}") String serviceIdentityPrivateKey) {
        this.issuer = issuer;
        this.audience = audience;
        this.serviceIdentityPrivateKey = serviceIdentityPrivateKey;
    }

    /**
	 * This method generates the JWT token.
	 *
	 * @return the generated JWT token
	 */
    /**
     * This method generates a JSON Web Token (JWT).
     * 
     * The process involves the following steps:
     * 
     * 1. **Token Creation**: The method begins by assembling the necessary data for the JWT, 
     *    including the header, payload, and claims (e.g., issuer, subject, expiration time).
     * 
     * 2. **Signing Process**: The method then signs the JWT using a specified algorithm 
     *    and secret key or private key. This ensures the integrity and authenticity 
     *    of the token.
     * 
     * 3. **Return of JWT Token**: After the token is successfully created and signed, 
     *    the method returns the generated JWT as a string. This token can be used 
     *    for authenticating requests or securing communications.
     * 
     * @return A signed JWT as a string.
     */
    public String getJwtToken() {
        try {
        	serviceIdentityPrivateKey = serviceIdentityPrivateKey.replaceAll("\\s+", "");
            byte[] serviceIndentityKeyInBytes = Base64.getDecoder().decode(serviceIdentityPrivateKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(serviceIndentityKeyInBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(spec);
            Algorithm algorithm = Algorithm.RSA256(null, privateKey);
            String token = JWT.create()
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .withSubject(issuer)
                    .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRY)) // Expires in 1 hour
                    .sign(algorithm);
            return token;
        } catch (Exception e) {
            log.error("Failed to generate JWT token: {}", e.getMessage());
        }
        return null;
    }
}
