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


import org.eclipse.core.runtime.preferences.ConfigurationScope;


public class Preferences {

	private org.osgi.service.prefs.Preferences preferences;
	private org.osgi.service.prefs.Preferences sub1;
	
	public Preferences(String node) {
		preferences = ConfigurationScope.INSTANCE.getNode(node);
		sub1 = preferences.node("");
	}
	
	
	public void setPreferenceValue(String key, String value) {
		
		sub1.put(key, value);
		try {
		  // forces the application to save the preferences
		  preferences.flush();

		  } catch (org.osgi.service.prefs.BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	public String getPreferenceKey(String key) {
		return sub1.get(key, "");
	}

	
}
