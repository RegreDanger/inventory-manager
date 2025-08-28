package common.platform.config.db.sqlite.enums;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import bootstrap.Main;
import common.kernel.exceptions.api.InternalServerException;

public enum SqlitePaths {

    SQLITE_PROPERTIES_PATH("/sqlite.properties"),

    JAR_PATH(getJarPath()),
    SQLITE_DB_PATH(JAR_PATH + "db/");

    private final String path;

    SqlitePaths(String path) {
        this.path = path;
    }

    private static String getJarPath() {
        try {
            String path = Paths.get(Main.class
                       .getProtectionDomain()
                       .getCodeSource()
                       .getLocation()
                       .toURI())
                       .toString();
            Pattern pattern = Pattern.compile("[/\\\\]target[/\\\\]classes(?:[/\\\\])?$|[/\\\\][^/\\\\]+\\.jar$");
            String pathModified = pattern.matcher(path).replaceAll("");
            return pathModified + "/";
        } catch (URISyntaxException e) {
            throw new InternalServerException(
                    "Error getting JAR path", e
            );
        }
    }


    public String getPath() {
        return this.path;
    }

    @Override
    public String toString() {
        return this.path;
    }
}
