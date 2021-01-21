import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Data reader
 * @param <T> data type to read
 */
abstract class DataReader<T> {
    private static final String DELIMITER = "\t";
    final String fileName;

    DataReader(String fileName) {
        this.fileName = fileName;
    }

    void process(Consumer<T> processor) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.lines().forEach(line -> processor.accept(parse(line.split(DELIMITER))));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + fileName, e);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while processing file " + fileName, e);
        }
    }

    abstract T parse(String[] tokens);
}
