package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.AppData;
import model.Cliente;

import java.io.File;

public class JacksonExport {

    //Se usa el .registerModule(new JavaTimeModule()) para que jakson maneje correctactemente el uso de fechas
    // Se usa el .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) para que se escriban las
    //fechas en formato YYYY-MM-DD en el json, en lugar de un numero
    //crea sangria para hacer el JSOM mas legible

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static void exportToJson(AppData appData,String filePath){
        try{
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();
            MAPPER.writeValue(file,appData);
            System.out.println("Datos exportados a JSON en: " + filePath);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static AppData importFromJson(String filePath){
        try{
            return MAPPER.readValue(new File(filePath),AppData.class);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }




}
