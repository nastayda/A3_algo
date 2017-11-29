package com.company;

import com.sun.deploy.util.StringUtils;

import java.util.*;
import java.util.function.Function;
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
        //Определяем строки для добавления
        ArrayList<Integer> tMinIndexRows = determinWhatRowsAddToMinMass( T );
        //Добавляем строки
        int[][] tMinMatrix = addRowsToMinMass( T, tMinIndexRows );
        //Составление КНФ
        //Предварительный массив КНФ
        ArrayList<Integer> massKNF = generateMassWithKNF( tMinMatrix );
        //Обработка массива КНФ
        //Убрать повторяющиеся символы
        ArrayList<Integer> massKNFDeleteDoublesInItems = new ArrayList<>( );
        for (int i = 0; i < massKNF.size( ); i++) {
            massKNFDeleteDoublesInItems.add( Integer.parseInt( deleteDoublesSymbolsSec( massKNF.get( i ).toString( ) ) ) );
        }
        //Убрать дубликаты
        ArrayList<Integer> massKNFPreResult = deleteDuplicatesFromKNF( massKNFDeleteDoublesInItems );
        ArrayList<String> mKNFString = new ArrayList<>( );
        for (int item : massKNFPreResult) {
            mKNFString.add( Integer.toString( item ) );
        }
        deleteSameKNFElements( mKNFString );

        //Для подсчета весов
        String KNFstring = StringUtils.join( mKNFString, "" );
        System.out.println( KNFstring );
        List<Character> textInChar = KNFstring.chars( ).mapToObj( e -> (char) e ).collect( Collectors.toList( ) );
        Map<Character, Integer> countChar = textInChar.stream( ).collect( HashMap::new, ( m, c ) -> {
            if (m.containsKey( c ))
                m.put( c, m.get( c ) + 1 );
            else
                m.put( c, 1 );
        }, HashMap::putAll );
        ArrayList<Double> p = new ArrayList<Double>( );
        for (Integer item : countChar.values( )) {
            //p.add( (double) item / ( countChar.values( ).size( ) ) );
            p.add( 1.0 / 3 );
        }

        int[] X = { 1, 1, 1, 0, 1 };
        int[][] T1 = {
                { 1, 1, 0, 1 },
                { 0, 0, 0, 1 },
                { 1, 0, 1, 1 }
        };
        double[] Alphas = getAlphas( p, T1 );

        double AlphaMax = findMaxMin( Alphas, "max" );
        double AlphaMin = findMaxMin( Alphas, "min" );

        double z = AlphaMin - AlphaMax;
        
        if (z<0){
            //Инвертируем
        }
    }

    private static double findMaxMin( double[] vals, String defineVar ) {
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        if (defineVar.contains( "max" )) {
            for (double d : vals) {
                if (d > max) max = d;
            }
            return max;
        } else {
            for (double d : vals) {
                if (d < min) min = d;
            }
            return min;
        }

    }

    public static double[] getAlphas( ArrayList<Double> p, int[][] t1 ) {
        double[] Alphas = new double[ t1.length ];
        for (int i = 0; i < Alphas.length; i++) {
            for (int j = 0; j < p.size( ); j++)
                Alphas[ i ] += p.get( j ) * t1[ i ][ j ];
        }
        return Alphas;
    }

    private static void getR( ArrayList<Double> p, int[] x, int[][] t1 ) {
        double sum = 0;
        for (int i = 0; i < t1.length; i++) {
            for (int j = 0; j < p.size( ); j++) {
                sum += p.get( j ) * ( t1[ i ][ j ] ^ x[ j ] );
            }
        }
        double r = ( 1.0 / t1.length ) * sum;
        System.out.println( r );
    }

    public static ArrayList<Integer> deleteRepeatedElements( ArrayList<Integer> massKNFPreResult ) {
        ArrayList<Integer> massKNFResult = new ArrayList<>( );
        for (int i = 0; i < massKNFPreResult.size( ); i++) {
            String item = massKNFPreResult.get( i ).toString( );
            for (int j = 0; j < massKNFPreResult.size( ); j++) {
                String itemForCompare = massKNFPreResult.get( j ).toString( );
                if (i != j) {
                    if (itemForCompare.contains( item )) {
                        if (!massKNFResult.contains( Integer.parseInt( item ) )) {
                            massKNFResult.add( Integer.parseInt( item ) );
                        }
                    }
                }
            }
        }
        //ArrayList <Integer> massKNFResultNew = new ArrayList <>(massKNFResult);
        //massKNFResult.clear();
        //deleteRepeatedElements(massKNFResultNew);
        // massKNFResultNew.clear();
        return massKNFResult;
    }

    private static ArrayList<Integer> deleteDuplicatesFromKNF( ArrayList<Integer> massKNFDeleteDoublesInItems ) {
        ArrayList<Integer> l1 = new ArrayList( massKNFDeleteDoublesInItems );
        ArrayList<Integer> l2 = new ArrayList( );
        Iterator iterator = l1.iterator( );
        while (iterator.hasNext( )) {
            int o = (int) iterator.next( );
            if (!l2.contains( o )) l2.add( o );
        }
        return l2;
    }

    private static void printMassString( ArrayList<String> massKNFDeleteDoublesInItems ) {
        for (String item : massKNFDeleteDoublesInItems) {
            System.out.print( item + " " );
        }
        System.out.println( );
    }

    private static void printMassInt( ArrayList<Integer> massKNF ) {
        for (int item : massKNF) {
            System.out.print( item + " " );
        }
        System.out.println( );
    }

    private static String deleteDoublesSymbolsSec( String string ) {
        Set<Character> chars = new HashSet( );
        for (int i = 0; i < string.length( ); i++) {
            chars.add( string.charAt( i ) );
        }
        return chars.toString( ).replaceAll( "[\\[\\]]", "" ).replaceAll( ", ", "" );
    }

    private static void deleteDoubleSymbols( String str ) {
        //https://ru.stackoverflow.com/questions/722427/Как-удалить-одинаковые-буквы-из-строки-java-se

        Map<Integer, Long> frequencies = str.toLowerCase( )
                .codePoints( )
                .boxed( )
                .collect( Collectors.groupingBy(
                        Function.identity( ), Collectors.counting( ) ) );

        String duplicates = frequencies.entrySet( )
                .stream( )
                .filter( e -> e.getValue( ) > 1 )
                .map( Map.Entry::getKey )
                .map( Character::toChars )
                .map( String::valueOf )
                .filter( i -> !i.equals( " " ) )
                .collect( Collectors.joining( ) );

        System.out.println( str.replaceAll( "(?i)[" + duplicates + "]", "" ) );
    }

    private static void deleteSameKNFElements( List<String> massKNF ) {
        for (int j = 0; j < massKNF.size( ); j++) {
            String item = massKNF.get( j );
            massKNF.removeIf( p -> p.contains( item.toString( ) ) && p.length( ) > item.toString( ).length( ) );
        }
        for (String key : massKNF) {
            System.out.print( key + " " );
        }
    }

    private static int[][] addRowsToMinMass( int[][] t, ArrayList<Integer> tMinIndexRows ) {
        //Добавить строки в tMinMatrix
        int[][] tMinMatrix = new int[ tMinIndexRows.size( ) ][ t[ 0 ].length ];
        for (int i = 0; i < tMinIndexRows.size( ); i++) {
            for (int j = 0; j < t[ i ].length; j++) {
                tMinMatrix[ i ][ j ] = t[ tMinIndexRows.get( i ) ][ j ];
            }
        }
        return tMinMatrix;
    }

    private static ArrayList<Integer> determinWhatRowsAddToMinMass( int[][] t ) {
        //Определить какие стркои надо добавить в tMinIndexRows
        ArrayList<Integer> tMinIndexRows = new ArrayList<>( );
        for (int i = 0; i < 5; i++) {
            for (int k = i + 1; k < 6; k++) {
                for (int j = 0; j < 5; j++) {
                    if (i != k) {
                        if (t[ i ][ j ] <= t[ k ][ j ]) {
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
        return tMinIndexRows;
    }

    private static ArrayList<Integer> generateMassWithKNF( int[][] tMinMatrix ) {
        //Присвоить первую строку КНФ массиву
        ArrayList<Integer> testRowFirst = new ArrayList<>( );
        for (int i = 0; i < tMinMatrix[ 0 ].length; i++) {
            testRowFirst.add( tMinMatrix[ 0 ][ i ] );
        }
        ArrayList<Integer> testRowThird;
        //Для всех элементов КНФ умножаем на все элементы по строкам существующей матрицы
        for (int i = 1; i < tMinMatrix.length; i++) {
            int[] testRowSecond = new int[ tMinMatrix[ 0 ].length ];
            for (int j = 0; j < tMinMatrix[ 0 ].length; j++) {
                testRowSecond[ j ] = tMinMatrix[ i ][ j ];
            }
            //Выливаю все в третий лист
            testRowThird = mapResult( testRowFirst, testRowSecond );
            //Удаляю все элементы которые были в первом листе
            testRowThird.removeAll( testRowFirst );
            testRowFirst.clear( );
            testRowFirst.addAll( testRowThird );
            testRowThird.clear( );
        }
        return testRowFirst;
    }

    public static ArrayList<Integer> mapResult( ArrayList<Integer> M1, int[] M2 ) {
        ArrayList<Integer> resMass = new ArrayList<>( );
        for (int i = 0; i < M2.length; i++) {
            for (int j = 0; j < M1.size( ); j++) {
                if (M2[ i ] * M1.get( j ) > 0) {
                    if (M1.size( ) < 6) {
                        resMass.add( Integer.parseInt( Integer.toString( i + 1 ) + Integer.toString( j + 1 ) ) );
                    } else {
                        resMass.add( Integer.parseInt( Integer.toString( i + 1 ) + Integer.toString( M1.get( j ) ) ) );
                    }
                }
            }
        }
        return resMass;
    }
}
