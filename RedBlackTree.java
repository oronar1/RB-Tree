/**
 * The purpose of this assignment is to write and implement pseudo online algorithm for finding K smallest 
 * numbers in online receiving data while k<n. Numbers in array are generated with standard random function 
 * and are in range between 1 to 1023.
 * Algorithm should in one scan of the array find out k smallest nembers in that array.     
 * Algorithm is implemented with Red Black Binary Search Tree with augmented data-size 
 * of the tree to support ability to control numbers of elements in the tree
 * Element will be inserted into tree iff size of the tree is smaller then k,
 * otherwise iff candidate element is smaller than the maximum element in the tree.
 * In that case, maximum element will e deleted and candidate element will be inserted 
 * in to the tree.  
 * Output is the Red-Black Tree elements in sorted order. 
 *      
 *  @author Michael Bochkovsky
 *  @ID 317035038
 *  @Task 16 
 *  @version 1.2 
 */
public class RedBlackTree
{
 
    /**
     * Declaration of colors of nodes in RB-Tree
     */    
    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;     // root of the RB-Tree

    /**
     *  RB-Tree node type structure
     */
    private class Node{
        private int key;           // key
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of the parent
        private int size;          // size of the tree
        
        /**
         * RB-Tree node constructor  
         * @param key Value of the node
         * @param color Color of the parent node
         * @param size Size value of the node
         */
        public Node(int key, boolean color, int size){
            this.key = key;
            this.color = color;
            this.size = size;
        }
    }

    /**
     * Initializes an empty RB-Tree.
     */
    public RedBlackTree(){
    }

    
    /**
     * Checks if the node is red
     * @param n Node of the RB-Tree
     * @return false if node is null
     */
    private boolean isRed(Node n){
        if (n == null) 
            return false;
        return n.color == RED;
    }

    /**
     * Checks the size value of node in subtree rooted at n
     * @param n Node of the R-Tree
     * @return 0 if node is null
     */
    private int size(Node n){
        if (n == null) 
            return 0;
        return n.size;
    } 


    /**
     * Returns the size of the RB-Tree
     * @return the size of the RB-Tree
     */
    public int size(){
        return size(root);
    }

   /**
     * Checks if RB-Tree is empty
     * @return True if RB-Tree is empty, otherwise returns False
     */
    public boolean isEmpty(){
        return root == null;
    }

    /**
     * Inserts the key into RB-Tree.If there is same key already exists, 
     * add it to the tree.
     * Insert performed a call to a private function RB_Insert
     * @param key the key value to be inserted
     */
    public void RB_Insert(int key){
        root = RB_Insert(root, key);
        root.color = BLACK;
    }

    /**
     * Insert the key into the subtree rooted at n.
     * If there is same key already exists, add it to the tree.
     * @param n Node of the RB-Tree
     * @param key Key of the node to be inserted
     * @return n Node of the RB-Tree
     */
    private Node RB_Insert(Node n, int key){ 
        if (n == null) 
            return new Node(key, RED, 1);
        
        if  (key < n.key)                           //insert on left subtree
            n.left  = RB_Insert(n.left,  key); 
        else if (key >= n.key)                      //insert on right subtree
            n.right = RB_Insert(n.right, key); 
        
        // fixup of collors of linked nodes
        if (isRed(n.right) && !isRed(n.left))       //if right node is red and left is black      
            n = rotateLeft(n);                      //rotate left
        
        if (isRed(n.left)  &&  isRed(n.left.left))  //if left node is red and its left node is red
            n = rotateRight(n);                     //rotate right
        
        if (isRed(n.left)  &&  isRed(n.right))      //if left node is red and right is red
            changeColors(n);                        //change collors such than n must have opposite color of its two children
        
        n.size = size(n.left) + size(n.right) + 1;  //fix the size value of the sub-tree 
        
        return n;
    }

    /**
     * Delete the smallest key node from RB-Tree.
     */
    public void RB_DeleteMin(){
        if (!isEmpty()){ 
            if (!isRed(root.left) && !isRed(root.right))    // if both children of root are black, set root to red
                root.color = RED;

            root = RB_DeleteMin(root);                      //call for private function  
            
            if (!isEmpty()) 
                root.color = BLACK;
            }
    }

