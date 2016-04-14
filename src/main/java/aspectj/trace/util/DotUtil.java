package aspectj.trace.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

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
    private FileUtil fileUtil = new FileUtil();

    /**
     * 生成dot代码
     */
    public void generateDotCode(String fileName) {
        FileUtil fileUtil = new FileUtil();
        String callSeqFile = fileUtil.getProjectDir() + "/out_dot.txt";
        String callSeqFileWithNumber = fileUtil.getProjectDir() + "/out_dot_number.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(callSeqFile));
            FileWriter numberWriter = new FileWriter(callSeqFileWithNumber);
            File numberFile = new File(callSeqFileWithNumber);
            if (!numberFile.exists()) {
                numberFile.createNewFile();
            }

            String line = "";
            Integer number = 0;
            while ((line = reader.readLine()) != null) {
                number++;
                numberWriter.write(line + "[label=" + number + "];\n");
            }
            numberWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读入文件
        String callSeqFileContent = FileUtil.readFileContent(callSeqFileWithNumber);
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
                    "    edge [color=\"sienna\" fontcolor=\"red\"]\n" + callSeqFileContent + "\n}";
            writer.write(dotContent);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘图并打开
     *
     * @param srcFileName
     * @param destFileName
     */
    public void plotDot(String srcFileName, String destFileName) {
        String execStr = "dot -Tpng " + srcFileName + " -o " + destFileName;
        ExecUtil.exec(execStr);
        execStr = "open " + destFileName;
        ExecUtil.exec(execStr);
    }

    /**
     * 生成匹配之后的dot代码
     *
     * @param matchedResult   匹配的结果
     * @param destDotFileName 目标dot代码文件名
     * @param fuzzySearch     是否是模糊查询
     */
    public void generateMatchedDotCode(Set<Pair<NodeUtil, List<List<NodeUtil>>>> matchedResult,
                                       String destDotFileName, Boolean fuzzySearch) {
        String callSeqFile = fileUtil.getProjectDir() + "/out_dot.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(callSeqFile));
            // 得到所有的调用序列,并且初始化匹配为false
            List<Pair<Pair<String, String>, Boolean>> allCallList = new ArrayList<Pair<Pair<String, String>, Boolean>>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.substring(0, line.length() - 1);
                String[] seqs_t = line.split("(->|\\s+)");
                List<String> seqs_t2 = new ArrayList<String>();
                for (String s : seqs_t) {
                    if (s.equals("")) {
                        continue;
                    }
                    seqs_t2.add(s);
                }
                String[] seqs = new String[]{seqs_t2.get(0), seqs_t2.get(1)};
                Pair<Pair<String, String>, Boolean> pair = new Pair<Pair<String, String>, Boolean>(
                        new Pair<String, String>(seqs[0], seqs[1]), false
                );
                allCallList.add(pair);
            }
            // 遍历得到匹配的序列
            List<Pair<String, String>> matchedList = new ArrayList<Pair<String, String>>();
            for (Pair<NodeUtil, List<List<NodeUtil>>> result_pair : matchedResult) {
                for (List<NodeUtil> c : result_pair.second) {
                    final int c_size_nl = c.size() - 1;
                    for (int i = 0; i < c_size_nl; ++i) {
                        matchedList.add(new Pair<String, String>(c.get(i).getName(), c.get(i + 1).getName()));
                    }
                }
            }
            // 得到唯一的除去重复的调用序列
            // 比如有:A1->B2,B2->B3,A1->B2
            // 那么除去重复之后:A1->B2,B2->B3
            Map<String, Boolean> uniqueCallMap = new HashMap<String, Boolean>();
            for (Pair<Pair<String, String>, Boolean> pair : allCallList) {
                String key = pair.first.first + "->" + pair.first.second;
                Boolean exist = uniqueCallMap.get(key);
                if (exist == null) {
                    uniqueCallMap.put(key, pair.second);
                }
            }

            // 在所有的调用序列中标记已经匹配到的
            StringBuilder builder = new StringBuilder();
            StringBuilder nodeBuilder = new StringBuilder();
            for (Pair<Pair<String, String>, Boolean> pair : allCallList) {
                String commonBlock = pair.first.first + " -> " + pair.first.second + ";\n";
                Boolean match = false;
                for (Pair<String, String> matchedPair : matchedList) {
                    if (matchedPair.first.equals(pair.first.first)
                            && matchedPair.second.equals(pair.first.second)) {
                        // 是否是模糊查询
                        String redBlock = "";
                        if (fuzzySearch) {// 模糊查询显示虚线
                            // 匹配的只显示一次
                            String key = pair.first.first + "->" + pair.first.second;
                            Boolean alreadyMatch = uniqueCallMap.get(key);
                            if (alreadyMatch != null && !alreadyMatch) {
                                redBlock = matchedPair.first + " -> " + matchedPair.second + "[color=red, style = dotted];\n";
                                uniqueCallMap.put(key, true);
                            } else {
                                redBlock = matchedPair.first + " -> " + matchedPair.second + "[color=red];\n";
                            }
                        } else {
                            redBlock = matchedPair.first + " -> " + matchedPair.second + "[color=red];\n";
                        }

                        builder.append(redBlock);
                        nodeBuilder.append(matchedPair.first + "[color=red];\n");
                        nodeBuilder.append(matchedPair.second + "[color=red];\n");
                        match = true;
                    }
                }
                if (!match) {
                    builder.append(commonBlock);
                }
            }
            builder.append(nodeBuilder.toString());
            // 写入dot代码
            File file = new File(destDotFileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileWriter writer = new FileWriter(destDotFileName);
                String dotContent = "digraph G {\n" +
                        "    /*初始化节点和边的颜色*/\n" +
                        "    node [peripheries=2 style=filled color=\"#eecc80\"]\n" +
                        "    edge [color=\"sienna\" fontcolor=\"red\"]\n" + builder.toString() + "\n}";
                writer.write(dotContent);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileUtil fileUtil = new FileUtil();
        DotUtil dotUtil = new DotUtil();
        // 生成dot代码
        String dotName = fileUtil.getOutFilePath() + "/dot/match.dot";
        String destFileName = fileUtil.getOutFilePath() + "/dot/match.png";
        dotUtil.generateDotCode(dotName);
        dotUtil.plotDot(dotName, destFileName);
    }
}
