enum Operation {SELL, BUY}

class Order {
    private final Operation operation;
    private final String symbol;
    private final int quantity;
    private final int price;

    Order(Operation operation, String symbol, int price, int quantity) {
        if ((operation == null) || (symbol == null)) {
            throw new IllegalArgumentException("Operation and symbol in order couldn't be null");
        }
        if ((quantity <= 0) || (price <= 0)) {
            throw new IllegalArgumentException("Quantity and price should be positive");
        }

        this.operation = operation;
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
    }

    Operation getOperation() {
        return operation;
    }

    String getSymbol() {
        return symbol;
    }

    int getQuantity() {
        return quantity;
    }

    int getPrice() {
        return price;
    }

    Order getMatchingOrder() {
        return new Order(operation == Operation.BUY ? Operation.SELL : Operation.BUY, symbol, quantity, price);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Order) {
            Order order = (Order)o;
            return ((this.operation == order.operation) && this.symbol.equals(order.symbol)
                    && (this.quantity == order.quantity) && (this.price == order.price));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 31 * 7 + operation.ordinal();
        hash = 31 * hash + symbol.hashCode();
        hash = 31 * hash + quantity;
        return 31 * hash + price;
    }
}

class ClientOrder {
    private final String clientName;
    private final Order order;

    ClientOrder(String clientName, Order order) {
        this.clientName = clientName;
        this.order = order;
    }

    String getClientName() {
        return clientName;
    }

    Order getOrder() {
        return order;
    }
}