package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 5;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;
    int capacity;
    private int currentItem = 0;
    // You may add extra fields or helper methods though!

    /**
     * Constructs a new ArrayMap with default initial capacity.
     */
    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructs a new ArrayMap with the given initial capacity (i.e., the initial
     * size of the internal array).
     *
     * @param initialCapacity the initial capacity of the ArrayMap. Must be > 0.
     */
    public ArrayMap(int initialCapacity) {
        this.entries = this.createArrayOfEntries(initialCapacity);
        this.capacity = initialCapacity;

    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    private boolean equalsWithNull(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }
    @Override
    public V get(Object key) {
        for (int i = 0; i < this.capacity; i++) {
            if (this.entries[i] != null && equalsWithNull(this.entries[i].getKey(), key)) {
                return this.entries[i].getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        currentItem++;
        if (currentItem > this.capacity) {
            // if you add too much in one arrayMap
            int newCapacity = this.capacity * 2;
            SimpleEntry<K, V>[] newArrayMap = this.createArrayOfEntries(newCapacity);
            for (int i = 0; i < this.capacity; i++) {
                newArrayMap[i] = this.entries[i];
            }
            this.entries = newArrayMap;
            this.capacity = newCapacity;
        }
        // first check if there exist the same key
        boolean isDuplicated = get(key) != null;
        V originalValue = get(key);
        if (isDuplicated) {
            // if yes, find that key and insert new SimpleEntry<K, V>
            for (int i = 0; i < this.capacity; i++) {
                if (this.entries[i] != null && equalsWithNull(this.entries[i].getKey(), key)) {
                    this.entries[i] = new SimpleEntry<>(key, value);
                }
            }
        } else {
            // if not, add it to the empty bracket
            for (int i = 0; i < this.capacity; i++) {
                if (this.entries[i] == null) {
                    this.entries[i] = new SimpleEntry<>(key, value);
                    break;
                }
            }
        }


        return originalValue; // return the original value of the key.
    }


    private int binarySearch(SimpleEntry<K, V>[] array) {
        // binary search to find the last non-null element
        // return the index of the last non-null element.
        int l = 0;
        int r = array.length - 1;

        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (array[mid] != null) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return r;
    }
    @Override
    public V remove(Object key) {
        currentItem--;
        V originalValue = get(key);
        if (originalValue == null) {
            return null;
        } else {
            SimpleEntry<K, V> temp = null; // store the last non-null SimpleEntry
            // for (int i = this.capacity - 1; i > -1; i--) {
            //     // find the last non-null simpleEntry
            //     if (this.entries[i] != null && equalsWithNull(this.entries[i].getKey(), key)) {
            //         // case 1 : the SimpleEntry we want to remove is the last non-null SimpleEntry
            //         this.entries[i] = null;
            //         return originalValue; // return here to avoid implement the below things.
            //     } else if (this.entries[i] != null) {
            //         // case 2 : the one we want to remove is not the last one non-null
            //         temp = this.entries[i];
            //         this.entries[i] = null;
            //         break;
            //     }
            // }

            // try to reduce the time -> binary search
            int lastNonnullIndex = binarySearch(this.entries);
            temp = this.entries[lastNonnullIndex];
            this.entries[lastNonnullIndex] = null;
            if (equalsWithNull(temp.getKey(), key)) {
                // case 1: the SimpleEntry we want to remove is the last non-null SimpleEntry
                return originalValue;
            }

            for (int i = 0; i < this.capacity; i++) {
                if (this.entries[i] != null && equalsWithNull(this.entries[i].getKey(), key)) {
                    this.entries[i] = temp;
                    break;
                }
            }
            return originalValue;
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.capacity; i++) {
            this.entries[i] = null;
        }
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0; i < this.capacity; i++) {
            if (this.entries[i] != null && equalsWithNull(this.entries[i].getKey(), key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        int res = 0;
        for (int i = 0; i < this.capacity; i++) {
            if (this.entries[i] != null) {
                res++;
            }
        }
        return res;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: You may or may not need to change this method, depending on whether you
        // add any parameters to the ArrayMapIterator constructor.
        return new ArrayMapIterator<>(this.entries);
    }


    // @Override
    // public String toString() {
    //     return super.toString();
    // }

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        // You may add more fields and constructor parameters
        private int currentPos = -1;

        public ArrayMapIterator(SimpleEntry<K, V>[] entries) {
            this.entries = entries;
        }

        @Override
        public boolean hasNext() {
            if (currentPos == this.entries.length - 1) {
                return false;
            }
            return currentPos < this.entries.length && this.entries[currentPos + 1] != null;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return this.entries[++currentPos];
        }
    }
}
