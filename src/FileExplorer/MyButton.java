package FileExplorer;

import javax.swing.JButton;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class MyButton extends JButton {
    public String result;
    FileX FileXTopPanel = new FileX();
    public MyButton(String s) {
        super(s);
        setFont(new Font("Arial", Font.BOLD, 16));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
    }

}
