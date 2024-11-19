/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: AppDiscoveryApplication.java
 */

package com.philips.onespace.appdiscoveryframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = { "com.philips.onespace.*" })
@EntityScan(basePackages = "com.philips.onespace.jpa.entity")
@EnableJpaRepositories(basePackages = "com.philips.onespace.jpa.repository")
@EnableScheduling
@EnableAspectJAutoProxy
public class AppDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppDiscoveryApplication.class, args);
	}

}
