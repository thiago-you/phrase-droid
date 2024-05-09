package you.thiago.phrasedroid.util

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object NotificationUtil {
    fun success(project: Project, message: String, title: String? = null) {
        val notificationGroup = getNotificationGroup() ?: return

        notificationGroup.createNotification(
            title ?: "PhraseDroid: done!",
            message,
            NotificationType.INFORMATION
        ).notify(project)
    }

    fun warning(project: Project, message: String, title: String? = null) {
        val notificationGroup = getNotificationGroup() ?: return

        notificationGroup.createNotification(
            title ?: "PhraseDroid: warning!",
            message,
            NotificationType.WARNING
        ).notify(project)
    }

    fun error(project: Project, message: String, title: String? = null) {
        val notificationGroup = getNotificationGroup() ?: return

        notificationGroup.createNotification(
            title ?: "PhraseDroid: error!",
            message,
            NotificationType.ERROR
        ).notify(project)
    }

    private fun getNotificationGroup(): NotificationGroup? {
        return NotificationGroupManager.getInstance().getNotificationGroup("PhraseDroidNotification")
    }
}