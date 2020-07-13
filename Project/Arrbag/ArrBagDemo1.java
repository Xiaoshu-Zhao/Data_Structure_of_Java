// this file formatted by JFormat - jlbalder@netscape.net
public class ArrBagDemo1
{
    public static void main(String a[])
    {
        // INSTANCE AN OBECT OF OUR GENERIC CLASS AND SPECIFY IT AS A CONTAINER OF TYPE ARRAY OF STRING
        ArrBag<String> bag1 = new ArrBag<String>();
        String[] words =
        {
            "alpha", "bravo", "charlie",  "delta",  "echo"
        };
        
        for ( String word : words )
        bag1.add( word );
        System.out.println( "bag1 contains these strings: " + bag1 );
        String[] keys =
        {
            "alpha", "bristo", "charlie",  "dakota",  "echo"
        };
        
        for ( String key : keys )
        if ( bag1.contains( key ) )
        System.out.println( key + " found" );
        else
        System.out.println( key + " NOT found" );
    }//~public static void main(...
}//~public class ArrBagDemo1...

// T IS PLACEHOLDER FOR ACTUAL TYPE TAHT WILL BE PASSED IN FROM MAIN OR CLIENT
class ArrBag<T>
{
    final int NOT_FOUND = -1;
    final int INITIAL_CAPACITY = 10;
    private int count; // LOGICAL SIZE
    
    // DEFINE & INITIALIZE ARRAY OF T OBJECTS
    @SuppressWarnings("unchecked") 
	T[] theArray = (T[]) new Object[INITIAL_CAPACITY];
	
    public ArrBag(  )
    {
        count = 0; // i.e. logical size, actual number of elems in the array
    }
    public int size()
    {
        return count;
    }    
    public boolean add( T element )
    {
        if (element == null) return false;
        if (size() == theArray.length) return false; // FAILS IF WE ARE ALREADY FULL
        theArray[ count++] = element; // WE RELAX RULE ABOUT ASSIGNING INTO COUNT OTHER THAN IN SETTER
        return true; // success. it was added
    }
    
    // SEARCHES FOR THE KEY. TRUE IF FOUND, OTHERWISE FALSE
    public boolean contains( T key )
    {
        if (key == null) return false;
        for ( int i=0 ; i < size() ; ++i )
        if ( theArray[i].equals( key ) ) return true;/ WE ARE MAKING AN ASSUMPTION ABOUT TYPE T... WHAT IS IT?
	
        return false;
    }
    
    public String toString()
    {
        String toString  = ""; // YES YOU ARE ALLOWED TO NAME VAR SAME AS METHOD IT's IN
        for ( int i=0 ; i < size() ; ++i  )
        {
            toString += theArray[i];
            if ( i < size()-1 ) 	// DONT PUT SPACE AFTER LAST ELEM
            toString += " ";
        }
        return toString;
    }//~public String toString()...
}//~class ArrBag<T>...

