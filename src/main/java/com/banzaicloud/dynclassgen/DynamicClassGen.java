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

package com.banzaicloud.dynclassgen;



import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


public class DynamicClassGen {
    public static void main(String args[]) throws InterruptedException {

        // number of classes to generate and instantiate
        int genClassCount = Integer.parseInt(System.getenv("DYN_CLASS_COUNT"));

        // memory to consume by each instantiated object
        int memToConsumeMb  = Integer.parseInt(System.getenv("MEM_USAGE_PER_OBJECT_MB"));


        System.out.println("Generating and instantiating " + genClassCount + " classes.");
        System.out.println("Each instantiated class will consume " + memToConsumeMb + " MB of memory.");
        System.out.println("Available CPUs:" + Runtime.getRuntime().availableProcessors());

        List<MemoryConsumer> memoryConsumers = new ArrayList<>();

        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(DynamicClassGen.class));

        try {

            for (int i = 0; i < genClassCount; i++) {
                // generate a new class from DynClassBase
                String className = DynClassBase.class.getCanonicalName();
                CtClass cc = pool.get(className);
                cc.setName(className + i);

                Class genClass = cc.toClass();
                Class<?> []ctorPrms = null;

                Constructor ctor = genClass.getConstructor(ctorPrms);

                MemoryConsumer memConsumer = (MemoryConsumer)ctor.newInstance(null);
                memoryConsumers.add(memConsumer);

                System.out.println("Instantiated class " + memConsumer.getClass().getSimpleName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // gradually increase memory consumption
        for (MemoryConsumer consumer: memoryConsumers) {
            int memToConsumeInBytes = memToConsumeMb * 1024 * 1024;
            consumer.consumeSomeMemory(memToConsumeInBytes);

            System.out.println(consumer.getClass().getSimpleName() + " instance consuming " + memToConsumeMb + "MB");
        }

        while(true){
            Thread.sleep(1000);
        }
    }
}
