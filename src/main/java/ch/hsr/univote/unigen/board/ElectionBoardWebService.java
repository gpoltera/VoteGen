/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.board;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.BlindedGenerator;
import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.DecodedVotes;
import ch.bfh.univote.common.DecryptedVotes;
import ch.bfh.univote.common.ElectionData;
import ch.bfh.univote.common.ElectionDefinition;
import ch.bfh.univote.common.ElectionGenerator;
import ch.bfh.univote.common.ElectionOptions;
import ch.bfh.univote.common.ElectionSystemInfo;
import ch.bfh.univote.common.ElectoralRoll;
import ch.bfh.univote.common.EncryptedVotes;
import ch.bfh.univote.common.EncryptionKey;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.KnownElectionIds;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.common.SignatureParameters;
import ch.bfh.univote.common.VerificationKeys;
import ch.bfh.univote.common.VoterCertificates;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.hsr.univote.unigen.VoteGenerator;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author Gian
 */
@Stateless
@WebService(serviceName = "ElectionBoardService",
        portName = "ElectionBoardPort",
        endpointInterface = "ch.bfh.univote.election.ElectionBoard",
        targetNamespace = "http://univote.bfh.ch/election")
		//wsdlLocation = "/wsdl/ElectionBoardService.wsdl")
public class ElectionBoardWebService implements ch.bfh.univote.election.ElectionBoard {
    private ElectionBoard electionBoard;
    
    public ElectionBoardWebService(ElectionBoard electionBoard) {
        this.electionBoard = electionBoard;
    }
    
    public SignatureParameters getSignatureParameters() throws ElectionBoardServiceFault {
        return electionBoard.getSignatureParameters();
    }

    public Certificate getRootCertificate() throws ElectionBoardServiceFault {
        return electionBoard.getRootCertificate();
    }

    public KnownElectionIds getKnownElectionIds() throws ElectionBoardServiceFault {
        return electionBoard.getKnownElectionIds();
    }

    public ElectionSystemInfo getElectionSystemInfo(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getElectionSystemInfo();
    }

    public ElectionDefinition getElectionDefinition(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getElectionDefinition();
    }

    public EncryptionParameters getEncryptionParameters(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getEncryptionParameters();
    }

    public EncryptionKeyShare getEncryptionKeyShare(String electionId, String tallierId) throws ElectionBoardServiceFault {
        return electionBoard.getEncryptionKeyShare(tallierId);
    }

    public EncryptionKey getEncryptionKey(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getEncryptionKey();
    }

    public BlindedGenerator getBlindedGenerator(String electionId, String mixerId) throws ElectionBoardServiceFault {
        return electionBoard.getBlindedGenerator(mixerId);
    }

    public ElectionGenerator getElectionGenerator(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getElectionGenerator();
    }

    public ElectionOptions getElectionOptions(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getElectionOptions();
    }

    public ElectionData getElectionData(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getElectionData();
    }

    public ElectoralRoll getElectoralRoll(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getElectoralRoll();
    }

    public VoterCertificates getVoterCertificates(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getVoterCertificates();
    }

    public MixedVerificationKeys getVerificationKeysMixedBy(String electionId, String mixerId) throws ElectionBoardServiceFault {
        return electionBoard.getVerificationKeysMixedBy(mixerId);
    }

    public VerificationKeys getMixedVerificationKeys(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getMixedVerificationKeys();
    }

    public List<Certificate> getLatelyRegisteredVoterCertificates(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getLatelyRegisteredVoterCertificates();
    }

    public List<MixedVerificationKey> getVerificationKeysLatelyMixedBy(String electionId, String mixerId) throws ElectionBoardServiceFault {
        return electionBoard.getVerificationKeysLatelyMixedBy(mixerId);
    }

    public List<MixedVerificationKey> getLatelyMixedVerificationKeys(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getLatelyMixedVerificationKeys();
    }

    public Ballot getBallot(String electionId, BigInteger verificationKey) throws ElectionBoardServiceFault {
        return electionBoard.getBallot(verificationKey);
    }

    public Ballots getBallots(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getBallots();
    }

    public MixedEncryptedVotes getEncryptedVotesMixedBy(String electionId, String mixerId) throws ElectionBoardServiceFault {
        return electionBoard.getEncryptedVotesMixedBy(mixerId);
    }

    public EncryptedVotes getEncryptedVotes(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getEncryptedVotes();
    }

    public PartiallyDecryptedVotes getPartiallyDecryptedVotes(String electionId, String tallierId) throws ElectionBoardServiceFault {
        return electionBoard.getPartiallyDecryptedVotes(tallierId);
    }

    public DecryptedVotes getDecryptedVotes(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getDecryptedVotes();
    }

    public DecodedVotes getDecodedVotes(String electionId) throws ElectionBoardServiceFault {
        return electionBoard.getDecodedVotes();
    }
}