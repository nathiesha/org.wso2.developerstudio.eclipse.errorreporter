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


package org.wso2.developerstudio.eclipse.errorreporter.ui.dialog;

/**
 * @author Nathie
 *
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.wso2.developerstudio.eclipse.errorreporter.other.ErrorInformation;


public class ErrorNotificationDialog extends ErrorDialog {

	private static final boolean shouldIncludeTopLevelErrorInDetails = false;
	private static final Object NESTING_INDENT = " ";
	private List list;
	private boolean listCreated = false;
	private ErrorInformation status;
	private IStatus statusI;
	private Button detailsButton;
	private Clipboard clipboard;
	private int displayMask = 0xFFFF;
	private String title;
	//private int value=0;
	
	public ErrorNotificationDialog(Shell parentShell, String dialogTitle, String message,
			ErrorInformation status, int displayMask,IStatus statusI) {
		super(parentShell, message, message, statusI, displayMask);
		this.title = dialogTitle == null ? JFaceResources
				.getString("A problem was detected!") : //$NON-NLS-1$
				dialogTitle;
		this.message = message == null ? status.getMessage()
				: JFaceResources
						.format(
								"An unexpected error occured. Please press OK to report the error to the development team\n\n", new Object[] { message, status.getMessage() }); //$NON-NLS-1$
		this.status = status;
		this.displayMask = displayMask;
		this.statusI=statusI;

	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}
	
	@Override
	protected void buttonPressed(int id) {
		if (id == IDialogConstants.DETAILS_ID) {
			// was the details button pressed?
			toggleDetailsArea();
		} else {
			buttonPress(id);
		}
	}

	private void buttonPress(int id) {
		if(id==IDialogConstants.CANCEL_ID)
		{
			setReturnCode(CANCEL);
			close();
		}
		
		if(id==IDialogConstants.OK_ID)
		{
			setReturnCode(OK);
			close();
		}
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK CANCEL and Details buttons
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL,
				true);
		createDetailsButton(parent);
	}
	

	@Override
	protected void createDetailsButton(Composite parent) {
		if (shouldShowDetailsButton()) {
			detailsButton = createButton(parent, IDialogConstants.DETAILS_ID,
					IDialogConstants.SHOW_DETAILS_LABEL, false);
		}
	}
	

	public static int openError(Shell parent, String dialogTitle,
			String message, ErrorInformation status,Status statusI) {
		return openError(parent, dialogTitle, message, status, IStatus.OK
				| IStatus.INFO | IStatus.WARNING | IStatus.ERROR, statusI);
	}

	
	public static int openError(Shell parentShell, String title,
			String message, ErrorInformation status, int displayMask,Status statusI) {
		ErrorNotificationDialog dialog = new ErrorNotificationDialog(parentShell, title, message,
				status, displayMask,statusI);
		return dialog.open();
	}

	@Override
	protected List createDropDownList(Composite parent) {
		// create the list
		list = new List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.MULTI);
		// fill the list
		populateList(list);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL
				| GridData.GRAB_VERTICAL);
		data.heightHint = 150;
		data.horizontalSpan = 2;
		list.setLayoutData(data);
		list.setFont(parent.getFont());
		Menu copyMenu = new Menu(list);
		MenuItem copyItem = new MenuItem(copyMenu, SWT.NONE);
		copyItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				copyToClipboard();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				copyToClipboard();
			}
		});
		copyItem.setText(JFaceResources.getString("copy")); //$NON-NLS-1$
		list.setMenu(copyMenu);
		listCreated = true;
		return list;
	}

	private void populateList(List listToPopulate) {
		
		addReportInfo(listToPopulate,status);
		populateList(listToPopulate, statusI, 0,
				shouldIncludeTopLevelErrorInDetails);
	}

	private void addReportInfo(List listToPopulate, ErrorInformation status2) {
		StringBuffer sb = new StringBuffer();
		sb.append("Info-----------------------------------------------------");
		java.util.List<String> lines = readLines(sb.toString());
		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
			String line = iterator.next();
			listToPopulate.add(line);
		}
		
	}
	
	private void populateList(List listToPopulate,IStatus statusI,
			int nesting, boolean includeStatus) {

		if (statusI.equals(null)) {
			return;
		}

		Throwable t = statusI.getException();
		boolean incrementNesting = false;

		if (includeStatus) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < nesting; i++) {
				sb.append(NESTING_INDENT);
			}
			String message = statusI.getMessage();
			sb.append(message);
			java.util.List<String> lines = readLines(sb.toString());
			for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
				String line = iterator.next();
				listToPopulate.add(line);
			}
			incrementNesting = true;
		}
		if (!(t instanceof CoreException) && t != null) {
			// Include low-level exception message
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < nesting; i++) {
				sb.append(NESTING_INDENT);
			}
			String message = t.getLocalizedMessage();
			if (message == null) {
				message = t.toString();
			}

			sb.append(message);
			listToPopulate.add(sb.toString());
			incrementNesting = true;
		}

		if (incrementNesting) {
			nesting++;
		}

		// Look for a nested core exception
		if (t instanceof CoreException) {
			CoreException ce = (CoreException) t;
			IStatus eStatus = ce.getStatus();
			// Only print the exception message if it is not contained in the
			// parent message
			if (message == null || message.indexOf(eStatus.getMessage()) == -1) {
				populateList(listToPopulate, eStatus, nesting, true);
			}
		}

		// Look for child status
		IStatus[] children = statusI.getChildren();
		for (int i = 0; i < children.length; i++) {
			populateList(listToPopulate, children[i], nesting, true);
		}
	}


	private static java.util.List<String> readLines(final String s) {
		java.util.List<String> lines = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new StringReader(s));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0)
					lines.add(line);
			}
		} catch (IOException e) {
			// shouldn't get this
		}
		return lines;
	}

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
		// increase the dialog height if details were opened and such increase is necessary
		// decrease the dialog height if details were closed and empty space appeared
		if ((opened && diffY > 0) || (!opened && diffY < 0)) {
			getShell().setSize(new Point(windowSize.x, windowSize.y + (diffY)));
		}
	}
	

	private void copyToClipboard() {
		if (clipboard != null) {
			clipboard.dispose();
		}
		StringBuffer statusBuffer = new StringBuffer();
		populateCopyBuffer(statusI, statusBuffer, 0);
		clipboard = new Clipboard(list.getDisplay());
		clipboard.setContents(new Object[] { statusBuffer.toString() },
				new Transfer[] { TextTransfer.getInstance() });
	}
	
	private void populateCopyBuffer(IStatus buildingStatus,
			StringBuffer buffer, int nesting) {
		if (!buildingStatus.matches(displayMask)) {
			return;
		}
		for (int i = 0; i < nesting; i++) {
			buffer.append(NESTING_INDENT);
		}
		buffer.append(buildingStatus.getMessage());
		buffer.append("\n"); //$NON-NLS-1$

		// Look for a nested core exception
		Throwable t = buildingStatus.getException();
		if (t instanceof CoreException) {
			CoreException ce = (CoreException) t;
			populateCopyBuffer(ce.getStatus(), buffer, nesting + 1);
		} else if (t != null) {
			// Include low-level exception message
			for (int i = 0; i < nesting; i++) {
				buffer.append(NESTING_INDENT);
			}
			String message = t.getLocalizedMessage();
			if (message == null) {
				message = t.toString();
			}
			buffer.append(message);
			buffer.append("\n"); //$NON-NLS-1$
		}

		IStatus[] children = buildingStatus.getChildren();
		for (int i = 0; i < children.length; i++) {
			populateCopyBuffer(children[i], buffer, nesting + 1);
		}
	}
	
	@Override
	public int open() {
		
			super.open();
			return getReturnCode();

	}


}
