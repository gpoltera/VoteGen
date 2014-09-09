/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.common;

import ch.bfh.univote.common.EncryptionKeyShare;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gian
 */
public class EncryptionKeyShareList {

    protected Map<String, EncryptionKeyShare> encryptionKeyShareList;

    public Map<String, EncryptionKeyShare> getEncryptionKeyShareList() {
        if (encryptionKeyShareList == null) {
            encryptionKeyShareList = new HashMap<String, EncryptionKeyShare>();
        }
        return this.encryptionKeyShareList;
    }

    public void setEncryptionKeyShareList(Map<String, EncryptionKeyShare> encryptionKeyShareList) {
        this.encryptionKeyShareList = encryptionKeyShareList;
    }
}
