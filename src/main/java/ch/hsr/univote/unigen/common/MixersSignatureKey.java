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
public class MixersSignatureKey {

    protected List<RSAPrivateKey> mixersSignatureKey;

    public List<RSAPrivateKey> getMixersSignatureKey() {
        if (mixersSignatureKey == null) {
            mixersSignatureKey = new ArrayList<RSAPrivateKey>();
        }
        return this.mixersSignatureKey;
    }

    public void setMixersSignatureKey(List<RSAPrivateKey> mixersSignatureKey) {
        this.mixersSignatureKey = mixersSignatureKey;
    }
}
