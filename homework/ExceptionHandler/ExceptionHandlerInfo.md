# 예외처리 설명

---

### 1. ExceptionHandler

- 역할 : 예외가 발생했을 때, 이 예외를 어떻게 처리해야 할 지에 관련해서 호출되는

→ “**예외 처리 담당 컨트롤러 메소드**”

- 위치 : 컨트롤러 내부에 선언되어 있을 수 있고 / 따로 전역적으로 설정된 클래스 내부에 존재 가능
- 메소드가 어디에 존재하는 지에 따라서 아래로 명명

→ (**A) : 로컬 예외 처리 /  (B) : 전역 예외 처리**

→ 클라이언트가 서버로 요청 url을 보내고 해당 url에 맞는 컨트롤러가 호출이 될 것임. 이때 스프링이 관련 예외처리를 담당하는 메소드를 호출하는 것.

### 2. 로컬 예외 처리 -(A)

- 컨트롤러 클래스 안에서만 예외를 처리할 수 있음
- 클래스 안에 예외 처리 메소드가 존재하기 때문에 어노테이션을 사용함

→ @ExceptionHandler가 위에 붙은 메소드를 스프링이 찾아서 코드 실행의 흐름을 변경

- 예시 코드

```java
@Controller
public class MyController {
    
    @GetMapping("/example")
    public String example() {
        throw new IllegalArgumentException("잘못된 입력!");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("예외 발생: " + e.getMessage());
    }
}

```

▶️ “/example”이 서버로 들어오면 해당 컨틀러인 <마이컨트롤러>가 호출됨

▶️ 예외 객체 타입인 IllegalArgument인 예외를 나타내는 객체 생성

→ 해당 클래스(C1)가 예외 처리 메소드의 매개변수로 던져짐

▶️ 그러면 코드 흐름이 중지되고 → 스프링이 어노테이션이 붙은 메소드를 찾아서 실행

→ hanlderIllegalArgument가 실행되는 것

→ C1 객체가 예외처리 메소드의 e로

→ 리턴 타입이 responseEntity로 설정

ResponseEntity
.status(HttpStatus.BAD_REQUEST)  ⇒ 응답의 상태코드가 400
.body("예외 발생: " + e.getMessage()); ⇒ 응답 본문에 “잘못된 입력!”이 적힘

⇒ ResponseEntity로 응답 전체를 담아서 클라리언트로 전달

### 3. 전역 예외 처리 -(B)

- 로컬 예외 처리는 특정 컨트롤러 내부에 예외 처리 메소드가 존재하기 때문에 다른 컨트롤러가 이 예외 처리 메소드를 호출할 수 없음
- 하지만 예외 처리 메소드는 여러 컨트롤러에서 필요할 수 있음

→ 매핑되는 정보에 따라 실행되는 컨트롤러는 다르게 지정해 놓았을 때, 동일한 예외 처리 메소드를 필요로 할 수 있기 때문에 전역 예외 처리가 더 확장성이 큼

- 코드 전체 구성은 로컬 예외 처리와 비슷하지만 ! 사용되는 어노테이션이 다르다.

→ @ControllerAdvice가 예외 처리 메소드가 존재하는 클래스에 붙여진다.

→ 위에서 말했듯, 여러 컨트롤러에서 공통적으로 필요한 예외 처리를 가능하게 해주는 것

- 예시 코드

```java
@Controller
public class MyController {

    @GetMapping("/example")
    public String example() {
        throw new IllegalArgumentException("잘못된 입력!");
    }
}

```

```java
// 전역 예외 처리 클래스에 따로 넣어놓음
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("전역 예외 발생: " + e.getMessage());
    }
}

```

▶️ throw 를 통해서 예외 처리 객체가 생성되면 스프링이 예외를 알게 됨

▶️ @ControllerAdvice 어노테이션이 붙은 클래스에 @ExceptionHandler가 붙은 메소드를 찾아 실행

▶️ 이후 과정은 로컬과 동일

✅️  iceBreaker code 에는?

- 응답 본문에 텍스트 타입이 적히는 방식이 아닌 json으로 보내는 방법

return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.fail(”IE”, “(잘못된 입력));

→ dto 클래스를 사용해서 json 데이터 형식에 맞춰서 클라이언트로 응답 메시지를 보냄

### 4. 로컬과 전역 모두 공통적으로 적용할 수 있는 흐름

1. 클라이언트가 URL 요청 → 특정 컨트롤러 메서드 실행
2. 해당 메서드에서 예외 발생 (`throw new SomeException(...)`)
3. Spring이 예외를 감지
4.  해당 컨트롤러 내부에 `@ExceptionHandler`가 있는지 먼저 찾음
5.  없다면 → `@ControllerAdvice`가 붙은 클래스들 중에서
    
    `@ExceptionHandler(예외타입)` 메서드를 찾아 실행함
    
6. 예외를 처리하고 `ResponseEntity` 응답 반환

### ✅️ advICE code는 전역으로 처리되어 있음

- main → exception > businessException + GlobalExceptionHandler

→ businessException : 예외 처리 객체 

→ GlobalExceptionHandler : 예외 처리 메소드가 존재하는 클래스