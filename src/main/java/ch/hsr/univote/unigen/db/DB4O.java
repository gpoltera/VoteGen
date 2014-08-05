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
import java.text.SimpleDateFormat;

/**
 *
 * @author Gian
 */
public class DB4O {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    static Timestamp time = new Timestamp(System.currentTimeMillis());
    static String filename = sdf.format(time);

    public static void storeDB(String electionid, Object object) {
        ObjectContainer db = Db4o.openFile("db/" + filename + "_" + electionid + ".db");
        try {
            db.store(object);
            db.commit();
        } finally {
            db.close();
        }
    }

    public static void readDB(String electionid, Object object) {
        Timestamp tstamp = new Timestamp(System.currentTimeMillis());
        ObjectContainer db = Db4o.openFile("db/" + electionid + "_" + tstamp + ".db");
        try {
            ObjectSet result = db.queryByExample(object);
            while (result.hasNext()) {
                System.out.println(result.next());
            }
        } finally {
            db.close();
        }
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
