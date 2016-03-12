package com.shalom.tollfree;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by sha on 3/5/16.
 */
public class Node {

    Character character;
    Map<Character,Node> nodes;
    boolean endOfword;

    public Node(Character character, Map<Character,Node> nodes, boolean endOfword) {
        this.character = character;
        this.nodes = nodes;
        this.endOfword = endOfword;
    }

    public Map<Character,Node> getNodes() {
        return nodes;
    }

    public void setNodes(Map<Character,Node> nodes) {
        this.nodes = nodes;
    }

    public boolean isEndOfword() {
        return endOfword;
    }

    public void setEndOfword(boolean endOfword) {
        this.endOfword = endOfword;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }



}
