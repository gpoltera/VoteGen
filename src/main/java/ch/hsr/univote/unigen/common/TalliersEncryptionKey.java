/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.common;

import java.security.interfaces.DSAPublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian
 */
public class TalliersEncryptionKey {

    protected List<DSAPublicKey> talliersEncryptionKey;

    public List<DSAPublicKey> getTalliersEncryptionKey() {
        if (talliersEncryptionKey == null) {
            talliersEncryptionKey = new ArrayList<DSAPublicKey>();
        }
        return this.talliersEncryptionKey;
    }

    public void setTalliersEncryptionKey(List<DSAPublicKey> talliersEncryptionKey) {
        this.talliersEncryptionKey = talliersEncryptionKey;
    }
}
