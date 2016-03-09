package aspectj.trace.code.asjectj;

import org.aspectj.lang.Signature;

/**
 * TraceApp Demo
 *
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午4:40.
 */
public aspect TraceApp extends IndentedLogging {

    // print的切点
    pointcut printCut(): execution(* java.io.PrintStream.println(..));

    // 拦截 bar 的执行
    pointcut barPoint(): execution(* bar());

    // 拦截 foo 的执行
    pointcut fooPoint(): execution(* foo());

    // cflow的参数是一个pointcut
    pointcut barCFlow(): cflow(barPoint()) && !within(TraceApp);

    // 获取bar流程内的foo的方法的调用
    pointcut fooInBar(): barCFlow() && fooPoint();

    //    pointcut callApp(): call(* foo(int, String));
//
//    before(): barCFlow(){
//        System.out.println("Enter:" + thisJoinPoint);
//    }
//
//    before(): callApp() {
//        System.out.println("In Aspect TraceApp ------>>>");
//        System.out.println("Sigunature:" + thisJoinPoint.getStaticPart().getSignature());
//        System.out.println("Source Line:" + thisJoinPoint.getStaticPart().getSourceLocation());
//        System.out.println("<<<--- Out Aspect TraceApp ------");
//    }

    protected pointcut loggedOperations():
//            (execution(* *.*(..))
//            || execution(*.new(..)))
            cflow(barPoint()) && !within(IndentedLogging+) && !within(printCut);

    before(): loggedOperations() {

        String methodName = thisJoinPoint.getStaticPart().getSignature().toString();
        String sourceLine = thisJoinPoint.getStaticPart().getSourceLocation().toString();
        if (!methodName.contains("java")) {
            System.out.println("-->" + methodName + " at " + sourceLine);
        }
//
//        Signature sig = thisJoinPointStaticPart.getSignature();
//        System.out.println("Entering ["
//                + sig.getDeclaringType().getName() + "."
//                + sig.getName() + "]");
    }
}