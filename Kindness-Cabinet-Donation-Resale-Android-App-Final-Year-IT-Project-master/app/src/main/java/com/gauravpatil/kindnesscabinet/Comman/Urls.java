package com.gauravpatil.kindnesscabinet.Comman;
public class Urls
{
    public static String webService = "http://192.168.137.132:80/KindnessCabinetAPI/";
    public static String webServiceAdmin = "http://192.168.137.132:80/KindnessCabinetAPI/Admin/";
    public static String userLogin = webService + "userlogin.php";
    public static String adminLogin = webServiceAdmin + "adminlogin.php";
    public static String adminAllUser = webServiceAdmin + "getAllUser.php";
    public static String getAllHistory = webServiceAdmin + "getAllHistory.php";
    public static String url_delete_user = webServiceAdmin + "deleteuser.php";
    public static String userRegister = webService + "userregisterdetailstbl.php";
    public static String forgetpassword = webService + "userforgetpassword.php";
    public static String myDetails = webService + "mydetails.php";
    public static String getDonerSalerCategoryDetails = webService + "doner_saler_tbl.php";

    public static String image = webService + "images/";
    public static String getAllCategoryDetails = webService + "getAllCategoryDetails.php";
    public static String addFeedback = webService + "feedback.php";
    public static String urladddonateimage=webService+ "adddonateimage.php";
    public static String getAllDonateandSellerInformation=webService+ "getAllDonateandSellIerinformation.php";
    public static String getCategorywiseDonateandSellerInformation=webService+ "getCategorywiseDonateandSellIerinformation.php";
    public static String addContactUsMessage=webService+ "addcontactus.php";
    public static String getHistory= webService+"getHistory.php";
    public static String getFavourite= webService+"getFavorite.php";
    public static String url_delete_favourite= webService+"deleteFavourite.php";
    public static String addRequestThroughPostOffice= webService+"addRequestThroughPostOffice.php";
    public static String url_add_payment= webService+"addpayment.php";
    public static String urladdFavouriteProduct= webService+"addFavouriteProduct.php";
    public static String getCommanFavoriteProduct= webService+"getCommanFavoriteProduct.php";
    public static String getRequestProduct= webService+ "getRequestProduct.php";
}