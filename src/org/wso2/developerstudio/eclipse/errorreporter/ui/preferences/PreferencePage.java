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
import org.wso2.developerstudio.eclipse.errorreporter.constants.PreferencePageStrings;

/**
 * This class creates the Error reporter Preference Page and its contents
 * 
 */

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {
		super(GRID);

	}

	/**
	 * Thismethod creates the Error reporter Preference Page
	 * 
	 */
	@Override
	public void init(IWorkbench workbench) {

		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(PreferencePageStrings.DESCRIPTION);
	}

	/**
	 * This method creates the Error reporter Preference Page contents
	 * 
	 */
	@Override
	public void createFieldEditors() {

		Composite composite = new Composite(getFieldEditorParent(), SWT.LEFT);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);

		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout());
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;

		Group userInfoGroup = new Group(composite, SWT.SHADOW_OUT);
		Group sendOptionsGroup = new Group(composite, SWT.SHADOW_OUT);
		Group serverInfoGroup = new Group(composite, SWT.SHADOW_OUT);
		Group gmailInfoGroup = new Group(composite, SWT.SHADOW_OUT);

		userInfoGroup.setText(PreferencePageStrings.CONTACT_INFO_GROUP);
		sendOptionsGroup.setText(PreferencePageStrings.SEND_OPTIONS_GROUP);
		serverInfoGroup.setText(PreferencePageStrings.JIRA_USER_CRED);
		gmailInfoGroup.setText(PreferencePageStrings.GMAIL_USER_CRED);

		addField(new StringFieldEditor(PreferencePageStrings.NAME, PreferencePageStrings.NAME_S, userInfoGroup));
		addField(new StringFieldEditor(PreferencePageStrings.EMAIL_USER, PreferencePageStrings.EMAIL_USER_S, userInfoGroup));
		addField(new StringFieldEditor(PreferencePageStrings.ORGANIZATION, PreferencePageStrings.ORGANIZATION_S, userInfoGroup));

		addField(new RadioGroupFieldEditor(PreferencePageStrings.SEND_OPTIONS, PreferencePageStrings.SEND_OPTIONS_S, 1,
				new String[][] { { PreferencePageStrings.JIRA_S, PreferencePageStrings.JIRA },
						{ PreferencePageStrings.EMAIL_S, PreferencePageStrings.EMAIL } },
				sendOptionsGroup));

		addField(new StringFieldEditor(PreferencePageStrings.SERVER_URL, PreferencePageStrings.SERVER_URL_S, serverInfoGroup));
		addField(new StringFieldEditor(PreferencePageStrings.PROJECT_KEY, PreferencePageStrings.PROJECT_KEY_S, serverInfoGroup));

		addField(new StringFieldEditor(PreferencePageStrings.EMAIL_SERVER_URL, PreferencePageStrings.EMAIL_SERVER_URL_S,
				gmailInfoGroup));
		addField(new StringFieldEditor(PreferencePageStrings.REC_EMAIL, PreferencePageStrings.REC_EMAIL_S, gmailInfoGroup));

	}

}