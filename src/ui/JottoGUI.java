package ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import model.JottoModel;

/**
 * JottoGUI is interface for playing Jotto guessing 5-letter word game by communicating remote server.
 * According to MVC design pattern, JottoGUI is Complex JComponent, where sub components has their own data structure model.
 * It is up to implementor which models 'extract' to main JottoModel.
 */
public class JottoGUI extends JFrame implements ActionListener{

    private static final long serialVersionUID = 1L; // required by Serializable
    
    // Init JottoModel.
    private final JottoModel model = new JottoModel();
    // Queue for queries to processed one-by-one by threads. Thread-safe ADT.
    private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    // Components to use in the GUI
    private final JButton newPuzzleButton;
    private final JTextField newPuzzleNumber;
    private final JLabel puzzleNumber;
    private final JTextField guess;
    private final JTable guessTable;
    private int countQuery = -1;

    /**
     * Construct  client JottoGUI using GroupLayout.
     */
    public JottoGUI() {
        super("JottoGUI");
        
        // call System.exit() when user closes the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
     
        // Create Container 'Panel' with top essential GUI elements.
        Container panel = new JPanel(); //PREFERRED!
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        panel.setLayout(layout);
        
        // Create JComponents
        // components must be named with "setName" as specified in the problem set
        // remember to use these objects in your GUI!
        newPuzzleButton = new JButton("New Puzzle");
        newPuzzleButton.setName("newPuzzleButton");
        
        newPuzzleNumber = new JTextField(10);
        newPuzzleNumber.setName("newPuzzleNumber");        
        
        puzzleNumber = new JLabel("Puzzle #" + new Random().nextInt(10000) + 1); // always starting app with random puzzle number
        puzzleNumber.setName("puzzleNumber");
        model.setPuzzleID(Integer.parseInt(puzzleNumber.getText().substring(8)));
        
        guess = new JTextField(15);
        guess.setName("guess");
        JLabel guessLabel = new JLabel("Type a guess here:");
        
        guessTable = new JTable(1, 3);
        guessTable.setName("guessTable");
        DefaultTableModel tm = (DefaultTableModel) guessTable.getModel();
        tm.setValueAt("guess", 0, 0);
        tm.setValueAt("in common", 0, 1);
        tm.setValueAt("correct position", 0, 2);
        guessTable.repaint();

        // Add ActionListeners to set New Puzzle Number
        newPuzzleNumber.addActionListener(this);
        newPuzzleButton.addActionListener(this);
        guess.addActionListener(this); // finish this listener...
        
        // TODO Problems 2, 3, 4, and 5
        // Set top panel JComponets into GroupLayout        
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                   .addComponent(puzzleNumber)
                   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                           .addComponent(newPuzzleButton)
                           .addComponent(guessLabel)) 
                   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                           .addComponent(newPuzzleNumber)
                           .addComponent(guess))     
             );
             layout.setVerticalGroup(
                layout.createSequentialGroup()
                   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(puzzleNumber)
                        .addComponent(newPuzzleButton)
                        .addComponent(newPuzzleNumber))
                   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                           .addComponent(guessLabel)
                           .addComponent(guess))   
             );
        
        
        
        
        // Sets the layout manager of the content pane to a GroupLayout
        Container cp = this.getContentPane();
        GroupLayout mainLayout = new GroupLayout(cp);
        mainLayout.setAutoCreateGaps(true);
        mainLayout.setAutoCreateContainerGaps(true);
        cp.setLayout(mainLayout);
             
        // Set Panel container and JTable into main GroupLayout        
        mainLayout.setHorizontalGroup(
                mainLayout.createSequentialGroup()                   
                   .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                           .addComponent(panel)
                           .addComponent(guessTable)) 
                          
             );
        mainLayout.setVerticalGroup(
                mainLayout.createSequentialGroup()
                   .addComponent(panel)
                   .addComponent(guessTable)
             );
        // size the window to fit the default sizes of all the components
        this.pack();
    }

    /**
     * Start the GUI Jotto client.
     * @param args unused
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JottoGUI main = new JottoGUI();
                main.setVisible(true);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //  If no number is provided or the input is not a positive integer, pick a random positive integer.
        if(e.getSource().equals(newPuzzleButton) || e.getSource().equals(newPuzzleNumber)){
            setPuzzleNumber(); // code is extracted into separated method for testing purposes.
        }else if(e.getSource().equals(guess)){            
            processRespond(guess.getText() + " " + (++countQuery)); // 'guess i', code is extracted into separated method for testing purposes, as Controller.
            updateGUItable();
            /**
             *  Run query in new background Tread
             *  each new Tread starting, will grab head query in 'queue' represented as a string:  guess + index,
             *  will process this query and update:
             *  - GUI table by 'guess' using linear search.
             *  - update Jotto db list using guess 'index'.
             * @author win8
             *
             */
            class RunInBackgraund extends SwingWorker<String[], Void> {                
                @Override
                public String[] doInBackground() {
                    System.out.println("new BackGrTread strated!");
                    String record = null;
                    try {
                        record = queue.take();
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    String[] splitRecord = record.split(" ");
                    String word = splitRecord[0];
                    String index = splitRecord[1];
                    System.out.println("INDEX :" + index);
                    String res = null;
                    System.out.println("getpuzid: " + model.getPuzzleID() + " "+ word);
                    try {
                        res = model.makeGuess(model.getPuzzleID(), word);
                        System.out.println("Respond server " + word + res);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                                        
                    String[] respond = {res, index,  word};
                    return respond;
                }
                
                @Override
                // Executed on the Event Dispatch Thread after the doInBackground method is finished.
                public void done() {        
                    System.out.println("From Swing.invokeLater()");
                    String[] respond = null;
                    try {
                        respond = get();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // prepare to update record
                    String[] updateData = null;
                    if(respond[0].contains("error")){
                        updateData = new String[]{respond[2], respond[0].substring(8), ""};
                    }else if(respond[0].contains("guess 5 5")){
                        updateData = new String[]{respond[2], "**** You win!  :)*************************", "******************************************"};
                        model.setWinningStatus(respond[0].replaceFirst("guess", respond[2]));
                        System.out.println(model.getWinningStatus());
                    }
                    else{
                        updateData = respond[0].split(" "); // split server respond into tokens
                        updateData[0] = respond[2]; // replace 'guess' with actual word     
                    }
                                   
                    // search tm for guess word & update it                    
                    DefaultTableModel tm = (DefaultTableModel) guessTable.getModel();
                    int k = tm.getRowCount();
                    for (int i = 0; i < k; i++) {
                        System.out.println("data form table :"+tm.getValueAt(i, 0));
                        if(tm.getValueAt(i, 0).equals(updateData[0])){
                            tm.removeRow(i);
                            tm.insertRow(i, updateData);
                        }                            
                    }
  
                    //update JottoModel DB record
                    String updateRecord = "";
                    for (int i = 0; i < updateData.length; i++) {
                        updateRecord += updateData[i];
                    }
                    int i = Integer.parseInt(respond[1]) + 2; // make index valid in DB position, as 0, 1 pos in DB reserved not for records.
                    model.addGuess(updateRecord, Integer.toString(i) );        
        }        
     }
            RunInBackgraund fib = new RunInBackgraund();
            fib.execute(); // run Worker thread
   }
 }
  
    

    /**
     * CONTROLLER.
     * Set new Puzzle Number in the GUI.
     * require: nothing
     * effects: nothing
     * As method is private, used my custom testing technique.
     */
    private void setPuzzleNumber(){
        Random randomInt = new Random();
        try {
            Integer num = Integer.parseInt(newPuzzleNumber.getText());           
            if (num > 0 || num == 0){
                // if input is correct
                model.setPuzzleID(num);
                puzzleNumber.setText("Puzzle #" + model.getPuzzleID());
                newPuzzleNumber.setText("");
                clearGUItable();
            }
            else{ 
                // if input is negative int, pick random num from 0 to 10 000
                model.setPuzzleID(randomInt.nextInt(10000) + 1);
                puzzleNumber.setText("Puzzle #" + model.getPuzzleID());
                newPuzzleNumber.setText("");
                clearGUItable();
            }
            
        } catch (Exception e2) {
            // TODO: handle exception
            // if no input is provided, pick random num from 0 to 10 000
            model.setPuzzleID(randomInt.nextInt(10000) + 1);
            puzzleNumber.setText("Puzzle #" + model.getPuzzleID()); 
            newPuzzleNumber.setText("");
            clearGUItable();
        }     
    }
    
    /**
     * CONTROLLER.
     * Process respond from server.
     * @param guessing client's guess word
     * As method is private, used my custom testing technique.
     */
    private void processRespond(String guessing){
            model.addGuess(guessing);
            queue.add(guessing); // queue used by treads to take one query to process.
            // print �You win! The secret word was [secret word]!� when the user guesses the word correctly, i.e. the server responds with �guess 5 5.�                        
            if(guessing.equals("guess 5 5")){
                // this code is used in NOT multithreaded version
                model.setWinningStatus(guessing);
                System.out.println("You win! The secret word was [" + guessing + "]!");
            }
            else System.out.println(guessing);
    }
    
    /**
     * CONTTOLLER
     * Adds row to the defaultTableModel of JTable, filled with server respond data.
     * As method is private, used my custom testing technique.
     */
    private void updateGUItable(){
        DefaultTableModel tm = (DefaultTableModel) guessTable.getModel();
        // get respond from JottoModel, split it in array of strings
        // THIS CODE SETUP IS NOT FOR MULTITREADED CASE, ONLY USED TO PLACE GUESS WORD WITHOUT RESULTS (!)
        // AFTER ADDING MULTITHREAD SUPPORT USED ONLY LAST IF CASE.
        if(model.getGuess().length() == 9){
            // print out winning guess message
            String[] data = model.getGuess().split(" ");
            data[0] = guess.getText();
            if(data[1].equals("5") && data[2].equals("5")){
               data[1] = "--// You win!  :)---//----//----//---";
               data[2] = "---//----//------//----//---//----//--";                
            }          
            guess.setText("");
            tm.addRow(data);
        }else if(model.getGuess().contains("error")){
            // print out error message 
            String[] data = {guess.getText(), model.getGuess(), ""};
            guess.setText("");
            tm.addRow(data);
        }
        else{
            // add just guessing word (!)
            String[] data = {guess.getText(), "", ""};
            guess.setText("");
            tm.addRow(data);
        }
        
        guessTable.repaint();
    }
    
    /**
     * CONTROLLER
     * Clear table data in DefaultTableModel.
     * As method is private, no testing suit is provided. Checked carefully flow only.
     */
    private void clearGUItable(){
        DefaultTableModel tm = (DefaultTableModel) guessTable.getModel();
        int i = tm.getRowCount() - 1; // -1 to suit to row nums
        while(i != 0) tm.removeRow(i--);
        guessTable.repaint();        
    }
    
    /**
     * For testing Controllers here as private methods. To disable, change this method visibility to private.
     * @param var varies cmd's invoked to get wanted private method.
     * @return diff obj depending on invoked cmd.
     */
    public Object test(String... var){
        if(var[0] == "getJottoModel"){
            return this.model;
        }
        
        if(var[0].equals("getGuessText")){
            return guess.getText();
        }
      
        if(var[0] == "clearGUItable"){ // reference equality is also good in this case as param point to the same obj
            clearGUItable();
            return null;
        }
        
        
        // setNewPuzzleNumber cases
        if(var[0].equals("setNewPuzzleNumber_23")){
            newPuzzleNumber.setText("23");
            setPuzzleNumber();
            return model.getPuzzleID();
        }        
        if(var[0].equals("setNewPuzzleNumber_-1")){
            newPuzzleNumber.setText("-1");
            setPuzzleNumber();
            return model.getPuzzleID();
        }        
        if(var[0].equals("setNewPuzzleNumber_Empty")){
            newPuzzleNumber.setText("");
            setPuzzleNumber();
            return model.getPuzzleID();
        }
        if(var[0].equals("setNewPuzzleNumber_9999999")){
            newPuzzleNumber.setText("9999999");
            setPuzzleNumber();
            return model.getPuzzleID();
        }
        
        
        // processRespond cases
        if(var[0].equals("processRespond_guess 3 3_updateGUItable")){
            processRespond("guess 3 3");
            updateGUItable();
            return (DefaultTableModel) guessTable.getModel();
        }
        if(var[0].equals("processRespond_guess 5 5_updateGUItable")){
            processRespond("guess 5 5");
            updateGUItable();
            return (DefaultTableModel) guessTable.getModel();
        }
        if(var[0].equals("processRespond_Non-number puzzle ID") || var[0].equals("processRespond_Invalid guess")
                            || var[0] == "processRespond_Ill-formatted request"){
            if(var[0].equals("processRespond_Non-number puzzle ID")){
                processRespond("error 1: Non-number puzzle ID.");
                guess.setText("cargo");
            }else if(var[0].equals("processRespond_Ill-formatted request")){
                processRespond("error 0: Ill-formatted request.");
                guess.setText("");
            }            
            else {
                processRespond("error 2: Invalid guess. Length of guess != 5 or guess is not a dictionary word.");
                guess.setText("cargosss");
            }            
            updateGUItable();
            return (DefaultTableModel) guessTable.getModel();
        }
        
        return null;
    }
}
