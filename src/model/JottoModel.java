package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

/**
 * TODO Write the specification for JottoModel
 */
public class JottoModel {
    private final String guess = null;
    private final int puzzleID = 0; 
    
    /**
     * Send a client guess to Server and read back the reply.
     * @param guess a trial guessing word
     * @return servers respond on a client's guessing word.
     * @throws IOException 
     */
    public static String makeGuess(int puzzleID, String guess) throws IOException {
        // Assertions for input params
        // NO inline assertions here b/c on each invalid param case we must get respond from server to process it.
        
        URL oracle = new URL("http://courses.csail.mit.edu/6.005/jotto.py?puzzle=" + puzzleID + "&guess=" + guess);
        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null){
            String inResponse = inputLine.substring(0, 5);
             if(!inResponse.equals("error")) return inputLine;
             else System.out.println(inputLine);
        }           
        in.close();
        
        return "";    // if Server return error message, method will return empty string.
    }
    
    public static void main(String[] args) throws Exception {       
       //System.out.println(makeGuess(16952, "crazy"));
    }
}
