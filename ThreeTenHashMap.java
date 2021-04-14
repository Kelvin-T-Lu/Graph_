import java.util.Map;
import java.util.Set;

import java.util.Collection; //for returning in the values() function only

//If you uncomment the import to ArrayList below, the grader will manually
//look to make sure you are not using it anywhere else... if you use it
//somewhere else you will get 0pts on the entire project (not a joke).

//uncomment the line below ONLY if you are implementing values().
//import java.util.ArrayList; //for returning in the values() function only
/**
 * A Hash Map object for the graph that uses open addressing and an array based implementation.
 * @param <K> The data type of key in Map object. 
 * @param <V> The data type of value in Map object. 
 */
class ThreeTenHashMap<K,V> implements Map<K,V> {
	//********************************************************************************
	//   DO NOT EDIT ANYTHING IN THIS SECTION (except to add the JavaDocs)
	//********************************************************************************
	
	//you must use this storage for the hash table
	//and you may not alter this variable's name, type, etc.
	/**
	 * A storage with Node objects. 
	 * @param K The key value of the Node.
	 * @param V The value of the Node. 
	 */
	private Node<K,V>[] storage;
	
	//you must use to track the current number of elements
	//and you may not alter this variable's name, type, etc.
	/**
	 * The number of keys inside the storage. 
	 */
	private int numElements = 0;

	/**
	 * The original size of the number of slots in storage.
	 */
	private int originalSize;
	
	/**
	 * A hash map with an array implemented storage. 
	 * @param size The size of the array. 
	 */
	//Generic Array. 
	@SuppressWarnings("unchecked")
	public ThreeTenHashMap(int size) {
		//Create a hash table where the size of the storage is
		//the provided size (number of "slots" in the table)
		//You may assume size is >= 1
		
		//Remember... if you want an array of ClassWithGeneric<V>, the following format ___SHOULD NOT___ be used:
		//         ClassWithGeneric<V>[] items = (ClassWithGeneric<V>[]) new Object[10];
		//instead, use this format:
		//         ClassWithGeneric<V>[] items = (ClassWithGeneric<V>[]) new ClassWithGeneric[10];
		
		originalSize = size; 
		storage = (Node<K,V>[])new Node[originalSize]; 
	}
	
	/**
	 * Clearing a storage. 
	 */
	//Generic Array.
	@SuppressWarnings("unchecked")
	public void clear() {
		//the table should return to the original size it had
		//when constructed
		//O(1)
		storage = (Node<K,V>[]) new Node[originalSize]; 
	}
	
	/**
	 * Checks if the number of elements in storage is empty.
	 * @return Boolean value of an empty storage. 
	 */
	public boolean isEmpty() {
		//O(1)
		return numElements==0;
	}
	/**
	 * Returning the number of slots inside a storage. 
	 * @return The int of slots inside the storage. 
	 */
	public int getSlots() {
		//return how many "slots" are in the table
		//O(1)
		return storage.length;
	}
	/**
	 * The number of Node elements currently inside a table.
	 * @return The int size of a table. 
	 */
	public int size() {
		//return the number of elements in the table
		//O(1)
		return numElements;
	}
	/**
	 * Gets a V value of a table from a certain key.
	 * @param key The key that's being accessed. 
	 * @return The V value from a Key value
	 */
	public V get(Object key) {
		//Given a key, return the value from the table.

		//If the value is not in the table, return null.
		//Worst case: O(n), Average case: O(1)
		int index = Math.abs(key.hashCode()%storage.length); 
		//System.out.printf("%d\n", index); 
		if(storage[index] == null) {
			return null;
		}
		Node<K,V> iter = storage[index]; 
		
		while(!iter.entry.key.equals(key)&& iter.next !=null) {
			iter = iter.next; 
		}
		
		if(iter.entry.key.equals(key)) {
			return iter.entry.value; 
		}
		
		
		return null;  
	}
	/**
	 * {@inheritDoc}
	 */
	public Set<K> keySet() {
		//O(n+m) or better, where n is the size and m is the
		//number of slots
		
		//Hint: you aren't allowed to import
		//anything, but a ThreeTenHashSet is a Set
		ThreeTenHashSet<K> temp = new ThreeTenHashSet<K>(); 
		for(int i = 0; i<storage.length; i++) {
			if(storage[i]== null) {
				continue; 
			}
			temp.add(storage[i].entry.key); 
			Node<K,V> iter = storage[i]; 
			while(iter.next !=null) {
				iter = iter.next; 
				temp.add(iter.entry.key);
			}
		}
		
		return temp;
	}
	/**
	 * Removing a specific key from the storage. 
	 * @param key The key of the element being removed. 
	 * @return The value of the element being removed. 
	 */
	public V remove(Object key) {
		//Remove the given key (and associated value)
		//from the table. Return the value removed.		
		//If the value is not in the table, return null.
		
		//Hint: Remember there are no tombstones for
		//separate chaining! Don't leave empty nodes!
		int index = Math.abs(key.hashCode()) % storage.length; 
		V temp;
		//Worst case: O(n), Average case: O(1)
		
		//Searches the List at Storage index. 
		Node<K,V> iter = storage[index]; 
		if(iter.entry.key.equals(key)) {
			temp = iter.entry.value; 
			storage[index] = null;
			numElements--; 
			return temp; 
		}
		while(iter.next != null) {
			if(iter.next.entry.key.equals(key)) {
				temp = iter.next.entry.value; 
				iter.next = iter.next.next; 
				numElements--; 
				return temp; 
			}
		}
		return null;
	}
	
