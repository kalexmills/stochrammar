# stochrammar
Stochrammar is an abstract Stochastic grammar library implemented in Java. If you are willing to bring your own code, you can use it to procedurally generate "anything" without having to worry (much) about the correctness of the generation algorithm as part of the process. At the moment, stochrammar can only be used to generate entities from [context-free grammars](https://en.wikipedia.org/wiki/Context-free_grammar) (CFGs), but work on [context-sensitive grammars](https://en.wikipedia.org/wiki/Context-sensitive_grammar) (CSGs) and [semi-thue systems](https://en.wikipedia.org/wiki/Semi-Thue_system) are also on the roadmap.

Since grammars involving strings are easy to understand, design, implement, and reason about, this library separates the procedural generation problem into two phases. In the *token generation phase*, a collection of `GroundToken`s are generated by applying production rules to replace `GrammarToken`s. Next, in the *entity construction phase*, the tokens act on a blank entity in an order determined by a `GrammarRunner`.

`GrammarToken`s are responsible for defining the means by which they are replaced with other `GrammarToken`s, and the means by which they act on entities.

At the moment, the library is not flexible enough to be used without subclassing `GrammarToken` `GroundToken` and
`StochasticGrammar`, but if you do that properly, then the classes in `com.nifty.stochrammar.runner` should all work.
If you find that they do not, open an issue (this software is still young).

See `WeightedGrammar.java` for an example implementation.

### Brief History
An earlier, proprietary version of this library was used in the development of [Tweeter in Chief](https://play.google.com/store/apps/details?id=com.niftysoft.tweeter). The copyright holder released an updated version of the library under the AGPL on November 3rd, 2018.
