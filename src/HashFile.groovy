import java.security.MessageDigest

// Hash the contents of a file using MD5
// returns the digest hash value
class HashFile {
    static Map<String, Object> calculateHash(String file) {
        def chunkSize = 8192
        def startTime = System.currentTimeMillis()

        def hasher = MessageDigest.getInstance("MD5")
        def fileStream = new File(file).newInputStream()

        byte[] buffer = new byte[chunkSize]
        while (fileStream.read(buffer) != -1) {
            hasher.update(buffer)
        }

        def endTime = System.currentTimeMillis()

        return [
                hash: hasher.digest().encodeHex().toString(),
                timeTaken: endTime - startTime
        ]
    }
}
