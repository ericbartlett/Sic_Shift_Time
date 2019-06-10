public class Parsable {

    // these tests will reject anything with leading or trailing blanks
    // NOTE: in Java the strings "NaN" and "infinite" are FLOAT NUMBERS !!!  -- NOT !!!
/////////////////////////////////////////////////////

    public static boolean isParsableToInt(String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

////////////////////////////////////////////////////////

    public static boolean isParsableToLong(String i) {
        try {
            Long.parseLong(i);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

/////////////////////////////////////////////////////

    public static boolean isParsableToFloat(String i) {
        try {
            float f = Float.parseFloat(i);
            // in Java, the strings "NaN" and "infinite" are legitimate FLOAT NUMBERS !!!
            // so they will pass the  Float.parseFloat(i) test as numbers !!!
            // but they are NOT!!!
            if(Float.isNaN(f) || Float.isInfinite(f)) {return false;}

            return true;

        } catch (NumberFormatException nfe) {
            // IF ANY OTHER STRING BUT THE STRINGS THAT ARE NUMBERS !!!
            return false;
        }
    }

/////////////////////////////////////////////////////////

    public static boolean isParsableToDouble(String i) {
        try {
            Double f = Double.parseDouble(i);
            // in Java, the strings "NaN" and "infinite" are legitimate DOUBLE NUMBERS !!!
            // so they will pass the  Double.parseDouble(i) test as numbers !!!
            // but they are NOT!!!
            if(Double.isNaN(f) || Double.isInfinite(f)) {return false;}

            return true;

        } catch (NumberFormatException nfe) {
            // IF ANY OTHER STRING BUT THE STRINGS THAT ARE NUMBERS !!!
            return false;
        }
    }

    public static String DoubleToString(Double x) {

        String TheString = "";

        TheString = Double.toString(x);

        return TheString;
    }



}
