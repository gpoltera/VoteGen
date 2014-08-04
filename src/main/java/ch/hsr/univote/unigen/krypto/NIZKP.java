/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.common.Proof;
import ch.hsr.univote.unigen.helper.StringConcatenator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Gian Poltéra
 */
public class NIZKP {
    ConfigHelper config = new ConfigHelper();
    
    public Proof getProof(String Name, BigInteger a, BigInteger b, BigInteger p, BigInteger q, BigInteger g) throws NoSuchAlgorithmException {
        Proof proof = new Proof();

        // 1. Choose w E X randomly
        // 2. Compute t = o(w)
        // 3. Compute c = H(b,t) mod q, q= |image(o)|
        // 4. Compute s = w + c * a
        BigInteger w = PrimeGenerator.getPrime(config.getEncryptionKeyLength());

        // Proof commitment
        BigInteger t = g.modPow(w, p);

        StringConcatenator sc = new StringConcatenator();
        sc.pushObject(b);
        sc.pushObject(t);
        sc.pushObject(Name);

        String res = sc.pullAll();

        // c = H(b,t) mod q
        BigInteger c = Hash.getHash(res, config.getHashAlgorithm(), config.getCharEncoding()).mod(q);

        // Proof response
        BigInteger s = c.multiply(a).add(w);

        proof.getCommitment().add(t);
        proof.getResponse().add(s);

        return proof;
    }
    
    public Proof getProof(String tallierName, BigInteger a, EncryptionKeyShare encryptionKeyShare, PartiallyDecryptedVotes partiallyDecryptedVotes, BigInteger p, BigInteger q, BigInteger g) throws NoSuchAlgorithmException {
        Proof proof = new Proof();
        
        BigInteger w = PrimeGenerator.getPrime(config.getEncryptionKeyLength());
        
        StringConcatenator sc = new StringConcatenator();
        sc.pushObject(encryptionKeyShare.getKey());
        
        for (int i = 0; i < partiallyDecryptedVotes.getVote().size(); i++) {
            sc.pushObject(partiallyDecryptedVotes.getVote().get(i));
            BigInteger t = g.modPow(w, p);
            // Proof commitment
            proof.getCommitment().add(t);
        }
        
        for (int i = 0; i < proof.getCommitment().size(); i++) {
            sc.pushObject(proof.getCommitment().get(i));
        }
        
        sc.pushObject(tallierName);
        
        String res = sc.pullAll();
        
        BigInteger c = Hash.getHash(res, config.getHashAlgorithm(), config.getCharEncoding()).mod(q);
        BigInteger s = c.multiply(a).add(w);
        
        
        proof.getResponse().add(s);
        
        return proof;
    }
}
