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
import org.json.JSONException;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;
//import org.wso2.developerstudio.eclipse.errorreporter.publishers.JiraPublisher;
import org.wso2.developerstudio.eclipse.errorreporter.util.JiraStatusChecker;


/**
 * @author Nathie
 *
 */
public class ReportArchive extends TitleAreaDialog {
	
	String key;
	String dateTime;
	String error;
	String content;
	String id;
	
	public ReportArchive(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Error Report Archive");
		setMessage(
				"This windows displays the errors published to Jira",
				IMessageProvider.INFORMATION);
	}

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

	//create the contents of error report table
	private void createTable(Composite container) {

		 final Table table = new Table(container, SWT.BORDER | SWT.V_SCROLL
			        | SWT.H_SCROLL);
			    table.setHeaderVisible(true);
			    String[] titles = { "Error Report ID", "ID", "Date & Time", "Error Message" };
			    
			    // Create a multiple-line text field
			    final Text text = new Text(container,  SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
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
				System.out.println(fileNo);
				int counter=0;

				    for (int i = 0; i <fileNo ; i++) {
				      if (listOfFiles[i].isFile()) {
				        counter++;

				    }
				    }

			    for (int loopIndex = 0; loopIndex <counter ; loopIndex++) {
			      TableItem item = new TableItem(table, SWT.NULL);
			      item.setText("Item " + loopIndex);
			      item.setText(0, listOfFiles[loopIndex].getName());
			      
			      try {
					readFile(listOfFiles[loopIndex].getPath());
				      item.setText(1, key);
				      item.setText(2, dateTime);
				      item.setText(3, error);

				      
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			    }
				    

			    for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			      table.getColumn(loopIndex).pack();
			    }

			  //  table.setBounds(25, 25, 220, 200);

			    table.addListener(SWT.MouseDown, new Listener() {
			      public void handleEvent(Event event) {


			    	  String file=setText(table);
			    	  String content=(getContent( file));
			          text.setText(content);
			          TableItem[] selection = table.getSelection();
			            if(selection.length!=0 && (event.button == 3)){
			                contextMenu.setVisible(true);
			            }

			      }
			    });
			    
			    mItem1.addSelectionListener(new SelectionAdapter() {
			        public void widgetSelected(SelectionEvent e) {
			        	try {
							getStatus("TOOLS-3418");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

			        }});
                                              

			    }
	
				    
	
	public void getStatus(String id) throws IOException, JSONException
	{
		String status2="";
		String targetURL="https://wso2.org/jira/rest/api/2/issue/"+id+"?fields&expand";
		String username=Activator.getDefault().getPreferenceStore().getString("JIRA_USERNAME");
		String password=Activator.getDefault().getPreferenceStore().getString("JIRA_PASSWORD");
		String userCredentials = username+":"+password;
		JiraStatusChecker checker=new JiraStatusChecker();
		String jsonResponse=checker.executeGet(targetURL, userCredentials);

        status2=checker.getIssueStatus(jsonResponse);

        Shell shell=new Shell();
		MessageBox box=new MessageBox(shell);
		box.setText("Issue status");
		box.setMessage(status2);
		box.open();

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
        	//System.out.println(temp);
        	temp2=temp.substring(11,15);
        	
        	file = System.getProperty("user.dir")+"\\"+"ErrorReports"+"\\"+temp2+".txt";
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
	        line = br.readLine();
	        key=line.substring(11);
	        
	        line = br.readLine();
	        id = line.substring(10);

	        line = br.readLine();
	        dateTime=line.substring(6);

	        line = br.readLine();
	        line = br.readLine();
	        line = br.readLine();
	        line = br.readLine();	        
	        line = br.readLine();
	        line = br.readLine();
	        line = br.readLine();
	        line = br.readLine();
	        line = br.readLine();	        
	        line = br.readLine();
	        
	        error=line.substring(9);
	        
	        
	        
	    } finally {
	        br.close();
	    }
	}
	
	

}
