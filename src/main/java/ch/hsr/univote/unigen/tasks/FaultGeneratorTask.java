/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.BlindedGenerator;
import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.ElectionData;
import ch.bfh.univote.common.ElectionDefinition;
import ch.bfh.univote.common.ElectionGenerator;
import ch.bfh.univote.common.ElectionOptions;
import ch.bfh.univote.common.ElectionSystemInfo;
import ch.bfh.univote.common.EncryptionKey;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.SignatureParameters;
import ch.bfh.univote.common.VerificationKeys;
import ch.bfh.univote.common.VoterCertificates;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.crypto.CertificateGenerator;
import ch.hsr.univote.unigen.crypto.PrimeGenerator;
import ch.hsr.univote.unigen.crypto.RSASignatureGenerator;
import java.math.BigInteger;
import java.util.Map;

/**
 *
 * @author Gian Poltéra
 */
public class FaultGeneratorTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public FaultGeneratorTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {
        //Schnorr Parameters
        SignatureParameters signatureParameters = electionBoard.getSignatureParameters();
        if (config.getFault("faultSchnorrPIsprime")) {
            signatureParameters.setPrime(signatureParameters.getPrime().add(BigInteger.ONE));
        }
        if (config.getFault("faultSchnorrPIssafeprime")) {
            signatureParameters.setPrime(PrimeGenerator.getPrime(config.getSignatureKeyLength()));
        }
        if (config.getFault("faultSchnorrQIsprime")) {
            signatureParameters.setGroupOrder(signatureParameters.getPrime().add(BigInteger.ONE));
        }
        if (config.getFault("faultSchnorrGIsgenerator")) {
            signatureParameters.setGenerator(signatureParameters.getGenerator().subtract(BigInteger.ONE));
        }
        if (config.getFault("faultSchnorrParameterLength")) {
            signatureParameters.setPrime(signatureParameters.getPrime().divide(BigInteger.TEN));
        }

        //ElGamal Paramaters
        EncryptionParameters encryptionParameters = electionBoard.getEncryptionParameters();
        if (config.getFault("faultElGamalPIsprime")) {
            encryptionParameters.setPrime(encryptionParameters.getPrime().add(BigInteger.ONE));
        }
        if (config.getFault("faultElGamalPIssafeprime")) {
            encryptionParameters.setPrime(PrimeGenerator.getPrime(config.getEncryptionKeyLength()));
        }
        if (config.getFault("faultElGamalQIsprime")) {
            encryptionParameters.setGroupOrder(encryptionParameters.getGroupOrder().add(BigInteger.ONE));
        }
        if (config.getFault("faultElGamalGIsgenerator")) {
            encryptionParameters.setGenerator(encryptionParameters.getGenerator().subtract(BigInteger.ONE));
        }
        if (config.getFault("faultElGamalParameterLength")) {
            encryptionParameters.setPrime(encryptionParameters.getPrime().divide(BigInteger.TEN));
        }

        //EncryptionKey
        EncryptionKey encryptionKey = electionBoard.getEncryptionKey();
        if (config.getFault("faultEncryptionKey")) {
            encryptionKey.setKey(encryptionKey.getKey().add(BigInteger.ONE));
        }

        //ElectionGenerator
        ElectionGenerator electionGenerator = electionBoard.getElectionGenerator();
        if (config.getFault("faultElectionGenerator")) {
            electionGenerator.setGenerator(electionBoard.getBlindedGenerator(electionBoard.mixers[0]).getGenerator());
        }

        //VerificationKeys
        VerificationKeys verificationKeys = electionBoard.getMixedVerificationKeys();
        if (config.getFault("faultVerificationKeys")) {
            verificationKeys.getKey().add(verificationKeys.getKey().get(0).add(BigInteger.ONE));
        }

        //Ballots
        Ballots ballots = electionBoard.getBallots();
        if (config.getFault("faultBallots")) {
            Ballot ballot = ballots.getBallot().get(0);
            ballots.getBallot().add(ballot);
        }

        if (config.getFault("faultVerificationKeys")) {
            electionGenerator.setGenerator(electionBoard.getBlindedGenerator(electionBoard.mixers[0]).getGenerator());
        }

        //Certificates
        ElectionSystemInfo electionSystemInfo = electionBoard.getElectionSystemInfo();
        if (config.getFault("faultCertificateCA")) {
            Certificate caCertificate = new Certificate();
            caCertificate.setValue(new CertificateGenerator().getCertficate(config.getCertificateAuthorityId(), keyStore.getEMSignatureKey(), keyStore.getCAVerificationKey()).getBytes());
            electionSystemInfo.setCertificateAuthority(caCertificate);
        }

