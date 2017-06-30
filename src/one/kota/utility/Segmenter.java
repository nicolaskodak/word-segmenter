package one.kota.utility;
/**
 * Created by ta_ko on 2017/6/23.
 */
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class Segmenter {
    //public HashSet<String> lookup;
    public static HashMap< Integer, HashSet<String>> dictionary;
    static long startTime = System.nanoTime();
    static {
        System.out.println("Initialising Segmenter...");
        dictionary = new HashMap<Integer, HashSet<String>>();
        // UTF8
        /*
        BufferedReader reader=null;
        try{
            reader = new BufferedReader( new FileReader( "./data/Lexicon.utf8.txt"), 10 * 8192);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
        */
        // UTF16-LE
        HashSet<String> lexicon = new HashSet<String>();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader   = null;
        String word             = null;
        // ArrayList<String> lexicon = new ArrayList<String>();
        try{
            /*
            inputStream = new FileInputStream("./data/WP.bin.txt");
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-16")));
            */
            inputStream =  Segmenter.class.getClassLoader().getResourceAsStream("WP.bin.txt");
            inputStreamReader = new InputStreamReader( inputStream, Charset.forName("UTF-16") );
            reader = new BufferedReader( inputStreamReader );
        }catch(Exception e){
            e.printStackTrace();
        }

        String line;
        Integer line_length;
        HashSet<String> dictionaryByLength;
        try{
            while ((line = reader.readLine()) != null && line.length()!=0 ) {
                line_length = new Integer( line.length() );
                if( dictionary.containsKey( line_length )){ // has this length of key
                    dictionary.get( line_length ).add( line );
                } else {
                    dictionaryByLength = new HashSet<String>();
                    dictionaryByLength.add(line);
                    dictionary.put(line_length, dictionaryByLength);
                }
            }
            inputStream.close();
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Have spent " + (System.nanoTime() - startTime)/1000000000.0+ " seconds loading dictionary into memory...");
    }

    public static String cut( String string){
        String segString ="";
        segString = backwardCut( string );
        return segString;
    }

    public static String backwardCut( String string){
        String segString ="";
        Integer sub_str_length;
        String str;
        String par_str, seg_str, sub_str;
        ArrayList< int[] > strIndices;
        int sub_begin_idx, sub_end_idx;
        //seg_str="";
        str = Text.transformString( string ); // split str into multiple parts
        strIndices = Text.splitString( str ); // System.out.println( "length of strIndices after splitting=> " + strIndices.size());
        for( int j=0; j<strIndices.size(); j++){
            par_str = str.substring( strIndices.get(j)[0], strIndices.get(j)[1]);
            // System.out.println( "par_str=> " + par_str );
            seg_str = "";
            if( strIndices.get(j)[2]==1 ){ // text
                sub_begin_idx = 0;
                sub_end_idx = par_str.length();
                while( sub_begin_idx!=sub_end_idx || sub_end_idx!=0 ){
                    sub_str  = par_str.substring( sub_begin_idx, sub_end_idx);
                    // System.out.println( "sub_str=> " + par_str );
                    sub_str_length = new Integer( sub_str.length() );
                    if( !dictionary.containsKey( sub_str_length ) ){
                        sub_begin_idx += 1;
                        continue;
                    }
                    if( dictionary.get( sub_str_length ).contains( sub_str ) ){ // has current substr
                        seg_str = sub_str + " " + seg_str;
                        sub_end_idx = sub_begin_idx;
                        sub_begin_idx = 0;
                        continue;
                    }else if( sub_end_idx!=(sub_begin_idx+1) ){
                        sub_begin_idx+=1;
                    }else{
                        seg_str = sub_str + " " + seg_str;
                        sub_begin_idx = 0;
                        sub_end_idx -= 1;
                    }
                }
            }else if( strIndices.get(j)[2]==0 ){ // non-text
                // System.out.println( "non-text par_str=> " + par_str);
                if( par_str.trim().length() == 0 ){
                    // System.out.println( "Equals!=> " + par_str.trim());
                    continue;
                }
                seg_str += par_str.trim() + " " ;
            }else{
                System.out.println("Error");
            }
            segString += seg_str;
        }
        //System.out.println( segString );
        return segString;
    }

    public static String forwardCut( String string){
        String segString ="";
        Integer sub_str_length;
        String str;
        String par_str, seg_str, sub_str;
        ArrayList< int[] > strIndices;
        int sub_begin_idx, sub_end_idx;
        //seg_str="";
        str = Text.transformString( string ); // split str into multiple parts
        strIndices = Text.splitString( str ); // System.out.println( "length of strIndices after splitting=> " + strIndices.size());
        for( int j=0; j<strIndices.size(); j++){
            par_str = str.substring( strIndices.get(j)[0], strIndices.get(j)[1]);
            // System.out.println( "par_str=> " + par_str );
            seg_str = "";
            if( strIndices.get(j)[2]==1 ){ // text
                sub_begin_idx = 0;
                sub_end_idx = par_str.length();
                while( sub_begin_idx!=sub_end_idx || sub_begin_idx!=par_str.length() ){
                    System.out.println( "sub_begin_idx=" + sub_begin_idx + ", sub_end_idx=" + sub_end_idx);
                    sub_str  = par_str.substring( sub_begin_idx, sub_end_idx);
                    // System.out.println( "sub_str=> " + par_str );
                    sub_str_length = new Integer( sub_str.length() );
                    if( !dictionary.containsKey( sub_str_length ) ){
                        sub_end_idx -= 1;
                        continue;
                    }
                    if( dictionary.get( sub_str_length ).contains( sub_str ) ){ // has current substr
                        seg_str += sub_str + " ";
                        sub_begin_idx = sub_end_idx;
                        sub_end_idx = par_str.length();
                        continue;
                    }else if( sub_end_idx!=(sub_begin_idx+1) ){
                        sub_end_idx -= 1;
                    }else{
                        seg_str += sub_str + " ";
                        sub_end_idx = par_str.length();
                        sub_begin_idx += 1;
                        continue;
                    }
                }
            }else if( strIndices.get(j)[2]==0 ){ // non-text
                // System.out.println( "non-text par_str=> " + par_str);
                if( par_str.trim().length() == 0 ){
                    // System.out.println( "Equals!=> " + par_str.trim());
                    continue;
                }
                seg_str += par_str.trim() + " " ;
            }else{
                System.out.println("Error");
            }
            segString += seg_str;
        }
        //System.out.println( segString );
        return segString;
    }

    public Segmenter(){
    }
}

