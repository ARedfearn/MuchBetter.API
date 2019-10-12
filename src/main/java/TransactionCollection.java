import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransactionCollection implements Iterable<Transaction> {
    private final List<Transaction> list = new ArrayList<Transaction>();

    public TransactionCollection(String token) {
        //Go to data layer
    }

    private void GetTransactions() {
        //Get transactions from data layer (H2 database)

    }

    @Override
    public Iterator<Transaction> iterator() {
        return list.iterator();
    }

}