        if (config.getFault("faultCertificateEM")) {
            Certificate emCertificate = new Certificate();
            emCertificate.setValue(new CertificateGenerator().getCertficate(config.getElectionManagerId(), keyStore.getEASignatureKey(), keyStore.getEMVerificationKey()).getBytes());
            electionSystemInfo.setElectionManager(emCertificate);
        }

        if (config.getFault("faultCertificateEA")) {
            Certificate eaCertificate = new Certificate();
            eaCertificate.setValue(new CertificateGenerator().getCertficate(config.getElectionAdministratorId(), keyStore.getEMSignatureKey(), keyStore.getEAVerificationKey()).getBytes());
            electionSystemInfo.setElectionAdministration(eaCertificate);
        }

        if (config.getFault("faultCertificateMixer")) {
            String mixerid_fault = config.getProperty("faultCertificateMixer-Mixer");
            int mixer = 0;
            for (int i = 0; i < electionBoard.mixers.length; i++) {
                if (electionBoard.mixers[i].equals(mixerid_fault)) {
                    mixer = i;
                    break;
                }
            }
            Certificate certificate = new Certificate();
            certificate.setValue(new CertificateGenerator().getCertficate(electionBoard.mixers[mixer], keyStore.getTallierSignatureKey(0), keyStore.getMixerVerificationKey(mixer)).getBytes());
            electionSystemInfo.getMixer().set(mixer, certificate);
        }

        if (config.getFault("faultCertificateTallier")) {
            String tallierid_fault = config.getProperty("faultCertificateTallier-Tallier");
            int tallier = 0;
            for (int i = 0; i < electionBoard.talliers.length; i++) {
                if (electionBoard.talliers[i].equals(tallierid_fault)) {
                    tallier = i;
                    break;
                }
            }
            Certificate certificate = new Certificate();
            certificate.setValue(new CertificateGenerator().getCertficate(electionBoard.talliers[tallier], keyStore.getMixerSignatureKey(0), keyStore.getTallierVerificationKey(tallier)).getBytes());
            electionSystemInfo.getTallier().set(tallier, certificate);
        }

        VoterCertificates voterCertificates = electionBoard.getVoterCertificates();
        if (config.getFault("faultCertificateVoter")) {
            String voterid_fault = config.getProperty("faultCertificateVoter-Voter");
            int voter = 0;
            for (int i = 1; i <= config.getVotersNumber(); i++) {
                String voterid = "Voter" + i;
                if (voterid.equals(voterid_fault)) {
                    voter = i - 1;
                    break;
                }
            }
            Certificate certificate = new Certificate();
            certificate.setValue(new CertificateGenerator().getCertficate("voter" + (voter + 1), keyStore.getEASignatureKey(), keyStore.getVoterVerificationKey(voter)).getBytes());
            voterCertificates.getCertificate().set(voter, certificate);
        }

        //Signatures
        if (config.getFault("faultSignatureEACertificate")) {
            electionSystemInfo.setSignature(new RSASignatureGenerator().createSignature(electionSystemInfo, keyStore.getRASignatureKey()));
        }

        ElectionDefinition electionDefinition = electionBoard.getElectionDefinition();
        if (config.getFault("faultSignatureElectionBasicParameters")) {
            electionDefinition.setSignature(new RSASignatureGenerator().createSignature(electionDefinition, keyStore.getEMSignatureKey()));
        }

        if (config.getFault("faultSignatureTallierMixerCertificates")) {
            electionSystemInfo.setSignature(new RSASignatureGenerator().createSignature(electionSystemInfo, keyStore.getRASignatureKey()));
        }

        if (config.getFault("faultSignatureElGamalParameter")) {
            encryptionParameters.setSignature(new RSASignatureGenerator().createSignature(encryptionParameters, keyStore.getEASignatureKey()));
        }

        Map<String, EncryptionKeyShare> encryptionKeyShareList = electionBoard.getEncryptionKeyShareList();
        if (config.getFault("faultSignatureEncryptionKeyShare")) {
            String tallierid_fault = config.getProperty("faultSignatureEncryptionKeyShare-Tallier");
            int tallier = 0;
            for (int i = 0; i < electionBoard.talliers.length; i++) {
                if (electionBoard.talliers[i].equals(tallierid_fault)) {
                    tallier = i;
                    break;
                }
            }
            int replaceTallier = 0;
            if (tallier == 0) {
                replaceTallier = 1;
            }
            EncryptionKeyShare encryptionKeyShare = encryptionKeyShareList.get(electionBoard.talliers[tallier]);
            encryptionKeyShare.setSignature(encryptionKeyShareList.get(electionBoard.talliers[replaceTallier]).getSignature());
            encryptionKeyShareList.remove(electionBoard.talliers[tallier]);
            encryptionKeyShareList.put(electionBoard.talliers[tallier], encryptionKeyShare);
        }

