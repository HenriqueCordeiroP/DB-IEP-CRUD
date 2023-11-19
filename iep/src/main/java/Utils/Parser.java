package Utils;

import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;

public class Parser {
    
    public static Map<String, String> parseAppointmentFromRequest(HttpServletRequest request){
        Map<String, String> appointmentMap = new HashMap<>();
        appointmentMap.put("data", Conversion.parseStringOrNull(request.getParameter("data")));
        appointmentMap.put("descricao", Conversion.parseStringOrNull(request.getParameter("descricao")));
        String confirmed = "on".equals(request.getParameter("confirmada")) ? "true" : "false";
        appointmentMap.put("confirmada", confirmed);
        appointmentMap.put("historia_clinica", Conversion.parseStringOrNull(request.getParameter("historia_clinica")));
        appointmentMap.put("CID", Conversion.parseStringOrNull(request.getParameter("CID")));
        appointmentMap.put("cpf", Conversion.parseStringOrNull(request.getParameter("cpf")));
        appointmentMap.put("cpf_medico", Conversion.parseStringOrNull(request.getParameter("cpf_medico")));

    return appointmentMap;
    }
}
