/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.CertificateGenerator;
import ch.hsr.univote.unigen.krypto.NIZKP;
import ch.hsr.univote.unigen.krypto.NoPrimeGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class FaultGeneratorTask extends VoteGenerator {
    
    public void run() throws Exception {
        //load the fault-config
        boolean[] faults = ConfigHelper.getFaults();

        //Schnorr Parameter
        if (!faults[0]) {
            electionBoard.signatureParameters.setPrime(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[1]) {
            electionBoard.signatureParameters.setGroupOrder(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[2]) {
            electionBoard.signatureParameters.setGenerator(BigInteger.TEN);
        }
        if (!faults[3]) {
            electionBoard.signatureParameters.setPrime(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[4]) {
            electionBoard.signatureParameters.setPrime(electionBoard.signatureParameters.getPrime().divide(BigInteger.TEN));
        }
        //ElGamal Parameters
        if (!faults[5]) {
            electionBoard.encryptionParameters.setPrime(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[6]) {
            electionBoard.encryptionParameters.setGroupOrder(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[7]) {
            electionBoard.encryptionParameters.setGenerator(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[8]) {
            electionBoard.encryptionParameters.setPrime(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[9]) {
            electionBoard.encryptionParameters.setPrime(electionBoard.encryptionParameters.getPrime().divide(BigInteger.TEN));
        }
        //EncryptionKey
        if (!faults[10]) {
            electionBoard.encryptionKey.setKey(electionBoard.encryptionKey.getKey().add(BigInteger.ONE));
        }
        //ElectionGenerator
        if (!faults[11]) {
            electionBoard.electionGenerator.setGenerator(electionBoard.electionGenerator.getGenerator().add(BigInteger.ONE));
        }
        //VerificationKeys
        if (!faults[12]) {
            electionBoard.verificationKeys.getKey().remove(electionBoard.verificationKeys.getKey().size() - 1);
        }
        //CACertificate
        if (!faults[13]) {
            Certificate caCertificate = new Certificate();
            caCertificate.setValue(CertificateGenerator.main(ConfigHelper.getCertificateAuthorityId(), keyStore.electionManagerPrivateKey, keyStore.certificateAuthorityPublicKey).getBytes());
            electionBoard.electionSystemInfo.setCertificateAuthority(caCertificate);
        }
        //EMCertificate
        if (!faults[14]) {
            Certificate emCertificate = new Certificate();
            emCertificate.setValue(CertificateGenerator.main(ConfigHelper.getCertificateAuthorityId(), keyStore.certificateAuthorityPrivateKey, keyStore.electionManagerPublicKey).getBytes());
            electionBoard.electionSystemInfo.setElectionManager(emCertificate);
        }
        //EACertificate
        if (!faults[15]) {
            Certificate eaCertificate = new Certificate();
            eaCertificate.setValue(CertificateGenerator.main(ConfigHelper.getElectionAdministratorId(), keyStore.certificateAuthorityPrivateKey, keyStore.electionAdministratorPublicKey).getBytes());
            electionBoard.electionSystemInfo.setElectionAdministration(eaCertificate);
        }
        //TallierCertificates
        if (!faults[16]) {
            Certificate certificate = new Certificate();
            certificate.setValue(CertificateGenerator.main("fake", keyStore.certificateAuthorityPrivateKey, keyStore.electionAdministratorPublicKey).getBytes());
            electionBoard.electionSystemInfo.getTallier().set(0, certificate);
        }
        //MixerCertificates
        if (!faults[17]) {
            Certificate certificate = new Certificate();
            certificate.setValue(CertificateGenerator.main("fake", keyStore.certificateAuthorityPrivateKey, keyStore.electionAdministratorPublicKey).getBytes());
            electionBoard.electionSystemInfo.getMixer().set(0, certificate);
        }
        //VotersCertificates
        if (!faults[18]) {
            Certificate certificate = new Certificate();
            certificate.setValue(CertificateGenerator.main("fake", keyStore.votersPrivateKey[0], keyStore.votersPublicKey[0]).getBytes());
            electionBoard.voterCertificates.getCertificate().add(certificate);
        }
        //EACertificateSignature
        if (!faults[19]) {
            //Not yet implemented
        }
        if (!faults[20]) {
            Signature signature = electionBoard.electionDefinition.getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            electionBoard.electionDefinition.setSignature(signature);
        }
        if (!faults[21]) {
            //Not yet implemented
        }
        if (!faults[22]) {
            Signature signature = electionBoard.encryptionParameters.getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            electionBoard.encryptionParameters.setSignature(signature);
        }
        if (!faults[23]) {
            Signature signature = electionBoard.encryptionKeyShareList[0].getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            electionBoard.encryptionKeyShareList[0].setSignature(signature);
        }
        if (!faults[24]) {
            Signature signature = electionBoard.encryptionKey.getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            electionBoard.encryptionKey.setSignature(signature);
        }
        if (!faults[25]) {
            Signature signature = electionBoard.blindedGeneratorsList[0].getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            electionBoard.blindedGeneratorsList[0].setSignature(signature);
        }
        if (!faults[26]) {
            Signature signature = electionBoard.electionGenerator.getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            electionBoard.electionGenerator.setSignature(signature);
        }
        if (!faults[27]) {
            Signature signature = electionBoard.electionOptions.getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            electionBoard.electionOptions.setSignature(signature);
        }
        if (!faults[28]) {
            Signature signature = electionBoard.electionData.getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            electionBoard.electionData.setSignature(signature);
        }
        if (!faults[29]) {
            electionBoard.encryptionKeyShareList[0].setProof(NIZKP.getProof(
                    "fake",
                    keyStore.talliersDecryptionKey[1],
                    keyStore.talliersEncryptionKey[1],
                    ElectionBoard.encryptionParameters.getPrime(),
                    ElectionBoard.encryptionParameters.getGroupOrder(),
                    ElectionBoard.encryptionParameters.getGenerator()));
        }
        if (!faults[30]) {
            electionBoard.blindedGeneratorsList[0].setProof(NIZKP.getProof(
                    "fake",
                    keyStore.talliersDecryptionKey[1],
                    keyStore.talliersEncryptionKey[1],
                    ElectionBoard.encryptionParameters.getPrime(),
                    ElectionBoard.encryptionParameters.getGroupOrder(),
                    ElectionBoard.encryptionParameters.getGenerator()));
        }
    }
}
