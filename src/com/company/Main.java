package com.company;

import java.util.ArrayList;

public class Main {

    public static void main( String[] args ) {
        int[][] T = {
                { 1, 1, 0, 1, 0 },
                { 0, 1, 1, 1, 0 },
                { 1, 0, 1, 1, 0 },
                { 1, 1, 0, 1, 1 },
                { 0, 1, 1, 1, 1 },
                { 1, 0, 1, 1, 1 }
        };
        ArrayList<Integer> T_min = new ArrayList<>( );
        for (int i = 0; i < 5; i++) {
            for (int k = 1; k < 5; k++) {
                for (int j = 0; j < 5; j++) {
                    if (T[ i ][ j ] <= T[ k ][ j ]) {
                        if (j == 4) {
                            T_min.add( i );
                        }
                    } else if (k != 4) {
                        k++;
                        if (j != 0) {
                            j = 0;
                        }
                    }
                }
            }
        }

        for (int item : T_min) {
            System.out.println( T_min );
        }
        System.out.println( );
    }
}

