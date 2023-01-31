package com.login.demo.constants;

/**
 * Contains all endpoint information inside this class. The purpose of this class is to allow other classes to refer to
 * the application's endpoints without needing to hardcode any URL links.
 * @implNote The values in this class should NOT be changed unless needed.
 * If any values are to be changed, then ensure that all the path links in the HTML files (in resources)
 * are still valid.
 */
public class URLConstants {
//    ALL PATHS AND SUBDIRECTORIES SHOULD START WITH A '/' FOR CONSISTENCY
//    WARNING: if any paths or subdirectories are changed, then the new paths must be updated in the HTML files (the href values for
//    any HTML file)

//   === PATHS AND SUBDIRECTORIES ===
    public final static String CONTROLLER_PATH_PREFIX = "/api/v1";
    public final static String REGISTRATION_PATH_SUBDIRECTORY = "/registration";
    public final static String CONFIRMATION_PATH_SUBDIRECTORY = "/registration/confirm";
    public final static String HOMEPAGE_PATH_SUBDIRECTORY = "/homepage";

//  === PARAMETERS ===
    public final static String TOKEN_REQ_PARAMETER = "token";
}
