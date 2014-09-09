/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gian
 */
public class LatelyMixedVerificationKeysList {
    protected Map<String, List> latelyMixedVerificationKeysList;
    
    public Map<String, List> getLatelyMixedVerificationKeysList() {
        if (latelyMixedVerificationKeysList == null) {
            latelyMixedVerificationKeysList = new HashMap<String, List>();
        }
        return this.latelyMixedVerificationKeysList;
    }

    public void setLatelyMixedVerificationKeysList(Map<String, List> latelyMixedVerificationKeysList) {
        this.latelyMixedVerificationKeysList = latelyMixedVerificationKeysList;
    }
}
