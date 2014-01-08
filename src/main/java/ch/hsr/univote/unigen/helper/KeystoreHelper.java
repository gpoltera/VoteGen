/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.x509.X509V3CertificateGenerator;

/**
 *
 * @author Gian Poltéra
 */
public class KeystoreHelper {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static X509Certificate createKeyPair() {
        try {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(ConfigHelper.getSignatureKeyType());
            keyGenerator.initialize(ConfigHelper.getSignatureKeyLength());
            KeyPair keypair = keyGenerator.generateKeyPair();
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null);
            X509Certificate certificate = generateCertificate(keypair);
            char[] password = ConfigHelper.getKeystorePassword().toCharArray();
            keystore.setKeyEntry(ConfigHelper.getSignatureKeyAlias(), keypair.getPrivate(), password, new Certificate[]{certificate});
            OutputStream ostream = new FileOutputStream(ConfigHelper.getKeystorePath());
            keystore.store(ostream, password);
            return certificate;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static X509Certificate generateCertificate(KeyPair keypair) {
        try {
            X500Principal principal = new X500Principal("CN=" + ConfigHelper.getSignatureKeyName());
            Calendar calendar = Calendar.getInstance();
            Date startDate = calendar.getTime();
            calendar.add(Calendar.DATE, ConfigHelper.getSignatureKeyValidity());
            Date endDate = calendar.getTime();
            X509V3CertificateGenerator certificate = new X509V3CertificateGenerator();
            certificate.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
            certificate.setSubjectDN(principal);
            certificate.setIssuerDN(principal);
            certificate.setPublicKey(keypair.getPublic());
            certificate.setNotBefore(startDate);
            certificate.setNotAfter(endDate);
            certificate.setSignatureAlgorithm(ConfigHelper.getSignatureAlgorithm());
            return certificate.generate(keypair.getPrivate(), "BC");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void exportCertificate() {
        try {
            KeyStore keystore = loadKeystore();
            Certificate certificate = keystore.getCertificate(ConfigHelper.getSignatureKeyAlias());
            PEMWriter writer = new PEMWriter(new FileWriter(ConfigHelper.getSignatureCertificatePath()));
            writer.writeObject(certificate);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static RSAPrivateKey getPrivateKey() {
        try {
            KeyStore keystore = loadKeystore();
            PrivateKeyEntry entry = (PrivateKeyEntry) keystore.getEntry(
                    ConfigHelper.getSignatureKeyAlias(),
                    new KeyStore.PasswordProtection(ConfigHelper.getKeystorePassword().toCharArray()));
            RSAPrivateKey privateKey = (RSAPrivateKey) entry.getPrivateKey();
            return privateKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static RSAPublicKey getPublicKey() {
        try {
            KeyStore keystore = loadKeystore();
            Certificate certificate = keystore.getCertificate(ConfigHelper.getSignatureKeyAlias());
            return (RSAPublicKey) certificate.getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyStore loadKeystore() throws ConfigException, FileNotFoundException, KeyStoreException, IOException {
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream istream = new FileInputStream(ConfigHelper.getKeystorePath());
            keystore.load(istream, ConfigHelper.getKeystorePassword().toCharArray());
            return keystore;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
