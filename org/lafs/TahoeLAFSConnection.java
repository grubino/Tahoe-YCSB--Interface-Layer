package org.lafs;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;

import java.lang.Exception;
import java.io.IOException;
import java.lang.IllegalArgumentException;

import java.util.Set;
import java.util.Map;
import java.util.Vector;
import java.util.Iterator;

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

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;


import org.lafs.LAFSConnection;

/**
 * @class TahoeLAFSConnection model of LAFSConnection concept
 *
 * The TahoeLAFSConnection will use HTTP calls to a Tahoe server to manipulate LAFS objects
 * by default.
 *
 *
 */
public class TahoeLAFSConnection implements LAFSConnection {

    String mHost;
    String mPort;

    HttpClient mHttpClient;
    
    public TahoeLAFSConnection(String hostname, int port) throws IOException {

	mHost = hostname;
	mPort = (new Integer(port)).toString();

	mHttpClient = new DefaultHttpClient();

    }

    public InputStream get(String location) throws IOException {

	try {

	    String requestURL = "http://" + mHost + ":" + mPort + "/uri" + location;
	    
	    HttpGet getRequest = new HttpGet(requestURL);
	    HttpResponse response = mHttpClient.execute(getRequest);
	    
	    if(response.getStatusLine().getStatusCode() / 200 != 1)
		throw new IOException("Unexpected response from server: "
				  + response.getStatusLine().getReasonPhrase());

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
	
	if(response.getStatusLine().getStatusCode() / 200 != 1)
	    throw new IOException("Unexpected response from server: "
				  + response.getStatusLine().getReasonPhrase());

	return IOUtils.toString(response.getEntity().getContent());
	
    }
    
    public void del(String location) throws IOException {

	try {
	    
	    HttpDelete getRequest = new HttpDelete("http://" + mHost + ":" + mPort + "/uri" + location);
	    HttpResponse response = mHttpClient.execute(getRequest);
	    
	    // TODO: find out if the response entity is useful here.
	    if(response.getStatusLine().getStatusCode() / 200 != 1)
		throw new IOException("Unexpected response from server: "
				      + response.getStatusLine().getReasonPhrase());
	    
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
	    
	    if(response.getStatusLine().getStatusCode() / 200 != 1)
		throw new IOException("Unexpected response from server: "
				  + response.getStatusLine().getReasonPhrase());

	    return IOUtils.toString(response.getEntity().getContent());
	    
	}
	catch(IllegalArgumentException e) {
	    
	    throw new IOException(e.getMessage());
	    
	}
	
    }

    @SuppressWarnings("unchecked")
    public Vector<String> list(String location) throws IOException {

	try {

	    String requestURL = "http://" + mHost + ":" + mPort + "/uri" + location + "?t=json";
	    
	    HttpGet getRequest = new HttpGet(requestURL);
	    HttpResponse response = mHttpClient.execute(getRequest);

	    if(response.getStatusLine().getStatusCode() / 200 != 1)
		throw new IOException("Unexpected response from server: "
				      + response.getStatusLine().getReasonPhrase());
	    
	    String jsonStats = IOUtils.toString(response.getEntity().getContent());
	    return _getChildrenFromStats(String jsonStats);

	}
	catch(IllegalArgumentException e) {
	    
	    throw new IOException(e.getMessage());
	    
	}

    }

    private Vector<String> _getChildrenFromStats(String jsonStats) {

	JsonParser parser = new JsonParser();
	
	try {
	    
	    Set<Map.Entry<String, JsonElement>> childObjects =
		parser.parse(jsonStats).
		getAsJsonArray().get(1).
		getAsJsonObject().get("children").
		getAsJsonObject().entrySet();
	    Vector<String> output = new Vector<String>();
	    
	    Iterator child_it = childObjects.iterator();
	    while(child_it.hasNext()) {
		Map.Entry<String, JsonElement> e = (Map.Entry<String, JsonElement>) child_it.next();
		output.add(e.getValue().getAsJsonArray().get(1).
			   getAsJsonObject().get("ro_uri").getAsString()); 
	    }
	    
	    return output;
	    
	}
	catch(JsonParseException e) {
	    throw new IOException(e.getMessage() + ": " + jsonStats);
	}
	
    }
    
}