package juuxel.bacteria.util

import net.minecraft.container.PropertyDelegate

class SimplePropertyDelegate(private val getter: () -> Int, private val setter: (Int) -> Unit) : PropertyDelegate {
    override fun get(var1: Int) = getter()
    override fun set(var1: Int, var2: Int) = setter(var2)
    override fun size() = 1
}
