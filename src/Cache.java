import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Cache {

    @NotNull
    private List<LinkedList<Node>> cache;

    public Cache() {
        cache = new ArrayList<LinkedList<Node>>();
    }

    public void insert(char c, int index) {
        for (LinkedList<Node> linkedList : cache) {
            Node n = new Node(c, index, linkedList.size() + 1);
            linkedList.getLast().addChild(n);
            linkedList.add(n);
        }

        LinkedList<Node> listToAdd = new LinkedList<>();
        Node nodeToAdd = new Node(c, index, 1);
        listToAdd.add(nodeToAdd);
        cache.add(listToAdd);
    }

    @NotNull
    public List<Node> getNodes(char c) {
        List<Node> existingNodes = new ArrayList<>();

        for (LinkedList<Node> linkedList : cache) {
            Node firstNode = linkedList.getFirst();
            if (firstNode.getCharacter() == c) {
                existingNodes.add(firstNode);
            }
        }

        return existingNodes;
    }
}
