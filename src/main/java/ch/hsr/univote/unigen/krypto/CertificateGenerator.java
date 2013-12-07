/*
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 * Bern University of Applied Sciences, Engineering and Information Technology,
 * Research Institute for Security in the Information Society, E-Voting Group,
 * Biel, Switzerland.
 *
 * Project UniVote.
 *
 * Distributable under GPL license.
 * See terms of license at gnu.org.
 */
package ch.hsr.univote.unigen.krypto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * This class provides methods to create an X509 certificate and to
 * store it in a key store. A PEM representation of a certificate can
 * also be obtained.
 *
 * @author Eric Dubuis &lt;eric.dubuis@bfh.ch&gt;
 */
public class CertificateGenerator {
    public static final String STORETYPE = "JKS";
    public static final String KEYSTORE = "test.jks"; // CHECK WITH MICROSOFT WINDOWS
    public static final String KEYALG = "RSA";
    public static final int KEYSIZE = 1024;
    public static final int VALIDITY = 1000; // days
    public static final String SIGNATUREALGSPEC = "SHA256WithRSA";

    /*
     * keytool -genkeypair -storetype JKS -keystore config\keystore.jks -storepass %password% -keypass %password% -alias vsuzh -keyalg RSA -keysize 1024 -dname "CN=VSUZH UniverstÃ¤t ZÃ¼rich" -validity 1000
keytool -exportcert -rfc -keystore config\keystore.jks -storepass %password% -alias vsuzh -file data\output\vsuzh.pem
     */

    public static String main(String[] args) throws Exception {
        // @Stephan: Please adapt...
        CertificateGenerator ku = new CertificateGenerator();
        X509Certificate cert = ku.createCertitificate("CN=VSUZH UniverstÃ¤t ZÃ¼rich");
        KeyStore ks = ku.createAndPopulateKeyStore(cert, "vsuzh");
        ku.persistKeyStore(ks, new File(KEYSTORE), "12345678");
        String pem = ku.x509ToBase64PEMString(cert);
        System.out.println(pem);
        
        return pem;
    }

    /**
     * Creates an X509 V1 certificate. Uses BouncyCastle as security provider.
     *
     * @param cname canonicla name of the form "CN=..."
     * @return an X%09 V1 certificate
     * @throws Exception
     */
    public X509Certificate createCertitificate(String cname)
        throws Exception
    {
        // See also:
        // http://www.bouncycastle.org/wiki/display/JA1/X.509+Public+Key+Certificate+and+Certification+Request+Generation
        Security.addProvider(new BouncyCastleProvider());

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        // time from which certificate is valid
        Date startDate = c.getTime();
        c.add(Calendar.DATE, VALIDITY); // Adding count of VALIDITY days
        // time after which certificate is not valid
        Date expiryDate = c.getTime();

        // serial number for certificate
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

        // public/private key pair
        // See also:
        // http://stackoverflow.com/questions/1709441/generate-rsa-key-pair-and-encode-private-as-string
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(KEYSIZE);
        KeyPair keyPair = keyGenerator.generateKeyPair();


        // construct certificate
        X509V1CertificateGenerator certGenerator = new X509V1CertificateGenerator();
        X500Principal dnName = new X500Principal(cname);
        certGenerator.setSerialNumber(serialNumber);
        certGenerator.setIssuerDN(dnName);
        certGenerator.setNotBefore(startDate);
        certGenerator.setNotAfter(expiryDate);
        certGenerator.setSubjectDN(dnName);     // note: same as issuer
        certGenerator.setPublicKey(keyPair.getPublic());
        certGenerator.setSignatureAlgorithm(SIGNATUREALGSPEC);

        // get certificate
        X509Certificate cert = certGenerator.generate(keyPair.getPrivate(), "BC");
        return cert;
    }

    /**
     * Creates a key store and populates it with the given certificate
     * @param certificate a (X509) certificate
     * @param alias its alias
     * @return a key store containing the certificate
     * @throws Exception if there is an error
     */
    public KeyStore createAndPopulateKeyStore(X509Certificate certificate, String alias)
        throws Exception
    {
        // create an uninitialized key store
        KeyStore ks = KeyStore.getInstance(STORETYPE);
        // initialize it
        ks.load(null, null);
        // add certificate, associate it with the given alias
        ks.setCertificateEntry(alias, certificate);
        return ks;
    }

    /**
     * Persists the given key store in a file with given name, abd
     * protects its integrity with given password.
     * @param ks a key store
     * @param file the name of the key store file
     * @param storePassword a password for the integrity protection
     * @throws Exception if there is an error
     */
    public void persistKeyStore(KeyStore ks, File file, String storePassword) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        ks.store(fos, storePassword.toCharArray());
    }

    /**
     * Converts a X509Certificate instance into a Base64 encoded string (PEM format).
     * @param cert a certificate
     * @return a string (PEM format)
     * @throws IOException if the conversion fails
     */
    public String x509ToBase64PEMString(X509Certificate cert) throws IOException {
        // Convert certificate to PEM format.
        StringWriter sw = new StringWriter();
        PEMWriter pw = new PEMWriter(sw);
        pw.writeObject(cert);
        pw.flush();
        pw.close();
        return sw.toString();
    }
}