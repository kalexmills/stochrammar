# stochrammar
Stochrammar is an abstract Stochastic grammar library implemented in Java.

At the moment, the library is not flexible enough to be used without subclassing `GrammarToken` `GroundToken` and
`StochasticGrammar`, but if you do that properly, then the classes in `com.nifty.stochrammar.runner` should all work.
If you find that they do not, open an issue (this software is young, yet).

See `WeightedGrammar.java` for an example implementation.