	/**
	 * Places the key and value into the storage. 
	 * @param key The key of the adding element. 
	 * @param value The value of the adding element. 
	 * @return The previous value that element replaces if applicable. 
	 */
	private V putNoExpand(K key, V value) {
		//Place value v at the location of key k.
		//Use separate chaining if that location is in use.
		
		//If the key already exists in the table
		//replace the current value with v.
		
		//If the key does not exist in the table, add
		//the new node to the _end_ of the linked list.

		//Worst case: O(n) where n is the number
		//of items in the list, NOT O(m) where m
		//is the number of slots, and NOT O(n+m)
		
		TableEntry<K,V> add = new TableEntry<K,V>(key,value); 
		int index = Math.abs(key.hashCode() % storage.length); 
		//System.out.printf("Index: %d\n",  index); 
		
		//Full storage
		if(storage[index]!=null) {//If a linked list exists
			//Compares first Key
			if(storage[index].entry.key.equals(key)) {
				V temp = storage[index].entry.value; 
				storage[index].entry.value = value; 
				return temp; 
			}
			//Compares second to last keys
			Node<K,V> iter = storage[index]; 
			while(iter.next!=null) {
				iter= iter.next; 
				if(iter.entry.key.equals(key)) {
					V temp = iter.entry.value; 
					iter.entry.value = value; 
					return temp; 
				}
			}
			//Add Key to end of list
			numElements++; 
			iter.next = new Node<K,V>(add); 
		}
		else{//No key found and adding into storage; 
			storage[index] = new Node<K,V>(add); 
			numElements++; 
		}
		return null; 
	}
	
	//--------------------------------------------------------
	// testing code goes here... edit this as much as you want!
	//--------------------------------------------------------
	/**
	 * Testing main method. 
	 * @param args Command line arguments of main method. 
	 */
	public static void main(String[] args) {
		//main method for testing, edit as much as you want
		ThreeTenHashMap<String,String> st1 = new ThreeTenHashMap<>(10);
		ThreeTenHashMap<String,Integer> st2 = new ThreeTenHashMap<>(5);
		
		st1.put("a","apple");
		st1.put("b","banana");
		st1.put("banana","b");
		st1.put("b","butter");

		if(st1.toString().equals("a:apple\nbanana:b\nb:butter") && st1.toStringDebug().equals("[0]: null\n[1]: null\n[2]: null\n[3]: null\n[4]: null\n[5]: null\n[6]: null\n[7]: [a:apple]->[banana:b]->null\n[8]: [b:butter]->null\n[9]: null")) {
			System.out.println("Yay 1");
		}
		if((st1.getSlots() == 10) && st1.size() == 3 && st1.get("a").equals("apple")) {
			System.out.println("Yay 2");
		}
		
		st2.rehash(1);
		st2.put("a",1);
		st2.put("b",2);
		//System.out.printf("%s ", st2.toString());
		if(st2.toString().equals("b:2\na:1") && st2.toStringDebug().equals("[0]: [b:2]->null\n[1]: [a:1]->null")
			&& st2.put("e",3) == null && st2.put("y",4) == null &&
			st2.toString().equals("a:1\ne:3\ny:4\nb:2") && st2.toStringDebug().equals("[0]: null\n[1]: [a:1]->[e:3]->[y:4]->null\n[2]: [b:2]->null\n[3]: null")) {
			System.out.println("Yay 3");
		}
		
		if(st2.remove("e").equals(3) && st2.rehash(8) == true &&
			st2.size() == 3 && st2.getSlots() == 8 &&
			st2.toString().equals("a:1\ny:4\nb:2") && st2.toStringDebug().equals("[0]: null\n[1]: [a:1]->[y:4]->null\n[2]: [b:2]->null\n[3]: null\n[4]: null\n[5]: null\n[6]: null\n[7]: null")) {
			System.out.println("Yay 4");
		}
		ThreeTenHashMap<String,String> st3 = new ThreeTenHashMap<>(2);
		st3.put("a","a");
		st3.remove("a");
		
		if(st3.toString().equals("") && st3.toStringDebug().equals("[0]: null\n[1]: null")) {
			st3.put("a","a");
			if(st3.toString().equals("a:a") && st3.toStringDebug().equals("[0]: null\n[1]: [a:a]->null")) {
				System.out.println("Yay 5");
			}
		}
		
		//This is NOT all the testing you need... several methods are not
		//being tested here! Some method return types aren't being tested
		//either. You need to write your own tests!
		
		//Also, try this and see if it works:
		//ThreeTenHashMap<Integer,Integer> st4 = new ThreeTenHashMap<>(10);
		//st4.put(Integer.MIN_VALUE, Integer.MIN_VALUE);
	}
	
