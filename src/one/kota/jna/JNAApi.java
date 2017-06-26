package one.kota.jna; /**
 * Created by Kodak on 6/24/17.
 */
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class JNAApi{
    public static void main(String[] args){
        CLibrary.INSTANCE.printf( "using c printf to print shit");
        System.out.println( "CLibrary.class=>" + CLibrary.class);

        // Method 1
        CTest ctest = (CTest) Native.loadLibrary("/Users/Kodak/Developer/C/JNA/libctest.so", CTest.class);

        // Method 2
        // System.setProperty("jna.library.path", "/Users/Kodak/Developer/java/TextProcessing/src/");
        // CTest ctest = (CTest) Native.loadLibrary("libctest", CTest.class);

        // Method 3
        // System.setProperty("java.library.path", "/Users/Kodak/Developer/java/TextProcessing/src");
        // System.loadLibrary("libctest");
        // CTest ctest = (CTest) Native.loadLibrary("libctest.so", CTest.class);

        // Method 4
        // CTest ctest = (CTest) Native.loadLibrary("ctest", CTest.class);

        ctest.helloFromC();
        System.out.println( "CTest.class=>" + CTest.class);
    }

    public interface CLibrary extends Library {

        CLibrary INSTANCE = (CLibrary) Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);
        void printf(String format, Object... args);
    }

    public interface CTest extends Library{
        public void helloFromC();
    }
}
