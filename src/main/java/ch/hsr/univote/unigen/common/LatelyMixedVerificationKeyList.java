/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.common;

import ch.bfh.univote.common.MixedVerificationKey;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian
 */
public class LatelyMixedVerificationKeyList {

    protected List<MixedVerificationKey> latelyMixedVerificationKeyList;

    public List<MixedVerificationKey> getLatelyMixedVerificationKeyList() {
        if (latelyMixedVerificationKeyList == null) {
            latelyMixedVerificationKeyList = new ArrayList<MixedVerificationKey>();
        }
        return this.latelyMixedVerificationKeyList;
    }

    public void setLatelyMixedVerificationKeyList(List<MixedVerificationKey> latelyMixedVerificationKeyList) {
        this.latelyMixedVerificationKeyList = latelyMixedVerificationKeyList;
    }
}
