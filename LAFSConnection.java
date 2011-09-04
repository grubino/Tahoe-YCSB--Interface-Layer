/**
 *
 * LAFS connection driver
 * @author grubino
 *
 * @concept LAFSConnection
 * 
 * valid expressions:
 *
 * initialization (these may be no-ops)
 *=====================================
 * LAFSConnection.open() - open the communication channel to
 *                     the filesystem.
 * LAFSConnection.close() - close the communication channel to
 *                      the filesystem.
 *
 * file manipulation (failure will throw)
 *=======================================
 * LAFSConnection.get(FileIdentifier id) - get a stream for the LAFS object identified by id.
 * LAFSConnection.put(FileIdentifier id
 *                , OutputStream contents) - put the contents of an OutputStream into the
 *                                           LAFS if possible.
 * LAFSConnection.del(FileIdentifier id) - remove the LAFS object identified by id if possible.
 *
 */

package org.lafs;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;


/**
 * @class TahoeLAFSConnection model of LAFSConnection concept
 *
 * The TahoeLAFSConnection will use HTTP calls to a Tahoe server to manipulate LAFS objects
 * by default.
 *
 * TODO: there are many more LAFS operations to add (mkdir, deep-check, stat-ing, etc)
 *
 */
public class TahoeLAFSConnection {

    private URLConnection mUrlConnection;
    
    public TahoeLAFSConnection(URL url) {
	
    }

    public InputStream get(URL url)
	throws IOException, UnknownServiceException {

    }

    public void put(URL url
		    , OutputStream contents)
	throws IOException, UnknownServiceException {
	
    }

    public void del(URL url)
	throws IOException, UnknownServiceException {

    }
    
}