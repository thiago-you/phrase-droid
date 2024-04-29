package you.thiago.phrasedroid.data

data class ResourceFile(
    var filename: String,
    var filePath: String,
    var name: String,
    var content: String,
    var locale: String,
    var translation: String,
    var sourceTranslation: String,
)