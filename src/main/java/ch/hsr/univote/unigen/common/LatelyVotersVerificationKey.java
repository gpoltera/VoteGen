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
public class LatelyVotersVerificationKey {

    protected List<DSAPublicKey> latelyVotersVerificationKey;

    public List<DSAPublicKey> getLatelyVotersVerificationKey() {
        if (latelyVotersVerificationKey == null) {
            latelyVotersVerificationKey = new ArrayList<DSAPublicKey>();
        }
        return this.latelyVotersVerificationKey;
    }

    public void setLatelyVotersVerificationKey(List<DSAPublicKey> latelyVotersVerificationKey) {
        this.latelyVotersVerificationKey = latelyVotersVerificationKey;
    }
}
