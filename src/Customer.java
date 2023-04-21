/***
 * A customer object that stores a number of items, used to estimate their time in a checkout queue
 * @author Andrew Colbert
 */
public class Customer {
    /**
     * Number of items to be checked out
     */
    private int items;

    Customer(int items){
        this.items = items;
    }

    /**
     *  Getter method for number of items
     * @return items
     */
    public int getItems() {
        return items;
    }

    /**
     * Calculates an estimate time for the customer to checkout,
     * using the expression 45 + 5 * items.
     * @return Estimated time (seconds)
     */
    public int calculateEstTime(){
        return 45 + 5 * items;
    }

    /**
     * Outputs customer information
     * @return as "[items]([estimated time])"
     */
    @Override
    public String toString() {
        return items + "(" + calculateEstTime() + ")";
    }
}
