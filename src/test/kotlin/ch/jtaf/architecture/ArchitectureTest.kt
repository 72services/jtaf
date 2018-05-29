package ch.jtaf.architecture

import guru.nidi.codeassert.config.AnalyzerConfig
import guru.nidi.codeassert.dependency.*
import org.junit.Test

import guru.nidi.codeassert.junit.CodeAssertMatchers.matchesRulesExactly
import org.hamcrest.MatcherAssert.assertThat

class ArchitectureTest {


    @Test
    fun verifyPackageByLayer() {

        val analyzerConfig = AnalyzerConfig.maven().main()

        // Class name must match package name in upper case
        class ChMartinelli : DependencyRuler() {

            var boundary: DependencyRule? = null
            var controlService: DependencyRule? = null
            var controlRepository: DependencyRule? = null
            var entity: DependencyRule? = null

            override fun defineRules() {
                base().mayUse(base().allSubOf())
                boundary!!.mayUse(controlService)
                boundary!!.mayUse(entity)
                controlService!!.mayUse(controlRepository)
                controlService!!.mayUse(entity)
                controlRepository!!.mayUse(entity)
            }
        }

        val rules = DependencyRules.denyAll()
                .withRelativeRules(ChMartinelli())
                .withExternals("java.*", "javax.*", "org.springframework.*")

        val result = DependencyAnalyzer(analyzerConfig).rules(rules).analyze()

        assertThat(result, matchesRulesExactly())
    }

}
