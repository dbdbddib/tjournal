# ajax done responseData
IResponseController 클래스의 makeResponseEntity 메서드는 응답 데이터를 생성하는 공통 메서드

- 입력 매개변수:
    - httpStatus: 응답 HTTP 상태 코드 (e.g., HttpStatus.OK, HttpStatus.BAD_REQUEST).
    - responseCode: 서버에서 정의한 응답 코드 (e.g., R000000).
    - message: 응답에 포함할 메시지 (e.g., "OK", "Error occurred").
    - responseData: 실제로 클라이언트가 사용할 데이터.

- 응답 생성:
  - ResponseDto 객체를 생성합니다.
  - responseCode, message, responseData를 설정합니다.
  - ResponseEntity로 ResponseDto를 감싸서 반환합니다.
  - HTTP 상태 코드와 함께 전송됩니다.


# session, cookies
```
구분	        세션	                          쿠키
저장 위치	서버	                          클라이언트(브라우저)
저장 데이터	사용자 정보 등	                  문자열 데이터
유효 기간	기본적으로 브라우저 종료 시 소멸	  설정된 만료 시간까지 유지
보안	        상대적으로 안전 (서버 관리)	  상대적으로 취약 (클라이언트 관리)
```


# html 실행순서
```
- <head> 태그 파싱 및 처리:
  - <title>, <style>, <script> 등이 실행됨.
  - <script>는 순서대로 실행되며, 이후 코드는 바로 실행됩니다.

- HTML <body> 태그 파싱:
  - <body>의 HTML 요소가 렌더링되고 DOM에 추가됨.

- $(function () { ... })와 $(document).ready(function () { ... }) 실행
  - DOM 트리가 완전히 구성된 후 실행됨.

- $(function() { ... })
  - 축약형 표현으로, jQuery가 제공하는 $(document).ready()의 단축 문법
```

# uuid
- UUID.randomUUID()는 Java에서 랜덤한 UUID(Universally Unique Identifier, 범용 고유 식별자) 를 생성하는 메서드입니다.
- UUID는 전 세계에서 중복될 가능성이 극히 낮은 128비트(16바이트) 식별자를 의미하며, java.util.UUID 클래스에서 제공됩니다.

# Blob
- 파일처럼 동작하지만, 메모리에 존재함
  - Blob 객체는 실제 파일과 유사한 방식으로 동작하지만, 파일 시스템에 저장되지 않고 메모리에 존재합니다.

- 크기가 고정됨 (Immutable, 변경 불가능)
  - Blob 객체를 생성하면 한 번 생성된 데이터를 수정할 수 없고, 새로운 Blob을 만들어야 합니다.

- 부분 데이터 사용 가능
  - slice() 메서드를 사용하면 Blob에서 특정 부분만 잘라서 사용할 수 있습니다.

- 파일을 다룰 때 유용
  - 웹 브라우저에서 파일을 읽거나 다운로드, 미리보기할 때 사용됩니다.


# Bean
1. @Configuration의 역할
   - @Configuration은 이 클래스가 Spring의 설정 클래스임을 나타냅니다.
   -  설정 클래스 내부에서 @Bean을 사용하면 Spring 컨테이너가 해당 객체를 Spring Bean으로 관리합니다.
2. @Bean의 역할
   - @Bean이 붙은 메서드는 Spring이 애플리케이션 시작 시 자동으로 실행하여 반환된 객체를 Spring 컨테이너에 등록합니다.
   -  즉, amazonS3() 메서드가 애플리케이션 시작 시 한 번 실행되고, 그 결과인 AmazonS3 객체가 Spring 컨테이너에 등록됩니다.
   -  이후에는 @Autowired 또는 ApplicationContext.getBean(AmazonS3.class) 등을 통해 재사용할 수 있습니다
   - 싱글톤(Singleton) 패턴은 객체의 인스턴스를 하나만 생성하여 애플리케이션 전역에서 공유
3. Spring Boot가 자동으로 @Bean 메서드를 호출하는 과정
   - Spring Boot가 실행되면서 ApplicationContext를 생성함.
   - @Configuration 클래스를 스캔하여 @Bean이 붙은 메서드를 실행.
   - amazonS3() 메서드가 실행되어 AmazonS3 객체가 생성됨.
   - 생성된 AmazonS3 객체가 Spring 컨테이너에 등록됨 (싱글톤으로 관리).
4. Spring의 기본 동작 방식
   - Spring Boot는 애플리케이션 시작 시 모든 @Bean 메서드를 한 번 실행하여 객체를 생성하고, 이를 컨테이너에서 관리합니다.
    
  

# 싱글톤을 쓰는 이유
1. 불필요한 객체 생성을 방지 → 메모리 절약
   - 동일한 객체를 여러 번 생성하지 않고 하나만 유지하므로 메모리 낭비를 줄일 수 있음.

2. 객체 간의 데이터 일관성 유지
   - 같은 인스턴스를 공유하기 때문에 전역 상태를 유지할 수 있음.

3. 객체 생성 비용 절감
   - 생성 비용이 큰 객체(DB Connection, AmazonS3 Client 등)를 매번 새로 만들지 않고, 하나의 객체만 유지함으로써 성능 최적화.