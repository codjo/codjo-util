package net.codjo.util.system;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import net.codjo.util.file.FileUtil;
/**
 * Execute une commande. Une instance de <code>WindowsExec</code> peut être réutilisé pour executer plusieurs commande
 * de manière sequentiel.
 */
public class WindowsExec {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private String errorMessage;
    private String processMessage;
    private String processInput = null;


    /**
     * Execute de manière synchrone la commande <code>cmd</code>, en recuperant les sorties du process (ie. stdout et
     * stderr).
     *
     * @param cmd une commande
     *
     * @return Le code retour du process lancé pour la commande.
     */
    public int exec(String cmd) {
        return exec(cmd, null, -1);
    }


    /**
     * Execute de manière synchrone la commande <code>cmd</code>, en recuperant les sorties du process (ie. stdout et
     * stderr).
     *
     * @param cmd              une commande
     * @param workingDirectory Le repertoire de travail de la commande
     *
     * @return Le code retour du process lancé pour la commande.
     */
    public int exec(String cmd, File workingDirectory) {
        return exec(cmd, workingDirectory, -1);
    }


    /**
     * Execute de manière synchrone la commande <code>cmd</code>, en recuperant les sorties du process (ie. stdout et
     * stderr), dans le repertoire de travail spécifié.
     *
     * @param cmd              une commande
     * @param workingDirectory Le repertoire de travail de la commande
     * @param timeout          Timeout d'execution (si a la fin du timeout, le process tourne encore on lui fait la po)
     *
     * @return Le code retour du process lancé pour la commande.
     */
    public int exec(String cmd, File workingDirectory, int timeout) {
        Executor executor = new Executor(cmd, workingDirectory);
        if (cmd == null) {
            return buildError("Aucune commande specifiée");
        }
        return exec(executor, timeout);
    }


    public int exec(String[] commands) {
        return exec(commands, null, -1);
    }


    public int exec(String[] commands, File workingDirectory) {
        return exec(commands, workingDirectory, -1);
    }


    public int exec(String[] commands, File workingDirectory, int timeout) {
        if (commands == null) {
            return buildError("Aucune commande specifiée");
        }
        Executor executor = new Executor(commands, workingDirectory);
        return exec(executor, timeout);
    }


    private int exec(Executor executor, int timeout) {
        setErrorMessage(null);
        setProcessMessage(null);
        Process process = null;

        try {
            process = executor.doIt();

            if (timeout > 0) {
                createTimeoutKiller(process, timeout);
            }

            return checkProcess(process);
        }
        catch (Throwable t) {
            return buildError(t.getMessage());
        }
        finally {
            if (process != null) {
                process.destroy();
            }
        }
    }


    private int checkProcess(Process process) throws InterruptedException {
        setErrorMessage(null);
        setProcessMessage(null);

        writeProcessInput(process);
        readProcessMessages(process);

        int returnCode = process.exitValue();
        if (returnCode != 0) {
            StringBuilder errorMsg = new StringBuilder();
            if (!"".equals(getErrorMessage())) {
                errorMsg.append("Error message :").append(NEW_LINE).append(getErrorMessage());
            }
            errorMsg.append("Output message :").append(NEW_LINE).append(getProcessMessage());
            setErrorMessage(errorMsg.toString());
        }

        return returnCode;
    }


    /**
     * Recupère les sorties du process (ie. stdout et stderr).
     *
     * @param process Le Process
     *
     * @throws InterruptedException Erreur
     */
    private void readProcessMessages(Process process) throws InterruptedException {
        StreamReader errorReader = new StreamReader(process.getErrorStream());
        StreamReader outputReader = new StreamReader(process.getInputStream());

        errorReader.start();
        outputReader.start();

        process.waitFor();
        outputReader.waitReadFinished();
        errorReader.waitReadFinished();

        setErrorMessage(errorReader.getMessage());
        setProcessMessage(outputReader.getMessage());
    }


    private void writeProcessInput(Process process) {
        if (processInput == null) {
            return;
        }
        PrintStream printStream = new PrintStream(process.getOutputStream());
        printStream.println(processInput);
        printStream.flush();
        printStream.close();
    }


    public void setProcessInput(String processInput) {
        this.processInput = processInput;
    }


    /**
     * Retourne le message d'erreur de la derniere execution (stderr).
     *
     * @return La valeur de errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }


    /**
     * Retourne une chaine de caractère qui est la concatenation du flux stdout et stderr du process execute.
     *
     * @return La valeur de outputMessage (not null)
     */
    public String getProcessMessage() {
        return processMessage;
    }


    private void createTimeoutKiller(final Process process, final int timeout) {
        new Thread() {

            @Override
            public synchronized void run() {
                try {
                    //noinspection SynchronizeOnThis
                    wait(timeout);
                    try {
                        process.exitValue();
                    }
                    catch (IllegalThreadStateException ex) {
                        process.destroy();
                    }
                }
                catch (Throwable e) {
                    ;
                }
            }
        }.start();
    }


    /**
     * Positionne le message d'erreur de l'object WindowsExec.
     *
     * @param newErrorMessage La nouvelle valeur de errorMessage
     */
    private void setErrorMessage(String newErrorMessage) {
        if (newErrorMessage == null) {
            newErrorMessage = "";
        }
        errorMessage = newErrorMessage;
    }


    /**
     * Positionne l'attribut outputMessage.
     *
     * @param newProcessMessage La nouvelle valeur de processMessage
     */
    private void setProcessMessage(String newProcessMessage) {
        processMessage = newProcessMessage;
    }


    private int buildError(String message) {
        setErrorMessage(message);
        return -1;
    }


    private class Executor {
        private String cmd;
        private String[] commands;
        private File workingDirectory;


        public Executor(String cmd, File workingDirectory) {
            this.cmd = cmd;
            this.workingDirectory = workingDirectory;
        }


        public Executor(String[] commands, File workingDirectory) {
            this.commands = commands;
            this.workingDirectory = workingDirectory;
        }


        public Process doIt() throws IOException {
            if (cmd != null) {
                return Runtime.getRuntime().exec(cmd, null, workingDirectory);
            }
            else if (commands != null) {
                return Runtime.getRuntime().exec(commands, null, workingDirectory);
            }
            return null;
        }
    }

    /**
     * Lecteur d'un flux.
     *
     * @author $Author: acharif $
     * @version $Revision: 1.5 $
     */
    private static class StreamReader extends Thread {
        private InputStream inputStream;
        private String message = "";


        StreamReader(InputStream inputStream) {
            this.inputStream = inputStream;
        }


        public synchronized String getMessage() {
            return message;
        }


        @Override
        public synchronized void run() {
            try {
                message = FileUtil.loadContent(new InputStreamReader(inputStream));
            }
            catch (IOException ioe) {
                message += ioe.getLocalizedMessage();
            }
            //noinspection SynchronizeOnThis
            notifyAll();
        }


        private synchronized void waitReadFinished() {
            while (isAlive()) {
                try {
                    //noinspection SynchronizeOnThis
                    wait();
                }
                catch (InterruptedException ex) {
                    ;
                }
            }
        }
    }
}
