import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Graphics {
    private static final Object enterLock;
    private static final Object lock;
    private static final JFrame frame;
    private static final JPanel panel;
    private static final JTextPane board;
    private static final JLabel message;
    private static final JLabel hand;
    private static final JLabel cheats;
    private static final JTextArea input;
    private static final JLabel inputLabel;
    private static KeyEvent lastkey;

    static {
        lock = new Object();
        enterLock = new Object();
        frame = new JFrame("Scrabble");
        panel = new JPanel();
        board = new JTextPane();
        hand = new JLabel();
        cheats = new JLabel();
        message = new JLabel();
        input = new JTextArea();
        inputLabel = new JLabel("Enter word: ");
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.add(inputLabel);
        inputPanel.add(input);
        java.awt.Font f = new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12);
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        board.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        hand.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        message.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        centerText(board);
        board.setEditable(false);
        board.setMinimumSize(new java.awt.Dimension(board.getMinimumSize().width, 100));
        hand.setMinimumSize(new java.awt.Dimension(hand.getMinimumSize().width, 100));
        message.setMinimumSize(new java.awt.Dimension(message.getMinimumSize().width, 100));
        input.setMaximumSize(new java.awt.Dimension(input.getMaximumSize().width, 20));
        cheats.setMinimumSize(new java.awt.Dimension(cheats.getMinimumSize().width, 100));
        panel.add(board);
        panel.add(hand);
        panel.add(message);
        panel.add(inputPanel);
        cheats.setFont(f);
        board.setFont(f);
        hand.setFont(f);
        message.setFont(f);
        frame.add(panel);
        frame.setSize(800, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    synchronized (enterLock) {enterLock.notify();}
                }else{
                    lastkey = e;
                    synchronized (lock) {
                        lock.notify();
                    }
                }
            }
        });
    }

    public static String input() {
        try {
            synchronized (enterLock) {
                enterLock.wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String ret = input.getText();
        input.setText("");
        return ret;
    }

    private static void centerText(JTextPane textPane) {
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

    public static void showBoard(Board b){
        board.setText(b.toString());
    }
    public static void showHand(Hand h){
        hand.setText(h.toString());
    }
    public static void showMessage(String msg){
        message.setText(msg);
    }
    public static void cheatsOn(){
        panel.add(cheats);
    }
    public static void showCheats(String msg){
        cheats.setText(msg);
    }
    public static KeyEvent getKey(){
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return lastkey;
    }
}
