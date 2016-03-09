package com.huntdreams.asjectj;

import aspectj.in.action.ch05.aspect.IndentedLogging;
import org.aspectj.lang.Signature;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * CallApp Demo
 *
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午4:40.
 */
public aspect CallApp  extends IndentedLogging {

    // 拦截 bar 的执行
    pointcut barPoint(): execution(* bar());

    // 拦截 foo 的执行
    pointcut fooPoint(): execution(* foo());

    // cflow的参数是一个pointcut
    pointcut barCFlow(): cflow(barPoint()) && !within(CallApp);

    // 获取bar流程内的foo的方法的调用
    pointcut fooInBar(): barCFlow() && fooPoint();

//    pointcut callApp(): call(* foo(int, String));
//
//    before(): barCFlow(){
//        System.out.println("Enter:" + thisJoinPoint);
//    }
//
//    before(): callApp() {
//        System.out.println("In Aspect CallApp ------>>>");
//        System.out.println("Sigunature:" + thisJoinPoint.getStaticPart().getSignature());
//        System.out.println("Source Line:" + thisJoinPoint.getStaticPart().getSourceLocation());
//        System.out.println("<<<--- Out Aspect CallApp ------");
//    }



    protected pointcut loggedOperations():
//            (execution(* *.*(..))
//            || execution(*.new(..)))
            cflow(barPoint()) && !within(IndentedLogging+);

    before(): loggedOperations() {

        String methodName = thisJoinPoint.getStaticPart().getSignature().toString();
        String sourceLine = thisJoinPoint.getStaticPart().getSourceLocation().toString();
        System.out.println("-->" + methodName + " at " + sourceLine);
//
//        System.out.println("Sigunature:" + thisJoinPoint.getStaticPart().getSignature());
//        System.out.println("Source Line:" + thisJoinPoint.getStaticPart().getSourceLocation());
//        Signature sig = thisJoinPointStaticPart.getSignature();
//        System.out.println("Entering ["
//                + sig.getDeclaringType().getName() + "."
//                + sig.getName() + "]");
    }
}