package com.company;

import com.sun.deploy.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main( String[] args ) {
        int[][] T1 = { { 1, 1, 0, 1 },
                { 0, 0, 0, 1 } };
        int[][] T2 = { { 1, 0, 1, 0 },
                { 0, 1, 1, 0 } };
        int[] X = { 0, 1, 0, 1 };
        System.out.println( "Универсальный алгоритм нахождения тупиковых тестов" );
        System.out.println( "------------------" );
        //Универсальный алгоритм нахождения тупиковых тестов
        //ШАГ 1 - XOR------------------
        int[][] T = sumByMod( T1, T2 );
        //ШАГ 2 - MIN------------------
        //Определяем строки для добавления
        ArrayList<Integer> tMinIndexRows = determinWhatRowsAddToMinMass( T );
        //Добавляем строки
        int[][] tMinMatrix = addRowsToMinMass( T, tMinIndexRows );
        //ШАГ 3 - ДНФ------------------
        //Предварительный массив ДНФ
        ArrayList<Integer> massDNF = generateMassWithDNF( tMinMatrix );
        //Обработка массива ДНФ
        //Убрать повторяющиеся символы в элементах листа
        ArrayList<Integer> massDNFDeleteDoublesInItems = new ArrayList<>( );
        for (int i = 0; i < massDNF.size( ); i++) {
            massDNFDeleteDoublesInItems.add( Integer.parseInt( deleteDoubleSymbolsInElements( massDNF.get( i ).toString( ) ) ) );
        }
        //Убрать дубликаты в листе
        ArrayList<Integer> massDNFPreResult = deleteDuplicatesFromDNFList( massDNFDeleteDoublesInItems );
        ArrayList<String> mDNFString = new ArrayList<>( );
        for (int item : massDNFPreResult) {
            mDNFString.add( Integer.toString( item ) );
        }
        deleteSameDNFElements( mDNFString );
        //АЛГОРИТМ А3------------------
        //Для подсчета весов
        Map<Character, Integer> countChar = getCharacterIntegerMap( mDNFString );
        //Посчитать веса
        ArrayList<Double> p = countWeghts( countChar );

        double z = 0.0;
        z = getZ( T1, T2, p );
        int k = 0;
        //Инвертируем столбцы матрицы пока z не будет больше 0
        while (z < 0) {
            System.out.println( "Инвертируем столбцы матрицы пока z не будет больше 0" );
            for (int i = 0; i < T1.length; i++) {
                if (T1[ i ][ k ] == 0) {
                    T1[ i ][ k ] = 1;
                } else T1[ i ][ k ] = 0;
            }
            z = getZ( T1, T2, p );
            k++;
        }
        // double h = 0.5*(findMaxMin( AlphaMin, "max" )-findMaxMin( Alphas, "min" ));
        double h = 0.5 * z;
        double xp = 0;
        for (int i = 0; i < p.size( ); i++) {
            xp += p.get( i ) * X[ i ];
        }

    }

    public static double getZ( int[][] t1, int[][] t2, ArrayList<Double> p ) {
        double z;
        double[] AlphasT1 = getAlphas( p, t1 );
        double[] AlphasT2 = getAlphas( p, t2 );
        double AlphaMax = findMaxMin( AlphasT2, "max" );
        double AlphaMin = findMaxMin( AlphasT1, "min" );
        System.out.printf( "Вычисляем AlphaMin: %s и AlphaMax: %s", AlphaMin, AlphaMax );
        System.out.println( );
        System.out.println( "------------------" );
        z = AlphaMin - AlphaMax;
        System.out.printf( "Вычисляем z: %s", z );
        System.out.println( );
        System.out.println( "------------------" );
        return z;
    }

    public static ArrayList<Double> countWeghts( Map<Character, Integer> countChar ) {
        ArrayList<Double> p = new ArrayList<>( );
        for (Integer item : countChar.values( )) {
            p.add( (double) item / ( countChar.values( ).size( ) ) );
        }
        System.out.println( "Веса" );
        System.out.println( "------------------" );
        for (Double item : p) {
            System.out.print( item + " " );
        }
        System.out.println( );
        System.out.println( "------------------" );
        return p;
    }

    public static Map<Character, Integer> getCharacterIntegerMap( ArrayList<String> mDNFString ) {
        String DNFstring = StringUtils.join( mDNFString, "" );
        List<Character> textInChar = DNFstring.chars( ).mapToObj( e -> (char) e ).collect( Collectors.toList( ) );
        return textInChar.stream( ).collect( HashMap::new, ( m, c ) -> {
            if (m.containsKey( c ))
                m.put( c, m.get( c ) + 1 );
            else
                m.put( c, 1 );
        }, HashMap::putAll );
    }

    public static int[][] sumByMod( int[][] t1, int[][] t2 ) {
        int[][] t = new int[ 4 ][ 4 ];
        for (int i = 0; i < t.length; i++) {
            for (int k = 0; k < 2; k++) {
                for (int h = 1; h >= 0; h--) {
                    for (int j = 0; j < t[ 0 ].length; j++) {
                        t[ i ][ j ] = ( t1[ k ][ j ] ^ t2[ h ][ j ] );//mod2
                    }
                    i++;
                }
            }
        }
        System.out.println( "ШАГ 1 - XOR" );
        System.out.println( "-----------------" );
        printMatrixInt( t );
        System.out.println( "-----------------" );
        return t;
    }

    public static void printMatrixInt( int[][] t ) {
        for (int h = 0; h < t.length; h++) {
            for (int j = 0; j < t[ 0 ].length; j++) {
                System.out.print( t[ h ][ j ] + " " );
            }
            System.out.println( );
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

    private static ArrayList<Integer> deleteDuplicatesFromDNFList( ArrayList<Integer> massKNFDeleteDoublesInItems ) {
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

    private static String deleteDoubleSymbolsInElements( String string ) {
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

    private static void deleteSameDNFElements( ArrayList<String> massKNF ) {
        for (int j = 0; j < massKNF.size( ); j++) {
            String item = massKNF.get( j );
            massKNF.removeIf( p -> p.contains( item.toString( ) ) && p.length( ) > item.toString( ).length( ) );
        }
        System.out.println( "Тупиковые тесты" );
        System.out.println( "-----------------" );
        printMassString( massKNF );
        System.out.println( "-----------------" );
    }

    private static int[][] addRowsToMinMass( int[][] t, ArrayList<Integer> tMinIndexRows ) {
        //Добавить строки в tMinMatrix
        int[][] tMinMatrix = new int[ tMinIndexRows.size( ) ][ t[ 0 ].length ];
        for (int i = 0; i < tMinIndexRows.size( ); i++) {
            for (int j = 0; j < t[ i ].length; j++) {
                tMinMatrix[ i ][ j ] = t[ tMinIndexRows.get( i ) ][ j ];
            }
        }
        System.out.println( "ШАГ 2 - MIN" );
        System.out.println( "-----------------" );
        printMatrixInt( tMinMatrix );
        System.out.println( "-----------------" );
        return tMinMatrix;
    }

    private static ArrayList<Integer> determinWhatRowsAddToMinMass( int[][] t ) {
        //Определить какие стркои надо добавить в tMinIndexRows
        ArrayList<Integer> tMinIndexRows = new ArrayList<>( );
        for (int i = 0; i < t.length; i++) {
            for (int k = i + 1; k < t[ 0 ].length; k++) {
                for (int j = 0; j < t.length; j++) {
                    if (i != k) {
                        if (t[ i ][ j ] <= t[ k ][ j ]) {
                            if (j == t.length - 1) {
                                if (!tMinIndexRows.contains( i )) {
                                    tMinIndexRows.add( i );
                                }
                            }
                        } else if (k < t[ 0 ].length) {
                            if (k < t.length) {
                                k++;
                            }
                            if (k == t.length) {
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

    private static ArrayList<Integer> generateMassWithDNF( int[][] tMinMatrix ) {
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
