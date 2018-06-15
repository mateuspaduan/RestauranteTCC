package inatel.com.br.restaurante.controller;

import java.util.ArrayList;

import inatel.com.br.restaurante.model.Order;

/**
 * Created by mateus on 15/06/18.
 */

public class DataHandlerController {

    private static DataHandlerController handler;
    private ArrayList<Order> list;

    private DataHandlerController() {
    }

    public static DataHandlerController getInstance() {
        if (handler == null)
            handler = new DataHandlerController();
        return handler;
    }

    public ArrayList<Order> getList() {
        return list;
    }

    public void setList(ArrayList<Order> list) {
        this.list = list;
    }
}
