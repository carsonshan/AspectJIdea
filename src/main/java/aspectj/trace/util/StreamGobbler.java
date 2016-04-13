package aspectj.trace.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * StreamGobbler
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 4/13/16 11:44 PM.
 */
public class StreamGobbler extends Thread {

    InputStream is;
    String type;

    public StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (type.equals("Error")) {
                    System.out.println("Error	:" + line);
                } else {
                    System.out.println("Debug:" + line);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
