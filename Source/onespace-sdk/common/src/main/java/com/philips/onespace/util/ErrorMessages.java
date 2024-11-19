/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ErrorMessages.java
 */

package com.philips.onespace.util;

public class ErrorMessages {
	//Common config
	public static final String ERR_MESSGE_ACCESSDENIED = "err_message_accessdenied";
	
    public static final String HSP_IAM_CODE_ASSERTION_PARAMETER_MISSING = "Form parameter - code / assertion is not provided or empty";
    public static final String HSP_IAM_REDIRECT_URI_PARAMETER_MISSING = "Form parameter - redirectUri is not provided";
    public static final String CUSTOMER_NOT_FOUND = "customer_not_found";
    public static final String SESSION_COOKIE_AND_TOKEN_NOT_FOUND = "Authentication failed: both session cookie and bearer token are missing";
    public static final String ERROR_SOURCE = "application_discovery_service";
    public static final String VALIDATION_ERROR = "validation_error";
    public static final String CLIENT_ERROR = "client error";
    public static final String RESOURCE_NOT_FOUND = "resource_not_found";
    public static final String BAD_REQUEST = "bad_request";
    public static final String DUPLICATE_KEY = "duplicate_key";
    public static final String DATABASE_ERROR = "database_error";
    public static final String INVALID_TOKEN = "invalid_token";
    public static final String IAM_SOURCE = "IAM";
    public static final String HTTP_CLIENT_ERROR = "http_client_error";
    public static final String NO_RESOURCE = "no_resource";
    public static final String ARGUMENT_NOT_VALID = "argument_not_valid";
    public static final String DUPLICATE_APPLICATION_NAME = "duplicate_application_name";
    public static final String INVALID_APPLICATION_ID = "invalid_applicationId";
    public static final String INVALID_CATEGORY_ID = "invalid_category_id";
    public static final String INVALID_LANGUAGE_ID = "invalid_language_id";
    public static final String INVALID_MODALITY_ID = "invalid_modality_id";
    public static final String INVALID_SPECIALITY_ID = "invalid_speciality_id";
    public static final String INVALID_DEPLOYMENT_MODE_ID = "invalid_deployment_mode_id";
    public static final String INVALID_STATUS_ID = "invalid_status_id";
    public static final String INVALID_BUSINESS_UNIT_ID = "invalid_business_unit_id";
    public static final String NO_PROPOSITIONS_FOUND = "no_propositions_found";
    
    //App notification related err messages
    public static final String ERR_PAST_DATE_TIMESTAMP = "date_past_timestamp";
    public static final String ERR_PAST_DATE_EXPIRY = "date_past_expiry";
    public static final String ERR_SOURCE_APPLICATION_DOESNOT_EXIST = "source_app_doesnot_exist";
    public static final String MISSING_EMAIL_REQUEST_PARAM_ERR_CODE = "missing_email_request_param";
    public static final String LICENSE_USER_CONSUMED_ERR_CODE = "license_user_consumed";
    public static final String LICENSE_USER_NOT_CONSUMED_ERR_CODE = "license_user_not_consumed";
    public static final String ENTITLEMENT_NOT_FOUND_ERR_CODE = "entitlement_not_found";
    public static final String LICENSE_LIMIT_EXCEEDED_ERR_CODE = "license_limit_exceeded";
    public static final String MISSING_NOTIFICATION_ID_PARAM = "missing_notification_id_param";
    public static final String INVALID_NOTIFICATION_ID = "invalid_notification_id";
    public static final String NO_RECORDS_FOUND = "no_records_found";
    public static final String ENUM_RETRIEVAL_ERR_CODE = "enum_retrieval_error";
    public static final String MISSING_ACTION_UPDATE_PARAM_ERR_CODE = "missing_action_update_param";
    public static final String INVALID_ACTION_STATUS_ERR_CODE = "invalid_action_status";
    public static final String INVALID_ACTION_TYPE_ERR_CODE = "invalid_action_type";
    public static final String INVALID_ENUM_VALUE = "invalid_enum_value";
    public static final String INVALID_DATE_FORMAT = "invalid_date_format";
    public static final String INVALID_POTENTIAL_OWNER = "invalid_potential_owner";
    public static final String INVALID_INITIATOR = "invalid_initiator";
    public static final String INVALID_DUE_DATE = "invalid_due_date";
    public static final String INVALID_EXPIRY_DATE = "invalid_expiry_date";
    public static final String INVALID_DATE_TIME = "invalid_date_time";
    public static final String NOT_APPLICABLE_STATUS = "not_applicable_status";
    public static final String INVALID_API_VERSION = "invalid_api_version";
    public static final String INVALID_IAM_USER = "invalid_iam_user";
    public static final String INVALID_ID = "invalid_id";
    
    //ThirdParty application config
    public static final String NOTFOUND_NONONESPACEAPPLICATION_ID = "notfound_nononespaceapplication_id";
    public static final String NOTFOUND_IAMORG_ID = "notfound_iamorg_id";
    public static final String MAX_LIMIT_NONONESPACEAPPLICATION = "max_limit_nononespaceapplication";

}
