/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.ElectionGenerator;
import ch.bfh.univote.common.ElectionSystemInfo;
import ch.bfh.univote.common.EncryptionKey;
import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.Signature;
import ch.bfh.univote.common.SignatureParameters;
import ch.bfh.univote.common.VerificationKeys;
import ch.bfh.univote.common.VoterCertificates;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.crypto.CertificateGenerator;
import ch.hsr.univote.unigen.crypto.NIZKP;
import ch.hsr.univote.unigen.crypto.PrimeGenerator;
import java.math.BigInteger;

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
        //VerificationKeys verificationKeys = electionBoard.getV
        if (config.getFault("faultVerificationKeys")) {
            electionGenerator.setGenerator(electionBoard.getBlindedGenerator(electionBoard.mixers[0]).getGenerator());
        }

        //Certificates
        ElectionSystemInfo electionSystemInfo = electionBoard.getElectionSystemInfo();
        if (config.getFault("faultCaCertificate")) {
            Certificate caCertificate = new Certificate();
            caCertificate.setValue(new CertificateGenerator().getCertficate(config.getCertificateAuthorityId(), keyStore.getEMSignatureKey(), keyStore.getCAVerificationKey()).getBytes());
            electionSystemInfo.setCertificateAuthority(caCertificate);
        }

        if (config.getFault("faultEmCertificate")) {
            Certificate emCertificate = new Certificate();
            emCertificate.setValue(new CertificateGenerator().getCertficate(config.getElectionManagerId(), keyStore.getEASignatureKey(), keyStore.getEMVerificationKey()).getBytes());
            electionSystemInfo.setElectionManager(emCertificate);
        }

        if (config.getFault("faultEaCertificate")) {
            Certificate eaCertificate = new Certificate();
            eaCertificate.setValue(new CertificateGenerator().getCertficate(config.getElectionAdministratorId(), keyStore.getEMSignatureKey(), keyStore.getEAVerificationKey()).getBytes());
            electionSystemInfo.setElectionAdministration(eaCertificate);
        }

        if (config.getFault("faultMixerCertificate")) {
            String mixerid_fault = config.getProperty("faultMixerCertificate-Mixer");
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

        if (config.getFault("faultTallierCertificate")) {
            String tallierid_fault = config.getProperty("faultTallierCertificate-Tallier");
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
        if (config.getFault("faultVoterCertificate")) {
            String voterid_fault = config.getProperty("faultVoterCertificate-Voter");
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

        //Reingeschoben
        Ballots ballots = electionBoard.getBallots();
        if (config.getFault("faultBallots")) {
            Ballot ballot = ballots.getBallot().get(0);
            ballots.getBallot().add(ballot);
        }

        //Transmit the changes to the ElectionBoard
                electionBoard
        .setSignatureParameters(signatureParameters);
        electionBoard.setEncryptionParameters(encryptionParameters);
        electionBoard.setEncryptionKey(encryptionKey);
        electionBoard.setElectionGenerator(electionGenerator);
        //electionBoard.;
        electionBoard.setElectionSystemInfo(electionSystemInfo);
        electionBoard.setVoterCertificates(voterCertificates);
        electionBoard.setBallots(ballots);

//       
//        if (!faults[12]) {
//            verificationKeys.getKey().remove(verificationKeys.getKey().size() - 1);
//        }
//        //EACertificateSignature
//        if (!faults[19]) {
//            //Not yet implemented
//        }
//        if (!faults[20]) {
//            Signature signature = electionDefinition.getSignature();
//            signature.setValue(signature.getValue().add(BigInteger.ONE));
//            electionDefinition.setSignature(signature);
//        }
//        if (!faults[21]) {
//            //Not yet implemented
//        }
//        if (!faults[22]) {
//            Signature signature = encryptionParameters.getSignature();
//            signature.setValue(signature.getValue().add(BigInteger.ONE));
//            encryptionParameters.setSignature(signature);
//        }
//        if (!faults[23]) {
//            Signature signature = encryptionKeyShareList[0].getSignature();
//            signature.setValue(signature.getValue().add(BigInteger.ONE));
//            encryptionKeyShareList[0].setSignature(signature);
//        }
//        if (!faults[24]) {
//            Signature signature = encryptionKey.getSignature();
//            signature.setValue(signature.getValue().add(BigInteger.ONE));
//            encryptionKey.setSignature(signature);
//        }
//        if (!faults[25]) {
//            Signature signature = blindedGeneratorsList[0].getSignature();
//            signature.setValue(signature.getValue().add(BigInteger.ONE));
//            blindedGeneratorsList[0].setSignature(signature);
//        }
//        if (!faults[26]) {
//            Signature signature = electionGenerator.getSignature();
//            signature.setValue(signature.getValue().add(BigInteger.ONE));
//            electionGenerator.setSignature(signature);
//        }
//        if (!faults[27]) {
//            Signature signature = electionOptions.getSignature();
//            signature.setValue(signature.getValue().add(BigInteger.ONE));
//            electionOptions.setSignature(signature);
//        }
//        if (!faults[28]) {
//            Signature signature = electionData.getSignature();
//            signature.setValue(signature.getValue().add(BigInteger.ONE));
//            electionData.setSignature(signature);
//        }
//        if (!faults[29]) {
//            encryptionKeyShareList[0].setProof(NIZKP.getProof(
//                    "fake",
//                    keyStore.talliersDecryptionKey[1],
//                    keyStore.talliersEncryptionKey[1],
//                    ElectionBoard.encryptionParameters.getPrime(),
//                    ElectionBoard.encryptionParameters.getGroupOrder(),
//                    ElectionBoard.encryptionParameters.getGenerator()));
//        }
//        if (!faults[30]) {
//            blindedGeneratorsList[0].setProof(NIZKP.getProof(
//                    "fake",
//                    keyStore.talliersDecryptionKey[1],
//                    keyStore.talliersEncryptionKey[1],
//                    ElectionBoard.encryptionParameters.getPrime(),
//                    ElectionBoard.encryptionParameters.getGroupOrder(),
//                    ElectionBoard.encryptionParameters.getGenerator()));
//        }
    }
}
