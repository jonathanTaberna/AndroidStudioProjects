package hsneoclinica.neoclinica.constantes;

public class constantes {

    //preferencias
    public static String neoclinicaConfig = "neoclinica.config";

    //constantes
    public static String CONFIG_NOT_FOUND = "CONFIG_NOT_FOUND";
    public static String CONFIG_FOUND = "CONFIG_FOUND";
    public static int RESULT_NUEVO_USUARIO = 111;
    public static int RESULT_MAIN_ACTIVITY = 222;
    public static int CANTIDAD_TURNOS_LISTA = 10;
    public static int TAMANYO_ELEMENTO_OBS = 270;
    public static int TAMANYO_ELEMENTO_SIN_OBS = 150;

    public static String lanzoni = "http://192.168.2.28:8080/restfull-web-services-app-master/rest/user/";
    public static String lanzoniProductos = "http://192.168.2.28:8080/restfull-web-services-app-master/rest/product/";
    public static String flavio  = "http://192.168.2.57:8080/neoclinica/rest/user/"; //existeusu
    public static String flavioProductos  = "http://192.168.2.57:8080/neoclinica/rest/product/"; //existeusu
    public static String hsServer = "http://192.168.2.121:8080/valida-prof/servlet/isCobol"; //(HS-AGENDA)"; //get_matricula=123456&get_pass=1234";
    public static String hsServerProductos = "http://192.168.2.121:8080/valida-prof/servlet/isCobol(VALIDA_PROFE_INI)";
    public static String hsServerNombre = "http://highsoft.no-ip.org:8080/neoclinicaserver/rest/user/";
    public static String hsServerNombreProductos = "http://highsoft.no-ip.org:8080/neoclinicaserver/rest/product/";

    public static String delPuebloNombre = "http://delpueblocentral.ddns.net:8181/neoclinica/rest/user/";
    public static String delPuebloNombreProductos = "http://delpueblocentral.ddns.net:8181/neoclinica/rest/product/";

    public static String pathConnection = hsServer; //delPuebloNombre;
    //public static String pathConnectionProductos = hsServerProductos; //delPuebloNombreProductos;
    public static String metodoHsAgenda = "(HS-AGENDA)";
    public static String metodoValidaProfeIni = "(VALIDA_PROFE_INI)";
    public static String metodoGetAgendaDia = "(GET_AGENDA_DIA)";

    public static String pdfName = "NeoClinica Terminos y Condiciones.pdf";


}
