package inatel.com.br.restaurante.model;

/**
 * Created by mateus on 20/03/18.
 */

public class Order {

    public String custOrder;

    public Order(String order){

        this.custOrder = order;
    }

    public String getCustOrder() {
        return custOrder;
    }

    public void setCustOrder(String custOrder) {
        this.custOrder = custOrder;
    }
}
