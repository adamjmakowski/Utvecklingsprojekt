package Controller;

import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ClientController controller = new ClientController("127.0.0.1",1234);
    }
}
