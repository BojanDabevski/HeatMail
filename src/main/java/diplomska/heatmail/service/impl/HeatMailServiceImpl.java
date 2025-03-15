package diplomska.heatmail.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplomska.heatmail.dto.HeatMailDashboardDto;
import diplomska.heatmail.dto.HeatMailDto;
import diplomska.heatmail.dto.MailDto;
import diplomska.heatmail.kafka.KafkaProducer;
import diplomska.heatmail.model.HeatMail;
import diplomska.heatmail.model.User;
import diplomska.heatmail.model.enums.HeatMailStatusEnum;
import diplomska.heatmail.repository.HeatMailRepository;
import diplomska.heatmail.service.HeatMailService;
import diplomska.heatmail.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HeatMailServiceImpl implements HeatMailService {

    private final JavaMailSender mailSender;
    private final UserService userService;

    private final HeatMailRepository heatMailRepository;

    private final KafkaProducer kafkaProducer;

    private final Gson gson = new GsonBuilder().create();

    private final String variableSeparator = ";";
    private final String variableRegex = "%noData%";


    @Autowired
    public HeatMailServiceImpl(JavaMailSender mailSender, UserService userService, HeatMailRepository heatMailRepository, KafkaProducer kafkaProducer) {
        this.mailSender = mailSender;
        this.userService = userService;
        this.heatMailRepository = heatMailRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void sendEmail(MailDto mailDto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setRecipients(MimeMessage.RecipientType.TO ,mailDto.getTo());
        message.setSubject(mailDto.getTitle());
        message.setContent(mailDto.getBody(),"text/html; charset=utf-8");
        try {
            mailSender.send(message);
            heatMailRepository.updateStatusAndSent_atById(HeatMailStatusEnum.FINISHED, new Date(),mailDto.getId());
        } catch (Exception e) {
            heatMailRepository.updateStatusById(HeatMailStatusEnum.FAILED, mailDto.getId());
        }
    }

    @Override
    public void sendEmailForMonth(String month, String year) {
        User user = userService.getUserFromToken();
        List<HeatMail> heatMailList = heatMailRepository.findUsersImportedHeatMailForMonthAndYear(user.getId(), month, year);
        heatMailList.addAll(heatMailRepository.findUsersFailedHeatMailForMonthAndYear(user.getId(), month, year));

        for (HeatMail heatMail : heatMailList) {
            MailDto mailDto = new MailDto();
            mailDto.setId(heatMail.getId());
            mailDto.setTo(heatMail.getMail_receiver());
            mailDto.setTitle(heatMail.getMail_title());

            String mailBody = heatMail.getMail_body();
            if (null != heatMail.getMail_body_variables()) {
                List<String> mailBodyVariableList = new ArrayList<String>(Arrays.asList(heatMail.getMail_body_variables().split(variableSeparator)));

                for(String mailBodyVariable : mailBodyVariableList) {
                    mailBody = mailBody.replaceFirst(variableRegex,mailBodyVariable);
                }
            }

            mailDto.setBody(mailBody);


            try {
                kafkaProducer.sendMailMessage(gson.toJson(mailDto));
                heatMailRepository.updateStatusById(HeatMailStatusEnum.MAIL_IN_PROCESS, heatMail.getId());
            } catch (Exception e) {
                heatMailRepository.updateStatusById(HeatMailStatusEnum.FAILED, heatMail.getId());
            }
        }
    }

    @Override
    public void saveHeatMail(List<HeatMailDto> heatMailDtoList) {
        for (HeatMailDto heatMailDto : heatMailDtoList) {
            HeatMail heatMail = mapHeatMailDtoToHeatMail(heatMailDto);
            heatMailRepository.save(heatMail);
        }
    }

    @Override
    public List<HeatMailDashboardDto> getMailDashboard(String month, String year) {
        User user = userService.getUserFromToken();
        List<HeatMail> heatMailList = heatMailRepository.findUsersHeatMailForMonthAndYear(user.getId(), month, year);

        List<HeatMailDashboardDto> heatMailDashboardDtoList = new ArrayList<>();

        for (HeatMail heatMail : heatMailList) {
            heatMailDashboardDtoList.add(mapHeatMailToHeatMailDashboardDto(heatMail));
        }

        return heatMailDashboardDtoList;
    }

    @Override
    public HeatMail mapHeatMailDtoToHeatMail(HeatMailDto heatMailDto) {
        HeatMail heatMail = HeatMail.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .month(heatMailDto.getMonth())
                .year(heatMailDto.getYear())
                .user(userService.getUserFromToken())
                .status(HeatMailStatusEnum.IMPORTED)
                .mail_body(heatMailDto.getMail_body())
                .mail_title(heatMailDto.getMail_title())
                .mail_receiver(heatMailDto.getMail_receiver())
                .mail_body_variables(heatMailDto.getMail_body_variables())
                .build();
        return heatMail;
    }

    @Override
    public HeatMailDashboardDto mapHeatMailToHeatMailDashboardDto(HeatMail heatMail) {
        HeatMailDashboardDto heatMailDashboardDto = HeatMailDashboardDto.builder()
                .month(heatMail.getMonth())
                .year(heatMail.getYear())
                .mail_receiver(heatMail.getMail_receiver())
                .mail_title(heatMail.getMail_title())
                .mail_body(heatMail.getMail_body())
                .inserted_at(heatMail.getInserted_at())
                .sent_at(heatMail.getSent_at())
                .mail_status(heatMail.getStatus().toString())
                .build();

        return heatMailDashboardDto;
    }

    //obid za attachment
//    String base64="/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAcHBwcIBwgJCQgMDAsMD/psQZ//Z";
//    byte[] data = DatatypeConverter.parseBase64Binary(base64);
//    String path = "D:/Privatno/Diplomska/HeatMail/HeatMail/src/main/resources" + "/" + "nameFile.png";
//    File file = new File(path);
//
//    OutputStream outputStream = null;
//        try {
//        outputStream = new BufferedOutputStream(new FileOutputStream(file));
//    } catch (FileNotFoundException e) {
//        throw new RuntimeException(e);
//    }
//        helper.addAttachment("", (DataSource) outputStream);
}
