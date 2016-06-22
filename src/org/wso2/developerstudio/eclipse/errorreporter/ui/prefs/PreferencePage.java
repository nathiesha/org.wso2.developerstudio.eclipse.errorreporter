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


package org.wso2.developerstudio.eclipse.errorreporter.ui.prefs;

import org.eclipse.jface.preference.BooleanFieldEditor;
//import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
//import org.eclipse.jface.preference.RadioGroupFieldEditor;
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
	
	private static final String DESCRIPTION="Set default sending option for Developer Studio Error Reporting tool";
	
	//group labels
	private static final String CONTACT_INFO_GROUP="Conact Information";
	private static final String ANO_OPTIONS_GROUP="Anonymization Options";
	private static final String SEND_OPTIONS_GROUP="Sending Options";
	private static final String GMAIL_USER_CRED="Gmail User Credentials";
	private static final String JIRA_USER_CRED="Jira User Credentials";
	
	//contact information strings
	private static final String NAME="NAME";
	private static final String EMAIL_USER="EMAIL";
	private static final String NAME_S="Name:";
	private static final String EMAIL_USER_S="Email:";
	
	//annonymize options strings
	private static final String ANO_PACK="ANOPACK";
	private static final String ANO_PACK_S="&Anonymize package, class and method names";
	private static final String ANO_LOG="ANOLOG";
	private static final String ANO_LOG_S="&Anonymize error log messages";
	
	//send options Strings
	private static final String SEND_OPTIONS="SENDOPTIONS";
	private static final String SEND_OPTIONS_S="Select the sending preferences";
	private static final String JIRA="Jira";
	private static final String JIRA_S="&Report the error in Jira";
	private static final String EMAIL="Email";
	private static final String EMAIL_S="&Report the error in Jira and send an email";
	
	//Gmail user credentials
	private static final String GMAIL_USERNAME="GMAIL USERNAME";
	private static final String GMAIL_USERNAME_S="Gmail Username:";
	private static final String GMAIL_PASSWORD="GMAIL PASSWORD";
	private static final String GMAIL_PASSWORD_S="Password:";
	private static final String REC_EMAIL="REC EMAIL";
	private static final String REC_EMAIL_S="Recipient Email Address:";
	
	//Jira user credentials
	private static final String JIRA_URL="JIRA_URL";
	private static final String JIRA_URL_S="Remote Jira URL:";
	private static final String JIRA_USERNAME="JIRA_USERNAME";
	private static final String JIRA_USERNAME_S="Username:";
	private static final String JIRA_PASSWORD="JIRA_PASSWORD";
	private static final String JIRA_PASSWORD_S="Password:";


	public PreferencePage()  {
	    super(GRID);

	  }

	  public void createFieldEditors() {

		  Composite top = new Composite(getFieldEditorParent(), SWT.LEFT);
		  
			// Sets the layout data for the top composite's 
			// place in its parent's layout.
		  
		  GridData data=new GridData(GridData.FILL_HORIZONTAL);
		  top.setLayoutData(data);

			// Sets the layout for the top composite's 
			// children to populate.
		  top.setLayout(new GridLayout());
		  
		  data.horizontalAlignment = GridData.FILL;
		  data.grabExcessHorizontalSpace = true;
		  Group con = new Group(top, SWT.SHADOW_OUT);
		  Group ano = new Group(top, SWT.SHADOW_OUT);
		  Group sop = new Group(top, SWT.SHADOW_OUT);
		  Group guc = new Group(top, SWT.SHADOW_OUT);
		  Group juc = new Group(top, SWT.SHADOW_OUT);
		  
		  //set text to groups
		  con.setText(CONTACT_INFO_GROUP);
		  ano.setText(ANO_OPTIONS_GROUP);
		  sop.setText(SEND_OPTIONS_GROUP);
		  guc.setText(GMAIL_USER_CRED);
		  juc.setText(JIRA_USER_CRED);
		  
		  
		addField(new StringFieldEditor(NAME, NAME_S,
			        con));
		addField(new StringFieldEditor(EMAIL_USER,EMAIL_USER_S ,
			        con));
		
		
		  
		addField(new BooleanFieldEditor(ANO_PACK,
			        ANO_PACK_S, ano));
	    addField(new BooleanFieldEditor(ANO_LOG,
	    			ANO_LOG_S, ano));
	    
	    
	    
	    
	    addField(new RadioGroupFieldEditor(SEND_OPTIONS,
	        SEND_OPTIONS_S, 1,
	        new String[][] { { JIRA_S, JIRA },
	            { EMAIL_S, EMAIL } }, sop));
	    
	    
	    
	    
		addField(new StringFieldEditor(GMAIL_USERNAME,GMAIL_USERNAME_S ,
		        guc));
		addField(new StringFieldEditor(GMAIL_PASSWORD,GMAIL_PASSWORD_S ,
				guc));
		addField(new StringFieldEditor(REC_EMAIL,REC_EMAIL_S ,
				guc));
		
		
		addField(new StringFieldEditor(JIRA_URL,JIRA_URL_S ,
				juc));
		addField(new StringFieldEditor(JIRA_USERNAME,JIRA_USERNAME_S ,
		        juc));
		addField(new StringFieldEditor(JIRA_PASSWORD,JIRA_PASSWORD_S ,
				juc));

		
		

	  }

	  @Override
	  public void init(IWorkbench workbench) {
	    setPreferenceStore(Activator.getDefault().getPreferenceStore());
	    setDescription(DESCRIPTION);
	  }
	  

	  public void initializeDefaultPreferences() {
	    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		 // store.setDefault(NAME, "hello");
	  }
	  


	} 