import java.io.*;

public class Output {
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //  General printing  //
    //
    ////////////////////////////////////////////////////////////////////////////////////////
    //
    public static void PrintLineOutFAST(String outLine, String fileNameOut) {
        try {
            //
            FileWriter fStream = new FileWriter(fileNameOut, true);    // Open output file append
            PrintWriter myPrintWriter = new PrintWriter(fStream);
            //
            myPrintWriter.println(outLine);                                     // print line
            //
            myPrintWriter.close();                                              // close
            fStream.close();
            //
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
    //
    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    public static void PrintLineOutFASTTitleLine(String fileNameOut, String TitleLineData) {
        try {
            //
            File del = new File(fileNameOut);
            del.delete(); //  delete it immediately:                            // !!!!!!!!!!!!!!!!!!
            //
            FileWriter fStream = new FileWriter(fileNameOut, true);    // Open output file append
            PrintWriter myPrintWriter = new PrintWriter(fStream);
            //
            myPrintWriter.println(TitleLineData);                               // print line
            //
            myPrintWriter.close();                                              // close
            fStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
    //
    //////////////////////////////////////////////
    //
    public static void PrintLineOutFASTTitleLineFalse(String fileNameOut, String TitleLineData) {
        try {
            //
            File del = new File(fileNameOut);
            del.delete(); //  delete it immediately:                            // !!!!!!!!!!!!!!!!!!
            //
            FileWriter fStream = new FileWriter(fileNameOut, false);    // Open output file overwrite
            PrintWriter myPrintWriter = new PrintWriter(fStream);
            //
            myPrintWriter.println(TitleLineData);                               // print line
            //
            myPrintWriter.close();                                              // close
            fStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
