package spell;

public class Trie implements ITrie{
    //Instance Variables
    private Node root;
    private int wordCount;
    private int nodeCount;

    //Adding Constructor
    public Trie(){
        root = new Node();
        wordCount = 0;
        nodeCount = 1;
    }

    @Override
    public void add(String word) {
        word = word.toLowerCase();

        INode curr = root;

        for(char l: word.toCharArray()){
            int ind = l - 'a'; // Getting the index of the current letter l
            if (curr.getChildren()[ind] == null){
                curr.getChildren()[ind] = new Node();
                nodeCount++;
            }
            curr = curr.getChildren()[ind];
        }
        curr.incrementValue();
        wordCount++;

    }

    @Override
    public INode find(String word) {
        word = word.toLowerCase();
        INode curr = root;
        for(char l: word.toCharArray()){
            int ind = l - 'a';
            if(curr.getChildren()[ind]==null){return null;}
            curr = curr.getChildren()[ind];
        }

        return curr;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

//    Will have to add in the other interface objects in the interface (reason it doesnt pull is that it is from object class

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        INode curr = root;
        if(obj == null || obj.getClass() != root.getClass()){return false;}

        //Create helper function to check if they are equal
        private boolean checker(Node n1, Node n2){
            return n1 == null && n2 == null;
        }

        return super.equals(obj);
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
