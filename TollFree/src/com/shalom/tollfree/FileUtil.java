package com.shalom.tollfree;

import java.io.*;

/**
 * Created by sha on 3/5/16.
 */
public class FileUtil {

    private static final Object lock = new Object();
    private static volatile FileUtil _instance;

    private FileUtil() { }

    public static FileUtil getInstance() {

        if (_instance == null) {

            synchronized (lock) {

                if(_instance == null) {
                    _instance = new FileUtil();
                }

            }
        }

        return _instance;
    }

    public void processFile(String fullPathFileName, ProcessDataFromFile pdff ) throws IOException {

        try {

            FileInputStream fst = new FileInputStream(fullPathFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fst));

            // read ech line and add it to dictionary
            String line = br.readLine();
            while ( line != null) {

                String word = pdff.removeUnsupportedCharacters(line);
                if(pdff.validateWord(word)) {
                    pdff.processData(word);
                }

                line = br.readLine();
            }

            //Close the input stream
            br.close();

        } catch (FileNotFoundException e) {
            System.out.println("Failed to load dictionary file " + fullPathFileName + ". " + e.getMessage());
            throw e;
        } catch (IOException e) {
            System.out.println("Failed to read line in file " + fullPathFileName + ". " + e.getMessage());
            throw e;
        }

    }
}
