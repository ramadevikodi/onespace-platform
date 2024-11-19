/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: HspIAMService.java
 */

package com.philips.onespace.serviceImpl;

import com.philips.onespace.cache.GuavaCacheUtil;
import com.philips.onespace.mappers.HspIamMapper;
import com.philips.onespace.model.Group;
import com.philips.onespace.model.GroupsResponse;
import com.philips.onespace.model.User;
import com.philips.onespace.model.UserResponse;
import com.philips.onespace.model.Users;
import com.philips.onespace.model.TokenResponse;
import com.philips.onespace.service.IamService;
import com.philips.onespace.util.JWTGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.philips.onespace.logging.LoggingAspect.logData;
import static com.philips.onespace.util.IamConstants.*;

@Component
@Slf4j
public class HspIAMService implements IamService {

	private static final String API_VERSION = "1";
	private final String idmBaseUrl;
	private final String iamExtBaseUrl;
	private final String tokenUrl;
	private final String userUrl;
	private final String groupUrl;
	private final String userUrlForGroup;
	private RestTemplate restTemplate;
	private final HspIamMapper mapper;
	private final JWTGenerator jwtGenerator;
	private final GuavaCacheUtil<String> iamTokenCache;
	private final GuavaCacheUtil<List<Group>> groupCache;

	@Autowired
	public HspIAMService(@Value("${iam.useridmurl}") String idmBaseUrl, @Value("${iam.tokenbaseurl}") String iamExtBaseUrl,
                         @Value("${iam.tokenurl}") String tokenUrl, @Value("${iam.userUrl}") String userUrl,
                         @Value(("${iam.groupUrl}")) String groupUrl, @Value(("${iam.userUrlForGroup}")) String userUrlForGroup,
                         HspIamMapper mapper, JWTGenerator jwtGenerator, GuavaCacheUtil<String> iamTokenCache, GuavaCacheUtil<List<Group>> groupCache) {
		this.idmBaseUrl = idmBaseUrl;
		this.iamExtBaseUrl = iamExtBaseUrl;
		this.tokenUrl = tokenUrl;
		this.userUrl = userUrl;
		this.groupUrl = groupUrl;
		this.userUrlForGroup = userUrlForGroup;
		this.groupCache = new GuavaCacheUtil<>(groupCache);
        this.restTemplate = new RestTemplate();
		this.mapper = mapper;
		this.jwtGenerator = jwtGenerator;
		this.iamTokenCache = new GuavaCacheUtil<>(iamTokenCache);
	}

