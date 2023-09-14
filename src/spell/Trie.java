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
        if(curr.getValue()==0){
            curr.incrementValue();
            wordCount++;
        }
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
        if(this == obj){return true;}

        if(obj == null || obj.getClass() != getClass()){return false;} //Got an error here doing root.getClass instead of just getClass. Remember that
        Trie trie = (Trie) obj;

        return checkEqual(root, trie.root);
    }
    //Create helper function to check if they are equal
    private boolean checkEqual(INode n1, INode n2){
        if(n1 == null && n2 == null){return true;}

        if(n1 == null || n2 == null){return false;}

        if(n1.getValue() != n2.getValue()){return false;}

        for(int i = 0; i < n1.getChildren().length; i++){
           if(!checkEqual(n1.getChildren()[i], n2.getChildren()[i]))
               return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
