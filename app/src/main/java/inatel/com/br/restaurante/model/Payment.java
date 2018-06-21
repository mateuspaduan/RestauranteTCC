package inatel.com.br.restaurante.model;

/**
 * Created by mateus on 25/05/18.
 */

public class Payment {

    public String type;
    public String value;

    public Payment(String mType, String mValue){

        type = mType;
        value = mValue;
    }
}
