# 실습

---

### 1.  어떤 기능을 제공하는 사이트인가?

- 다음과 같은 상품 정보를 제공하는 사이트

<aside>

🛍️ 상품 정보

1. 상품의 ID
2. 상품 명
3. 상품 가격
4. 상품 수량
</aside>

- 어떤 서비스를 제공 하는가?

<aside>

👉 대표적인 기능 3개

1. 상품들 정보 확인
2. 상품 등록
3. 상품 정보 수정
</aside>

- 어떤 페이지 구조인가?

→ 총 4가지 페이지가 존재

→ 기능은 3가지이지만, 상품의 정보를 확인하는 페이지가 다음 2개로 나눠짐

1. 전체 상품 정보 페이지(목록)
2. 한 상품 정보 페이지

<aside>

1. **상품의 목록**이 보이는 페이지(main) → 위 상품 정보들이 존재
2. 상품 목록에서 상단의 [상품 등록] 버튼 클릭을 통해서 새로운 **상품 등록 페이지** 이동 가능 

→ 새로운 상품을 등록하면 상품 저장을 하고 상품 상세 페이지로 이동

1. **상품 상세 페이지** → 상품 정보 확인 가능 페이지
2. **상품 수정 페이지** → 상품 상세 페이지 하단 [수정] 버튼 클릭을 통해서 상품 수정 페이지 이동 가능

→ 상품 수정이 완료되면 다시 상품 상세 페이지로 이동

</aside>

---

### 2. 개발 단계 정리

1. 상품 도메인 개발

→ 상품 객체를 만드는 객체 선언

[Item.java](https://www.notion.so/Item-java-1ed782acbb5680448f88dca0661532e7?pvs=21)

1. 레포지토리 생성

→ 데이터를 저장하는 저장소의 역할

→ 아직 데이터베이스 도입이 X

[ItemRepository.java](https://www.notion.so/ItemRepository-java-1ed782acbb5680b39382e262e247b034?pvs=21)

1. 컨틀롤러 세팅

→ 매핑 정보에 대한 기능별 컨트롤러

[BasicItemController.java](https://www.notion.so/BasicItemController-java-1f1782acbb56805ebcbad76d154ada0c?pvs=21)

- 1+2+3 이런 파일 구성은 컴퓨터 메모리에 상품의 정보를 저장하는 구조
- 데이터베이스에 저장되도록 파일 구성을 수정

→ 그러기 위해서 해야 하는 작업은 아래와 같다.

➡️ Item 클래스를 Entity로 변경

- `@Entity`는 JPA에서 해당 클래스가 데이터베이스 테이블과 매핑되는 객체임을 알려주는 어노테이션

➡️ ItemRepository를 JPA interface로 변경

- 기존에는 `ItemRepository`에서 직접 데이터를 `List`에 넣고 꺼내는 방식으로 구현

→ 이건 메모리에만 저장되기 때문에 애플리케이션을 종료하면 데이터가 모두 사라짐

- JPA 기반의 `JpaRepository`를 쓰면, DB에 안전하게 저장되고 영구적으로 보존

➡️ application.properties에 데이터 베이스 관련 연관성 주입

`spring.datasource.url=jdbc:mysql://localhost:8081/test_for_product?useSSL=false&serverTimezone=Asia/Seoulspring.datasource.username=rootspring.datasource.password=0000spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver`

`spring.jpa.hibernate.ddl-auto=updatespring.jpa.show-sql=truespring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect`

- mysql 의 usename : root
- mysql의 password : 0000
- 저장할 스키마 이름 : test_for_product