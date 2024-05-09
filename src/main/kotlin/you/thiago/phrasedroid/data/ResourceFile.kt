package you.thiago.phrasedroid.data

data class ResourceFile(
    var filename: String,
    var filePath: String,
    var name: String,
    var locale: String,
    var language: String,
    var tag: String,
    var translation: String,
    var sourceTranslation: String,
    var content: String
)