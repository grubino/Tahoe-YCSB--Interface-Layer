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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import com.yahoo.ycsb.DB;
import com.yahoo.ycsb.DBException;

/**
 * TahoeLAFS client for YCSB framework.
 *
 * Properties to set:
 *
 * TahoeLAFS.url=http://localhost:3456
 * TODO: put more properties here if necessary.
 *
 * @author grubino
 *
 */
public class TahoeLAFSClient extends DB {

    /**
     * Initialize any state for this DB. Called once per DB instance; there is
     * one DB instance per client thread.
     */
    public void init() throws DBException {
        // initialize TahoeLAFS driver
        Properties props = getProperties();
        String url = props.getProperty("TahoeLAFS.url");
    }

    @Override
    /**
     * Delete a record from the database.
     *
     * @param table The name of the table
     * @param key The record key of the record to delete.
     * @return Zero on success, a non-zero error code on error. See this class's description for a discussion of error codes.
     */
    public int delete(String table, String key) {
        org.lafs.LAFSConnection conn = null;
        try {
            return ((Integer) errors.get("n")) == 1 ? 0 : 1;
        } catch (Exception e) {
            logger.error(e + "", e);
            return 1;
        }
        finally
        {
            if (conn != null)
            {
            }
        }
    }

    @Override
    /**
     * Insert a record in the database. Any field/value pairs in the specified values HashMap will be written into the record with the specified
     * record key.
     *
     * @param table The name of the table
     * @param key The record key of the record to insert.
     * @param values A HashMap of field/value pairs to insert in the record
     * @return Zero on success, a non-zero error code on error. See this class's description for a discussion of error codes.
     */
    public int insert(String table, String key, HashMap<String, String> values) {
        com.TahoeLAFS.DB db = null;
        try {
            return (errors.get("ok") != null && errors.get("err") == null) ? 0 : 1;
        } catch (Exception e) {
            logger.error(e + "", e);
            return 1;
        } finally {
            if (db!=null)
            {
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Read a record from the database. Each field/value pair from the result will be stored in a HashMap.
     *
     * @param table The name of the table
     * @param key The record key of the record to read.
     * @param fields The list of fields to read, or null for all of them
     * @param result A HashMap of field/value pairs for the result
     * @return Zero on success, a non-zero error code on error or "not found".
     */
    public int read(String table, String key, Set<String> fields,
            HashMap<String, String> result) {
        com.TahoeLAFS.DB db = null;
        try {
            if (queryResult != null) {
            }
            return queryResult != null ? 0 : 1;
        } catch (Exception e) {
            logger.error(e + "", e);
            return 1;
        } finally {
            if (db!=null)
            {
            }
        }
    }


    @Override
    /**
     * Update a record in the database. Any field/value pairs in the specified values HashMap will be written into the record with the specified
     * record key, overwriting any existing values with the same field name.
     *
     * @param table The name of the table
     * @param key The record key of the record to write.
     * @param values A HashMap of field/value pairs to update in the record
     * @return Zero on success, a non-zero error code on error. See this class's description for a discussion of error codes.
     */
    public int update(String table, String key, HashMap<String, String> values) {
        com.TahoeLAFS.DB db = null;
        try {
            return (Integer) errors.get("n") == 1 ? 0 : 1;
        } catch (Exception e) {
            logger.error(e + "", e);
            return 1;
        } finally {
            if (db!=null)
            {
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Perform a range scan for a set of records in the database. Each field/value pair from the result will be stored in a HashMap.
     *
     * @param table The name of the table
     * @param startkey The record key of the first record to read.
     * @param recordcount The number of records to read
     * @param fields The list of fields to read, or null for all of them
     * @param result A Vector of HashMaps, where each HashMap is a set field/value pairs for one record
     * @return Zero on success, a non-zero error code on error. See this class's description for a discussion of error codes.
     */
    public int scan(String table, String startkey, int recordcount,
            Set<String> fields, Vector<HashMap<String, String>> result) {
        com.TahoeLAFS.DB db=null;
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

