package inatel.com.br.restaurante.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import inatel.com.br.restaurante.R;
import inatel.com.br.restaurante.model.Order;

/**
 * Created by mateus on 24/05/18.
 */

public class OrderAdapter extends ArrayAdapter<Order> {

    private LayoutInflater lf;
    private List<ViewHolder> lstHolders = new ArrayList<ViewHolder>();
    private Context context;

    private ViewHolder holder;

    private Order mOrder;

    public OrderAdapter(Context context, ArrayList<Order> objects) {
        super(context, 0, objects);
        lf = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = lf.inflate(R.layout.order_list, parent, false);
            holder.orderName = (TextView) convertView.findViewById(R.id.list_item_message);
            holder.orderPrice = (TextView) convertView.findViewById(R.id.list_item_time);

            convertView.setTag(holder);

            synchronized (lstHolders) {
                lstHolders.add(holder);
            }
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        mOrder = (Order) getItem(position);
        holder.setData(getItem(position));

        return convertView;
    }
}
