/*
  * (C) Koninklijke Philips Electronics N.V. 2023
  *
  * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
  * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
  * the copyright owner.
  *
  * File name: IssueDetailsCoding.java
*/

package com.philips.onespace.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IssueDetailsCoding {

	private String system;
    private String code;
    private String display;
	
}
