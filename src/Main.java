import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    static int i = 0;

    public static void main(String[] args) {
        // object de type Main
        Main listFiles = new Main();
        // printf
        System.out.println("-------------------------------------------------");
        // printf
        System.out.println("reading files Java8 - Using Files.walk() method");
        // cahrger tout les fichiers dans le dossier dataFiles
        listFiles.listAllFiles(System.getProperty("user.dir") + "\\src\\dataFiles");
    }

    // Uses listFiles method
    public void listAllFiles(File folder) {
        // printf path
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        // printf in method
        System.out.println("In listAllfiles(File) method");
        // charger les sous fichier
        File[] fileNames = folder.listFiles();
        // pour chaque fichier de la list
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
        // printf
        System.out.println("In listAllfiles(String path) method");
        // stream pour chemain du fichier
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
    // lear le contenus ligne par ligne function non utiliser
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
// method pour lire un fichier
    public void readContent(Path filePath) throws IOException {
        // tableaux qui contient les valeurs cibler
        String[] possibleValues = {"(a) Chi^2",
                "(h) Chi^2",
                "(c) Chi^2",
                "(a) The maximum partial sum",
                "(d) d",
                "(b) S_n/n",
                "C0   C1   C2   C3   C4   C5   C6    CHI2",
                "0      1      2      3      4    >=5   Chi^2",
                "(b) V_n_obs (Total # of runs)",
                "(f) Del_1",
                "(g) Del_2",
                "(i) discarded",
                "(d) sum",
                "W[1] W[2] W[3] W[4] W[5] W[6] W[7] W[8]   Chi^2"};
        // printf le nom du fichier a qui on vas chercher les valeurs cibler
        System.out.println("file name " + filePath.getFileName());
        // outbjet pour ecrit dans un fichier
        FileWriter outFile = null;
        // list chainer qui contains tout les lignes du fichier
        List<String> fileList = Files.readAllLines(filePath);
        // pour chqaue valeur rechercher
        for (String possible : possibleValues) {
            i++;
            String name = possible;
            // cas special pour les noms qui contain un chracter special, parsing
            // pour pouvoir ecrire dans le disk le nom em question
            if(possible.equalsIgnoreCase("(b) S_n/n")){
                name="(b) sn";
            }else if(possible.equalsIgnoreCase("0      1      2      3      4    >=5   Chi^2")){
                name="Chi^2";
            }else if(possible.equalsIgnoreCase("W[1] W[2] W[3] W[4] W[5] W[6] W[7] W[8]   Chi^2")){
                name="Chi^2 (2)";
            }
            // lindex de la ligne
            int index=0;
            // creer un nouveau fichier avec le nom de la valeur a chercher. si il exist , ajouter dans le meme fichier
            if(i>14)
            outFile = new FileWriter(name + ".txt", true);
            else
                outFile = new FileWriter(name + ".txt");

            // pour chaque ligne
            for (String x : fileList) {

                // seperaer la ligne dependant des espaces
                String[] splited = x.split("\t");
                // pour chaque valeur entre espace
                for (String a : splited) {
                    // condition si le mot chercher exist dans la ligne
                    // cas special
                    if (a.indexOf(possible) != -1) {
                        if(possible.equalsIgnoreCase("W[1] W[2] W[3] W[4] W[5] W[6] W[7] W[8]   Chi^2")||possible.equalsIgnoreCase("C0   C1   C2   C3   C4   C5   C6    CHI2") || possible.equalsIgnoreCase("0      1      2      3      4    >=5   Chi^2"))
                        {   // prendre la deuxieme ligne a partire de lindex
                            String[] splited1 = fileList.get(index+2).split("\\s");
                            // write to file
                            if(possible.equalsIgnoreCase("W[1] W[2] W[3] W[4] W[5] W[6] W[7] W[8]   Chi^2"))
                                outFile.write(splited1[splited1.length-6] + "\r\n");

                            else
                                outFile.write(splited1[splited1.length-1] + "\r\n");
                            // cas geenral
                        }else {


                            String[] splited1 = a.split("=");
                            // write to file the number only
                            outFile.write(splited1[1] + "\r\n");
                        }
//                    i++;
//                    System.out.println(a);
                    }
                }


        // incrementer le compteur
        index++;
            }
            // fermer le fichier
            outFile.close();

        }
    }
}

