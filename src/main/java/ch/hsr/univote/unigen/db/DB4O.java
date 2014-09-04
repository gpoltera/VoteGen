/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.db;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.sql.Timestamp;

/**
 *
 * @author Gian
 */
public class DB4O {

    public static void storeDB(Object object, String filename) {
        ObjectContainer db = Db4o.openFile("db/" + filename + ".db");
        try {
            db.store(object);
            db.commit();
        } finally {
            db.close();
        }
    }

    public static Object readDB(Object object, String filename) {
        ObjectContainer db = Db4o.openFile("db/" + filename);
        Object robject = new Object();
        try {
            
            ObjectSet<Object> result = db.queryByExample(object);
            while (result.hasNext()) {
                robject = result.next();
            }
            
        } finally {
            db.close();
        }
        
        return robject;
    }

    public static void changeDB(String electionid, Object object) {
        Timestamp tstamp = new Timestamp(System.currentTimeMillis());
        ObjectContainer db = Db4o.openFile("db/" + electionid + "_" + tstamp + ".db");
        try {
            //tba
        } finally {
            db.close();
        }
    }

    public static void deleteDB(String electionid, Object object) {
        Timestamp tstamp = new Timestamp(System.currentTimeMillis());
        ObjectContainer db = Db4o.openFile("db/" + electionid + "_" + tstamp + ".db");
        try {
            db.delete(object);
            db.commit();
        } finally {
            db.close();
        }
    }
}
