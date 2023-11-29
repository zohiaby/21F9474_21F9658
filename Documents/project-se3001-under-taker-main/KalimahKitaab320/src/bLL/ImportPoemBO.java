//package bLL;
//
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
////import dl.DatabaseManager;
//
//
//public class ImportPoemBO implements IimportPoem {
//	@Override
//	// Returning the boolean if data added successfully
//    public boolean parsePoem(String filePath) {
//		BLLFascade fascade = new BLLFascade();// adding facade object
//        boolean ignoreLines = false;
//        String poemTitle = null, misra1 = null, misra2 = null, line;
//
//        // extracting the file data
//        try (BufferedReader breader = new BufferedReader(
//                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
//
//            while ((line = breader.readLine()) != null) { // checking the file parameters to start reading
//                if (line.contains("=========")) {
//                    ignoreLines = false;
//                    continue;
//                }
//
//                if (ignoreLines) {
//                    continue;
//                }
//
//                if (line.startsWith("[")) { // extracting the title of the poem
//                    Pattern titlePattern = Pattern.compile("\\[(.*?)\\]");
//                    Matcher titleMatcher = titlePattern.matcher(line);
//                    if (titleMatcher.find()) {
//                        poemTitle = titleMatcher.group(1);
//                    }
//                } else if (line.startsWith("(")) { // extracting the verses of poem
//                    Pattern versePattern = Pattern.compile("\\((.*?)\\)");
//                    Matcher verseMatcher = versePattern.matcher(line);
//                    if (verseMatcher.find()) {
//                        String verseText = verseMatcher.group(1);
//                        String[] misras = verseText.split("\\.\\.\\.");
//                        if (misras.length >= 1) {
//                            misra1 = misras[0].trim();
//                        }
//                        if (misras.length >= 2) {
//                            misra2 = misras[1].trim();
//                        }
//                        System.out.println("Poem Title:     " + poemTitle + "\n" + "Misra 1:     " + misra1 + "\nMisra 2:     " + misra2 + "\n");
//                        fascade.storePoem(poemTitle, misra1, misra2); // sending the extracted code to the database
//                    }
//                } else if (line.contains("_________")) { // ending of file 
//                    ignoreLines = true;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//}
package bLL;


