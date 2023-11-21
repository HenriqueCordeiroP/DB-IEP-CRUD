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
        appointmentMap.put("cpf", Conversion.parseStringOrNull(request.getParameter("cpf_paciente")));
        appointmentMap.put("cpf_medico", Conversion.parseStringOrNull(request.getParameter("cpf_medico")));
        System.out.println(appointmentMap);
        return appointmentMap;
    }

    public static Map<String, String> parseDoctorFromRequest(HttpServletRequest request){
        Map<String, String> doctorMap = new HashMap<>();
        doctorMap.put("nome", Conversion.parseStringOrNull(request.getParameter("nome")));
        doctorMap.put("rg", Conversion.parseStringOrNull(request.getParameter("rg")));
        doctorMap.put("cpf", Conversion.parseStringOrNull(request.getParameter("cpf")));
        doctorMap.put("tel_celular", Conversion.parseStringOrNull(request.getParameter("tel_celular")));
        doctorMap.put("tel_residencial", Conversion.parseStringOrNull(request.getParameter("tel_residencial")));
        doctorMap.put("email", Conversion.parseStringOrNull(request.getParameter("email")));
        doctorMap.put("cidade", Conversion.parseStringOrNull(request.getParameter("cidade")));
        doctorMap.put("bairro", Conversion.parseStringOrNull(request.getParameter("bairro")));
        doctorMap.put("rua", Conversion.parseStringOrNull(request.getParameter("rua")));
        doctorMap.put("numero", Conversion.parseStringOrNull(request.getParameter("numero")));
        doctorMap.put("dt_nascimento", Conversion.parseStringOrNull(request.getParameter("dt_nascimento")));
        doctorMap.put("sexo", Conversion.parseStringOrNull(request.getParameter("sexo")));
        doctorMap.put("crm", Conversion.parseStringOrNull(request.getParameter("crm")));
        doctorMap.put("rqe", Conversion.parseStringOrNull(request.getParameter("rqe")));
        doctorMap.put("especialidade", Conversion.parseStringOrNull(request.getParameter("especialidade")));

        return doctorMap;
    }

    public static Map<String, String> parsePatientFromRequest(HttpServletRequest request){
        Map<String, String> patientMap = new HashMap<>();

        patientMap.put("nome", Conversion.parseStringOrNull(request.getParameter("nome")));
        patientMap.put("nome_social", Conversion.parseStringOrNull(request.getParameter("nome_social")));
        patientMap.put("rg", Conversion.parseStringOrNull(request.getParameter("rg")));
        patientMap.put("cpf", Conversion.parseStringOrNull(request.getParameter("cpf")));
        patientMap.put("tel_celular", Conversion.parseStringOrNull(request.getParameter("tel_celular")));
        patientMap.put("tel_residencial", Conversion.parseStringOrNull(request.getParameter("tel_residencial")));
        patientMap.put("email", Conversion.parseStringOrNull(request.getParameter("email")));
        patientMap.put("cidade", Conversion.parseStringOrNull(request.getParameter("cidade")));
        patientMap.put("bairro", Conversion.parseStringOrNull(request.getParameter("bairro")));
        patientMap.put("rua", Conversion.parseStringOrNull(request.getParameter("rua")));
        patientMap.put("numero", Conversion.parseStringOrNull(request.getParameter("numero")));
        patientMap.put("dt_nascimento", Conversion.parseStringOrNull(request.getParameter("dt_nascimento")));
        patientMap.put("sexo", Conversion.parseStringOrNull(request.getParameter("sexo")));
        patientMap.put("convenio", Conversion.parseStringOrNull(request.getParameter("convenio")));
        patientMap.put("profissao", Conversion.parseStringOrNull(request.getParameter("profissao")));
        patientMap.put("quem_indicou", Conversion.parseStringOrNull(request.getParameter("quem_indicou")));
        patientMap.put("imc", Conversion.parseStringOrNull(request.getParameter("imc")));
        patientMap.put("cintura_abdominal", Conversion.parseStringOrNull(request.getParameter("cintura_abdominal")));
        patientMap.put("peso", Conversion.parseStringOrNull(request.getParameter("peso")));
        patientMap.put("altura", Conversion.parseStringOrNull(request.getParameter("altura")));
        patientMap.put("alergias", Conversion.parseStringOrNull(request.getParameter("alergias")));
        patientMap.put("pressao", Conversion.parseStringOrNull(request.getParameter("pressao")));

        return patientMap;
    }

    public static Map<String, String> parseFilterFromAppointment(HttpServletRequest request){
        Map<String, String> dateMap = new HashMap<>();

        dateMap.put("start_date", Conversion.parseStringOrNull(request.getParameter(("start_date"))));
        dateMap.put("end_date", Conversion.parseStringOrNull(request.getParameter(("end_date"))));
    
        return dateMap;
    }

    public static Map<String, String> parseFilterFromSearch(HttpServletRequest request){
        Map<String, String> map = new HashMap<>();

        map.put("search", Conversion.parseStringOrNull(request.getParameter("search_str")));

        return map;
    }
}
