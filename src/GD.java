public class GD {
    //General data

    public static String WorkingDir                 = "E:\\Conway\\SIC_Shift_times\\";
    public static boolean[] Mark                    = new boolean[500000];
    public static boolean[] FAC                     = new boolean[500+1];
    public static String[][] Data1                  = new String[2+1][500+1];
    public static String[][] Data2                  = new String[13+1+1][500000+1];
    public static String[][] DataCSV                = new String[13+1+1+1][500000+1];
    public static double[][] Data4                  = new double[7+1][500+1];
    public static int[][] p1090                     = new int[300+1][100000+1];
    public static double[] Sum1090                  = new double[1000000+1];
    public static String[] Shift                    = new String[3+1];
    public static Integer ArriveCount               = 0;
    public static Integer SICCount                  = 0;
    public static double[][] p10Found               = new double[3+1][100000+1];
    public static double[][] p90Found               = new double[3+1][100000+1];
    public static String[] SIC                      = new String[300+1];

/////////////////////////////////////////////////////////////////////////////////////////////////

    public static Integer NumParamColumnsOfTOTAL    = 100;
    public static String[] inputRowVector           = new String [NumParamColumnsOfTOTAL+1];
    public static String[] stuff                    = new String [NumParamColumnsOfTOTAL+1];
}
