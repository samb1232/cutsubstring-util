import java.io.*;
import java.util.Scanner;

public class Cut {

    private final boolean symbolFlag;

    private final boolean wordFlag;

    private final String outputFileName;

    private final String inputFileName;

    private final String range;

    public Cut(boolean symbolFlag, boolean wordFlag, String outputFileName, String inputFileName, String range) {
        this.symbolFlag = symbolFlag;
        this.wordFlag = wordFlag;
        this.outputFileName = outputFileName;
        this.inputFileName = inputFileName;
        this.range = range.replaceAll("\"", "");
    }

    public void checker() {
        if (symbolFlag == wordFlag) {
            throw new IllegalArgumentException("Symbol or word flag is missing or they both given");
        }

        if (outputFileName != null && !new File(outputFileName).exists()) {
            throw new IllegalArgumentException("Output File is not exist");
        }

        if (inputFileName != null && !new File(inputFileName).exists()) {
            throw new IllegalArgumentException("Input File is not exist");
        }
    }

    public void doCut() throws IOException {
        int rangeFrom;
        int rangeTo;
        boolean rangeToFlag = false;
        if (range.matches("^\\d+-\\d+$")) {
            rangeFrom = Integer.parseInt(range.split("-")[0]);
            rangeTo = Integer.parseInt(range.split("-")[1]);
            if (rangeFrom > rangeTo) {
                throw new IllegalArgumentException("Incorrect range input");
            }
        } else if (range.matches("^-\\d+$")) {
            rangeFrom = 0;
            rangeTo = Integer.parseInt(range.split("-")[1]);
        } else if (range.matches("^\\d+-$")) {
            rangeFrom = Integer.parseInt(range.split("-")[0]);
            rangeTo = -1;
            rangeToFlag = true;
        } else {
            throw new IllegalArgumentException("Incorrect range input");
        }

        String[] textToCut;
        if (inputFileName != null) {
            File inputFile = new File(inputFileName);
            textToCut = fileToStringArr(inputFile);
        } else {
            Scanner in = new Scanner(System.in);
            System.out.println("Введите текст: ");
            String line;
            StringBuilder lines = new StringBuilder();
            do {
                line = in.nextLine();
                lines.append(line).append("\n");
            } while(!line.equals(""));
            textToCut = lines.toString().split("\n");
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


    private String[] fileToStringArr(File file) throws FileNotFoundException {
        StringBuilder retString = new StringBuilder();
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            retString.append(scan.nextLine()).append("\n");
        }
        return retString.toString().split("\n");
    }
}
