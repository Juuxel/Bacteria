package juuxel.bacteria.util

import net.minecraft.container.PropertyDelegate

class CompoundPropertyDelegate(private vararg val propertyDelegates: SimplePropertyDelegate) : PropertyDelegate {
    override fun get(i: Int) =
        propertyDelegates[i].get(0)

    override fun set(i: Int, value: Int) =
        propertyDelegates[i].set(0, value)

    override fun size() = propertyDelegates.size
}
