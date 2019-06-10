public class InputSub {
    public static boolean InputStuff(String  LineInput,Boolean CommaFlag) {
        int quoteCount = 0;
        String aQuote = "\"";
        boolean flag = true;
        int sLength = LineInput.length();
        for (int iv = 0; iv < sLength; iv++) {
            String aTemp = LineInput.substring(iv, iv + 1);
            if (aTemp.equals(aQuote)) {
                quoteCount = quoteCount + 1;                                    // number of quotes in row LineInput
            }
        }
        if (quoteCount > 0) {                                                   // if there are quotes
            int iTemp1 = ((int) quoteCount) / ((int) 2);
            double dTemp2 = ((double) quoteCount) / 2.d;
            double dTemp3 = (double) iTemp1;
            boolean bTemp = dTemp3 != dTemp2;
            if (bTemp) {                                                        // no odd number of quotes allowed
                flag = false;                                                   // if odd number then flag = false = a bad data row in LineInput
            }
            if (flag) {                                                         // if data row with even number of quotes only
                // Now fix the '"12","stuff1,stuff2", 34' => '12, stuff1stuff2, 34'  in GD.inputRowVector[iv + 1]
                int nCol = 1, nQuote = 0;
                for (int iv = 0; iv < GD.NumParamColumnsOfTOTAL + 1; iv++) {    // zero out the fields
                    GD.inputRowVector[iv] = "";
                }
                for (int iv = 0; iv < sLength; iv++) {
                    String aTemp = LineInput.substring(iv, iv + 1);
                    if (aTemp.equals(aQuote)) {
                        nQuote = nQuote + 1;
                    }
                    if (!aTemp.equals(aQuote) && !aTemp.equals(",")) {
                        if (GD.inputRowVector[nCol] != null) {
                            GD.inputRowVector[nCol] = GD.inputRowVector[nCol] + aTemp;
                        } else {
                            GD.inputRowVector[nCol] = aTemp;
                        }
                    }
                    if (aTemp.equals(",") && (nQuote == 2 || nQuote == 0)) {
                        nCol = nCol + 1;
                        nQuote = 0;
                    }
                }
            }
        } else {
            // if there are no quotes here then do an easy split
            if (CommaFlag) {
                GD.stuff = LineInput.split("\\,");                              // delimiter = ","
            } else {
                GD.stuff = LineInput.split("\\s+");                             // delimiter = "  "
            }
            int thisLength = GD.stuff.length;
            for (int iv = 0; iv < thisLength + 1; iv++) {                       // NOTE: Stuff[] is zero indexed !!!
                try {
                    GD.inputRowVector[iv + 1] = GD.stuff[iv].trim();            // NOTE: Stuff[] is zero indexed !!!
                } catch (Exception e) {                                         // NOTE: inputRowVector[] is ONE indexed !!!
                    GD.inputRowVector[iv + 1] = "Null";                         //ParamData[iv + 1] = "";
                }
            }
        }
        return flag;
    }

}
