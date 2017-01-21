package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// MVC pattern:
// controller listens for input and instruct update model
// models provide methods for changing the state safely
// view querying model for updates

/**
 * JottoModel represents Jotto Game database as list of string records.
 */
public class JottoModel {
    // list[0] - puzzleID
    // list[1] - game current status
    // list[2]...[n] - guess result received form server
    List<String> gameDB = Collections.synchronizedList(new ArrayList<String>());
    public JottoModel(){
        gameDB.add(0, "none");
        gameDB.add(1, "notset");
    }
    
   
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
        String inputLine = in.readLine();       
        in.close();
        
        return inputLine;
    }

    /**
     * Set new PuzzleID.
     * @param id new pazzleID provided by client.
     */
    public synchronized void setPuzzleID(int id){
        gameDB.add(0, Integer.toString(id));
    }
    
    /**
     * @return current puzzleID.
     */
    public synchronized int getPuzzleID(){
        return Integer.parseInt(gameDB.get(0));
    }
    
    /**
     * Adds client's guess into model's guesses list.
     * @param guessResult returned result form server 
     */
    public synchronized void addGuess(String... guessResult){
        if(guessResult.length > 1 && Integer.parseInt(guessResult[1]) >= 0)
            gameDB.add(Integer.parseInt(guessResult[1]), guessResult[0]);
        else
            gameDB.add(guessResult[0]);
    }
    
    /** 
     * @return last guess result, made by client.
     */
    public synchronized String getGuess(int... n){
        if(n.length > 0) return gameDB.get(n[0]);
        else return gameDB.get(gameDB.size()-1);
    }
    
    
    
    /**
     * Set winning status.
     * @param result winning result from server.
     */
    public synchronized void setWinningStatus(String result){
        gameDB.add(1, result);
    }
    
    /**
     * Get winning status.
     */
    public synchronized String getWinningStatus(){
        return gameDB.get(1);
    }
}
