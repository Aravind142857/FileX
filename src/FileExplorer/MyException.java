package FileExplorer;

import java.io.File;

public class MyException extends Throwable {
    String method;
    String type;
    File file;
    public MyException(String method, String type, File file) {
        this.method = method;
        this.type = type;
        this.file = file;
        System.out.println(file);
    }

    public String message() {
        String e1man = "---╾━╤デ╦︻(0̿u̯0̿)";
        String e2man = "¯\\_(ツ)_/¯";
        String e3man = ":(";
        String ANSI_RED = "u001B[1;31m";
        String ANSI_RESET = "u001B[0m";
        String beep = "\007";
        if (this.type.equals("File is not a directory")) {
            return (beep + ANSI_RED + this.method + ": Directory access called on File: "  + this.file + "\n" + e1man + ANSI_RESET);
        }
        if (this.type.equals("File not found")) {
            return (beep + ANSI_RED + this.method + ": no such file or directory: " + this.file + "\n" + e2man + ANSI_RESET);
        }
        if (this.type.equals("Invalid method")) {
            return (beep + ANSI_RED + this.method + ": Command not found: " + "\n" + e3man + ANSI_RESET);
        }
        return "";
    }
}
