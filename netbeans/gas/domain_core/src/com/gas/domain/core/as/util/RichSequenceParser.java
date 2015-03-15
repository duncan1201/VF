package com.gas.domain.core.as.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojavax.Namespace;
import org.biojavax.SimpleNamespace;
import org.biojavax.bio.seq.io.GenbankFormat;
import org.biojavax.bio.seq.io.RichSequenceFormat;

import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.flexrs.IFlexRichSequence;
import com.gas.domain.core.flexrs.FlexSimpleRichSequenceBuilder;

public class RichSequenceParser {

    public static List<IFlexRichSequence> parse(String genbankStr,
            RichSequenceFormat format) {
        ByteArrayInputStream bais = new ByteArrayInputStream(
                genbankStr.getBytes());
        return parse(bais, format);
    }

    public static List<IFlexRichSequence> parse(InputStream inputStream,
            RichSequenceFormat format) {
        if (format instanceof GenbankFormat) {
            throw new IllegalArgumentException("Please use FlexGenbankFormat instead of GenbankFormat");
        }
        List<IFlexRichSequence> ret = new ArrayList<IFlexRichSequence>();

        BufferedInputStream bufferedInputStream = new BufferedInputStream(
                inputStream);

        boolean canRead = false;
        try {
            canRead = format.canRead(bufferedInputStream);
        } catch (IOException e) {
            canRead = false;
        }

        if (!canRead) {
            return ret;
        }

        SymbolTokenization symParser = null;
        try {
            symParser = format.guessSymbolTokenization(bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return ret;
        }
        Namespace namespace = new SimpleNamespace("lcl");

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                bufferedInputStream));

        FlexSimpleRichSequenceBuilder builder = null;
        boolean hasMoreSequence = true;
        while (hasMoreSequence) {
            try {
                builder = new FlexSimpleRichSequenceBuilder();
                hasMoreSequence = format.readRichSequence(reader, symParser,
                        builder, namespace);

                builder.setName(builder.getName());
                /*
                 builder.setName(builder.getName() + " "
                 + UUID.randomUUID().toString());
                 */
                ret.add(builder.makeRichSequence());
            } catch (Exception e) {
                e.printStackTrace();
                return ret;
            }
        }

        return ret;
    }

    public static List<IFlexRichSequence> parse(File file, RichSequenceFormat format) {
        StringBuffer genBankStr;
        genBankStr = FileHelper.toStringBuffer(file);
        return parse(genBankStr.toString(), format);
    }

    public static IFlexRichSequence singleParse(Class _class, String resourceName,
            RichSequenceFormat format) {
        List<IFlexRichSequence> list = parse(_class, resourceName, format);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static List<IFlexRichSequence> parse(Class _class, String resourceName,
            RichSequenceFormat format) {
        InputStream inputStream = _class.getResourceAsStream(resourceName);
        return parse(inputStream, format);
    }
}
