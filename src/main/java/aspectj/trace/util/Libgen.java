package aspectj.trace.util;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Libgen
 * <p/>
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 4/30/16 9:42 AM.
 */
public class Libgen {

    Logger logger = Logger.getLogger(getClass());

    private void download() {
        String fileName = "/Volumes/Mac/Libgen/repository_torrent/magnet-links.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String cmd;
                line = line.split("magnet")[0];
                line = line.trim();
                line = "http://libgen.io/repository_torrent/" + line;
                cmd = "wget -P /Volumes/Mac/Libgen/repository_torrent " + line;
                ExecUtil.exec(cmd);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Libgen libgen = new Libgen();
        libgen.download();
    }
}
