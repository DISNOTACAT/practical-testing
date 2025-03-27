package com.testcode.sample.cafekiosk.spring.api.service.mail;

import com.testcode.sample.cafekiosk.spring.api.client.mail.MailSendClient;
import com.testcode.sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import com.testcode.sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

  private final MailSendClient mailSendClient;
  private final MailSendHistoryRepository mailSendHistoryRepository;

  public boolean sendMail(String fromEmail, String toEmail, String subject, String content) {

    boolean result = mailSendClient.sendMail(fromEmail, toEmail, subject, content);
    if(result) {
      mailSendHistoryRepository.save(MailSendHistory.builder()
              .fromEmail(fromEmail)
              .toEmail(toEmail)
              .subject(subject)
              .content(content)
              .build()
          );
      return true;
    }

    return false;
  }
}
