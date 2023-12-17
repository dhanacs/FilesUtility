import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey

def removeMetadataForFolder(String folderPath) {
  def folder = new File(folderPath)

  // Check if the provided path is a directory
  if (!folder.isDirectory()) {
    println("Error: The specified path is not a directory.")
    return
  }

  // Iterate through each file and folder in the directory
  folder.eachFile { file ->
    if (file.isDirectory()) {
      // If it's a directory, call the function recursively
      removeMetadataForFolder(file.path)
    } else if (file.isFile() && file.name.endsWith(".mp3")) {
      // If it's an MP3 file, remove metadata
      removeMetadata(file.path)
    }
  }

  println("Metadata removal for all MP3 files in the folder and its sub-folders completed.")
}

def removeMetadata(def filePath) {
  try {
    // Load the MP3 file with explicit format detection
    def audioFile = AudioFileIO.read(new File(filePath))

    // Get the tag and remove all fields
    // Except Album & Year
    def tag = audioFile.tag

    def album = tag.getFirst(FieldKey.ALBUM)
    def artist = tag.getFirst(FieldKey.ARTIST)
    def albumArtist = tag.getFirst(FieldKey.ALBUM_ARTIST)
    def year = tag.getFirst(FieldKey.YEAR)
    def originalTagFieldIdList = tag.fields.collect { tagField ->
      tagField.id
    }

    originalTagFieldIdList.each { tagFieldId ->
      tag.deleteField(tagFieldId)
    }
    tag.setField(FieldKey.ALBUM, album)
    tag.setField(FieldKey.ARTIST, artist)
    tag.setField(FieldKey.ALBUM_ARTIST, albumArtist)
    tag.setField(FieldKey.YEAR, year)

    def id3v1tag = audioFile.id3v1tag
    id3v1tag.deleteField(FieldKey.TITLE)
    id3v1tag.deleteField(FieldKey.COMMENT)

    // Update the audio file
    AudioFileIO.write(audioFile)

    println("Metadata removed successfully.")
  } catch (Exception e) {
    println("Error removing metadata: ${e.message}")
  }
}

// Example usage for the entire folder
def mp3FolderPath = "C:\\Users\\dhana\\OneDrive\\Desktop\\Fun\\Songs\\Tamil"
removeMetadataForFolder(mp3FolderPath)
