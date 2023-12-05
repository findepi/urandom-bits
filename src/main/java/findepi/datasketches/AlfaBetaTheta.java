/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package findepi.datasketches;

import org.apache.datasketches.Family;
import org.apache.datasketches.Util;
import org.apache.datasketches.theta.CompactSketch;
import org.apache.datasketches.theta.UpdateSketch;

public class AlfaBetaTheta
{
    public static void main(String[] args)
    {
        UpdateSketch updateSketch = UpdateSketch.builder()
                .setFamily(Family.ALPHA) // on-heap
                .setNominalEntries(Util.DEFAULT_NOMINAL_ENTRIES) // 4096; default
                .build();

        for (int i = 0; i < 1_000_000; i++) {
            updateSketch.update(i + "aa" + i);
        }

        System.out.println();
        System.out.println("updateSketch.getEstimate() = " + updateSketch.getEstimate());

        System.out.println();
        System.out.println("updateSketch.getLowerBound(1) = " + updateSketch.getLowerBound(1));
        System.out.println("updateSketch.getUpperBound(1) = " + updateSketch.getUpperBound(1));

        System.out.println();
        System.out.println("updateSketch.getLowerBound(2) = " + updateSketch.getLowerBound(2));
        System.out.println("updateSketch.getUpperBound(2) = " + updateSketch.getUpperBound(2));

        CompactSketch compact = updateSketch.compact();

        System.out.println();
        System.out.println("compact.getEstimate() = " + compact.getEstimate());

        System.out.println();
        System.out.println("compact.getLowerBound(1) = " + compact.getLowerBound(1));
        System.out.println("compact.getUpperBound(1) = " + compact.getUpperBound(1));

        System.out.println();
        System.out.println("compact.getLowerBound(2) = " + compact.getLowerBound(2));
        System.out.println("compact.getUpperBound(2) = " + compact.getUpperBound(2));

        System.out.println();
        System.out.println("compact.getCompactBytes() = " + compact.getCompactBytes());
        System.out.println("compact.toByteArray().length = " + compact.toByteArray().length);
    }
}