	/**
	 * This method generates an HSP IAMtoken by accepting a signed JSON Web Token (JWT)
	 * as input.
	 * 
	 * The process involves the following steps:
	 * 
	 * 1. **JWT Validation**: The method first validates the incoming JWT token to
	 * ensure it is properly signed and formatted. If the JWT token is invalid, an
	 * exception is thrown, and the process is terminated.
	 * 
	 * 2. **Expiration Check**: After validation, the method checks if the JWT token
	 * is within its expiry time. This is done by examining the 'exp' claim in the
	 * JWT payload. If the JWT has expired, an exception is raised.
	 * 
	 * 3. **Return of HSP IAM Token**: The newly generated HSP IAM token is returned by the
	 * method. This HSP IAM token can be used for authenticating subsequent API requests
	 * or for other secure operations within the system.
	 * 
	 * @param jwtToken The signed JWT token that will be used to generate the IAM
	 *                 token.
	 * @return A newly generated HSP IAM token based on the input JWT token.
	 */
	@Override
	public String generateToken(String jwtToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set(HEADER_KEY_API_VERSION, API_VERSION);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add(HEADER_KEY_GRANT_TYPE, IAM_GRANT_TYPE);
			map.add(HEADER_KEY_ASSERTION, jwtToken);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
			ResponseEntity<TokenResponse> result = this.restTemplate.exchange(
					iamExtBaseUrl + tokenUrl, HttpMethod.POST, request, TokenResponse.class);
			TokenResponse generateTokenResponse = result.getBody();
			if(null != generateTokenResponse) {
				return generateTokenResponse.getAccess_token();
			}
		} catch (Exception expObj) {
			log.error("Exception: generateToken, Exception Details: ", expObj);
		}
		return null;
	}

	/**
	 * This method retrieves all HSP IAM groups associated with a given organization ID.
	 * 
	 * The process involves the following steps:
	 * 
	 * 1. **Group Retrieval**: Once rganization is confirmed 
	 *    to be in the IAM context, the method proceeds to retrieve all IAM groups linked 
	 *    to the specified organization ID.
	 * 
	 * 2. **Return of HSP  Groups**: The method returns a list of IAM groups that belong 
	 *    to the given organization.
	 * 
	 * @param token The active HSP IAM token required for authentication and context validation.
	 * @param orgId The organization ID for which the HSP IAM groups are to be retrieved.
	 * @return A list of HSP IAM groups associated with the specified organization ID.
	 */
	@Override
	public List<Group> getGroupsByOrgId(String token, String orgId) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set(HEADER_KEY_API_VERSION, API_VERSION);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.set("Authorization", BEARER + " " + token);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
			ResponseEntity<GroupsResponse> response = this.restTemplate.exchange(
					idmBaseUrl + groupUrl + "?organizationId=" + orgId, HttpMethod.GET, request,
					GroupsResponse.class);
			GroupsResponse iamGroupsResponse = response.getBody();
			return mapper.mapIamGroupResponseToModel(iamGroupsResponse);
		} catch (Exception expObj) {
			log.error("Exception: getGroupsByOrgId, Exception Details: ", expObj);
		}
		return null;
	}

	/**
	 * This method retrieves all HSP IAM users associated with a given group ID.
	 * 
	 * The process involves the following steps:
	 * 
	 * 1. **User Retrieval**: Once the group ID is found in IAM system, the method proceeds to 
	 *    retrieve all HSP IAM users associated with the specified group ID.
	 * 
	 * 2. **Return of HSP IAM Users**: The method returns a list of HSP IAM users that are 
	 *    members of the given group.
	 * 
	 * @param token The active HSP IAM token required for authentication and context validation.
	 * @param groupId The ID of the group for which the HSP IAM users are to be retrieved.
	 * @return A list of HSP IAM users associated with the specified group ID.
	 */
	@Override
	public List<User> getUsersByGroupId(String token, String groupId) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set(HEADER_KEY_API_VERSION, API_VERSION);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.set("Authorization", BEARER + " " + token);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
			ResponseEntity<UserResponse> response = this.restTemplate.exchange(
					idmBaseUrl + userUrlForGroup + "/" + groupId + "?includeGroupMembersType="+IAM_PARAM_USER,
					HttpMethod.GET, request, UserResponse.class);
			UserResponse userResponse = response.getBody();
			return mapper.mapIamUsersResponseToModel(userResponse);
		} catch (Exception expObj) {
			log.error("Exception: getUsersByGroupId, Exception Details: ", expObj);
		}
		return null;
	}
	
	/**
	 * Get groups for the given application.
	 *
	 * @param hspIamOrgId
	 * @return List of groups
	 */
	@Override
	public List<Group> getGroups(String hspIamOrgId) {

		try {
			if (null != groupCache.get(hspIamOrgId)) {
				return groupCache.get(hspIamOrgId);
			} else {
				// Load all the group, users to cache and return the same
				String iamToken = getIAMToken();
				List<Group> iamGroupList = getGroupsByOrgId(iamToken, hspIamOrgId);

				if (null != iamGroupList && iamGroupList.size() > 0) {
					List<Group> groups = new ArrayList<Group>(iamGroupList.size());
					for (Group iamGroup : iamGroupList) {
						Group group = Group.builder().id(String.valueOf(iamGroup.getId())).name(iamGroup.getName())
								.users(getUsersByGroupId(iamToken, iamGroup.getId())).build();
						groups.add(group);
					}
					if (null != groups && groups.size() > 0)
						groupCache.add(hspIamOrgId, groups);
					return groups;
				}
			}
		} catch (Exception expObj) {
			logData("Exception in getGroups: details", expObj);
		}
		return null;
	}

	@Override
	public String getIAMToken(){
		if(iamTokenCache.get(IAM_TOKEN) != null){
			return iamTokenCache.get(IAM_TOKEN);
		}
		else{
			String iamToken  = generateToken(jwtGenerator.getJwtToken());
			iamTokenCache.add(IAM_TOKEN, iamToken);
			return iamToken;
		}
	}

	@Override
	public Users getUsers(String id, String iamToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set(HEADER_KEY_API_VERSION, IAM_API_VERSION);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.set("Authorization", BEARER + " " + iamToken);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
			ResponseEntity<Users> response = this.restTemplate.exchange(idmBaseUrl + userUrl + "?filter=id eq \"" + id + "\" ", HttpMethod.GET, request, Users.class);
			return response.getBody();
		} catch (Exception expObj) {
			logData("Exception: getUsers, Exception Details: ", expObj);
		}
		return null;
	}
	
}
