package aspectj.trace.ui;

import aspectj.trace.core.compiler.AjcCompiler;
import aspectj.trace.util.FileUtil;
import aspectj.trace.util.Pair;
import aspectj.trace.util.TreeUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Style;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

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
    private JButton searchButton;
    private AjcCompiler ajcCompiler;            // Ajc编译器
    private String className;                   // 被编译的文件的类名

    private String outFileContent = "";                      //编译输出结果

    public MainForm() {
        // 初始化控件的显示
        inputTextArea.setMargin(new Insets(10, 10, 10, 10));
        matchTextArea.setMargin(new Insets(10, 10, 10, 10));
        matchTextArea.setEnabled(false);
        rawOutputTextArea.setMargin(new Insets(10, 10, 10, 10));
        rawOutputTextArea.setEnabled(false);
        inputTextArea.setPreferredSize(new Dimension(1000, 800));
        leftPanel.setPreferredSize(new Dimension(1000, 800));
        rightPanel.setPreferredSize(new Dimension(1000, 800));

        // 初始化ajc编译器
        ajcCompiler = new AjcCompiler();

        // 选择文件按钮点击事件
        fileChooseBtn.addActionListener(new FileChooseListener());
        // 编译按钮点击事件
        compileBtn.addActionListener(new ComplierListener());
        // 查询按钮点击事件
        searchButton.addActionListener(new SearchListener());

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
    //
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
//                        outFileContent = ajcCompiler.run(outFile, className);
                        ajcCompiler.run(outFile, className);

                        InputStream _Is = new FileInputStream("outaj.txt");
                        byte[] result = new byte[_Is.available()];
                        _Is.read(result);
                        outFileContent = new String(result);
                        // 设置最右侧输出框的内容
                        rawOutputTextArea.setText("");
                        rawOutputTextArea.append(outFileContent);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }).run();
        }
    }

    /**
     * 查询按钮点击事件
     */
    private class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            matchTextArea.setText("");

            /*生成调用关系树*/
            String[] outLines = outFileContent.split("\n");
            TreeUtil callTree = new TreeUtil();
            for (String r : outLines) {
                String[] words = r.split("\\s+");
                for (int i = 1; i < words.length - 1; ++i) {
                    if (words[i].equals("-->")) {
                        callTree.addRunTime(words[i - 1], words[i + 1], words[i + 2]);
                        break;
                    }
                }
            }

            String[] lines = inputTextArea.getText().split("\n");
            int linenum = 1;
            StringBuilder finalShow = new StringBuilder("");

            /*对每行查询指令进行查询*/
            for (String r : lines) {
                String[] com_t = r.split("\\s+");
                List<String> com = new ArrayList<String>();
                for (String k : com_t) {
                    if (!k.equals("")) {
                        com.add(k);
                    }
                }
                //A1 --> A2 --> A3 --> A4 --> A5 --> A6 --> A7 --> A8 --> A9 --> A10
                int comSize = com.size();
                boolean checkInput = true;
                int comIndex = 0;
                LinkedList<String> functions = new LinkedList<String>();
                for (;comIndex + 1 < comSize; comIndex += 2) {
                    if (!com.get(comIndex +1).equals("-->")) {
                        checkInput = false;
                        break;
                    }
                    functions.add(com.get(comIndex));
                }

                if (comSize < 3 || comSize % 2 != 1 || !checkInput) {
                    finalShow.append(linenum + ":ERROR!   wrong input!  -- " + r);
                    linenum++;
                } else {
                    /*添加最后的目标函数*/
                    functions.add(com.get(comSize-1));
                    /*掐头去尾*/
                    String src = functions.removeFirst();
                    String dst = functions.removeLast();
                    /*查询结果存入字符串*/
                    String[] paths = functions.toArray(new String[functions.size()]);
                    List<List<Pair<String, Pair<String, String>>>> result = callTree.getCallPathMultiNode(src, dst, paths);
                    for (List<Pair<String, Pair<String, String>>> c : result) {
                        int indent = 0;
                        StringBuilder toshow = new StringBuilder();
                        for (Pair<String, Pair<String, String>> t : c) {
                            toshow.append(linenum + ":");
                            for (int i = 0; i < indent; ++i) {
                                toshow.append("    ");
                            }
                            toshow.append(t.second.first + " --> " + t.second.second + "    " + t.first + "\n");
                            indent++;
                            linenum++;
                        }
                        finalShow.append(toshow);
                    }
                }
            }
            matchTextArea.append(finalShow.toString());
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
