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
public class LatelyVotersSignatureKey {

    protected List<DSAPrivateKey> latelyVotersSignatureKey;

    public List<DSAPrivateKey> getLatelyVotersSignatureKey() {
        if (latelyVotersSignatureKey == null) {
            latelyVotersSignatureKey = new ArrayList<DSAPrivateKey>();
        }
        return this.latelyVotersSignatureKey;
    }

    public void setLatelyVotersSignatureKey(List<DSAPrivateKey> latelyVotersSignatureKey) {
        this.latelyVotersSignatureKey = latelyVotersSignatureKey;
    }
}
