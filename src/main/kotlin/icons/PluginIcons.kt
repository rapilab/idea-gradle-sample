package icons

import com.intellij.ui.IconManager
import javax.swing.Icon

object PluginIcons {
    private fun load(path: String): Icon {
        return IconManager.getInstance().getIcon(path, PluginIcons::class.java)
    }

    object Console {
        val Python = load("/icons/python.svg")
    }
}
