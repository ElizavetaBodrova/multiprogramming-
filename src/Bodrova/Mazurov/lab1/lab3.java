package Bodrova.Mazurov.lab1;

import java.util.Random;

public class lab3 {
    private static final int n = 3;
    private static int threadsCount = 4;
    private static int maxNumElem = n / threadsCount;
    private static Arr result = new Arr();

    public static void main(String[] args) {
        if (n < threadsCount) {
            threadsCount = n;
            maxNumElem = 1;
        }
        int[][] arr = new int[n][n];
        System.out.println("Generating matrix");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = getRandomNumberInRange(-500, getRandomNumberInRange(1, 500));
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
        int[][] arr1 = {{1, 3, 3}, {1, 3, 3}, {3, 3, 3}};
        System.out.println("Results:");
        findMaxThreads(arr1);
        System.out.println("Final max: " + result.getM() + " - Count: " + result.getC());
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static void findMaxThreads(int[][] matrix) {
        final int[][] mat = matrix;
        Thread[] t = new Thread[threadsCount];
        for (int i = 0; i < t.length; i++) {
            final int a = i;
            t[a] = new Thread(() -> {
                System.out.println(a + " - " + System.currentTimeMillis());
                int firstMatNum = a * maxNumElem;
                int lastMatNum;
                if (a + 1 == threadsCount) {
                    lastMatNum = n;
                } else {
                    lastMatNum = (a + 1) * maxNumElem;
                }
                int m = mat[firstMatNum][0];
                int c = 1;
                for (int k = firstMatNum; k < lastMatNum; k++) {
                    for (int j = 1; j < n; j++) {
                        if (mat[k][j] > m) {
                            m = mat[k][j];
                            c = 1;
                        } else if (m == mat[k][j]) {
                            c++;
                        }
                    }
                }
                setResult(m, c);
                System.out.println(a + " - " + System.currentTimeMillis() + " -> " + result.getM() + " " + result.getC());
            });
        }
        for (int i = 0; i < threadsCount; i++) {
            t[i].start();
        }
        while (true) { //ждем завершения всех потоков
            boolean b = true;
            for (int i = 0; i < threadsCount; i++) {
                b = b && !t[i].isAlive();
            }
            if (b) break;
        }
    }

    public static synchronized void setResult(int m, int c) {
        if (result.getM() == null) {
            result.setM(m);
            result.setC(c);
        }
        else if (result.getM() < m) {
            result.setM(m);
            result.setC(c);
        }
        else if (result.getM() == m) {
            result.setC(result.getC() + c);
        }
    }

    private static class Arr {
        Integer m = null;
        Integer c = 0;

        public Integer getM() {
            return m;
        }

        public void setM(Integer m) {
            this.m = m;
        }

        public Integer getC() {
            return c;
        }

        public void setC(Integer c) {
            this.c = c;
        }
    }
}