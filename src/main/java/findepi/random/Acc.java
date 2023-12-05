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
package findepi.random;

import java.net.ServerSocket;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Nov 20, 2012
 */
public class Acc {
	public static void main(String[] args) throws Exception {
		Thread t = new Thread(new Runnable() {
			public void run() {
				
				try {
					ServerSocket s = new ServerSocket(0);

					System.out.println("accept()");
					s.accept();
					
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		t.setDaemon(true);
		t.start();
		
		Thread.sleep(100);
		
		System.out.println("checking");
		
		System.out.println("state: " + t.getState());
	}
}
