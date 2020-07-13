import java.io.*;
import java.util.*;

public class LinkedListTester
{
	public static void main( String args[] ) throws Exception
	{
		if ( args.length<2 ) 
			die( "FATAL ERROR: Did not put 2 input filenames (i.e. set1.txt set2.txt)\n" + 
			     "on cmd line when you executed this program. EXITING PROGRAM." );
		LinkedList<String> myList = new LinkedList<String>( args[0]);
		System.out.println( "myList loaded from " + args[0] );
		System.out.println("myList: (contains " + myList.size() + " elements) " +  myList ); // invokes toString
		BufferedReader infile = new BufferedReader( new FileReader( args[1] ) );
		System.out.println( "Searching myList for the following words from " + args[1] );
		while (infile.ready() )
		{	
			String word = infile.readLine();
			if ( myList.contains( word ) )
				System.out.println( word + "\tfound" );
			else
				System.out.println( word + "\tNOT found" );
		}
	} // END MAIN

	static void die( String errMsg )
	{
		System.out.println( errMsg );
		System.exit(0);
	}
} // EOF