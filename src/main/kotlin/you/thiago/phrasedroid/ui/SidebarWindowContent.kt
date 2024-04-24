package you.thiago.phrasedroid.ui

import com.intellij.openapi.wm.ToolWindow
import you.thiago.phrasedroid.state.MyState
import java.awt.BorderLayout
import javax.swing.*

class SidebarWindowContent(toolWindow: ToolWindow) {

    val contentPanel: JPanel = JPanel()

    private val currentDate = JLabel()
    private val timeZone = JLabel()
    private val currentTime = JLabel()

    init {
        contentPanel.layout = BorderLayout(0, 20)
        contentPanel.border = BorderFactory.createEmptyBorder(40, 0, 0, 0)
        contentPanel.add(createCalendarPanel(), BorderLayout.PAGE_START)

        if (MyState().getInstance().state.isLoading) {
            contentPanel.add(createControlsPanel(toolWindow), BorderLayout.PAGE_START)
        }
    }

    private fun createCalendarPanel(): JPanel {
        val calendarPanel = JPanel()
        setIconLabel(currentDate, CALENDAR_ICON_PATH)
        setIconLabel(timeZone, TIME_ZONE_ICON_PATH)
        setIconLabel(currentTime, TIME_ICON_PATH)
        calendarPanel.add(currentDate)
        calendarPanel.add(timeZone)
        calendarPanel.add(currentTime)
        return calendarPanel
    }

    private fun setIconLabel(label: JLabel, imagePath: String) {
//        label.icon = ImageIcon(javaClass.getResource(imagePath))
    }

    private fun createControlsPanel(toolWindow: ToolWindow): JPanel {
        val controlsPanel = JPanel()
        val refreshDateAndTimeButton = JButton("Refresh")
        refreshDateAndTimeButton.addActionListener { _ -> }
        controlsPanel.add(refreshDateAndTimeButton)
        val hideToolWindowButton = JButton("Hide")
        hideToolWindowButton.addActionListener { _ -> toolWindow.hide(null) }
        controlsPanel.add(hideToolWindowButton)
        return controlsPanel
    }
//
//    private fun updateCurrentDateTime() {
//        val calendar: Calendar = Calendar.getInstance()
//        currentDate.text = getCurrentDate(calendar)
//        timeZone.text = getTimeZone(calendar)
//        currentTime.text = getCurrentTime(calendar)
//    }
//
//    private fun getCurrentDate(calendar: Calendar): String {
//        return ((calendar.get(Calendar.DAY_OF_MONTH) + "/"
//                + (calendar.get(Calendar.MONTH) + 1)).toString() + "/"
//                + calendar.get(Calendar.YEAR))
//    }
//
//    private fun getTimeZone(calendar: Calendar): String {
//        val gmtOffset: Long = calendar.get(Calendar.ZONE_OFFSET) // offset from GMT in milliseconds
//        val gmtOffsetString = (gmtOffset / 3600000).toString()
//        return if ((gmtOffset > 0)) "GMT + $gmtOffsetString" else "GMT - $gmtOffsetString"
//    }
//
//    private fun getCurrentTime(calendar: Calendar): String {
//        return getFormattedValue(calendar, Calendar.HOUR_OF_DAY) + ":" + getFormattedValue(calendar, Calendar.MINUTE)
//    }
//
//    private fun getFormattedValue(calendar: Calendar, calendarField: Int): String {
//        val value: Int = calendar.get(calendarField)
//        return StringUtils.leftPad(value.toString(), 2, "0")
//    }

    companion object {
        private const val CALENDAR_ICON_PATH = "/toolWindow/Calendar-icon.png"
        private const val TIME_ZONE_ICON_PATH = "/toolWindow/Time-zone-icon.png"
        private const val TIME_ICON_PATH = "/toolWindow/Time-icon.png"
    }
}