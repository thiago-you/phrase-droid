package you.thiago.phrasedroid.state

import you.thiago.phrasedroid.data.ResourceFile

object FlashState {
    var translationKey: String = ""
    var isUpdateSelected: Boolean = false
    var translations: List<ResourceFile> = emptyList()
}