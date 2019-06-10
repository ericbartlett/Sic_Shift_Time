import java.util.SortedSet;
import java.util.TreeSet;

public class    Sort_input_data {

    public static void      Sort_input_data_now(){

        try {
            int gdi;
            SortedSet<SpmtData2> dataSet2 = new TreeSet<SpmtData2>();
            for (gdi = 1; gdi < GD.ArriveCount + 1; gdi++) {
                SpmtData2 sd2 = new SpmtData2();
                //  1                 2                   3                   4               5       6           7           8           9                   10          11           12                     13
                //  TRP_INSTC_NBR,    TRP_ORIG_SIC_CD,    TRP_DEST_SIC_CD,    DRVR_DOM_SIC,   SHIFT,  ob_flag,    ib_flag,    fac_flag,   ACTL_ARIV_DTTM_LCL, Date,       Time1,      STANDARD_TIME_SHPMT,   Time2,  TimeStar, another;
                sd2.TRP_INSTC_NBR = GD.Data2[1][gdi];
                sd2.TRP_ORIG_SIC_CD = GD.Data2[2][gdi];
                sd2.TRP_DEST_SIC_CD = GD.Data2[3][gdi];
                sd2.DRVR_DOM_SIC = GD.Data2[4][gdi];
                sd2.SHIFT = GD.Data2[5][gdi];
                sd2.ob_flag = GD.Data2[6][gdi];
                sd2.ib_flag = GD.Data2[7][gdi];
                sd2.fac_flag = GD.Data2[8][gdi];
                sd2.ACTL_ARIV_DTTM_LCL = GD.Data2[9][gdi];
                sd2.Date = GD.Data2[10][gdi];
                sd2.Time1 = GD.Data2[11][gdi];
                sd2.STANDARD_TIME_SHPMT = GD.Data2[12][gdi];
                sd2.Time2 = GD.Data2[13][gdi];
                sd2.TimeStar = GD.Data2[14][gdi];
                sd2.another = GD.Data2[14][gdi];  // sort by TimeStar
                dataSet2.add(sd2);
            }
            gdi = 1;   // SHIPMENT
            for (SpmtData2 sd2 : dataSet2) {
                GD.DataCSV[1][gdi] = sd2.TRP_INSTC_NBR;
                GD.DataCSV[2][gdi] = sd2.TRP_ORIG_SIC_CD;
                GD.DataCSV[3][gdi] = sd2.TRP_DEST_SIC_CD;
                GD.DataCSV[4][gdi] = sd2.DRVR_DOM_SIC;
                GD.DataCSV[5][gdi] = sd2.SHIFT;
                GD.DataCSV[6][gdi] = sd2.ob_flag;
                GD.DataCSV[7][gdi] = sd2.ib_flag;
                GD.DataCSV[8][gdi] = sd2.fac_flag;
                GD.DataCSV[9][gdi] = sd2.ACTL_ARIV_DTTM_LCL;
                GD.DataCSV[10][gdi] = sd2.Date;
                GD.DataCSV[11][gdi] = sd2.Time1;
                GD.DataCSV[12][gdi] = sd2.STANDARD_TIME_SHPMT;
                GD.DataCSV[13][gdi] = sd2.Time2;
                GD.DataCSV[14][gdi] = sd2.TimeStar;
                GD.DataCSV[15][gdi] = sd2.another;
                gdi++;
            }
            dataSet2.clear();

        }catch (Exception e) { e.printStackTrace(); }
    }

//-------------------------------------


}
