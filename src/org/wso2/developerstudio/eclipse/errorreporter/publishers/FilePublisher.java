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

package org.wso2.developerstudio.eclipse.errorreporter.publishers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.wso2.developerstudio.eclipse.errorreporter.constants.ProjectConstants;
import org.wso2.developerstudio.eclipse.errorreporter.reportgenerators.TextReportGenerator;

/**
 * This class contains logic to persistently store the error report.
 */

public class FilePublisher implements ErrorPublisher {

	// Error Report Contents
	private static final String DATE = "\nDate: ";
	private static final String KEY = "\nIssue Key: ";
	private static final String ID = "\nIssue ID: ";

	// The Id and key value of error
	String Id;
	String key;

	/**
	 * The constructor.
	 */
	public FilePublisher(String key, String Id) {

		this.key = key;
		this.Id = Id;
	}

	/**
	 * This method implements the logic t store the error report in the system
	 * 
	 * @param reportGen
	 * @return filepath
	 */
	@Override
	public String publish(TextReportGenerator reportGen) {

		String filePath = "";

		// file name is created using the issue Id
		String fileName = Id + ".txt";

		// persistent storage in User Directory
		String userDirLocation = System.getProperty("user.dir");

		File persistentFolder = new File(userDirLocation);
		File errorReportsFolder = new File(persistentFolder, ProjectConstants.ERROR_REPORT_DIRECTORY);

		// if the folder does not exist already
		if (!errorReportsFolder.exists()) {

			errorReportsFolder.mkdir();
		}

		File reportPersistent = new File(errorReportsFolder, fileName);
		FileWriter writer;
		try {

			writer = new FileWriter(reportPersistent);
			writeReport(reportGen, writer, Id, key);
			writer.close();
			filePath = reportPersistent.getPath();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return filePath;
	}

	/**
	 * This method writes the text file in the given location
	 * 
	 * @param reportGen
	 * @param writer
	 * @param id
	 * @param key
	 */
	private void writeReport(TextReportGenerator reportGen, FileWriter writer, String id, String key)
			throws IOException {

		// get the system time
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String timeStamp = (dateFormat.format(date));

		// store the issue id, key and data values in the file
		writer.write(KEY + key);
		writer.write(ID + id);
		writer.write(DATE + timeStamp);
		writer.write(reportGen.getTextString());

	}

}
