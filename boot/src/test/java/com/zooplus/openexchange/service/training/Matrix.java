package com.zooplus.openexchange.service.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Matrix {
    private static Logger logger = LoggerFactory.getLogger(Matrix.class);
    private int[][] m;

    protected Matrix(int[][] m) {
        this.m = m;
    }

    public static Matrix from(int[][] m) {
        return new Matrix(m);
    }

    public Matrix mark() {
        for (int col = 0; col < m[0].length; col++) {
            if (col > 0 && m[0][col] == 0) {
                continue;
            }
            boolean zeroDetectedInRow = false;
            for (int row = 0; row < m.length; row++) {
                zeroDetectedInRow |= m[row][col] == 0;
                if(scanRowForZeros(m, row, col)){
                    fillRowWithZero(m, row);
                }
            }
            if(zeroDetectedInRow){
                fillColumnWithZero(m, col);
            }
            logger.info(toString());
        }
        return this;
    }

    private boolean scanRowForZeros(int[][] m, final int row, final int col) {
        boolean zeroDetected = false;
        for (int i = col; i < m[0].length; i++) {
            if (m[row][i] == 0) {
                if(i != col){
                    fillColumnWithZero(m, i);
                }
                zeroDetected = true;
            }
        }
        return zeroDetected;
    }

    private static void fillRowWithZero(int[][] m, final int row) {
        for (int col = 0; col < m[0].length; col++) {
            m[row][col] = 0;
        }
    }

    private static void fillColumnWithZero(int[][] m, final int col) {
        for (int row = 0; row < m.length; row++) {
            m[row][col] = 0;
        }
    }

    public boolean equalsTo(int[][] other) {
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
        for(int row = 0; row < m.length; row++){
            sb.append(String.format("%n"));
            for (int col = 0; col < m[0].length; col++) {
                sb.append(String.format("|%-4s", m[row][col]));
            }
            sb.append("|");
        }
        return sb.toString();
    }
}
