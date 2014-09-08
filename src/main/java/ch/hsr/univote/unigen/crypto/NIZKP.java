/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.crypto;

import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.EncryptedVotes;
import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.common.SignatureParameters;
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

    /*1.4.2 LatelyMixedVerificationKeys*/
    public Proof getLatelyMixedVerificationKeysProof(String mixer, BigInteger previous_VerificationKey, BigInteger new_VerificationKey, BigInteger previous_g, DSAPrivateKey privateKey, SignatureParameters signatureParameters) {        
        BigInteger p = signatureParameters.getPrime();
        BigInteger q = signatureParameters.getGroupOrder();
        BigInteger g = signatureParameters.getGenerator();

        BigInteger a = privateKey.getX();
        BigInteger g_k = privateKey.getParams().getG();        
        
        Proof proof = new Proof();

        // 1. Choose w E q randomly
        // 2. Compute t = (t1,t2) = (g_k-1^w mod p, vk_ik-1^w mod p
        // 3. Compute c = H(g_k,vk_ik,t,M) mod q
        // 4. Compute s = w + c * a mod q
        BigInteger w = PrimeGenerator.getPrime(q.bitLength() - 1);
        BigInteger t1 = previous_g.modPow(w, p);
        BigInteger t2 = previous_VerificationKey.modPow(w, p);
        
        StringConcatenator sc = new StringConcatenator();
        sc.pushObject(g_k);
        sc.pushObject(new_VerificationKey);
        sc.pushObject(t1);
        sc.pushObject(t2);
        sc.pushObject(mixer);
        String res = sc.pullAll();
        
        BigInteger c = new Hash().getHash(res, config.getHashAlgorithm(), config.getCharEncoding()).mod(q);
        BigInteger s = (c.multiply(a).add(w)).mod(q);
        
        proof.getCommitment().add(t1);
        proof.getCommitment().add(t2);
        proof.getResponse().add(s);
        
        return proof;
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
    public Proof getBallotProof(BigInteger a, BigInteger vk_j, BigInteger r, EncryptionParameters encryptionParameters) {
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
        sc.pushInnerDelim();
        sc.pushObject(t);
        sc.pushInnerDelim();
        sc.pushObject(vk_j);
        String res = sc.pullAll();

        BigInteger c = new Hash().getHash(res, config.getHashAlgorithm(), config.getCharEncoding()).mod(q);
        BigInteger s = (c.multiply(r).add(w)).mod(q);

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
    public Proof getPartiallyDecryptedVotesProof(String tallier, EncryptedVotes encryptedVotes, PartiallyDecryptedVotes partiallyDecryptedVotes, DSAPrivateKey privateKey, DSAPublicKey publicKey) {
        BigInteger p = publicKey.getParams().getP();
        BigInteger q = publicKey.getParams().getQ();
        BigInteger g = publicKey.getParams().getG();
        BigInteger x = privateKey.getX();
        BigInteger y = publicKey.getY();
        List<BigInteger> t_list = new ArrayList<>();

        Proof proof = new Proof();

        // 1. Choose w E q randomly
        // 2. Compute t = g^w mod p, a1^-w mod p ,...
        // 3. Compute c = H(y,aj,t,T) mod q
        // 4. Compute s = w + c * x mod q
        BigInteger w = PrimeGenerator.getPrime(q.bitLength() - 1);

        StringConcatenator sc = new StringConcatenator();
        sc.pushObject(y);

        for (BigInteger aj : partiallyDecryptedVotes.getVote()) {
            sc.pushObject(aj);
        }

        t_list.add(g.modPow(w, p));
        sc.pushObject(g.modPow(w, p));
        for (EncryptedVote encryptedVote : encryptedVotes.getVote()) {
            BigInteger a = encryptedVote.getFirstValue();
            BigInteger t = a.modPow(w.negate(), p);
            t_list.add(t);
            sc.pushObject(t);
        }
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
