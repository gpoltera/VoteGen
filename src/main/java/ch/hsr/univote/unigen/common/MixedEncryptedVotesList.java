/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.common;

import ch.bfh.univote.common.MixedEncryptedVotes;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gian
 */
public class MixedEncryptedVotesList {

    protected Map<String, MixedEncryptedVotes> mixedEncryptedVotesList;

    public Map<String, MixedEncryptedVotes> getMixedEncryptedVotesList() {
        if (mixedEncryptedVotesList == null) {
            mixedEncryptedVotesList = new HashMap<String, MixedEncryptedVotes>();
        }
        return this.mixedEncryptedVotesList;
    }

    public void setMixedEncryptedVotesList(Map<String, MixedEncryptedVotes> mixedEncryptedVotesList) {
        this.mixedEncryptedVotesList = mixedEncryptedVotesList;
    }
}
