package inatel.com.br.restaurante.model;

/**
 * Created by mateus on 20/03/18.
 */

public class Order {

    public String custName;
    public String custOrder;
    public String custPrice;
    public String custComment;

    public Order(String order, String price){

        this.custOrder = order;
        this.custPrice = price;
    }

    public Order(String order, String price, String comment){

        this.custOrder = order;
        this.custPrice = price;

        if(comment != null){

            this.custComment = comment;
        }

        else this.custComment = "Sem comentário.";
    }

    public String getCustComment() {
        return custComment;
    }

    public void setCustComment(String custComment) {
        this.custComment = custComment;
    }

    public String getCustOrder() {
        return custOrder;
    }

    public void setCustOrder(String custOrder) {
        this.custOrder = custOrder;
    }

    public String getCustPrice() {
        return custPrice;
    }

    public void setCustPrice(String custPrice) {
        this.custPrice = custPrice;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }
}
