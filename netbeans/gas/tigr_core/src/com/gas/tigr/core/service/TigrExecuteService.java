package com.gas.tigr.core.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.Semaphore;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.tasm.Condig;
import com.gas.domain.core.tasm.TasmParser;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.KromatogramList;
import com.gas.domain.core.tigr.TIGRSettings;
import com.gas.domain.core.tigr.TigrProject;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ITigrExecuteService.class)
public class TigrExecuteService implements ITigrExecuteService {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Semaphore outputSem;
    public String output;
    private Semaphore errorSem;
    public CharSequence error;
    private Process process;
    public File alignmentDir;
    private File scratchFile;
    private File fastaFile;
    private File qualFile;
    private File aceFile;
    private File coverageFile;
    private File repeatFile;
    private File asmFile;

    public File getAsmFile() {
        return asmFile;
    }

    public File getAlignmentDir() {
        return alignmentDir;
    }

    public File getScratchFile() {
        return scratchFile;
    }

    public File getFastaFile() {
        return fastaFile;
    }

    public File getQualFile() {
        return qualFile;
    }

    public File getAceFile() {
        return aceFile;
    }

    public File getCoverageFile() {
        return coverageFile;
    }

    public File getRepeatFile() {
        return repeatFile;
    }

    public Process getProcess() {
        return process;
    }

    public Semaphore getOutputSem() {
        return outputSem;
    }

    public void setOutputSem(Semaphore s) {
        outputSem = s;
    }

    public Semaphore getErrorSem() {
        return errorSem;
    }

    public CharSequence getError() {
        return error;
    }

    private String getExecutablePath() {
        File file = null;
        if (Utilities.isWindows()) {
            file = InstalledFileLocator.getDefault().locate("modules/ext/TIGR_Assembler_win.exe", "com.gas.tigr.core", false);
        } else if (Utilities.isMac()) {
            throw new IllegalStateException("MAC OS not supported yet");
        } else {
            throw new IllegalStateException("Your OS not supported yet");
        }

        if (file == null) {
            file = new File("D:\\sequenceanalysis_vf\\trunk\\netbeans\\gas\\tigr_core\\release\\modules\\ext\\TIGR_Assembler_win.exe");
        }

        file.setExecutable(true);
        return file.getAbsolutePath();
    }

    @Override
    public <T> T assembly(byte[] seq, byte[] qual, TIGRSettings settings, Class<T> retType) {
        T ret = null;
        asmFile = FileHelper.getUniqueFile("TIGR", ".asm");

        // fasta file
        fastaFile = FileHelper.getUniqueFile("TIGR", ".fasta");
        FileHelper.toFile(fastaFile, seq);

        // quality file
        if (qual != null) {
            qualFile = FileHelper.getUniqueFile("TIGR", ".qual");

            FileHelper.toFile(qualFile, qual);
        }

        // create alignment directory
        alignmentDir = FileHelper.getUniqueDir("TIGR");

        // scratch file
        scratchFile = FileHelper.getUniqueFile("TIGR", ".scratch");

        // create ACE file if required
        if (settings.getGenerateACE()) {
            aceFile = FileHelper.getUniqueFile("TIGR", ".ace2");
        }

        // create coverage file if required
        if (settings.getGenerateCoverage()) {
            coverageFile = FileHelper.getUniqueFile("TIGR", ".coverage");
        }

        // create repeat file if required
        if (settings.getGenerateRepeatFile()) {
            repeatFile = FileHelper.getUniqueFile("TIGR", ".repeat");
        }

        try {
            Executor executor = new DefaultExecutor();
            CommandLine command = getCommandLine(settings);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(seq);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorOutStream = new ByteArrayOutputStream();

            executor.setStreamHandler(new PumpStreamHandler(outputStream, errorOutStream, inputStream));
            executor.execute(command);
            output = outputStream.toString();

            byte[] bytes = outputStream.toByteArray();
            if (retType.isAssignableFrom(byte[].class)) {
                ret = (T) bytes;
            } else if (retType.isAssignableFrom(String.class)) {
                String tmp = new String(bytes);
                ret = (T) tmp;
            } else if (retType.isAssignableFrom(List.class)) {
                List<Condig> tmp = TasmParser.parse(bytes);
                ret = (T) tmp;
            } else {
                throw new IllegalArgumentException(output);
            }

            error = errorOutStream.toString();


        } catch (Exception e) {
            System.out.print("");
        }
        return ret;
    }

