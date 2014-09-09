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
public class BlindedGeneratorKey {

    protected List<DSAPrivateKey> blindedGeneratorKey;

    public List<DSAPrivateKey> getBlindedGeneratorKey() {
        if (blindedGeneratorKey == null) {
            blindedGeneratorKey = new ArrayList<DSAPrivateKey>();
        }
        return this.blindedGeneratorKey;
    }

    public void setBlindedGeneratorKey(List<DSAPrivateKey> blindedGeneratorKey) {
        this.blindedGeneratorKey = blindedGeneratorKey;
    }
}
