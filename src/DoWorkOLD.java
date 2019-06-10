import java.io.*;

import static java.lang.Boolean.TRUE;

public class DoWorkOLD {
    public static void Do() {

        try {
            GD.Shift[1] = "IB";
            GD.Shift[2] = "OB";
            GD.Shift[3] = "FAC";
            //
            // intermediate file
            String DataFileNameSTATS = GD.WorkingDir + "Arrival_Trailers2_v13_TMP.csv";
            File del2 = new File(DataFileNameSTATS);
            del2.delete(); //  delete it immediately:
            String Temp = " SIC , IB10% , IB90%plus1hour , OB10% , OB90%plus1hour , FAC10% , FAC90%plus1hour";
            Output.PrintLineOutFAST(Temp, DataFileNameSTATS);
            // output file
            String DataFileNameSTATS2 = GD.WorkingDir + "Arrival_Trailers2_v13_OUTPUT.csv";            File del22 = new File(DataFileNameSTATS2);
            del22.delete(); //  delete it immediately:
            String Temp2 = " SIC , IB10% , IB90%plus1hour , OB10% , OB90%plus1hour , FAC10% , FAC90%plus1hour";
            Output.PrintLineOutFAST(Temp2, DataFileNameSTATS2);
            //
            // input file 1 read data
            int InputCount = 0;                                                         // for title line
            String LineInput;
            String FileNameIN = GD.WorkingDir + "List_of_SICs.txt";                     // data  file
            BufferedReader br1 = new BufferedReader(new FileReader(FileNameIN));        // read input file from trigger file
            LineInput = br1.readLine();                                                 // title line
            while ((LineInput = br1.readLine()) != null) {                              // read data BY LINE
                InputCount = InputCount + 1;
                InputSub.InputStuff(LineInput, Boolean.FALSE);
                //for (int iv = 1; iv < 13 + 1; iv++) {                                   // NOTE: inputRowVector[] is one indexed !!!
                GD.Data1[1][InputCount] = GD.inputRowVector[1];
                //}
            }
            GD.SICCount = InputCount;
            //
            // input file 2 read data
            //TRP_INSTC_NBR,TRP_ORIG_SIC_CD,TRP_DEST_SIC_CD,DRVR_DOM_SIC,SHIFT,ob flag,ib flag,fac flag,ACTL_ARIV_DTTM_LCL,Date,Time,STANDARD_TIME_SHPMT,Time,
            // 1                 2               3               4           5       6       7       8       9               10      11            12       13
            InputCount = 0;                                                         // for title line
            //LineInput;
            FileNameIN = GD.WorkingDir + "Arrival_Trailers2_v13.csv";                         // data  file
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
            //
            double p10 = 0.d;
            double p90 = 0.d;
            for (int Count2 = 1; Count2 < 100000 + 1; Count2++) {
                GD.Sum1090[Count2] = 0.d;
            }
            for (int iShift = 1; iShift < 3 + 1; iShift++) {
                for (int iSIC = 1; iSIC < GD.SICCount + 1; iSIC++) {
                    int Count = 0;
                    for (int iArrival = 1; iArrival < GD.ArriveCount + 1; iArrival++) {
                        if (!GD.Mark[iArrival]) {
                            if (GD.Data1[1][iSIC].equals(GD.Data2[3][iArrival])) {
                                if (GD.Data2[5][iArrival].equals(GD.Shift[iShift])) {
                                    Count = Count + 1;
                                    GD.Mark[iArrival] = TRUE;
                                    GD.p1090[iSIC][Count] = iArrival;
                                    GD.Sum1090[Count] = GD.Sum1090[Count-1] + Double.parseDouble(GD.Data2[12][iArrival]); // standard time
                                    if (Count == 1) {
                                        GD.SIC[iSIC] = GD.Data2[3][iArrival];
                                    }
                                }
                            }
                        }
                    }
                    if(iShift == 3 && Count < 6){
                        // do nothing
                        GD.p10Found[iShift][iSIC] = -999.d;
                        GD.p90Found[iShift][iSIC] = -999.d;
                    }else {
                        p10 = GD.Sum1090[Count] * 0.10d;

                        int iCount;
                        for (iCount = 1; iCount < Count + 1; iCount++) {
                            if (GD.Sum1090[iCount] > p10) {
                                break;
                            }
                        }
                        if (GD.p1090[iSIC][iCount] > 0) {
                            GD.p10Found[iShift][iSIC] = Double.parseDouble(GD.Data2[11][GD.p1090[iSIC][iCount]]); //start time
                        }
                        p90 = GD.Sum1090[Count] * 0.9d;
                        for (iCount = 1; iCount < Count + 1; iCount++) {
                            if (GD.Sum1090[iCount] > p90) {
                                break;
                            }
                        }
                        if (GD.p1090[iSIC][iCount] > 0) {
                            GD.p90Found[iShift][iSIC] = Double.parseDouble(GD.Data2[11][GD.p1090[iSIC][iCount]]); //End time
                        }
                    }
                    //
                    // output stuff
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
                // FAC
                if (GD.Data4[7][iSIC] >= -9.0d) {  /// if FAC
                    GD.FAC[iSIC] = TRUE;
                    //morning start - morning end
                    if (0. < GD.Data4[6][iSIC] && GD.Data4[6][iSIC] < 17. / 24.) { //start
                        if (0. < GD.Data4[7][iSIC] && GD.Data4[7][iSIC] < 17. / 24.) { // end
                            if (GD.Data4[6][iSIC] > GD.Data4[7][iSIC]) {
                                double temp1 = GD.Data4[6][iSIC];
                                GD.Data4[6][iSIC] = GD.Data4[7][iSIC];
                                GD.Data4[7][iSIC] = temp1;
                            }
                            GD.Data4[7][iSIC] = GD.Data4[7][iSIC] + 1. / 24.;
                            if (GD.Data4[7][iSIC] > 4. / 24.) {     // must end by 4:00
                                GD.Data4[7][iSIC] = 4. / 24.;
                                if (GD.Data4[6][iSIC] > 4. / 24.) {
                                    GD.Data4[6][iSIC] = 4. / 24.;
                                }
                            }
                        }
                    }
                    //morning start - night end
                    //if(0. < GD.Data4[6][iSIC] &&  GD.Data4[6][iSIC] < 17./24.) { //start
                    //    if (17. / 24. < GD.Data4[7][iSIC] && GD.Data4[7][iSIC] < 24. / 24.) { // end
                    //        // nothing
                    //    }
                    //}
                    //night start - morning end
                    if (17. / 24. < GD.Data4[6][iSIC] && GD.Data4[6][iSIC] < 24. / 24.) { //start
                        if (0. < GD.Data4[7][iSIC] && GD.Data4[7][iSIC] < 17. / 24.) { // end
                            GD.Data4[7][iSIC] = GD.Data4[7][iSIC] + 1. / 24.;
                            if (GD.Data4[7][iSIC] > 4. / 24.) {
                                GD.Data4[7][iSIC] = 4. / 24.;
                            }
                        }
                    }
                    //night start - Night end
                    if (17. / 24. < GD.Data4[6][iSIC] && GD.Data4[6][iSIC] < 24. / 24.) { //start
                        if (17. / 24. < GD.Data4[7][iSIC] && GD.Data4[7][iSIC] < 24. / 24.) { // end
                            if (GD.Data4[6][iSIC] > GD.Data4[7][iSIC]) {
                                double temp1 = GD.Data4[6][iSIC];
                                GD.Data4[6][iSIC] = GD.Data4[7][iSIC];
                                GD.Data4[7][iSIC] = temp1;
                            }
                            GD.Data4[7][iSIC] = GD.Data4[7][iSIC] + 1. / 24.;
                        }
                    }
                }
                // IB & OB
                // shift ends 1 hour after the 90% time
                GD.Data4[3][iSIC] = GD.Data4[3][iSIC] + 1.d / 24.d;
                GD.Data4[5][iSIC] = GD.Data4[5][iSIC] + 1.d / 24.d;
                //
                // overlap START OVERRIDES END
                // IBE > OBS then =
                if (GD.Data4[3][iSIC] >= GD.Data4[4][iSIC]) {
                    GD.Data4[4][iSIC] = GD.Data4[3][iSIC];
                }
                //FAC overlap
                if(GD.FAC[iSIC]) {
                    // OBE > FACS then =
                    //FACS morning
                    //if (0. < GD.Data4[6][iSIC] && GD.Data4[6][iSIC] < 17. / 24.) {
                    if (GD.Data4[6][iSIC] >= GD.Data4[5][iSIC]) {
                        GD.Data4[5][iSIC] = GD.Data4[6][iSIC];
                    }
                    //}
                    // FACS Night
                    //if (17. / 24. < GD.Data4[6][iSIC] && GD.Data4[6][iSIC] < 24. / 24.) { //
                    //    if (GD.Data4[6][iSIC] >= GD.Data4[5][iSIC]) {
                    //        GD.Data4[5][iSIC] = GD.Data4[6][iSIC];
                    //    }
                    //}

                    ////////////////////////////////////
                    // FACE > IBS then FACE = IBS <= 4.0
                    // morning end
                    // IF FACE > IBS then FACE = IBS <= 4.0
                    //if (0. < GD.Data4[7][iSIC] && GD.Data4[7][iSIC] < 17. / 24.) { //start
                    //if (0. < GD.Data4[7][iSIC] && GD.Data4[7][iSIC] < 17. / 24.) { // end
                    if (GD.Data4[7][iSIC] >= GD.Data4[2][iSIC]) {
                        GD.Data4[2][iSIC] = GD.Data4[7][iSIC];
                    }
                    //}
                    //}
                    //night end
                    //FACE > IBS then FACE = IBS
                    //if (17. / 24. < GD.Data4[6][iSIC] && GD.Data4[6][iSIC] < 24. / 24.) {

                    //}

                    //}else{ // not FACE
                    // should " of OBE > IBS SET OBE = IBS ??
                    // ??
                }
            /*
//-------------------------------------------------------


                ///if IB end > 12: then IB end = 12:

///////////////////////////////////
                // only if OB starts before 12


                if (GD.Data4[3][iSIC] >= GD.Data4[4][iSIC]) {
                    GD.Data4[4][iSIC] = GD.Data4[3][iSIC];
                }else{
                    if (GD.Data4[4][iSIC] < 12. / 24. && GD.Data4[3][iSIC] > 12. / 24.) {  // overlap
                        GD.Data4[3][iSIC] = 12. / 24.;
                        GD.Data4[4][iSIC] = 12. / 24.;
                    }

                }
/////////////////////////////////////////////////////////////////
                // OB starts before IB ends - set to IB end
                if (GD.Data4[3][iSIC] >= GD.Data4[4][iSIC]) {
                    GD.Data4[4][iSIC] = GD.Data4[3][iSIC];
                }
                // if OB ends after FAC start ; FAC start = OB end
                if (GD.Data4[7][iSIC] >= -9.d) {
                    if (GD.Data4[6][iSIC] > 17. / 24.) {
                        if (GD.Data4[5][iSIC] >= GD.Data4[6][iSIC]) {
                            GD.Data4[5][iSIC] = GD.Data4[6][iSIC];
                        }
                    }
                }
                */
                // end of shift must be >= start //
                if (GD.Data4[2][iSIC] >= GD.Data4[3][iSIC]) {  //IB
                    GD.Data4[2][iSIC] = GD.Data4[3][iSIC];
                }
                if (GD.Data4[4][iSIC] >= GD.Data4[5][iSIC]) { //OB
                    GD.Data4[4][iSIC] = GD.Data4[5][iSIC];
                }
                // print output stuff
                String Thing1 = Double.toString(GD.Data4[6][iSIC]);  // do not print FAC if  no FAC
                String Thing2 = Double.toString(GD.Data4[7][iSIC]);
                if (GD.Data4[6][iSIC] <= -9.d || GD.Data4[7][iSIC] <= -9.d) {
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
