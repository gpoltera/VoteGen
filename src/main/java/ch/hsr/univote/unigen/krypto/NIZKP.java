/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.EncryptedVotes;
import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.common.Proof;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.helper.StringConcatenator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.math.BigInteger;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class NIZKP {

    ConfigHelper config;

    public NIZKP() {
        this.config = VoteGenerator.config;
    }

    /*1.4.3 DistributedKey*/
    public Proof getEncryptionKeyShareProof(String tallier, DSAPrivateKey privateKey, DSAPublicKey publicKey) {
        BigInteger p = publicKey.getParams().getP();
        BigInteger q = publicKey.getParams().getQ();
        BigInteger g = publicKey.getParams().getG();
        BigInteger x = privateKey.getX();
        BigInteger y = publicKey.getY();

        Proof proof = new Proof();

        // 1. Choose w E q randomly
        // 2. Compute t = g^w mod p
        // 3. Compute c = H(y_j,t,T_j) mod q
        // 4. Compute s = w + c * x_j mod q
        BigInteger w = PrimeGenerator.getPrime(q.bitLength() - 1);
        BigInteger t = g.modPow(w, p);

        StringConcatenator sc = new StringConcatenator();
        sc.pushObject(y);
        sc.pushObject(t);
        sc.pushObject(tallier);
        String res = sc.pullAll();
        
        BigInteger c = new Hash().getHash(res, config.getHashAlgorithm(), config.getCharEncoding()).mod(q);
        BigInteger s = (c.multiply(x).add(w)).mod(q);

        proof.getCommitment().add(t);
        proof.getResponse().add(s);

        return proof;
    }

    /*1.4.4 ElectionGenerator*/
    public Proof getBlindedGeneratorProof(String mixer, BigInteger g_k, BigInteger previous_g_k, DSAPrivateKey privateKey) {
        BigInteger p = privateKey.getParams().getP();
        BigInteger q = privateKey.getParams().getQ();
        BigInteger a = privateKey.getX();

        Proof proof = new Proof();

        // 1. Choose w E q randomly
        // 2. Compute t = (g_k-1)^w mod p
        // 3. Compute c = H(g_k,t,M_k) mod q
        // 4. Compute s = w + c * a_k mod q
        BigInteger w = PrimeGenerator.getPrime(q.bitLength() - 1);
        BigInteger t = previous_g_k.modPow(w, p);

        StringConcatenator sc = new StringConcatenator();
        sc.pushObject(g_k);
        sc.pushObject(t);
        sc.pushObject(mixer);
        String res = sc.pullAll();

        BigInteger c = new Hash().getHash(res, config.getHashAlgorithm(), config.getCharEncoding()).mod(q);
        BigInteger s = (c.multiply(a).add(w)).mod(q);

        proof.getCommitment().add(t);
        proof.getResponse().add(s);

        return proof;
    }

    /*1.4.5 MixedVerificationKeys*/
    public Proof getMixedVerificationKeysProof(String mixer) {
        //Not yet implemented

        Proof proof = new Proof();

        return proof;
    }

    /*1.4.6 Ballot*/
    public Proof getBallotProof(BigInteger a, BigInteger vk_j, EncryptionParameters encryptionParameters) {
        BigInteger p = encryptionParameters.getPrime();
        BigInteger q = encryptionParameters.getGroupOrder();
        BigInteger g = encryptionParameters.getGenerator();
        
        Proof proof = new Proof();
        
        // 1. Choose w E q randomly
        // 2. Compute t = g^w mod p
        // 3. Compute c = H(a,t,vk'_j) mod q
        // 4. Compute s = w + c * r_i mod q
        BigInteger w = PrimeGenerator.getPrime(q.bitLength() - 1);
        BigInteger t = g.modPow(w, p);
        
        StringConcatenator sc = new StringConcatenator();
        sc.pushObject(a);
        sc.pushObject(t);
        sc.pushObject(vk_j);
        String res = sc.pullAll();
        
        BigInteger c = new Hash().getHash(res, config.getHashAlgorithm(), config.getCharEncoding()).mod(q);
        BigInteger s = (c.multiply(a).add(w)).mod(q);

        proof.getCommitment().add(t);
        proof.getResponse().add(s);

        return proof;
    }
    
    /*1.4.7 MixedEncryptedVotes*/
    public Proof getMixedEncryptedVotesProof(String mixer) {
        //Not yet implemented

        Proof proof = new Proof();

        return proof;
    }
    
    /*1.4.8 PartiallyDecryptedVotes*/
    public Proof getPartiallyDecryptedVotesProof(String tallier, PartiallyDecryptedVotes partiallyDecryptedVotes, EncryptedVotes encryptedVotes, DSAPrivateKey privateKey, DSAPublicKey publicKey) {
        BigInteger p = publicKey.getParams().getP();
        BigInteger q = publicKey.getParams().getQ();
        BigInteger g = publicKey.getParams().getG();
        BigInteger x = privateKey.getX();
        BigInteger y = publicKey.getY();
        List<BigInteger> t_list = new ArrayList<>();
        
        Proof proof = new Proof();
        
        // 1. Choose w E q randomly
        // 2. Compute t = g^w mod p
        // 3. Compute c = H(a,t,vk'_j) mod q
        // 4. Compute s = w + c * r_i mod q
        BigInteger w = PrimeGenerator.getPrime(q.bitLength() - 1);
        t_list.add(g.modPow(w, p));
        for (EncryptedVote encryptedVote : encryptedVotes.getVote()) {
            t_list.add(encryptedVote.getFirstValue().modPow(w.negate(), p));
        }
  
        StringConcatenator sc = new StringConcatenator();
        sc.pushObject(y);
        sc.pushList(partiallyDecryptedVotes.getVote(), true);
        sc.pushList(t_list, true);
        sc.pushObject(tallier);
        String res = sc.pullAll();
        
        BigInteger c = new Hash().getHash(res, config.getHashAlgorithm(), config.getCharEncoding()).mod(q);
        BigInteger s = (c.multiply(x).add(w)).mod(q);
        
        for (BigInteger t : t_list) {
            proof.getCommitment().add(t);
        }
        proof.getResponse().add(s);

        return proof;
    }
}
