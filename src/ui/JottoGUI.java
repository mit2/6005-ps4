package ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.JottoModel;

/**
 * TODO Write the specification for JottoGUI
 */
public class JottoGUI extends JFrame implements ActionListener{

    private static final long serialVersionUID = 1L; // required by Serializable
    
    // Init JottoModel here, to avoid two treads running (main & event dispatch tread).
    private final JottoModel model = new JottoModel();

    // Components to use in the GUI
    private final JButton newPuzzleButton;
    private final JTextField newPuzzleNumber;
    private final JLabel puzzleNumber;
    private final JTextField guess;
    private final JTable guessTable;

    /**
     * TODO Write the specification for this constructor
     */
    public JottoGUI() {
        super("JottoGUI");
        
        // call System.exit() when user closes the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        Container cp = this.getContentPane();        
        
        // Sets the layout manager of the content pane to a GroupLayout
        GroupLayout layout = new GroupLayout(cp);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        cp.setLayout(layout);
        
        // Create JComponents
        // components must be named with "setName" as specified in the problem set
        // remember to use these objects in your GUI!
        newPuzzleButton = new JButton("New Puzzle");
        newPuzzleButton.setName("newPuzzleButton");
        
        newPuzzleNumber = new JTextField(10);
        newPuzzleNumber.setName("newPuzzleNumber");        
        
        puzzleNumber = new JLabel("Puzzle #" + new Random().nextInt(10000) + 1); // always starting app with random puzzle number
        puzzleNumber.setName("puzzleNumber");
        
        guess = new JTextField(15);
        guess.setName("guess");
        JLabel guessLabel = new JLabel("Type a guess here:");
        
        guessTable = new JTable();
        guessTable.setName("guessTable");

        // Add ActionListeners to set New Puzzle Number
        newPuzzleNumber.addActionListener(this);
        newPuzzleButton.addActionListener(this);
        guess.addActionListener(this); // finish this listener...
        
        // TODO Problems 2, 3, 4, and 5
        // Set JComponets into GroupLayout        
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
            try {
                processRespond(model.makeGuess(model.getPuzzleID(), guess.getText())); // code is extracted into separated method for testing purposes.          
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }            
        }        
    }
    

    /**
     * CONTROLLER.
     * Set new Puzzle Number in the GUI.
     * require: nothing
     * effects: nothing
     * As method is private, no testing suit is provided. Checked carefully flow only.
     */
    private void setPuzzleNumber(){
        Random randomInt = new Random();
        try {
            Integer num = Integer.parseInt(newPuzzleNumber.getText());           
            if (num > 0 || num == 0){
                model.setPuzzleID(num);
                puzzleNumber.setText("Puzzle #" + model.getPuzzleID());
                newPuzzleNumber.setText("");
            }
            else{ 
                // if input is negative int, pick random num from 0 to 10 000
                model.setPuzzleID(randomInt.nextInt(10000) + 1);
                puzzleNumber.setText("Puzzle #" + model.getPuzzleID());
                newPuzzleNumber.setText("");
            }
            
        } catch (Exception e2) {
            // TODO: handle exception
            // if no input is provided, pick random num from 0 to 10 000
            model.setPuzzleID(randomInt.nextInt(10000) + 1);
            puzzleNumber.setText("Puzzle #" + model.getPuzzleID()); 
            newPuzzleNumber.setText("");
        }     
    }
    
    /**
     * CONTROLLER.
     * Process respond from server.
     * @param guessing client's guess word
     * As method is private, no testing suit is provided. Checked carefully flow only.
     */
    private void processRespond(String guessing){
            model.addGuess(guessing);
            // print “You win! The secret word was [secret word]!” when the user guesses the word correctly, i.e. the server responds with “guess 5 5.”                        
            if(guessing.equals("guess 5 5")){
                model.setWinningStatus(guessing);
                System.out.println("You win! The secret word was [" + guessing + "]!");
            }
            else System.out.println(guessing);
            guess.setText("");      
    }
}
