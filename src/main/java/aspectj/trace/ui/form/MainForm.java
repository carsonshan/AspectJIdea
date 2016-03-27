package aspectj.trace.ui.form;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * 程序主界面
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 3/26/16 9:49 AM.
 */
public class MainForm extends Component {
    Logger logger = Logger.getLogger(getClass());

    private JPanel mainJPanel;                  // 主面板
    private JPanel leftPanel;                   // 左面板
    private JPanel rightPanel;                  // 右面板
    private JButton fileChooseBtn;              // 选择文件的按钮
    private JButton parseBtn;                   // 解析的按钮
    private JTextField filePathTextField;       // 选择文件的位置
    private JTextArea rawOutput;                // 原始输出
    private JTextArea matchTextArea;            // 匹配区内容
    private JTextArea inputTextArea;            // 输入匹配区内容

    public MainForm() {
        // 初始化控件的显示
        inputTextArea.setMargin(new Insets(10, 10, 10, 10));
        matchTextArea.setMargin(new Insets(10, 10, 10, 10));
        rawOutput.setMargin(new Insets(10, 10, 10, 10));
        inputTextArea.setPreferredSize(new Dimension(1000, 800));
        leftPanel.setPreferredSize(new Dimension(1000, 800));
        rightPanel.setPreferredSize(new Dimension(1000, 800));

        // 选择文件按钮点击事件
        fileChooseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("choose file btn clicked...");
                JFileChooser chooser = new JFileChooser(); // 选择文件对话框
                chooser.setMultiSelectionEnabled(false); // 暂时只选择单个文件
                chooser.setFileFilter(new FileFilter() { // 只允许选择.java文件
                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory())
                            return true;
                        return f.getName().endsWith(".java");  //设置为选择以.java为后缀的文件
                    }

                    @Override
                    public String getDescription() {
                        return ".java";
                    }
                });
                int rVal = chooser.showOpenDialog(MainForm.this); // 显示对话框
                if (rVal == JFileChooser.APPROVE_OPTION) {// 确定按钮
                    String fileName = chooser.getSelectedFile().getName(); // 选择文件的文件名
                    String filePath = chooser.getCurrentDirectory().toString()
                            + File.separator + fileName; // 选择文件的文件路径

                    logger.debug(fileName);
                    logger.debug(filePath);
                    // 读出文件内容
//                    try {
////                        PrintWriter writer = new PrintWriter(new BufferedWriter(
////                                new FileWriter(filePath))); // newһ��PrintWriter����
////
////                        writer.write(mTextArea.getText()); // ���ı�����д���ļ�
////                        writer.close(); // �ر������
//                        chooser.cancelSelection(); // 选择取消
//                    } catch (IOException e) {
//                        logger.error("Exception in choosing files.");
//                    }

                } else if (rVal == JFileChooser.CANCEL_OPTION) {
                    logger.info("Cancel choose file...");
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().mainJPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
