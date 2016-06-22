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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
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



public class ErrorNotificationDialog extends ErrorDialog {

	private static final String NESTING_INDENT = " ";
	private static final String DIALOG_TITLE="A problem was detected";
	private static final String DIALOG_MESSAGE= "An unexpected error occured. Please press send to report the error to the development team";
	private static final int DISPLAY_MASK = 0xFFFF;

	private String filePath;
	private IStatus status;

	private List list;
	private boolean listCreated = false;

	private Button detailsButton;
	private Clipboard clipboard;
	private BufferedReader br;


	public ErrorNotificationDialog(Shell parentShell, 
			String filePath,IStatus status) {
		super(parentShell, DIALOG_TITLE, DIALOG_MESSAGE, status, DISPLAY_MASK);

		this.filePath = filePath;
		this.status=status;

	}


	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(DIALOG_TITLE);
	}


	@Override
	public int open() {

		super.open();
		return getReturnCode();

	}


	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK CANCEL and Details buttons
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL,
				true);
		detailsButton = createButton(parent, IDialogConstants.DETAILS_ID,
				IDialogConstants.SHOW_DETAILS_LABEL, false);
	}


	@Override
	protected List createDropDownList(Composite parent) {
		// create the list
		list = new List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.MULTI);
		// fill the list
		try {
			addReportInfo(list,filePath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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




	private void addReportInfo(List listToPopulate, String fileName) throws IOException {

		StringBuffer sb = new StringBuffer();
		sb.append(readFile(fileName));

		java.util.List<String> lines = readLines(sb.toString());
		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
			String line = iterator.next();
			listToPopulate.add(line);
		}

	}

	private String readFile(String fileName) throws IOException {
		br = new BufferedReader(new FileReader(fileName));

			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();}

			return sb.toString();

	}


	private  java.util.List<String> readLines(final String s) throws IOException {
		java.util.List<String> lines = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new StringReader(s));
		String line;

			while ((line = reader.readLine()) != null) {
				if (line.length() > 0)
					lines.add(line);
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
		populateCopyBuffer(status, statusBuffer, 0);
		clipboard = new Clipboard(list.getDisplay());
		clipboard.setContents(new Object[] { statusBuffer.toString() },
				new Transfer[] { TextTransfer.getInstance() });
	}

	private void populateCopyBuffer(IStatus buildingStatus,
			StringBuffer buffer, int nesting) {
		if (!buildingStatus.matches(DISPLAY_MASK)) {
			return;
		}
		for (int i = 0; i < nesting; i++) {
			buffer.append(NESTING_INDENT);
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
				buffer.append(NESTING_INDENT);
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



}
