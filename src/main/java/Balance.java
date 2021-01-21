import java.util.HashMap;
import java.util.Map;

/**
 * Balance - how many dollars and shares
 */
class Balance {
    private int dollars = 0;
    private final Map<String, Integer> shares = new HashMap<>();

    int getDollars() {
        return dollars;
    }

    int getShares(String symbol) {
        return shares.getOrDefault(symbol, 0);
    }

    void receiveDollars(int dollars) {
        this.dollars += dollars;
    }

    void receiveShares(String symbol, int shares) {
        int newShares = this.shares.getOrDefault(symbol, 0) + shares;
        this.shares.put(symbol, newShares);
    }
}

/**
 * Client name + balance
 */
class ClientBalance {
    private final String clientName;
    private final Balance balance;

    ClientBalance(String clientName, Balance balance) {
        this.clientName = clientName;
        this.balance = balance;
    }

    String getClientName() {
        return clientName;
    }

    Balance getBalance() {
        return balance;
    }
}
