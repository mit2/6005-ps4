import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class SnippetsAndHelps {
/**
 * getActionCommand() gives you a String representing the action command. The value is component specific; for a JButton you have the option to set the value with setActionCommand(String command) but for a JTextField if you don't set this, it will automatically give you the value of the text field. According to the javadoc this is for compatability with java.awt.TextField.

getSource() is specified by the EventObject class that ActionEvent is a child of (via java.awt.AWTEvent). This gives you a reference to the object that the event came from.

Edit:

Here is a example. There are two fields, one has an action command explicitly set, the other doesn't. Type some text into each then press enter.

public class Events implements ActionListener {

  private static JFrame frame; 

  public static void main(String[] args) {

    frame = new JFrame("JTextField events");
    frame.getContentPane().setLayout(new FlowLayout());

    JTextField field1 = new JTextField(10);
    field1.addActionListener(new Events());
    frame.getContentPane().add(new JLabel("Field with no action command set"));
    frame.getContentPane().add(field1);

    JTextField field2 = new JTextField(10);
    field2.addActionListener(new Events());
    field2.setActionCommand("my action command");
    frame.getContentPane().add(new JLabel("Field with an action command set"));
    frame.getContentPane().add(field2);


    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(220, 150);
    frame.setResizable(false);
    frame.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    String cmd = evt.getActionCommand();
    JOptionPane.showMessageDialog(frame, "Command: " + cmd);
  }

}
 */
//=====================================================================================================================    
   /* processRespond(guess.getText()); // code is extracted into separated method for testing purposes, as Controller.
    updateGUItable();
    // run query in new background Tread
    class RunInBackgraund extends SwingWorker<String[], Void> {                
        @Override
        public String[] doInBackground() {
            System.out.println("new BGTread strated!");
            int i = model.getCurrDBRecordIndex();
            String res = null;
            try {
                res = model.makeGuess(model.getPuzzleID(), guess.getText());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
                                
            String[] respond = {res, Integer.toString(i)};
            return respond;
        }
        
        @Override
        // Executed on the Event Dispatch Thread after the doInBackground method is finished.
        public void done() {        
            System.out.println("From Swing.invokeLater()");
            String[] respond = null;
            try {
                respond = get();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            DefaultTableModel tm = (DefaultTableModel) guessTable.getModel();
            //System.out.println(tm.getRowCount());
            //System.out.println(respond[1]);
            
            String[] updateData = respond[0].split(" "); // split server respond into tokens
            updateData[0] = model.getGuess(Integer.parseInt(respond[1])); // get data record from JottoModel DB
            tm.insertRow(Integer.parseInt(respond[1]) - 1, updateData);
            //update JottoModel DB record
            String updateRecord = "";
            for (int i = 0; i < updateData.length; i++) {
                updateRecord += updateData[i];
            }
            model.addGuess(updateRecord, respond[1]);
            updateGUItable(index);*/
}
