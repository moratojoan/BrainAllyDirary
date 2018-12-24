package com.example.joan.brainallydiary;


//LOCAL
public class Contract {
    //Info per la Taula Usuari Informació pel Login
    public static String NOM_TAULA_Usuari_Info_Login = "Usuari_Info_Login";
    public static String Camp_Usuari_id = "Usuari_id";
    public static String Camp_Email = "Email";
    public static String Camp_Password = "Password";


    //Info per la Taula Usuaris Informació Basica
    public static String NOM_TAULA_Usuari_Info_Basica = "Usuari_Info_Basica";
    public static String Camp_Nom_U = "Nom_U";
    public static String Camp_Cognoms_U = "Cognoms_U";
    public static String Camp_Data_Neixament = "Data_Neixament";
    public static String Camp_Genere = "Genere";
    public static String Camp_Pais = "Pais";
    public static String Camp_Codi_Postal = "Codi_Postal";


    //Info per la Taula Relacio Usuari Infant
    public static String NOM_TAULA_Relacio_Usuari_Infant = "Relacio_Usuari_Infant";
    //Camp Usuari_id
    public static String Camp_Infant_ID = "Infant_ID";
    public static String Camp_Relacio = "Relacio";


    //Info per la Taula Infant Informació Basica
    public static String NOM_TAULA_Infant_Info_Basica = "Infant_Info_Basica";
    //Camp Infant ID
    public static String Camp_Nom_I = "Nom_I";
    public static String Camp_Cognoms_I = "Cognoms_I";
    //Camp Data Neixament
    //Camp Genere
    //Camp Pais
    //Camp Codi Postal


    //Info per la Taula Infant Informació Transtorn
    public static String NOM_TAULA_Infant_Info_Trastorn = "Infant_Info_Trastorn";
    //Camp Infant ID
    public static String Camp_Trastorn_Principal = "Trastorn_Principal";


    //Info per la Taula Infant Medicines
    public static String NOM_TAULA_Infant_Medicines = "Infant_Medicines";
    //Camp Infant ID
    public static String Camp_Medicina = "Medicina";


    //Info per la Taula Activitats
    public static String NOM_TAULA_Activitats_Infants = "Activitats_Infants";
    public static String Camp_Id_Activitat = "ID_Activitat";
    //Camp Infant ID
    public static String Camp_Activitat = "Activitat";
    public static String Camp_ComparadorI = "Comparador_Inicial";
    public static String Camp_DI = "Data_Inicial";
    public static String Camp_DF = "Data_Final";
    public static String Camp_HI = "Hora_Inicial";
    public static String Camp_HF = "Hora_Final";
    public static String Camp_Index_Emocio = "Index_Emocio";
    public static String Camp_Activitat_o_Emocio = "Activitat_o_Emocio";


    //Info per la Taula Companys
    public static String NOM_TAULA_Relacio_Companys_Infants = "Relacio_Companys_Infants";
    public static String Camp_Company_id = "Company_id";
    public static String Camp_Nom_C = "Nom_C";
    public static String Camp_Cognoms_C = "Cognoms_C";
    //Camp Infant Id
    //Camp Nom Infant
    //Camp Cognoms Infant
    public static String Camp_Relacio_CI = "Relacio_CI";


    //Trofeus
    public static String NOM_TAULA_Trofeus = "Trofeus";
    public static String Camp_Totals = "Totals";
    public static String Camp_Mensuals = "Mensuals";
    public static String Camp_Setmanals = "Setmanals";
    public static String Camp_Diaris = "Diaris";
    public static String Camp_Ultim_Dia_Entrat = "Ultim_Dia_Entrat";
    public static String Camp_Ultim_Dia_20 = "Ultim_Dia_20";
    public static String Camp_Contador_03 = "Dies_Seguits_03";
    public static String Camp_lvl_perseverancia = "lvl_perseverancia";
    public static String Camp_Contador_perseverancia = "Dies_Seguits";
    public static String Camp_RatingBar_perseverancia = "RatingBar_perseverancia";
    public static String Camp_lvl_registres = "lvl_registres";
    public static String Camp_Contador_registres = "Registres";
    public static String Camp_RatingBar_registres = "RatingBar_registres";


    //Notificacions de les Activitats
    public static String NOM_TAULA_Notificacions_Activitats = "Notificacions_Activitats";
    //Camp ID Infant
    //Camp ID Activitat
    public static String Camp_id_Notif = "id_Notificacions";
    public static String Camp_Titol = "Titol";
    public static String Camp_Missatge = "Missatge";
    public static String Camp_ComparadorF = "Comparador_Final";
    //Camp Data Final
    //Camp Hora Final






