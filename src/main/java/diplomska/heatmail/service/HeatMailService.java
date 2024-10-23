package diplomska.heatmail.service;

public interface HeatMailService {
    void sendEmail(String to, String subject, String body);
}
