import java.util.ArrayList;
import java.util.List;

public class Canteen {
    private List<Table> tables;
    private String canteenID;

    public Canteen(String canteenID){
        this.canteenID = canteenID;
        tables = new ArrayList<>();
        tables.add(new Table("01", 1));
        tables.add(new Table("02", 2));;
    }

    public void makeReservation(Customer c, String TableID){
        Table ble = tables.stream().filter(t -> t.getTableID().equals(TableID))
        .findFirst().orElse(null);
        if (!ble.checkAvailability()) return;
        ble.addReservation(c);
    


    
    }
}
