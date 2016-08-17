/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.developerstudio.eclipse.errorreporter.constants;

/**
 * This class contains the Preference Page label constants used by
 * PreferencePage class.
 * 
 */
public class PreferencePageStrings {

	// description label
	public static final String DESCRIPTION = "Set default sending option for Developer Studio Error Reporting tool";

	// group labels
	public static final String CONTACT_INFO_GROUP = "Contact Information";
	public static final String SEND_OPTIONS_GROUP = "Sending Options";
	public static final String GMAIL_USER_CRED = "Gmail - Credentials";
	public static final String JIRA_USER_CRED = "Jira - Credentials";

	// contact information labels
	public static final String NAME = "NAME";
	public static final String EMAIL_USER = "EMAIL";
	public static final String ORGANIZATION = "ORGANIZATION";
	public static final String NAME_S = "Name:";
	public static final String EMAIL_USER_S = "Email:";
	public static final String ORGANIZATION_S = "Organization:";

	// send options labels
	public static final String SEND_OPTIONS = "SENDOPTIONS";
	public static final String SEND_OPTIONS_S = "Select the sending preferences";
	public static final String JIRA = "Jira";
	public static final String JIRA_S = "&Report the error in Jira";
	public static final String EMAIL = "Email";
	public static final String EMAIL_S = "&Report the error in Jira and send an email";

	// remote server - Jira labels
	public static final String SERVER_URL = "SERVER_URL";
	public static final String SERVER_URL_S = "Remote Jira URL:";
	public static final String PROJECT_KEY = "PROJECT_KEY";
	public static final String PROJECT_KEY_S = "Project Key:";

	// remote server - Email labels
	public static final String EMAIL_SERVER_URL = "EMAIL_URL";
	public static final String EMAIL_SERVER_URL_S = "Remote Email Publisher URL:";
	public static final String REC_EMAIL = "REC EMAIL";
	public static final String REC_EMAIL_S = "Recipient Email Address:";
}
