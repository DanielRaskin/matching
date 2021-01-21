/**
 * Clients reader from file
 */
class ClientDataReader extends DataReader<ClientBalance> {
    private static final String FILE_NAME = "clients.txt";

    ClientDataReader() {
        super(FILE_NAME);
    }

    @Override
    ClientBalance parse(String[] tokens) {
        Balance balance = new Balance();
        balance.receiveDollars(Integer.parseInt(tokens[1]));
        balance.receiveShares("A", Integer.parseInt(tokens[2]));
        balance.receiveShares("B", Integer.parseInt(tokens[3]));
        balance.receiveShares("C", Integer.parseInt(tokens[4]));
        balance.receiveShares("D", Integer.parseInt(tokens[5]));
        return new ClientBalance(tokens[0], balance);
    }
}