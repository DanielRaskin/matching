import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Matching {
    private final Map<String, Balance> balances = new HashMap<>();
    private final Map<Order, LinkedList<String>> openOrders = new HashMap<>();

    private void processOrder(String client, Order order) {
        LinkedList<String> matchingClients = openOrders.get(order.getMatchingOrder());
        if ((matchingClients == null) || matchingClients.isEmpty()) {
            LinkedList<String> clients = openOrders.get(order);
            if (clients == null) {
                clients = new LinkedList<>();
                clients.add(client);
                openOrders.put(order, clients);
            } else {
                clients.addLast(client);
            }
        } else {
            String matchingClient = matchingClients.removeFirst();
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
        clientDataReader.process(clientBalance -> balances.put(clientBalance.getClientName(), clientBalance.getBalance()));
        orderDataReader.process(clientOrder -> processOrder(clientOrder.getClientName(), clientOrder.getOrder()));
        resultWriter.saveResults(balances);
    }

    public static void main(String[] args) throws Exception {
        new Matching().process(new ClientDataReader(), new OrderDataReader(), new ResultWriter());
    }
}
