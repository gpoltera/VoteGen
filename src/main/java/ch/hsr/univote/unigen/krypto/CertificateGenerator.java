/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.krypto;

import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.x509.X509V1CertificateGenerator;

/**
 *
 * @author Gian Poltéra
 */
public class CertificateGenerator {
    //Adapt to config file
    public static final String STORETYPE = "JKS";
    public static final String KEYALG = "RSA";
    public static final int KEYSIZE = 1024;
    public static final int VALIDITY = 1000; // days
    public static final String SIGNATUREALGSPEC = "SHA256WithRSA";

    /*
     * keytool -genkeypair -storetype JKS -keystore config\keystore.jks -storepass %password% -keypass %password% -alias vsuzh -keyalg RSA -keysize 1024 -dname "CN=VSUZH UniverstÃ¤t ZÃ¼rich" -validity 1000
keytool -exportcert -rfc -keystore config\keystore.jks -storepass %password% -alias vsuzh -file data\output\vsuzh.pem
     */

    public static String main(String alias, RSAPrivateKey privateKey, RSAPublicKey publicKey) throws Exception {
        // @Stephan: Please adapt...
        CertificateGenerator ku = new CertificateGenerator();
        X509Certificate cert = ku.createCertitificate("CN=" + ConfigHelper.getElectionId(), privateKey, publicKey);
        KeyStore ks = ku.createAndPopulateKeyStore(cert, alias);
        String pem = ku.x509ToBase64PEMString(cert);
        
        return pem;
    }

    /**
     * Creates an X509 V1 certificate. Uses BouncyCastle as security provider.
     *
     * @param cname canonicla name of the form "CN=..."
     * @return an X%09 V1 certificate
     * @throws Exception
     */
    public X509Certificate createCertitificate(String cname, RSAPrivateKey privateKey, RSAPublicKey publicKey)
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


        // construct certificate
        X509V1CertificateGenerator certGenerator = new X509V1CertificateGenerator();
        X500Principal dnName = new X500Principal(cname);
        certGenerator.setSerialNumber(serialNumber);
        certGenerator.setIssuerDN(dnName);
        certGenerator.setNotBefore(startDate);
        certGenerator.setNotAfter(expiryDate);
        certGenerator.setSubjectDN(dnName);     // note: same as issuer
        certGenerator.setPublicKey(publicKey);
        certGenerator.setSignatureAlgorithm(SIGNATUREALGSPEC);

        // get certificate
        X509Certificate cert = certGenerator.generate(privateKey, "BC");
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
