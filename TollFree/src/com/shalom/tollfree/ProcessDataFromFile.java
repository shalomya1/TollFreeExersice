package com.shalom.tollfree;

/**
 * Created by sha on 3/5/16.
 *
 * This interface creates to save duplication of code related to File reading and processing
 */
public interface ProcessDataFromFile {

    public String removeUnsupportedCharacters(String line);
    public boolean validateWord(String word);
    public void processData(String word);
}
