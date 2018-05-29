package ch.jtaf.architecture

import guru.nidi.codeassert.config.AnalyzerConfig
import guru.nidi.codeassert.config.Language
import guru.nidi.codeassert.dependency.DependencyAnalyzer
import guru.nidi.codeassert.dependency.DependencyResult
import org.junit.Test

import guru.nidi.codeassert.junit.CodeAssertMatchers.hasNoCycles
import org.hamcrest.MatcherAssert.assertThat

class CyclicDependencyTest {

    @Test
    fun verifyThatThereAreNoCyclicDependencies() {
        val analyzerConfig = AnalyzerConfig.maven(Language.KOTLIN).main()

        assertThat<DependencyResult>(DependencyAnalyzer(analyzerConfig).analyze(), hasNoCycles())
    }
}
