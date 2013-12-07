/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.bfh.univote.common.DecodedVotes;
import ch.bfh.univote.common.ElectionData;
import ch.bfh.univote.common.ElectionDefinition;
import ch.bfh.univote.common.ElectionOptions;
import ch.bfh.univote.common.ElectoralRoll;
import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.ObjectFactory;
import ch.bfh.univote.common.Signature;
import ch.bfh.univote.common.SignatureParameters;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.bind.Element;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

/**
 *
 * @author Gian
 */
public class SignatureGenerator {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static SignatureParameters getSignature(String signerId) throws DatatypeConfigurationException {
        ObjectFactory of = new ObjectFactory();

        Signature sign = of.createSignature();
        sign.setSignerId(signerId);
        sign.setTimestamp(TimestampGenerator.generateTimestamp());

        //Berechung der Signierung
        sign.setValue(BigInteger.ZERO);
        SignatureParameters sp = new SignatureParameters();
        sp.setSignature(sign);

        // Prime p
        sp.setPrime(new BigInteger("161931481198080639220214033595931441094586304918402813506510547237223787775475425991443924977419330663170224569788019900180050114468430413908687329871251101280878786588515668012772798298511621634145464600626619548823238185390034868354933050128115662663653841842699535282987363300852550784188180264807606304297"));

        // GroupOrder q
        sp.setGroupOrder(new BigInteger("65133683824381501983523684796057614145070427752690897588060462960319251776021"));

        // Generator g
        sp.setGenerator(new BigInteger("109291242937709414881219423205417309207119127359359243049468707782004862682441897432780127734395596275377218236442035534825283725782836026439537687695084410797228793004739671835061419040912157583607422965551428749149162882960112513332411954585778903685207256083057895070357159920203407651236651002676481874709"));

        return sp;
    }

    private static final BigInteger[] sign(BigInteger m, BigInteger p, BigInteger q, BigInteger g, BigInteger sk) throws NoSuchAlgorithmException {

        BigInteger r = PrimeGenerator.getPrime(1024).mod(q);
        byte[] gr = g.modPow(r, p).toByteArray();
        byte[] mgr = ByteUtils.concatenate(m.toByteArray(), gr);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(mgr);

        BigInteger a = new BigInteger(md.digest());
        a = a.mod(q);

        BigInteger b = r.subtract(sk.multiply(a).mod(p));

        BigInteger[] S = new BigInteger[2];
        S[0] = b;
        S[1] = a;

        return S;
    }

    public static Signature createSignature(ElectionDefinition electionDefinition, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        SecureRandom random = new SecureRandom();
        byte[] sign = RSASignatur.sign(electionDefinition.toString().getBytes(), privateKey, random);
        signature.setValue(new BigInteger(sign));

        return signature;
    }

    public static Signature createSignature(ElectionOptions electionOptions, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        SecureRandom random = new SecureRandom();
        byte[] sign = RSASignatur.sign(electionOptions.toString().getBytes(), privateKey, random);
        signature.setValue(new BigInteger(sign));

        return signature;
    }

    public static Signature createSignature(ElectoralRoll roll, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        SecureRandom random = new SecureRandom();
        byte[] sign = RSASignatur.sign(roll.toString().getBytes(), privateKey, random);
        signature.setValue(new BigInteger(sign));

        return signature;
    }

    public static Signature createSignature(DecodedVotes dov, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        SecureRandom random = new SecureRandom();
        byte[] sign = RSASignatur.sign(dov.toString().getBytes(), privateKey, random);
        signature.setValue(new BigInteger(sign));

        return signature;
    }

    public static Signature createSignature(EncryptionParameters ep, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        SecureRandom random = new SecureRandom();
        byte[] sign = RSASignatur.sign(ep.toString().getBytes(), privateKey, random);
        signature.setValue(new BigInteger(sign));

        return signature;
    }
    
        public static Signature createSignature(ElectionData edat, RSAPrivateKey privateKey) throws Exception {
        Signature signature = new Signature();
        SecureRandom random = new SecureRandom();
        byte[] sign = RSASignatur.sign(edat.toString().getBytes(), privateKey, random);
        signature.setValue(new BigInteger(sign));

        return signature;
    }
}
