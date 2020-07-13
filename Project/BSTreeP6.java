import java.io.*;
import java.util.*;

///////////////////////////////////////////////////////////////////////////////
class BSTNode<T>
{	T key;
	BSTNode<T> left,right;
	BSTNode( T key, BSTNode<T> left, BSTNode<T> right )
	{	this.key = key;
		this.left = left;
		this.right = right;
	}
}
///////////////////////////////////////////////////////////////////////////////////////
class Queue<T>
{	LinkedList<BSTNode<T>> queue;
	Queue() { queue =  new LinkedList<BSTNode<T>>(); }
	boolean empty() { return queue.size() == 0; }
	void enqueue( BSTNode<T>  node ) { queue.addLast( node ); }
	BSTNode<T>  dequeue() { return queue.removeFirst(); }
	// THROWS NO SUCH ELEMENT EXCEPTION IF Q EMPTY
}
////////////////////////////////////////////////////////////////////////////////
class BSTreeP6<T>
{
	private BSTNode<T> root;
	private int nodeCount;
	private boolean addAttemptWasDupe=false;

	public BSTreeP6()
	{
		nodeCount = 0;
		root=null;
	}

	@SuppressWarnings("unchecked")
	public BSTreeP6( String infileName ) throws Exception
	{
		nodeCount = 0;
		root=null;
		Scanner infile = new Scanner( new File( infileName ) );
		while ( infile.hasNext() )
			add( (T) infile.next() ); // THIS CAST RPODUCES THE WARNING
		infile.close();
	}

	// DUPES BOUNCE OFF & RETURN FALSE ELSE INCR COUNT & RETURN TRUE
	@SuppressWarnings("unchecked")
	public boolean add( T key )
	{	addAttemptWasDupe=false;
		root = addHelper( this.root, key );
		return !addAttemptWasDupe;
	}
	@SuppressWarnings("unchecked")
	private BSTNode<T> addHelper( BSTNode<T> root, T key )
	{
		if (root == null) return new BSTNode<T>(key,null,null);
		int comp = ((Comparable)key).compareTo( root.key );
		if ( comp == 0 )
			{ addAttemptWasDupe=true; return root; }
		else if (comp < 0)
			root.left = addHelper( root.left, key );
		else
			root.right = addHelper( root.right, key );

		return root;
  } // END addHelper

	public int size()
	{
		return nodeCount; // LOCAL VAR KEEPING COUNT
	}

	public int countNodes() // DYNAMIC COUNT ON THE FLY TRAVERSES TREE
	{
		return countNodes( this.root );
	}
	private int countNodes( BSTNode<T> root )
	{
		if (root==null) return 0;
		return 1 + countNodes( root.left ) + countNodes( root.right );
	}

	// INORDER TRAVERSAL REQUIRES RECURSION
	public void printInOrder()
	{
		printInOrder( this.root );
		System.out.println();
	}
	private void printInOrder( BSTNode<T> root )
	{
		if (root == null) return;
		printInOrder( root.left );
		System.out.print( root.key + " " );
		printInOrder( root.right );
	}

	public void printLevelOrder()
	{
		if (this.root == null) return;
		Queue<T> q = new Queue<T>();
		q.enqueue( this.root ); // this. just for emphasis/clarity
		while ( !q.empty() )
		{	BSTNode<T> n = q.dequeue();
			System.out.print( n.key + " " );
			if ( n.left  != null ) q.enqueue( n.left );
			if ( n.right != null ) q.enqueue( n.right );
		}
	}

  public int countLevels()
  {
    return countLevels( root );
  }
  private int countLevels( BSTNode root)
  {
    if (root==null) return 0;
    return 1 + Math.max( countLevels(root.left), countLevels(root.right) );
  }

  public int[] calcLevelCounts()
  {
    int levelCounts[] = new int[countLevels()];
    calcLevelCounts( root, levelCounts, 0 );
    return levelCounts;
  }
  private void calcLevelCounts( BSTNode root, int levelCounts[], int level )
  {
    if (root==null)return;
    ++levelCounts[level];
    calcLevelCounts( root.left, levelCounts, level+1 );
    calcLevelCounts( root.right, levelCounts, level+1 );
  }

	//////////////////////////////////////////////////////////////////////////////////////
	// # # # #   WRITE THE REMOVE METHOD AND ALL HELPERS / SUPPORTING METHODS   # # # # #

