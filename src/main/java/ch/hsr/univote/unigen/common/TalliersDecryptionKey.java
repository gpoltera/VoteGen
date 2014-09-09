/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.common;

import java.security.interfaces.DSAPrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian
 */
public class TalliersDecryptionKey {

    protected List<DSAPrivateKey> talliersDecryptionKey;

    public List<DSAPrivateKey> getTalliersDecryptionKey() {
        if (talliersDecryptionKey == null) {
            talliersDecryptionKey = new ArrayList<DSAPrivateKey>();
        }
        return this.talliersDecryptionKey;
    }

    public void setTalliersDecryptionKey(List<DSAPrivateKey> talliersDecryptionKey) {
        this.talliersDecryptionKey = talliersDecryptionKey;
    }
}
