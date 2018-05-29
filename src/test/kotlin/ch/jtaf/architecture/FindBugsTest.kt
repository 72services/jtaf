package ch.jtaf.architecture

import edu.umd.cs.findbugs.Priorities
import guru.nidi.codeassert.config.AnalyzerConfig
import guru.nidi.codeassert.config.In
import guru.nidi.codeassert.config.Language
import guru.nidi.codeassert.dependency.DependencyRules
import guru.nidi.codeassert.findbugs.BugCollector
import guru.nidi.codeassert.findbugs.FindBugsAnalyzer
import guru.nidi.codeassert.findbugs.FindBugsResult
import guru.nidi.codeassert.pmd.PmdRuleset
import org.junit.Test

import guru.nidi.codeassert.junit.CodeAssertMatchers.hasNoBugs
import org.hamcrest.MatcherAssert.assertThat

class FindBugsTest {

    @Test
    fun findBugs() {
        val config = AnalyzerConfig.maven(Language.KOTLIN).main()

        val collector = BugCollector().maxRank(17).minPriority(Priorities.NORMAL_PRIORITY)
                .just(In.everywhere().ignore(
                        "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR",
                        "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE",
                        "BC_BAD_CAST_TO_ABSTRACT_COLLECTION",
                        "REC_CATCH_EXCEPTION"))
                .because("It's checked and OK like this",
                        In.classes(DependencyRules::class.java, PmdRuleset::class.java).ignore("DP_DO_INSIDE_DO_PRIVILEGED"),
                        In.classes("*Test", "Rulesets")
                                .and(In.classes("ClassFileParser").withMethods("doParse"))
                                .ignore("URF_UNREAD_FIELD"))

        val result = FindBugsAnalyzer(config, collector).analyze()
        assertThat(result, hasNoBugs())
    }
}