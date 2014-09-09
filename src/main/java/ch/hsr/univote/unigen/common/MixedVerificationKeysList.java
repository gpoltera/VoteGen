/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.common;

import ch.bfh.univote.common.MixedVerificationKeys;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gian
 */
public class MixedVerificationKeysList {

    protected Map<String, MixedVerificationKeys> mixedVerificationKeysList;

    public Map<String, MixedVerificationKeys> getListMixedVerificationKeys() {
        if (mixedVerificationKeysList == null) {
            mixedVerificationKeysList = new HashMap<String, MixedVerificationKeys>();
        }
        return this.mixedVerificationKeysList;
    }

    public void setListMixedVerificationKeys(Map<String, MixedVerificationKeys> mixedVerificationKeysList) {
        this.mixedVerificationKeysList = mixedVerificationKeysList;
    }
}
