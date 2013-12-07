/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.bfh.univote.common.ElectionDefinition;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Gian
 */
public class RSASignatur {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] sign(byte[] value, PrivateKey privKey, SecureRandom random) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA", "BC");
        signature.initSign(privKey, random);
        signature.update(value);
        byte[] signBytes = signature.sign();
        
        return signBytes;
    }
    
    
    public static ch.bfh.univote.common.Signature createSignature(ElectionDefinition electionDefinition) throws Exception {
        List<String> encoded = new ArrayList<String>();
		encoded.add(electionDefinition.getElectionId());
		encoded.add(electionDefinition.getTitle());
		encoded.add(Integer.toString(electionDefinition.getKeyLength()));
		encoded.add(electionDefinition.getMixerId().toString());
		encoded.add(electionDefinition.getTallierId().toString());
		encoded.add(electionDefinition.getVotingPhaseBegin().toString());
		encoded.add(electionDefinition.getVotingPhaseEnd().toString());
                
                SecureRandom sc = new SecureRandom();
                byte[] signvalue = sign(encoded.toString().getBytes(), RSAGenerator.getPrivateKey(), sc);
                
                ch.bfh.univote.common.Signature signature = new ch.bfh.univote.common.Signature();
                signature.setSignerId("Gian Poltera");
                signature.setTimestamp(TimestampGenerator.generateTimestamp());
                signature.setValue(new BigInteger(signvalue));
                
        return signature;
    }
}
