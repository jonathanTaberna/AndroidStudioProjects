package hsneoclinica.neoclinica.constantes;

public class constantes {

    //preferencias
    public static String neoclinicaConfig = "neoclinica.config";

    //constantes
    public static String CONFIG_NOT_FOUND = "CONFIG_NOT_FOUND";
    public static String CONFIG_FOUND = "CONFIG_FOUND";
    public static String HS_ACTIVITY = "HS_ACTIVITY";
    public static String LOGIN_ACTIVITY = "LOGIN_ACTIVITY";
    public static int RESULT_CERRAR_SESION = 111;
    public static int RESULT_MAIN_ACTIVITY = 222;
    public static int RESULT_HS_ACTIVITY = 333;
    public static int TAMANYO_ELEMENTO_OBS = 280;
    public static int TAMANYO_ELEMENTO_SIN_OBS = 150;
    public static int TAMANYO_TEXT_SIZE = 17;


    //constantes para permisos REQUEST CODE
    public static final int REQUEST_CODE_GEOLOCALIZACION = 10;


    public static String sanLucasServer  = "http://200.123.248.20:9500/valida-prof/servlet/isCobol";
    public static String neoServer  = "http://neomat.dyndns.org:9500/valida-prof/servlet/isCobol";
    public static String sanatorioPrivadoServer  = "http://sanatorioprivado.no-ip.org:9500/valida-prof/servlet/isCobol";
    public static String odontograssiServer  = "http://neomat.dyndns.org:9500/valida-prof/servlet/isCobol";
    public static String resonanciaR4Server  = "http://neomat.dyndns.org:9500/valida-prof/servlet/isCobol";
    public static String hospitalGralDehezaServer  = "http://neomat.dyndns.org:9500/valida-prof/servlet/isCobol";
    public static String urologicoServer  = "http://neomat.dyndns.org:9500/valida-prof/servlet/isCobol";
    public static String clinicaPrivadaGralDehezaServer  = "http://neomat.dyndns.org:9500/valida-prof/servlet/isCobol";
    public static String hsServer = "http://192.168.2.121:9500/valida-prof/servlet/isCobol"; //(HS-AGENDA)";

    public static String pathConnection;// = neoServer;
    public static String metodoHsAgenda = "(HS-AGENDA)";
    public static String metodoValidaProfeIni = "(VALIDA_PROFE_INI)";
    public static String metodoGetAgendaDia = "(GET_AGENDA_DIA)";
    public static String metodoGetInternacionesDia = "(GET_INTERNACIONES_DIA)";
    public static String metodoGetNoJob = "(GET_NO_JOB)";
    public static String metodoGetProfesionales = "(GET_PROFESIONALES)";
    public static String metodoRegPosicion = "(REG_POSICION)";

    public static String cuitHS = "30640685782";
    public static String passHS = "H"; //"HS852";
    public static Boolean activarGeoLocalizacion = true; //activa o desactiva la geolocalizacion
    public static double presicionGeolocalizacion = 30;
    public static int tiempoEsperaGeolocalizacion = 3;

}
