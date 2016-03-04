package com.huntdreams.asjectj;

/**
 * CallApp Demo
 *
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午4:40.
 */
public aspect CallApp {

    pointcut callApp(): call(* foo(int, String));

    before(): callApp() {
        System.out.println("In Aspect CallApp ------>>>");
        System.out.println("Sigunature:" + thisJoinPoint.getStaticPart().getSignature());
        System.out.println("Source Line:" + thisJoinPoint.getStaticPart().getSourceLocation());
        System.out.println("<<<--- Out Aspect CallApp ------");
    }
}
