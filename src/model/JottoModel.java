package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// MVC pattern:
// controller listens for input and instruct update model
// models provide methods for changing the state safely
// view querying model for updates

/**
 * JottoModel represents Jotto Game current state.
 */
public class JottoModel {
    private String winningStatus = "notset";
    private int puzzleID = 0; 
    private List<String> guessResults = new ArrayList<String>();
    
    /**
     * Send a client guess to Server and read back the reply.
     * @param guess a trial guessing word
     * @param puzzleID current puzzle ID
     * @return servers respond on a client's guessing word, as "guess n1 n2" character string. Were n1 and n2 are decimals.
     * @throws IOException 
     */
    public String makeGuess(int puzzleID, String guess) throws IOException {
        // Assertions for input params
        // No in-line assertions here b/c on each invalid param case we must get respond from server to process it.
        
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

    /**
     * Set new PuzzleID.
     * @param id new pazzleID provided by client.
     */
    public void setPuzzleID(int id){
        puzzleID = id;
    }
    
    /**
     * @return current puzzleID.
     */
    public int getPuzzleID(){
        return puzzleID;
    }
    
    /**
     * Adds client's guess into model's guesses list.
     * @param guessResult returned result form server 
     */
    public void addGuess(String guessResult){
        guessResults.add(guessResult);
    }
    
    /**
     * Set winning status.
     * @param result winning result from server.
     */
    public void setWinningStatus(String result){
        winningStatus = result;
        System.out.println("from model "+winningStatus);
    }
}
