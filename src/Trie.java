import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Trie {

    @NotNull
    private List<LinkedList<Node>> trie;

    public Trie() {
        trie = new ArrayList<LinkedList<Node>>();
    }

    public void insert(char c, int index) {
        for (LinkedList<Node> linkedList : trie) {
            Node n = new Node(c, index, linkedList.size() + 1);
            linkedList.getLast().addChild(n);
            linkedList.add(n);
        }

        LinkedList<Node> listToAdd = new LinkedList<>();
        Node nodeToAdd = new Node(c, index, 1);
        listToAdd.add(nodeToAdd);
        trie.add(listToAdd);
    }

    @NotNull
    public List<Node> getNodes(char c) {
        List<Node> existingNodes = new ArrayList<>();

        for (LinkedList<Node> linkedList : trie) {
            Node firstNode = linkedList.getFirst();
            if (firstNode.getCharacter() == c) {
                existingNodes.add(firstNode);
            }
        }

        return existingNodes;
    }

    public void printTrie() {

        for (LinkedList<Node> linkedList : trie) {

            Node node = linkedList.getFirst();
            while (node != null) {
                System.out.println(node);
                node = node.getChild();
            }

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }
}