    //Taules
    public static final String TAULA_Usuaris_Info_Login = "CREATE TABLE "+NOM_TAULA_Usuari_Info_Login+" (" +
            ""+Camp_Usuari_id+" TEXT,"+Camp_Email+" TEXT,"+Camp_Password+" TEXT)";

    public static final String TAULA_Usuaris_Info_Basica = "CREATE TABLE "+NOM_TAULA_Usuari_Info_Basica+" (" +
            ""+Camp_Nom_U+" TEXT,"+Camp_Cognoms_U+" TEXT,"+Camp_Data_Neixament+" TEXT,"+Camp_Genere+" TEXT,"+Camp_Pais+" TEXT,"+Camp_Codi_Postal+" TEXT)";

    public static final String TAULA_Relacio_Usuari_Infants = "CREATE TABLE "+NOM_TAULA_Relacio_Usuari_Infant+" (" +
            ""+Camp_Infant_ID+" TEXT,"+Camp_Relacio+" TEXT)";

    public static final String TAULA_Infants_Info_Basica = "CREATE TABLE "+NOM_TAULA_Infant_Info_Basica+" (" +
            ""+Camp_Infant_ID+" TEXT,"+Camp_Nom_I+" TEXT,"+Camp_Cognoms_I +" TEXT,"+Camp_Data_Neixament+" TEXT,"+Camp_Genere+" TEXT,"+Camp_Pais+" TEXT,"+Camp_Codi_Postal+" TEXT)";

    public static final String TAULA_Infants_Info_Trastorn = "CREATE TABLE "+NOM_TAULA_Infant_Info_Trastorn+" (" +
            ""+Camp_Infant_ID+" TEXT,"+Camp_Trastorn_Principal+" TEXT)";

    public static final String TAULA_Infants_Medicines = "CREATE TABLE "+NOM_TAULA_Infant_Medicines+" (" +
            ""+Camp_Infant_ID+" TEXT,"+Camp_Medicina+" TEXT)";

    public static final String TAULA_Activitats_Infants = "CREATE TABLE "+NOM_TAULA_Activitats_Infants+" (" +
            ""+Camp_Id_Activitat+" TEXT,"+Camp_Infant_ID+" TEXT,"+Camp_Activitat+" TEXT,"+Camp_ComparadorI+" INTEGER,"+Camp_DI+" TEXT,"+Camp_DF+" TEXT,"+Camp_HI+" TEXT,"+Camp_HF+" TEXT,"+Camp_Index_Emocio+" INTEGER,"+Camp_Activitat_o_Emocio+" TEXT)";

    public static final String TAULA_Relacio_Companys_Infants = "CREATE TABLE "+NOM_TAULA_Relacio_Companys_Infants+" (" +
            ""+Camp_Company_id+" TEXT,"+Camp_Nom_C+" TEXT,"+Camp_Cognoms_C+" TEXT,"+Camp_Infant_ID+" TEXT,"+Camp_Nom_I+" TEXT,"+Camp_Cognoms_I+" TEXT,"+Camp_Relacio_CI+" TEXT)";

    public static final String TAULA_Trofeus = "CREATE TABLE "+NOM_TAULA_Trofeus+" (" +
            ""+Camp_Totals+" INTEGER,"+Camp_Mensuals+" INTEGER,"+Camp_Setmanals+" INTEGER,"+Camp_Diaris+" INTEGER," +
            ""+Camp_Ultim_Dia_Entrat+" TEXT,"+Camp_Ultim_Dia_20+" TEXT,"+Camp_Contador_03+" INTEGER," +
            ""+Camp_lvl_perseverancia+" INTEGER,"+Camp_Contador_perseverancia+" INTEGER,"+Camp_RatingBar_perseverancia+" INTEGER," +
            ""+Camp_lvl_registres+" INTEGER,"+Camp_Contador_registres+" INTEGER,"+Camp_RatingBar_registres+" INTEGER)";

    public static final String TAULA_Notificacions_Activitats = "CREATE TABLE "+ NOM_TAULA_Notificacions_Activitats+" (" +
            ""+Camp_Infant_ID+" TEXT," +Camp_Id_Activitat+" TEXT,"+
            "" +Camp_id_Notif+" INTEGER,"+Camp_Titol+" TEXT,"+Camp_Missatge+" TEXT,"+Camp_ComparadorF+" INTEGER," +
            ""+Camp_DF+" TEXT,"+Camp_HF+" TEXT)";

}
