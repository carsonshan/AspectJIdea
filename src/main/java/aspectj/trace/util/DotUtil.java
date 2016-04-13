package aspectj.trace.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * DotUtil
 * 使用dot语言进行绘图
 * dot -Tpng 1.dot -o 1.png
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 4/13/16 8:27 PM.
 */
public class DotUtil {

    private Logger logger = Logger.getLogger(getClass());

    /**
     * 生成dot代码
     */
    public void generateDotCode(String fileName) {
        FileUtil fileUtil = new FileUtil();
        String callSeqFile = fileUtil.getProjectDir() + "/out_dot.txt";
        String callSeqFileContent = FileUtil.readFileContent(callSeqFile);
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter writer = new FileWriter(fileName);
            String dotContent = "digraph G {\n" +
                    "    /*初始化节点和边的颜色*/\n" +
                    "    node [peripheries=2 style=filled color=\"#eecc80\"]\n" +
                    "    edge [color=\"sienna\" fontcolor=\"green\"]\n" + callSeqFileContent + "\n}";
            writer.write(dotContent);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘图并且显示出来
     *
     * @param srcFileName
     * @param destFileName
     */
    public void plotDot(String srcFileName, String destFileName) {
        String execStr = "dot -Tpng " + srcFileName + " -o " + destFileName;
        try {
            Process ps = Runtime.getRuntime().exec(execStr);
            execStr = "open " + destFileName;
            Runtime.getRuntime().exec(execStr);
            logger.debug(execStr);
            // 得到执行的结果
            String resultStr = FileUtil.loadStream(ps.getInputStream());
            String errorStr = FileUtil.loadStream(ps.getErrorStream());
            logger.debug(resultStr);
            logger.error(errorStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DotUtil dotUtil = new DotUtil();
        FileUtil fileUtil = new FileUtil();
        String dotName = fileUtil.getOutFilePath() + "/dot/ori.dot";
        String outName = fileUtil.getOutFilePath() + "/dot/ori.png";
        dotUtil.generateDotCode(dotName);
        dotUtil.plotDot(dotName, outName);
    }
}
