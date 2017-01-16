
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
    
}
