/**
 *
 * LAFS java driver
 * @author grubino
 *
 */

package org.lafs;

import org.lafs.LAFSConnection;

public class LAFS<ConnectionType> {

    private ConnectionType mConnection;
    
    /*
     * Creates a LAFS instance from a ConnectionType
     * object.
     *
     * @param connection the connection object
     * 
     */
    public LAFS(ConnectionType connection) {
	mConnection = connection;
    }
    
}