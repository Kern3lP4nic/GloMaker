import java.lang.String;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.FileVisitResult;

public class Main {

    private static String GREEN = "\033[00m\033[32m";
    private static String BROWN = "\033[00m\033[33m";
    private static String RED = "\033[00m\033[31m";
    private static String CYAN = "\033[00m\033[36m";
    private static String BLUE = "\033[00m\033[34m";
    private static String PURPLE = "\033[00m\033[35m";

    private static String BOLD = "\033[00m\033[01m";
    private static String ITALIC = "\033[00m\033[3m";
    private static String NORMAL = "\033[00m";

    private static String APP_NAME = NORMAL+GREEN+"G"+RED+"L"+PURPLE+"O"+CYAN+"M"+BLUE+"A"+BROWN+"K"+GREEN+"E"+RED+"R"+NORMAL;

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        String currentDir = System.getProperty("user.dir");
        File folder = new File(currentDir);
        ArrayList<File> filesList = new ArrayList<>();
        for (File file : folder.listFiles()) {
            if (!file.getName().startsWith(".")) filesList.add(file);
        }

        System.out.println(BOLD + "\nWelcome to " + APP_NAME + BOLD + ", please read README before continue.\n");
        int firstIndex = -1;
        do {
            System.out.println(BOLD + "Where are your main glossary items file?");
            for (int i = 0; i < filesList.size(); i++) System.out.println(i + ". " + filesList.get(i).getName());
            firstIndex = reader.nextInt();
        } while (firstIndex < 0 || firstIndex > filesList.size() || filesList.get(firstIndex).isDirectory());

		List<String> list = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filesList.get(firstIndex).getPath()))) {
			list = br.lines().collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
            System.exit(0);
		}
        String glossary = new String();
		for (String value : list) glossary += value;
        
        Map<String, String> currentGlossary = new HashMap<String, String>();
        String start = new String();
        for (int i = 0; i < glossary.length(); i++) {
            start += glossary.charAt(i);
            if (start.contains("\\newglossaryentry{")) {
                String name = new String();
                String desc = new String();

                start = new String();
                String end = new String();
                // Looking for name
                main: while (i < glossary.length()) {
                    end += glossary.charAt(i);
                    i++;
                    if (end.contains("name=")) {
                        end = new String();
                        while (i < glossary.length()) {
                            if (glossary.charAt(i) == ',') break main;
                            name += glossary.charAt(i);
                            i++;
                        }
                    }
                }
                // Looking for description
                main: while (i < glossary.length()) {
                    end += glossary.charAt(i);
                    i++;
                    if (end.contains("description={")) {
                        end = new String();
                        while (i < glossary.length()) {
                            if (glossary.charAt(i) == '}') break main;
                            desc += glossary.charAt(i);
                            i++;
                        }
                    }
                }
                // Salvo name e description
                currentGlossary.put(capitalizeFirst(name), desc);
            }  
        }
        System.out.println(BOLD + "\nWords found:" + NORMAL);
        for (String key : currentGlossary.keySet()) {
            System.out.println("Name: " + key + "\nDescription: " + currentGlossary.get(key));
        }

        int secondIndex = -1;
        do {
            System.out.println(BOLD + "\nWhich directory should I look for new items?");
            for (int i = 0; i < filesList.size(); i++) System.out.println(i + ". " + filesList.get(i).getName());
            secondIndex = reader.nextInt();
        } while (secondIndex < 0 || secondIndex > filesList.size() || !filesList.get(secondIndex).isDirectory());

        System.out.println(BOLD + "\nLateX files found:" + NORMAL);
        ArrayList<File> latexFiles = new ArrayList<>();
        try {
            Files.walkFileTree(Paths.get(filesList.get(secondIndex).getName()), new SimpleFileVisitor<Path>() {        
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Objects.requireNonNull(file);
                    if (file.getFileName().toString().endsWith(".tex")) {
                        latexFiles.add(file.toFile());
                        System.out.println(file.getFileName());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        } 
        list = new ArrayList<>();
        for (File file : latexFiles) {
            try (BufferedReader br = Files.newBufferedReader(Paths.get(file.getPath()))) {
                list.addAll(br.lines().collect(Collectors.toList()));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }        
        glossary = new String();
		for (String value : list) glossary += value;
        
        ArrayList<String> filesGlossary = new ArrayList<>();
        start = new String();

        for (int i = 0; i < glossary.length(); i++) {
            start += glossary.charAt(i);
            if (start.contains("\\gl{")) {
                String name = new String();
                start = new String();
                // Looking for name
                while (i < glossary.length()) {
                    i++;
                    if (glossary.charAt(i) == '}') break;
                    name += glossary.charAt(i);                    
                }
                // Salvo name
                filesGlossary.add(capitalizeFirst(name));
            }
        }
        System.out.println(BOLD + "\nWords found:" + NORMAL);
        for (String key : filesGlossary) {
            System.out.println("Name: " + key);
        }

        // Rilevo nuove parole, e le aggiungo all'hashmap glossario
        int count = 0;
        for (String value : filesGlossary) {
            if (!currentGlossary.containsKey(value)) {
                currentGlossary.put(value, "");  
                count++;
            }
        }

        System.out.println(BOLD + "\nI have found " + count + " new words." + NORMAL);
        if (count > 0) {
            System.out.println(BOLD + "\nRewriting all glossary file..." + NORMAL);
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filesList.get(firstIndex).getPath()))) {
                for (Map.Entry<String, String> entry : currentGlossary.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    writer.write("\\newglossaryentry{" + key + "} {\n");
                    writer.write("\tname=" + key + ",\n");
                    writer.write("\tdescription={" + value + "}\n");
                    writer.write("}\n");
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        System.out.println(BOLD + "\nDone!" + NORMAL);

        System.out.println(NORMAL);
    }

    public static String capitalizeFirst(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}