	//********************************************************************************
	//   YOU MAY, BUT DON'T NEED TO EDIT THINGS IN THIS SECTION
	// These are some methods we didn't write for you, but you could write,
	// if you need/want them for building your graph. We will not test
	// (or grade) these methods.
	//********************************************************************************
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}
	/**
	 * {@inheritDoc}
	 */
	public void	putAll(Map<? extends K,? extends V> m) {
		throw new UnsupportedOperationException();
	}
	/**
	 * {@inheritDoc}
	 */
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Set<Map.Entry<K,V>> entrySet() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean containsKey(Object key) {
		throw new UnsupportedOperationException();
	}
	
	//********************************************************************************
	//   DO NOT EDIT ANYTHING BELOW THIS LINE (except to add the JavaDocs)
	// We will check these things to make sure they still work, so don't break them.
	//********************************************************************************
	
	//THIS CLASS IS PROVIDED, DO NOT CHANGE IT
	/**
	 * A Node data type to represent a linked list with map pairings. 
	 * @param <K> The data type of the key.
	 * @param <V> The data type of the value. 
	 */
	public static class Node<K,V> {
		/**
			The data of the Node. 
		 */
		public TableEntry<K,V> entry;
		/**
		 * The linked node of current node. 
		 */
		public Node<K,V> next;
		
		/**
		 * A Node with a map pairing data type. 
		 * @param entry The entry data type. 
		 */
		public Node(TableEntry<K,V> entry) {
			this.entry = entry;
		}
		
		/**
		 * A map pairing data type with a linked node. 
		 * @param entry The map pairing data. 
		 * @param next The linked node. 
		 */
		public Node(TableEntry<K,V> entry, Node<K,V> next) {
			this(entry);
			this.next = next;
		}
		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			return "[" + entry.toString() + "]->";
		}
	}
	
	//THIS CLASS IS PROVIDED, DO NOT CHANGE IT
	/**
	 * A specific map pairing, with a key and value. 
	 * @param <K> The data type of the key. 
	 * @param <V> The data type of the value. 
	 */
	public static class TableEntry<K,V> {
		/**
		 * Key of the object. 
		 */
		public K key;
		/**
		 * Value of the object. 
		 */
		public V value;
		
		/**
		 * A pairing of a key and value. 
		 * @param key The key of the pairing. 
		 * @param value The value of the pairing. 
		 */
		public TableEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			return key.toString()+":"+value.toString();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	//Generic Array Edited.
	@SuppressWarnings("unchecked")
	public TableEntry<K,V>[] toArray(){
		//THIS METHOD IS PROVIDED, DO NOT CHANGE IT
		
		TableEntry<K,V>[] collection = (TableEntry<K,V>[]) new TableEntry[this.numElements];
		int index = 0;
		for(int i = 0; i < storage.length; i++) {
			if(storage[i] != null) {
				Node<K,V> curr = storage[i];
				while(curr != null) {
					collection[index] = curr.entry;
					index++;
					curr = curr.next;
				}
			}
		}
		return collection;		
	}
	/**
	* {@inheritDoc}
	*/
	public String toString() {
		//THIS METHOD IS PROVIDED, DO NOT CHANGE IT
		
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < storage.length; i++) {
			Node<K,V> curr = storage[i];
			if(curr == null) continue;
			
			while(curr != null) {
				s.append(curr.entry.toString());
				s.append("\n");
				curr = curr.next;
			}
		}
		return s.toString().trim();
	}
	/**
	 * A string representation of the Hash map. 
	 * Primary intent is for debugging purposes.
	 * @return The string representation used for debugging. 
	 */
	public String toStringDebug() {
		//THIS METHOD IS PROVIDED, DO NOT CHANGE IT
		
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < storage.length; i++) {
			Node<K,V> curr = storage[i];
			
			s.append("[" + i + "]: ");
			while(curr != null) {
				s.append(curr.toString());
				curr = curr.next;
			}
			s.append("null\n");
		}
		return s.toString().trim();
	}
	
	/**
	 * Resizing of the storage array.
	 * @param size The new size of the storage array. 
	 * @return The new array with updated storage capacity. 
	 */
	//Generic Array
	@SuppressWarnings("unchecked")
	public boolean rehash(int size) {
		//THIS METHOD IS PROVIDED, DO NOT CHANGE IT
		
		if(size < 1) return false;
		
		Node<K,V>[] oldTable = storage;
		storage = (Node<K,V>[]) new Node[size];
		numElements = 0;
		
		for(Node<K,V> node : oldTable) {
			while(node != null) {
				putNoExpand(node.entry.key, node.entry.value);
				node = node.next;
			}
		}
		
		return true;
	}
	/**
	 * {@inheritDoc}
	 */
	public V put(K key, V value) {
		//THIS METHOD IS PROVIDED, DO NOT CHANGE IT
		
		V ret = putNoExpand(key, value);
		while((numElements/(double)storage.length) >= 2) {
			rehash(storage.length*2);
		}
		return ret;
	}
}