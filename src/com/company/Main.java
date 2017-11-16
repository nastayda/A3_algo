package com.company;

import java.util.*;
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
        //Определить какие стркои надо добавить в tMinIndexRows
        ArrayList<Integer> tMinIndexRows = new ArrayList<>( );
        for (int i = 0; i < 5; i++) {
            for (int k = i + 1; k < 6; k++) {
                for (int j = 0; j < 5; j++) {
                    if (i != k) {
                        if (T[ i ][ j ] <= T[ k ][ j ]) {
                            if (j == 4) {
                                if (!tMinIndexRows.contains( i )) {
                                    tMinIndexRows.add( i );
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

        //Добавить строки в tMinMatrix
        int[][] tMinMatrix = new int[ tMinIndexRows.size( ) ][ T[ 0 ].length ];
        for (int i = 0; i < tMinIndexRows.size( ); i++) {
            for (int j = 0; j < T[ i ].length; j++) {
                tMinMatrix[ i ][ j ] = T[ tMinIndexRows.get( i ) ][ j ];
            }
        }
        /*
        for (int i = 0; i < tMinIndexRows.size( ); i++) {
            for (int j = 0; j < T[ i ].length; j++) {
                System.out.print( tMinMatrix[i][j] +" ");
            }
            System.out.println(" " );
        }*/

        //Составление КНФ
        for (int i = 0; i < tMinMatrix.length; i++) {
            for (int k = 0; k < tMinMatrix[ 0 ].length; k++) {
                for (int h = 0; h > tMinMatrix[ 0 ].length; h++) {
                    for (int j = 0; j < tMinMatrix[ 0 ].length; j++) {
                        //T[i][j] = ( T1[k][j] ^ T2[h][j] );//mod2
                    }
                    i++;
                }
            }
        }

        List<String> Tt = new ArrayList<>( );
        Tt.add( "13" );
        Tt.add( "31" );
        Tt.add( "0" );
        Tt.add( "0" );
        Tt.add( "0" );

        Tt.removeIf( p -> p.equals( "0" ) );
        Set<String> s = new HashSet<>( Tt );
        for (String item : Tt) {
            System.out.println( item );
        }
        //Получение p
        ArrayList<String> DNF = new ArrayList<>( );
        DNF.add( "12" );

        //
       /* int k = 1;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (T[ i ][ j ] <= T[ i+k ][ j ]){
                    if (j==4){
                    tMinIndexRows.add( Integer.toString( i ) + " " + Integer.toString( i+1 ) );
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

