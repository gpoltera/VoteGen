package ch.hsr.univote.unigen;

import ch.bfh.univote.common.ElectionSystemInfo;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        ElectionSystemInfo esi = new ElectionSystemInfo();
        esi.setElectionId("some-election-2013");
        System.out.println( "Hello World!" );
    }
}
