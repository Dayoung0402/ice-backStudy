import lombok.Getter;
import lombok.Setter;

@Getter
@Setter //Data 어노테이션은 쓰지말기 -> DTO를 따로 정의하지 않는 경우는 @Getter, @Setter만ackage hello.itemservice.domain.item;
import lombok.Getter;import lombok.Setter;
@Getter@Setter //Data 어노테이션은 쓰지말기 -> DTO를 따로 정의하지 않는 경우는 @Getter, @Setter만
public class Item {

    private Long id; // 상품 아이디
    private String itemName; // 상품의 이름
    private Integer price; // 상품의 가격
    // int가 아니라 Integer로 형을 맞춘 이유는 값이 들어가지 않았을 때를 대비하여 null을 받지 않기 위함이다.
    private Integer quantity;
    // 이것도 위에 price와 같은 의도로

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
