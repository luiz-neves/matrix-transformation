import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class Test : FreeSpec() {
    init {
        "Test" {
            "a" shouldBe "a"
        }
    }
}