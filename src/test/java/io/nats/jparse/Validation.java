/*
 * Copyright 2013-2023 Richard M. Hightower
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.nats.jparse;

import io.nats.jparse.node.RootNode;
import io.nats.jparse.parser.JsonParser;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.Sources;

import java.io.File;

public class Validation {

    public static void main(String... args) {
        try {
            final File file = new File("./src/test/resources/validation/");


            System.out.println("Event Strict Parser");
            final boolean showPass = false;
            final boolean showFail = false;
            validateParser(file, (JsonParser) Json.builder().setStrict(true).buildEventParser(), showFail, showPass);

            System.out.println("Strict Parser");
            final JsonParser jsonParser = Json.builder().setStrict(true).build();
            validateParser(file, jsonParser, showFail, showPass);

        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private static void validateParser(File file, JsonParser jsonParser,
                                       boolean showFail, boolean showPass) {
        int[] result1 = validate(file, "y_", showFail, showPass, jsonParser);
        int[] result2 = validate(file, "i_", showFail, showPass, jsonParser);
        int[] result3 = validate(file, "n_", showFail, showPass, jsonParser);

        System.out.printf("Passed Mandatory %d Failed %d \n", result1[0], result1[1]);
        System.out.printf("Passed Optional %d Failed %d \n", result2[0], result2[1]);
        System.out.printf("Passed Garbage %d Failed %d \n", result3[0], result3[1]);
    }

    private static int[] validate(File file, String match, boolean showFail, boolean showPass,
                                  JsonParser jsonParser) {


        try {
            int pass = 0;
            int error = 0;
            for (File listFile : file.listFiles()) {
                if (listFile.getName().startsWith(match)) {

                    final CharSource cs = Sources.fileSource(listFile);
                    final String jsonString = cs.toString();

                    //System.out.println("TESTING " + listFile);
                    try {

                        RootNode root = jsonParser.parse(jsonString);
                        if (showPass) {
                            System.out.println("PASS! " + listFile);
                            System.out.println(cs);
                            System.out.println();
                        }
                        pass++;
                    } catch (Exception ex) {
                        //ex.printStackTrace();
                        if (showFail) {
                            System.out.println("FAIL! " + listFile);
                            System.out.println(cs);
                            System.out.println();
                        }
                        error++;
                    }
                }
            }

            return new int[]{pass, error};
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return new int[]{-1, -1};
    }
}
