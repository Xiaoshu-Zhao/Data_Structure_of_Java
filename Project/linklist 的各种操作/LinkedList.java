import java.io.*;
import java.util.*;

public class LinkedList<T>
{
	private Node<T> head;  // pointer to the front (first) element of the list

	public LinkedList()
	{
		head = null; // compiler does this anyway. just for emphasis
	}

	// LOAD LINKED LIST FORM INCOMING FILE
	@SuppressWarnings("unchecked")	
	public LinkedList( String fileName, boolean orderedFlag )
	{
		head=null;
		try
		{
			BufferedReader infile = new BufferedReader( new FileReader( fileName ) );
			while ( infile.ready() )
			{
				if (orderedFlag)
					insertInOrder( (T)infile.readLine() );  // WILL INSERT EACH ELEM INTO IT'S SORTED POSITION
				else
					insertAtTail( (T)infile.readLine() );  // TACK EVERY NEWELEM ONTO END OF LIST. ORIGINAL ORDER PRESERVED
			}
			infile.close();
		}
		catch( Exception e )
		{
			System.out.println( "FATAL ERROR CAUGHT IN C'TOR: " + e );
			System.exit(0);
		}
	}

	//-------------------------------------------------------------

	// inserts new elem at front of list - pushing old elements back one place
	public void insertAtFront(T data)
	{
		head = new Node<T>(data,head);
	}

	// we use toString as our print


	public String toString()
	{
		String toString = "";

		for (Node curr = head; curr != null; curr = curr.getNext())
		{
			toString += curr.getData();		// WE ASSUME OUR T TYPE HAS toString() DEFINED
			if (curr.getNext() != null)
				toString += " ";
		}

		return toString;
	}

	// ########################## Y O U   W R I T E    T H E S E    M E T H O D S ########################

	
	public int size() // OF COURSE MORE EFFICIENT TO MAINTAIN COUNTER. BUT YOU WRITE LOOP!
	{
		int count = 0;
		for (Node<T> curr = head; curr != null; curr = curr.getNext()){
			count++;
		}
		return count;
	}


	public boolean empty()
	{
		if(size() == 0) return true;
		return false;
	}

	
	public boolean contains( T key )
	{
		return !(search(key) == null);
	}

	
	public Node<T> search( T key )
	{
		for (Node<T> curr = head; curr != null; curr = curr.getNext()){
			if( key.equals(curr.getData())) return curr;
		}
		return null;
	}

	
	public void insertAtTail(T data) // TACK A NEW NODE (CABOOSE) ONTO THE END OF THE LIST
	{
		if(head == null){
			insertAtFront(data);
			return;
		}

		Node<T> curr = head;
		while(curr.getNext() != null){
			curr = curr.getNext();
		}
		curr.setNext(new Node<T>(data, null));
	}

	@SuppressWarnings("unchecked")
	public void insertInOrder(T  data) // PLACE NODE IN LIST AT ITS SORTED ORDER POSTIOPN
	{
		Comparable cdata = (Comparable) data;
		if(head == null || cdata.compareTo( head.getData()) < 0){//输入值小于第一个值
			insertAtFront(data);
			return;
		}

		Node<T> curr = head;
		while(curr.getNext() != null && cdata.compareTo( curr.getNext().getData()) > 0){//找到前面一个
			curr = curr.getNext();
		}
		curr.setNext(new Node<T> (data, curr.getNext()));


	}
	
	
	public boolean remove(T key) // FIND/REMOVE 1st OCCURANCE OF A NODE CONTAINING KEY
	{
		if( head == null) return false;

		if(search(key) == null) return false;

		if( key.equals(head.getData())){
			removeAtFront();
			return true;
		}

		Node<T> curr = head;
		while( !key.equals(curr.getNext().getData())){//找到要删除的前一个
			curr = curr.getNext();
		}

		if(curr.getNext() == null) removeAtTail();
		curr.setNext( curr.getNext().getNext());
		return true;
	}

	
	public boolean removeAtTail()	// RETURNS TRUE IF THERE WAS NODE TO REMOVE
	{
		if(head == null) return false;

		if(head.getNext() == null){
			head = null;
			return true;
		}

		Node<T> curr = head;
		while(curr.getNext().getNext() != null){
			curr = curr.getNext();
		}
		curr.setNext( null );
		return true;
	}

	
	public boolean removeAtFront() // RETURNS TRUE IF THERE WAS NODE TO REMOVE
	{
		if(head == null) return false; // CHANGE TO YOUR CODE
		head = head.getNext();
		return true;
	}


	public LinkedList<T> union( LinkedList<T> other )
	{
		LinkedList<T> unionResult = new LinkedList<T>();

		for (Node<T> curr = this.head; curr != null; curr = curr.getNext()){
			unionResult.insertInOrder(curr.getData());
		}

		for(Node<T> curr = other.head; curr != null; curr = curr.getNext()){
			if(!unionResult.contains(curr.getData())) unionResult.insertInOrder(curr.getData());
		}

		return unionResult;
	}
	
	
	public LinkedList<T> inter( LinkedList<T> other )
	{
		LinkedList<T> interectResult = new LinkedList<T>();
		for (Node<T> curr = other.head; curr != null; curr = curr.getNext()) {
            if(this.contains(curr.getData())) interectResult.insertInOrder(curr.getData());            
        }

        return interectResult;
	}


	public LinkedList<T> diff( LinkedList<T> other )
	{
		LinkedList<T> diffResult = new LinkedList<T>();
		for (Node<T> curr = this.head; curr != null; curr = curr.getNext()) {
            if(!other.contains(curr.getData())) diffResult.insertInOrder(curr.getData());            
        }
        return diffResult;
	}

	
	public LinkedList<T> xor( LinkedList<T> other )
	{
		return this.union(other).diff(this.inter(other));
	}

} //END LINKEDLIST CLASS

