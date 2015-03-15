package com.gas.primer3.core;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.ParentLoc;
import com.gas.domain.core.primer3.IPrimer3Service;
import com.gas.domain.core.primer3.Oligo;
import com.gas.domain.core.primer3.P3Output;
import com.gas.domain.core.primer3.P3OutputHelper;
import com.gas.primer3.core.util.P3OutputParser;
import com.gas.domain.core.primer3.UserInput;
import com.gas.primer3.core.api.BoulderIOUtil;
import com.gas.primer3.core.input.UserInputFactory;
import com.gas.primer3.core.mispriminglib.MisprimingLib;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IPrimer3Service.class)
public class Primer3Service implements IPrimer3Service {

    private Logger logger = Logger.getLogger(Primer3Service.class.getName());
    private boolean strictTags = true;
    private boolean formatOutput = false;
    private boolean echoSettingsFile = true;
    private File errorFile;

    public Primer3Service() {
    }

    @Override
    public void setErrorFile(File errorFile) {
        this.errorFile = errorFile;
    }

    public boolean isStrictTags() {
        return strictTags;
    }

    public void setStrictTags(boolean strictTags) {
        this.strictTags = strictTags;
    }

    public boolean isEchoSettingsFile() {
        return echoSettingsFile;
    }

    public void setEchoSettingsFile(boolean echoSettingsFile) {
        this.echoSettingsFile = echoSettingsFile;
    }

    @Override
    public boolean isFormatOutput() {
        return formatOutput;
    }

    @Override
    public void setFormatOutput(boolean formatOutput) {
        this.formatOutput = formatOutput;
    }

    @Override
    public AnnotatedSeq convertToOligo(AnnotatedSeq parent, int start, int end, Boolean forward) {
        AnnotatedSeq ret = AsHelper.subAs(parent, start, end, AnnotatedSeq.ELEMENT.DESC,
                AnnotatedSeq.ELEMENT.FEATURE,
                AnnotatedSeq.ELEMENT.OVERHANG,
                AnnotatedSeq.ELEMENT.SEQ);
        ret.setOligo(true);
        ret.setP3output(null);
        //ret.getFetureSet().clear();

        ParentLoc parentLoc = new ParentLoc();
        parentLoc.setStart(start);
        parentLoc.setEnd(end);
        parentLoc.setTotalPos(parent.getLength());
        parentLoc.setOffset(0);
        ret.getParentLocSet().clear();
        ret.getParentLocSet().add(parentLoc);

        UserInput userInput = UserInputFactory.getP3WEB_V_3_0_0(true);

        String data = ret.getSiquence().getData();
        data = data.replaceAll("u", "t");
        data = data.replaceAll("U", "T");
        if (forward != null && forward) {
            userInput.getData().put("SEQUENCE_PRIMER", data);
        } else if (forward == null) {
            userInput.getData().put("SEQUENCE_INTERNAL_OLIGO", data);
        } else if (!forward) {
            userInput.getData().put("SEQUENCE_PRIMER_REVCOMP", BioUtil.reverseComplement(data));
        }

        P3Output p3ouput = checkPrimer(userInput, P3Output.class);
        ret.getFetureSet().addAll(P3OutputHelper.toFetures(p3ouput, ret.getLength(), ret.isCircular()));

        return ret;
    }

    /**
     * @param strand if true,
     */
    @Override
    public void checkPrimer(AnnotatedSeq oligo, Boolean strand) {
        if (oligo.getLength() > IPrimer3Service.PRIMER_LENGTH_MAX) {
            return;
        }
        UserInput userInput = UserInputFactory.getP3WEB_V_3_0_0(true);
        String data = oligo.getSiquence().getData();
        data = data.replaceAll("u", "t");
        data = data.replaceAll("U", "T");
        if (strand != null && strand) {
            userInput.getData().put("SEQUENCE_PRIMER", data);
        } else if (strand != null && !strand) {
            userInput.getData().put("SEQUENCE_PRIMER_REVCOMP", data);
        } else {
            userInput.getData().put("SEQUENCE_INTERNAL_OLIGO", data);
        }


        P3Output p3output = checkPrimer(userInput, P3Output.class);
        if (p3output != null) {
            List<Feture> fetures = p3output.toFetures(oligo.getLength(), false);
            oligo.getFetureSet().addAll(fetures);
        }
    }

