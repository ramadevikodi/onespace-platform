/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: Constants.java
 */

package com.philips.onespace.util;

public class Constants {

    public static final String SENTINEL_APP_NAME = "Sentinel";
    public static final String DRAFT = "Draft";
    public static final String AWAITING_BUSINESS_OWNER_APPROVAL = "Awaiting Business Owner Approval";
    public static final String REJECTED_BY_BUSINESS_OWNER = "Rejected by Business Owner";
    public static final String ON_HOLD_BY_BUSINESS_OWNER = "On Hold by Business Owner";
    public static final String LICENSES_LIMIT_CACHE_KEY = "_limit";
    public static final String SESSION_API_PATH = "/Session/**";
    public static final String ERROR_PATH = "/error";
    public static final String SESSION_COOKIE = "onespacecookie";
    public static final String SEMICOLON = ";";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    public static final String REGISTRATION = "registration";
    public static final String DEPLOYMENT = "deployment";
    public static final String STATUS = "status";
    public static final String ID = "id";
    public static final String OWNERSHIP = "ownership";
    public static final String AWAITING_MARKET_SOLUTION_OWNER_APPROVAL = "Awaiting Market/Solution Owner Approval";
    public static final String SOLUTION_OWNER_APPROVED = "Approved by Market/Solution Owner";
    public static final String DEFAULT_LOCALE = "en-US";
    public static final String DATE_FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC";
    public static final String USER_MANAGEMENT_APP_NAME = "User Management";
    public static final String DELETE_NOTIFICATION_CRON_EXP = "0 0 12 * * ?";
    public static final String EMAIL_NOT_SENT_ERR_CODE = "email_not_sent_error";
    public static final String PG_CHANNEL = "app_notifications";
    public static final String DATE_FORMAT_WITHOUT_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss";
    public static long NOTIFICATION_EVENT_LISTENER_DELAY = 5 * 1000;
    public static final String SSE_KEEP_ALIVE = "SSE_KEEP_ALIVE";

    public static final String HEADER_KEY_SET_COOKIE = "Set-Cookie";
    public static final String HEADER_KEY_COOKIE = "cookie";
    public static final String HEADER_KEY_ONESPACE_COOKIE = "onespacecookie";
    public static final String HEADER_KEY_CF_ID = "x-amz-cf-id";
    
    //Pagination configs
    public static final String PAGINATION_HEADER_PAGE = "page"; 
    public static final String PAGINATION_HEADER_SIZE = "size"; 
    public static final String PAGINATION_HEADER_SORT = "sort"; 
    public static final String PAGINATION_HEADER_TOTAL_PAGES = "Total-Pages"; 
    public static final String PAGINATION_HEADER_TOTAL_RECORDS = "Total-Records"; 

}

