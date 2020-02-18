package poc;

import com.prowidesoftware.swift.model.field.Field;
import com.prowidesoftware.swift.model.field.Field20;
import com.prowidesoftware.swift.model.field.Field32A;
import com.prowidesoftware.swift.model.mt.mt1xx.MT103;
import com.prowidesoftware.swift.utils.Lib;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParseMT103FromFileExample {

    public static void main(String[] args) throws Exception{
        /*
         * Read and parse the file content into a SWIFT message object
         * Parse from File could also be used here
         */
        MT103 mt = MT103.parse(Lib.readResource("mt103", null));
        Map<String,String> data = new LinkedHashMap<>();
        /*
         * Print header information
         */
        data.put("Sender", mt.getSender());
        data.put("Receiver", mt.getReceiver());

        /*
         */
        Field20 ref = mt.getField20();
        data.put(Field.getLabel(ref.getName(), mt.getMessageType(), null), ref.getComponent(Field20.REFERENCE));

        Field32A f = mt.getField32A();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        data.put("Value Date", sdf.format(f.getDateAsCalendar().getTime()));
        data.put("Amount", f.getCurrency()+" "+f.getAmount());

        Charset utf8 = StandardCharsets.UTF_8;
        String headers = data.keySet().stream().collect(Collectors.joining(","));
        String value = data.values().stream().collect(Collectors.joining(","));
        List<String> list = Arrays.asList(headers, value);


        try {
            Files.write(Paths.get("mt103_output.csv"), list, utf8);
            System.out.println("File Generated with below data");
            data.forEach((key, val) -> {
                System.out.println(key + " : " + val);
            });
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

    }

}
