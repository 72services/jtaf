package ch.jtaf.architecture

import guru.nidi.codeassert.config.AnalyzerConfig
import guru.nidi.codeassert.config.Language
import guru.nidi.codeassert.dependency.*
import guru.nidi.codeassert.junit.CodeAssertMatchers
import org.junit.Test

import guru.nidi.codeassert.junit.CodeAssertMatchers.matchesRulesExactly
import org.hamcrest.MatcherAssert.assertThat

class ArchitectureCheckWithCodeAssertTest {

    @Test
    fun verifyPackageByLayer() {

        val analyzerConfig = AnalyzerConfig.maven(Language.KOTLIN).main()

        // Class name must match package name in upper case
        class ChJtaf : DependencyRuler() {

            var config: DependencyRule? = null
            var boundaryController: DependencyRule? = null
            var boundaryDto: DependencyRule? = null
            var boundarySecurity: DependencyRule? = null
            var boundaryWeb: DependencyRule? = null
            var controlService: DependencyRule? = null
            var controlReportingReport: DependencyRule? = null
            var controlReportingData: DependencyRule? = null
            var controlRepository: DependencyRule? = null
            var entity: DependencyRule? = null

            override fun defineRules() {
                base().mayUse(base().allSubOf())

                config!!.mayUse(controlService)
                config!!.mayUse(controlRepository)
                config!!.mayUse(entity)

                boundaryController!!.mayUse(boundaryWeb)
                boundaryController!!.mayUse(boundaryDto)
                boundaryController!!.mayUse(controlService)
                boundaryController!!.mayUse(controlRepository)
                boundaryController!!.mayUse(entity)

                boundarySecurity!!.mayUse(controlRepository)
                boundarySecurity!!.mayUse(entity)

                boundaryDto!!.mayUse(entity)

                controlService!!.mayUse(controlReportingReport)
                controlService!!.mayUse(controlReportingData)
                controlService!!.mayUse(controlRepository)
                controlService!!.mayUse(entity)

                controlReportingReport!!.mayUse(controlReportingData)
                controlReportingReport!!.mayUse(entity)

                controlReportingData!!.mayUse(entity)

                controlRepository!!.mayUse(entity)
            }
        }

        val rules = DependencyRules.denyAll()
                .withRelativeRules(ChJtaf())
                .withExternals("kotlin.*", "java.*", "javax.*",
                        "org.springframework.*", "org.aspectj.*", "org.jetbrains.*",
                        "org.slf4j.*", "com.itextpdf.*", "com.sendgrid.*")

        val result = DependencyAnalyzer(analyzerConfig).rules(rules).analyze()

        assertThat(result, matchesRulesExactly())
    }

    @Test
    fun checkCycles() {
        val analyzerConfig = AnalyzerConfig.maven(Language.KOTLIN).main()

        assertThat<DependencyResult>(DependencyAnalyzer(analyzerConfig).analyze(), CodeAssertMatchers.hasNoCycles())
    }
}
