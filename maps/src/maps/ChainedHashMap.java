package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {

    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 2;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 10;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 5;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    double lambda;
    int currentCapacity;
    int currentItem = 0;

    /**
     * Constructs a new ChainedHashMap with default resizing load factor threshold,
     * default initial chain count, and default initial chain capacity.
     */
    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    /**
     * Constructs a new ChainedHashMap with the given parameters.
     *
     * @param resizingLoadFactorThreshold the load factor threshold for resizing. When the load factor
     *                                    exceeds this value, the hash table resizes. Must be > 0.
     * @param initialChainCount the initial number of chains for your hash table. Must be > 0.
     * @param chainInitialCapacity the initial capacity of each ArrayMap chain created by the map.
     *                             Must be > 0.
     */
    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        this.chains = this.createArrayOfChains(initialChainCount);
        this.currentCapacity = chainInitialCapacity; // for each arraymap
        this.lambda = resizingLoadFactorThreshold;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    @Override
    public V get(Object key) {
        int hashcode;
        hashcode = key == null ? 0 : ((key.hashCode() % this.chains.length) + this.chains.length) % this.chains.length;
        Map<K, V> m = this.chains[hashcode];
        if (m == null) {
            return null;
        }
        return m.get(key);
    }

    @Override
    public V put(K key, V value) {
        int hashcode;
        hashcode = key == null ? 0 : ((key.hashCode() % this.chains.length) + this.chains.length) % this.chains.length;
        // some trick to prevent mod the negative number.
        Map<K, V> m = this.chains[hashcode];

        if (m == null) {
            // corner case: the first time to insert in this space.
            this.chains[hashcode] = createChain(this.currentCapacity);
            m = this.chains[hashcode];
        }
        V res = m.put(key, value);
        this.currentItem++;
        if (this.currentItem / this.chains.length > this.lambda) {
            // corner case: resize the ChainedMap length
            int newCapacity = this.chains.length * 2;
            // create a twice as long as original map
            AbstractIterableMap<K, V>[] newMap = this.createArrayOfChains(newCapacity);
            for (int i = 0; i < this.chains.length; i++) {
                // go through all items in original map and insert them to new map
                if (this.chains[i] != null) {
                    for (Entry<K, V> a : this.chains[i]) {
                        if (a != null) {
                            K k = a.getKey();
                            V v = a.getValue();
                            int newHash = ((k.hashCode() % newCapacity) + newCapacity) % newCapacity;
                            if (newMap[newHash] == null) {
                                newMap[newHash] = createChain(this.currentCapacity);
                            }
                            newMap[newHash].put(k, v);
                        }

                    }
                }

            }
            this.chains = newMap;

        }

        return res;
    }

    @Override
    public V remove(Object key) {
        currentItem--;
        int hashcode;
        hashcode = key == null ? 0 : ((key.hashCode() % this.chains.length) + this.chains.length) % this.chains.length;
        if (this.chains[hashcode] == null) {
            return null;
        }
        V result = this.chains[hashcode].remove(key);
        if (this.chains[hashcode].size() == 0) {
            // when there is no item in current arrayMap, we set it to null
            // so we won't affect the iterator function.
            this.chains[hashcode] = null;
        }
        return result;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.chains.length; i++) {
            this.chains[i] = null;
        }
    }

    @Override
    public boolean containsKey(Object key) {
        int hashcode;
        hashcode = key == null ? 0 : ((key.hashCode() % this.chains.length) + this.chains.length) % this.chains.length;
        if (this.chains[hashcode] == null) {
            return false;
        }
        return this.chains[hashcode].containsKey(key);
    }

    @Override
    public int size() {
        int res = 0;
        for (int i = 0; i < this.chains.length; i++) {
            if (this.chains[i] != null) {
                res++;
            }
        }
        return res;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private AbstractIterableMap<K, V>[] chains;
        private Iterator<Map.Entry<K, V>> itr;
        // You may add more fields and constructor parameters
        private int currentPos = -1;
        private int currentChains = 0;

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
            while (currentChains < this.chains.length - 1 && this.chains[currentChains] == null) {
                currentChains++;
            }
            // here check if we are at the last index and its value is null
            itr = this.chains[currentChains] == null ? null : this.chains[currentChains].iterator();
        }

        @Override
        public boolean hasNext() {
            if (itr == null) {
                return false;
            }

            if (!itr.hasNext()) {
                // update the iterator when we go through the current ArrayMap
                currentChains++;
                while (currentChains < this.chains.length && this.chains[currentChains] == null) {
                    currentChains++;
                }
                // prevent we go out of index
                if (currentChains >= this.chains.length) {
                    return false;
                }
                itr = this.chains[currentChains].iterator();

            }
            return itr.hasNext();
        }

        @Override
        public Map.Entry<K, V> next() {
            if (itr == null) {
                throw new NoSuchElementException();
            }
            if (!itr.hasNext()) {
                if (currentChains >= this.chains.length - 1) {
                    throw new NoSuchElementException();
                }
                itr = this.chains[++currentChains].iterator();
            }
            return itr.next();
        }
    }
}
