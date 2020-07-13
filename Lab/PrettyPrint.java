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

class Queue<T>
{	LinkedList<BSTNode<T>> queue;
	Queue() { queue =  new LinkedList<BSTNode<T>>(); }
	boolean empty() { return queue.size() == 0; }
	void enqueue( BSTNode<T>  node ) { queue.addLast( node ); }
	BSTNode<T>  dequeue() { return queue.removeFirst(); }
}

///////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////
class PrettyPrint<T>
{
	private BSTNode<T> root;
	private int nodeCount;
	private boolean addAttemptWasDupe=false;

	// DEFAULT C'TOR
	public PrettyPrint()
	{
		nodeCount=0;
		root=null;
	}

	// INPUT FILE C'TOR
	@SuppressWarnings("unchecked")
	public PrettyPrint( String infileName ) throws Exception
	{
		nodeCount=0;
		root=null;
		BufferedReader infile = new BufferedReader( new FileReader( infileName ) );
		while ( infile.ready() )
			add( (T) infile.readLine() ); // THIS CAST RPODUCES THE WARNING
		infile.close();
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


	public void prettyPrint()
	{	if (this.root == null) return;
		ArrayList<String> levelOrderArray = new ArrayList<String>(countNodes());
		int index = 0;
		Queue<T> q = new Queue<T>();
		q.enqueue( this.root ); // this. just for emphasis/clarity
		while ( !q.empty() )
		{	BSTNode<T> n = q.dequeue();
			levelOrderArray.add(index, n.key + "");
			index++;
			if ( n.left  != null ) q.enqueue( n.left );
			if ( n.right != null ) q.enqueue( n.right );
		}
		ArrayList<String> sortArray = levelOrderArray;

		//define the levelcount
		int levelCount[] = calcLevelCounts();

		//input the element into a 2D array
		index = 0;
		String grid[][] = new String[levelCount.length][levelOrderArray.size()];

		//create the grid to contain the tree
		for (int row = 0; row < levelCount.length ; row++) {
			for (int col = 0; col < levelCount[row]; col++ ) {
				grid[row][col] = levelOrderArray.get(index++);
			}

			for (int col = levelCount[row]; col < levelOrderArray.size(); col++ ) {
				grid[row][col] = " ";
			}
		}

		Collections.sort(sortArray);



		//change the position for each element
		for (int row = 0; row < levelCount.length ; row++ ) {
			for (int col = levelOrderArray.size() - 1; col >= 0; col--) {
				if (!grid[row][col].equals(" ")) {
					int belongPosition = searchKey(sortArray, grid[row][col]);
					String reserve = grid[row][col];
					grid[row][col] = " ";
					grid[row][belongPosition] = reserve;

				}
			}
		}


		//print the grid
		for (int row = 0; row < levelCount.length ; row++ ) {
			for (int col = 0; col < levelOrderArray.size(); col++) {
				System.out.print(grid[row][col] );
			}
			System.out.println();
		}
	}

	public int searchKey(ArrayList<String> sortedListOfKey, String key2find)
	{
		for (int i = 0; i < sortedListOfKey.size(); ++i) {

			if (sortedListOfKey.get(i).equals(key2find)) {
				return i;
			}
		}
			return -1;
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
	public PrettyPrint<T> makeBalancedCopyOf( )
	{
		ArrayList<T> keys = new ArrayList<T>();
		makeBalancedHelper( keys , this.root);
		// define a new BSTreeL6<T>
		PrettyPrint<T> balancedBST = new PrettyPrint<T>();
		addKeysInBalancedOrder(keys, 0, keys.size()-1, balancedBST );
		return balancedBST;   // return that balancedBST;
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

	void makeBalancedHelper( ArrayList<T> keys, BSTNode<T> root)
	{
		if (root == null) return;
		makeBalancedHelper(keys, root.left);
		keys.add(root.key);
		makeBalancedHelper(keys, root.right);

	}
	void addKeysInBalancedOrder ( ArrayList<T> keys, int lo, int hi, PrettyPrint<T> balancedBST )  // V L R
	{
		if (lo > hi) return;
		int mid = lo + (hi-lo)/2;
		balancedBST.add( keys.get(mid) );
		addKeysInBalancedOrder(keys, lo, mid-1, balancedBST);
		addKeysInBalancedOrder(keys, mid+1, hi, balancedBST); 
	}
} // END BSTreeL6 CLASS