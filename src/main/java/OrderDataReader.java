/**
 * Orders reader
 */
public class OrderDataReader extends DataReader<ClientOrder> {
    private static final String FILE_NAME = "orders.txt";

    OrderDataReader() {
        super(FILE_NAME);
    }

    @Override
    ClientOrder parse(String[] tokens) {
        Operation operation;
        if (tokens[1].equals("b")) {
            operation = Operation.BUY;
        } else if (tokens[1].equals("s")) {
            operation = Operation.SELL;
        } else {
            throw new RuntimeException("Incorrect operation symbol " + tokens[1]);
        }
        return new ClientOrder(tokens[0], new Order(operation, tokens[2], Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4])));
    }
}
