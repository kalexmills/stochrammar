/**
 * Copyright (c) 2018, NiftySoft LLC.
 *
 * This file is part of Stochrammar.
 *
 * Stochrammar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Stochrammar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Stochrammar.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.kalexmills.stochrammar.examples;

import com.github.kalexmills.stochrammar.CFToken;
import com.github.kalexmills.stochrammar.GroundToken;
import com.github.kalexmills.stochrammar.runner.GroundSequenceRunner;
import com.github.kalexmills.stochrammar.StochasticGrammar;

import java.util.Random;

/**
 * WeightedGrammar provides an example implementation that demonstrates how random weighting can be implemented by
 * client code. The grammar implemented here follows the below weighted BNF grammar, in which the RHS of each production
 * rule is labeled with its probability of occurrence.
 *
 * ROOT := AB(0.2) | A(0.2) | ROOT(0.6)
 * A := "a"(0.5) | "A"(0.5)
 * B := "b"(0.5) | "B"(0.5)
 */
public class WeightedGrammar implements StochasticGrammar<String> {

    public WeightedGrammar() {

    }

    @Override
    public CFToken<String> generateRootToken() {
        return new RootToken();
    }

    @Override
    public String blankEntity() {
        return new String();
    }

    private static class RootToken extends CFToken<String> {
        @Override
        public CFToken<String>[] replace(Random rand) {
            float sample = rand.nextFloat();
            if (sample < 0.2f) return new CFToken[] { new A(), new B() };
            if (sample < 0.4f) return new CFToken[] { new A() };
                               return new CFToken[] { new RootToken() };
        }
    }

    private static class A extends CFToken<String> {
        public CFToken<String>[] replace(Random rand) {
            float sample = rand.nextFloat();
            if (sample < 0.5f) return new CFToken[] { new Base("a")};
                               return new CFToken[] { new Base("A")};
        }
    }

    private static class B extends CFToken<String> {
        public CFToken<String>[] replace(Random rand) {
            float sample = rand.nextFloat();
            if (sample < 0.5f) return new CFToken[] { new Base("b")};
                               return new CFToken[] { new Base("B")};
        }
    }

    private static class Base extends GroundToken<String> {
        private String token;
        public Base(String token) {
            this.token = token;
            this.setAction((str) -> str.concat(token));
        }
    }

    public static void main(String[] args) {
        GroundSequenceRunner<String> runner = new GroundSequenceRunner<>(new WeightedGrammar());

        int n = 100;
        for (int i = 0; i < n; ++i) {
            System.out.println(runner.run());
        }
    }
}