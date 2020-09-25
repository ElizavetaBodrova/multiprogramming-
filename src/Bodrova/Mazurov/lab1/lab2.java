package Bodrova.Mazurov.lab1;

import java.util.Random;

public class lab2 {
    private static final int n = 300;
    private static int threadsCount = 4;
    private static int maxNumElem = n / threadsCount;
    private static int[][] max;

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
        int[][] arr1 = {{1, 3, 3}, {1, 3, 3}, {3, 3, 3}}; //тестоввый массив смотри на n!!!
        System.out.println("Results:");
        Arr result = findMaxThreads(arr);
        System.out.println("Final max: " + result.getM() + " - Count: " + result.getC());
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static Arr findMaxThreads(int[][] matrix) {
        final int[][] mat = matrix;
        max = new int[threadsCount][2];
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
                max[a][0] = m;
                max[a][1] = c;
                System.out.println(a + " - " + System.currentTimeMillis() + " -> " + m + " " + c);
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
        int m = max[0][0];
        int c = max[0][1];
        for (int i = 1; i < threadsCount; i++) {
            if (max[i][0] > m) {
                m = max[i][0];
                c = max[i][1];
            } else if (max[i][0] == m) {
                c += max[i][1];
            }
        }
        return new Arr(m, c);
    }

    private static class Arr {
        Integer m = null;
        Integer c = 0;

        public Arr() {
        }

        public Arr(Integer m, Integer c) {
            this.m = m;
            this.c = c;
        }

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
