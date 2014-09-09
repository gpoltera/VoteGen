/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.common;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian
 */
public class TalliersVerificationKey {

    protected List<RSAPublicKey> talliersVerificationKey;

    public List<RSAPublicKey> getTalliersVerificationKey() {
        if (talliersVerificationKey == null) {
            talliersVerificationKey = new ArrayList<RSAPublicKey>();
        }
        return this.talliersVerificationKey;
    }

    public void setTalliersVerificationKey(List<RSAPublicKey> talliersVerificationKey) {
        this.talliersVerificationKey = talliersVerificationKey;
    }
}
