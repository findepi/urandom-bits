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
package findepi.test.java.util.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

import static java.lang.String.format;

//import static io.airlift.concurrent.Threads.daemonThreadsNamed;

public class ReadWriteLockStarvation
{
    public static void main(String[] args)
            throws Exception
    {
        int readers = 20;
        ExecutorService e = Executors.newFixedThreadPool(readers * 2 + 10/*, daemonThreadsNamed("rwlock")*/);

        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();

        CountDownLatch underLock = new CountDownLatch(readers);

        e.submit(() -> {
            while (true) {
                System.out.printf("                readers inside: %s\n", rwLock.getReadLockCount());
                Thread.sleep(200 / readers);
            }
        });

        IntStream.range(0, readers)
                .forEach(readerNumber -> {

                    e.submit(() -> {
                        String s = format("reader#%s ", readerNumber);
                        Thread.sleep((int) (1000. / readers * readerNumber));
                        while (true) {
                            System.out.println(s + "locking");
                            readLock.lock();
                            try {
                                System.out.println(s + "locked");
                                underLock.countDown();
                                Thread.sleep(200);
                            }
                            finally {
                                readLock.unlock();
                                System.out.println(s + "unlocked");
                            }
                        }
                    });
                });

        underLock.await();
        Thread.sleep(530);
        System.out.println("C locking write ...");
        rwLock.writeLock().lock();
        System.out.println("C locked, exiting");
    }
}
