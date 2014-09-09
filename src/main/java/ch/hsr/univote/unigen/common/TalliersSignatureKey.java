/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.common;

import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian
 */
public class TalliersSignatureKey {

    protected List<RSAPrivateKey> talliersSignatureKey;

    public List<RSAPrivateKey> getTalliersSignatureKey() {
        if (talliersSignatureKey == null) {
            talliersSignatureKey = new ArrayList<RSAPrivateKey>();
        }
        return this.talliersSignatureKey;
    }

    public void setTalliersSignatureKey(List<RSAPrivateKey> talliersSignatureKey) {
        this.talliersSignatureKey = talliersSignatureKey;
    }
}
