package com.koopers.techservice.data.remote;

import com.koopers.techservice.App;
import com.koopers.techservice.utils.sharedPref.DarkModePrefManager;

import java.util.Objects;

//Cadenas que almacenas la informacion con la que accedemos a cada servicio de la Api

public class ApiConstants {

    public static String getURLBASE(){
        String IpAdress = new DarkModePrefManager(App.instance.getApplicationContext()).getIpAddress();
        IpAdress = "http://" + IpAdress + "/hardware/";
        return IpAdress;
    }

    private static final String ROOT_URL = "Api.php?apicall=";

    //#Usuario
    public static final String CREAR_USUARIO = ROOT_URL + "crear_usuario";
    public static final String LEER_USUARIO = ROOT_URL + "leer_usuario";
    public static final String EDIT_USUARIO = ROOT_URL + "edit_usuario";

    //#Equipo
    public static final String LEER_EQUIPOS = ROOT_URL + "leer_equipos";
    public static final String LEER_EQUIPO = ROOT_URL + "leer_equipo";
    public static final String CREAR_EQUIPO = ROOT_URL + "crear_equipo";
    public static final String EDITAR_EQUIPO = ROOT_URL + "editar_equipo";

    //#Estado
    public static final String LEER_ESTADOS = ROOT_URL + "leer_estado";

    //#Categoria
    public static final String LEER_CATEGORIAS = ROOT_URL + "leer_categoria";

    //#Elemento
    public static final String LEER_COMPONENTES = ROOT_URL + "leer_componente";

    //#Imagen
    public static final String CARGAR_IMAGEN = getURLBASE() + "users/images/";
    public static final String SUBIR_IMAGEN = getURLBASE() + "fileUpload.php";

}
