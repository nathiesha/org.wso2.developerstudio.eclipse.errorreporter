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

package org.wso2.developerstudio.eclipse.errorreporter.ui.dialogs;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.wso2.developerstudio.eclipse.errorreporter.constants.DialogBoxLabels;
import org.wso2.developerstudio.eclipse.errorreporter.constants.ProjectConstants;

/**
 * This class creates the Dialog Box to be displayed, when an error occurs, if
 * the user has not set vales to the mandatory fields in the prefernces Page
 */

public class EditPreferencesDialog extends TitleAreaDialog {

	public EditPreferencesDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle(DialogBoxLabels.EDIT_PREFERENCES_TITLE);
		setMessage(DialogBoxLabels.EDIT_PREFERENCES_MESSAGE, IMessageProvider.INFORMATION);
	}

	/**
	 * This method creates contents of the Dialog Box
	 * 
	 * @param parent
	 * @return Control
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		// creates the link to the preferences page
		createLink(container);

		return area;
	}

	/**
	 * This method creates a link to the preferencs page of Developer Studio
	 * error reporting tool.
	 * 
	 * @param container
	 */

	private void createLink(Composite container) {

		final Link link = new Link(container, SWT.NONE);
		link.setFont(container.getFont());
		link.setText("<A>" + DialogBoxLabels.EDIT_PREFERENCES_LINK + "</A>");
		GridData data = new GridData(SWT.LEFT, SWT.TOP, false, false);
		data.horizontalSpan = 3;
		link.setLayoutData(data);

		// directs the user to the prefernce page, once the link is pressed
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				Shell shell = new Shell();
				PreferenceDialog preferencePage = PreferencesUtil.createPreferenceDialogOn(shell,
						ProjectConstants.PREFERENCE_PAGE, null, null);
				if (preferencePage != null)
					preferencePage.open();
			}
		});
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}

}
