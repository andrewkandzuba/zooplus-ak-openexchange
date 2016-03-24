package com.zooplus.openexchange.service.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Matrix {
    private static final Logger logger = LoggerFactory.getLogger(Matrix.class);


    private int[][] m;

    protected Matrix(int size) {
        this.m = new int[size][size];
        int v = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                m[col][row] = v++;
            }
        }
    }

    public static Matrix init(int size){
        return new Matrix(size);
    }

    public Matrix rotate2steps(){
        int N = m.length;
        for(int row = 0; row < (N/2); row++){
            for(int col = 0; col < N; col++){
                int temp = m[col][N - row - 1];
                m[col][N - row - 1] = m[col][row];
                m[col][row] = temp;
            }
        }
        logger.info(toString());
        for(int c = 0; c < N; c++){
            for(int r = c;  r < N; r++){
                int temp =  m[c][r];
                m[c][r] = m[r][c];
                m[r][c] = temp;
            }
        }
        return this;
    }

    public Matrix rotate1step(){
        int N = m.length;
        for(int layer = 0; layer < N / 2; layer++){
            int last = N - 1 - layer;
            for(int offset = layer; offset < last; ++offset){
                int tmp = m[offset][layer];
                m[offset][layer] =  m[layer][last - offset];
                m[offset][layer] = m[layer][last - offset];
                m[layer][last - offset] = m[last - offset][last - offset];
                m[last - offset][last - offset] = m[last - offset][layer];
                m[last - offset][layer] = tmp;
            }
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int col = 0; col < m.length; col++){
            sb.append(String.format("%n"));
            for(int row = 0; row < m.length; row++){
                sb.append(String.format("|%-4s",m[row][col]));
            }
            sb.append("|");
        }
        return sb.toString();
    }
}
