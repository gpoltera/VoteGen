/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.krypto;

import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.x509.X509V1CertificateGenerator;

/**
 *
 * @author Gian Poltéra
 */
public class CertificateGenerator {

    private ConfigHelper config;

    //Adapt to config file
    public static final int VALIDITY = 1000; // days

    public CertificateGenerator() {
        this.config = VoteGenerator.config;
    }

    /*
     * keytool -genkeypair -storetype JKS -keystore config\keystore.jks -storepass %password% -keypass %password% -alias vsuzh -keyalg RSA -keysize 1024 -dname "CN=VSUZH UniverstÃ¤t ZÃ¼rich" -validity 1000
     keytool -exportcert -rfc -keystore config\keystore.jks -storepass %password% -alias vsuzh -file data\output\vsuzh.pem
     */
    public String getCertficate(String alias, PrivateKey privateKey, PublicKey publicKey) {
        //
        X509Certificate cert = createCertitificate("CN=" + config.getElectionId(), config.getSignatureAlgorithm(), privateKey, publicKey);
        String pem = x509ToBase64PEMString(cert);
        
        return pem;
    }

    /**
     * Creates an X509 V1 certificate. Uses BouncyCastle as security provider.
     *
     * @param cname canonicla name of the form "CN=..."
     * @param signatureAlgorithm
     * @param privateKey
     * @param publicKey
     * @return an X%09 V1 certificate
     */
    public X509Certificate createCertitificate(String cname, String signatureAlgorithm, PrivateKey privateKey, PublicKey publicKey) {
        X509Certificate certificate = null;
        try {
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
            X509V1CertificateGenerator certificateGenerator = new X509V1CertificateGenerator();
            X500Principal dnName = new X500Principal(cname);
            certificateGenerator.setSerialNumber(serialNumber);
            certificateGenerator.setIssuerDN(dnName);
            certificateGenerator.setNotBefore(startDate);
            certificateGenerator.setNotAfter(expiryDate);
            certificateGenerator.setSubjectDN(dnName);     // note: same as issuer
            certificateGenerator.setPublicKey(publicKey);
            certificateGenerator.setSignatureAlgorithm(signatureAlgorithm);

            // get certificate
            certificate = certificateGenerator.generate(privateKey, "BC");

        } catch (CertificateEncodingException | IllegalStateException | NoSuchProviderException | NoSuchAlgorithmException | SignatureException | InvalidKeyException ex) {
            Logger.getLogger(CertificateGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return certificate;
    }

    /**
     * Converts a X509Certificate instance into a Base64 encoded string (PEM
     * format).
     *
     * @param cert a certificate
     * @return a string (PEM format)
     */
    public String x509ToBase64PEMString(X509Certificate cert) {
        StringWriter sw = null;
        try {
            // Convert certificate to PEM format.
            sw = new StringWriter();
            PEMWriter pw = new PEMWriter(sw);
            pw.writeObject(cert);
            pw.flush();
            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(CertificateGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sw.toString();
    }
}
