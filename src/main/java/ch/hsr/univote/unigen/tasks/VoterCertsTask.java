/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Certificate;
import ch.hsr.univote.unigen.board.ElectionBoard;
import static ch.hsr.univote.unigen.board.ElectionBoard.vc;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.CertificateGenerator;
import ch.hsr.univote.unigen.krypto.RSA;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 *
 * @author Gian Poltéra
 */
public class VoterCertsTask extends ElectionBoard{

    public static void run() throws Exception {
        vc.setElectionId(ConfigHelper.getElectionId());
        
        for (int i = 0; i < ConfigHelper.getVotersNumber(); i++) {
            KeyPair keyPair = RSA.getRSAKeyPair();
            votersPrivateKey[i] = (RSAPrivateKey) keyPair.getPrivate();
            votersPublicKey[i] = (RSAPublicKey) keyPair.getPublic();
            
            votersSignatureKey[i] = votersPrivateKey[i].getPrivateExponent();
            votersVerificationKey[i] = signatureParameters.getGenerator().modPow(votersSignatureKey[i], signatureParameters.getPrime());
            
            Certificate certificate = new Certificate();
            certificate.setValue(CertificateGenerator.main("voter" + i + 1, certificateAuthorityPrivateKey, votersPublicKey[i]).getBytes());
            vc.getCertificate().add(certificate);
        }
        
        /*sign by electionamanger*/
        vc.setSignature(SignatureGenerator.createSignature(vc, electionManagerPrivateKey));
        
        /*save in db*/
        DB4O.storeDB(ConfigHelper.getElectionId(),vc);
    }
}
