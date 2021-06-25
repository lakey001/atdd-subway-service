package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class DiscountStrategyTest {

    @DisplayName("일반 고객은 요금 할인이 없음")
    @ValueSource(ints = { 1000, 2000, 3000, 5000 })
    @ParameterizedTest
    void noDiscountStrategyTest(int fare) {
        DiscountStrategy discountStrategy = new NoDiscountStrategy();
        assertThat(discountStrategy.discount(fare)).isEqualTo(fare);
    }

    @DisplayName("어린이 고객의 할인 정책은 (요금 - 350) / 2")
    @CsvSource(value = { "1350,500" , "2350,1000", "5350,2500" })
    @ParameterizedTest
    void childDiscountStrategyTest(int fare, int expect) {
        DiscountStrategy discountStrategy = new ChildDiscountStrategy();
        assertThat(discountStrategy.discount(fare)).isEqualTo(expect);
    }

    @DisplayName("청소년 고객의 할인 정책은 (요금 - 350) / 5 * 4")
    @CsvSource(value = { "1350,800" , "2350,1600", "5350,4000" })
    @ParameterizedTest
    void teenagerDiscountStrategyTest(int fare, int expect) {
        DiscountStrategy discountStrategy = new TeenagerDiscountStrategy();
        assertThat(discountStrategy.discount(fare)).isEqualTo(expect);
    }

    @DisplayName("할인된 최종 요금은 0원 이상의 정수여야 한다. 즉, 요금은 최소 350원 이상이어야 할인받을 수 있다.")
    @ValueSource(ints = { 0, 10, 50, 100, 200, 300, 340, 349 })
    @ParameterizedTest
    void minimumFareTest(int fare) {
        DiscountStrategy discountStrategy1 = new ChildDiscountStrategy();
        assertThatIllegalArgumentException().isThrownBy(() -> discountStrategy1.discount(fare));

        DiscountStrategy discountStrategy2 = new TeenagerDiscountStrategy();
        assertThatIllegalArgumentException().isThrownBy(() -> discountStrategy2.discount(fare));
    }
}
