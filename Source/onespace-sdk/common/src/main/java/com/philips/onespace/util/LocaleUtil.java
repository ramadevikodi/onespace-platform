/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: LocaleUtil.java
 */

package com.philips.onespace.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.philips.onespace.jpa.repository.LanguageRepository;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class LocaleUtil {
	private Locale locale;
	private RestTemplate restTemplate;
	private String userIdmUrl;
	private String userUrl;

	@Autowired
	private LanguageRepository languageRepository;

	public LocaleUtil(@Value("${iam.useridmurl}") String userIdmUrl, @Value("${iam.userUrl}") String userUrl) {
		this.userIdmUrl = userIdmUrl;
		this.userUrl = userUrl;
		this.restTemplate = new RestTemplate();
	}

	public boolean isLocaleValid(String locale) {
		return (!locale.equalsIgnoreCase("en-US") && languageRepository.existsByLanguageCodeIgnoreCase(locale));
	}

	public String getUserPreferredLanguage(String token, String id) {
		final String authorization = "Bearer " + token;
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authorization);
		headers.set("Api-version", "1");
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(userIdmUrl + userUrl + "/" + id);
		JsonNode result = this.restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, JsonNode.class)
				.getBody();
		if (result != null && result.has("preferredLanguage")) {
			return result.get("preferredLanguage").asText();
		}
		return Constants.DEFAULT_LOCALE;
	}

	public Locale getLanguage() {
		String[] parts = locale.getLanguage().split("-");
		String language = parts[0];
		String country = parts.length > 1 ? parts[1] : "";
		return new Locale(language, country);
	}
}
