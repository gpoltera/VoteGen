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
import static ch.hsr.univote.unigen.board.ElectionBoard.knownElectionIds;
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
public class ElectionBoardWebService implements ch.bfh.univote.election.ElectionBoard {

    public SignatureParameters getSignatureParameters() throws ElectionBoardServiceFault {

        return ElectionBoard.signatureParameters;
    }

    public Certificate getRootCertificate() throws ElectionBoardServiceFault {

        return ElectionBoard.cert;
    }

    public KnownElectionIds getKnownElectionIds() throws ElectionBoardServiceFault {

        return ElectionBoard.knownElectionIds;
    }

    public ElectionSystemInfo getElectionSystemInfo(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.esi;
    }

    public ElectionDefinition getElectionDefinition(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.ed;
    }

    public EncryptionParameters getEncryptionParameters(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.encryptionParameters;
    }

    public EncryptionKeyShare getEncryptionKeyShare(String electionId, String tallierId) throws ElectionBoardServiceFault {
        EncryptionKeyShare encryptionKeyShare = new EncryptionKeyShare();
        for (int i = 0; i < ElectionBoard.talliers.length; i++) {
            if (ElectionBoard.talliers[i].equals(tallierId)) {
                encryptionKeyShare = ElectionBoard.encryptionKeyShareList[i];
            }
        }

        return encryptionKeyShare;
    }

    public EncryptionKey getEncryptionKey(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.ek;
    }

    public BlindedGenerator getBlindedGenerator(String electionId, String mixerId) throws ElectionBoardServiceFault {
        BlindedGenerator blindedGenerator = new BlindedGenerator();
        for (int i = 0; i < ElectionBoard.mixers.length; i++) {
            if (ElectionBoard.mixers[i].equals(mixerId)) {
                blindedGenerator = ElectionBoard.blindedGeneratorsList[i];
            }
        }

        return blindedGenerator;
    }

    public ElectionGenerator getElectionGenerator(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.eg;
    }

    public ElectionOptions getElectionOptions(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.eo;
    }

    public ElectionData getElectionData(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.edat;
    }

    public ElectoralRoll getElectoralRoll(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.er;
    }

    public VoterCertificates getVoterCertificates(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.vc;
    }

    public MixedVerificationKeys getVerificationKeysMixedBy(String electionId, String mixerId) throws ElectionBoardServiceFault {
        MixedVerificationKeys mixedVerificationKeys = new MixedVerificationKeys();
        for (int i = 0; i < ElectionBoard.mixers.length; i++) {
            if (ElectionBoard.mixers[i].equals(mixerId)) {
                mixedVerificationKeys = ElectionBoard.mixedVerificationKeysList[i];
                break;
            }
        }

        return mixedVerificationKeys;
    }

    public VerificationKeys getMixedVerificationKeys(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.vk;
    }

    public List<Certificate> getLatelyRegisteredVoterCertificates(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.lcert;
    }

    public List<MixedVerificationKey> getVerificationKeysLatelyMixedBy(String electionId, String mixerId) throws ElectionBoardServiceFault {
        List<MixedVerificationKey> mixedVerificationKeys = new ArrayList<MixedVerificationKey>();

        for (int i = 0; i < ElectionBoard.mixers.length; i++) {
            if (ElectionBoard.mixers[i].equals(mixerId)) {
                mixedVerificationKeys.add(ElectionBoard.lmvk.get(i));
                break;
            }
        }

        return mixedVerificationKeys;
    }

    public List<MixedVerificationKey> getLatelyMixedVerificationKeys(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.lmvk;
    }

    public Ballot getBallot(String electionId, BigInteger verificationKey) throws ElectionBoardServiceFault {
        Ballot vkballot = null;
        for (Ballot ballot : ElectionBoard.bts.getBallot()) {
            if (ballot.getVerificationKey().equals(verificationKey)) {
                vkballot = ballot;
                break;
            }
        }

        return vkballot;
    }

    public Ballots getBallots(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.bts;
    }

    public MixedEncryptedVotes getEncryptedVotesMixedBy(String electionId, String mixerId) throws ElectionBoardServiceFault {
        MixedEncryptedVotes mixedEncryptedVotes = new MixedEncryptedVotes();
        for (int i = 0; i < ElectionBoard.mixers.length; i++) {
            if (ElectionBoard.mixers[i].equals(mixerId)) {
                mixedEncryptedVotes = ElectionBoard.mixedEncryptedVotesList[i];
            }
        }

        return mixedEncryptedVotes;
    }

    public EncryptedVotes getEncryptedVotes(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.ev;
    }

    public PartiallyDecryptedVotes getPartiallyDecryptedVotes(String electionId, String tallierId) throws ElectionBoardServiceFault {
        PartiallyDecryptedVotes partiallyDecryptedVotes = new PartiallyDecryptedVotes();
        for (int i = 0; i < ElectionBoard.talliers.length; i++) {
            if (ElectionBoard.talliers[i].equals(tallierId)) {
                partiallyDecryptedVotes = ElectionBoard.partiallyDecryptedVotesList[i];
            }
        }

        return partiallyDecryptedVotes;
    }

    public DecryptedVotes getDecryptedVotes(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.dyv;
    }

    public DecodedVotes getDecodedVotes(String electionId) throws ElectionBoardServiceFault {

        return ElectionBoard.dov;
    }
}
