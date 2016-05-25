package org.wso2.developerstudio.eclipse.errorreporter.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

@SuppressWarnings("deprecation")
public class RemoteJiraConnector {

	private HttpClient client;
	private HttpClient client2;

	public void Get() throws IOException {

		client = new DefaultHttpClient();
		HttpGet request = new HttpGet("");
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		}
	}

	public void Post() throws ClientProtocolException, IOException {

		client2 = new DefaultHttpClient();
		HttpPost post = new HttpPost("https://<JIRA_HOST>/rest/api/2/issue/");
		StringEntity input = new StringEntity("");
		post.setEntity(input);
		HttpResponse response = client2.execute(post);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		}

	}

}
