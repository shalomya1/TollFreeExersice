package com.shalom.tollfree;

import java.io.*;
import java.util.*;

/**
 * Created by sha on 3/5/16.
 *
 * The dictionary holds all words permitted to replace phone numbers, i.e. toll free numbers.
 */
public class Dictionary implements ProcessDataFromFile {


    private Node root;

    private static final String regex = "[^a-zA-Z0-9\\-\\\\s]";

    public Dictionary() {

        initDictionary();
    };

    /*
        initialize dictionary
     */
    private void initDictionary() {
        root = new Node('.', new HashMap<Character, Node>(), false);
    }

    public void loadDictionary(String fullPathFileName) throws IOException {

        // reset dictionary if already contains data
        initDictionary();

        FileUtil.getInstance().processFile(fullPathFileName, this);
    }

    /*
        remove any special characters
     */
    @Override
    public String removeUnsupportedCharacters (String line) {

        String word = line.replaceAll(regex, "").toUpperCase();
        return word;
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
        addToDictionary(word);
    }

    /*
        add words to dictionary, assumptions words can contain numbers and dash (-)
     */
    private void addToDictionary(String str) {

        char[] characters = str.toCharArray();
        Node node = root;
        for (char ch : characters) {

            Map<Character,Node> nodes = node.getNodes();

            if (nodes.containsKey(ch)) {
                node = nodes.get(ch);
            } else {
                node = new Node(ch, new HashMap<Character, Node>(), false);
                nodes.put(ch,node);

            }
        }

        node.setEndOfword(true);

    }

    public final Node getDictionary() {

        return root;

    }




}
