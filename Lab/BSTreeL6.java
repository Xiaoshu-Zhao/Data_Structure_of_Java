import java.io.*;
import java.util.*;

//////////////////////////////////////////////////////////////////////////////////////
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

///////////////////////////////////////////////////////////////////////////////////////
class BSTreeL6<T>
{
	private BSTNode<T> root;
	private int nodeCount;
	private boolean addAttemptWasDupe=false;

	// DEFAULT C'TOR
	public BSTreeL6()
	{
		nodeCount=0;
		root=null;
	}

	// INPUT FILE C'TOR
	@SuppressWarnings("unchecked")
	public BSTreeL6( String infileName ) throws Exception
	{
		nodeCount=0;
		root=null;
		BufferedReader infile = new BufferedReader( new FileReader( infileName ) );
		while ( infile.ready() )
			add( (T) infile.readLine() ); // THIS CAST RPODUCES THE WARNING
		infile.close();
	}

	// COPY C'TOR
	public BSTreeL6(  BSTreeL6<T> other )
	{
		nodeCount=0;
		root=null;
		// DO A PRE ORDER TRAVERSAL OF OTHER TREE WHERE VISITATION
		// OPERATION IS TO ADD EACH NODE FROM OTHER TREE INTO THIS TREE

		addNodesInPrOrder( other.root );
	}
	private void addNodesInPrOrder( BSTNode<T> otherBSTNode )
	{
		if ( otherBSTNode == null ) return;
		this.add( otherBSTNode.key );
		this.addNodesInPrOrder( otherBSTNode.left );
		this.addNodesInPrOrder( otherBSTNode.right );
	}


	// DUPES BOUNCE OFF & RETURN FALSE ELSE INCR COUNT & RETURN TRUE
	@SuppressWarnings("unchecked")
	public boolean add( T key )
	{	addAttemptWasDupe=false;
		root = addHelper( this.root, key );
		if (!addAttemptWasDupe) ++nodeCount;
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

	// PRE ORDER TRAVERSAL REQUIRES RECURSION
	public void printPreOrder()
	{	printPreOrder( this.root );
		System.out.println();
	}
	private void printPreOrder( BSTNode<T> root )
	{	if (root == null) return;
		System.out.print( root.key + " " );
		printPreOrder( root.left );
		printPreOrder( root.right );
	}

	// POST ORDER TRAVERSAL REQUIRES RECURSION
	public void printPostOrder()
	{	printPostOrder( this.root );
		System.out.println();
	}
	private void printPostOrder( BSTNode<T> root )
	{	if (root == null) return;
		printPostOrder( root.left );
		printPostOrder( root.right );
		System.out.print( root.key + " " );
	}

	public void printLevelOrder()
	{	if (this.root == null) return;
		Queue<T> q = new Queue<T>();
		q.enqueue( this.root ); // this. just for emphasis/clarity
		while ( !q.empty() )
		{	BSTNode<T> n = q.dequeue();
			System.out.print( n.key + " " );
			if ( n.left  != null ) q.enqueue( n.left );
			if ( n.right != null ) q.enqueue( n.right );
		}
		System.out.println();
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

	// # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
	// 				DO NOT MODIFY ANYTHING ABOVE THIS LINE.  YOU FILL IN ALL THE CODE BELOW

	// SIMILAR TO COPY CONSTRUCTOR BUT PRODUCES BALANCED COPY OF OTHER BST
	public BSTreeL6<T> makeBalancedCopyOf( )
	{
		// define an ArrayList<T>
		ArrayList<T> keys = new ArrayList<T>();
		// traverse this tree IN-ORDER adding A B C D E F G in that order to ArrayList
		// you'll need a recursive in order helper. The visitation operation will be
		// to add the key of each root to the ArrayList
		makeBalancedHelper( keys , this.root, 0 );


		// define a new BSTreeL6<T>
		BSTreeL6<T> balancedBST = new BSTreeL6<T>();
		addKeysInBalancedOrder(keys, 0, keys.size()-1, balancedBST );

		// now call a recursive method (you write it below) that looks a lot like a binary
		// search that  visits the elememnts of the ArrayList likea bserach would and
		// the visitation operation is to add that ArrayList element to the BST in an
		// ordering that will produce a balanced BST

		return balancedBST;   // return that balancedBST;
	}

	void makeBalancedHelper( ArrayList<T> keys, BSTNode<T> root, int index)
	{
		if (root == null) return;
		makeBalancedHelper(keys, root.left, index + 1);
		keys.add(index, root.key);
		makeBalancedHelper(keys, root.right, index + 1);

	}

	// suggested signature for method that imitates binary search thru keys array
	// and inserts keys into the new BST instead of doing comparisons etc
	void addKeysInBalancedOrder ( ArrayList<T> keys, int lo, int hi, BSTreeL6<T> balancedBST )  // V L R
	{
		if (lo > hi) return;
		int mid = (lo + hi)/2;
		balancedBST.add( keys.get(mid) );
		addKeysInBalancedOrder(keys, lo, mid-1, balancedBST);
		addKeysInBalancedOrder(keys, mid+1, hi, balancedBST); 
	}

	public boolean equals( BSTreeL6<T> other )
	{
		BSTNode<T> thisRoot = this. root, otherRoot = other.root;
		return equals ( thisRoot, otherRoot );
	}
	// TRUE ONLY IF BOTH TREES IDENTICAL IN EVERY WAY INCLUDING SHAPE
	private boolean equals( BSTNode<T> thisRoot, BSTNode<T> otherRoot )
	{
		if (thisRoot==null && otherRoot==null) return true;
		if (thisRoot == otherRoot) return true;
		if (thisRoot==null && otherRoot != null) return false;
		if (thisRoot != null && otherRoot == null) return false;
		if (thisRoot.key != otherRoot.key) return false;
		return equalsHelper( thisRoot, otherRoot );
	}

	boolean equalsHelper(BSTNode<T> thisRoot, BSTNode<T> otherRoot)
	{
		if (thisRoot==null && otherRoot==null) return true;
		if (thisRoot==null && otherRoot != null) return false;
		if (thisRoot != null && otherRoot == null) return false;
		if (thisRoot.key != otherRoot.key) return false;
		equalsHelper( thisRoot.left, otherRoot.left );
		equalsHelper( thisRoot.right, otherRoot.right );
		return true;
	}
} // END BSTreeL6 CLASS


