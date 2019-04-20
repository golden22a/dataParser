import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    static int i = 0;

    public static void main(String[] args) {
        Main listFiles = new Main();
        System.out.println("-------------------------------------------------");
        System.out.println("reading files Java8 - Using Files.walk() method");
        listFiles.listAllFiles(System.getProperty("user.dir") + "\\src\\dataFiles");
    }

    // Uses listFiles method
    public void listAllFiles(File folder) {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        System.out.println("In listAllfiles(File) method");
        File[] fileNames = folder.listFiles();
        for (File file : fileNames) {
            // if directory call the same method again
            if (file.isDirectory()) {
                listAllFiles(file);
            } else {
                try {
                    readContent(file);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    // Uses Files.walk method
    public void listAllFiles(String path) {
        System.out.println("In listAllfiles(String path) method");
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    try {
                        readContent(filePath);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void readContent(File file) throws IOException {
        System.out.println("read file " + file.getCanonicalPath());
        System.out.println("Here");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String strLine;
            // Read lines from the file, returns null when end of stream
            // is reached
            while ((strLine = br.readLine()) != null) {
                System.out.println("Line is - " + strLine);
            }
        }
    }

    public void readContent(Path filePath) throws IOException {
        String[] possibleValues = {"(a) Chi^2", "(c) Chi^2", "(a) The maximum partial sum", "(d) d"};
        System.out.println("file name " + filePath.getFileName());
        FileWriter outFile = null;
        List<String> fileList = Files.readAllLines(filePath);
        for(String possible : possibleValues) {
            int j=0;
            outFile = new FileWriter(possible+".txt",true);
            for (String x : fileList) {
                String[] splited = x.split("\t");
                for (String a : splited) {
                    if (a.indexOf(possible) != -1) {
                        j++;
                        String[] splited1 = a.split("=");
                        outFile.write(splited1[1] + "\r\n");
//                    i++;
//                    System.out.println(a);
                    }
                }

//        System.out.println("" + fileList);
            }
            System.out.println("this is the data to check in each line  "+possible+" and  number of it  : "+j);
            outFile.close();
        }


    }
}

