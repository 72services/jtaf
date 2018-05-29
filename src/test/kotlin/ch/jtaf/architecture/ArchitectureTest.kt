package ch.jtaf.architecture

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.junit.ArchUnitRunner
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition
import org.junit.runner.RunWith

@RunWith(ArchUnitRunner::class)
@AnalyzeClasses(packages = [basePackage])
class ArchitectureTest {

    companion object {
        @ArchTest
        val cycles: ArchRule = SlicesRuleDefinition.slices().matching("$basePackage.(*)..").should().beFreeOfCycles()
    }

}

const val basePackage = "ch.jtaf"