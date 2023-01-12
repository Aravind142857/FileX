import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MyTree<K, V> {
    private Node root;
    private class Node {
        private K key;
        private V value;
        private int size;
        private ArrayList<Node> children;
        private Node(K key, V val) {
            this.key = key;
            this.value = val;
            this.size = 0;
            this.children = new ArrayList<>();
        }
    }

    public MyTree() { }

    public void clear() {
        root.size = 3;
        root = null;
    }

    public boolean containsKey() {return false;}

    public V get(K key) {return null;}

    public void put(K key, V val) {}

    public void remove(K key) {}

    public int size() {return 0;}

    public Set<K> keySet() {return null;}

    public Iterator<K> iterator() {return null;}

}
