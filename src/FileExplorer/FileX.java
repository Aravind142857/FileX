package FileExplorer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileX implements myExplorer {
    private String currPath;
    private String currDirectory;
    private String error1 = "File is not a directory";
    private String error2 = "File not found";
    public FileX() {
        currPath = System.getProperty("user.home");
        currDirectory = "~";
    }
    /**__________________________________________________________*/
    /**
     * Returns the Current Working Directory
     */
    @Override
    public String getWorkingPath() {
        return currPath;
    }
    /**__________________________________________________________*/
    @Override
    public String getWorkingDirectory() {
        return currDirectory;
    }
    /**__________________________________________________________*/
    /**
     * Changes the current working directory to
     *
     * @param newPath
     */
    @Override
    public void changeWorkingDirectory(String newPath){
            currPath = newPath;
            if (newPath.equals(System.getProperty("user.home"))) {
                currDirectory = "~";
            }
            else if (newPath.equals(Path.of(System.getProperty("user.home")).getRoot().toString())) {
                currDirectory = "/";
            }
            else {
                currDirectory = newPath.substring(newPath.lastIndexOf("/") + 1);
            }

    }
    /**__________________________________________________________*/
    /**
     * Moves inward and sets current Working Directory to @param inLoc
     *
     * @param inLoc
     */
    @Override
    public void moveInward(String inLoc) throws MyException {
        if (inLoc.charAt(0) == '/') {
            inLoc = inLoc.substring(1);
        }
        String newCurr = getWorkingPath() + "/" + inLoc;
        newCurr = (newCurr.lastIndexOf("~") >= 0) ? (newCurr.substring(newCurr.lastIndexOf("~")).replaceAll("~", System.getProperty("user.home"))):(newCurr);
        File fp = new File(newCurr);
        if (fp.isDirectory()) {
            if (inLoc.charAt(0) == '~') {
                inLoc = inLoc.substring(2);
                toHome();
            }
            changeWorkingDirectory(getWorkingPath() + "/" + inLoc.replaceAll("[.][/]", ""));
        }
        else if (fp.exists()){
            throw new MyException("cd", error1, fp);
        }
        else {
            throw new MyException("cd", error2, fp);
        }
    }
    /**__________________________________________________________*/
    /**
     * Moves outward and sets current Working Directory to inLoc
     *
     */
    @Override
    public void moveOutward() {
        changeWorkingDirectory(currPath.substring(0, currPath.lastIndexOf("/")));
    }
    /**__________________________________________________________*/
    /**
     * Return a list of all files in the current directory
     * @return
     */

    @Override
    public File[] getFiles(String currPath) {
        File currFile = new File(currPath);
        File[] toReturn = currFile.listFiles();
        if (toReturn != null) {
            Arrays.sort(toReturn);
        }
        return toReturn;
    }
    /**__________________________________________________________*/
    /**
     * Prints all files in the current directory
     *
     * @param currentPath
     * @return
     */
    @Override
    public String print(String currentPath, boolean format) throws IOException {
        File[] list = getFiles(currentPath);
        String toReturn = "";
        for (File f: list) {
            if (f.isDirectory()) {
                /* add format(f) + "\n" or convertFileToString(f) to start of toReturn */
                toReturn = ((format)?(format(f) + "\n") : convertFileToString(f)) + toReturn;
            } else {
                /* add format(f) + "\n" or convertFileToString(f) to end of toReturn */
                toReturn += ((format) ? (format(f) + "\n") : convertFileToString(f));
            }
        }
        toReturn = toReturn.substring(0, toReturn.length() - 1);
        return toReturn;
    }

    private String convertFileToString(File f) {
        if (f.isDirectory()) {
            return f.getName() + "\n";
        }
        else if (f.isFile()) {// && f.getName().lastIndexOf(".")!= -1) {
            String fileName = f.getName();
            return fileName + "\n"; //+ fileName.substring(fileName.lastIndexOf(".")) + "\n";
        }
        return "";
    }
    /** non-hidden directory? */
    private Predicate<File> isNonHiddenPredicate() {
        return p -> p.isDirectory() && !p.isHidden();
    }


    /**__________________________________________________________*/
    /** Return a String with all directories with current directory */
    public String printDirectories(String currentPath){
        File[] list = getFiles(currentPath);
        //list = Arrays.stream(list).filter(File::isDirectory).collect(Collectors.toList()).toArray(new File[]{});
        return String.join("\n",  Arrays.stream(list).toList().stream().filter(isNonHiddenPredicate()).map(f -> f.getName()).collect(Collectors.toList()).toArray(new String[]{}));
    }
    /**__________________________________________________________*/
    /**
     * Prints all files in current and sub-directories
     *  @param beginning
     * @param currentPath Current Path
     * @return
     */
    @Override
    public String printAll(String beginning, String currentPath) throws IOException {
        String toReturn = "";
        File[] files = getFiles(currentPath);
        if (files != null) {
            for (File temp : files) {
                toReturn += format(beginning, temp) + "\n";
                if (temp.isDirectory() && !temp.isHidden()) {
                    toReturn += printAll(beginning + "\t", String.valueOf(temp));
                }
            }
        }
        return toReturn;
    }
    public ArrayList<String> autocompleteFromIncompleteString (String command) {
        File[] files = getFiles(currPath);
        ArrayList<String> temp = new ArrayList<String>();
        for (File f: files) {
            if (f.toString().matches(command + ".*")) {
                temp.add(f.toString());
            }
        }
        return temp;
    }
    /**__________________________________________________________*/
    /**
     * Formats each String while outputting
     *
     * @param f
     */
    private String format(File f) throws IOException {
        return format("", f);
    }
    /**__________________________________________________________*/
    /** Does the formatting */
    private String format(String start, File f) throws IOException {
        String ANSI_RESET = "u001B[0m";

        String ANSI_BLACK = "u001B[30m";
        String ANSI_RED = "u001B[31m";
        String ANSI_GREEN = "u001B[32m";
        String ANSI_YELLOW = "u001B[33m";
        String ANSI_BLUE = "u001B[34m";
        String ANSI_PURPLE = "u001B[35m";
        String ANSI_CYAN = "u001B[36m";
        String ANSI_WHITE = "u001B[37m";

        String BRIGHT_BLACK = "u001B[1;30m";
        String BRIGHT_RED = "u001B[1;31m";
        String BRIGHT_GREEN = "u001B[1;32m";
        String BRIGHT_YELLOW = "u001B[1;33m";
        String BRIGHT_BLUE = "u001B[1;34m";
        String BRIGHT_PURPLE = "u001B[1;35m";
        String BRIGHT_CYAN = "u001B[1;36m";
        String BRIGHT_WHITE = "u001B[1;37m";

        if (f.isHidden()) {
            return (start + ANSI_RED + f + ANSI_RESET);
        }

        else if (f.isDirectory()) {
            return (start + BRIGHT_BLUE + f.getCanonicalFile() + ANSI_RESET );
        }

        else if (f.isFile()) {
            return (start + BRIGHT_GREEN + f.getName() + ANSI_RESET);
        }
        else {
            return "";
        }
    }
    /**__________________________________________________________*/
    /** Sets the current working Directory to Home */
    @Override
    public void toHome() {
        changeWorkingDirectory(System.getProperty("user.home"));
    }

    /**
     * Navigate back to the root directory
     */
    @Override
    public void toRoot() {
        changeWorkingDirectory(Path.of(System.getProperty("user.home")).getRoot().toString());
    }
    public String printTextFile(String filename) throws FileNotFoundException {
        StringBuffer bf = new StringBuffer();
        File fp = new File(filename);
        Scanner sc = new Scanner(fp);
        while (sc.hasNextLine()) {
            bf.append(sc.nextLine() + "\n");
        }
        return bf.toString();
    }
    /** Parses the user input and calls the corresponding method */
    /**__________________________________________________________*/
    @Override
    public String convert(String cmd) throws MyException, IOException {

        if (cmd.matches("[c][d].+")) {
            /* Call moveInward or changeWorkingDirectory or toHome() or moveOutward*/
            if (cmd.substring(2).replaceAll("\s", "").matches("/")) {
                toRoot();
                return "";
            }
            if (cmd.endsWith("/")) {
                cmd = cmd.substring(0, cmd.length() - 1);
            }
            if (cmd.substring(2).replaceAll("\s", "").equals("..")) {
                moveOutward();
            }
            else if (cmd.substring(2).replaceAll("\s", "").equals("~")) {
                toHome();
            }
            else if (cmd.substring(2).replaceAll("\s", "").matches("\\.\\..+")) {
                moveOutward();
                moveInward(cmd.substring(5));
            } else { /* Move Inward */
                moveInward(cmd.substring(3));
            }
        }
        else if (cmd.matches("pwd")) {
            /* Call getWorkingDirectory() */
            //System.out.println(getWorkingPath());
            return ("u001B[1;35m" + getWorkingPath());
        }
        else if (cmd.replaceAll("\s", "").equals("ls")) {
            /* Call print(getWorkingDirectory()) */
            return print(getWorkingPath(), true);

        }
        else if (cmd.equals("ls -aR")) {
            /* Call printAll(getWorkingPath()) */
            return printAll("\t", getWorkingPath());
        }
        else if (cmd.equals("ls -d */")) {
            return printDirectories(getWorkingPath());
        } else if (cmd.equals("help")) {
            return printTextFile("/Users/aravind/Desktop/Code for Fun/FIleX Project/File Explorer/src/FileExplorer/Help");
            /** TODO: Print all contents in the help page */
        }else {
            throw new MyException(cmd, "Invalid method", new File(getWorkingPath()));
        }
        return "";
    }
    /**__________________________________________________________*/

}
/**
 * TODO:    Remove (rm).
 *          Tab to autocomplete (\t)
 *          Pipe to Compose methods (|)
 *          Grep to fetch (grep)
 *          Last Login, Save cache
 *          Save last N lines
 *
 *          Enable compiling java program to run a file within File Explorer from within terminal
 *
 *
 *          start making Web Browser
 *          Selenium?
 *          https://www.youtube.com/watch?v=3Zbn94NgjsA
 *          https://www.youtube.com/watch?v=Sl_Ub5m_zBw
 *
 */