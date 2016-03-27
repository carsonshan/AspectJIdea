package aspectj.trace.ui.form;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * 程序主界面
 *
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 3/26/16 9:49 AM.
 */
public class MainForm {
    Logger logger = Logger.getLogger(getClass());

    private JPanel mainJPanel;
    private JTextArea textArea1;
    private JButton button1;
    private JButton button2;
    private JTextField textField1;
    private JTextArea textArea2;
    private JTextArea textArea3;
    private JPanel leftPanel;
    private JPanel rightPanel;

    public MainForm() {
        leftPanel.setPreferredSize(new Dimension(500, 500));
        rightPanel.setPreferredSize(new Dimension(500, 500));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().mainJPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
