package ch.jtaf.architecture

import com.puppycrawl.tools.checkstyle.api.SeverityLevel
import guru.nidi.codeassert.checkstyle.CheckstyleAnalyzer
import guru.nidi.codeassert.checkstyle.CheckstyleResult
import guru.nidi.codeassert.checkstyle.StyleChecks
import guru.nidi.codeassert.checkstyle.StyleEventCollector
import guru.nidi.codeassert.config.AnalyzerConfig
import guru.nidi.codeassert.config.In
import guru.nidi.codeassert.config.Language
import org.junit.Test

import guru.nidi.codeassert.junit.CodeAssertMatchers.hasNoCheckstyleIssues
import org.hamcrest.MatcherAssert.assertThat

class CheckstyleTest {

    @Test
    fun checkstyle() {
        val config = AnalyzerConfig.maven(Language.KOTLIN).main()

        val collector = StyleEventCollector().severity(SeverityLevel.WARNING)
                .just(In.everywhere().ignore("import.avoidStar", "javadoc.missing"))
                .because("in tests, long lines are ok", In.classes("*Test").ignore("maxLineLen"))

        val checks = StyleChecks.google().maxLineLen(120).indentBasic(4)

        val result = CheckstyleAnalyzer(config, checks, collector).analyze()
        assertThat(result, hasNoCheckstyleIssues())
    }
}