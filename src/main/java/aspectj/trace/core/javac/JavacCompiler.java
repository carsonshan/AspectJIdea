package aspectj.trace.core.javac;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * JavacCompiler
 * 模拟javac编译
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 4/1/16 5:17 PM.
 */
public class JavacCompiler {

    private String codePath = "src/main/java/aspectj/trace/core/app/App.java";

    //window下面的换行是\r\t 回车换行
    public static void main(String[] args) throws Exception {

        //得到系统当前的路径,即是java工程的路径(E:\workspace\base)
        System.out.println(System.getProperty("user.dir"));
        //在程序里对java文件进行编译，也应该就是动态编译了
        //writejavaFile();
        //得到系统当前的java编译器，也就是javac
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        //不能是jre的运行环境，因为这个是纯净版本，不包含javac等，需要使用jdk才行,否则会是null
        //先得到一个文件管理对象
        //该对象的第一个参数是诊断监听器,
        StandardJavaFileManager javafile = javac.getStandardFileManager(null, null, null);
        //String filename = "/Users/noprom/Documents/Dev/Java/Pro/IdeaPro/AspectJIdea/src/main/java/aspectj/trace/core/app/App.java";
        String filename = "/Users/noprom/Desktop/App.java";
        //编译单元，可以有多个
        Iterable units = javafile.getJavaFileObjects(filename);
        //编译任务
        JavaCompiler.CompilationTask t = javac.getTask(null, javafile, null, null, null, units);
        t.call();
        javafile.close();

        ////把刚才在D:/下生成的class文件JavaTest.class加载进内存并生成实例对象
        URL[] urls = new URL[]{new URL("file:/Users/noprom/Desktop")};
        URLClassLoader classload = new URLClassLoader(urls);
        Class clazz = classload.loadClass("aspectj.trace.core.app.App");
        Method method = clazz.getMethod("main", String[].class);

        //注意，调用Method类的方法invoke(Object,Object), main方法是类Run的静态方法，调用时是不需要对象实例的。
        //Java中通过反射调用其他类中的main方法时要注意的问题
        //http://www.cnblogs.com/duancanmeng/archive/2012/04/14/2524614.html
        method.invoke(clazz.newInstance(), (Object) new String[]{});

    }

    //创建java文件
    public static void writejavaFile() throws IOException {
        File file = new File("~/Desktop/JavaTest.java");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        fw.write(javaClassContent());
        fw.close();
    }

    //用字符串连接成一个java类
    public static String javaClassContent() {
        String rt = "\r\n";
        String java = new String();
        java += "public class JavaTest{" + rt;
        java += " public static void main(String[] args){" + rt;
        java += "		System.out.println(\"hello world\");" + rt;
        java += "		show();" + rt;
        java += "	}" + rt;
        java += "	public static void show(){" + rt;
        java += "		for(int i=0;i<4;i++){" + rt;
        java += "			System.out.println(\"i=:\"+i);" + rt;
        java += "		}" + rt;
        java += "	}" + rt;
        java += "}";
        return java;
    }

    /**
     * 运行结果：
     * hello world
     * i=:0
     * i=:1
     * i=:2
     * i=:3
     */
}
