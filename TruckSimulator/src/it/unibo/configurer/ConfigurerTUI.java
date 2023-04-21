package it.unibo.configurer;

import java.io.*;

public class ConfigurerTUI {



    Reader reader ;

    public ConfigurerTUI(InputStream reader) {

        this.reader = new BufferedReader(new InputStreamReader(reader));


    }


    public void config() throws IOException {


        System.out.println("");
    }
}
