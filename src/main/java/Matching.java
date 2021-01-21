import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Matches processing
 */
public class Matching {
    private final Map<String, Balance> balances = new HashMap<>();

    // open orders
    // key is order, value is list of clients which have such order
    private final Map<Order, LinkedList<String>> openOrders = new HashMap<>();
    int count = 0;

    // processes order
    private void processOrder(String client, Order order) {
        count++;
        // try to find matching order
        LinkedList<String> matchingClients = openOrders.get(order.getMatchingOrder());
        if ((matchingClients == null) || matchingClients.isEmpty()) {
            // if matching order not found, store order as open
            LinkedList<String> clients = openOrders.get(order);
            if (clients == null) {
                clients = new LinkedList<>();
                clients.add(client);
                openOrders.put(order, clients);
            } else {
                clients.addLast(client);
            }
        } else {
            // if matching order found
            // get first client
            String matchingClient = matchingClients.removeFirst();
            // make operation and modify balances
            Balance clientBalance = balances.get(client);
            String symbol = order.getSymbol();
            int shares = ((order.getOperation() == Operation.SELL) ? -1 : 1) * order.getQuantity();
            int dollars = - shares * order.getPrice();
            clientBalance.receiveDollars(dollars);
            clientBalance.receiveShares(symbol, shares);
            Balance matchingClientBalance = balances.get(matchingClient);
            matchingClientBalance.receiveDollars(-dollars);
            matchingClientBalance.receiveShares(symbol, -shares);
        }
    }

    void process(ClientDataReader clientDataReader, OrderDataReader orderDataReader, ResultWriter resultWriter) {
        // read and store client data
        clientDataReader.process(clientBalance -> balances.put(clientBalance.getClientName(), clientBalance.getBalance()));
        // read and process orders
        orderDataReader.process(clientOrder -> processOrder(clientOrder.getClientName(), clientOrder.getOrder()));
        // save results
        resultWriter.saveResults(balances);
    }

    /**
     * main() method
     */
    public static void main(String[] args) throws Exception {
        new Matching().process(new ClientDataReader(), new OrderDataReader(), new ResultWriter());
    }
}
