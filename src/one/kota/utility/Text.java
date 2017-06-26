package one.kota.utility; /**
 * Created by Kodak on 6/24/17.
 */
//import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
public class Text {
    public static void main(String[] args) throws Exception{

        //String string = "你好kota!最近 有去Taipei    101嗎?";
        System.out.println();
        String string = "你喜歡吃什麼？";

        string = transformString( string );
        System.out.println( "after transform=> " + string );
        ArrayList< int[] > textIndices = splitString( string );

        for(int[] idxPair: textIndices){
            if( idxPair[2]==1 ){
                System.out.println( "text=> " + string.substring( idxPair[0], idxPair[1] ) );
            }else if( idxPair[2]==0){
                System.out.println( "non-text=> " + string.substring( idxPair[0], idxPair[1] ) );
            }else{
                throw new Exception();
            }
        }
        /*
        String[] testStrArr = { "｜＝「」、’；！＠＃＄％＾＆＊（）＿＋『』，．／", "'`~!@#$%^&*()", "\"", "\\/", "[]{}", "+-_=?|", ",.<>:;"};
        for( String s: testStrArr){
            System.out.println( transformString(s).length() );
        }
        */
    }
    public static String transformString( String string){
        string = string.replaceAll("( )+", " ");
        // System.out.println( string );
        string = string.replaceAll("[`~!@#$%\\^&*\\(\\)\\-_+=\\|\\}\\]\\[\\{\"':;/?>\\.<,]+", " ");
        // System.out.println( string );
        string = string.replaceAll( "[？。｜＝「」、’；！＠＃＄％＾＆＊（）＿＋『』，．／]+", " ");
        return string.trim();
    }

    public static ArrayList<int[]> splitString(String string){
        Pattern pat;
        Matcher mat;
        ArrayList< int[] > indices = new ArrayList< int[] >();
        pat = Pattern.compile("[\\w0-9\\s]+");
        mat = pat.matcher( string );
        int idx=0;
        while( mat.find() ){
            //System.out.println( "start=" + mat.start() + ", end=" + mat.end() +
            //        ", str[]=" + string.substring( mat.start(), mat.end()) +
            //        ", group()=" + mat.group() );
            indices.add( new int[]{ mat.start(), mat.end()} );
        }
        ArrayList< int[] > textIndices = new ArrayList< int[] >();
        if( indices.size()!=0 ){
            if( indices.get(0)[0]!= 0 ){
                textIndices.add( new int[]{ 0, indices.get(0)[0], 1 } );
                //System.out.println( string.substring( 0, indices.get(0)[0]));
            }
            for(int i=0; i<(indices.size()-1); i++){
                textIndices.add( new int[]{indices.get(i)[0], indices.get(i)[1], 0} );
                //System.out.println( string.substring( indices.get(i)[0], indices.get(i)[1]));
                textIndices.add( new int[]{indices.get(i)[1], indices.get(i+1)[0], 1} );
                //System.out.println( string.substring( indices.get(i)[1], indices.get(i+1)[0]));
            }
            textIndices.add( new int[]{indices.get( indices.size()-1 )[0], indices.get( indices.size()-1 )[1], 0} );
            //System.out.println( string.substring( indices.get( indices.size()-1 )[0], indices.get( indices.size()-1 )[1]));

            if( indices.get( indices.size()-1 )[1]!= string.length()){
                textIndices.add( new int[]{indices.get( indices.size()-1 )[1], string.length(), 1} );
                //System.out.println( string.substring( indices.get( indices.size()-1 )[1], string.length() ));
            }
        }else{
            textIndices.add( new int[]{0, string.length(), 1} );
        }
        return textIndices;
    }
}
