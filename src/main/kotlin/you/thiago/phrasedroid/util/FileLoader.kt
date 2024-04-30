package you.thiago.phrasedroid.util

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

object FileLoader {
    fun getVirtualFileByPath(path: String): VirtualFile? = LocalFileSystem.getInstance()
        .findFileByPath(path)
        ?.takeIf { it.exists() }
        ?: run {
            System.err.println("No file found at specified path: $path")
            return null
        }
}