package spell;

public class Node implements INode{
    // INSTANCE VARIABLES
    private int count;
    private Node[] children;

    public Node(){
        this.count = 0;
        this.children = new Node[26];
    }

    @Override
    public int getValue() {
        return count;
    }

    @Override
    public void incrementValue() {
        count++;
    }

    @Override
    public INode[] getChildren() {
        return children;
    }
}
