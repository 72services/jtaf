// Press F1 to get detailled help about Sonargraph's architecture DSL.
//
// To enable architecture checking on this file please right click on it in
// the architecture view or files view and select 'Add to Architecture Check'.
//
// The allowed dependencies (connect statements) are based on your workspace
// dependencies which can be visualized by opening the workspace dependendencies
// view. If you get "Missing workspace dependcy" warnings in the issues view you
// will also get architecture violations. To remedy this problem you have to add
// the allowed connections to the artifacts causing the violations. The best way
// to inspect architecture violations is to use the architecture view and select the
// architecture file of interest. You can also add the missing workspace dependencies
// in the workspace dependecies view and then re-generate this architecture.
//
// You can now enhance this initial architecture by fleshing out the internal structure of
// your modules. Please make sure to read our documentation about the architecture DSL (F1) so
// that you get a better idea how you can create a concise and elegant architectural blueprint
// for your application.
artifact jtaf2
{
    include "jtaf2/**"
    
    artifact Config
    {
        include "**/config/**"
        connect to Control, Entity, External.Java, External.Kotlin, External.SpringFramework, External.Jetbrains
    }
    
    artifact Boundary
    {
        include "**/boundary/**"
        include "**/Jtaf2Application*"
        connect to Control, Entity, External.Java, External.Kotlin, External.SpringFramework, External.Jetbrains, External.Slf4j, External.AspectJ
    }
    
    artifact Control
    {
        include "**/control/**"
        connect to Entity, External.IText, External.Java, External.Kotlin, External.SpringFramework, External.Jetbrains, External.Slf4j
    }
    
    artifact Entity
    {
        include "**/entity/**"
        connect to External.Java, External.Kotlin, External.Jetbrains
    }
}

artifact External
{
    include "External [Java]/**"
    
    exposed public artifact IText
    {
        include "**/itextpdf/**"
    }
    
    exposed public artifact Java
    {
        include "**/java/**"
        include "**/javax/**"
    }
    
    exposed public artifact Kotlin
    {
        include "**/kotlin/**"
    }
    
    exposed public artifact SpringFramework
    {
        include "**/springframework/**"
    }
    
    exposed public artifact AspectJ
    {
        include "**/aspectj/**"
    }
    
    exposed public artifact Slf4j
    {
        include "**/slf4j/**"
    }
    
    exposed public artifact Jetbrains
    {
        include "**/jetbrains/**"
    }
}
