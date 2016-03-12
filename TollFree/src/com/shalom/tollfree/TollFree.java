package com.shalom.tollfree;

import com.shalom.tollfree.Exceptions.TollFreeGeneralException;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by sha on 3/5/16.
 */
public class TollFree implements ProcessDataFromFile{


    private static final Object lock = new Object();
    private static volatile TollFree _instance;

    private Map<Integer, List<Character>> mapTable;
    private final static char wordSeparator = '-';
    private Dictionary dictionary;
    private static final String regex = "[^02-9\\.]";

    /*
            DIGIT CHARACTERS
            2 A B C
            3 D E F
            4 G H I
            5 J K L
            6 M N O
            7 P Q R S
            8 T U V
            9 W X Y Z

            // TODO: unit test

     */

    private TollFree() {

    }

    public static TollFree getInstance() {

        if (_instance == null) {

            synchronized (lock) {

                if(_instance == null) {
                    _instance = new TollFree();
                    _instance.initTollFree();

                }

            }
        }

        return _instance;
    }

    private void initTollFree() {

        initNumToCharsTable();

    }

    public void loadDictionary(String fullPathFileName) throws TollFreeGeneralException, IOException {

        if (fullPathFileName == null || fullPathFileName.trim().isEmpty()){
            throw new TollFreeGeneralException("Unable to load dictionary file with empty fullPathFileName");
        }

        dictionary = new Dictionary();
        dictionary.loadDictionary(fullPathFileName);
    }

    private void initNumToCharsTable() {

        mapTable = new HashMap<Integer, List<Character>>();

        // add special charcter dot (.)
        mapTable.put(0, Arrays.asList(wordSeparator));
        // generate map starting with A, ascii value 65
        int asciiVal = 65;
        for (int i = 2 ; i <= 9; i ++ ) {
            int max = 3;
            mapTable.put(i,new ArrayList<Character>());
            for(int j = 0 ;j < max ; j++) {

                mapTable.get(i).add((char)asciiVal);

                if ( (i == 7 || i == 9 ) && j ==2) {
                    max = 4;
                }

                asciiVal++;
            }

        }

    }

    /*

     if no match can be made, a single digit can be left as is at that point.
     No two consecutive digits can remain unchanged and the program should skip over a number (producing no output) if a match cannot be made
        //TODO - add unit test
     */

    public Set<String> getAvailableWords(String tollFree) {

        Node root = dictionary.getDictionary();
        Set<StringBuffer> results = new HashSet<StringBuffer>();

        for (Node node : root.getNodes().values()) {
            Set<StringBuffer> result = getTollFreeByNumber(tollFree, 0, true, node);
            if(result != null) {
                results.addAll(result);
            }
        }


        Set<String> finalResults = new HashSet<>();
        if (results.size() != 0) {

            for (StringBuffer sb : results) {
                String str = sb.reverse().toString();
                finalResults.add(str);
            }

            return finalResults;
        }

        return null;
    }

    /*
        recursive function to find related character at the dictionary tree
     */
    public Set<StringBuffer> getTollFreeByNumber(String tollFree, int digitIndex, boolean enableSingleDigit, Node node) {

        List<Character> optionalChars = getOptionalLetters(tollFree.charAt(digitIndex));
        Set<StringBuffer> results = null;
        if(digitIndex == tollFree.length()-1) {
            // we got to the end of the word, check if the tree contains this word
            if (node.isEndOfword()) {

                if (optionalChars.contains(node.getCharacter())) {
                    return createResults(node.getCharacter(), false);
                } else if (enableSingleDigit){
                    return createResults(tollFree.charAt(digitIndex), true);
                }
            }
            // we got the the ned of the word, the word was not found in dictionary
            return null;
        }

        // if we got to node with no children then return
        if(node.getNodes() == null) {
            return null;
        }

        for (Character optionalChar : optionalChars) {

            if (node.getCharacter().equals(optionalChar) ||
                    !node.getCharacter().equals(optionalChar) && enableSingleDigit) {

                for (Node child : node.getNodes().values()) {
                    // call recursive to next node
                    Set<StringBuffer> result = getTollFreeByNumber(tollFree, digitIndex+1,
                            node.getCharacter().equals(optionalChar), // add option to enable one number
                            child);

                    if (result != null) { // check if results already found - for other children under this node.
                        if (results == null) {
                            results = result;
                        } else {
                            results.addAll(result);
                        }
                    }
                }
            }

        }

        if(results != null) {

            for (StringBuffer result : results) {
                if (optionalChars.contains(node.getCharacter())) {
                    result.append(node.getCharacter());
                } else { //  support enable Single Digit
                    Character c = tollFree.charAt(digitIndex);
                    if ( result.charAt(result.length()-1) != wordSeparator) {
                        result.append(wordSeparator);
                    }
                    result.append(c);
                    result.append(wordSeparator);
                }
            }
        }

        return results;

    }

    // Map number to optional letters
    // assumption integer can be digit or '.'
    private List<Character> getOptionalLetters(Character ch) {


        if (ch == '.') {
            return mapTable.get(0);
        }

        int digit = ch - 48;
        return mapTable.get(digit);

    }


    private Set<StringBuffer> createResults(Character ch, boolean addDash) {

        StringBuffer result = new StringBuffer();
        result.append(ch);
        if(addDash){
            result.append(wordSeparator);
        }
        Set<StringBuffer> results = new HashSet<> (1);
        results.add(result);
        return results;
    }

    public void processTollFree(String phoneNumber) {
        Set<String> words = getAvailableWords(phoneNumber);
        if (words == null) {
            System.out.println("no words found to phoneNumber " + phoneNumber);
            return;
        } else {

            System.out.println("words available to phoneNumber " + phoneNumber);
            for (String word : words) {
                System.out.println(word);
            }
        }
    }

    public void processTollFreeFromInputFile(String fullPathFileName) {

        try {
            FileUtil.getInstance().processFile(fullPathFileName, this);
        } catch (IOException e) {
            // error already written to user
            return;
        }

    }

    @Override
    public String removeUnsupportedCharacters(String line) {
        return line.replaceAll(regex, "");

    }

    @Override
    public boolean validateWord(String word) {
        if (word != null && !word.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void processData(String word) {
        processTollFree(word);
    }
}
