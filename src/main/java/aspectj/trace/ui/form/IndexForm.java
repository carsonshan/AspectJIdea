package aspectj.trace.ui.form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 3/25/16 10:33 PM.
 */
public class IndexForm {

    private JButton button1;
    private JTable table1;
    private JPanel panel1;

    public IndexForm() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("111");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Index Form");

        //frame.setContentPane(new IndexForm.contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        panel1.setSize(new Dimension(200, 300));
    }
}