    /**
     * Delete the key with the smallest key value rooted at n
     * @param n node of the RB-Tree
     * @return restored RB-Tree property or null in case no left nodes
     */
    private Node RB_DeleteMin(Node n){ 
        if (n.left == null)                         //if no left nodes left return null
            return null;

        if (!isRed(n.left) && !isRed(n.left.left))  //if left node is black and left node of the left node is black
            n = moveRedLeft(n);                     //make left node or one of its chldren red

        n.left = RB_DeleteMin(n.left);              //recursive call 
        return rbInvariantRestore(n);               //restore RB-Tree property
    }


    /**
     * Delete the biggest key node from RB-Tree.
     * 
     */
    public void RB_DeleteMax(){
        if (!isEmpty()){
        
            if (!isRed(root.left) && !isRed(root.right))    // if both children of root are black, set root to red
                root.color = RED;

            root = RB_DeleteMax(root);                      //call for private function
            if (!isEmpty()) 
                root.color = BLACK;
        }
    }

    /**
     * Delete the key with the biggest key value rooted at n
     * @param n RB-Tree node
     * @return restored RB-Tree property or null in case no left nodes
     */
    private Node RB_DeleteMax(Node n){ 
        if (isRed(n.left))                              //if left node is red
            n = rotateRight(n);                         //perform right rotate

        if (n.right == null)                            //if no right nodes left
            return null;

        if (!isRed(n.right) && !isRed(n.right.left))    //if right node is black and left node of the right node is black
            n = moveRedRight(n);                        //make right node or one of its chldren red

        n.right = RB_DeleteMax(n.right);                //recursive call

        return rbInvariantRestore(n);                   //restore RB-Tree property
    }

    /**
     * Removes the specified key from RB-Tree    
     *
     * @param  key the key value to be deleted
     */
    public void RB_Delete(int key){ 
        if (!isRed(root.left) && !isRed(root.right))    // if both children of root are black, set root to red
            root.color = RED;

        root = RB_Delete(root, key);                    //call for private function
        if (!isEmpty())                                 //if not empty set root to black
            root.color = BLACK;
    }
    /**
     * Delete the node with the given key rooted at n
     * @param n - RB-Tree
     * @param key - Key value to be deleted
     * @return restored RB-Tree or null
     */
    private Node RB_Delete(Node n, int key){ 
        if (key < n.key){                               //if key less then node n key
            if (!isRed(n.left) && !isRed(n.left.left))  //if left node is black and left node of the left node is black
                n = moveRedLeft(n);                     //make left node or one of its chldren red
            n.left = RB_Delete(n.left, key);            //look for key on left subtree of n node
        }
        else{
            if (isRed(n.left))                          //if node is red
                n = rotateRight(n);                     //perform right rotate
            if ((key== n.key) && (n.right == null))     //node is with key value and no right node  
                return null;                            //return null
            if (!isRed(n.right) && !isRed(n.right.left))//right node is black and left node of right node is black
                n = moveRedRight(n);                    //make left node or one of its chldren red
            if (key==n.key) {                           //if desired node found
                Node y = min(n.right);                  //find minimum key node on right side
                n.key = y.key;                          //swap the value of desired node with found minimum
                n.right = RB_DeleteMin(n.right);        //delete minimum from right sutree
            }
            else 
                n.right = RB_Delete(n.right, key);         //look for desired node on right subtree
        }
        return rbInvariantRestore(n);                     //restore RB-Tree property on subrooted tree
    }

    /**
     * Rotate to the right function
     * @param n node of RB-Tree
     * @return node of RB-Tree
     */
    private Node rotateRight(Node n) {
        Node y = n.left;                                //set y as left node of n
        n.left = y.right;                               //set left node of n as right node of y
        y.right = n;                                    //set n as right node of y
        y.color = y.right.color;                        //correct the colors
        y.right.color = RED;                            //set right node of y as red
        y.size = n.size;                                //correct the size value of y node
        n.size = size(n.left) + size(n.right) + 1;      //change value of size of n node
        return y;
    }
    
