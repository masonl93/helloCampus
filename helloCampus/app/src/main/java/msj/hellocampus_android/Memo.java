package msj.hellocampus_android;


/**
 * Created by Mason on 3/7/16.
 */
public class Memo {

    private String message;
    private String user;
    private String node_key;

    public Memo() {
    }

    public Memo(String message, String author, String node_key) {
        this.message = message;
        this.user = author;
        this.node_key = node_key;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public String getNode_key() {
        return node_key;
    }

}