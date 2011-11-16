package net.codjo.util.file;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.text.DecimalFormat;
/**
 * Ensemble de méthodes utilitaires pour les fichiers.
 */
public class FileUtil {

    private FileUtil() {
    }


    /**
     * Place le fichier <code>file</code> dans le repertoire <code>dir</code> avec un versionning.
     *
     * @param dir  Le repertoire de destination du fichier a deplacer
     * @param file Le chemin complet du fichier a deplacer
     *
     * @return Le fichier destination
     *
     * @throws IOException Impossible de deplacer le fichier
     */
    public static File moveFileTo(String dir, File file) throws IOException {
        return moveFileTo(dir, file, true);
    }


    /**
     * Place le fichier <code>file</code> dans le repertoire <code>dir</code>
     *
     * @param dir         Le repertoire de destination du fichier a deplacer
     * @param file        Le chemin complet du fichier a deplacer
     * @param versionning si on versionne le fichier
     *
     * @return Le fichier destination
     *
     * @throws IOException Impossible de deplacer le fichier
     */
    public static File moveFileTo(String dir, File file, boolean versionning) throws IOException {
        File newFile;
        File newDir = new File(dir);

        if (!newDir.exists()) {
            throw new IOException("Le repertoire destination : " + newDir.toString()
                                  + " n'existe pas.");
        }

        if (file.exists()) {
            if (versionning) {
                newFile = fileVersionManager(new File(dir, file.getName()));
            }
            else {
                newFile = new File(dir, file.getName());
                newFile.delete();
            }

            copyFile(file, newFile);
            boolean deleteSucceeded = file.delete();
            if (!deleteSucceeded) {
                newFile.delete();
                throw new IOException("Impossible de deplacer le fichier >"
                                      + file.toString() + "< même si la copie vers >" + newFile.toString()
                                      + "< à fonctionner.");
            }
        }
        else {
            throw new IOException("Le fichier : " + file.toString() + " n'existe pas.");
        }
        return newFile;
    }


    /**
     * Copie de fichier en mode binaire.
     *
     * @param sourceFile le fichier source
     * @param destFile   le fichier destination
     *
     * @throws IOException Erreur durant la copie
     */
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        checkBeforeCopy(sourceFile, destFile);

        BufferedInputStream source =
              new BufferedInputStream(new FileInputStream(sourceFile));
        BufferedOutputStream destination = null;
        try {
            destination = new BufferedOutputStream(new FileOutputStream(destFile));

            int bufferSize =
                  (source.available() < 1000000) ? source.available() : 1000000;
            byte[] buf = new byte[bufferSize];

            int bytesRead;
            while (source.available() != 0) {
                bytesRead = source.read(buf);

                destination.write(buf, 0, bytesRead);
            }
            destination.flush();
        }
        finally {
            closeStream(source);
            closeStream(destination);
        }
    }


    public static void deleteRecursively(File toDelete) {
        if (toDelete.isDirectory()) {
            File[] files = toDelete.listFiles();
            for (File file : files) {
                deleteRecursively(file);
            }
        }
        toDelete.delete();
    }


    public static String loadContent(Reader reader) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        char[] buffer = new char[10000];
        int charRead = reader.read(buffer);
        while (charRead != -1) {
            fileContent.append(buffer, 0, charRead);
            charRead = reader.read(buffer);
        }
        return fileContent.toString();
    }


    public static String loadContent(URL url) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        try {
            return loadContent(reader);
        }
        finally {
            reader.close();
        }
    }


    public static String loadContent(URL url, String charset) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(url.openStream(), charset);
        try {
            return loadContent(streamReader);
        }
        finally {
            streamReader.close();
        }
    }


    public static String loadContent(File file) throws IOException {
        Reader reader = new BufferedReader(new FileReader(file));
        try {
            return loadContent(reader);
        }
        finally {
            reader.close();
        }
    }


    public static void saveContent(File file, String fileContent) throws IOException {
        Writer writer = new BufferedWriter(new FileWriter(file));
        try {
            writer.write(fileContent);
        }
        finally {
            writer.close();
        }
    }


    public static void saveContent(File file, String fileContent, String charset) throws IOException {
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), charset);
        try {
            writer.write(fileContent);
        }
        finally {
            writer.close();
        }
    }


    public static class FileCopyException extends IOException {
        public FileCopyException(String msg) {
            super(msg);
        }
    }


    protected static int getExtensionIndex(File file) {
        int idx = file.getName().lastIndexOf(".");
        if (idx != -1) {
            idx = file.toString().lastIndexOf(".");
        }
        return idx;
    }


    private static File fileVersionManager(File file) {
        if (!file.exists()) {
            return file;
        }
        String newFile = file.toString();
        int idx = 1;
        DecimalFormat decimalFormat = new DecimalFormat("000");

        int extensionIndex = getExtensionIndex(file);
        if (extensionIndex == -1) {
            //fichier sans extension.
            while (new File(newFile + decimalFormat.format(idx)).exists()) {
                idx++;
            }
            return new File(newFile + decimalFormat.format(idx));
        }
        else {
            // fichier avec extension.
            while (new File(newFile.substring(0, extensionIndex) + decimalFormat.format(idx)
                            + newFile.substring(extensionIndex)).exists()) {
                idx++;
            }
            return new File(newFile.substring(0, extensionIndex) + decimalFormat.format(idx)
                            + newFile.substring(extensionIndex));
        }
    }


    private static void checkBeforeCopy(File sourceFile, File destinationFile) throws IOException {
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            throw new FileCopyException("Fichier source inexistant: " + sourceFile);
        }
        if (!sourceFile.canRead()) {
            throw new FileCopyException("fichier source illisible: " + sourceFile);
        }

        destinationFile.delete();
        if (destinationFile.exists()) {
            throw new FileCopyException("Impossible d'effacer le fichier de destination "
                                        + destinationFile);
        }
        else {
            File parentdir = determineParent(destinationFile);
            if (!parentdir.exists()) {
                throw new FileCopyException("Dossier destination inexistant: "
                                            + parentdir);
            }
            if (!parentdir.canWrite()) {
                throw new FileCopyException("Dossier destination: " + destinationFile
                                            + " en lecture seule.");
            }
        }
    }


    private static File determineParent(File file) {
        String dirname = file.getParent();
        if (dirname == null) {
            if (file.isAbsolute()) {
                return new File(File.separator);
            }
            else {
                return new File(System.getProperty("user.dir"));
            }
        }
        return new File(dirname);
    }


    private static void closeStream(final InputStream source) {
        if (source != null) {
            try {
                source.close();
            }
            catch (IOException ex) {
                // Normalement ça passe
            }
        }
    }


    private static void closeStream(final OutputStream source) {
        if (source != null) {
            try {
                source.close();
            }
            catch (IOException ex) {
                // Normalement ça passe
            }
        }
    }
}
