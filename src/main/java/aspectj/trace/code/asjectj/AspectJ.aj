package aspectj.trace.code.asjectj;


import java.util.Stack;

/**
 * Created by syc on 3/18/16.
 */
public aspect AspectJ {

    protected int _indentationLevel = 0;

    pointcut anyFunction(): execution(* *.*(..));

    pointcut loggedOperations(): ((cflow(anyFunction())) && !within(AspectJ));

    Stack<Object> _Stack = new Stack<Object>();

    before(): loggedOperations(){
        _indentationLevel++;

        String name = thisJoinPoint.getStaticPart().getSignature().getName();
        if (name.equals("println") || name.equals("out"))
            return;

        String declKind = thisJoinPoint.getKind();

        int size = _Stack.size();
        if (declKind.equals("method-call")) {
            Object signature = thisJoinPoint.getSignature();

            if (!_Stack.empty())
                System.out.println(_Stack.peek() + " --> " + signature + "\tstact size" + size);
            else {
                System.out.println("main()" + " --> " + signature);
            }
            _Stack.push(signature);
        } else {
            System.out.println(thisJoinPoint.toString() + "\t" + thisJoinPoint.getStaticPart().getSourceLocation().toString());
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

    before():
            call(* java.io.PrintStream.println(..)) &&
                    within(AspectJ+) {
        for (int i = 0, spaces = _indentationLevel * 4;
             i < spaces; ++i) {
            System.out.print(" ");
//            try {
//                writer.append(" ");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
}
