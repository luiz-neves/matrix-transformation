import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.shouldBe
import java.io.File

const val tmpDirPath = "src/test/resources/tmp"

class Test : FreeSpec() {
    override fun beforeSpec(spec: Spec) {
        val tmpDir = File(tmpDirPath)
        if (tmpDir.exists()) {
            tmpDir.deleteRecursively()
        }
        tmpDir.mkdir()

        super.beforeSpec(spec)
    }

    override fun afterSpec(spec: Spec) {
        File(tmpDirPath).deleteRecursively()
        super.afterSpec(spec)
    }

    init {
        "DRAW SHAPE" - {
            "Base case" - {
                testFactory("ex1")
            }

            "Rotation" - {
                testFactory("ex2")
                "Scale" - {
                    testFactory("ex3")
                    "Position" - {
                        testFactory("ex4")
                        testFactory("ex5")
                        testFactory("ex8")
                        testFactory("entrada1")
                    }
                }
            }

            "Direction" - {
                testFactory("entrada2")
                testFactory("ex9")
                testFactory("ex10")
                testFactory("ex11")
            }
        }

        "DRAW SHAPE BASE" - {
            testFactory("ex6")
            testFactory("ex7")
        }
    }
}

suspend fun FreeSpecContainerScope.testFactory(testCase: String) = "Test case $testCase" {
        val inputFilePath = "src/test/resources/casos_de_teste/input/$testCase.txt"
        val actualOutputFilePath = "$tmpDirPath/$testCase.pgm"
        val args = arrayOf(inputFilePath, actualOutputFilePath)

        EP2_esqueleto.main(args)

        val actualOutput = File(actualOutputFilePath).readBytes()
        val expectedOutputFilePath = "src/test/resources/casos_de_teste/output/$testCase.pgm"
        val expectedOutput = File(expectedOutputFilePath).readBytes()

        actualOutput shouldBe expectedOutput
}
