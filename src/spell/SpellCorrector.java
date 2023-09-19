package spell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//My imports
import java.util.HashMap;
import java.util.Map;


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
            String[] words = line.toLowerCase().split("\\s+"); // Split by one or more whitespace characters
            for (String word : words) {
                if (!word.isEmpty()) {
//                    System.out.println(word);
//                    System.out.println(word.length());
                    // Store the word as lowercase
                    dictTrie.add(word);
                }
            }
        }
        read.close();
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        //Make the word lowercase
        inputWord = inputWord.toLowerCase();

        //Create HashMap of words
        HashMap<String, Integer> dictSuggest = new HashMap<>();

        //Check if the word is the same
        if (dictTrie.find(inputWord)!=null){
            return inputWord;
        }

        //----Deletion Algorithm Below----
        findDeletionDistance(inputWord, dictSuggest);
        //----Insertion Distance Below----
        findInsertionDistance(inputWord, dictSuggest);
        //----Alteration Distance Below----
        findAlterationDistance(inputWord, dictSuggest);


        // Iterate over the hashmap and get value the word
        if (!dictSuggest.isEmpty()) {
            String keyWithMaxValue = null;
            int maxValue = Integer.MIN_VALUE;

            // Iterate through the entries to find the key with the greatest value
            for (Map.Entry<String, Integer> entry : dictSuggest.entrySet()) {
                String key = entry.getKey();
                int value = entry.getValue();

                if (value > maxValue) {
                    maxValue = value;
                    keyWithMaxValue = key;
                }
            }
            return keyWithMaxValue;
        }

        return null;
    }

    //Deletion Dist Helper Function
    private void findDeletionDistance(String inputWord, HashMap<String, Integer> dict) {
        for (int i = 0; i < inputWord.length(); i++) {
            String possibleWord = inputWord.substring(0, i) + inputWord.substring(i + 1);

            // Check if the possible word exists in the dictionary
            if (dictTrie.find(possibleWord) != null) {
                dict.put(possibleWord, dictTrie.find(possibleWord).getValue());
            }
        }
    }
    //Insertion Distance Helper Function
    private void findInsertionDistance(String inputWord, HashMap<String, Integer> dict) {
        for (int i = 0; i <= inputWord.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                String possibleWord = inputWord.substring(0, i) + c + inputWord.substring(i);

                // Check if the possible word exists in the dictionary
                if (dictTrie.find(possibleWord) != null) {
                    dict.put(possibleWord, dictTrie.find(possibleWord).getValue());
                }
            }
        }
    }
    //Alteration Distance Function TODO: This needs to be fixed
    private void findAlterationDistance(String inputWord, HashMap<String, Integer> dict) {
        for (int i = 0; i < inputWord.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                if (c != inputWord.charAt(i)) {
                    String possibleWord = inputWord.substring(0, i) + c + inputWord.substring(i + 1);

                    // Check if the possible word exists in the dictionary
                    if (dictTrie.find(possibleWord) != null) {
                        dict.put(possibleWord, dictTrie.find(possibleWord).getValue());
                    }
                }
            }
        }
    }


}
