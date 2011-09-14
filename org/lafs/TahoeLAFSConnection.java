package org.lafs;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

import java.util.HashMap;

import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.HttpException;
import org.apache.http.util.EntityUtils;

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

    private DefaultHttpClientConnection mConnection;
    private HttpHost mHttpHost;
    private HttpContext mHttpContext;
    private Socket mSocket;
    private HttpRequestExecutor mHttpExecutor;
    private ImmutableHttpProcessor mHttpProcessor;
    private SyncBasicHttpParams mHttpParams;
    
    public TahoeLAFSConnection(String hostname, int port) throws UnknownHostException, IOException {

	mHttpHost = new HttpHost(hostname, port);
	mHttpProcessor =
	    new ImmutableHttpProcessor(new HttpRequestInterceptor[] {
		    new RequestContent()
		    , new RequestTargetHost()
		    , new RequestConnControl()
		    , new RequestExpectContinue()
		    , new RequestUserAgent()
		});
	mHttpExecutor = new HttpRequestExecutor();
	mHttpContext = new BasicHttpContext(null);

	mHttpParams = new SyncBasicHttpParams();
	
	HttpProtocolParams.setVersion(mHttpParams, HttpVersion.HTTP_1_1);
	HttpProtocolParams.setContentCharset(mHttpParams, "UTF-8");
	HttpProtocolParams.setUserAgent(mHttpParams, "TahoeLAFS Java Client");
	HttpProtocolParams.setUseExpectContinue(mHttpParams, true);

	mConnection = new DefaultHttpClientConnection();

	mHttpContext.setAttribute(ExecutionContext.HTTP_CONNECTION, mConnection);
	mHttpContext.setAttribute(ExecutionContext.HTTP_TARGET_HOST, mHttpHost);

    }

    public InputStream get(String readCap) throws IOException {

	if(!mConnection.isOpen()) {
	    mSocket = new Socket(mHttpHost.getHostName(), mHttpHost.getPort());
	    mConnection.bind(mSocket, mHttpParams);
	}

	BasicHttpRequest request = new BasicHttpRequest("GET", ("/uri/" + readCap));

	try {
	    
	    request.setParams(mHttpParams);
	    mHttpExecutor.preProcess(request, mHttpProcessor, mHttpContext);
	    
	    HttpResponse response = mHttpExecutor.execute(request
							  , mConnection
							  , mHttpContext);
	    response.setParams(mHttpParams);
	    mHttpExecutor.postProcess(response
				      , mHttpProcessor
				      , mHttpContext);

	    System.out.println("Response: " + response.toString());

	    return response.getEntity().getContent();
	
	}
	catch(HttpException e) {
	    throw new IOException(e.getMessage());
	}
	
    }
    public void put(String writeCap
		    , OutputStream contents) throws IOException {}
    public void del(String writeCap) throws IOException {}
    public void mkdir(String writeCap) throws IOException {}
    public HashMap stat(String readCap) throws IOException {
	return (new HashMap());
    }

}