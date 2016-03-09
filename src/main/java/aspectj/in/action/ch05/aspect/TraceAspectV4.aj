package aspectj.in.action.ch05.aspect;

import org.aspectj.lang.Signature;

/**
 * TraceAspectV2
 *
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/7 下午8:31.
 */
public aspect TraceAspectV4
//        extends IndentedLogging
{
//    protected pointcut loggedOperations()
//            : (execution(* *.*(..))
//            || execution(*.new(..))) && !within(IndentedLogging+);
//
//    before(): loggedOperations() {
//        Signature sig = thisJoinPointStaticPart.getSignature();
//        System.out.println("Entering ["
//                + sig.getDeclaringType().getName() + "."
//                + sig.getName() + "]");
//    }
}