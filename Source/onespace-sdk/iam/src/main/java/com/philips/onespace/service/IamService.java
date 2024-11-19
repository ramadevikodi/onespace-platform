/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: IamService.java
 */

package com.philips.onespace.service;

import java.util.List;

import com.philips.onespace.model.Group;
import com.philips.onespace.model.User;
import com.philips.onespace.model.Users;

public interface IamService {

	/**
	 * This method generates an IAM token by accepting a signed JSON Web Token (JWT)
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
	 * 3. **Return of IAM Token**: The newly generated IAM token is returned by the
	 * method. This IAM token can be used for authenticating subsequent API requests
	 * or for other secure operations within the system.
	 * 
	 * @param jwtToken The signed JWT token that will be used to generate the IAM
	 *                 token.
	 * @return A newly generated IAM token based on the input JWT token.
	 */
	String generateToken(String jwtToken);

	/**
	 * This method retrieves all IAM groups associated with a given organization ID.
	 * 
	 * The process involves the following steps:
	 * 
	 * 1. **Group Retrieval**: Once rganization is confirmed 
	 *    to be in the IAM context, the method proceeds to retrieve all IAM groups linked 
	 *    to the specified organization ID.
	 * 
	 * 2. **Return of IAM Groups**: The method returns a list of IAM groups that belong 
	 *    to the given organization.
	 * 
	 * @param token The active IAM token required for authentication and context validation.
	 * @param orgId The organization ID for which the IAM groups are to be retrieved.
	 * @return A list of IAM groups associated with the specified organization ID.
	 */
	List<Group> getGroupsByOrgId(String token, String orgId);

	/**
	 * This method retrieves all IAM users associated with a given group ID.
	 * 
	 * The process involves the following steps:
	 * 
	 * 1. **User Retrieval**: Once the group ID is found in IAM system, the method proceeds to 
	 *    retrieve all IAM users associated with the specified group ID.
	 * 
	 * 2. **Return of IAM Users**: The method returns a list of IAM users that are 
	 *    members of the given group.
	 * 
	 * @param token The active IAM token required for authentication and context validation.
	 * @param groupId The ID of the group for which the IAM users are to be retrieved.
	 * @return A list of IAM users associated with the specified group ID.
	 */
	List<User> getUsersByGroupId(String token, String groupId);
	
	/**
	 * Get groups for the given application.
	 *
	 * @param hspIamOrgId
	 * @return List of groups
	 */
	List<Group> getGroups(String hspIamOrgId);

	Users getUsers(String id, String iamToken);

	String getIAMToken();

}
