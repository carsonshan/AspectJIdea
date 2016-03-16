package aspectj.trace.code.asjectj;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * TraceApp Demo
 *
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午4:40.
 */
public aspect TraceApp extends IndentedLogging {

    protected String outFilePath = docPath + "/out.txt";

    // 获取main函数的执行
    pointcut mainPoint(): execution(* main(..));

    after(): mainPoint() {
//        try {
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    protected pointcut loggedOperations():
//            (execution(* *.*(..))
//            || execution(*.new(..)))
            cflow(mainPoint()) && !within(IndentedLogging+);

    before(): loggedOperations() {
        String methodName = thisJoinPoint.getStaticPart().getSignature().toString();
        String sourceLine = thisJoinPoint.getStaticPart().getSourceLocation().toString();
        // 打印到控制台
        System.out.print(this.caller + " ");
        System.out.println("-->" + methodName + " at " + sourceLine);
//
//        if (!methodName.contains("java")) {
//            if (this.caller != null
//                //&& !this.caller.contains("init")
//                    ) {
//                try {
//                    writer = new FileWriter(new File(outFilePath));
//                    writer.append(this.caller + " ");
//                    writer.append("-->" + methodName + " at " + sourceLine + "\n");
//
//                    // 打印到控制台
//                    System.out.print(this.caller + " ");
//                    System.out.println("-->" + methodName + " at " + sourceLine);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        writer.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
    }
}