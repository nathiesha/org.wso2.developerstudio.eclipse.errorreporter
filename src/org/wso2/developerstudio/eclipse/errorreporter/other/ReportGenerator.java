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


package org.wso2.developerstudio.eclipse.errorreporter.other;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONObject;
/**
 * @author Nathie
 *
 */
public class ReportGenerator {
	
//	String[][] systemInformation;
//	String[][] userInformation;
	ErrorInformation errorInformation;
	
	public ReportGenerator(ErrorInformation errorInformation	) {		
		super();
		this.errorInformation=errorInformation;
	}


	@SuppressWarnings("unchecked")
	public JSONObject createIssue(Integer projectKey, Integer issuetypeName, String summary, String description) {
        
		JSONObject fields = new JSONObject();
		
        fields.put("project", new JSONObject().put("key", projectKey.toString()));
        fields.put("issuetype", new JSONObject().put("name", issuetypeName.toString()));
        fields.put("summary", summary);
        fields.put("description", description);
 
        JSONObject issue = new JSONObject();
        issue.put("fields", fields);

 
        return issue;
    }
	
	public void createReport() throws IOException
	{
		String path="F:\\eclipseProjects\\org.wso2.developerstudio.eclipse.errorreporter\\in.txt";
        File file = new File(path);

           // if file doesnt exists, then create it
           if (!file.exists()) {
               file.createNewFile();
           }
		
//           ArrayList<String[][]> arrayFile = new ArrayList<String[][]>(); 
//		    arrayFile.add( "Ramesh Tendulkar", "2008-12-31");
//		    arrayFile.add("John Machleyn", "2008-12-31");


//		    FileReader fr = new FileReader("in.txt");
//		    BufferedReader br =new BufferedReader(fr); 
//		    String s;
//		    while((s = br.readLine()) != null) { 
//		        String[] spS = s.split("|")[1].split("+"); 
//		        arrayFile.add(new StackFile(spS[0],spS[1] ,spS[2]));
//		        } 
//		        fr.close(); 

		    FileWriter fw = new FileWriter(file);

//		    fw.write("+--------+-------------------------------+-------------+\n");
//		    fw.write("| ID     | Name                          | Date        |\n");
//		    fw.write("+--------+-------------------------------+-------------+\n");

//		    Iterator<String[][]> itS = arrayFile.iterator();
		    
//		    int length=errorInformation.length;
//
//		    for(int i=0;i<length;i++)
//		    {
//		        //String[][] sf = itS.next();
//		        fw.write("| "+String.format("%-6s", errorInformation[i][0])+" | "+String.format("%-30s", errorInformation[i][1])+" |\n");
////		        fw.write((itS.hasNext())
////		                ?"|--------|-------------------------------|-------------|\n"
////		                :"+--------+-------------------------------+-------------+\n");
//
//		    }
		    
		    fw.close();
	}
}