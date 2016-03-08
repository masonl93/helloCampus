package msj.hellocampus_android;


/**
 * Created by Mason on 3/7/16.
 */
public class Memo {

    private String message;
    private String user;
    private String title;

    public Memo() {
    }

    public Memo(String message, String author, String title) {
        this.message = message;
        this.user = author;
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public String getTitle() { return title; }

}