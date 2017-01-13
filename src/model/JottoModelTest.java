package model;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
     *      "guess int int", "" if Server return error message, method will return empty string.
     */
    @Test
    public void testMakeGuess_Empty() {
        try {
            assertEquals("", JottoModel.makeGuess(0, "")); // server return error
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testMakeGuess_ValidRequestParams() {
        try {
            assertEquals("guess 3 1", JottoModel.makeGuess(16952, "crazy"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testMakeGuess_inValidRequestParams() {
        try {
            assertEquals("", JottoModel.makeGuess(16952, "code")); // (valid-id, invalid word)
            assertEquals("", JottoModel.makeGuess(16952, "yellow"));
            
            assertEquals("", JottoModel.makeGuess(Integer.MIN_VALUE, "crazy"));
            assertEquals("", JottoModel.makeGuess(Integer.MAX_VALUE + 1, "crazy"));
            
            assertEquals("", JottoModel.makeGuess(Integer.MIN_VALUE, "yellow"));
            assertEquals("", JottoModel.makeGuess(Integer.MAX_VALUE, "yellow"));
            
            assertEquals("", JottoModel.makeGuess(Integer.MIN_VALUE, "code"));
            assertEquals("", JottoModel.makeGuess(Integer.MAX_VALUE, "code"));
            
            assertEquals("", JottoModel.makeGuess(16952, "yessssssss")); // (valid-ID, word not in dict)
            assertEquals("", JottoModel.makeGuess(Integer.MIN_VALUE, "yessssssss"));
                       
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
