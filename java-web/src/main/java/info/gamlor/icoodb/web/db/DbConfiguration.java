package info.gamlor.icoodb.web.db;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;

/**
 * @author roman.stoffel@gamlor.info
 * @since 23.08.2010
 */
public class DbConfiguration {
    public static EmbeddedConfiguration newConfig(){
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        configure(config);
        return config;
    }

    public static void configure(EmbeddedConfiguration config){
        config.common().updateDepth(2);
    }
}
