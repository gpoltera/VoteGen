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
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
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
    ConfigHelper config = new ConfigHelper();
    
    //Adapt to config file
    public static final String STORETYPE = "JKS";
    public static final int KEYSIZE = 1024;
    public static final int VALIDITY = 1000; // days

    /*
     * keytool -genkeypair -storetype JKS -keystore config\keystore.jks -storepass %password% -keypass %password% -alias vsuzh -keyalg RSA -keysize 1024 -dname "CN=VSUZH UniverstÃ¤t ZÃ¼rich" -validity 1000
keytool -exportcert -rfc -keystore config\keystore.jks -storepass %password% -alias vsuzh -file data\output\vsuzh.pem
     */

    public String getCertficate(String alias, RSAPrivateKey privateKey, RSAPublicKey publicKey) {
        // 
        CertificateGenerator ku = new CertificateGenerator();
        X509Certificate cert = ku.createCertitificate("CN=" + config.getElectionId(), config.getSignatureAlgorithm(), privateKey, publicKey);
        String pem = ku.x509ToBase64PEMString(cert);
        
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
    public X509Certificate createCertitificate(String cname, String signatureAlgorithm, RSAPrivateKey privateKey, RSAPublicKey publicKey) {
        X509Certificate cert = null;
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
            X509V1CertificateGenerator certGenerator = new X509V1CertificateGenerator();
            X500Principal dnName = new X500Principal(cname);
            certGenerator.setSerialNumber(serialNumber);
            certGenerator.setIssuerDN(dnName);
            certGenerator.setNotBefore(startDate);
            certGenerator.setNotAfter(expiryDate);
            certGenerator.setSubjectDN(dnName);     // note: same as issuer
            certGenerator.setPublicKey(publicKey);
            certGenerator.setSignatureAlgorithm(signatureAlgorithm);
            
            // get certificate
            cert = certGenerator.generate(privateKey, "BC");
            
        } catch (CertificateEncodingException | IllegalStateException | NoSuchProviderException | NoSuchAlgorithmException | SignatureException | InvalidKeyException ex) {
            Logger.getLogger(CertificateGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cert;
    }

    /**
     * Converts a X509Certificate instance into a Base64 encoded string (PEM format).
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
