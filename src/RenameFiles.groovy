import java.nio.file.*

// Below code renames all the files in a directory to:
// Common prefix followed by a number
def i = 1
def prefix = "Cats"
def directoryPath = "C:\\Users\\dhana\\OneDrive\\Pictures\\Wall Papers\\Wild Cats"

try {
    // Sort the files similiar to that of Windows explorer
    // sort by name
    def files = Files.list(Paths.get(directoryPath))
            .sorted { file1, file2 ->
                // Custom comparator to handle numerical values in filenames
                def fileName1 = file1.getFileName().toString()
                def fileName2 = file2.getFileName().toString()

                def numericPart1 = extractNumericPart(fileName1)
                def numericPart2 = extractNumericPart(fileName2)

                numericPart1 <=> numericPart2
            }
            .collect()

    // Iterate through the files
    files.each { file ->
        def name = file.getFileName().toString()
        def extension = name.replaceAll(/^.*\./, '').toLowerCase()

        // Ignore .ini files
        if (!extension.equals("ini")) {
            def fileName = "${prefix}${i}.${extension}"
            println("Renaming file: ${name} to ${fileName}")
            def newFilePath = file.parent.resolve(fileName)
            Files.move(file, newFilePath, StandardCopyOption.REPLACE_EXISTING)
            ++i
        }
    }
} catch (IOException e) {
    // Handle any potential exceptions
    println("An error occurred: ${e.message}")
}

// Function to extract and parse the numeric part from a filename
def extractNumericPart(filename) {
    def numericPart = filename.replaceAll("\\D", "")
    return numericPart.isEmpty() ? 0 : numericPart.toInteger()
}
