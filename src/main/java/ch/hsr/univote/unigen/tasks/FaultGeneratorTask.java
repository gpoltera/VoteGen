/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.board.ElectionBoard;
import static ch.hsr.univote.unigen.board.ElectionBoard.certificateAuthorityPrivateKey;
import static ch.hsr.univote.unigen.board.ElectionBoard.certificateAuthorityPublicKey;
import static ch.hsr.univote.unigen.board.ElectionBoard.esi;
import static ch.hsr.univote.unigen.board.ElectionBoard.talliersDecryptionKey;
import static ch.hsr.univote.unigen.board.ElectionBoard.talliersEncryptionKey;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.CertificateGenerator;
import ch.hsr.univote.unigen.krypto.NIZKP;
import ch.hsr.univote.unigen.krypto.NoPrimeGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class FaultGeneratorTask extends ElectionBoard {
    
    public static void run() throws Exception {
        //load the fault-config
        boolean[] faults = ConfigHelper.getFaults();

        //Schnorr Parameter
        if (!faults[0]) {
            signatureParameters.setPrime(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[1]) {
            signatureParameters.setGroupOrder(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[2]) {
            signatureParameters.setGenerator(BigInteger.TEN);
        }
        if (!faults[3]) {
            signatureParameters.setPrime(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[4]) {
            signatureParameters.setPrime(signatureParameters.getPrime().divide(BigInteger.TEN));
        }
        //ElGamal Parameters
        if (!faults[5]) {
            encryptionParameters.setPrime(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[6]) {
            encryptionParameters.setGroupOrder(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[7]) {
            encryptionParameters.setGenerator(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[8]) {
            encryptionParameters.setPrime(NoPrimeGenerator.getNoPrime());
        }
        if (!faults[9]) {
            encryptionParameters.setPrime(encryptionParameters.getPrime().divide(BigInteger.TEN));
        }
        //EncryptionKey
        if (!faults[10]) {
            ek.setKey(ek.getKey().add(BigInteger.ONE));
        }
        //ElectionGenerator
        if (!faults[11]) {
            eg.setGenerator(eg.getGenerator().add(BigInteger.ONE));
        }
        //VerificationKeys
        if (!faults[12]) {
            vk.getKey().remove(vk.getKey().size() - 1);
        }
        //CACertificate
        if (!faults[13]) {
            Certificate caCertificate = new Certificate();
            caCertificate.setValue(CertificateGenerator.main(ConfigHelper.getCertificateAuthorityId(), electionManagerPrivateKey, certificateAuthorityPublicKey).getBytes());
            esi.setCertificateAuthority(caCertificate);
        }
        //EMCertificate
        if (!faults[14]) {
            Certificate emCertificate = new Certificate();
            emCertificate.setValue(CertificateGenerator.main(ConfigHelper.getCertificateAuthorityId(), certificateAuthorityPrivateKey, electionManagerPublicKey).getBytes());
            esi.setElectionManager(emCertificate);
        }
        //EACertificate
        if (!faults[15]) {
            Certificate eaCertificate = new Certificate();
            eaCertificate.setValue(CertificateGenerator.main(ConfigHelper.getElectionAdministratorId(), certificateAuthorityPrivateKey, electionAdministratorPublicKey).getBytes());
            esi.setElectionAdministration(eaCertificate);
        }
        //TallierCertificates
        if (!faults[16]) {
            Certificate certificate = new Certificate();
            certificate.setValue(CertificateGenerator.main("fake", certificateAuthorityPrivateKey, electionAdministratorPublicKey).getBytes());
            esi.getTallier().set(0, certificate);
        }
        //MixerCertificates
        if (!faults[17]) {
            Certificate certificate = new Certificate();
            certificate.setValue(CertificateGenerator.main("fake", certificateAuthorityPrivateKey, electionAdministratorPublicKey).getBytes());
            esi.getMixer().set(0, certificate);
        }
        //VotersCertificates
        if (!faults[18]) {
            Certificate certificate = new Certificate();
            certificate.setValue(CertificateGenerator.main("fake", votersPrivateKey[0], votersPublicKey[0]).getBytes());
            vc.getCertificate().add(certificate);
        }
        //EACertificateSignature
        if (!faults[19]) {
            //Not yet implemented
        }
        if (!faults[20]) {
            Signature signature = ed.getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            ed.setSignature(signature);
        }
        if (!faults[21]) {
            //Not yet implemented
        }
        if (!faults[22]) {
            Signature signature = encryptionParameters.getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            encryptionParameters.setSignature(signature);
        }
        if (!faults[23]) {
            Signature signature = encryptionKeyShareList[0].getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            encryptionKeyShareList[0].setSignature(signature);
        }
        if (!faults[24]) {
            Signature signature = ek.getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            ek.setSignature(signature);
        }
        if (!faults[25]) {
            Signature signature = blindedGeneratorsList[0].getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            blindedGeneratorsList[0].setSignature(signature);
        }
        if (!faults[26]) {
            Signature signature = eg.getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            eg.setSignature(signature);
        }
        if (!faults[27]) {
            Signature signature = eo.getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            eo.setSignature(signature);
        }
        if (!faults[28]) {
            Signature signature = edat.getSignature();
            signature.setValue(signature.getValue().add(BigInteger.ONE));
            edat.setSignature(signature);
        }
        if (!faults[29]) {
            encryptionKeyShareList[0].setProof(NIZKP.getProof(
                    "fake",
                    talliersDecryptionKey[1],
                    talliersEncryptionKey[1],
                    ElectionBoard.encryptionParameters.getPrime(),
                    ElectionBoard.encryptionParameters.getGroupOrder(),
                    ElectionBoard.encryptionParameters.getGenerator()));
        }
        if (!faults[30]) {
            blindedGeneratorsList[0].setProof(NIZKP.getProof(
                    "fake",
                    talliersDecryptionKey[1],
                    talliersEncryptionKey[1],
                    ElectionBoard.encryptionParameters.getPrime(),
                    ElectionBoard.encryptionParameters.getGroupOrder(),
                    ElectionBoard.encryptionParameters.getGenerator()));
        }
    }
}
