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


package org.wso2.developerstudio.eclipse.errorreporter.ui.menu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;
import org.wso2.developerstudio.eclipse.errorreporter.util.RemoteJiraConnector;

/**
 * @author Nathie
 *
 */
public class ReportArchive extends TitleAreaDialog {
	
	String id;
	String dateTime;
	String error;
	String status;
	String content;
	
	public ReportArchive(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Error Report Archive");
//		setMessage(
//				"Thank you for enabling Developer studio automated error reporting tool. Please eneter your contact details.",
//				IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(1, false);
		container.setLayout(layout);

		createTable(container);
		
//		Composite container2 = new Composite(area, SWT.NONE);
//		container2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		GridLayout layout2 = new GridLayout(1, false);
//		container2.setLayout(layout2);
		


		   

		return area;
	}

	private void createTable(Composite container) {

		    
		 final Table table = new Table(container, SWT.BORDER | SWT.V_SCROLL
			        | SWT.H_SCROLL);
			    table.setHeaderVisible(true);
			    String[] titles = { "Error Report ID", "ID", "Date & Time", "Error Message","Status" };
			    
			    // Create a multiple-line text field
			    final Text text = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
			    text.setLayoutData(new GridData(SWT.FILL, SWT.FILL,false, true));
				    

			    			    
			    final Menu contextMenu = new Menu(table);
				table.setMenu(contextMenu);
				MenuItem mItem1 = new MenuItem(contextMenu, SWT.None);
				mItem1.setText("Inquire status");

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
			      
			      try {
					readFile(listOfFiles[loopIndex].getPath());
				      item.setText(1, id);
				      item.setText(2, dateTime);
				      item.setText(3, error);
				      item.setText(4, status);
				      
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			    }

			    for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			      table.getColumn(loopIndex).pack();
			    }

			    table.setBounds(25, 25, 220, 200);

			    table.addListener(SWT.MouseDown, new Listener() {
			      public void handleEvent(Event event) {
			    	  String file=setText(table);
			          text.setText(getContent( file));
			          TableItem[] selection = table.getSelection();
			            if(selection.length!=0 && (event.button == 3)){
			                contextMenu.setVisible(true);
			            }

			      }
			    });
			    

		
	}
                                              

	}
	
	public String getStatus()
	{
		
		String targetURL="https://wso2.org/jira/rest/api/2/issue/TOOLS-3418?fields&expand";
		RemoteJiraConnector rj=new RemoteJiraConnector();
		String username=Activator.getDefault().getPreferenceStore().getString("JIRA_USERNAME");
		String password=Activator.getDefault().getPreferenceStore().getString("JIRA_PASSWORD");
		String userCredentials = username+":"+password;
		String st=rj.executeGet(targetURL, userCredentials);
		System.out.println(st);
		
		return st;
	}
	
	private String setText(Table table)
	{

        String file="";
        String temp;
        String temp2;
        TableItem[] selection = table.getSelection();
        
        for (int i = 0; i < selection.length; i++)
        {
        	temp=selection[i]+"";
        	temp2=temp.substring(11,30);
        	file = System.getProperty("user.dir")+"\\"+"ErrorReports"+"\\"+temp2+"";
        }
      
        return file;
	}
	
	protected String getContent(String fileName) {
    	
    	try {
			content = new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	private void readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {

	        
	        String line = br.readLine();
	        id=line.substring(9);

	        line = br.readLine();
	        dateTime=line.substring(6);

	        line = br.readLine();
	        error=line.substring(11);
	        
	        line = br.readLine();
	        status=line.substring(8);
	        
	        
	    } finally {
	        br.close();
	    }
	}
	
	

}
