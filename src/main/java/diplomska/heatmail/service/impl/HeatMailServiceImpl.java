package diplomska.heatmail.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplomska.heatmail.dto.*;
import diplomska.heatmail.kafka.KafkaProducer;
import diplomska.heatmail.model.HeatMail;
import diplomska.heatmail.model.HeatMailAttachment;
import diplomska.heatmail.model.User;
import diplomska.heatmail.model.enums.HeatMailStatusEnum;
import diplomska.heatmail.repository.HeatMailAttachmentRepository;
import diplomska.heatmail.repository.HeatMailRepository;
import diplomska.heatmail.service.HeatMailService;
import diplomska.heatmail.service.UserService;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.*;

import jakarta.mail.util.ByteArrayDataSource;
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

    private final HeatMailAttachmentRepository heatMailAttachmentRepository;

    private final Gson gson = new GsonBuilder().create();

    private final String variableSeparator = ";";
    private final String variableRegex = "%noData%";


    @Autowired
    public HeatMailServiceImpl(JavaMailSender mailSender, UserService userService, HeatMailRepository heatMailRepository, KafkaProducer kafkaProducer, HeatMailAttachmentRepository heatMailAttachmentRepository) {
        this.mailSender = mailSender;
        this.userService = userService;
        this.heatMailRepository = heatMailRepository;
        this.kafkaProducer = kafkaProducer;
        this.heatMailAttachmentRepository = heatMailAttachmentRepository;
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
    public void sendMultipartMail(MailDto mailDto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMultipart multipart = new MimeMultipart();

        message.setRecipients(MimeMessage.RecipientType.TO, mailDto.getTo());
        message.setSubject(mailDto.getTitle());

        // Create the message body part (HTML)
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(mailDto.getBody(), "text/html; charset=utf-8");
        multipart.addBodyPart(messageBodyPart);

        // Create the attachment part
        if (null!=mailDto.getAttachment() && null != mailDto.getAttachment_title()) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            byte[] pdfBytes = Base64.getDecoder().decode(mailDto.getAttachment());
            DataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
            attachmentPart.setDataHandler(new DataHandler(dataSource));
            attachmentPart.setFileName(mailDto.getAttachment_title());
            multipart.addBodyPart(attachmentPart);

        }

        // Set content
        message.setContent(multipart);

        try {
            mailSender.send(message);
            heatMailRepository.updateStatusAndSent_atById(HeatMailStatusEnum.FINISHED, new Date(), mailDto.getId());
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

            if (null != heatMail.getMail_attachment_title()) {
                List<HeatMailAttachment> heatMailAttachment = heatMailAttachmentRepository.findByUser_IdAndMail_attachment_title(user.getId(),heatMail.getMail_attachment_title());
                if (heatMailAttachment.size() >0) {
                    mailDto.setAttachment(heatMailAttachment.get(0).getMail_attachment_body());
                    mailDto.setAttachment_title(heatMailAttachment.get(0).getMail_attachment_title());
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
    public void saveHeatMailAttachments(List<HeatMailAttachmentDto> heatMailAttachmentDtoList) {
        for (HeatMailAttachmentDto heatMailAttachmentDto : heatMailAttachmentDtoList) {
            HeatMailAttachment heatMailAttachment = mapHeatMailAttachmentDtoToHeatMailAttachment(heatMailAttachmentDto);
            heatMailAttachmentRepository.save(heatMailAttachment);
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
    public List<HeatMailStatisticsDto> getMailStatistics(String month, String year) {
        List<HeatMailStatisticsDto> heatMailStatisticsDtoList = new ArrayList<>();
        User user = userService.getUserFromToken();

        long statisticForAll = heatMailRepository.countByUser_IdAndMonthAndYear(user.getId(), month, year);

        HeatMailStatisticsDto heatMailStatisticsDtoForAll = new HeatMailStatisticsDto();
        heatMailStatisticsDtoForAll.setName("ALL_HEATMAIL");
        heatMailStatisticsDtoForAll.setValue(statisticForAll);

        heatMailStatisticsDtoList.add(heatMailStatisticsDtoForAll);


        for(HeatMailStatusEnum heatMailStatusEnum : HeatMailStatusEnum.values()){
            long statistcForEnum = heatMailRepository.countByUser_IdAndMonthAndYearAndStatus(user.getId(), month, year, heatMailStatusEnum);

            HeatMailStatisticsDto heatMailStatisticsDto = new HeatMailStatisticsDto();
            heatMailStatisticsDto.setName(heatMailStatusEnum.toString());
            heatMailStatisticsDto.setValue(statistcForEnum);

            heatMailStatisticsDtoList.add(heatMailStatisticsDto);

        }
        return heatMailStatisticsDtoList;
    }

    @Override
    public HeatMailStatisticsDto getAvailableMailToSendCount(String month, String year) {
        HeatMailStatisticsDto heatMailStatisticsDto = new HeatMailStatisticsDto();
        User user = userService.getUserFromToken();

        long statistcForEnumImported = heatMailRepository.countByUser_IdAndMonthAndYearAndStatus(user.getId(), month, year, HeatMailStatusEnum.IMPORTED);
        long statistcForEnumFailed = heatMailRepository.countByUser_IdAndMonthAndYearAndStatus(user.getId(), month, year, HeatMailStatusEnum.FAILED);

        long statistcForEnum = statistcForEnumFailed + statistcForEnumImported;
        heatMailStatisticsDto.setName(HeatMailStatusEnum.IMPORTED.toString());
        heatMailStatisticsDto.setValue(statistcForEnum);

        return heatMailStatisticsDto;
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
                .mail_attachment_title(heatMailDto.getMail_attachment_title())
                .build();
        return heatMail;
    }

    @Override
    public HeatMailAttachment mapHeatMailAttachmentDtoToHeatMailAttachment(HeatMailAttachmentDto heatMailAttachmentDto) {

        HeatMailAttachment heatMailAttachment = HeatMailAttachment.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .user(userService.getUserFromToken())
                .mail_attachment_body(heatMailAttachmentDto.getMail_attachment_body())
                .mail_attachment_title(heatMailAttachmentDto.getMail_attachment_title())
                .build();

        return heatMailAttachment;
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
                .mail_attachment_title(heatMail.getMail_attachment_title())
                .build();

        return heatMailDashboardDto;
    }

    @Override
    public boolean checkIfAttachmentExistsForUser(HeatMailAttachmentDto heatMailAttachmentDto) {
        if (heatMailAttachmentRepository.findByUser_IdAndMail_attachment_title(
                userService.getUserFromToken().getId(), heatMailAttachmentDto.getMail_attachment_title()).size() > 0) {
            return true;
        } else{
            return false;
        }
    }
}
