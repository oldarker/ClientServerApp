/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package serialserver;

import java.io.*;
import java.net.*;
import matrix.Matrix;

public class SerialServer {
    private static final int PORT = 12344;
    
    public static void main(String[] args) {
        
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
            System.setErr(new PrintStream(System.err, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("ПОСЛЕДОВАТЕЛЬНЫЙ СЕРВЕР");
        System.out.println("Порт: " + PORT);
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен. Ожидание подключений...");
            
            while (true) {
                
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клиент подключен: " + clientSocket.getInetAddress());
                
                processClient(clientSocket);
                
                System.out.println("Клиент обработан. Ожидаем следующего...");
            }
            
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void processClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            
            Matrix matrix = (Matrix) ois.readObject();
            System.out.println("Получена матрица " + 
                matrix.getRows() + "x" + matrix.getCols());
            
            double sum = Matrix.sumNumb(matrix);
            System.out.println("Сумма нечетных элементов: " + sum);
            
            oos.writeDouble(sum);
            oos.flush();
            System.out.println("Результат отправлен");
            
        } catch (ClassNotFoundException e) {
            System.err.println("Ошибка: неверный формат данных");
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
