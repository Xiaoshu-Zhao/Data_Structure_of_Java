import java.io.*;
import java.util.*;

public class CDLL_JosephusList<T>
{
	private CDLL_Node<T> head;  // pointer to the front (first) element of the list
	private int count=0;
	// private Scanner kbd = new Scanner(System.in); // FOR DEBUGGING. See executeRitual() method 
	public CDLL_JosephusList()
	{
		head = null; // compiler does this anyway. just for emphasis
	}

	// LOAD LINKED LIST FORM INCOMING FILE
	
	public CDLL_JosephusList( String infileName ) throws Exception
	{
		BufferedReader infile = new BufferedReader( new FileReader( infileName ) );	
		while ( infile.ready() )
		{	@SuppressWarnings("unchecked") 
			T data = (T) infile.readLine(); // CAST CUASES WARNING (WHICH WE CONVENIENTLY SUPPRESS)
			insertAtTail( data ); 
		}
		infile.close();
	}
	

	
	// ########################## Y O U   W R I T E / F I L L   I N   T H E S E   M E T H O D S ########################
	
	// TACK ON NEW NODE AT END OF LIST
	@SuppressWarnings("unchecked")
	public void insertAtTail(T data)
	{
		CDLL_Node<T> newNode = new CDLL_Node( data,null,null);
		if (head == null){
			newNode.next = newNode;
			newNode.prev = newNode;
			head = newNode;
			return;
		}

		CDLL_Node<T> old1st=head,oldlast=head.prev;
		newNode.next=old1st;
		newNode.prev=oldlast;
		old1st.prev=newNode;
		oldlast.next=newNode;
	}

	
	public int size()
	{
		count = 0;
		CDLL_Node<T> curr = head;
		do{
			count++;
			curr = curr.next;
		} while(curr != head);
		return count;
	}
	
	// RETURN REF TO THE FIRST NODE CONTAINING  KEY. ELSE RETURN NULL
	public CDLL_Node<T> search( T key )
	{
		CDLL_Node<T> curr = head;
		do{
			if( key.equals(curr.data)) return curr;
			curr = curr.next;
		} while(curr != head);
		return null;
	}
	
	// RETURNS CONATENATION OF CLOCKWISE TRAVERSAL
	@SuppressWarnings("unchecked")
	public String toString()
	{
		String toString = "";
		CDLL_Node<T> curr = head;
		do{
			toString += curr.data;
			if(curr.next != head) toString += "<=>";
			curr = curr.next;
		} while(curr != head);
		return toString;
	}
	
	void removeNode( CDLL_Node<T> deadNode )
	{
		if(deadNode.next == deadNode){
			head = null;
			return;
		}

		deadNode.prev.next = deadNode.next;
		deadNode.next.prev = deadNode.prev;
		deadNode = null;
	}
	
	public void executeRitual( T first2Bdeleted, int skipCount )
	{
		if (size() < 1 ) return;
		CDLL_Node<T> curr = search( first2Bdeleted );
		if ( curr==null ) return;
		
		// OK THERE ARE AT LEAST 2 NODES AND CURR IS SITING ON first2Bdeleted
		do
		{
			CDLL_Node<T> deadNode = curr;
			T deadName = deadNode.data;
			System.out.println("stopping on " + deadName + " to delete " + deadName);
			
			//先把curr往前/后移动
			if(skipCount > 0) curr = curr.next;
			if(skipCount < 0) curr = curr.prev;
			if(head == deadNode) head = curr;

			removeNode(deadNode);
			System.out.println("deleted. list now: " + toString());

			if(size() == 1) break;

			if(skipCount > 0){
				System.out.println("resuming at " + curr.data + ", skipping " + curr.data + " + " + (Math.abs(skipCount) - 1) + " nodes CLOCKWISE after");
			}

			if(skipCount < 0){
				System.out.println("resuming at " + curr.data + ", skipping " + curr.data + " + " + (Math.abs(skipCount) - 1) + " nodes COUNTERWISE after");
			}
			
			for (int i = 0; i < Math.abs(skipCount); i++) {
				if(skipCount > 0) curr = curr.next;
				if(skipCount < 0) curr = curr.prev;
			}
		}
		while (size() > 1 );

	}


	class CDLL_Node<T>
	{
		T data;
		CDLL_Node<T> prev, next;
		CDLL_Node(){
			this(null, null, null);
		}

		CDLL_Node(T data){
			this(data, null, null);
		}

		CDLL_Node(T data, CDLL_Node<T> prev, CDLL_Node<T> next){
			this.data = data;
			this.prev = prev;
			this.next = next;		
		}

		public String toString(){
			return "" + data;
		}
	}
	
} // END CDLL_LIST CLASS