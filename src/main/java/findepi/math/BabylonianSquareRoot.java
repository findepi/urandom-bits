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
package findepi.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author findepi
 * @since Apr 9, 2016
 */
public class BabylonianSquareRoot {

	public static void main(String[] args) {
		System.out.println(sqrt(BigDecimal.valueOf(9)));
		System.out.println(sqrt(BigDecimal.valueOf(2)));
	}

	private static final BigDecimal TWO = BigDecimal.valueOf(2);
	private static final int SQRT_CALC_SCALE = 8;
	private static final BigDecimal SQRT_ERROR = new BigDecimal("0.000001");

	/*
	 * Copied from the Java Performance Tuning book (which probably had it from somewhere?) and modified (fixed var
	 * naming, fixed .setScale() with ignored return value, added comment so that i understand).
	 */
	public static BigDecimal sqrt(final BigDecimal bd) {
		// sqrt candidate.
		BigDecimal sqrt = bd;
		BigDecimal diff;
		do {
			BigDecimal divided = bd.divide(sqrt, SQRT_CALC_SCALE, RoundingMode.FLOOR);
			/*
			 * If sqrt was really sqrt(initial), then divided would equal sqrt. If they're not equal, let's try their
			 * average as next sqrt candidate.
			 */
			BigDecimal average = divided.add(sqrt).divide(TWO, SQRT_CALC_SCALE, RoundingMode.FLOOR);
			/*
			 * See how close sqrt is to average of sqrt and divided for the purpose of the stop condition.
			 */
			diff = average.subtract(sqrt).abs().setScale(8, RoundingMode.FLOOR);
			sqrt = average;
		} while (diff.compareTo(SQRT_ERROR) > 0);

		return sqrt;
	}
}
