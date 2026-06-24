/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matrix;
import java.io.Serializable;
import java.util.Random;

public class Matrix implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double[][] matrix;
    private int rows;
    private int colums;
    
    public Matrix(int rows, int colums) {
        if (rows <= 0 || colums <= 0) {
            throw new IllegalArgumentException("Size>0");
        }
        
        this.colums = colums;
        this.rows = rows;
        this.matrix = new double[rows][colums];
        
        Random rnd = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colums; j++) {
                matrix[i][j] = -10 + rnd.nextDouble() * 20;
            }
        }
    }
    
     public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return colums;
    }
    
    public double getElement(int row, int colum) {
        if (row < 0 || row >= rows || colum < 0 || colum >= colums) {
            throw new IndexOutOfBoundsException("Неверные индексы");
        }
        return matrix[row][colum];
    }
    
    public void setElement(int row, int colum, double value) {
        if (row < 0 || row >= rows || colum < 0 || colum >= colums) {
            throw new IndexOutOfBoundsException("Неверные индексы");
        }
        matrix[row][colum] = value;
    }
    
    public static double sumNumb(Matrix matrix) 
    {
        double sum = 0.0;
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                double value = matrix.getElement(i, j);
                if (Math.abs(value % 2) > 0.001) {
                    sum += value;
                }
            }
        }
        return sum;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colums; j++) {
                sb.append(String.format("%8.2f", matrix[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }   
}

