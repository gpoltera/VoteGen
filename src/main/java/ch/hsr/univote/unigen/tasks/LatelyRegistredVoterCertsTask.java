/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Certificate;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.db.DB4O;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class LatelyRegistredVoterCertsTask extends VoteGenerator {

    public void run() throws Exception {
        /*read VoterCertificates from ElectionBoard*/
        List<Certificate> voterCertificates = electionBoard.getVoterCertificates().getCertificate();
                
        /*create ElectionOptions*/
        List<Certificate> listCertificate = createListCertficate(voterCertificates);
              
        /*submit to ElectionBoard*/
        electionBoard.setLatelyRegisteredVoterCertificates(listCertificate);
    }
    
    private List<Certificate> createListCertficate(List<Certificate> voterCertificates) {
        List<Certificate> listCertificate = new ArrayList<>();
                
        // for each certificate
        for (Certificate certificate : voterCertificates) {
            listCertificate.add(certificate);    
        }
        
        return listCertificate;
    }
}
