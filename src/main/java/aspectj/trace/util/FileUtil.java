package aspectj.trace.util;

import aspectj.trace.core.compiler.AjcCompiler;

import javax.swing.*;
import java.io.*;

/**
 * FileUtil
 * 文件工具类
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 4/2/16 8:35 PM.
 */
public class FileUtil {

    private static String MESSAGE = "";

    private String classPathDir;
    private String outFilePath;
    private String projectDir;

    FileUtil() {
        // 初始化文件的路径
        File classPath = new File(this.getClass().getResource("/").getPath());
        File targetPath = new File(classPath.getParent());
        File projectPath = new File(targetPath.getParent());

        projectDir = targetPath.getParent();
        classPathDir = classPath.getAbsolutePath();
        outFilePath = projectPath.getAbsolutePath() + "/out";
    }

    public String getClassPathDir() {
        return classPathDir;
    }

    public void setClassPathDir(String classPathDir) {
        this.classPathDir = classPathDir;
    }

    public String getOutFilePath() {
        return outFilePath;
    }

    public void setOutFilePath(String outFilePath) {
        this.outFilePath = outFilePath;
    }

    public String getProjectDir() {
        return projectDir;
    }

    public void setProjectDir(String projectDir) {
        this.projectDir = projectDir;
    }

    /**
     * 复制单个文件
     *
     * @param srcFileName  待复制的文件名
     * @param destFileName 目标文件名
     * @param overlay      如果目标文件存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String destFileName,
                                   boolean overlay) {
        File srcFile = new File(srcFileName);

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            MESSAGE = "源文件：" + srcFileName + "不存在！";
            JOptionPane.showMessageDialog(null, MESSAGE);
            return false;
        } else if (!srcFile.isFile()) {
            MESSAGE = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";
            JOptionPane.showMessageDialog(null, MESSAGE);
            return false;
        }

        // 判断目标文件是否存在
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(destFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制整个目录的内容
     *
     * @param srcDirName  待复制目录的目录名
     * @param destDirName 目标目录名
     * @param overlay     如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(String srcDirName, String destDirName,
                                        boolean overlay) {
        // 判断源目录是否存在
        File srcDir = new File(srcDirName);
        if (!srcDir.exists()) {
            MESSAGE = "复制目录失败：源目录" + srcDirName + "不存在！";
            JOptionPane.showMessageDialog(null, MESSAGE);
            return false;
        } else if (!srcDir.isDirectory()) {
            MESSAGE = "复制目录失败：" + srcDirName + "不是目录！";
            JOptionPane.showMessageDialog(null, MESSAGE);
            return false;
        }

        // 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        File destDir = new File(destDirName);
        // 如果目标文件夹存在
        if (destDir.exists()) {
            // 如果允许覆盖则删除已存在的目标目录
            if (overlay) {
                new File(destDirName).delete();
            } else {
                MESSAGE = "复制目录失败：目的目录" + destDirName + "已存在！";
                JOptionPane.showMessageDialog(null, MESSAGE);
                return false;
            }
        } else {
            // 创建目的目录
            System.out.println("目的目录不存在，准备创建。。。");
            if (!destDir.mkdirs()) {
                System.out.println("复制目录失败：创建目的目录失败！");
                return false;
            }
        }

        boolean flag = true;
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 复制文件
            if (files[i].isFile()) {
                flag = FileUtil.copyFile(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!flag)
                    break;
            } else if (files[i].isDirectory()) {
                flag = FileUtil.copyDirectory(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            MESSAGE = "复制目录" + srcDirName + "至" + destDirName + "失败！";
            JOptionPane.showMessageDialog(null, MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 注释掉一个文件的包名
     *
     * @param fileName
     * @return
     */
    public static boolean removePackageName(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String tempString = null;
            int line = 0;
            int lineCnt = 0;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                tempString = tempString.trim();
                if (tempString.contains("package")) {//包含package则替换
                    break;
                }
                line++;
                lineCnt += tempString.length();
            }
            // 注释掉含有package的那一行
            appendFile(line, fileName, "//");
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 在制定的行写入内容
     *
     * @param position 行号
     * @param fileName 文件名
     * @param content  写入内容
     */
    private static void appendFile(long position, String fileName, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件头
            randomFile.seek(position);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向指定文件写入内容
     *
     * @param fileName
     * @param content
     * @return
     */
    public static boolean writeToFile(String fileName, String content) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(content);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 获取文件内容
     *
     * @param fileName
     * @return
     */
    public static String getFileContent(String fileName) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                builder.append(tempString);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * 读文件内容
     *
     * @param fileName 文件名
     * @return 文件内容
     */
    public static String readFileContent(String fileName) {
        String content = "";
        try {
            InputStream inputStream = new FileInputStream(fileName);
            byte[] result = new byte[inputStream.available()];
            inputStream.read(result);
            content = new String(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 载入流中的内容
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        while ((ptr = in.read()) != -1) {
            buffer.append((char) ptr);
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        AjcCompiler ajcCompiler = new AjcCompiler();
        FileUtil.removePackageName(ajcCompiler.getOutFilePath() + "/App.java");
    }
}
