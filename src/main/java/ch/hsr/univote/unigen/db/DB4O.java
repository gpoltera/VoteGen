/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.db;

import ch.hsr.univote.unigen.helper.FileHandler;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;

/**
 *
 * @author Gian
 */
public class DB4O {

    private EmbeddedConfiguration configEmbedded;
    private ObjectContainer container;
    private String filename;
    private FileHandler fileHandler;

    public DB4O(String filename) {
        this.filename = filename;
        this.fileHandler = new FileHandler();
    }

    public void storeDB(Object object) {
        configEmbedded = Db4oEmbedded.newConfiguration();
        configEmbedded.common().activationDepth(10);
        container = Db4oEmbedded.openFile(configEmbedded, fileHandler.userDBFolderPath + filename);
        try {
            container.store(object);
            container.commit();
        } finally {
            container.close();
        }
    }

    public Object readDB(Object object) {
        configEmbedded = Db4oEmbedded.newConfiguration();
        configEmbedded.common().activationDepth(10);
        container = Db4oEmbedded.openFile(configEmbedded, fileHandler.userDBFolderPath + filename);
        Object robject = new Object();
        try {
            ObjectSet result = container.queryByExample(object);
            while (result.hasNext()) {
                robject = result.next();
            }

        } finally {
            container.close();
        }

        return robject;
    }
}
