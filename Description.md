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