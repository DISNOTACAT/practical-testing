package com.testcode.sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

import com.testcode.sample.cafekiosk.spring.api.client.mail.MailSendClient;
import com.testcode.sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import com.testcode.sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // @Mock 사용시 필수
class MailServiceTest {

  @Spy
  private MailSendClient mailSendClient;
  @Mock
  private MailSendHistoryRepository mailSendHistoryRepository;

  @InjectMocks
  private MailService mailService;

  @DisplayName("메일 전송 테스트")
  @Test
  void sendMail() {
    // given
//    @Spy 사용시 when 사용 불가 do 사용
//    Mockito.when(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
//        .thenReturn(true);

    doReturn(true)
        .when(mailSendClient) // 내부 여러 메서드가 있을 때, 하위에 명시된 메서드만 목으로 돌림. 그외는 실제 객체로 동작함 (일부만 stubbing하고 싶을 때 사용)
        .sendMail(anyString(), anyString(), anyString(), anyString());

    // when
    boolean result = mailService.sendMail("","","", "");


    // then
    Mockito.verify(mailSendHistoryRepository, times(1))
        .save(any(MailSendHistory.class)); // save 가 한번 동작함을 검증
    assertThat(result).isTrue();
  }

}