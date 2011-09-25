/**
 * Tahoe-LAFS client binding for YCSB.
 *
 * Submitted by Greg Rubino 
 * 
 * plagiarized from Yen Pai's MongoDB YCSB interface layer:
 * https://gist.github.com/000a66b8db2caf42467b#file_mongo_db.java
 *
 */

package com.yahoo.ycsb.db;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import java.io.File;

import org.apache.commons.io.IOUtils;

import com.yahoo.ycsb.DB;
import com.yahoo.ycsb.DBException;

import org.lafs.TahoeLAFSConnection;

/**
 * TahoeLAFS client for YCSB framework.
 *
 * Properties to set:
 *
 * TahoeLAFS.host=localhost
 * TahoeLAFS.port=3456
 * TODO: put more properties here if necessary.
 *
 * @author grubino
 *
 */
public class TahoeLAFSClient extends DB {

    private String mHost;
    private int mPort;
    private String mDirCap; 
    
    /**
     * Initialize any state for this DB. Called once per DB instance; there is
     * one DB instance per client thread.
     */
    public void init() throws DBException {
        // initialize TahoeLAFS driver
        Properties props = getProperties();
	mHost = props.getProperty("TahoeLAFS.host");
	mPort = props.getProperty("TahoeLAFS.port");

	try {
	    
	    TahoeLAFSConnection conn = new TahoeLAFSConnection(mHost, mPort);
	    mDirCap = conn.mkdir("");
	    conn = null;
	    
	}
	catch(IOException e) {
	    throw new DBException(e.getMessage());
	}
	
    }

    @Override
    /**
     * Delete a file from the grid.
     *
     * @param dir The directory name
     * @param subdir The subdirectory name
     * @return Zero on success, a non-zero error code on error. See this class's description for a discussion of error codes.
     */
    public int delete(String dircap, String subdir) {
        try {
	    
	    TahoeLAFSConnection conn = new TahoeLAFSConnection(mHost, mPort);
	    
	    conn.del("/" + mDirCap + "/" + dir + "/" + subdir);
	    conn = null;
	    
            return ((Integer) errors.get("n")) == 1 ? 0 : 1;
	    
        } catch (Exception e) {
            logger.error(e , e);
            return 1;
        }
        finally
        {
            if (conn != null)
            {
		conn = null;
            }
        }
	
    }

    @Override
    /**
     * Insert a file into the grid. Any field/value pairs in the specified valuesb HashMap will be written into the
     * record with the specified key.
     *
     * @param dir The name of the directory
     * @param subdir The name of the subdirectory to insert.
     * @param values A HashMap of remote filename/local filename pairs to insert in the file
     * @return Zero on success, a non-zero error code on error. See this class's description for a discussion of error codes.
     */
    public int insert(String dir
		      , String subdir
		      , HashMap<String, String> values) {

        try {
	    
	    TahoeLAFSConnection conn = new TahoeLAFSConnection(mHost, mPort);

	    Iterator it = values.entrySet().iterator();
	    while(it.hasNext()) {
		Map.Entry current = (Map.Entry) it.next();
		conn.put("/" + mDirCap + "/" + dir + "/" + subdir + "/" + current.getKey()
			 , new File(current.getValue()));
	    }
	    
	    conn = null;
	    
            return (errors.get("ok") != null && errors.get("err") == null) ? 0 : 1;
	    
        } catch (Exception e) {
            logger.error(e, e);
            return 1;
        } finally {
            if (conn!=null)
            {
		conn = null;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Read a set of files from the grid. Each file/content pair from the result will be stored in a HashMap.
     *
     * @param dir the name of the directory to read.
     * @param subdir The name of the subdirectory to read.
     * @param filenames The list of files to read, or null for all of them
     * @param result A HashMap of file name/content pairs for the result
     * @return Zero on success, a non-zero error code on error or "not found".
     */
    public int read(String dir
		    , String subdir
		    , Set<String> filenames
		    , HashMap<String, String> fileContents) {

        try {
	    
	    TahoeLAFSConnection conn = new TahoeLAFSConnection(mHost, mPort);

	    Iterator filename_it = filenames.iterator();
	    while(filename_it.hasNext()) {
		String filename = filename_it.next();
		Iterator contents_it = fileContents.entrySet().iterator();
		
		// warning!  larger files will make this very memory intensive!
		fileContents.put(filename
				 , IOUtils.toString(conn.get("/" + mDirCap
							     + "/" + dir
							     + "/" + filename)));
		
	    }
	    
            return fileContents != null ? 0 : 1;
	    
        } catch (Exception e) {
            logger.error(e, e);
            return 1;
        } finally {
            if (conn!=null)
            {
		conn = null;
            }
        }
    }


    @Override
    /**
     * Update a record in the database. Any field/value pairs in the specified values HashMap will be written into the
     * record with the specified record key, overwriting any existing values with the same field name.
     *
     * @param dir The name of the directory
     * @param subdir The name of the subdirectory to which to write.
     * @param fileContents A HashMap of file name/value pairs to update in the record
     * @return Zero on success, a non-zero error code on error. See this class's description for a discussion of error codes.
     */
    public int update(String dir
		      , String subdir
		      , HashMap<String, String> fileContents) {

        try {
	    conn = new TahoeLAFSConnection(mHost, mPort);
            return insert(dir, subdir, fileContents);
        } catch (Exception e) {
            logger.error(e, e);
            return 1;
        } finally {
            if (db!=null)
            {
		conn = null;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Perform a range scan for a set of records in the database. Each field/value pair from the result will be
     * stored in a HashMap.
     *
     * @param dir The name of the directory
     * @param subdir The name of the first subdirectory to read.
     * @param recordcount The number of records to read
     * @param fields The list of fields to read, or null for all of them
     * @param result A Vector of HashMaps, where each HashMap is a set field/value pairs for one record
     * @return Zero on success, a non-zero error code on error. See this class's description for a discussion of error codes.
     */
    public int scan(String table
		    , String startkey
		    , int recordcount
		    , Set<String> fields
		    , Vector<HashMap<String, String>> result) {

        try {
            return 0;
        } catch (Exception e) {
            logger.error(e + "", e);
            return 1;
        }
        finally
        {
            if (db!=null)
            {
            }
        }

    }
}

