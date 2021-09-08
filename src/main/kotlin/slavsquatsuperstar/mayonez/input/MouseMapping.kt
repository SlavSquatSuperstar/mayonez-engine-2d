package slavsquatsuperstar.mayonez.input

import java.awt.event.MouseEvent
import java.util.*

internal enum class MouseMapping(var button: Int) {
    LEFT_MOUSE(MouseEvent.BUTTON1), RIGHT_MOUSE(MouseEvent.BUTTON3), MIDDLE_MOUSE(MouseEvent.BUTTON2);

    // ex: MouseMapping.LEFT_MOUSE returns "left mouse"
    override fun toString(): String = name.replace('_', ' ').lowercase(Locale.getDefault())
}