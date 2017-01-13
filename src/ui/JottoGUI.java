package ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

/**
 * TODO Write the specification for JottoGUI
 */
public class JottoGUI extends JFrame implements ActionListener{

    private static final long serialVersionUID = 1L; // required by Serializable

    // components to use in the GUI
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
        
        guessTable = new JTable();
        guessTable.setName("guessTable");

        // Add ActionListeners to set New Puzzle Number
        newPuzzleNumber.addActionListener(this);
        newPuzzleButton.addActionListener(this);
        
        // TODO Problems 2, 3, 4, and 5
        // Set JComponets into GroupLayout
        // label, button  and text field are in sequence horizontally
        SequentialGroup h1 = layout.createSequentialGroup();
        h1.addComponent(puzzleNumber);
        h1.addComponent(newPuzzleButton);
        h1.addComponent(newPuzzleNumber);
        layout.setHorizontalGroup(h1);
        
        // label, button and text field are in parallel vertically, aligned at text baseline
        ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        v1.addComponent(puzzleNumber);
        v1.addComponent(newPuzzleButton);
        v1.addComponent(newPuzzleNumber);
        layout.setVerticalGroup(v1);
        
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
        Random randomInt = new Random();
        try {
            Integer num = Integer.parseInt(newPuzzleNumber.getText());
            // if input is negative int, pick random num from 0 to 10 000
            if (num > 0 || num == 0){
                puzzleNumber.setText("Puzzle #" + num.toString());
                newPuzzleNumber.setText("");
            }
            else{                
                puzzleNumber.setText("Puzzle #" + randomInt.nextInt(10000) + 1);
                newPuzzleNumber.setText("");
            }
            
        } catch (Exception e2) {
            // TODO: handle exception
            // if no input is provided, pick random num from 0 to 10 000
            puzzleNumber.setText("Puzzle #" + randomInt.nextInt(10000) + 1); 
            newPuzzleNumber.setText("");
        }   
    }
}
