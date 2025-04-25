import java.io.*;
import java.net.*;

public class MainServer {
    public static void main(String[] args) {
        try (ServerSocket mainServerSocket = new ServerSocket(8080)) {
            System.out.println("MainServer is running on port 8080...");
            while (true) {
                try (
                    Socket connectionSocket = mainServerSocket.accept();
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream())
                ) {
                    String request = inFromClient.readLine();
                    if (request != null && request.startsWith("GET /")) {
                        System.out.println("MainServer received request: " + request);

                        String[] parts = request.split(" ");
                        if (parts.length >= 2) {
                            String fileName = parts[1].substring(1); // Remove leading '/'
                            if (fileName.isEmpty()) fileName = "index.html";

                            File file = new File(fileName);
                            if (!file.exists()) {
                                outToClient.writeBytes("HTTP/1.1 404 Not Found\r\n\r\nPage not found.");
                                continue;
                            }

                            byte[] bytes = new byte[(int) file.length()];
                            try (FileInputStream fis = new FileInputStream(file)) {
                                fis.read(bytes);
                            }

                            outToClient.writeBytes("HTTP/1.1 200 OK\r\n");
                            outToClient.writeBytes("Content-Type: text/html\r\n");
                            outToClient.writeBytes("Content-Length: " + bytes.length + "\r\n");
                            outToClient.writeBytes("\r\n");
                            outToClient.write(bytes);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
