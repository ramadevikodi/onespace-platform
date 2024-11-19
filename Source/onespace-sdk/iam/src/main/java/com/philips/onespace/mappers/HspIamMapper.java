/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: HspIamMapper.java
 */

package com.philips.onespace.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.philips.onespace.model.Group;
import com.philips.onespace.model.GroupsResponse;
import com.philips.onespace.model.User;
import com.philips.onespace.model.UserResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HspIamMapper {

	/**
	 * This method maps an HSP IAM group response to a group model.
	 * 
	 * The process involves the following steps:
	 * 
	 * 1. **Mapping Process**: The method maps the data from the HSP IAM group response to 
	 *    the corresponding fields in the group model. This involves translating the 
	 *    structure and data types from the response to fit the model.
	 * 
	 * 2. **Return of Group Model**: After successful mapping, the method returns the 
	 *    populated group model that can be used within the application.
	 * 
	 * @param iamGroups The HSP IAM group response object that needs to be mapped.
	 * @return A list of group model populated with data from the HSP IAM group response.
	 */
	public List<Group> mapIamGroupResponseToModel(GroupsResponse iamGroups) {
		List<Group> groups = null;
		try {
			if (null != iamGroups.getEntry()) {
				groups = iamGroups.getEntry().stream().map(entry -> {
					return Group.builder().id(entry.getResource().getId()).name(entry.getResource().getGroupName())
							.build();
				}).collect(Collectors.toList());
			}
		} catch (Exception expObj) {
			log.error("Exception: mapIamGroupResponseToModel, Exception Details: ", expObj);
		}
		return groups;
	}

	/**
	 * This method maps an IAM users response to an User model.
	 * 
	 * The process involves the following steps:
	 * 
	 * 1. **Mapping Process**: The method then maps the data from the HSP IAM users response 
	 *    to the corresponding fields in the User model. This includes transforming 
	 *    the structure and data types to match the User model.
	 * 
	 * 2. **Return of IamUsers Model**: After successful mapping, the method returns the 
	 *    populated User model, ready for use within the application.
	 * 
	 * @param iamUsers The HSP IAM users response object that needs to be mapped.
	 * @return An list of User model populated with data from the IAM users response.
	 */
	public List<User> mapIamUsersResponseToModel(UserResponse iamUsers) {
		List<User> users = null;
		try {
			if (null != iamUsers.getUrnGroup() && null != iamUsers.getUrnGroup().getGroupMembers() && null != iamUsers.getUrnGroup().getGroupMembers().getResources()) {
				users = iamUsers.getUrnGroup().getGroupMembers().getResources().stream().map(entry -> {
					return User.builder().id(entry.getId()).userName(entry.getUserName()).userEmail(entry.getUserName()).build();
				}).collect(Collectors.toList());
			}
		} catch (Exception expObj) {
			log.error("Exception: mapIamUsersResponseToModel, Exception Details: ", expObj);
		}
		return users;
	}
}
