package ch.hsr.univote.unigen;

import ch.hsr.univote.unigen.generator.ElectionDefinitionTask;
import ch.hsr.univote.unigen.generator.ElectionOptionsTask;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import ch.hsr.univote.unigen.board.Publisher;
import ch.hsr.univote.unigen.generator.BallotsTask;
import ch.hsr.univote.unigen.generator.BlindedGeneratorTask;
import ch.hsr.univote.unigen.generator.DecodedVotesTask;
import ch.hsr.univote.unigen.generator.DecryptedVotesTask;
import ch.hsr.univote.unigen.generator.ElectionDataTask;
import ch.hsr.univote.unigen.generator.ElectionGeneratorTask;
import ch.hsr.univote.unigen.generator.ElectionResultsTask;
import ch.hsr.univote.unigen.generator.ElectoralRollTask;
import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xml.sax.SAXException;

/**
 * Test
 *
 */
public class App
{
    public static void main( String[] args ) throws JAXBException, FileNotFoundException, SAXException, DatatypeConfigurationException, CertificateException, Exception
    {
        ElectionDefinitionTask.run();
        ElectionOptionsTask.run();
        ElectoralRollTask.run();
        ElectionGeneratorTask.run();
        BlindedGeneratorTask.run();
        ElectionDataTask.run();
        BallotsTask.run();
        DecodedVotesTask.run();
        DecryptedVotesTask.run();
        ElectionResultsTask.run();
        WahlGenerator.addElectionId("HSR2013");
        
        Publisher.main(args);
        
        System.out.println("WebService gestartet");
    } 
}