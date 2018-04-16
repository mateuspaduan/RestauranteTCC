package inatel.com.br.restaurante.model;

/**
 * Created by mateus on 20/03/18.
 */

public class Order {

    public String custOrder;
    public String custTime;

    public Order(String order, String time){

        this.custOrder = order;
        this.custTime = time;
    }

    public String getCustOrder() {
        return custOrder;
    }

    public void setCustOrder(String custOrder) {
        this.custOrder = custOrder;
    }


    public String getCustPrice() {
        return custTime;
    }

    public void setCustPrice(float custPrice) {
        this.custTime = custTime;
    }
}
