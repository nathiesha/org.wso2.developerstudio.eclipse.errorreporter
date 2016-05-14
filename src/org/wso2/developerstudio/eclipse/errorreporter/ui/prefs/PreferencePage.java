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
//import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;

/**
 * @author Nathie
 *
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * 
	 */
	public PreferencePage()  {
	    super(GRID);

	  }

	  public void createFieldEditors() {
//	    addField(new DirectoryFieldEditor("PATH", "&Directory preference:",
//	        getFieldEditorParent()));
		  
		  
		addField(new StringFieldEditor("NAME", "Name:",
			        getFieldEditorParent()));
		addField(new StringFieldEditor("EMAIL", "Email:",
			        getFieldEditorParent()));
		  
		addField(new BooleanFieldEditor("ANOPACK",
			        "&Anonymize package, class and method names", getFieldEditorParent()));
	    addField(new BooleanFieldEditor("ANOLOG",
	        "&Anonymize error log messages", getFieldEditorParent()));
	    
//	    addField(new RadioGroupFieldEditor("CHOICE",
//	        "An example of a multiple-choice preference", 1,
//	        new String[][] { { "&Choice 1", "choice1" },
//	            { "C&hoice 2", "choice2" } }, getFieldEditorParent()));
	    

	  }

	  @Override
	  public void init(IWorkbench workbench) {
	    setPreferenceStore(Activator.getDefault().getPreferenceStore());
	    setDescription("Set default sending option for Developer Studio Error Reporting tool");
	  }
	} 