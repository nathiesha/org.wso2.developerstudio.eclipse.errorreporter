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

package org.wso2.developerstudio.eclipse.errorreporter.ui.archivemenu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;
import org.wso2.developerstudio.eclipse.errorreporter.constants.PreferencePageStrings;
import org.wso2.developerstudio.eclipse.errorreporter.constants.ProjectConstants;
import org.wso2.developerstudio.eclipse.errorreporter.constants.ReportArchiveLabels;
import org.wso2.developerstudio.eclipse.errorreporter.util.IssueStatusChecker;

/**
 * This class contains the code to create the Report Archive window, which
 * displays previously reported errors to Jira.
 */

public class ReportArchive extends TitleAreaDialog {

	// The colomns that are displayed in the error Reports table
	String key;
	String dateTime;
	String error;
	String content;
	String id;

	public ReportArchive(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * This method sets the title and message of the Report Archive window.
	 */
	@Override
	public void create() {
		super.create();
		setTitle(ReportArchiveLabels.REPORT_ARCHIVE_TITLE);
		setMessage(ReportArchiveLabels.REPORT_ARCHIVE_MESSAGE, IMessageProvider.INFORMATION);
	}

	/**
	 * This method creates the contents of the Report Archive window.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(1, false);
		container.setLayout(layout);

		createTable(container);
		return area;

	}

	/**
	 * This method creates the table that contains the error reports.
	 * 
	 * @param container
	 */
	private void createTable(Composite container) {

		// create a new table
		final Table table = new Table(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		table.setHeaderVisible(true);

		// set the table labels
		String[] titles = { ReportArchiveLabels.TABLE_LABEL_1, ReportArchiveLabels.TABLE_LABEL_2,
				ReportArchiveLabels.TABLE_LABEL_3, ReportArchiveLabels.TABLE_LABEL_4 };

		// Create a multiple-line text field
		final Text text = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

		// create a context menu option for each item-Inquire status
		final Menu contextMenu = new Menu(table);
		table.setMenu(contextMenu);
		MenuItem menuItem = new MenuItem(contextMenu, SWT.None);
		menuItem.setText(ReportArchiveLabels.CONTEXT_MENU_TEXT);

		// set labels for each colomn
		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(titles[loopIndex]);
		}

		// get the list of files in error folder
		File folder = new File(System.getProperty("user.dir"), ProjectConstants.ERROR_REPORT_DIRECTORY);
		File[] listOfFiles = folder.listFiles();

		int fileNo = listOfFiles.length;
		int counter = 0;

		// check whether all the content in the folder are files
		for (int i = 0; i < fileNo; i++) {
			if (listOfFiles[i].isFile()) {
				counter++;

			}
		}

		// set the field values for each table item
		for (int loopIndex = 0; loopIndex < counter; loopIndex++) {
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText("Item " + loopIndex);
			item.setText(0, listOfFiles[loopIndex].getName());

			readFile(listOfFiles[loopIndex].getPath());
			item.setText(1, key);
			item.setText(2, dateTime);
			item.setText(3, error);

		}

		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			table.getColumn(loopIndex).pack();
		}

		// add a selection listener to the table item
		table.addListener(SWT.MouseDown, new Listener() {

			// display the error report contents of the selected table item
			@Override
			public void handleEvent(Event event) {

				// get file name and fil content
				String file = getFileName(table);
				String content = (getContent(file));

				// display the contet in the text area
				text.setText(content);

				TableItem[] selection = table.getSelection();
				if (selection.length != 0 && (event.button == 3)) {
					contextMenu.setVisible(true);
				}

			}
		});

		// add a listener to menu item
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// get the idof the selected table item and get its current
				// status
				TableItem[] selection = table.getSelection();
				TableItem tb = selection[0];
				String id = tb.getText(1);
				getStatus(id);

			}
		});

	}

	/**
	 * This method is called when the user selects the Inquire status option of
	 * the menu
	 * 
	 * @param id
	 */
	public void getStatus(String id) {

		String status = "";

		// get remote server url from preferences page
		String targetURL = Activator.getDefault().getPreferenceStore().getString(PreferencePageStrings.STATUS_URL) + id;

		String jsonResponse = IssueStatusChecker.executeGet(targetURL);

		if (jsonResponse != "Error") {
			// extract the status from the JSON response
			status = IssueStatusChecker.getIssueStatus(jsonResponse);

		}

		else {
			status = "Not Found.";
		}

		// opens up a message box to display the status to the user
		Shell shell = new Shell();
		MessageBox box = new MessageBox(shell);
		box.setText(ReportArchiveLabels.DIALOG_BOX_TITLE);
		box.setMessage(status);
		box.open();

	}

	/**
	 * This method contains logic to extract the error id of the selected table
	 * itme and set the file name using that.
	 * 
	 * @param table
	 * @return the filepath of the selected table item
	 */
	private String getFileName(Table table) {

		String filePath = "";
		String tableItemId;
		String fileName;
		TableItem[] selection = table.getSelection();

		// extract the error id from each selection
		for (int i = 0; i < selection.length; i++) {
			tableItemId = selection[i] + "";
			fileName = tableItemId.substring(11, 16);

			// get the file name of the selected table item
			filePath = System.getProperty("user.dir") + "\\" + ProjectConstants.ERROR_REPORT_DIRECTORY + "\\" + fileName
					+ ".txt";
		}

		return filePath;
	}

	/**
	 * This method contains logic to read all the contents of the text report
	 * 
	 * @param fileName
	 * @return The contents of the file
	 */
	protected String getContent(String fileName) {

		try {
			content = new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (IOException e) {

			Shell shell = new Shell();
			MessageBox messageBox = new MessageBox(shell);
			messageBox.setText("Error!");
			messageBox.setMessage("File not found in the given location.");
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * This method contains the logic to extract the information like eror key,
	 * error id, date time and error message from each error report.
	 * 
	 * @param fileName
	 * 
	 *            This method has a dependancy on the format of the text report
	 *            generated. in case the format of the error report canges, this
	 *            method needs to be updated as well
	 * 
	 */
	private void readFile(String fileName) {

		BufferedReader bufferredReader = null;
		try {
			// get the file
			bufferredReader = new BufferedReader(new FileReader(fileName));
			String line = bufferredReader.readLine();

			// set the key value from the file
			line = bufferredReader.readLine();
			key = line.substring(11);

			// set the id value from the file
			line = bufferredReader.readLine();
			id = line.substring(10);

			// set the dae time value from the file
			line = bufferredReader.readLine();
			dateTime = line.substring(6);

			// skip lines until the reader reaches the error message
			line = bufferredReader.readLine();
			line = bufferredReader.readLine();
			line = bufferredReader.readLine();
			line = bufferredReader.readLine();
			line = bufferredReader.readLine();
			line = bufferredReader.readLine();
			line = bufferredReader.readLine();
			line = bufferredReader.readLine();
			line = bufferredReader.readLine();
			line = bufferredReader.readLine();

			error = line.substring(9);
		} catch (FileNotFoundException e) {

			Shell shell = new Shell();
			MessageBox messageBox = new MessageBox(shell);
			messageBox.setText("Error!");
			messageBox.setMessage("File not found in the given location.");

		} catch (IOException e) {

			Shell shell = new Shell();
			MessageBox messageBox = new MessageBox(shell);
			messageBox.setText("Error!");
			messageBox.setMessage("Error report format changed!");
		} finally {
			try {
				bufferredReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
