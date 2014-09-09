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
public class MixersVerificationKey {

    protected List<RSAPublicKey> mixersVerificationKey;

    public List<RSAPublicKey> getMixersVerificationKey() {
        if (mixersVerificationKey == null) {
            mixersVerificationKey = new ArrayList<RSAPublicKey>();
        }
        return this.mixersVerificationKey;
    }

    public void setMixersVerificationKey(List<RSAPublicKey> mixersVerificationKey) {
        this.mixersVerificationKey = mixersVerificationKey;
    }
}