    /**
     * Rotate to the left function
     * @param n node of RB-Tree
     * @return node of RB-Tree
     */
    private Node rotateLeft(Node n) {
        Node y = n.right;                               //set y as right node of n
        n.right = y.left;                               //set right node of n as left node of y
        y.left = n;                                     //set n as left node of y
        y.color = y.left.color;                         //correct the colors
        y.left.color = RED;                             //set left node of y as red
        y.size = n.size;                                //correct the size value of y node
        n.size = size(n.left) + size(n.right) + 1;      //change value of size of n node
        return y;
    }

   /**
    * Change colors of a node, and its left and right child
    * @param n parent node those colors have to be changed
    */
    private void changeColors(Node n) {
        if(n!=null){                                    // n must be of the opposite color of its two children
            n.color = !n.color;
            n.left.color = !n.left.color;
            n.right.color = !n.right.color;
        }
    }
    
    /**
     * When n is red and both left nodes, (n.left and n.left.left),
     * are black, make n.left or one of its children red.
     * @param n - node of RB-Tree
     * @return n node of RB-Tree
     */
    private Node moveRedLeft(Node n) {
        if(n!=null){
            changeColors(n);
            if (isRed(n.right.left)) {                      //if left node of right node of n is red 
                n.right = rotateRight(n.right);             //perform rotate to right on right node of n
                n = rotateLeft(n);                          //perform left rotate on n node itself
                changeColors(n);                            //change colors of n node and its two childrens
            }
        }
        return n;
        
    }
    
    /**
     * In case n is red and both h.right and h.right.left
     * are black, make n.right or one of its children red.
     * @param n Node of RB-Tree
     * @return Node of RB tree
     */
    private Node moveRedRight(Node n) {
        if (n!=null){                                       
            changeColors(n);                                    //change colors of n node and its two childrens
            if (isRed(n.left.left)){                            //if left node of the left node of n is red              
                n = rotateRight(n);                             //perform right rotate on n node itself
                changeColors(n);                                //change colors of n node and its two childrens
            }
        }
        return n;
    }
    
    /**
     * Restore red-black tree property
     * @param n RB-Tree node
     * @return n RB-Tree node
     */
    private Node rbInvariantRestore(Node n) {
        if(n!=null){
            if (isRed(n.right))                                 //if right node is red
                 n = rotateLeft(n);                             //perform rotate to the left
            if (isRed(n.left) && isRed(n.left.left))            //if left node is red and its left node is red
                 n = rotateRight(n);                            //perform rotate to the right
            if (isRed(n.left) && isRed(n.right))                //if left node is red and right node is red
                 changeColors(n);                               //change colors of the node and its two childrens

        n.size = size(n.left) + size(n.right) + 1;              //correct the size value
        }
        return n;
        
    }
   
    /**
     * Returns the smallest key in RB-Tree
     * @return the smallest key in RB-Tree
     */
    public int min() {
        //if (!isEmpty()) 
                return min(root).key;
    } 

    /**
     * Returns smallest key in subtree rooted at n, otherwise null if no such key
     * Complexity Theta(lg n)
     * @param n
     * @return node with minimum key value
     */
    private Node min(Node n) { 
        if (n.left == null)                                         //if no left leafes left
            return n; 
        else
            return min(n.left);                                     //keep looking for minimum on the left subtree
    } 

    /**
     * Returns the largest key in the RB-Tree
     * @return the largest key in the RB-Tree
     * @throws NoSuchElementException if the symbol table is empty
     */
    public int max() {
        //if (!isEmpty()) 
            return max(root).key;
    } 

    /**
     * Returns largest key in subtree rooted at x; null if no such key
     * Complexity Theta(lg n)
     * @param n
     * @return node with maximum key value
     */
    private Node max(Node n) { 
        if (n.right == null)                                        //if no right leafes left 
            return n; 
        else
            return max(n.right);                                    //keep looking for maximum on right subtree
    } 

    /**
     * Function to print k smallest elements in growing up sequence 
     * Complexity is Theta(lg K)
     */
    public void printKMin(){
        if(!isEmpty())
            this.InOrderPrint(root);
    }
    
