/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parallelserver;
import matrix.Matrix;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ParallelServer {
    private static final int PORT = 12345;
    private static final int MAX_THREADS = 10; 
    private static final ExecutorService threadPool = 
        Executors.newFixedThreadPool(MAX_THREADS);
    
    public static void main(String[] args) {
        System.out.println("ПАРАЛЛЕЛЬНЫЙ СЕРВЕР с пулом потоков");
        System.out.println("Порт: " + PORT + ", Максимум потоков: " + MAX_THREADS);
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен. Ожидание подключений...");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientInfo = clientSocket.getInetAddress() + ":" + clientSocket.getPort();
                System.out.println("Подключен клиент: " + clientInfo);
                
                threadPool.submit(() -> processClient(clientSocket, clientInfo)); //лямбда
            }
            
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
            System.out.println("Пул потоков завершен");
        }
    }
    
    private static void processClient(Socket clientSocket, String clientInfo) {
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + threadName + "] Обработка клиента: " + clientInfo);
        
        try (clientSocket;
             ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            
            clientSocket.setSoTimeout(30000);
            
            Object obj = ois.readObject();
            if (!(obj instanceof Matrix)) {
                oos.writeDouble(0);
                oos.flush();
                return;
            }
            
            Matrix matrix = (Matrix) obj;
            System.out.println("[" + threadName + "] Получена матрица " + 
                matrix.getRows() + "x" + matrix.getCols());
            
            double sum = Matrix.sumNumb(matrix);
            System.out.println("[" + threadName + "] Сумма нечетных: " + sum);
            
            oos.writeDouble(sum);
            oos.flush();
            System.out.println("[" + threadName + "] Результат отправлен");
            
        } catch (SocketTimeoutException e) {
            System.err.println("[" + threadName + "] Таймаут: клиент " + clientInfo);
        } catch (ClassNotFoundException e) {
            System.err.println("[" + threadName + "] Ошибка десериализации: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[" + threadName + "] Сетевая ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[" + threadName + "] Неизвестная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