    private CommandLine getCommandLine(TIGRSettings settings) {
        String exePath = getExecutablePath();
        CommandLine ret = new CommandLine(exePath);

        if (alignmentDir != null) {
            ret.addArgument("-a");
            ret.addArgument(alignmentDir.getAbsolutePath());
        }

        if (aceFile != null) {
            ret.addArgument("-A");
            ret.addArgument(aceFile.getAbsolutePath());
        }
        if (coverageFile != null) {
            ret.addArgument("-c");
            ret.addArgument(coverageFile.getAbsolutePath());
        }
        ret.addArgument("-e");
        ret.addArgument(Integer.toString(settings.getMaximumEnd()));
        if (fastaFile != null) {
            ret.addArgument("-f");
            ret.addArgument(fastaFile.getAbsolutePath());
        }
        if (repeatFile != null) {
            ret.addArgument("-F");
            ret.addArgument(repeatFile.getAbsolutePath());
        }
        if (settings.getIncludeBadSeq()) {
            ret.addArgument("-b");
        }
        ret.addArgument("-g");
        ret.addArgument(Integer.toString(settings.getMaxError32()));
        ret.addArgument("-l");
        ret.addArgument(Integer.toString(settings.getMinimumLength()));
        if (settings.getLowScores()) {
            ret.addArgument("-L");
        }
        if (settings.getNoNorm()) {
            ret.addArgument("-N");
        }
        ret.addArgument("-p");
        ret.addArgument(Float.toString(settings.getMinPercent()));
        if (qualFile != null) {
            ret.addArgument("-q");
            ret.addArgument(qualFile.getAbsolutePath());
        }
        ret.addArgument("-r");
        ret.addArgument(Integer.toString(settings.getRestart_inc()));
        if (settings.getGenerateSingletons()) {
            ret.addArgument("-s");
        }
        ret.addArgument("-S");
        ret.addArgument(Integer.toString(settings.getMaxSpanLength()));
        if (!settings.getSearchTandem()) {
            ret.addArgument("-t");
        } else {
            ret.addArgument("-u");
        }

        ret.addArgument("-y");
        ret.addArgument(Integer.toString(settings.getRepeatNumCufoff()));
        ret.addArgument("-z");
        ret.addArgument(Integer.toString(settings.getNumConflicts()));
        if (settings.getZapQuestionable()) {
            ret.addArgument("-Z");
        }



        // scratch file must be the last parameter
        ret.addArgument(scratchFile.getAbsolutePath());
        return ret;
    }

    @Override
    public TigrProject assembly(Collection<Kromatogram> kromatograms, byte[] qual, TIGRSettings settings) {
        TigrProject ret = new TigrProject();
        KromatogramList kList = new KromatogramList(kromatograms);
        byte[] data = kList.toFasta().toByteArray(60);
        List<Condig> condigs = assembly(data, qual, settings, List.class);
        if (condigs != null && !condigs.isEmpty()) {
            ret.addKromatograms(kromatograms);
            ret.setCondigs(new HashSet<Condig>(condigs));            
            ret.createAlteredKromatograms();
        }
        ret.setName("Assembly");
        ret.setDesc(String.format("assembly of %d chromatograms", kromatograms.size()));
        ret.setCreationDate(new Date());
        ret.setLastModifiedDate(new Date());
        ret.setSettings(settings);
        return ret;
    }
}
