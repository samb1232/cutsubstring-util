import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Cut {

    private final boolean symbolFlag;

    private final boolean wordFlag;

    private final String outputFileName;

    private final String inputFileName;

    private final int rangeFrom;

    private int rangeTo;

    private final boolean rangeToFlag;

    public Cut(boolean symbolFlag, boolean wordFlag, String outputFileName, String inputFileName,
               int rangeFrom, int rangeTo, boolean rangeToFlag) {
        this.symbolFlag = symbolFlag;
        this.wordFlag = wordFlag;
        this.outputFileName = outputFileName;
        this.inputFileName = inputFileName;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
        this.rangeToFlag = rangeToFlag;
    }

    public void checker() {
        if (symbolFlag == wordFlag) {
            throw new IllegalArgumentException("Symbol or word flag is missing or they both given");
        }

        if (inputFileName != null && !new File(inputFileName).exists()) {
            throw new IllegalArgumentException("Input File with such name is not exist");
        }
    }

    public void doCut() throws IOException {
        String[] textToCut;
        if (inputFileName != null) {
            File inputFile = new File(inputFileName);
            textToCut = fileToStringArr(inputFile).toArray(new String[0]);
        } else {
            Scanner in = new Scanner(System.in);
            textToCut = in.next().split("\n");
        }

        StringBuilder cutText = new StringBuilder();

        for (String textLine : textToCut) {
            String[] line;
            if (symbolFlag) {
                line = textLine.split("");
            } else {
                line = textLine.split("\\s");
            }
            if (rangeToFlag) {
                rangeTo = line.length - 1;
            }
            for (int i = rangeFrom; i <= rangeTo; i++) {
                if (i == line.length) {
                    break;
                }
                cutText.append(line[i]);
                if (wordFlag && i < rangeTo) {
                    cutText.append(" ");
                }
            }
            cutText.append("\n");
        }
        if (outputFileName != null) {
            FileWriter writer = new FileWriter(outputFileName);
            writer.write(cutText.toString());
            writer.close();
        } else {
            System.out.println(cutText.toString());
        }
    }


    private ArrayList<String> fileToStringArr(File file) throws FileNotFoundException {
        ArrayList<String> retString = new ArrayList<>();
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            retString.add(scan.nextLine());
        }
        return retString;
    }
}
