package com.codeit.project.deokhugam;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DeokhugamApplicationTests {

  @Test
  void test(){
    assertThat("test").isEqualTo("test");
  }

}