     /**
     * Printing In-Order traversal of the RB-tree
     * in order to return elements in growing up sequence.
     * Function implemented recursively with time complexity of O(n)    
     * @param x Root of the RB-Tree 
     */
    private void InOrderPrint(Node x){
        if(x!=null){
            InOrderPrint(x.left);
            System.out.print(x.key+"  ");
            InOrderPrint(x.right);
        }
    }
    
    /**
     * Function to print RBtree of size K and appropriate parts of a test array.
     * Part of array chosen accordingly to values of 'a' and 'b' parameters.
     * For example, in order to print 3/4 of array, values are a=3, b=4.
     * @param a value of the 'a' parameter
     * @param b value of the 'b' parameter
     * @param Array array to be printed
     * @param x RB-Tree
     */
    private void toString(int a,int b,int[ ]Array, RedBlackTree x, int kVal){
        /**
           * Printing a*n/b part of input Array
           */    
          System.out.println("After " + (a*Array.length)/b +" inputs: ");
          System.out.println("The "+kVal+ " smallest numbers are: ");
          x.InOrderPrint(x.root);                                               //In-Order traversal print of the RB-Tree
          System.out.println(); 
    }  
    
    
    /**
     * Function that find and prints K smallest elements of the array in growing up sequence
     * Also, prints appropriate part of test array accordingly to values of a and b
     * (1/4*n,1/2*n,3/4*n,n)
     * @param x - pointer to root of the RB-tree
     * @param sizeVal - amount of K elements to find
     * @param Array - Input array that simulate streaming data
     */       
    public void kSmallest (RedBlackTree x, int sizeVal, int[]Array){
        kSmallestElements (x,sizeVal,Array);
    }
    
    /**
     * kSmallestElements function simulates online streaming algorithm to find K
     * smallest elements in streaming data.Data is simulated with arrays.
     * for test of Pseudo-online algorithm to find K minimum elements.
     * Function shows result at three test points depending on size of testing array.
     * Test points are : n/4, n/2, 3n/4
     * The number of K elements received from main function.
     * K value is: 10, 50 , 100.
     * @param x - pointer to root of the RB-tree
     * @param sizeVal - amount of K elements to find
     * @param Array - Input array that simulate streaming data
     */
    private void kSmallestElements (RedBlackTree x, int sizeVal, int[]Array){
      
      System.out.println();
      System.out.println("The value of K is: "+ sizeVal);        
      
      RedBlackTree pnt = x;                                                       //initiate pointer on RB-Tree
      
        for(int i=0;i<Array.length;i++){
            
            if(i==((1*Array.length)/4)){                                        //Printing n/4 of input array
                toString(1,4,Array,x,sizeVal);
             }//end if
             
            if(i==((1*Array.length)/2)){                                        //Printing n/2 of input array
                toString(1,2,Array,x,sizeVal);
              }//end if
              
            if(i==((3*Array.length)/4)){                                        //Printing 3n/4 of input array 
                toString(3,4,Array,x,sizeVal);
             }//end if
            
            
            if(pnt.size()==sizeVal){                                             //if tree is from requested size 
                int max=pnt.max();                                               //find the maximum in RB-tree after insertion                                                                         
                if(Array[i]<max){                                               //if potential inserted value is lower then maximum element
                      pnt.RB_DeleteMax();                                        //remove maximum from tree and insert smaller value
                      pnt.RB_Insert(Array[i]);
                }//end if               
            }//end if
            else if(pnt.size()<sizeVal){                                         //Inserting firsst K numbers into RB-TRee
                        pnt.RB_Insert(Array[i]);                                 //from array A/B/C of sizes 200/400/800.
            
            }
          
            
        }//end for
        
        /*
         * Printing whole input array
         */
        toString(1,1,Array,x,sizeVal);                  //print K smalles elements
        System.out.println("The Input Array is: ");     //print whole input array                         
        for(int j=0;j<Array.length;j++)
            System.out.print(""+Array[j]+"  ");
        System.out.println();
          
    }
    
    }