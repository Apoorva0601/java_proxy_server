# Java Proxy Server with GUI and Web Page Caching

This project demonstrates a simple **Java Proxy Server** with a **Graphical User Interface (GUI)** that works alongside a **Main Server** to fetch, cache, and serve HTML web pages. The project also includes three well-designed HTML pages: a login form, a greeting page, and a motivational landing page.

---

## 📌 Features

- Java-based Proxy Server (port 8888) with a Swing GUI
- HTML page caching to reduce repeated requests
- Main Server (port 8080) that serves HTML files
- Clean and interactive HTML pages:
  - `index.html` – Login form with popup message
  - `hello.html` – Greeting with name input
  - `text1.html` – Motivational page with background

---

## 📁 Project Structure

📄 ProxyServerGUI.java → Proxy Server with GUI 📄 MainServer.java → Main Server serving HTML files 📄 index.html → Login Page 📄 hello.html → Greeting Page 📄 text1.html → Landing Page 📁 cached_pages/ → (Auto-created folder for cached files)


---

## 🚀 How to Run This Project

### 1. Compile Java Files
```bash
javac ProxyServerGUI.java MainServer.java

```
### 2. Compile Java Files
#### Terminal 1 – Start Main Server

```bash
java MainServer
```
#### Terminal 2 – Start Proxy Server with GUI
```bash
java ProxyServerGUI

```
---
## Test the Pages
Set your browser to use a proxy:

- Host: localhost
- Port: 8888

Then open any of these URLs in your browser:

- http://localhost/index.html
- http://localhost/hello.html
- http://localhost/text1.html

The proxy will fetch the page from the main server the first time, then serve it from the cache on future requests.

---

## Technologies Used
- Java – Socket Programming, File I/O, Swing GUI
- HTML, CSS – For styling and layout
- JavaScript – For interactive elements

---

### Author
Apoorva
MCA Student
NMAMIT,Nitte
