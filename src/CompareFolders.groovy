import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.FileVisitOption

def hashFiles(rootPath, exclusionPaths) {
    def hashes = [:];

    try {
        def paths = Files.walk(Paths.get(rootPath), FileVisitOption.FOLLOW_LINKS)
                .collect()

        for(def path : paths) {
            try {
                path = path.toString();

                def skip = false;
                for(def i : exclusionPaths) {
                    def key = i.key;

                    if(path.startsWith(key)) {
                        skip = true;
                        println("Excluded: " + path);
                        break;
                    }
                }
                if(skip) continue;

                def file = new File(path)
                def result = HashFile.calculateHash(path)
                hashes[file.getName()] = [hash: result.hash, path: path]
            } catch (e) {
                // To Do: update this section
                continue;
            }
        }
    } catch (IOException e) {
        println("Error occurred while file walking: ${e.message}")
    }

    return hashes;
}

def process(hashes1, hashes2) {
    // Deleted
    println()
    println("Deleted files:")
    for(def i : hashes1) {
        def key = i.key
        if(!hashes2.containsKey(key)) {
            println(i.value.path)
        }
    }

    // Added
    println()
    println("Added files:")
    for(def i : hashes2) {
        def key = i.key
        if(!hashes1.containsKey(key)) {
            println(i.value.path)
        }
    }

    // Modified
    println()
    println("Modified files:")
    for(def i : hashes1) {
        def key = i.key
        def value = i.value.hash
        if(hashes2.containsKey(key) && value != hashes2[key].hash) {
            println(i.value.path)
        }
    }
}

// Exclude paths
def exclusionPaths = [:]
exclusionPaths["D:\\Personal\\Softwares\\"] = true;

// Specify the path to the directory
def rootPath1 = "D:\\Pictures"
def hashes1 = hashFiles(rootPath1, exclusionPaths)

def rootPath2 = "C:\\Users\\dhana\\OneDrive\\Pictures"
def hashes2 = hashFiles(rootPath2, exclusionPaths)

process(hashes1, hashes2)
