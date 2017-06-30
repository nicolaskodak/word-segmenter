package one.kota.utility;
/**
 * Created by ta_ko on 2017/6/23.
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import one.kota.utility.Text;

public class Main {
    public static void main(String[] args){
        //Segmenter segmenter = new Segmenter();
        //HashMap<Integer, HashSet<String>> dictionary = segmenter.dictionary;
        HashMap<Integer, HashSet<String>> dictionary = Segmenter.dictionary;

        //String[] strArr = { "hi你是?", "hello啊!", "人  呢~   ", "超~nice to 認識you啊!"};
        // String string = "超~nice to 認識you啊!";
        String string = "明天天氣會好嗎";
        //String[] segStrArr = new String[ strArr.length ];
        System.out.println( "After forward cut => " + Segmenter.forwardCut( string ) );
        System.out.println( "After backward cut => " + Segmenter.backwardCut( string ) );
    }
}

