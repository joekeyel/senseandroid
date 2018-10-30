package my.com.tm.sense;

/**
 * Created by user on 29/11/2017.
 */

public class Config {

    //PSTN SITES
    public static final String URL_PSTN="http://58.27.84.166/mcconline/MCC%20Online%20V3/decom_pstn.php";

    public static final String URL_EMPLOYEE="http://58.27.84.166/mcconline/MCC%20Online%20V3/sense/employeelist.php";
    public static final String URL_EMPLOYEEUPDATE="http://58.27.84.166/mcconline/MCC%20Online%20V3/sense/updateuserid.php";

    public static final String URL_EMPLOYEEINSERT="http://58.27.84.166/mcconline/MCC%20Online%20V3/sense/insertuser.php";

    public static final String URL_EMPLOYEEINSERT_RATING = "http://58.27.84.166/mcconline/MCC%20Online%20V3/sense/insertuserrating.php";
    //IPMSAN SITES
    public static final String URL_IPMSAN="http://58.27.84.166/mcconline/MCC%20Online%20V3/decom/totalcab.php";
    public static final String URL_IPMSANBlocklist="http://58.27.84.166/mcconline/MCC%20Online%20V3/decom/totalcablist_blocked.php";
    public static final String URL_IPMSANBlocklist_perstate = "http://58.27.84.166/mcconline/MCC%20Online%20V3/decom/totalcablist_blocked_perstate.php";


    public static final String URL_GET_RATING = "http://58.27.84.166/mcconline/MCC%20Online%20V3/sense/getuserrating.php";
}