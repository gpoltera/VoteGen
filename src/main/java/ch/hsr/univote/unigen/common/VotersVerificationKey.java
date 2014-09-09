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
public class VotersVerificationKey {

    protected List<DSAPublicKey> votersVerificationKey;

    public List<DSAPublicKey> getVotersVerificationKey() {
        if (votersVerificationKey == null) {
            votersVerificationKey = new ArrayList<DSAPublicKey>();
        }
        return this.votersVerificationKey;
    }

    public void setVotersVerificationKey(List<DSAPublicKey> votersVerificationKey) {
        this.votersVerificationKey = votersVerificationKey;
    }
}
