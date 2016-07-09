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

import java.io.File;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
/**
 * @author Nathie
 *
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class TableClass extends TitleAreaDialog{
	

		public TableClass(Shell parentShell) {
			super(parentShell);
		}

		@Override
		public void create() {
			super.create();
			setTitle("Error Report Archive");
//			setMessage(
//					"Thank you for enabling Developer studio automated error reporting tool. Please eneter your contact details.",
//					IMessageProvider.INFORMATION);
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite area = (Composite) super.createDialogArea(parent);
			Composite container = new Composite(area, SWT.NONE);
			container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			GridLayout layout = new GridLayout(1, false);
			container.setLayout(layout);

			createTable(container);
			
//			Composite container2 = new Composite(area, SWT.NONE);
//			container2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//			GridLayout layout2 = new GridLayout(1, false);
//			container2.setLayout(layout2);
			


			   

			return area;
		}

		private void createTable(Composite container) {

			    
			 Table table = new Table(container, SWT.BORDER | SWT.V_SCROLL
				        | SWT.H_SCROLL);
				    table.setHeaderVisible(true);
				    String[] titles = { "Error Report ID", "Date", "Time", "Status" };
				    
					 final Text text = new Text(container, SWT.BORDER);
					    text.setBounds(25, 240, 220, 250);

				    for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
				      TableColumn column = new TableColumn(table, SWT.NULL);
				      column.setText(titles[loopIndex]);
				    }

					File folder = new File(System.getProperty("user.dir"),"ErrorReports");
					File[] listOfFiles = folder.listFiles();
					
					int fileNo=listOfFiles.length;
					int counter=0;

					    for (int i = 0; i <fileNo ; i++) {
					      if (listOfFiles[i].isFile()) {
					        System.out.println("File " + listOfFiles[i].getName());
					        counter++;

					    }
					    
					    
				    for (int loopIndex = 0; loopIndex <counter ; loopIndex++) {
				      TableItem item = new TableItem(table, SWT.NULL);
				      item.setText("Item " + loopIndex);
				      item.setText(0, listOfFiles[loopIndex].getName());
				      item.setText(1, "Yes");
				      item.setText(2, "No");
				      item.setText(3, "A table item");
				    }

				    for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
				      table.getColumn(loopIndex).pack();
				    }

				    table.setBounds(25, 25, 220, 200);

				    table.addListener(SWT.Selection, new Listener() {
				      public void handleEvent(Event event) {
				          text.setText("You selected " + event.item);
				        
				      }
				    });
			
		}

   
		}}
		
