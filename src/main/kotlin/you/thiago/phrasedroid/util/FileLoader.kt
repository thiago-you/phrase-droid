package you.thiago.phrasedroid.util

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile


object FileLoader {
    fun getVirtualFileByPath(path: String): VirtualFile? {
        // Obtain the instance of LocalFileSystem
        val localFileSystem = LocalFileSystem.getInstance()

        // Find the VirtualFile at the given path
        val file = localFileSystem.findFileByPath(path)

        // Check if the file exists and return it
        if (file != null && file.exists()) {
            return file
        } else {
            // Handle the case where the file does not exist or is null
            System.err.println("No file found at specified path: $path")
            return null
        }
    }
}