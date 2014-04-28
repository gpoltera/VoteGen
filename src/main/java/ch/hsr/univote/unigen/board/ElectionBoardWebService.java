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
import java.util.ArrayList;
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
public class ElectionBoardWebService extends VoteGenerator implements ch.bfh.univote.election.ElectionBoard {

    public SignatureParameters getSignatureParameters() throws ElectionBoardServiceFault {
        return electionBoard.signatureParameters;
    }

    public Certificate getRootCertificate() throws ElectionBoardServiceFault {
        return electionBoard.certificate;
    }

    public KnownElectionIds getKnownElectionIds() throws ElectionBoardServiceFault {

        return electionBoard.knownElectionIds;
    }

    public ElectionSystemInfo getElectionSystemInfo(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.electionSystemInfo;
    }

    public ElectionDefinition getElectionDefinition(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.electionDefinition;
    }

    public EncryptionParameters getEncryptionParameters(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.encryptionParameters;
    }

    public EncryptionKeyShare getEncryptionKeyShare(String electionId, String tallierId) throws ElectionBoardServiceFault {
        EncryptionKeyShare encryptionKeyShare = new EncryptionKeyShare();
        for (int i = 0; i < electionBoard.talliers.length; i++) {
            if (electionBoard.talliers[i].equals(tallierId)) {
                encryptionKeyShare = electionBoard.encryptionKeyShareList[i];
            }
        }

        return encryptionKeyShare;
    }

    public EncryptionKey getEncryptionKey(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.encryptionKey;
    }

    public BlindedGenerator getBlindedGenerator(String electionId, String mixerId) throws ElectionBoardServiceFault {
        BlindedGenerator blindedGenerator = new BlindedGenerator();
        for (int i = 0; i < electionBoard.mixers.length; i++) {
            if (electionBoard.mixers[i].equals(mixerId)) {
                blindedGenerator = electionBoard.blindedGeneratorsList[i];
            }
        }

        return blindedGenerator;
    }

    public ElectionGenerator getElectionGenerator(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.electionGenerator;
    }

    public ElectionOptions getElectionOptions(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.electionOptions;
    }

    public ElectionData getElectionData(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.electionData;
    }

    public ElectoralRoll getElectoralRoll(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.electoralRoll;
    }

    public VoterCertificates getVoterCertificates(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.voterCertificates;
    }

    public MixedVerificationKeys getVerificationKeysMixedBy(String electionId, String mixerId) throws ElectionBoardServiceFault {
        MixedVerificationKeys mixedVerificationKeys = new MixedVerificationKeys();
        for (int i = 0; i < electionBoard.mixers.length; i++) {
            if (electionBoard.mixers[i].equals(mixerId)) {
                mixedVerificationKeys = electionBoard.mixedVerificationKeysList[i];
                break;
            }
        }

        return mixedVerificationKeys;
    }

    public VerificationKeys getMixedVerificationKeys(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.verificationKeys;
    }

    public List<Certificate> getLatelyRegisteredVoterCertificates(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.listCertificate;
    }

    public List<MixedVerificationKey> getVerificationKeysLatelyMixedBy(String electionId, String mixerId) throws ElectionBoardServiceFault {
        List<MixedVerificationKey> mixedVerificationKeys = new ArrayList<MixedVerificationKey>();

        for (int i = 0; i < electionBoard.mixers.length; i++) {
            if (electionBoard.mixers[i].equals(mixerId)) {
                mixedVerificationKeys.add(electionBoard.listMixedVerificationKey.get(i));
                break;
            }
        }

        return mixedVerificationKeys;
    }

    public List<MixedVerificationKey> getLatelyMixedVerificationKeys(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.listMixedVerificationKey;
    }

    public Ballot getBallot(String electionId, BigInteger verificationKey) throws ElectionBoardServiceFault {
        Ballot vkballot = null;
        for (Ballot ballot : electionBoard.ballots.getBallot()) {
            if (ballot.getVerificationKey().equals(verificationKey)) {
                vkballot = ballot;
                break;
            }
        }

        return vkballot;
    }

    public Ballots getBallots(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.ballots;
    }

    public MixedEncryptedVotes getEncryptedVotesMixedBy(String electionId, String mixerId) throws ElectionBoardServiceFault {
        MixedEncryptedVotes mixedEncryptedVotes = new MixedEncryptedVotes();
        for (int i = 0; i < electionBoard.mixers.length; i++) {
            if (electionBoard.mixers[i].equals(mixerId)) {
                mixedEncryptedVotes = electionBoard.mixedEncryptedVotesList[i];
            }
        }

        return mixedEncryptedVotes;
    }

    public EncryptedVotes getEncryptedVotes(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.encryptedVotes;
    }

    public PartiallyDecryptedVotes getPartiallyDecryptedVotes(String electionId, String tallierId) throws ElectionBoardServiceFault {
        PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
        for (int i = 0; i < electionBoard.talliers.length; i++) {
            if (electionBoard.talliers[i].equals(tallierId)) {
                partiallyDecryptedVotes = electionBoard.partiallyDecryptedVotesList[i];
            }
        }

        return partiallyDecryptedVotes;
    }

    public DecryptedVotes getDecryptedVotes(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.decryptedVotes;
    }

    public DecodedVotes getDecodedVotes(String electionId) throws ElectionBoardServiceFault {

        return electionBoard.decodedVotes;
    }
}
