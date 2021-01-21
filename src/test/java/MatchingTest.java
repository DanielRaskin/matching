import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;

/**
 * Tests
 */
public class MatchingTest {
    @Test
    public void test1() {
        test(new String[] {"C1\t5\t5\t5\t5\t5"}, new String[] {"C1\ts\tA\t5\t5"}, Collections.singleton("C1\t5\t5\t5\t5\t5"));
    }

    @Test
    public void test2() {
        test(new String[] {"C1\t5\t5\t5\t5\t5", "C2\t5\t5\t5\t5\t5"},
                new String[] {"C1\ts\tA\t1\t5", "C2\tb\tA\t1\t5"},
                new HashSet<>(Arrays.asList("C1\t10\t0\t5\t5\t5", "C2\t0\t10\t5\t5\t5")));
    }

    @Test
    public void test3() {
        test(new String[] {"C1\t100\t25\t0\t0\t0", "C2\t100\t0\t25\t0\t0", "C3\t250\t0\t0\t50\t50"},
                new String[] {"C1\ts\tA\t5\t15", "C2\ts\tB\t5\t15", "C3\tb\tB\t5\t15", "C3\tb\tA\t5\t15"},
                new HashSet<>(Arrays.asList("C1\t175\t10\t0\t0\t0", "C2\t175\t0\t10\t0\t0", "C3\t100\t15\t15\t50\t50")));
    }

    @Test
    public void test4() {
        test(new String[] {"C1\t100\t25\t0\t0\t0", "C2\t100\t0\t25\t0\t0", "C3\t100\t0\t0\t25\t0", "C4\t100\t0\t0\t0\t25"},
                new String[] {"C1\ts\tA\t5\t10", "C2\ts\tB\t5\t10", "C3\ts\tC\t5\t10", "C4\tb\tC\t5\t10", "C4\tb\tA\t5\t10"},
                new HashSet<>(Arrays.asList("C1\t150\t15\t0\t0\t0", "C2\t100\t0\t25\t0\t0", "C3\t150\t0\t0\t15\t0", "C4\t0\t10\t0\t10\t25")));
    }

    @Test
    public void test5() {
        test(new String[] {"C1\t1000\t50\t50\t0\t0", "C2\t1500\t0\t0\t50\t50", "C3\t3000\t0\t0\t0\t0", "C4\t500\t10\t15\t20\t25", "C5\t0\t25\t50\t75\t125"},
                new String[] {"C1\ts\tA\t50\t25", "C2\ts\tD\t20\t10", "C5\ts\tC\t30\t10", "C4\ts\tB\t30\t15", "C3\tb\tA\t40\t50",
                        "C5\ts\tD\t20\t10", "C2\tb\tA\t50\t25", "C3\tb\tC\t30\t10", "C1\tb\tD\t20\t10", "C3\tb\tD\t20\t10"},
                new HashSet<>(Arrays.asList("C1\t2050\t25\t50\t0\t10", "C2\t450\t25\t0\t50\t40", "C3\t2500\t0\t0\t10\t10", "C4\t500\t10\t15\t20\t25", "C5\t500\t25\t50\t65\t115")));
    }

    private void test(String[] clients, String[] orders, Set<String> result) {
        TestResultWriter testResultWriter = new TestResultWriter();
        new Matching().process(new TestClientDataReader(clients), new TestOrderDataReader(orders), testResultWriter);
        assert testResultWriter.getResult().equals(result);

    }
}

/**
 * Special clients reader for testing - reads from given string array
 */
class TestClientDataReader extends ClientDataReader {
    private String[] lines;

    TestClientDataReader(String[] lines) {
        this.lines = lines;
    }

    @Override
    void process(Consumer<ClientBalance> processor) {
        for (String line : lines) {
            processor.accept(parse(line.split("\t")));
        }
    }
}

/**
 * Special orders reader for testing - reads from given string array
 */
class TestOrderDataReader extends OrderDataReader {
    private String[] lines;

    TestOrderDataReader(String[] lines) {
        this.lines = lines;
    }

    @Override
    void process(Consumer<ClientOrder> processor) {
        for (String line : lines) {
            processor.accept(parse(line.split("\t")));
        }
    }
}

/**
 * Special results writer for testing - stores results in set
 */
class TestResultWriter extends ResultWriter {
    private Set<String> result = new HashSet<String>();

    @Override
    void saveResults(Map<String, Balance> clientBalances) {
        for (Map.Entry<String, Balance> clientBalance : clientBalances.entrySet()) {
            Balance balance = clientBalance.getValue();
            result.add(new StringBuilder(clientBalance.getKey())
                    .append("\t").append(balance.getDollars())
                    .append("\t").append(balance.getShares("A"))
                    .append("\t").append(balance.getShares("B"))
                    .append("\t").append(balance.getShares("C"))
                    .append("\t").append(balance.getShares("D")).toString());
        }
    }

    Set<String> getResult() {
        return result;
    }
}