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

/**
 * @author Nathie
 *
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {


	public PreferencePage()  {
	    super(GRID);

	  }

	  public void createFieldEditors() {
//	    addField(new DirectoryFieldEditor("PATH", "&Directory preference:",
//	        getFieldEditorParent()));
		  
		  //Group con2 = new Group(getFieldEditorParent(), SWT.SHADOW_OUT);
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
		  
		  con.setText("Conact Information");
		  ano.setText("Anonymization Options");
		  sop.setText("Sending Options");
		  
		  
		addField(new StringFieldEditor("NAME", "Name:",
			        con));
		addField(new StringFieldEditor("EMAIL", "Email:",
			        con));
		  
		addField(new BooleanFieldEditor("ANOPACK",
			        "&Anonymize package, class and method names", ano));
	    addField(new BooleanFieldEditor("ANOLOG",
	        "&Anonymize error log messages", ano));
	    
	    addField(new RadioGroupFieldEditor("SENDOPTIONS",
	        "Select the sending preferences", 1,
	        new String[][] { { "&Report the error in Jira", "Jira" },
	            { "&Report the error in Jira and send an email", "Email" } }, sop));
	    
		addField(new StringFieldEditor("USERNAME", "Username:",
		        sop));
		addField(new StringFieldEditor("PASSWORD", "Password:",
		        sop));

	  }

	  @Override
	  public void init(IWorkbench workbench) {
	    setPreferenceStore(Activator.getDefault().getPreferenceStore());
	    setDescription("Set default sending option for Developer Studio Error Reporting tool");
	  }
	} 