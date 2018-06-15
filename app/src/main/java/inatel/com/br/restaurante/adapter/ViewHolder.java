package inatel.com.br.restaurante.adapter;

import android.content.Context;
import android.widget.TextView;

import inatel.com.br.restaurante.model.Order;

/**
 * Created by mateus on 24/05/18.
 */

public class ViewHolder {

    TextView orderName;
    TextView orderPrice;

    Order order;

    Context context;

    public void setData(Order item){

        order = item;
        orderName.setText("Pedido: " + order.getCustOrder());
        orderPrice.setText("Preço: " + order.getCustPrice());
    }
}
