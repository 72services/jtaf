package ch.jtaf.architecture

import guru.nidi.codeassert.config.AnalyzerConfig
import guru.nidi.codeassert.config.In
import guru.nidi.codeassert.config.Language
import guru.nidi.codeassert.dependency.Dependencies
import guru.nidi.codeassert.dependency.DependencyRule
import net.sourceforge.pmd.RulePriority
import org.junit.Test

import guru.nidi.codeassert.junit.CodeAssertMatchers.hasNoCodeDuplications
import guru.nidi.codeassert.junit.CodeAssertMatchers.hasNoPmdViolations
import guru.nidi.codeassert.pmd.*
import guru.nidi.codeassert.pmd.PmdRulesets.*
import org.hamcrest.MatcherAssert.assertThat

class PmdTest {

    private val config = AnalyzerConfig.maven(Language.KOTLIN).main()

    @Test
    fun pmd() {
        val collector = PmdViolationCollector().minPriority(RulePriority.MEDIUM)
                .because("It's not severe and occurs very often",
                        In.everywhere().ignore("MethodArgumentCouldBeFinal"),
                        In.locs("JavaClassBuilder#from", "FindBugsMatchers").ignore("AvoidInstantiatingObjectsInLoops"))
                .because("it'a an enum",
                        In.classes("SignatureParser").ignore("SwitchStmtsShouldHaveDefault"))
                .just(In.classes("*Test").ignore("TooManyStaticImports"))

        val analyzer = PmdAnalyzer(config, collector).withRulesets(
                basic(), braces(), design(), empty(), optimizations(),
                codesize().excessiveMethodLength(40).tooManyMethods(30))

        assertThat<PmdResult>(analyzer.analyze(), hasNoPmdViolations())
    }

    @Test
    fun cpd() {
        val collector = CpdMatchCollector()
                .because("equals",
                        In.everywhere().ignore("public boolean equals(Object o) {"))
                .just(
                        In.classes(DependencyRule::class.java, Dependencies::class.java).ignoreAll(),
                        In.classes("SignatureParser").ignoreAll())

        val analyzer = CpdAnalyzer(config, 20, collector)

        assertThat<CpdResult>(analyzer.analyze(), hasNoCodeDuplications())
    }
}