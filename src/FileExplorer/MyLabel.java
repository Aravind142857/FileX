package FileExplorer;

import org.junit.internal.runners.statements.FailOnTimeout;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MyLabel extends JButton {// Replace with JLabel for old version
    public MyLabel(String text, FileX exp) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 16));
        if (new File(exp.getWorkingPath() + File.separator + text).isDirectory()) {//text.lastIndexOf(".") == -1) {      /** If File is a Directory */
            setForeground(new Color(134, 11, 255));
        }
    }

}
