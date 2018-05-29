package ch.jtaf.architecture

import guru.nidi.codeassert.config.For
import guru.nidi.codeassert.jacoco.CoverageCollector
import guru.nidi.codeassert.jacoco.JacocoAnalyzer
import org.junit.Ignore
import org.junit.Test

import guru.nidi.codeassert.jacoco.CoverageType.*
import guru.nidi.codeassert.jacoco.JacocoResult
import guru.nidi.codeassert.junit.CodeAssertMatchers.hasEnoughCoverage
import org.hamcrest.MatcherAssert.assertThat

class CodeCoverageTest {

    @Ignore
    @Test
    fun coverage() {
        val analyzer = JacocoAnalyzer(CoverageCollector(BRANCH, LINE, METHOD)
                .just(For.global().setMinima(70, 80, 90))
                .just(For.allPackages().setMinima(70, 80, 90))
                .just(For.thePackage("org.proj.entity.*").setNoMinima()))

        assertThat<JacocoResult>(analyzer.analyze(), hasEnoughCoverage())
    }
}
