import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TwoChoiceHashTable<T> {
	static int initialSize = 16;
	LinkedList<T>[] table;
	int size, maxSize;

	TwoChoiceHashTable() {
		table = new LinkedList[initialSize];
		for (int i = 0; i < initialSize; i++)
			table[i] = new LinkedList<>();
		size = 0;
		maxSize = initialSize;
	}

	TwoChoiceHashTable(int n) {
		table = new LinkedList[n];
		for (int i = 0; i < n; i++)
			table[i] = new LinkedList<>();
		size = 0;
		maxSize = n;
	}

	/**
	 * To find the position of the element in the hash table
	 * 
	 * @param x:
	 *            T - element to be to found
	 * @return: index of the element in the table, returns -1 if not found
	 */
	int find(T x) {
		// two possible locations(lists) - using hashcode1 and hashcode2
		if (table[hashcode1(x)].contains(x))
			return hashcode1(x);
		else if (table[hashcode2(x)].contains(x))
			return hashcode2(x);
		else
			return -1;
	}

	/**
	 * Add the element to the hashtable
	 * 
	 * @param x:
	 *            T - element to be added
	 */
	public void add(T x) {
		LinkedList<T> l1 = table[hashcode1(x)];
		LinkedList<T> l2 = table[hashcode2(x)];
		if (l1.contains(x) || l2.contains(x)) // ignore if the element is
												// already present
			return;
		else {
			if (l1.size() <= l2.size()) // add to the smaller list
				l1.add(x);
			else
				l2.add(x);
			if (++size > maxSize) // resize the table and rehash the contents
				resize();
		}
	}

	/**
	 * To remove the element from the hashtable
	 * 
	 * @param x:
	 *            T - element to be removed
	 * @return: T - element that is removed. If not found returns null
	 */
	public T remove(T x) {
		if (!contains(x))
			return null;
		int loc = find(x);// find the position in the table
		Iterator itr = table[loc].iterator();
		T h = null;
		while (itr.hasNext()) {// loop through the iterator to find the element
			h = (T) itr.next();
			if (h.equals(x)) {
				itr.remove();
				size--;
				break;
			}
		}
		return (T) h;
	}

	/**
	 * To check if the element is present in the hashtable
	 * 
	 * @param x:
	 *            T - element to be looked for in the table
	 * @return: true - if element is present else false
	 */
	public boolean contains(T x) {
		if (table[hashcode1(x)].contains(x) || table[hashcode2(x)].contains(x))
			return true;
		else
			return false;
	}

	/**
	 * To resize and rehash the contents of the hashtable
	 */
	void resize() {
		maxSize = maxSize * 2;// double the size of the table
		List<T>[] oldTable = table;// copy the current hashtable

		table = new LinkedList[maxSize];// new table with new size
		for (int i = 0; i < maxSize; i++)
			table[i] = new LinkedList<>();

		size = 0;
		// rehash the existing elements from the old table to new table
		for (List<T> list : oldTable)
			for (T item : list)
				add(item);
	}

	/**
	 * Hashcode 1
	 * 
	 * @param x:
	 *            T - element to be hashed
	 * @return: hashcode of the element
	 */
	public int hashcode1(T x) {
		int h = x.hashCode();
		return h % maxSize;
	}

	/**
	 * Hashcode2
	 * 
	 * @param x:
	 *            T - element to be hashed
	 * @return: hashcode of the element
	 */
	public int hashcode2(T x) {
		int h = x.hashCode();
		h ^= (h >>> 20) ^ (h >>> 12);
		h = (h ^ (h >>> 7) ^ (h >>> 4));
		// System.out.println("Hashcode2 of "+x+": "+h%maxSize);
		return h % maxSize;
	}

	/**
	 * To print the hashtable
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < maxSize; i++) {
			Iterator<T> itr = (Iterator<T>) table[i].iterator();
			sb.append("table[" + i + "]: ");
			while (itr.hasNext()) {
				sb.append(itr.next() + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) throws FileNotFoundException {
		TwoChoiceHashTable<Integer> h = new TwoChoiceHashTable<>(150);

		Scanner in;
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else
			in = new Scanner(System.in);

		Timer t = new Timer();
		t.start();
		while (in.hasNextInt())
			h.add(in.nextInt());
		t.end();
		System.out.println("Time taken:" + t);
		in.close();
//		System.out.println(h);
//		System.out.println(h.remove(314));
//		System.out.println(h);
	}
}
