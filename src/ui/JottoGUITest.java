package ui;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.swing.table.DefaultTableModel;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import model.JottoModel;

public class JottoGUITest {

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

    @Test
    /**
     * Controllers in JottoGUI set as private methods.
     * Chosen my special technique:
     * insert public void test("cmd_param") into ADT
     * in JUnit Test just 'query' what private data you need from ADT thru test("cmd_param").
     * Look example.
     */
    public void testControllers(){
        // set initial data
        JottoGUI gui = new JottoGUI();
        JottoModel model = (JottoModel) gui.test("getJottoModel");
        
        /*model.addGuess("guess 5 5");
        gui.test(updateGUITable);
        gui.test(clearGUItable);
        gui.test("getDefaultTableModel");*/
        
        // setPuzzleNumber()
        assertEquals(23, gui.test("setNewPuzzleNumber_23"));
        assertTrue((int)gui.test("setNewPuzzleNumber_-1") > 0);
        assertTrue((int)gui.test("setNewPuzzleNumber_Empty") > 0);
        
        // processRespond() & updateGUItable()
        // test regular guess status
        DefaultTableModel tm = (DefaultTableModel) gui.test("processRespond_guess 3 3_updateGUItable");
        assertEquals("guess 3 3", model.getGuess());
        assertEquals(gui.test("getGuessText"), tm.getValueAt(1, 0));
        assertEquals("3", tm.getValueAt(1, 1));
        assertEquals("3", tm.getValueAt(1, 2));
        assertEquals("", gui.test("getGuessText"));
        // test winning guess status
        gui.test("processRespond_guess 5 5_updateGUItable");
        assertEquals("guess 5 5", model.getGuess());
        assertEquals("guess 5 5", model.getWinningStatus());
        assertEquals(gui.test("getGuessText"), tm.getValueAt(1, 0));
        assertEquals("--// You win!  :)---//----//----//---", tm.getValueAt(1, 1));
        assertEquals("---//----//------//----//---//----//--", tm.getValueAt(1, 2));
        assertEquals("", gui.test("getGuessText"));
        // test guess errors
        // error 0: Ill-formatted request
        gui.test("processRespond_Ill-formatted request");
        assertEquals("", tm.getValueAt(1, 0));
        assertEquals("error 0: Ill-formatted request.", tm.getValueAt(1, 1));
        assertEquals("", tm.getValueAt(1, 2));
        // error 1: Non-number puzzle ID.
        gui.test("processRespond_Non-number puzzle ID");
        assertEquals("cargo", tm.getValueAt(1, 0));
        assertEquals("error 1: Non-number puzzle ID.", tm.getValueAt(1, 1));
        assertEquals("", tm.getValueAt(1, 2));
        // error 2: Invalid guess. Length of guess != 5 or guess is not a dictionary word.
        gui.test("processRespond_Invalid guess");
        assertEquals("cargosss", tm.getValueAt(1, 0));
        assertEquals("error 2: Invalid guess. Length of guess != 5 or guess is not a dictionary word.", tm.getValueAt(1, 1));
        assertEquals("", tm.getValueAt(1, 2));
        
        // clearGUItable()
        assertEquals(6, tm.getRowCount());
        gui.test("clearGUItable");
        assertEquals(1, tm.getRowCount());      
    }

}
