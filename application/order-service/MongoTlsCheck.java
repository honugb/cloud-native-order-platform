import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class MongoTlsCheck {
    public static void main(String[] args) throws Exception {
        String host = "ac-swa9sh1-shard-00-00.in2jpqx.mongodb.net";

        try (SSLSocket socket = (SSLSocket)
                SSLSocketFactory.getDefault().createSocket(host, 27017)) {

            socket.setSoTimeout(10000);
            socket.startHandshake();

            System.out.println("TLS SUCCESS");
            System.out.println(
                "Protocol: " + socket.getSession().getProtocol()
            );
            System.out.println(
                "Cipher: " + socket.getSession().getCipherSuite()
            );
        }
    }
}
