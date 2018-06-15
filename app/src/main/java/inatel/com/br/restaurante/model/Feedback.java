package inatel.com.br.restaurante.model;

/**
 * Created by mateus on 23/05/18.
 */

public class Feedback {

    String email;
    String feedback;

    public Feedback(String mEmail, String mFeedback){

        this.email = mEmail;
        this.feedback = mFeedback;
    }

    public String getTime() {
        return email;
    }

    public void setTime(String email) {
        this.email = email;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
