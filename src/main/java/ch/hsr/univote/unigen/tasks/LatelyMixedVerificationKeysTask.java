/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedVerificationKey;
import ch.hsr.univote.unigen.VoteGenerator;


/**
 *
 * @author Gian Poltéra
 */
public class LatelyMixedVerificationKeysTask extends VoteGenerator {

    public void run() throws Exception {
        
        /*submit to ElectionBoard*/
        //List<MixedVerificationKey> test = electionBoard.mixedVerificationKeysList;

    }
    
    private MixedVerificationKey createMixedVerificationKey() {
        MixedVerificationKey mixedVerificationKey = new MixedVerificationKey();
        
        
        
        return mixedVerificationKey;
    }
}
