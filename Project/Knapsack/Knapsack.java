import java.io.*;
import java.util.*;

public class Knapsack
{
	static int setCount = 0; // REMOVE THIS AFTER DEBUG
	public static void main( String[] args ) throws Exception
	{
		
		final int SET_LENGTH = 16;
		int[] set = new int[SET_LENGTH];
		Scanner infile = new Scanner( new File(args[0]) );

		for (int i = 0; i < SET_LENGTH ; i++ ) set[i] = infile.nextInt();
		int target = infile.nextInt(); // the number on the second line
		infile.close(); 
		
		for (int i = 0; i < SET_LENGTH; i++ )
			System.out.print(set[i]);
		System.out.println();

		System.out.println(target);

		for (int bitMap=0 ; bitMap < 65536 ; ++ bitMap) // test a possible of array bitmap 是 01010101011111 这种的
		{
			int sum = 0;
			for (int i = SET_LENGTH-1 ; i >=0  ; --i ) //建立一种可能的target
				if ( (bitMap >> i) % 2 == 1 )
						sum = sum + set[i];
	
			if(sum == target) printSet( set, bitMap );//检验这种target能否print
		}
	} // END MAIN
	static void printSet( int[] set, int bitMap )
	{
		for (int i = 15; i >= 0; --i){
			if((bitMap >> i) % 2 ==1)
				System.out.print( set[i] + " ");
		}
		System.out.println();
		//System.out.println( "setCount: " + ++setCount + ".  bitMap: " + bitMap );
	}
	
} // END CLASS