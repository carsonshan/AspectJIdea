package aspectj.trace.core.aspectj;

import aspectj.trace.core.compiler.AjcCompiler;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Stack;

/**
 * 利用aspectj打印程序调用顺序
 * <p/>
 * Author: syc <522560298@qq.com>
 * Date: 16/3/18 下午5:23
 */
public aspect TraceApp {

    Logger logger = Logger.getLogger(getClass());

    // docpath
    private String outFilePath;

    /**
     * 写入的文件位置，如果要标准化的话就用 File.separator 定义
     * */
    private String _SavePath;

    private int _indentationLevel = 0;

    /**
     * 程序调用记录栈
     */
    private Stack<Object> _Stack = new Stack<Object>();

    /**
     * 标准文件输出流
     */
    private OutputStream _OS;

    TraceApp() throws IOException {
        AjcCompiler ajcCompiler = new AjcCompiler();
        // 初始化文件的路径
        File classPath = new File(this.getClass().getResource("/").getPath());
        this._SavePath = classPath.getAbsolutePath() + "/out.txt";
        //程序入口，一般为main
        _Stack.push("main");
        //创建输出流
        try {
            _OS = new FileOutputStream(_SavePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    pointcut anyFunction(): execution(* *.*(..));

    pointcut loggedOperations(): ((cflow(anyFunction())) && !within(aspectj.trace.core.aspectj.TraceApp));

    before(): loggedOperations() {
        _indentationLevel++;

        String name = thisJoinPoint.getStaticPart().getSignature().getName();
        if (name.equals("println") || name.equals("out"))
            return;

        String declKind = thisJoinPoint.getKind();
        int size = _Stack.size();
        if (declKind.equals("method-call")) {
            Object signature = thisJoinPoint.getSignature();

            if (!_Stack.empty()) {
                System.out.println(_Stack.peek() + " --> " + name);

                //写入文件
                try {
                    _OS.write((_Stack.peek() + " --> " + name + "\n").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
            }
            _Stack.push(name);
        } else {
            
        }
    }

    after(): loggedOperations(){
        _indentationLevel--;
        String name = thisJoinPoint.getStaticPart().getSignature().getName();
        if (name.equals("println") || name.equals("out"))
            return;

        String declKind = thisJoinPoint.getKind();
        int size = _Stack.size();
        if (declKind.equals("method-execution")) {
            if (!_Stack.empty())
                _Stack.pop();
        }
    }

    before(): call(* java.io.PrintStream.println(..)) && within(aspectj.trace.core.aspectj.TraceApp+) {
        for (int i = 0, spaces = _indentationLevel*4 ; i < spaces; ++i) {
            System.out.print(" ");
            //写入文件
            try {
                _OS.write((" ").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}