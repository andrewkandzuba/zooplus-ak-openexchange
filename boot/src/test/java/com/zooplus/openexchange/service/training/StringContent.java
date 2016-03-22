package com.zooplus.openexchange.service.training;

public class StringContent {

    private char[] intern;

    public StringContent(String s) {
        this.intern = s.toCharArray();
    }

    public StringContent replace() {
        final char[] replacement = {'%', '2', '0'};

        // 0. trim
        int fns = 0;
        int lns = intern.length - 1;
        do {
            if (intern[fns] == ' ') {
                fns++;
            }
            if (intern[lns] == ' ') {
                lns--;
            }
            if (intern[fns] != ' ' && intern[lns] != ' ') {
                break;
            }
        } while (fns < lns);

        // 1. Calculate number of spaces and allocate new array size;
        char[] cs = new char[0];
        int sfns = fns;
        while (fns < lns) {
            while (fns <= lns && intern[fns] != ' ') {
                fns++;
            }
            int l = fns - sfns;
            if (cs.length == 0) {
                cs = new char[l];
                System.arraycopy(intern, sfns, cs, 0, l);
            } else {
                char[] temp = new char[cs.length + 3 + l];
                System.arraycopy(cs, 0, temp, 0, cs.length);
                System.arraycopy(replacement, 0, temp, cs.length, 3);
                System.arraycopy(intern, sfns, temp, cs.length + 3, l);
                cs = temp;
            }
            while (fns <= lns && intern[fns] == ' ') {
                fns++;
            }
            sfns = fns;
        }
        intern = cs;
        return this;
    }

    public String get() {
        return String.copyValueOf(intern);
    }

}
