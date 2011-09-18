package org.lafs;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.StringWriter;

import java.io.IOException;
import java.lang.IllegalArgumentException;

import java.util.HashMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.FileEntity;
import org.apache.http.util.EntityUtils;

import org.apache.commons.io.IOUtils;

import org.lafs.LAFSConnection;

/**
 * @class TahoeLAFSConnection model of LAFSConnection concept
 *
 * The TahoeLAFSConnection will use HTTP calls to a Tahoe server to manipulate LAFS objects
 * by default.
 *
 * TODO: there are many more LAFS operations to add (mkdir, deep-check, stat-ing, etc)
 *
 */
public class TahoeLAFSConnection implements LAFSConnection {

    String mHost;
    String mPort;

    HttpClient mHttpClient;
    
    public TahoeLAFSConnection(String hostname, String port) throws IOException {

	mHost = hostname;
	mPort = port;

	mHttpClient = new DefaultHttpClient();

    }

    public InputStream get(String location) throws IOException {

	try {

	    String requestURL = "http://" + mHost + ":" + mPort + "/uri" + location;
	    
	    HttpGet getRequest = new HttpGet(requestURL);
	    HttpResponse response = mHttpClient.execute(getRequest);

	    return response.getEntity().getContent();
	    
	}
	catch(IllegalArgumentException e) {
	    
	    throw new IOException(e.getMessage());
	    
	}
	
    }
    
    public String put(String location
		      , String contentType
		      , File file) throws IOException {

	String requestURL = "http://" + mHost + ":" + mPort + "/uri" + location;
	FileEntity uploadEntity = new FileEntity(file, contentType);

	HttpPut putRequest = new HttpPut(requestURL);
	putRequest.setEntity(uploadEntity);
	HttpResponse response = mHttpClient.execute(putRequest);
	
	InputStream responseStream = response.getEntity().getContent();
	StringWriter fileCapWriter = new StringWriter();

	IOUtils.copy(responseStream, fileCapWriter);

	return fileCapWriter.toString();
	
    }
    
    public void del(String location) throws IOException {

	try {
	    
	    HttpDelete getRequest = new HttpDelete("http://" + mHost + ":" + mPort + "/uri" + location);
	    HttpResponse response = mHttpClient.execute(getRequest);
	    // TODO: find out if the response entity is useful here.
	    EntityUtils.consume(response.getEntity());
	    
	}
	catch(IllegalArgumentException e) {
	    
	    throw new IOException(e.getMessage());
	    
	}
	
    }
    
    public String mkdir(String location) throws IOException {

	try {

	    String[] pathComponents = location.split("/");
	    String requestURL = new String();

	    if(pathComponents.length > 1) {
		
		String parentPath = new String();
		String childDirName = pathComponents[pathComponents.length-1];
		
		for(int i = 0; i < pathComponents.length-1; i++) {
		    parentPath += pathComponents[i];
		    if(i < pathComponents.length-2)
			parentPath += "/";
		}
		
		requestURL =
		    "http://" + mHost + ":" + mPort
		    + "/uri/" + parentPath
		    + "?t=mkdir&name=" + childDirName;

	    }
	    else {
		requestURL = "http://" + mHost + ":" + mPort + "/uri?t=mkdir";
	    }
	    
	    System.out.println(requestURL);
	    
	    HttpPost postRequest = new HttpPost(requestURL);
	    HttpResponse response = mHttpClient.execute(postRequest);
	    
	    InputStream responseStream = response.getEntity().getContent();
	    StringWriter dirCapWriter = new StringWriter();

	    IOUtils.copy(responseStream, dirCapWriter);

	    return dirCapWriter.toString();
	    
	}
	catch(IllegalArgumentException e) {
	    
	    throw new IOException(e.getMessage());
	    
	}
	
    }
    
    public HashMap stat(String location) throws IOException {

	try {

	    String requestURL = "http://" + mHost + ":" + mPort + "/uri" + location + "?t=json";
	    
	    HttpGet getRequest = new HttpGet(requestURL);
	    HttpResponse response = mHttpClient.execute(getRequest);

	    // parse JSON
	    
	}
	catch(IllegalArgumentException e) {
	    
	    throw new IOException(e.getMessage());
	    
	}
	
	return (new HashMap());
    }

    private class TahoeFileStatsReader {
	
	public JsonArray readJsonStream(InputStream in) {
	    
	}
	
    }
    
}