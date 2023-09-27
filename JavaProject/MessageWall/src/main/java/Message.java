public class Message {
    public String from;
    public String to;
    public String msg;

    @Override
    public String toString() {
        return "message{" + "from='" + from + '\'' + ", to='" + to + '\'' + ", msg='" + msg + '\'' + '}';
    }
}