    @Override
    public <T> T checkPrimer(UserInput userInput, Class<T> retType) {
        T ret = null;
        UserInput cloned = userInput.clone();
        replaceThermodynamicParaPath(cloned);
        cloned.removeSettingsFileTag();
        byte[] bytes = _execute(cloned);
        if (retType.isAssignableFrom(String.class)) {
            String tmp = new String(bytes);
            ret = (T) tmp;
        } else if (retType.isAssignableFrom(P3Output.class)) {
            P3Output output = P3OutputParser.parse(bytes);
            if (userInput.getData().containsKey("SEQUENCE_INTERNAL_OLIGO")) {
                output.getOligos();
                output.setOligoStart(1);
                output.setOligoLength(userInput.getData().get("SEQUENCE_INTERNAL_OLIGO").length());
            } else if (userInput.getData().containsKey("SEQUENCE_PRIMER_REVCOMP")) {
                output.setOligoStart(userInput.getData().get("SEQUENCE_PRIMER_REVCOMP").length());
                output.setOligoLength(userInput.getData().get("SEQUENCE_PRIMER_REVCOMP").length());
            }
            output.setUserInput(cloned);
            output.updateConc();
            ret = (T) output;
        } else {
            throw new IllegalArgumentException(String.format("%s not supported", retType.getName()));
        }
        return ret;
    }

    private void replaceMisprimingLib(UserInput userInput) {
        String name = userInput.getData().get("PRIMER_MISPRIMING_LIBRARY");
        MisprimingLib lib = MisprimingLib.getByName(name);
        String fileName = getMisprimingLibFile(lib);
        userInput.getData().put("PRIMER_MISPRIMING_LIBRARY", fileName);
    }

    private String getMisprimingLibFile(MisprimingLib lib) {
        File file = null;

        if (lib == MisprimingLib.DROSOPHILA) {
            if (Utilities.isWindows()) {
                file = InstalledFileLocator.getDefault().locate("modules/ext/drosophila_w_transposons.txt", "com.gas.primer3.core", false);
            } else {
                throw new UnsupportedOperationException();
            }
        } else if (lib == MisprimingLib.HUMAN) {
            if (Utilities.isWindows()) {
                file = InstalledFileLocator.getDefault().locate("modules/ext/humrep_and_simple.txt", "com.gas.primer3.core", false);
            } else {
                throw new UnsupportedOperationException();
            }
        } else if (lib == MisprimingLib.NONE) {
            // nothing to do
        } else if (lib == MisprimingLib.RODENT) {
            if (Utilities.isWindows()) {
                file = InstalledFileLocator.getDefault().locate("modules/ext/rodent_ref.txt", "com.gas.primer3.core", false);
            } else {
                throw new UnsupportedOperationException();
            }
        } else if (lib == MisprimingLib.RODENT_AND_SIMPLE) {
            if (Utilities.isWindows()) {
                file = InstalledFileLocator.getDefault().locate("modules/ext/rodrep_and_simple.txt", "com.gas.primer3.core", false);
            } else {
                throw new UnsupportedOperationException();
            }
        }
        if (file != null) {
            return file.toString();
        } else {
            return "";
        }
    }

    private void replaceThermodynamicParaPath(UserInput userInput) {
        boolean useThermodynamicModels = userInput.isThermodynamicModels();
        if (useThermodynamicModels) {
            userInput.getData().put("PRIMER_THERMODYNAMIC_PARAMETERS_PATH", getThermodynamicParamPath());
        }
    }

