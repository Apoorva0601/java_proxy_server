import java.awt.BorderLayout;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;

public class ProxyServerGUI extends JFrame {
    private JTextArea logArea;
    private JButton startButton;
    private boolean isRunning = false;

    public ProxyServerGUI() {
        setTitle("Proxy Server GUI");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        startButton = new JButton("Start Proxy Server");
        startButton.addActionListener(e -> {
            if (!isRunning) {
                isRunning = true;
                startButton.setEnabled(false);
                new Thread(this::startProxyServer).start();
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }

    private void startProxyServer() {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            log("ProxyServer is running on port 8888...");
            File cacheDir = new File("cached_pages");
            if (!cacheDir.exists()) {
                cacheDir.mkdir();
                log("Created cache directory: cached_pages");
            }

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                    String request = in.readLine();

                    if (request != null && request.startsWith("GET /")) {
                        log("Received request: " + request);

                        String[] parts = request.split(" ");
                        if (parts.length >= 2) {
                            String fileName = parts[1].substring(1);
                            if (fileName.isEmpty()) fileName = "index.html";

                            File cachedFile = new File("cached_pages/" + fileName);
                            if (!cachedFile.exists()) {
                                log("Fetching " + fileName + " from MainServer...");
                                try (Socket mainServerSocket = new Socket("localhost", 8080);
                                     BufferedReader mainServerIn = new BufferedReader(new InputStreamReader(mainServerSocket.getInputStream()));
                                     DataOutputStream mainServerOut = new DataOutputStream(mainServerSocket.getOutputStream())) {

                                    mainServerOut.writeBytes("GET /" + fileName + " HTTP/1.1\r\n\r\n");

                                    StringBuilder pageContent = new StringBuilder();
                                    String line;
                                    while ((line = mainServerIn.readLine()) != null) {
                                        pageContent.append(line).append("\n");
                                    }

                                    try (FileWriter writer = new FileWriter(cachedFile)) {
                                        writer.write(pageContent.toString());
                                    }

                                    out.writeBytes(pageContent.toString());
                                    log("Cached: " + fileName);
                                }
                            } else {
                                log("Serving " + fileName + " from cache...");
                                try (BufferedReader cachedIn = new BufferedReader(new FileReader(cachedFile))) {
                                    String cachedLine;
                                    while ((cachedLine = cachedIn.readLine()) != null) {
                                        out.writeBytes(cachedLine + "\n");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            log("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProxyServerGUI().setVisible(true));
    }
}
