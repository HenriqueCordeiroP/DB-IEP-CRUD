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

    public static Map<String, String> parseDoctorFromRequest(HttpServletRequest request){
        Map<String, String> doctorMap = new HashMap<>();
        doctorMap.put("nome", Conversion.parseStringOrNull(request.getParameter("nome")));
        doctorMap.put("rg", Conversion.parseStringOrNull(request.getParameter("rg")));
        doctorMap.put("cpf", Conversion.parseStringOrNull(request.getParameter("cpf")));
        doctorMap.put("celular", Conversion.parseStringOrNull(request.getParameter("celular")));
        doctorMap.put("residencial", Conversion.parseStringOrNull(request.getParameter("residencial")));
        doctorMap.put("email", Conversion.parseStringOrNull(request.getParameter("email")));
        doctorMap.put("cidade", Conversion.parseStringOrNull(request.getParameter("cidade")));
        doctorMap.put("bairro", Conversion.parseStringOrNull(request.getParameter("bairro")));
        doctorMap.put("rua", Conversion.parseStringOrNull(request.getParameter("rua")));
        doctorMap.put("numero", Conversion.parseStringOrNull(request.getParameter("numero")));
        doctorMap.put("data_nascimento", Conversion.parseStringOrNull(request.getParameter("data_nascimento")));
        doctorMap.put("sexo", Conversion.parseStringOrNull(request.getParameter("sexo")));
        doctorMap.put("crm", Conversion.parseStringOrNull(request.getParameter("crm")));
        doctorMap.put("rqe", Conversion.parseStringOrNull(request.getParameter("rqe")));
        doctorMap.put("especialidade", Conversion.parseStringOrNull(request.getParameter("especialidade")));

        return doctorMap;
    }
}