	// return true only if it finds/removes the node


    public boolean remove( T key2remove )
    {
		if (this.root == null) return false;
    	BSTNode<T> deadNode = findDeadNode( key2remove ); // ret null if Key not in tree
    	if (deadNode == null) return false;

    	int deadNodeOffspring = findOffspring( deadNode );
    	if ( deadNodeOffspring == 0 ) removeLeaf( key2remove );
    	if ( deadNodeOffspring == -1 || deadNodeOffspring == 1 ) removeOnechild( key2remove );
    	if ( deadNodeOffspring == 2 ) removeTwochild( key2remove );

    	return true;
    }

	public boolean removeLeaf( T key2remove )
	{
		BSTNode<T> deadNode = findDeadNode( key2remove );
		BSTNode<T> parent = ref2Parent( deadNode.key );
		if (parent == null){
			root = null;
			return true;
		}
		if (parent.left != null && parent.left.key.equals(key2remove)) parent.left = null;
		if (parent.right != null && parent.right.key.equals(key2remove)) parent.right = null;
		return true;
	}

	public boolean removeOnechild( T key2remove )
	{
		BSTNode<T> deadNode = findDeadNode( key2remove );

		BSTNode<T> parent = ref2Parent( deadNode.key );

		int deadNodeSign = findDeadNodePositon( parent, deadNode );
		int deadNodeOffspring = findOffspring( deadNode );

		if ( deadNodeSign == 0 && deadNodeOffspring < 0) root = deadNode.left;
		if ( deadNodeSign == 0 && deadNodeOffspring > 0) root = deadNode.right;
		if ( deadNodeSign < 0 && deadNodeOffspring < 0) parent.left = deadNode.left;
		if ( deadNodeSign < 0 && deadNodeOffspring > 0) parent.left = deadNode.right;
		if ( deadNodeSign > 0 && deadNodeOffspring < 0) parent.right = deadNode.left;
		if ( deadNodeSign > 0 && deadNodeOffspring > 0) parent.right = deadNode.right;// duide

		return true;
	}

	public int findOffspring( BSTNode<T> curr )
	{
		if ( curr.left != null && curr.right != null) {
			return 2;
		}
		else if (curr.left == null && curr.right == null) {
			return 0;
		}
		else if (curr.left != null && curr.right == null) {
			return -1;
		}
		else return 1;
	}

	public int findDeadNodePositon( BSTNode<T> parent, BSTNode<T> deadNode )
	{
		if (parent == null) {
			return 0;
		}
		int deadNodePosition;
		if (parent.left.key.equals(deadNode.key)) {
			deadNodePosition = -1;
		}
		else {
			deadNodePosition = +1;
		}

		return deadNodePosition;
	}

	public boolean removeTwochild( T key2remove )
	{
		BSTNode<T> deadNode = findDeadNode( key2remove );
		BSTNode<T> beginner = deadNode.left;
		while ( beginner.right != null)
			beginner = beginner.right;
		T reserveBox = beginner.key;

		if ( findOffspring( beginner ) == 0) {
			removeLeaf( beginner.key );
		}

	    else {
	    	removeOnechild( beginner.key );
	    }

		deadNode.key = reserveBox;
		return true;
	}

	@SuppressWarnings("unchecked")
	BSTNode<T> ref2Parent( T key2rem )
	{
		BSTNode<T> curr = this.root;
		BSTNode<T> parent = null;

		while ( curr != null && !curr.key.equals(key2rem) )
		{	if ( ((Comparable)key2rem).compareTo(curr.key) > 0 )
			{
				parent = curr;
			    curr = curr.right;
			}
			else
			{
				parent = curr;
				curr = curr.left;
			}
		}
		if (curr == null ) return null; // never found it
		return parent;
	} // getParentOfKey2Rem

	@SuppressWarnings("unchecked")
	public BSTNode<T> findDeadNode( T key2remove)
	{
		BSTNode<T> curr = this.root;
		while( curr != null  && !curr.key.equals(key2remove) )
		{
			int comp = ((Comparable)key2remove).compareTo( curr.key );
		    if (comp > 0) curr = curr.right;
		    if (comp < 0) curr = curr.left;
		}

		return curr;
	}

} // END BSTREEP6 CLASS
