package com.zooplus.openexchange.service.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Matrix {
    private static Logger logger = LoggerFactory.getLogger(Matrix.class);
    private int[][] m;

    private Matrix(int[][] m) {
        this.m = m;
    }

    static Matrix from(int[][] m) {
        return new Matrix(m);
    }

    Matrix mark() {
        boolean colHasZero = false;
        boolean rowHasZero = false;

        for (int[] aM : m) {
            if (aM[0] == 0) {
                colHasZero = true;
                break;
            }
        }

        for(int col = 0; col < m[0].length; col++){
            if(m[0][col] == 0){
                rowHasZero = true;
                break;
            }
        }

        for(int row = 1; row < m.length; row++){
            for(int col = 1; col < m[0].length; col++){
                if(m[row][col] == 0){
                    m[row][0] = 0;
                    m[0][col] = 0;
                }
            }
        }

        for (int row = 1; row < m.length; row++) {
            if (m[row][0] == 0) {
                nullifyRow(m, row);
            }
        }

        for (int col = 1; col < m[0].length; col++) {
            if (m[0][col] == 0) {
                nullifyColumn(m, col);
            }
        }

        if (rowHasZero) {
            nullifyRow(m, 0);
        }

        if (colHasZero) {
            nullifyColumn(m, 0);
        }
        return this;
    }

    private static void nullifyRow(int[][] matrix, int row) {
        for (int j = 0; j < matrix[0].length; j++) {
            matrix[row][j] = 0;
        }
    }

    private static void nullifyColumn(int[][] matrix, int col) {
        for (int i = 0; i < matrix.length; i++) {
            matrix[i][col] = 0;
        }
    }


    boolean equalsTo(int[][] other) {
        if (m.length != other.length || m[0].length != other[0].length) {
            return false;
        }
        for(int row = 0; row < m.length; row++){
            for (int col = 0; col < m[0].length; col++) {
                if(m[row][col]!=other[row][col]){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] aM : m) {
            sb.append(String.format("%n"));
            for (int col = 0; col < m[0].length; col++) {
                sb.append(String.format("|%-4s", aM[col]));
            }
            sb.append("|");
        }
        return sb.toString();
    }
}
