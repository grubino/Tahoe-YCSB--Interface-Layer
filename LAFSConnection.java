/**
 *
 * LAFS connection driver
 * @author grubino
 *
 * @concept LAFSConnection
 * 
 * valid expressions:
 *
 * file manipulation
 * =================
 * LAFSConnection.get(FileIdentifier id) - get a stream for the LAFS object identified by id.
 * LAFSConnection.put(FileIdentifier id
 *                    , OutputStream contents) - put the contents of an OutputStream into the
 *                                               LAFS if possible.
 * LAFSConnection.del(FileIdentifier id) - remove the LAFS object identified by id if possible.
 * LAFSConnection.mkdir(FileIdentifier id) - create a new container for LAFS objects if possible.
 *
 * file statistics
 * ===============
 * LAFSConnection.stat(FileIdentifier id) - get a map containing file statistics
 */

package org.lafs;

import java.io.Socket;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.HttpRequestInterceptor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.protocol.TargetHost;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.protocol.RequestExpectContinue;

import java.util.HashMap;

public interface LAFSConnection {

    public InputStream get(String readCap) throws IOException;
    public void put(String writeCap, OutputStream contents) throws IOException;
    public void mkdir(String writeCap) throws IOException;
    public void del(String writeCap) throws IOException;
    public HashMap stat(String readCap) throws IOException;
    
}

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
    
    public TahoeLAFSConnection(String hostname, int port) {

	mHttpHost = new HttpHost(hostname, port);
	mSocket = new Socket(hostname, port);
	mHttpProcessor =
	    new ImmutableHttpProcessor(new HttpRequestInterceptor[] {
		    new RequestContent()
		    , new RequestTargetHost()
		    , new RequestConnControl()
		    , new RequestExpectContinue()
		    , new RequestUserAgent()
		});
	mHttpExecutor = new HttpRequestExecutor();
	mHttpContext = new HttpContext(null);

	HttpParams params = new SyncBasicHttpParams();
	HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	HttpProtocolParams.setContentCharset(params, "UTF-8");
	HttpProtocolParams.setUserAgent(params, "TahoeLAFS YCSB Workload Tester");
	HttpProtocolParams.setUseExpectContinue(params, true);

	mConnection = new DefaultHttpClientConnection();

	mHttpContext.setAttribute(ExecutionContext.HTTP_CONNECTION, mConnection);
	mHttpContext.setAttribute(ExecutionContext.HTTP_TARGET_HOST, mHost);
	
	mConnection.bind(mSocket, params);

    }

    public InputStream get(String readCap) throws IOException {}
    public void put(String writeCap
		    , OutputStream contents) throws IOException {}
    public void del(String writeCap) throws IOException {}
    public HashMap stat(String readCap) throws IOException {}

}