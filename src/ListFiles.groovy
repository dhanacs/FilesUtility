import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.FileVisitOption

def getPaths(rootPath, extension) {
    def iniFile = "desktop.ini";
    def hashes = [:];

    try {
        def paths = Files.walk(Paths.get(rootPath), FileVisitOption.FOLLOW_LINKS)
                .findAll { path ->
                    Files.isRegularFile(path) && path.toString().toLowerCase().endsWith(".${extension}")
                }
                .collect()

        // List the paths
        for(def path : paths) {
            path = path.toString();
            def file = new File(path);
            def fileName = file.getName().toLowerCase();
            if(fileName.equals(iniFile)) {
                println(path);
                file.delete();
            }
        }
    } catch (IOException e) {
        println("Error occurred while file walking: ${e.message}")
    }

    return hashes;
}

def rootPath = "C:\\Users\\dhana\\Downloads\\";
getPaths(rootPath, "ini");