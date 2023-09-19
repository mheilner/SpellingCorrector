package spell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//My imports
import java.io.PrintStream;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        HashMap<String, Integer> editDistOneDict = new HashMap<>();
        HashMap<String, Integer> editDistTwoDict = new HashMap<>();

        //Check if the word is the same
        if (dictTrie.find(inputWord)!=null){
            return inputWord;
        }

        // ---- Deletion Algorithm Below ----
        findDeletionDistance(inputWord, editDistOneDict, false, editDistTwoDict);
        // ---- Insertion Distance Below ----
        findInsertionDistance(inputWord, editDistOneDict, false, editDistTwoDict);
        // ---- Alteration Distance Below ----
        findAlterationDistance(inputWord, editDistOneDict, false, editDistTwoDict);
        // ---- Transposition Distance Below ----
        findTranspositionDistance(inputWord, editDistOneDict, false, editDistTwoDict);



        String bestWord = getBestWord(editDistOneDict);
        if(bestWord != null){return bestWord;}

        bestWord = getBestWord(editDistTwoDict);
        if(bestWord != null){return bestWord;}

        return null;
    }

    //Function that chooses the best word
    private String getBestWord(HashMap<String, Integer> dict) {
        if (!dict.isEmpty()) {
            String keyWithMaxValue = null;
            int maxValue = Integer.MIN_VALUE;
            String firstAlphabeticalWord = null;

            for (Map.Entry<String, Integer> entry : dict.entrySet()) {
                String key = entry.getKey();
                int value = entry.getValue();

                // Check if this word has a higher frequency
                if (value > maxValue) {
                    maxValue = value;
                    keyWithMaxValue = key;
                    firstAlphabeticalWord = key;
                }
                // If the frequency is the same, choose the first word alphabetically
                else if (value == maxValue && key.compareTo(firstAlphabeticalWord) < 0) {
                    keyWithMaxValue = key;
                    firstAlphabeticalWord = key;
                }
            }

            return keyWithMaxValue;
        }
        return null;
    }



    //Deletion Dist Helper Function
    private void findDeletionDistance(String inputWord, HashMap<String, Integer> editOneDict, boolean secondCheck, HashMap<String, Integer> editTwoDict) {
        //If we are checking first edit distance then we need to hold the possible other vals
        List<String> secondDistance = new ArrayList<String>();
        // Boolean that holds if edit dist of 1 worked
        boolean hasEditOne = false;
        for (int i = 0; i < inputWord.length(); i++) {
            String possibleWord = inputWord.substring(0, i) + inputWord.substring(i + 1);
            // Check if the possible word exists in the dictionary
            if (dictTrie.find(possibleWord) != null) {
                editOneDict.put(possibleWord, dictTrie.find(possibleWord).getValue());
                hasEditOne = true;
            } else if (!secondCheck && !hasEditOne) {
                secondDistance.add(possibleWord);
            }
        }
        if(!hasEditOne && !secondCheck){
            for(String word: secondDistance){
                findInsertionDistance(word, editTwoDict, true, editTwoDict);
                findDeletionDistance(word, editTwoDict, true, editTwoDict);
                findAlterationDistance(word, editTwoDict, true, editTwoDict);
                findTranspositionDistance(word, editTwoDict, true, editTwoDict);
            }
        }
    }
    //Insertion Distance Helper Function
    private void findInsertionDistance(String inputWord, HashMap<String, Integer> editOneDict, boolean secondCheck, HashMap<String, Integer> editTwoDict) {
        //If we are checking first edit distance then we need to hold the possible other vals
        List<String> secondDistance = new ArrayList<String>();
        // Boolean that holds if edit dist of 1 worked
        boolean hasEditOne = false;
        for (int i = 0; i <= inputWord.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                String possibleWord = inputWord.substring(0, i) + c + inputWord.substring(i);
                // Check if the possible word exists in the dictionary
                if (dictTrie.find(possibleWord) != null) {
                    editOneDict.put(possibleWord, dictTrie.find(possibleWord).getValue());
                    //Since there is a word with edit dist of one, make it true
                    hasEditOne = true;
                //This will add the words to another list that will be used for edit dist 2
                } else if (!secondCheck && !hasEditOne) {
                    secondDistance.add(possibleWord);
                }
            }
        }
        if(!hasEditOne && !secondCheck){
            for(String word: secondDistance){
                findInsertionDistance(word, editTwoDict, true, editTwoDict);
                findDeletionDistance(word, editTwoDict, true, editTwoDict);
                findAlterationDistance(word, editTwoDict, true, editTwoDict);
                findTranspositionDistance(word, editTwoDict, true, editTwoDict);
            }
        }

    }
    //Alteration Distance Function
    private void findAlterationDistance(String inputWord, HashMap<String, Integer> editOneDict, boolean secondCheck, HashMap<String, Integer> editTwoDict) {
        //If we are checking first edit distance then we need to hold the possible other vals
        List<String> secondDistance = new ArrayList<String>();
        // Boolean that holds if edit dist of 1 worked
        boolean hasEditOne = false;
        for (int i = 0; i < inputWord.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                if (c != inputWord.charAt(i)) {
                    String possibleWord = inputWord.substring(0, i) + c + inputWord.substring(i + 1);
                    // Check if the possible word exists in the dictionary
                    if (dictTrie.find(possibleWord) != null) {
                        editOneDict.put(possibleWord, dictTrie.find(possibleWord).getValue());
                        hasEditOne = true;
                    } else if (!secondCheck && !hasEditOne) {
                        secondDistance.add(possibleWord);
                    }
                }
            }
        }
        if(!hasEditOne && !secondCheck){
            for(String word: secondDistance){
                findInsertionDistance(word, editTwoDict, true, editTwoDict);
                findDeletionDistance(word, editTwoDict, true, editTwoDict);
                findAlterationDistance(word, editTwoDict, true, editTwoDict);
                findTranspositionDistance(word, editTwoDict, true, editTwoDict);
            }
        }
    }
    //Transposition Distance Function
    private void findTranspositionDistance(String inputWord, HashMap<String, Integer> editOneDict,  boolean secondCheck, HashMap<String, Integer> editTwoDict) {
        //If we are checking first edit distance then we need to hold the possible other vals
        List<String> secondDistance = new ArrayList<String>();
        // Boolean that holds if edit dist of 1 worked
        boolean hasEditOne = false;
        for (int i = 0; i < inputWord.length() - 1; i++) {
            // Swap adjacent characters
            char[] charArray = inputWord.toCharArray();
            char temp = charArray[i];
            charArray[i] = charArray[i + 1];
            charArray[i + 1] = temp;
            String possibleWord = new String(charArray);

            // Check if the possible word exists in the dictionary
            if (dictTrie.find(possibleWord) != null) {
                editOneDict.put(possibleWord, dictTrie.find(possibleWord).getValue());
                hasEditOne = true;
            } else if (!secondCheck && !hasEditOne) {
                secondDistance.add(possibleWord);
            }
        }
        if(!hasEditOne && !secondCheck){
            for(String word: secondDistance){
                findInsertionDistance(word, editTwoDict, true, editTwoDict);
                findDeletionDistance(word, editTwoDict, true, editTwoDict);
                findAlterationDistance(word, editTwoDict, true, editTwoDict);
                findTranspositionDistance(word, editTwoDict, true, editTwoDict);
            }
        }
    }



}