    private UserInput preprocess(UserInput userInput) {
        UserInput cloned = userInput.clone();
        cloned.removeSettingsFileTag();
        replaceThermodynamicParaPath(cloned);
        replaceMisprimingLib(cloned);
        cloned.removeEmptyParams();
        return cloned;
    }

    @Override
    public <T> T execute(UserInput userInput, Class<T> clazz) {
        T ret = null;
        UserInput original = userInput.clone();
        UserInput cloned = preprocess(userInput);

        byte[] bytes = _execute(cloned);

        if (bytes == null) {
            return null;
        }

        if (clazz.isAssignableFrom(String.class)) {
            String retStr = new String(bytes);
            ret = (T) retStr;
        } else if (clazz.isAssignableFrom(P3Output.class)) {
            P3Output output = P3OutputParser.parse(bytes);
            output.setUserInput(original);
            output.updateConc();
            ret = (T) output;
        } else if (clazz.isAssignableFrom(List.class)) {
            P3Output output = P3OutputParser.parse(bytes);
            if (output != null) {
                List tmp = P3OutputHelper.toFetures(output, null, null);
                ret = (T) tmp;
            } else {
                ret = (T) new ArrayList<Feture>();
            }
        } else {
            throw new IllegalArgumentException(clazz.getName() + " is not supported!");
        }

        return ret;
    }

    /**
     * @return null if error
     */
    private byte[] _execute(UserInput cloned) {
        byte[] bytes = null;
        Executor exec = new DefaultExecutor();
        ByteArrayOutputStream outputStream = null;
        ByteArrayOutputStream errorStream = null;

        try {

            File userInputFile = BoulderIOUtil.toFile(cloned.getData());

            InputStream inputStream = new FileInputStream(userInputFile);
            outputStream = new ByteArrayOutputStream();
            errorStream = new ByteArrayOutputStream();
            exec.setStreamHandler(new PumpStreamHandler(outputStream,
                    errorStream, inputStream));
        } catch (FileNotFoundException e) {            
            throw new RuntimeException(e);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        CommandLine cl = getCommandLine();
        try {
            String clStr = cl.toString();
            int exitValue = exec.execute(cl);
            if (exitValue == 0) {
                bytes = outputStream.toByteArray();
            } else {
                return null;
            }
        } catch (ExecuteException e) {            
            throw new RuntimeException(e);
        } catch (IOException e) {            
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    CommandLine getCommandLine() {
        CommandLine ret = new CommandLine(getExecutablePath());
        if (isFormatOutput()) {
            ret.addArgument("-format_output");
        }
        if (isStrictTags()) {
            ret.addArgument("-strict_tags");
        }
        if (isEchoSettingsFile()) {
            ret.addArgument("-echo_settings_file");
        }
        if (errorFile != null && errorFile.exists()) {
            ret.addArgument("-error=" + errorFile.getAbsolutePath());
        }
        return ret;
    }

    private String getThermodynamicParamPath() {
        File file = null;
        if (Utilities.isWindows()) {
            file = InstalledFileLocator.getDefault().locate("modules/ext/primer3_config", "com.gas.primer3.core", false);
        } else if (Utilities.isMac()) {
            throw new IllegalStateException("MAC OS not supported yet");
        } else {
            throw new IllegalStateException("Your OS not supported yet");
        }
     
        String ret = file.getAbsolutePath() + File.separatorChar;
        return ret;
    }

    private String getExecutablePath() {
        File file = null;
        if (Utilities.isWindows()) {
            file = InstalledFileLocator.getDefault().locate("modules/ext/primer3_core.exe", "com.gas.primer3.core", false);
        } else if (Utilities.isMac()) {
            throw new IllegalStateException("MAC OS not supported yet");
        } else {
            throw new IllegalStateException("Your OS not supported yet");
        }

        if (file == null) {
            file = new File("C:\\Downloads\\primer3\\primer3-2.3.5\\src\\primer3_core.exe");
        }

        file.setExecutable(true);
        return file.getAbsolutePath();
    }
}