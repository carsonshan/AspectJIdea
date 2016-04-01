package aspectj.trace.core.app;

import org.apache.log4j.Logger;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.tools.ajc.Main;

import java.io.*;

/**
 * AjcCompiler
 * 使用aspectj自带的编译ajc来执行代码
 * 本程序仅作为测试程序使用
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 4/1/16 3:41 PM.
 */
public class AjcCompiler {

    private Logger logger = Logger.getLogger(getClass());

    private String destinationPath;//目标代码的编译路径

    private String ideaPath;//需要去除的idea自带的编译

    public AjcCompiler() {
        destinationPath = "/Users/noprom/Desktop";
        ideaPath = "/Applications/IntelliJ IDEA 15.app/Contents/lib/idea_rt.jar";
    }

    /**
     * 调用Ajc进行编译
     */
    public void compile() {
        // 调用ajc编译器来编译程序
        Main compiler = new Main();
        MessageHandler m = new MessageHandler();
        // sources.lst文件中存放的是要编译的java和aj文件列表,一行一个
        String defaultArgs[] = {"-argfile", "sources.lst", "-1.7"};
        compiler.run(defaultArgs, m);
    }

    /**
     * 运行程序并将结果存放到指定文件中
     *
     * @param fileName  保存结果的文件名
     * @param className 执行文件的类名
     */
    public void run(String fileName, String className) throws Exception {
        // 调用命令行来执行程序
        String JAVA_HOME = System.getenv("JAVA_HOME");
        String JAVA_PATH = JAVA_HOME + "/bin/java";
        String CLASSPATH = ".:" + destinationPath + ":" + System.getProperty("java.class.path");
        String execStr = JAVA_PATH + " -cp " + CLASSPATH + " " + className;
        // 去除idea的编译部分
        execStr = execStr.replace(":" + ideaPath, "");
        Process ps = Runtime.getRuntime().exec(execStr);
        // 得到执行的结果
        String resultStr = loadStream(ps.getInputStream());
        String errorStr = loadStream(ps.getErrorStream());
        logger.debug(resultStr);
        logger.error(errorStr);
        // 写入文件
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file);
        writer.write(resultStr);
        writer.close();
//        ps.wait();
//        ps.destroy();
    }

    /**
     * 载入流中的内容
     *
     * @param in
     * @return
     * @throws IOException
     */
    private String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        while ((ptr = in.read()) != -1) {
            buffer.append((char) ptr);
        }
        return buffer.toString();
    }

    public static void main(String[] args) throws Exception {
        AjcCompiler compiler = new AjcCompiler();
        compiler.compile();
        compiler.run("outxxx.txt", "App");
    }
}
/**
 * java -classpath ".:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/javafx-doclet.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/tools.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/htmlconverter.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Users/noprom/Documents/Dev/Java/Pro/IdeaPro/AspectJIdea/src/main/java/aspectj/trace/core/aspectj:/Users/noprom/Documents/Dev/Java/Pro/IdeaPro/AspectJIdea/target/classes:/Users/noprom/.m2/repository/com/intellij/forms_rt/5.0/forms_rt-5.0.jar:/Users/noprom/.m2/repository/org/aspectj/aspectjrt/1.6.7/aspectjrt-1.6.7.jar:/Users/noprom/.m2/repository/org/aspectj/aspectjtools/1.8.9/aspectjtools-1.8.9.jar:/Users/noprom/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:/Applications/IntelliJ IDEA 15.app/Contents/lib/idea_rt.jar" App
 */