package inatel.com.br.restaurante.model;

/**
 * Created by mateus on 19/03/18.
 */

public class User {

    public String mEmail;
    public String mPhoneNumber;
    public String mCPF;

    public User(String email, String phoneNumber, String CPF){

        this.mEmail = email;
        this.mPhoneNumber = phoneNumber;
        this.mCPF = CPF;
    }
}
