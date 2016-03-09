package aspectj.trace.code.asjectj;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * IndentedLogging
 * 控制缩进
 *
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/7 下午10:01.
 */
public abstract aspect IndentedLogging {

    // logger
    protected Logger logger = Logger.getLogger(getClass().getName());


    // docpath
    protected String docPath;

    FileWriter writer;

    // init
    public IndentedLogging() {
        // 初始化路径信息
        File classPath = new File(this.getClass().getResource("/").getPath());
        File targetPath = new File(classPath.getParent());
        File docPath = new File(targetPath.getParent());
        this.docPath = docPath.getAbsolutePath() + "/doc";
    }

    protected int _indentationLevel = 0;

    protected abstract pointcut loggedOperations();
    protected String caller;// 方法调用者

    before(): loggedOperations() {
        _indentationLevel++;
        this.caller = thisJoinPoint.getStaticPart().getSignature().getName();
    }

    after(): loggedOperations() {
        _indentationLevel--;
        this.caller = thisJoinPoint.getStaticPart().getSignature().getName();
//        try {
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    before():
            call(* java.io.PrintStream.println(..)) &&
                    within(IndentedLogging+) {
        for (int i = 0, spaces = _indentationLevel * 4;
             i < spaces; ++i) {
            System.out.print(" ");
            try {
                writer.append(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}