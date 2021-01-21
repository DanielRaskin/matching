import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Results writer
 */
public class ResultWriter {
    private static final String FILE_NAME = "result.txt";
    private static final String SEPARATOR = "\t";

    void saveResults(Map<String, Balance> clientBalances) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (Map.Entry<String, Balance> clientBalance : clientBalances.entrySet()) {
                Balance balance = clientBalance.getValue();
                writer.write(new StringBuilder(clientBalance.getKey())
                        .append(SEPARATOR).append(balance.getDollars())
                        .append(SEPARATOR).append(balance.getShares("A"))
                        .append(SEPARATOR).append(balance.getShares("B"))
                        .append(SEPARATOR).append(balance.getShares("C"))
                        .append(SEPARATOR).append(balance.getShares("D")).append("\n").toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while saving results", e);
        }
    }
}
