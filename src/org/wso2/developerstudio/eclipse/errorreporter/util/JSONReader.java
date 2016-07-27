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


package org.wso2.developerstudio.eclipse.errorreporter.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Nathie
 *
 */
public class JSONReader {
	
	public String getJsonId(String jsonStr) throws JSONException
	{
		
			JSONObject jsonObj;

			jsonObj = new JSONObject(jsonStr);
	        String id = jsonObj.getString("id");
	        System.out.println(id);
//	        first = jsonObj.getJSONArray("arrArray").getJSONObject(0).getString("a");
//	        System.out.println(first);
	        
		return id;
	}
	
	public String getJsonKey(String jsonStr) throws JSONException {
		JSONObject jsonObj;

		jsonObj = new JSONObject(jsonStr);
        String key = jsonObj.getString("key");
        System.out.println(key);
        
	return key;
	
	}

}