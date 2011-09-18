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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;

import java.util.HashMap;

public interface LAFSConnection {

    public InputStream get(String location) throws IOException;
    public String put(String location, String contentType, File file) throws IOException;
    public String mkdir(String location) throws IOException;
    public void del(String location) throws IOException;

    // TODO: change this back to HashMap when done learning how to parse
    //       JSON in java.  For now just return a string representation.
    public String stat(String location) throws IOException;
    
}

