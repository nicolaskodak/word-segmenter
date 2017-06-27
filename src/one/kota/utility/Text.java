package one.kota.utility; /**
 * Created by Kodak on 6/24/17.
 */
//import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;

public class Text {
    public static void main(String[] args) throws Exception{
        // testTransformString();
        // testSplitString();
        testTrimTag();
        // testReduceMandarinSpace();
    }

    public static String transformString( String string){
        string = string.replaceAll("( )+", " ");
        // System.out.println( string );
        string = string.replaceAll("[`~!@#$%\\^&*\\(\\)\\-_+=\\|\\}\\]\\[\\{\"':;/?>\\.<,]+", " ");
        // System.out.println( string );
        string = string.replaceAll( "[？。｜＝「」、’；！＠＃＄％＾＆＊（）＿＋『』，．／]+", " ");
        return string.trim();
    }

    public static void testTransformString( ){
        /*
        String[] testStrArr = { "｜＝「」、’；！＠＃＄％＾＆＊（）＿＋『』，．／", "'`~!@#$%^&*()", "\"", "\\/", "[]{}", "+-_=?|", ",.<>:;"};
        for( String s: testStrArr){
            System.out.println( transformString(s).length() );
        }
        */
        String string = "你好kota!最近 有去Taipei    101嗎?";
        // String string = "你喜歡吃什麼？";
        System.out.println( "before transform=> " + string );
        string = transformString( string );
        System.out.println( "after transform=> " + string );
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

    public static void testSplitString( ) throws Exception{
        String string = "你好kota!最近 有去Taipei    101嗎?";
        string = transformString( string );
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
    }

    public static String trimTag( String string){
        String trimmedString = "";
        Pattern pattern = Pattern.compile( "(?:<[a-zA-Z0-9\"/=\\s]+>)([^<>]+)(?:<[a-zA-Z0-9\"/=\\s]+>)");
        Matcher matcher;
        matcher = pattern.matcher( string );
        while( matcher.find() ){
            //System.out.println( "matcher.group() => " + matcher.group(1) );
            trimmedString += matcher.group(1);
        }
        return trimmedString;
    }

    public static void testTrimTag(){
        String string = "<pattern>這 是 我 jessica</pattern>";
        System.out.println( "before trim => " + string);
        string = trimTag( string );
        System.out.println( "after trim => " + string);
    }

    public static String reduceMandarinSpace(String string){
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]+");
        Matcher matcher = pattern.matcher( string );
        while( matcher.find() ){
            if( matcher.group().trim().length()!=0 ){
                string = string.replace( matcher.group(), matcher.group().replace(" ", "") );
                //System.out.println( string );
            }
        }
        return string;
    }

    public static void testReduceMandarinSpace(){
        String string = "這 是 我 jessica啊一起去Taipei 101 和 taipower building 嗎";
        System.out.println( "before trim => " + string);
        string = reduceMandarinSpace( string );
        System.out.println( "after trim => " + string);
        ArrayList< int[] > textIndices = splitString( string );
        for(int[] idxPair: textIndices){
            if( idxPair[2]==1 ){
                System.out.println( "text=> " + string.substring( idxPair[0], idxPair[1] ) );
            }else if( idxPair[2]==0){
                System.out.println( "non-text=> " + string.substring( idxPair[0], idxPair[1] ) );
            }
        }

    }
}
