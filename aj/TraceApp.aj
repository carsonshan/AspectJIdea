package aspectj.trace.code.asjectj;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.SyncFailedException;

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
            (execution(* *.*(..)) ||
//            || execution(*.new(..)))
            (cflow(mainPoint()) && !within(IndentedLogging+) ));

    before(): loggedOperations() {
        // 调用者信息
        String methodName = thisJoinPoint.getStaticPart().getSignature().toString();
        String sourceLine = thisJoinPoint.getStaticPart().getSourceLocation().toString();

        if (this.caller != null && methodName != null
                && !this.caller.equals("println")
                && !this.caller.equals("out")
                && !methodName.contains("out")) {
                 // 打印到控制台
                //System.out.print(this.caller + " ");
                System.out.println("调用者-->" + methodName + " at " + sourceLine);

            // 被调用者信息
            Object target = thisJoinPoint.getTarget();
            if (target != null) {
                System.out.println("目标-->" + target);
            }

        }

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