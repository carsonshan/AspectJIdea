package aspectj.trace.ui;

import aspectj.trace.core.compiler.AjcCompiler;
import aspectj.trace.util.FileUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
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
    private JButton compileBtn;                 // 解析的按钮
    private JTextArea rawOutputTextArea;        // 原始输出
    private JTextArea matchTextArea;            // 匹配区内容
    private JTextArea inputTextArea;            // 输入匹配区内容
    private JLabel fileNamesLabel;
    private AjcCompiler ajcCompiler;            // Ajc编译器
    private String className;                   // 被编译的文件的类名

    public MainForm() {
        // 初始化控件的显示
        inputTextArea.setMargin(new Insets(10, 10, 10, 10));
        matchTextArea.setMargin(new Insets(10, 10, 10, 10));
        rawOutputTextArea.setMargin(new Insets(10, 10, 10, 10));
        inputTextArea.setPreferredSize(new Dimension(1000, 800));
        leftPanel.setPreferredSize(new Dimension(1000, 800));
        rightPanel.setPreferredSize(new Dimension(1000, 800));

        // 初始化ajc编译器
        ajcCompiler = new AjcCompiler();

        // 选择文件按钮点击事件
        fileChooseBtn.addActionListener(new FileChooseListener());
        // 编译按钮点击事件
        compileBtn.addActionListener(new ComplierListener());
    }

    /**
     * 选择文件执行的事件
     */
    private class FileChooseListener implements ActionListener {

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
                final String fileName = chooser.getSelectedFile().getName(); // 选择文件的文件名
                final String filePath = chooser.getCurrentDirectory().toString()
                        + File.separator + fileName; // 选择文件的文件路径
                className = fileName.replace(".java", "");
                // 开启线程在out文件夹下面创建一个同样的文件,并去掉包名
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String outFilePath = ajcCompiler.getOutFilePath();
                        // 1.首先复制一份文件到项目out目录下面
                        FileUtil.copyFile(filePath, outFilePath + "/" + fileName, true);
                        // 2.接着替换包名的那一行
                        FileUtil.removePackageName(outFilePath + "/" + fileName);
                        // 3.最后替换与aj编译的文件的内容
                        String content = outFilePath + "/TraceApp.aj\n" + outFilePath + "/" + fileName;
                        FileUtil.writeToFile(ajcCompiler.getOutFilePath() + "/sources.lst", content);
                    }
                }).run();
            } else if (rVal == JFileChooser.CANCEL_OPTION) {
                logger.info("Cancel choose file...");
            }
        }
    }

    /**
     * 编译按钮点击事件
     */
    private class ComplierListener implements ActionListener {
        String outFileName = "out.txt";
        String outFile = ajcCompiler.getOutFilePath() + "/" + outFileName;

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // ajc编译生成class文件
                        ajcCompiler.compile();
                        // 运行class文件并得到运行结果
                        String outFileContent = ajcCompiler.run(outFile, className);
                        // 设置最右侧输出框的内容
                        rawOutputTextArea.append(outFileContent);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }).run();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("子程序调用序列匹配");
        frame.setContentPane(new MainForm().mainJPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
