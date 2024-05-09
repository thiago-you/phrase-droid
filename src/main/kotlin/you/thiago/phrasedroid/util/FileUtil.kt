package you.thiago.phrasedroid.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import you.thiago.phrasedroid.enums.SettingsEnum
import java.nio.file.Paths

object FileUtil {
    fun getVirtualFileByPath(path: String): VirtualFile? = LocalFileSystem.getInstance()
        .findFileByPath(path)
        ?.takeIf { it.exists() }
        ?: run {
            System.err.println("No file found at specified path: $path")
            return null
        }

    fun getVirtualFileByProjectRelativePath(project: Project): VirtualFile? = project.basePath
        ?.let { Paths.get(it, SettingsEnum.FILENAME.value).toString() }
        ?.let { getVirtualFileByPath(it) }

    fun createSettingsFileFromTemplate(project: Project): VirtualFile? = JsonTemplate.get()
        .trimIndent()
        .let { createFileAtProjectRoot(project, it) }

    private fun createFileAtProjectRoot(project: Project, content: String): VirtualFile? = project.guessProjectDir()
        ?.findOrCreateChildData(this, SettingsEnum.FILENAME.value)
        ?.apply { setBinaryContent(content.toByteArray()) }
}