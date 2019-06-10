


public class SpmtData2 implements Comparable{

    String  TRP_INSTC_NBR,    TRP_ORIG_SIC_CD,    TRP_DEST_SIC_CD,    DRVR_DOM_SIC,   SHIFT,  ob_flag,    ib_flag,    fac_flag,   ACTL_ARIV_DTTM_LCL, Date,       Time1,      STANDARD_TIME_SHPMT,    Time2, TimeStar, another;

    public int compareTo(Object o) {
        int result = 0;
        SpmtData2 to = (SpmtData2) o;
        if (another.compareTo(to.another)>0) {
            result = 1;
        } else {
            result = -1;
        }
        return result;
    }

}
