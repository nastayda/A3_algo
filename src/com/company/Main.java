package com.company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        //Определить какие стркои надо добавить в T_min
        ArrayList<Integer> T_min = new ArrayList<>( );
        for (int i = 0; i < 5; i++) {
            for (int k = i + 1; k < 6; k++) {
                for (int j = 0; j < 5; j++) {
                    if (i != k) {
                        if (T[ i ][ j ] <= T[ k ][ j ]) {
                            if (j == 4) {
                                if (!T_min.contains( i )) {
                                    T_min.add( i );
                                }
                            }
                        } else if (k < 6) {
                            if (k < 5) {
                                k++;
                            }
                            if (k == 5) {
                                break;
                            }
                            if (j != 0) {
                                j = 0;
                            }
                        }
                    }
                }
            }
        }

        List<String> Tt = new ArrayList<>( );
        Tt.add( "413" );
        Tt.add( "123" );
        Tt.add( "123" );
        Tt.add( "456" );
        Set<String> s = new HashSet<>(Tt);

        for (String item : s) {
            System.out.println( item );
        }
        //Tt.removeIf( p -> p.equals( " " ) );
       /* int k = 1;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (T[ i ][ j ] <= T[ i+k ][ j ]){
                    if (j==4){
                    T_min.add( Integer.toString( i ) + " " + Integer.toString( i+1 ) );
                    }
                }
                else if (k+1 < 6) {
                    if (k < 4) {
                        k++;
                    }
                    if (i == 5) {
                        break;
                    }
                    if (j != 0) {
                        j = 0;
                    }
                }
            }
        }*/

    }
}

