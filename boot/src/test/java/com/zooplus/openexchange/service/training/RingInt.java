package com.zooplus.openexchange.service.training;


public class RingInt {
    private int[] b;

    private RingInt(int[] b){
        this.b = b;
    }

    public static RingInt from(int[] b){
        return new RingInt(b);
    }

    public RingInt shift(int distance){
        int pos = distance % b.length;
        if(pos < 0) {
            pos += b.length - 1;
        }
        int[] temp = new int[b.length];
        System.arraycopy(b, pos, temp, 0, b.length - pos);
        System.arraycopy(b, 0, temp, pos, pos);
        b = temp;
        return this;
    }

    public boolean equalsTo(int[] a){
        if(a == null || a.length != b.length){
            return false;
        }
        for(int i = 0; i < b.length; i++){
            if(a[i] != b[i]){
                return false;
            }
        }
        return true;
    }

    public int[] get(){
        return b;
    }

}
