package model;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ui.JottoGUI;

public class JottoModelTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    
    // makeGuess(guess, puzzleID)
    /**
     * in:
     *      guess: "", str.len == 5, > 5, > 5, str not in Dictionary
     *      puzzleID: 0, < 0, > 0
     * out:
     *      "guess int int"
     */
    @Test
    public void testMakeGuess_Empty() {
        JottoModel model = new JottoModel();
        try {
            assertEquals("error 0: Ill-formatted request.", model.makeGuess(0, "")); // server return error
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testMakeGuess_ValidRequestParams() {
        JottoModel model = new JottoModel();
        try {
            assertEquals("guess 3 1", model.makeGuess(16952, "crazy"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testMakeGuess_inValidRequestParams() {
        // DO TESTS ACCORDENLY TO SPECS! ONLY
        JottoModel model = new JottoModel();
        try {
            assertEquals("error 2: Invalid guess. Length of guess != 5 or guess is not a dictionary word.", model.makeGuess(16952, "code")); // (valid-id, invalid word < 5)
            assertEquals("error 2: Invalid guess. Length of guess != 5 or guess is not a dictionary word.", model.makeGuess(16952, "yellow")); // (valid-id, invalid word > 5)
            //NOT IN SPECS assertEquals("error 1: Non-number puzzle ID.", model.makeGuess(Integer.MIN_VALUE, "yellow")); // (invalid_id < 0, invalidWord.len > 5)
            //NOT IN SPECS assertEquals("error 1: Non-number puzzle ID.", model.makeGuess(Integer.MIN_VALUE, "crazy")); //(invalid_id < 0, validWord)
            //NOT IN SPECS assertEquals("error 0: Ill-formatted request", model.makeGuess(Integer.MIN_VALUE, "code")); // (invalid_id < 0, invalidWord.len < 5)
            assertEquals("error 1: Non-number puzzle ID.", model.makeGuess(Integer.MAX_VALUE + 1, "crazy")); // (invalid_id > 0, validWord)
            assertEquals("error 2: Invalid guess. Length of guess != 5 or guess is not a dictionary word.", model.makeGuess(Integer.MAX_VALUE, "yellow")); // (invalid_id, > 0 invalidWord.len > 5)            
            assertEquals("error 2: Invalid guess. Length of guess != 5 or guess is not a dictionary word.", model.makeGuess(Integer.MAX_VALUE, "code")); // (invalid_id > 0, invalidWord.len < 5)            
            assertEquals("error 2: Invalid guess. Length of guess != 5 or guess is not a dictionary word.", model.makeGuess(16952, "yessssssss")); // (valid-ID, word not in dict)
            assertEquals("error 2: Invalid guess. Length of guess != 5 or guess is not a dictionary word.", model.makeGuess(Integer.MAX_VALUE, "yessssssss")); //(invalid-ID, word not in dict)
            assertEquals("error 0: Ill-formatted request.", model.makeGuess(16952, "")); // (valid-ID, word EMPTY)
                       
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    

}
