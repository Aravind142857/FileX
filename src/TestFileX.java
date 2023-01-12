

import FileExplorer.FileX;
import FileExplorer.MyException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestFileX {
    @Test
    public void TestGetWorkingDirectory() {
        FileX XPlorer = new FileX();
        assertEquals(XPlorer.getWorkingPath(), "/Users/aravind");
    }
    @Test
    public void TestChangeWorkingDirectory() {
        FileX XPlorer = new FileX();
        XPlorer.changeWorkingDirectory((XPlorer.getWorkingPath() + "/Desktop"));
        assertEquals(XPlorer.getWorkingPath(), "/Users/aravind/Desktop");
    }
    @Test
    public void TestGetFiles() throws IOException {
        FileX XPlorer = new FileX();
        //XPlorer.changeWorkingDirectory(XPlorer.getWorkingDirectory() + "/C");
        XPlorer.print(XPlorer.getWorkingPath(), true);
        //String[] expected = new String[]{"Hello world.c"}
    }
    @Test
    public void TestMoveInwardOutward() throws MyException {
        FileX XPlorer = new FileX();
        XPlorer.moveInward("/Desktop");
        assertEquals(XPlorer.getWorkingPath(), "/Users/aravind/Desktop");
        XPlorer.moveOutward();
        assertEquals(XPlorer.getWorkingPath(), "/Users/aravind");
    }



}
