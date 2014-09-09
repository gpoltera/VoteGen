/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.common;

import ch.bfh.univote.common.PartiallyDecryptedVotes;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gian
 */
public class PartiallyDecryptedVotesList {

    protected Map<String, PartiallyDecryptedVotes> partiallyDecryptedVotesList;

    public Map<String, PartiallyDecryptedVotes> getPartiallyDecryptedVotesList() {
        if (partiallyDecryptedVotesList == null) {
            partiallyDecryptedVotesList = new HashMap<String, PartiallyDecryptedVotes>();
        }
        return this.partiallyDecryptedVotesList;
    }

    public void setPartiallyDecryptedVotesList(Map<String, PartiallyDecryptedVotes> partiallyDecryptedVotesList) {
        this.partiallyDecryptedVotesList = partiallyDecryptedVotesList;
    }
}
