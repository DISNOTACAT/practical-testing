package com.testcode.sample.cafekiosk.spring.api.client.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailSendClient {

  public boolean sendMail(String fromEmail, String toEmail, String subject, String content) {
    // 외부 API 로 메일 전동
    log.info("메일 전송");
    throw new IllegalArgumentException("메일 전송"); // 외부와 연결되는 기능 -> 테스트마다 외부로 메일 전송 해야하는가? --> Mocking 활용
  }
}
