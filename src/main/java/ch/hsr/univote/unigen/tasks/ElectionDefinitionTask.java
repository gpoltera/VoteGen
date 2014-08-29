package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.ElectionDefinition;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.util.Arrays;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

public class ElectionDefinitionTask {
    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public ElectionDefinitionTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;
        
        run();
    }
    

    private void run() {    
        /*create ElectionDefinition*/
        ElectionDefinition electionDefinition = createElectionDefinition();
        
        /*sign by ElectionAdministrator*/
        electionDefinition.setSignature(new RSASignatureGenerator().createSignature(electionDefinition, keyStore.getEASignatureKey()));
        
        /*submit to ElectionBoard*/
        electionBoard.setElectionDefinition(electionDefinition);
    }

    // Create the ElectionDefinition
    private ElectionDefinition createElectionDefinition() {
        try {
            ElectionDefinition electionDefinition = new ElectionDefinition();
            GregorianCalendar calendar = new GregorianCalendar();
            DatatypeFactory factory = DatatypeFactory.newInstance();
            electionDefinition.setElectionId(config.getElectionId());
            electionDefinition.setTitle(config.getElectionTitle());
            calendar.setTime(config.getVotingPhaseBegin());
            electionDefinition.setVotingPhaseBegin(factory.newXMLGregorianCalendar(calendar).normalize());
            calendar.setTime(config.getVotingPhaseEnd());
            electionDefinition.setVotingPhaseEnd(factory.newXMLGregorianCalendar(calendar).normalize());
            electionDefinition.getMixerId().addAll(Arrays.asList(config.getMixerIds()));
            electionDefinition.getTallierId().addAll(Arrays.asList(config.getTallierIds()));
            electionDefinition.setKeyLength(config.getEncryptionKeyLength());
            
            return electionDefinition;
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}