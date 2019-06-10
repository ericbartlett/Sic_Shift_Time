

import java.io.*;

import static java.lang.Boolean.TRUE;

public class DoWork {

    public static void Do() {

        try {
            GD.Shift[1] = "IB";
            GD.Shift[2] = "OB";
            GD.Shift[3] = "FAC";
            //
            // output file stuff
            String DataFileNameSTATS2 = GD.WorkingDir + "Arrival_Trailers2_v13_OUTPUT.csv";            File del22 = new File(DataFileNameSTATS2);
            del22.delete(); //  delete it immediately:
            String Temp2 = " SIC , IB10% , IB90%plus1hour , OB10% , OB90%plus1hour , FAC10% , FAC90%plus1hour";
            Output.PrintLineOutFAST(Temp2, DataFileNameSTATS2);
            // intermediate (output - input) file stuff
            String DataFileNameSTATS = GD.WorkingDir + "Arrival_Trailers2_v13_TMP.csv";
            File del2 = new File(DataFileNameSTATS);
            del2.delete(); //  delete it immediately:
            String Temp = " SIC , IB10% , IB90%plus1hour , OB10% , OB90%plus1hour , FAC10% , FAC90%plus1hour";
            Output.PrintLineOutFAST(Temp, DataFileNameSTATS);
            //
            // input file No1; read data1 (for each SIC -> find FACs)
            int InputCount = 0;                                                         // for title line
            String LineInput;
            String FileNameIN = GD.WorkingDir + "List_of_SICs.csv";                     // data  file
            BufferedReader br1 = new BufferedReader(new FileReader(FileNameIN));        // read input file from trigger file
            LineInput = br1.readLine();                                                 // title line
            while ((LineInput = br1.readLine()) != null) {                              // read data BY LINE
                InputCount = InputCount + 1;
                InputSub.InputStuff(LineInput, TRUE);
                for (int iv = 1; iv < 2 + 1; iv++) {                                    // NOTE: inputRowVector[] is one indexed !!!
                    GD.Data1[1][InputCount] = GD.inputRowVector[1];                     /// SIC
                    GD.Data1[2][InputCount] = GD.inputRowVector[2];                     /// FAC
                    GD.FAC[InputCount] = false;
                    if(GD.Data1[2][InputCount].equals("TRUE")){                         // if not N/A or False
                        GD.FAC[InputCount] = true;
                    }
                }
            }
            GD.SICCount = InputCount;                                                   // total number of SICs
            //
            // input file No2 read data2 -> for each arrival
            //  INPUT LINE
            //  TRP_INSTC_NBR,    TRP_ORIG_SIC_CD,    TRP_DEST_SIC_CD,    DRVR_DOM_SIC,   SHIFT,  ob_flag,    ib_flag,    fac_flag,   ACTL_ARIV_DTTM_LCL, Date,       Time,      STANDARD_TIME_SHPMT,    Time,
            //  1                 2                  3                   4               5       6           7           8           9                   10          11           12                     13
            InputCount = 0;                                                             // for title line
            //LineInput;
            FileNameIN = GD.WorkingDir + "Arrival_Trailers2_v13.csv";                   // data  file
            BufferedReader br2 = new BufferedReader(new FileReader(FileNameIN));        // read input file from trigger file
            LineInput = br2.readLine();                                                 // title line
            while ((LineInput = br2.readLine()) != null) {                              // read data BY LINE
                InputCount = InputCount + 1;
                InputSub.InputStuff(LineInput, TRUE);
                for (int iv = 1; iv < 13 + 1; iv++) {                                   // NOTE: inputRowVector[] is one indexed !!
                    GD.Data2[iv][InputCount] = GD.inputRowVector[iv];
                }
            }
            GD.ArriveCount = InputCount;
            //  AS PER GRAHAM'S instructions (6/10/2019)
            // concatenate another column onto Data2 called tStar
            //  if time >0.5 (noon) then tStar = time - 0.5
            //  if time <0.5 (noon) then tStar = time + 0.5
            for (int iv = 1; iv < GD.ArriveCount + 1; iv++) {
                if(Double.parseDouble(GD.Data2[11][iv]) > 0.5) {
                    GD.Data2[14][iv] = Double.toString(Double.parseDouble(GD.Data2[11][iv]) - 0.5);
                }
                if(Double.parseDouble(GD.Data2[11][iv]) <= 0.5) {
                    GD.Data2[14][iv] = Double.toString(Double.parseDouble(GD.Data2[11][iv]) + 0.5);
                }
            }
            // Sort Data2 by tStar (smallest to largest (input file no longer needs to be sorted)
            // run the algorithm below (unchanged) AS PER GRAHAM'S instructions (6/10/2019)
            Sort_input_data.Sort_input_data_now();
            //
            double p10 = 0.d;
            double p90 = 0.d;
            //for (int Count2 = 1; Count2 < 1000000 + 1; Count2++) {
            //    GD.Sum1090[Count2] = 0.d;
            //}
            for (int iShift = 1; iShift < 3 + 1; iShift++) {                            // 3 shifts
                for (int iSIC = 1; iSIC < GD.SICCount + 1; iSIC++) {                    // all SICs
                    int Count = 0;                                                      // Arrival count for each SIC_Shift
                    for (int iArrival = 1; iArrival < GD.ArriveCount + 1; iArrival++) { // all arrivals
                        if (!GD.Mark[iArrival]) {                                       // if not previously used
                            if (GD.Data1[1][iSIC].equals(GD.DataCSV[3][iArrival])) {      // GD.Data1[1][iSIC] = SIC == GD.DataCSV[3][iArrival] = TRP_DEST_SIC_CD
                                if (GD.DataCSV[5][iArrival].equals(GD.Shift[iShift])) {   // GD.DataCSV[5][iArrival] = arrival shift == shift
                                    Count = Count + 1;                                  // arrival count for this SIC_Shift
                                    GD.Mark[iArrival] = TRUE;                           // DataCSV used
                                    GD.p1090[iSIC][Count] = iArrival;                   // indexes the arrivals by SIC and Count (Count = the number of arrivals at that SIC , iArrival = arrival number for ALL SICS)
                                    GD.Sum1090[Count] = GD.Sum1090[Count-1] + Double.parseDouble(GD.DataCSV[12][iArrival]); // accumulate standard time up until Count (GD.Sum1090[0] = 0)
                                    if (Count == 1) {
                                        GD.SIC[iSIC] = GD.DataCSV[3][iArrival];           // SIC name for this iSIC // GD.DataCSV[3][iArrival] = TRP_DEST_SIC_CD
                                    }
                                }
                            }
                        }
                    }
                    if(Count < 6){ // must be at least 7 arrivals at this SIC
                        // do nothing
                        GD.p10Found[iShift][iSIC] = -999.d;
                        GD.p90Found[iShift][iSIC] = -999.d;
                    }else {
                        p10 = 0.10d * GD.Sum1090[Count];                                // 10% of total sum = 0.10 * cumulative sum until last arrival
                        int iCount;
                        for (iCount = 1; iCount < Count + 1; iCount++) {
                            if (GD.Sum1090[iCount] > p10) {
                                break;
                            }
                        }
                        // now you have the iCount that give the arrival number for the 10% (of total) of standard time for this SIC_Shift
                        if (GD.p1090[iSIC][iCount] > 0) {                               // iCount is the counter number for the arrival of the 10% trailer (must be >0)
                            int iArrivalsForThisSIC = GD.p1090[iSIC][iCount];           // GD.p1090[iSIC][iCount] = the DataCSV row number in the file of all SICs
                            GD.p10Found[iShift][iSIC] = Double.parseDouble(GD.DataCSV[11][iArrivalsForThisSIC]); // start time = 10% time
                        }
                        p90 = GD.Sum1090[Count] * 0.9d;                                 // 90% of total sum = 0.90 * cumulative sum until last arrival
                        for (iCount = 1; iCount < Count + 1; iCount++) {
                            if (GD.Sum1090[iCount] > p90) {
                                break;
                            }
                        }
                        // now you have the iCount that give the arrival number for the 90% (of total) of standard time for this SIC_Shift
                        if (GD.p1090[iSIC][iCount] > 0) {
                            int iArrivalsForThisSIC = GD.p1090[iSIC][iCount];
                            GD.p90Found[iShift][iSIC] = Double.parseDouble(GD.DataCSV[11][iArrivalsForThisSIC]); // End time = 0.90 * cumulative sum until last arrival
                        }
                    }
                    //
                    // Write TMP file // output stuff
                    if(iShift == 3) { // FAC starts late & ends early
                        String sTemp = GD.SIC[iSIC] + ", " + GD.p10Found[1][iSIC] + ", " + GD.p90Found[1][iSIC] + ", " + GD.p10Found[2][iSIC] + ", " + GD.p90Found[2][iSIC] + ", " + GD.p90Found[3][iSIC] + ", " + GD.p10Found[3][iSIC];
                        Output.PrintLineOutFAST(sTemp, DataFileNameSTATS);
                    }
                }
            }
            // read tmp file
            // input file 2 read data
            InputCount = 0;                                                         // for title line
            //LineInput;
            FileNameIN = GD.WorkingDir + "Arrival_Trailers2_v13_TMP.csv";                         // data  file
            BufferedReader br3 = new BufferedReader(new FileReader(FileNameIN));        // read input file from trigger file
            LineInput = br3.readLine();                                                 // title line
            while ((LineInput = br3.readLine()) != null) {                              // read data BY LINE
                InputCount = InputCount + 1;
                InputSub.InputStuff(LineInput, TRUE);
                for (int iv = 2; iv < 7 + 1; iv++) {                                   // NOTE: inputRowVector[] is one indexed !!
                    GD.Data4[iv][InputCount] = Double.parseDouble(GD.inputRowVector[iv]);
                }
            }
            // for all SICs
            for (int iSIC = 1; iSIC < InputCount + 1; iSIC++) {
                //90% times plus 1.5 hour
                GD.Data4[3][iSIC] = GD.Data4[3][iSIC] + 1.5 / 24.; // IBE
                GD.Data4[5][iSIC] = GD.Data4[5][iSIC] + 1.5 / 24.; // OBE
                GD.Data4[7][iSIC] = GD.Data4[7][iSIC] + 1.5 / 24.; // FACE
                //10% times - 1.5 hour
                GD.Data4[2][iSIC] = GD.Data4[2][iSIC] - 1.5 / 24.; // IBS
                GD.Data4[4][iSIC] = GD.Data4[4][iSIC] - 1.5 / 24.; // OBS
                GD.Data4[6][iSIC] = GD.Data4[6][iSIC] - 1.5 / 24.; // FACS
                // if < 0 add 24
                if(GD.Data4[2][iSIC] < 0.) {
                    GD.Data4[2][iSIC] = GD.Data4[2][iSIC] + 24. / 24.;
                }
                if(GD.Data4[4][iSIC] < 0.) {
                    GD.Data4[4][iSIC] = GD.Data4[4][iSIC] + 24. / 24.;
                }
                if(GD.Data4[6][iSIC] < 0.) {
                    GD.Data4[6][iSIC] = GD.Data4[6][iSIC] + 24. / 24.;
                }

                // FAC
                if (GD.FAC[iSIC]) {
                    //if (GD.Data4[7][iSIC] >= -9.0d) {  /// if FAC
                    //GD.FAC[iSIC] = TRUE;
                    if (GD.Data4[6][iSIC] < 12. / 24.) {/// if FAC IN NEXT DAY
                        GD.Data4[6][iSIC] = GD.Data4[6][iSIC] + 24. / 24.;
                    }
                    if (GD.Data4[7][iSIC] < 12. / 24.) {/// if FAC IN NEXT DAY
                        GD.Data4[7][iSIC] = GD.Data4[7][iSIC] + 24. / 24.;
                    }
                    if (GD.Data4[7][iSIC] > (24. + 4.) / 24.) {  // FAC ENDS AT 4:AM
                        GD.Data4[7][iSIC] = (24. + 4.) / 24.;
                    }
                    if (0 < GD.Data4[6][iSIC] && GD.Data4[6][iSIC] < (12. + 8.) / 24.) {  // FAC STARTS by 8:PM
                        GD.Data4[6][iSIC] = (12. + 8.) / 24.;
                    }
                }
                if (GD.Data4[4][iSIC] < 10. / 24.) {  //OBS
                    GD.Data4[4][iSIC] = 10. / 24.;
                }
                if (GD.Data4[3][iSIC] > GD.Data4[4][iSIC]) { // IF IBE > OBS
                    GD.Data4[3][iSIC] = GD.Data4[4][iSIC];
                }
                if (GD.FAC[iSIC]) {
                    if (GD.Data4[5][iSIC] > GD.Data4[6][iSIC]) { // IF OBE > FACS
                        GD.Data4[5][iSIC] = GD.Data4[6][iSIC];
                    }
                    if (GD.Data4[7][iSIC] > GD.Data4[2][iSIC]) { // IF FACE > OBS
                        GD.Data4[7][iSIC] = GD.Data4[2][iSIC];
                    }
                    if (4. / 24. < GD.Data4[7][iSIC] && GD.Data4[7][iSIC] < 12. / 24.) {
                        GD.Data4[7][iSIC] = 4. / 24.;
                    }
                }
                if(GD.Data4[5][iSIC] < GD.Data4[4][iSIC]){
                    GD.Data4[5][iSIC] = GD.Data4[4][iSIC] + 5./24.;
                }
                //
                // print output stuff
                String Thing1 = Double.toString(GD.Data4[6][iSIC]);  // do not print FAC if  no FAC
                String Thing2 = Double.toString(GD.Data4[7][iSIC]);
                if (!GD.FAC[iSIC]) {
                    //if (GD.Data4[6][iSIC] <= -9.d || GD.Data4[7][iSIC] <= -9.d) {
                    Thing1 = "";
                    Thing2 = "";
                }
                String sTemp = GD.SIC[iSIC] + ", " + GD.Data4[2][iSIC] + ", " + GD.Data4[3][iSIC] + ", " + GD.Data4[4][iSIC]
                        + ", " + GD.Data4[5][iSIC] + ", " + Thing1 + ", " + Thing2;
                Output.PrintLineOutFAST(sTemp, DataFileNameSTATS2);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
