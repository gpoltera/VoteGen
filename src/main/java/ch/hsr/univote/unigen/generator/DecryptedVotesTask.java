/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.PoliticalList;
import ch.hsr.univote.unigen.common.StringConcatenator;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Gian Poltéra
 */
public class DecryptedVotesTask extends WahlGenerator {

    public static void run() throws Exception {
        dyv.setElectionId(ConfigHelper.getElectionId());

        //loop each voter
        for (int i = 0; i < ConfigHelper.getVotersNumber(); i++) {
            Random generator = new Random();
            //concatenate to cnln..c2c1li BitString
            StringConcatenator sc = new StringConcatenator();

            //loop each choice and generate a vote
            for (int j = 0; j < eo.getChoice().size(); j++) {
                Choice choice = eo.getChoice().get(eo.getChoice().size() - j - 1);
                if (choice instanceof PoliticalList) {
                    PoliticalList politicalList = (PoliticalList) choice;
                    politicalLists.add(politicalList);
                    sc.pushObject(1);
                } else if (choice instanceof Candidate) {
                    Candidate candidate = (Candidate) choice;
                    candidateList.add(candidate);
                    int ramdonCount = generator.nextInt(ConfigHelper.getMaxCumulation());
                    String maxBinCan = Integer.toBinaryString(ConfigHelper.getMaxCumulation());
                    String binChoice = Integer.toBinaryString(ramdonCount);
                    //fill with 0 for correct BitString
                    while (binChoice.length() < maxBinCan.length()) {
                        binChoice = "0" + binChoice;
                    }
                    sc.pushObject(binChoice);
                }
            }
            //convert from BitString to decimal 
            BigInteger vote = new BigInteger(sc.pullAll(), 2);
            
            dyv.getVote().add(vote);
            

            //     00  0  00 00 00 0
            //     --  -  -- -- -- -
            //     C4  L2 C3 C2 C1 L1
            //
            // cId 6  5  4  3  2  1  
        }
    }
}
