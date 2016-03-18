package aspectj.trace.code.asjectj;


import java.io.*;
import java.util.Stack;

/**
 * 利用aspectj打印程序调用顺序
 *
 * Created by syc on 3/18/16.
 */
public aspect AspectJ {

    // docpath
    private String docPath;

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

    AspectJ() {
        // 初始化文件的路径
        // 初始化路径信息
        File classPath = new File(this.getClass().getResource("/").getPath());
        File targetPath = new File(classPath.getParent());
        File docPath = new File(targetPath.getParent());
        this.docPath = docPath.getAbsolutePath() + "/doc";
        this._SavePath = docPath + "/out.txt";

        //程序入口，一般为main
        _Stack.push("MAIN");

        //创建输出流
        try {
            _OS = new FileOutputStream(_SavePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    pointcut anyFunction(): execution(* *.*(..));

    pointcut loggedOperations(): ((cflow(anyFunction())) && !within(AspectJ));

    before(): loggedOperations(){
        _indentationLevel++;

        String name = thisJoinPoint.getStaticPart().getSignature().getName();
        if (name.equals("println") || name.equals("out"))
            return;

        String declKind = thisJoinPoint.getKind();

        int size = _Stack.size();
        if (declKind.equals("method-call")) {
            Object signature = thisJoinPoint.getSignature();

            if (!_Stack.empty()) {
                System.out.println(_Stack.peek() + " --> " + signature);

                //写入文件
                try {
                    _OS.write((_Stack.peek() + " --> " + signature + "\n").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
//                System.out.println("main" + " --> " + signature);
//                try {
//                    _OS.write(("main" + " --> " + signature + "\n").getBytes());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
            _Stack.push(signature);
        } else {
            System.out.println(thisJoinPoint.toString() + "\t" + thisJoinPoint.getStaticPart().getSourceLocation().toString());

            //写入文件
            try {
                _OS.write((thisJoinPoint.toString() + "\t" + thisJoinPoint.getStaticPart().getSourceLocation().toString()
                        + "\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    before(): call(* java.io.PrintStream.println(..)) && within(AspectJ+) {
        for (int i = 0, spaces = _indentationLevel * 4; i < spaces; ++i) {
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