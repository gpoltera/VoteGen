package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.ElectionDefinition;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.util.Arrays;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

public class ElectionDefinitionTask extends VoteGenerator {

    public void run() throws Exception {    
        /*create ElectionDefinition*/
        ElectionDefinition electionDefinition = createElectionDefinition();
        
        /*sign by ElectionAdministrator*/
        electionDefinition.setSignature(SignatureGenerator.createSignature(electionDefinition, keyStore.electionAdministratorPrivateKey));
        
        /*submit to ElectionBoard*/
        electionBoard.electionDefinition = electionDefinition;
        
        /*save in db*/
        DB4O.storeDB(ConfigHelper.getElectionId(),electionDefinition);
    }

    // Create the ElectionDefinition
    private ElectionDefinition createElectionDefinition() {
        try {
            ElectionDefinition electionDefinition = new ElectionDefinition();
            GregorianCalendar calendar = new GregorianCalendar();
            DatatypeFactory factory = DatatypeFactory.newInstance();
            electionDefinition.setElectionId(ConfigHelper.getElectionId());
            electionDefinition.setTitle(ConfigHelper.getElectionTitle());
            calendar.setTime(ConfigHelper.getVotingPhaseBegin());
            electionDefinition.setVotingPhaseBegin(factory.newXMLGregorianCalendar(calendar).normalize());
            calendar.setTime(ConfigHelper.getVotingPhaseEnd());
            electionDefinition.setVotingPhaseEnd(factory.newXMLGregorianCalendar(calendar).normalize());
            electionDefinition.getMixerId().addAll(Arrays.asList(ConfigHelper.getMixerIds()));
            electionDefinition.getTallierId().addAll(Arrays.asList(ConfigHelper.getTallierIds()));
            electionDefinition.setKeyLength(ConfigHelper.getEncryptionKeyLength());
            
            return electionDefinition;
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}