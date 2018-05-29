package ch.jtaf.architecture

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import org.junit.Test

class ArchitectureCheckWithArchUnitTest {

    val basePackage = "ch.jtaf"
    val boundary = "boundary"
    val control = "control"
    val entity = "entity"

    @Test
    fun checkLayers() {
        layeredArchitecture()
                .layer(boundary).definedBy("..$boundary..")
                .layer(control).definedBy("..$control..")
                .layer(entity).definedBy("..$entity..")

                .whereLayer(boundary).mayNotBeAccessedByAnyLayer()
                .whereLayer(control).mayOnlyBeAccessedByLayers(boundary)
                .whereLayer(entity).mayOnlyBeAccessedByLayers(boundary, control)
    }

    @Test
    fun checkCycles() {
        slices().matching("$basePackage.(*)..").should().beFreeOfCycles()
    }
}