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
    public static int TAMANYO_ELEMENTO_OBS = 280;
    public static int TAMANYO_ELEMENTO_SIN_OBS = 150;

    public static String neoServer  = "http://neomat.dyndns.org:9500/valida-prof/servlet/isCobol"; //existeusu
    public static String hsServer = "http://192.168.2.121:8080/valida-prof/servlet/isCobol"; //(HS-AGENDA)";

    public static String pathConnection = hsServer; //delPuebloNombre;
    public static String metodoHsAgenda = "(HS-AGENDA)";
    public static String metodoValidaProfeIni = "(VALIDA_PROFE_INI)";
    public static String metodoGetAgendaDia = "(GET_AGENDA_DIA)";

    public static String pdfName = "NeoClinica Terminos y Condiciones.pdf";


}
