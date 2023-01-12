package UIDesign;
import FileExplorer.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Runner extends JFrame {
    private final Color LEFT_PANE_FG_COLOR = Color.getHSBColor(40, 255, 120);
    private final Color LEFT_PANE_COLOR = new Color(10,200,150);
    private final Color TOP_PANE_COLOR = new Color(95, 187, 249);
    private final Color BOTTOM_PANE_BG_COLOR = Color.black;
    private final Color BOTTOM_PANE_FG_COLOR = Color.BLACK;
    public final Color DIRECTORY_COLOR = Color.BLUE;
    public final Color FILE_COLOR = Color.GREEN;
    public final Color HIDDEN_COLOR = Color.BLACK;
    JPanel leftPane;
    JPanel topPane;
    ColorPane bottomPane;
    ColorPane textBox;
    Main explorer;
    JFrame tempFrame;
    FileX FileXBottomPanel = new FileX();
    FileX FileXTopPanel = new FileX();
    public int x = 0;
    public Runner(Main main) {

        explorer = main;

        /** Creating Condensed View Panel = Panel 3 */
        //leftPane = new JPanel();
        leftPane = new JPanel();
        //leftPane.setForeground(LEFT_PANE_FG_COLOR);
        leftPane.setBackground(LEFT_PANE_COLOR);
        leftPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //JToolBar
        Container container = new Container();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        /** Creating Expanded Visualizer = Panel 1 */
        topPane = new JPanel();
        topPane.setBackground(TOP_PANE_COLOR);
        JScrollPane topScroll = new JScrollPane(topPane);
        //JPanel topBar = new JPanel();\
        /** Add topBar */
        JToolBar topBar = new JToolBar();
        topBar.setFloatable(false);
        /** Add BackButton */
        BackButton btn = new BackButton("Back");
        putButtons(btn);
        topBar.add(btn);
        /** Add other tools */
        //topScroll.add(topBar, BorderLayout.NORTH);
        topPane.setLayout(new BoxLayout(topPane, BoxLayout.PAGE_AXIS));
        topScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //topPane.setLayout(new BoxLayout());
        container.add(topBar, BorderLayout.NORTH);
        container.add(topScroll);
        /** Creating Terminal view = Panel 2 */
        bottomPane = new ColorPane();
        bottomPane.setBackground(BOTTOM_PANE_BG_COLOR);
        //bottomPane.setForeground(BOTTOMPANEFGCOLOR);
        //bottomPane.setContentType("text/html");
        //bottomPane.setLineWrap(true);
        bottomPane.setEditable(false);
        bottomPane.insert(explorer.getPrompt());


        /** Adding a ScrollPane to bottomPane */
        JScrollPane spB = new JScrollPane(bottomPane);
        spB.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spB.getVerticalScrollBar().setBackground(Color.RED);
        spB.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.GREEN;
            }
        });

        /** Creating Text Box */
        textBox = new ColorPane();
        //textBox.setRows(1);
        textBox.addKeyListener(new KeyListener() {
            StringBuilder command = new StringBuilder();
            int startPressed = 0;

            /**
             * Invoked when a key has been typed.
             * See the class description for {@link KeyEvent} for a definition of
             * a key typed event.
             *
             * @param e the event to be processed
             */
            @Override
            public void keyTyped(KeyEvent e) {
                /*if (!bottomPane.hasFocus()) {
                    bottomPane.requestFocus();

                }*/
                int cursorIndex = textBox.getCaretPosition();
                //System.out.println(1 + " " + cursorIndex + " " + command);
                //System.out.println(cursorIndex);
                if (e.getExtendedKeyCode() != 10 && e.getExtendedKeyCode() != 8 && e.getExtendedKeyCode() != 9) {
                    if (cursorIndex == command.length()) {
                        command.append(e.getKeyChar());
                    } else {
                        command = new StringBuilder(command.substring(0, cursorIndex) + e.getKeyChar() + command.substring(cursorIndex));
                    }
                }
            }

            /**
             * Invoked when a key has been pressed.
             * See the class description for {@link KeyEvent} for a definition of
             * a key pressed event.
             *
             * @param e the event to be processed
             */
            @Override
            public void keyPressed(KeyEvent e) {
                startPressed = textBox.getCaretPosition();
            }

            /**
             * Invoked when a key has been released.
             * See the class description for {@link KeyEvent} for a definition of
             * a key released event.
             *
             * @param e the event to be processed
             */
            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println(2 + " " + textBox.getCaretPosition() + " " + startPressed + " " + command);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  /** Print the user inputted command to the console and then print the output of that command */
                    //System.out.println("Command entered is: " + command + "...");
                    outCommandToTextArea(command.toString());
                    command = new StringBuilder();
                    textBox.setText("");
                } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {  /** Delete last user typed character */
                    //int cursorIndex = textBox.getCaretPosition();
                    if (startPressed > 0) {
                        //command = command.substring(0, startPressed) + command.substring(cursorIndex + 1);
                        command.deleteCharAt(startPressed - 1);
                    } else {
                        //command = "";
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_TAB) { /** Auto-complete */
                    ArrayList<String> temp = FileXBottomPanel.autocompleteFromIncompleteString(command.toString());
                    if (temp.size() == 1) {
                        String only = temp.get(0);
                        command = new StringBuilder(only);
                        textBox.setText(only);
                    } else if (temp.size() > 1){
                        //TODO: Maybe open a new frame with some implementation of showing possible strings.
                        //TODO: Problem: Not able to trap tab key. Need some other way to override its function.
                        tempFrame = new JFrame();
                        GridLayout layout = new GridLayout(0, 3);
                        JTextArea area = new JTextArea();
                        area.setLayout(layout);
                        temp.stream().map(s -> area.add(new JLabel(s)));
                        tempFrame.add(area);
                        tempFrame.setVisible(true);
                        tempFrame.requestFocus();
                    }
                // if multiple possible files then print them, else change the halfcompleted user input file to file.
                    // command.replace(input, file);
                }
                //textBox.requestFocus();
            }

        });
        textBox.addFocusListener(new FocusListener() {

            /**
             * Invoked when a component gains the keyboard focus.
             *
             * @param e the event to be processed
             */
            @Override
            public void focusLost(FocusEvent e) {
                if (textBox.getText().trim().equals("")) {
                    textBox.setText("");
                    textBox.insert("u001B[1;30m" + "Enter a command or 'help'." + "u001B[30m");
                    textBox.setCaretPosition(0);
                } else {
                    //ignore
                }

            }

            /**
             * Invoked when a component loses the keyboard focus.
             *
             * @param e the event to be processed
             */
            @Override
            public void focusGained(FocusEvent e) {
                if (tempFrame != null && tempFrame.isVisible()) {
                    tempFrame.setVisible(false);
                }
                if (textBox.getText().trim().equals("Enter a command or 'help'.")) {
                    textBox.setText(null);
                    textBox.setForeground(Color.BLACK);
                } else {
                    // ignore
                }

            }
        });
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        /**
                                ________________________________________________
                                |X____|____________FILEX_WINDOW_1______________|
                                |     |__-path-__|                             |
                                |     |                                        |
                                |     |          Expanded File Visualizer      |
                                |     |                                        |
                                |     |                                        |
                                |     |________________________________________|
        Condensed File          |     |            Terminal Output             |
            Visualizer          |     |                                        |
                                |     |________________________________________|
                                |     |           Terminal Input               |
                                |_____|________________________________________|
         */

        /** Splitting Panes to match the desired view */
        JPanel panel = new JPanel();
        TitledBorder title = new TitledBorder("text box");
        title.setTitleJustification(TitledBorder.CENTER);
        panel.setLayout(new BorderLayout());
        spB.setSize(525, 275);
        textBox.setBorder(title);
        JSplitPane splitBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spB, textBox);
        //splitBottom.setBorder(null);
        splitBottom.setDividerLocation(270);
        JSplitPane split1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, container, splitBottom);
        split1.setDividerLocation(375);
        split1.setBorder(null);

        JScrollPane spL = new JScrollPane(leftPane);
        leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.PAGE_AXIS));
        for (JButton lbl: makeButtons(explorer.runCommand("ls -d */"))) {
            leftPane.add(lbl);
            leftPane.add(Box.createRigidArea(new Dimension(5, 2)));
        }
        //leftPane.setVisible(true);
        JSplitPane split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spL, split1);

        spL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spL.getVerticalScrollBar().setBackground(Color.RED);
        spL.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.GREEN;
            }
        });

        split2.setDividerLocation(225);
        split2.setBorder(null);
        add(split2);

        /** JFrame Preferences */
        setSize(new Dimension(750, 750));
        setTitle("FileX window 1");

        /** Make JFrame visible */
        setVisible(true);
    }
    private void putButtons(JButton btn) {
        btn.addActionListener(e -> {
            String directory = btn.getText();
            try {
                String curr = FileXTopPanel.getWorkingPath();
                    if (e.getSource() instanceof MyLabel) {
                        FileXTopPanel.moveInward(directory);
                    } else if (e.getSource() instanceof BackButton) {
                        FileXTopPanel.moveOutward();
                    } else {
                        FileXTopPanel.toHome();
                        FileXTopPanel.moveInward(directory);
                    }
                    if (!FileXTopPanel.getWorkingPath().equals(curr)) {
                        for (Component cmp : topPane.getComponents()) {
                            if (cmp instanceof MyLabel lbl) {
                                topPane.remove(lbl);
                            }
                        }
                        String result = FileXTopPanel.print(FileXTopPanel.getWorkingPath(), false);
                        for (MyLabel label : makeLabels(result)) {
                            topPane.add(label);
                            putButtons(label);
                        }
                        topPane.revalidate();
                        topPane.repaint();
                    }

            } catch (IOException ex) {
                System.out.println("OOPS!?");
            } catch (MyException ex) {
                ex.printStackTrace();
            }
        });
    }
    private ArrayList<MyButton> makeButtons(String cdOutput) {
        ArrayList<MyButton> listOfFiles = (ArrayList<MyButton>) Arrays.stream(cdOutput.split("\n")).map(s -> new MyButton(s)).collect(Collectors.toList());
        for (MyButton btn: listOfFiles) {
            putButtons(btn);
        }
        return listOfFiles;

    }
    private ArrayList<MyLabel> makeLabels(String cdOutput) {
        ArrayList<MyLabel> listOfFiles = (ArrayList<MyLabel>) Arrays.stream(cdOutput.split("\n")).map(s -> new MyLabel(s, FileXTopPanel)).collect(Collectors.toList());
        return listOfFiles;
    }
    private void outCommandToTextArea(String command) {
        try {
            bottomPane.insert(command + "\n");
            String s = explorer.runCommand(command);
            bottomPane.insert(s + "\n");
            TimeUnit.MILLISECONDS.sleep(250);
            bottomPane.insert(explorer.getPrompt());
        } catch (InterruptedException e) {
        }
    }


    public static void main(String[] args) {
        Main explorer = new Main();
        Runner window = new Runner(explorer);
    }
}

/**
 * Holding backspace
 * CMD + Backspace
 * CMD + A
 * CTRL + C to stop code
 *
 */
/* TODO:    Put all folders first then files, maybe remove all hidden files.
            0) Enable option so that user can say change color of elements from terminal
            1) Sort items in topPane by type
            6) Implement grid like structure to add storage and file extension in different columns
            2) Add back button in topPane
            3) ADD MENU BAR, with preferences.
            4) Reduce height of leftPane and add Small Box to put a logo inside.
            5) Add a bar at the top of topPane Panel that displays location of subdirectory relative to home
            7) Maybe add icons in topPane to symbolize Folder/file
            Welcome prompts in terminal
            Options that navigates in topPane and leftPane using terminal?
            Try to prevent topPane from giving exception when navigating into file in putButtons method.

 */