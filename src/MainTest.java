import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private void assertFilesEquals(File file1, File file2) throws FileNotFoundException {
        Scanner scan1 = new Scanner(file1);
        Scanner scan2 = new Scanner(file2);
        while (scan1.hasNextLine()) {
            assertTrue(scan1.hasNextLine() && scan2.hasNextLine());
            assertEquals(scan1.nextLine(), scan2.nextLine());
        }
    }

    @Test
    public void shouldReturnError() {
        assertThrows(IllegalArgumentException.class, () ->
                Main.main("-w -o src/outputFile.txt -file INVALIDFILE.txt 2-6".split(" ")));
        assertThrows(IllegalArgumentException.class, () ->
                Main.main("-w -o INVALIDFILE.txt -file src/inputFile.txt 2-6".split(" ")));
        assertThrows(IllegalArgumentException.class, () ->
                Main.main("-c -o src/outputFile.txt -file src/inputFile.txt 15-10".split(" ")));
        assertThrows(IllegalArgumentException.class, () ->
                Main.main("-o src/outputFile.txt -file src/inputFile.txt 15-25".split(" ")));
        assertThrows(IllegalArgumentException.class, () ->
                Main.main("-w -c -o src/outputFile.txt -file src/inputFile.txt 2-6".split(" ")));
    }

    @Test
    public void shouldWorks() throws IOException, CmdLineException {
        Main.main("-w -o src/outputFile.txt -file src/inputFile.txt 2-5".split(" "));
        assertFilesEquals(new File("src/testFiles/testFile1.txt"), new File("src/outputFile.txt"));
    }

    @Test
    public void indexesTest1() throws IOException, CmdLineException {
        Main.main("-c -o src/outputFile.txt -file src/inputFile.txt 5-".split(" "));
        assertFilesEquals(new File("src/testFiles/testFile2.txt"), new File("src/outputFile.txt"));
    }

    @Test
    public void indexesTest2() throws IOException, CmdLineException {
        Main.main("-c -o src/outputFile.txt -file src/inputFile.txt 1-5".split(" "));
        assertFilesEquals(new File("src/testFiles/testFile3.txt"), new File("src/outputFile.txt"));
    }

    @Test
    public void indexesTest3() throws IOException, CmdLineException {
        Main.main("-w -o src/outputFile.txt -file src/inputFile.txt \"-5\"".split(" "));
        assertFilesEquals(new File("src/testFiles/testFile4.txt"), new File("src/outputFile.txt"));
    }
}