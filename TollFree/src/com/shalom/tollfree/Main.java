package com.shalom.tollfree;

import com.shalom.tollfree.Exceptions.TollFreeGeneralException;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by sha on 3/5/16.
 */
public class Main {

    /*
        Toll Free
        Many companies like to list their phone numbers using the letters printed on most
        telephones. This makes the number easier to remember for customers. An example may
        be 1-800-FLOWERS
        This program show a user possible matches for a list of provided phone numbers.

        program designed to calculate numbers with minimal performance and memory allocation,
        i.e. using tree and return null object instead off empty objects (specially when using recursive function)

        the program can read file with number with the following syntax
        file fullFathFileName
        or it can get phone number from the user

        To exit the program type exit
     */


    public static final String defaultDictionaryPath = "/home/sha/Documents/dictionaryDef.txt";
    public static final String fileCommand = "file ";


    public static void main(String[] args) {

        String dictionaryPath = defaultDictionaryPath;
        // check if the user set dictionary
        // no validation where made regarding input,
        // i.e. non valid input such as args as " -d -ef -e" etc...
        for(int i = 0 ; i < args.length ; i++) {
            if(args[i].equals("-d") && args.length > i+1){
                dictionaryPath = args[i+1];
                break;
            }
        }
        TollFree tollFree = TollFree.getInstance();
        try {
            tollFree.loadDictionary(dictionaryPath);
        } catch (TollFreeGeneralException | IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter phone number:");
            String inputLine = in.nextLine().trim();
            if (inputLine.toLowerCase().equals("exit")){
                break;
            }

            // read phone numbers from input file
            if (inputLine.toLowerCase().startsWith(fileCommand)) {
                // read file numbers
                tollFree.processTollFreeFromInputFile(inputLine.substring(fileCommand.length(), inputLine.length()));
            } else {
                tollFree.processTollFree(inputLine);
            }
        }

    }

}
