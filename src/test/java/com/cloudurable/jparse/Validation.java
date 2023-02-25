package com.cloudurable.jparse;

import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.source.Sources;

import java.io.File;

public class Validation {

    public static void main(String... args) {
        try {
            final File file = new File("./src/test/resources/validation/");


            int[] result1 = validate(file, "y_", true, false);
            int[] result2 = validate(file, "i_", true, false);
            int[] result3 = validate(file, "n_", false, true);

            System.out.printf("Passed Mandatory %d Failed %d \n", result1[0], result1[1]);
            System.out.printf("Passed Optional %d Failed %d \n", result2[0], result2[1]);
            System.out.printf("Passed Garbage %d Failed %d \n", result3[0], result3[1]);

        }catch ( Exception ex) {
            ex.printStackTrace();
        }
    }

    private static int [] validate(File file, String match, boolean showFail, boolean showPass) {
        int pass = 0;
        int error = 0;
        for (File listFile : file.listFiles()) {
            if (listFile.getName().startsWith(match)) {

                final CharSource cs = Sources.fileSource(listFile);
                final var jsonString = cs.toString();

                //System.out.println("TESTING " + listFile);
                try {

                    RootNode root = Json.toRootNode(jsonString);
                    if (showPass) {
                        System.out.println("PASS! " + listFile);
                        System.out.println(cs);
                        System.out.println();
                    }
                    pass++;
                }catch (Exception ex) {
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
    }
}
