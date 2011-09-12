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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

import java.util.HashMap;

public interface LAFSConnection {

    public InputStream get(String readCap) throws IOException;
    public void put(String writeCap, OutputStream contents) throws IOException;
    public void mkdir(String writeCap) throws IOException;
    public void del(String writeCap) throws IOException;
    public HashMap stat(String readCap) throws IOException;
    
}