        if (config.getFault("faultSignatureEncryptionKeys")) {
            encryptionKey.setSignature(new RSASignatureGenerator().createSignature(encryptionKey, keyStore.getEASignatureKey()));
        }

        Map<String, BlindedGenerator> blindedGeneratorList = electionBoard.getBlindedGeneratorList();
        if (config.getFault("faultSignatureBlindedGenerator")) {
            String mixerid_fault = config.getProperty("faultSignatureBlindedGenerator-Mixer");
            int mixer = 0;
            for (int i = 0; i < electionBoard.mixers.length; i++) {
                if (electionBoard.mixers[i].equals(mixerid_fault)) {
                    mixer = i;
                    break;
                }
            }
            int replaceMixer = 0;
            if (mixer == 0) {
                replaceMixer = 1;
            }
            BlindedGenerator blindedGenerator = blindedGeneratorList.get(electionBoard.mixers[mixer]);
            blindedGenerator.setSignature(blindedGeneratorList.get(electionBoard.mixers[replaceMixer]).getSignature());
            blindedGeneratorList.remove(electionBoard.mixers[mixer]);
            blindedGeneratorList.put(electionBoard.mixers[mixer], blindedGenerator);
        }

        if (config.getFault("faultSignatureElectionGenerator")) {
            electionGenerator.setSignature(new RSASignatureGenerator().createSignature(electionGenerator, keyStore.getEASignatureKey()));
        }

        ElectionOptions electionOptions = electionBoard.getElectionOptions();
        if (config.getFault("faultSignatureElectionOptions")) {
            electionOptions.setSignature(new RSASignatureGenerator().createSignature(electionOptions, keyStore.getEMSignatureKey()));
        }

        ElectionData electionData = electionBoard.getElectionData();
        if (config.getFault("faultSignatureElectionData")) {
            electionData.setSignature(new RSASignatureGenerator().createSignature(electionData, keyStore.getEASignatureKey()));
        }

        //NIZKP
        if (config.getFault("faultNIZKPBlindedGenerator")) {
            String mixerid_fault = config.getProperty("faultNIZKPBlindedGenerator-Mixer");
            int mixer = 0;
            for (int i = 0; i < electionBoard.mixers.length; i++) {
                if (electionBoard.mixers[i].equals(mixerid_fault)) {
                    mixer = i;
                    break;
                }
            }
            int replaceMixer = 0;
            if (mixer == 0) {
                replaceMixer = 1;
            }
            BlindedGenerator blindedGenerator = blindedGeneratorList.get(electionBoard.mixers[mixer]);
            blindedGenerator.setProof(blindedGeneratorList.get(electionBoard.mixers[replaceMixer]).getProof());
            blindedGeneratorList.remove(electionBoard.mixers[mixer]);
            blindedGeneratorList.put(electionBoard.mixers[mixer], blindedGenerator);
        }

        if (config.getFault("faultNIZKPEncryptionKeyShare")) {
            String tallierid_fault = config.getProperty("faultNIZKPEncryptionKeyShare-Tallier");
            int tallier = 0;
            for (int i = 0; i < electionBoard.talliers.length; i++) {
                if (electionBoard.talliers[i].equals(tallierid_fault)) {
                    tallier = i;
                    break;
                }
            }
            int replaceTallier = 0;
            if (tallier == 0) {
                replaceTallier = 1;
            }
            EncryptionKeyShare encryptionKeyShare = encryptionKeyShareList.get(electionBoard.talliers[tallier]);
            encryptionKeyShare.setProof(encryptionKeyShareList.get(electionBoard.talliers[replaceTallier]).getProof());
            encryptionKeyShareList.remove(electionBoard.talliers[tallier]);
            encryptionKeyShareList.put(electionBoard.talliers[tallier], encryptionKeyShare);
        }

        //Transmit the changes to the ElectionBoard
        electionBoard.setSignatureParameters(signatureParameters);
        electionBoard.setEncryptionParameters(encryptionParameters);
        electionBoard.setEncryptionKey(encryptionKey);
        electionBoard.setElectionGenerator(electionGenerator);
        electionBoard.setMixedVerificationKeys(verificationKeys);
        electionBoard.setBallots(ballots);
        electionBoard.setElectionSystemInfo(electionSystemInfo);
        electionBoard.setVoterCertificates(voterCertificates);
        electionBoard.setElectionDefinition(electionDefinition);
        electionBoard.setEncryptionKeyShareList(encryptionKeyShareList);
        electionBoard.setBlindedGeneratorList(blindedGeneratorList);
        electionBoard.setElectionOptions(electionOptions);
        electionBoard.setElectionData(electionData);
    }
}
