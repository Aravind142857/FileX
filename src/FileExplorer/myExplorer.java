package FileExplorer;

import FileExplorer.MyException;

import java.io.File;
import java.io.IOException;

public interface myExplorer {

    /** Returns the Current Working Path */
    String getWorkingPath();

    /** Returns current working directory */
    String getWorkingDirectory();

    /** Changes the current working Path to  */
    void changeWorkingDirectory(String newPath);

    /** Moves inward and sets current Working Path to inLoc */
    void moveInward (String inLoc) throws MyException;

    /** Moves outward and sets current Working Path to inLoc */
    void moveOutward ();

    /** Return a list of all files in the current directory */
    File[] getFiles(String currPath);

    /** Prints all files in the current directory
     * @return*/
    String print(String currentPath, boolean format) throws IOException;

    /** Prints all files in current and sub-directories
     * @return*/
    String printAll(String beggining, String currentPath) throws IOException;

    /** Convert user command to Program's command
     * @return*/
    String convert(String cmd) throws MyException, IOException;

    /** Navigate back to home directory */
    void toHome();

    /** Navigate back to the root directory */
    void toRoot();
}
