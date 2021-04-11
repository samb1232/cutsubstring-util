import org.kohsuke.args4j.*;

import java.io.IOException;

/** Вариант 8 -- cut
 *       Выделение из каждой строки текстового файла некоторой подстроки:
 *       file задаёт имя входного файла.
 *       Если параметр отсутствует, следует считывать входные данные с консольного ввода;
 *
 * Флаг -o ofile задаёт имя выходного файла (в данном случае ofile).
 * Если параметр отсутствует, следует выводить результат на консольный вывод.
 * Флаг -с означает, что все числовые параметры задают отступы в символах (буквах) входного файла.
 * Флаг -w означает, что все числовые параметры задают отступы в словах
 * (т.е. последовательностях символов без пробелов) входного файла.
 *
 *       Параметр range задаёт выходной диапазон и имеет один из следующих видов (здесь N и К -- целые числа):
 *        -K диапазон от начала строки до K
 *       N- диапазон от N до конца строки
 *
 *       N-K диапазон от N до K
 *       Command line: cut [-c|-w] [-o ofile] [file] range
 *
 *       Программа построчно обрабатывает входные данные и для каждой строки выдаёт часть этой строки
 *       согласно заданному диапазону. Если какого-то из указанных файлов не существует
 *       или неправильно указаны параметры -c и -w (должен быть указан ровно один из них),
 *       следует выдать ошибку. Если в строке не хватает символов или слов, это ошибкой не является,
 *       в этом случае следует выдать ту часть входных данных, которая попадает в диапазон.
 *
 *       Кроме самой программы, следует написать автоматические тесты к ней.
 */

public class Main {
    @Option(name = "-c")
    private boolean symbolFlag;

    @Option(name = "-w")
    private boolean wordFlag;

    @Option(name = "-o")
    private String outputFileName;

    @Argument(index = 1)
    private String inputFileName;

    @Argument(required = true)
    private String range;


    public static void main(String[] args) {
        try {
            new Main().launch(args);
        } catch (IOException | IllegalArgumentException exception) {
            System.err.println(exception.getLocalizedMessage());
            System.exit(1);
        }
    }

    public void launch(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getLocalizedMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        if (inputFileName != null) {
            String t = range;
            range = inputFileName;
            inputFileName = t;
        }


        range = range.replaceAll("\"", "");
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

        Cut core = new Cut(symbolFlag, wordFlag, outputFileName, inputFileName, rangeFrom, rangeTo, rangeToFlag);
        core.checker();
        core.doCut();
    }
}
