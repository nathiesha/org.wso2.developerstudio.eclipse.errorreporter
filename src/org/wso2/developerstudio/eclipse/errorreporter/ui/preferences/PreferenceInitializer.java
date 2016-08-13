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

package org.wso2.developerstudio.eclipse.errorreporter.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.wso2.developerstudio.eclipse.errorreporter.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	private static final String TARGET_URL = "http://test4475-jira-publisher-test-1-0-0.wso2apps.com/publish";
	private static final String PROJECT_KEY = "TOOLS";

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		// store.setDefault(PreferencePage.ANO_PACK, false);
		// store.setDefault(PreferencePage.ANO_LOG, false);
		store.setDefault(PreferencePage.JIRA, true);
		store.setDefault(PreferencePage.EMAIL, false);

		store.setDefault(PreferencePage.SERVER_URL, TARGET_URL);
		store.setDefault(PreferencePage.PROJECT_KEY, PROJECT_KEY);

	}

}
