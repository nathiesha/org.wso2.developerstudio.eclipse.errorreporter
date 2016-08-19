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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.wso2.developerstudio.eclipse.errorreporter.constants.DialogBoxLabels;
import org.wso2.developerstudio.eclipse.errorreporter.constants.ProjectConstants;

/**
 * This class contains logic create and display the contents of the Error
 * Notification Dialog, which is opened when an error is detected.
 */

public class ErrorNotificationDialog extends ErrorDialog {

	private String errorMessage;
	private IStatus status;

	private List list;
	private boolean listCreated = false;

	private Button detailsButton;
	private Clipboard clipboard;
	private int selection = 0;

	/**
	 * This constructor calls its super class constructor and sets values to the
	 * errorMessage and status fields, which are required t create a report to
	 * display.
	 * 
	 * @param parentShell
	 * @param errorMessage
	 * @param status
	 */
	public ErrorNotificationDialog(Shell parentShell, String errorMessage, IStatus status) {
		super(parentShell, DialogBoxLabels.DIALOG_TITLE, DialogBoxLabels.DIALOG_MESSAGE, status,
				DialogBoxLabels.DISPLAY_MASK);
		this.errorMessage = errorMessage;
		this.status = status;

	}

	/**
	 * This method sets the title of the dialog box.
	 * 
	 * @param shell
	 */

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(DialogBoxLabels.DIALOG_TITLE);
		// Create the first Group
	}

	/**
	 * This method open up the Dialog Box.
	 * 
	 * @return the user reponse(OK/CANCEL)
	 */

	@Override
	public int open() {

		super.open();
		return getReturnCode();

	}

	/**
	 * This method creates the contents in the Dialog Box.
	 * 
	 * @param parent
	 * 
	 */

	@Override
	protected void createButtonsForButtonBar(Composite parent) {

		// labels and radio buttons to select the sending options
		Label label = new Label(parent, SWT.NULL);
		label.setText(DialogBoxLabels.SENDING_OPTIONS_LABEL);

		// create radio button-Publish in Jira
		Button jira = new Button(parent, SWT.RADIO);
		jira.setText(DialogBoxLabels.SENDING_OPTIONS_JIRA);
		jira.addSelectionListener(new SelectionListener() {

			// if Jira option is selected, return int is set to zero
			@Override
			public void widgetSelected(SelectionEvent e) {
				selection = 0;

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				selection = 0;
			}

		});

		// create radio button-Publish in Jira and Email
		Button email = new Button(parent, SWT.RADIO);
		email.setText(DialogBoxLabels.SENDING_OPTIONS_JIRA_EMAIL);
		email.addSelectionListener(new SelectionListener() {

			// if Email option is selected, return int is set to one
			@Override
			public void widgetSelected(SelectionEvent e) {
				selection = 1;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				selection = 1;

			}

		});

		// create a link to edit prefernces
		final Link link = new Link(parent, SWT.NONE);
		link.setFont(parent.getFont());
		link.setText("<A>" + DialogBoxLabels.PREFERENCE_PAGE_LINK + "</A>");
		GridData data = new GridData(SWT.LEFT, SWT.TOP, false, false);
		data.horizontalSpan = 3;
		link.setLayoutData(data);

		// open up the preferences page
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				Shell shell = new Shell();
				PreferenceDialog pref = PreferencesUtil.createPreferenceDialogOn(shell,
						ProjectConstants.PREFERENCE_PAGE, null, null);
				if (pref != null)
					pref.open();
			}
		});

		// create OK CANCEL and Details buttons
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
		detailsButton = createButton(parent, IDialogConstants.DETAILS_ID, IDialogConstants.SHOW_DETAILS_LABEL, false);
	}

	/**
	 * This method contains the logic to create the contents in the Error Report
	 * text area
	 * 
	 * @param parent
	 */

	@Override
	protected List createDropDownList(Composite parent) {
		// create the list
		list = new List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		// fill the list {
		try {
			addReportInfo(list, errorMessage);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
		data.heightHint = 150;
		data.horizontalSpan = 2;
		list.setLayoutData(data);
		list.setFont(parent.getFont());
		Menu copyMenu = new Menu(list);
		MenuItem copyItem = new MenuItem(copyMenu, SWT.NONE);
		copyItem.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				copyToClipboard();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				copyToClipboard();
			}
		});
		copyItem.setText(JFaceResources.getString("copy")); //$NON-NLS-1$
		list.setMenu(copyMenu);
		listCreated = true;
		return list;
	}

	/**
	 * This method adds the contents of the errorReport string, to the
	 * listToPopulate list
	 * 
	 * @param listToPopulate
	 * @param errorReport
	 */

	private void addReportInfo(List listToPopulate, String errorReport) throws IOException {

		// converts the string to a stringBuffer
		StringBuffer sb = new StringBuffer();
		sb.append(errorReport);

		// add each line to the list
		java.util.List<String> lines = readLines(sb.toString());
		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
			String line = iterator.next();
			listToPopulate.add(line);
		}

	}

	/**
	 * This method contains logic to convert a string to a List of strings
	 */

	private java.util.List<String> readLines(final String errorReport) throws IOException {
		java.util.List<String> lines = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new StringReader(errorReport));
		String line;

		while ((line = reader.readLine()) != null) {
			if (line.length() > 0)
				lines.add(line);
		}

		return lines;
	}

	/**
	 * This method contains the logic to execute when the details button is
	 * pressed. This method shows and hides the error report.
	 */

	private void toggleDetailsArea() {
		boolean opened = false;
		Point windowSize = getShell().getSize();
		if (listCreated) {
			list.dispose();
			listCreated = false;
			detailsButton.setText(IDialogConstants.SHOW_DETAILS_LABEL);
			opened = false;
		} else {
			list = createDropDownList((Composite) getContents());
			detailsButton.setText(IDialogConstants.HIDE_DETAILS_LABEL);
			getContents().getShell().layout();
			opened = true;
		}
		Point newSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int diffY = newSize.y - windowSize.y;
		// increase the dialog height if details were opened and such increase
		// is necessary
		// decrease the dialog height if details were closed and empty space
		// appeared
		if ((opened && diffY > 0) || (!opened && diffY < 0)) {
			getShell().setSize(new Point(windowSize.x, windowSize.y + (diffY)));
		}
	}

	/**
	 * This method creates the text area in the ErrorNotificationDialog and
	 * displays the error report in that text area.
	 * 
	 * @param buildingStatus
	 * @param buffer
	 * @param nesting
	 * 
	 */

	private void populateCopyBuffer(IStatus buildingStatus, StringBuffer buffer, int nesting) {

		if (!buildingStatus.matches(DialogBoxLabels.DISPLAY_MASK)) {
			return;
		}
		for (int i = 0; i < nesting; i++) {
			buffer.append(DialogBoxLabels.NESTING_INDENT);
		}
		buffer.append(buildingStatus.getMessage());
		buffer.append("\n");

		// Look for a nested core exception
		Throwable t = buildingStatus.getException();
		if (t instanceof CoreException) {
			CoreException ce = (CoreException) t;
			populateCopyBuffer(ce.getStatus(), buffer, nesting + 1);
		} else if (t != null) {
			// Include low-level exception message
			for (int i = 0; i < nesting; i++) {
				buffer.append(DialogBoxLabels.NESTING_INDENT);
			}
			String message = t.getLocalizedMessage();
			if (message == null) {
				message = t.toString();
			}
			buffer.append(message);
			buffer.append("\n");
		}

		IStatus[] children = buildingStatus.getChildren();
		for (int i = 0; i < children.length; i++) {
			populateCopyBuffer(children[i], buffer, nesting + 1);
		}
	}

	/**
	 * This method is called when a button is pressed If the Details button is
	 * pressed, toggleDetailsArea method is called else buttonPress method is
	 * called
	 * 
	 * @param buttonId
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.DETAILS_ID) {
			// was the details button pressed?
			toggleDetailsArea();
		} else {
			// directs to the buttonPress method
			buttonPress(buttonId);
		}
	}

	/**
	 * This method sets the return codes based on the button user presses.
	 * 
	 * @param buttonId
	 */
	private void buttonPress(int buttonId) {
		// if cancel button is pressed
		if (buttonId == IDialogConstants.CANCEL_ID) {
			setReturnCode(CANCEL);
			close();
		}
		// if OK button is presses and 1st radio button(Jira) is selected
		if (buttonId == IDialogConstants.OK_ID && selection == 0) {
			setReturnCode(100);
			close();
		}
		// if OK button is presses and 2nd radio button(email) is selected
		if (buttonId == IDialogConstants.OK_ID && selection == 1) {
			setReturnCode(200);
			close();
		}
	}

	/**
	 * This method provides functionality for the user to copy the error report
	 * using a context menu
	 */

	private void copyToClipboard() {
		if (clipboard != null) {
			clipboard.dispose();
		}
		StringBuffer statusBuffer = new StringBuffer();
		populateCopyBuffer(status, statusBuffer, 0);
		clipboard = new Clipboard(list.getDisplay());
		clipboard.setContents(new Object[] { statusBuffer.toString() }, new Transfer[] { TextTransfer.getInstance() });
	}

}
