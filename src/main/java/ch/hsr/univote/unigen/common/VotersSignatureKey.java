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
public class VotersSignatureKey {

    protected List<DSAPrivateKey> votersSignatureKey;

    public List<DSAPrivateKey> getVotersSignatureKey() {
        if (votersSignatureKey == null) {
            votersSignatureKey = new ArrayList<DSAPrivateKey>();
        }
        return this.votersSignatureKey;
    }

    public void setVotersSignatureKey(List<DSAPrivateKey> votersSignatureKey) {
        this.votersSignatureKey = votersSignatureKey;
    }
}
