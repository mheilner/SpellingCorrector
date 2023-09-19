package spell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SpellCorrector implements ISpellCorrector {

    //Instance Variables
    private Trie dictTrie;

    //Constructor
    public SpellCorrector(){
        dictTrie = new Trie();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        BufferedReader read = new BufferedReader(new FileReader((dictionaryFileName)));
        String line;
        while ((line = read.readLine()) != null){
            //Store all the words as lowercase
            dictTrie.add(line.toLowerCase());
        }
        read.close();

    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        //Make the word lowercase
        inputWord = inputWord.toLowerCase();

        //Check if the word is the same
        if (dictTrie.find(inputWord)!=null){
            return inputWord;
        }

        return null;
    }
}
