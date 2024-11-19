/*
  * (C) Koninklijke Philips Electronics N.V. 2023
  *
  * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
  * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
  * the copyright owner.
  *
  * File name: ErrorResponseModel.java
*/

package com.philips.onespace.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.ToString;

@ToString
public class ErrorResponseModel {

	private String resourceType;
    private List<IssueItem> issue;

    public ErrorResponseModel(){
    	
    }

    public ErrorResponseModel(HttpStatus httpStatus, String errorMessage, String endPoint, String display, String diagnostics) {
        List<IssueItem> issues = new ArrayList<>();
        IssueItem issueItem = new IssueItem();
        String cdlNamespacePath = endPoint + "/operation-outcome";

        IssueDetailsCoding issueDetailsCoding = new IssueDetailsCoding();
        issueDetailsCoding.setCode(httpStatus.getReasonPhrase());
        issueDetailsCoding.setDisplay(display);
        issueDetailsCoding.setSystem(cdlNamespacePath);
        List<IssueDetailsCoding> issueDetailsCodingsList = new ArrayList<>();
        issueDetailsCodingsList.add(issueDetailsCoding);

        IssueDetails issueDetails = new IssueDetails();
        issueDetails.setText(errorMessage);
        issueDetails.setCoding(issueDetailsCodingsList);
        issueItem.setDetails(issueDetails);
        issueItem.setCode(httpStatus.getReasonPhrase());
        issueItem.setDiagnostics(diagnostics);
        issueItem.setSeverity("error");
        issues.add(issueItem);
        this.issue = issues;
        this.resourceType = "OperationOutcome";
    }



    public String getResourceType() {

        return resourceType;
    }

    public List<IssueItem> getIssue() {
        return issue != null ? new ArrayList<>(issue) : null;
    }

}
