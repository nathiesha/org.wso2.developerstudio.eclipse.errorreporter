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

package org.wso2.developerstudio.eclipse.errorreporter.ui.preferences;

//import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final String DESCRIPTION = "Set default sending option for Developer Studio Error Reporting tool";

	// group labels
	private static final String CONTACT_INFO_GROUP = "Contact Information";
	private static final String SEND_OPTIONS_GROUP = "Sending Options";
	private static final String GMAIL_USER_CRED = "Gmail - Credentials";
	private static final String JIRA_USER_CRED = "Jira - Credentials";

	// contact information strings
	public static final String NAME = "NAME";
	private static final String EMAIL_USER = "EMAIL";
	public static final String ORGANIZATION = "ORGANIZATION";
	private static final String NAME_S = "Name:";
	private static final String EMAIL_USER_S = "Email:";
	public static final String ORGANIZATION_S= "Organization:";

	// annonymize options strings
//	public static final String ANO_PACK = "ANOPACK";
//	private static final String ANO_PACK_S = "&Anonymize package, class and method names";
//	public static final String ANO_LOG = "ANOLOG";
//	private static final String ANO_LOG_S = "&Anonymize error log messages";

	// send options Strings
	public static final String SEND_OPTIONS = "SENDOPTIONS";
	private static final String SEND_OPTIONS_S = "Select the sending preferences";
	public static final String JIRA = "Jira";
	private static final String JIRA_S = "&Report the error in Jira";
	public static final String EMAIL = "Email";
	private static final String EMAIL_S = "&Report the error in Jira and send an email";

	public static final String SERVER_URL = "SERVER_URL";
	private static final String SERVER_URL_S = "Remote Jira URL:";
	public static final String PROJECT_KEY = "PROJECT_KEY";
	private static final String PROJECT_KEY_S = "Project Key:";
	
	// Gmail user credentials
//	private static final String GMAIL_USERNAME = "GMAIL USERNAME";
//	private static final String GMAIL_USERNAME_S = "Gmail Username:";
//	private static final String GMAIL_PASSWORD = "GMAIL PASSWORD";
//	private static final String GMAIL_PASSWORD_S = "Password:";
	public static final String EMAIL_SERVER_URL = "EMAIL_URL";
	private static final String EMAIL_SERVER_URL_S = "Remote Email Publisher URL:";
	private static final String REC_EMAIL = "REC EMAIL";
	private static final String REC_EMAIL_S = "Recipient Email Address:";

	// Jira user credentials
//	public static final String JIRA_URL = "JIRA_URL";
//	private static final String JIRA_URL_S = "Remote Jira URL:";
//	public static final String PROJECT_KEY = "PROJECT_KEY";
//	private static final String PROJECT_KEY_S = "Project Key:";
//	private static final String JIRA_USERNAME = "JIRA_USERNAME";
//	private static final String JIRA_USERNAME_S = "Username:";
//	private static final String JIRA_PASSWORD = "JIRA_PASSWORD";
//	private static final String JIRA_PASSWORD_S = "Password:";

	public PreferencePage() {
		super(GRID);

	}

	@Override
	public void createFieldEditors() {

		Composite top = new Composite(getFieldEditorParent(), SWT.LEFT);

		// Sets the layout data for the top composite's
		// place in its parent's layout.

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		top.setLayoutData(data);

		// Sets the layout for the top composite's
		// children to populate.
		top.setLayout(new GridLayout());

		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		Group con = new Group(top, SWT.SHADOW_OUT);
		Group sop = new Group(top, SWT.SHADOW_OUT);	
		Group rem = new Group(top, SWT.SHADOW_OUT);
		Group guc = new Group(top, SWT.SHADOW_OUT);
//		Group juc = new Group(top, SWT.SHADOW_OUT);

		// set text to groups
		con.setText(CONTACT_INFO_GROUP);
		sop.setText(SEND_OPTIONS_GROUP);
		rem.setText(JIRA_USER_CRED);
		guc.setText(GMAIL_USER_CRED);
//		juc.setText(JIRA_USER_CRED);

		addField(new StringFieldEditor(NAME, NAME_S, con));
		addField(new StringFieldEditor(EMAIL_USER, EMAIL_USER_S, con));
		addField(new StringFieldEditor(ORGANIZATION, ORGANIZATION_S, con));

		addField(new RadioGroupFieldEditor(SEND_OPTIONS, SEND_OPTIONS_S, 1,
				new String[][] { { JIRA_S, JIRA }, { EMAIL_S, EMAIL } }, sop));

		addField(new StringFieldEditor(SERVER_URL, SERVER_URL_S, rem));
		addField(new StringFieldEditor(PROJECT_KEY, PROJECT_KEY_S, rem));
		
		addField(new StringFieldEditor(EMAIL_SERVER_URL, EMAIL_SERVER_URL_S, guc));
		addField(new StringFieldEditor(REC_EMAIL, REC_EMAIL_S, guc));
		
//		addField(new StringFieldEditor(GMAIL_USERNAME, GMAIL_USERNAME_S, guc));
//		StringFieldEditor passwordGmail = new StringFieldEditor(GMAIL_PASSWORD, GMAIL_PASSWORD_S, guc) {
//
//			@Override
//			protected void doFillIntoGrid(Composite parent, int numColumns) {
//				super.doFillIntoGrid(parent, numColumns);
//
//				getTextControl().setEchoChar('*');
//			}
//
//		};
//		addField(passwordGmail);
//
//		addField(new StringFieldEditor(REC_EMAIL, REC_EMAIL_S, guc));
//
//		addField(new StringFieldEditor(JIRA_URL, JIRA_URL_S, juc));
//		addField(new StringFieldEditor(PROJECT_KEY, PROJECT_KEY_S, juc));
//		addField(new StringFieldEditor(JIRA_USERNAME, JIRA_USERNAME_S, juc));
//		StringFieldEditor passwordJira = new StringFieldEditor(JIRA_PASSWORD, JIRA_PASSWORD_S, juc) {
//
//			@Override
//			protected void doFillIntoGrid(Composite parent, int numColumns) {
//				super.doFillIntoGrid(parent, numColumns);
//
//				getTextControl().setEchoChar('*');
//			}
//
//		};
//		addField(passwordJira);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(DESCRIPTION);
	}

	public Boolean setValues(String key, String value) {
		Activator.getDefault().getPreferenceStore().setValue(key, value);
		super.performApply();
		return super.performOk();

	}

}