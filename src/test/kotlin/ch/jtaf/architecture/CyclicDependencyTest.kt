package ch.jtaf.architecture

import guru.nidi.codeassert.config.AnalyzerConfig
import guru.nidi.codeassert.dependency.DependencyAnalyzer
import org.junit.Test

import guru.nidi.codeassert.junit.CodeAssertMatchers.hasNoCycles
import org.hamcrest.MatcherAssert.assertThat

class CyclicDependencyTest {

    @Test
    fun verifyThatThereAreNoCyclicDependencies() {
        val analyzerConfig = AnalyzerConfig.maven().main()

        assertThat<DependencyResult>(DependencyAnalyzer(analyzerConfig).analyze(), hasNoCycles())
    }
}
