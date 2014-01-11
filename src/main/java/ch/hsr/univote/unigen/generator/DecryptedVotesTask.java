/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class DecryptedVotesTask extends WahlGenerator {

    public static void run() throws Exception {
        dyv.setElectionId(ConfigHelper.getElectionId());
        
        for (int i = 1; i <= ConfigHelper.getVotersNumber(); i++) {
            dyv.getVote().add(BigInteger.ONE);
        }
    }
}
