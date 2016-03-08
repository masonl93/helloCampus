package msj.hellocampus_android;


/**
 * Created by Mason on 3/7/16.
 */
public class Memo {

    private String message;
    private String user;

    public Memo() {
    }

    public Memo(String message, String author) {
        this.message = message;
        this.user = author;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

}