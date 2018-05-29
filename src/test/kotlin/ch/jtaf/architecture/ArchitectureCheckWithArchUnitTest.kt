package ch.jtaf.architecture

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import org.junit.Test


class ArchitectureCheckWithArchUnitTest {

    private val basePackage = "ch.jtaf"

    private val config = "config"

    private val boundary = "boundary"
    private val controller = "controller"
    private val dto = "dto"
    private val security = "security"
    private val web = "web"

    private val control = "control"
    private val reporting = "reporting"
    private val repository = "repository"
    private val service = "service"
    private val data = "data"
    private val report = "report"

    private val entity = "entity"

    private var classes = ClassFileImporter().importPackages(basePackage)

    @Test
    fun checkLayers() {
        layeredArchitecture()
                .layer(boundary).definedBy("..$boundary..", "..$config..")
                .layer(control).definedBy("..$control..")
                .layer(entity).definedBy("..$entity..")

                .whereLayer(boundary).mayNotBeAccessedByAnyLayer()
                .whereLayer(control).mayOnlyBeAccessedByLayers(boundary)
                .whereLayer(entity).mayOnlyBeAccessedByLayers(boundary, control)

                .check(classes)
    }

    @Test
    fun checkCycles() {
        slices().matching("$basePackage.(*)..").should().beFreeOfCycles()

                .check(classes)
    }

    @Test
    fun checkBoundary() {
        noClasses().that().resideInAPackage("..$boundary.$dto..")
                .should().accessClassesThat().resideInAPackage("..$boundary.$controller..")
                .orShould().accessClassesThat().resideInAPackage("..$boundary.$security")
                .orShould().accessClassesThat().resideInAPackage("..$boundary.$web")

                .check(classes)
    }

    @Test
    fun checkControlReportingReport() {
        noClasses().that().resideInAPackage("..$control.$reporting.$report..")
                .should().accessClassesThat().resideInAPackage("..$control.$repository")
                .orShould().accessClassesThat().resideInAPackage("..$control.$service")

                .check(classes)
    }

    @Test
    fun checkControlReportingData() {
        noClasses().that().resideInAPackage("..$control.$reporting.$data..")
                .should().accessClassesThat().resideInAPackage("..$control.$reporting.$report..")
                .orShould().accessClassesThat().resideInAPackage("..$control.$repository")
                .orShould().accessClassesThat().resideInAPackage("..$control.$service")

                .check(classes)
    }

    @Test
    fun checkControlRepository() {
        noClasses().that().resideInAPackage("..$control.$repository..")
                .should().accessClassesThat().resideInAPackage("..$control.$reporting..")
                .orShould().accessClassesThat().resideInAPackage("..$control.$service")

                .check(classes)
    }

}