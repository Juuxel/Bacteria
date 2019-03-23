package juuxel.bacteria.container.gui

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.gui.ContainerScreen
import net.minecraft.container.Container
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.TranslatableTextComponent
import net.minecraft.util.Identifier

open class BacteriaContainerScreen<C : Container>(
    container: C, player: PlayerEntity, id: String
) : ContainerScreen<C>(
    container,
    player.inventory,
    TranslatableTextComponent("container.bacteria.$id")
) {
    private val background = Identifier("bacteria", "textures/gui/$id.png")

    override fun drawBackground(v: Float, i: Int, i1: Int) {
        GlStateManager.color4f(1f, 1f, 1f, 1f)
        client?.textureManager?.bindTexture(background)
        drawTexturedRect(left, top, 0, 0, width, height)
    }

    override fun drawForeground(int_1: Int, int_2: Int) {
        val nameText = title.formattedText
        fontRenderer.draw(
            nameText,
            (this.width / 2 - this.fontRenderer.getStringWidth(nameText) / 2).toFloat(),
            6.0f,
            4210752
        )
        fontRenderer.draw(
            this.playerInventory.displayName.formattedText,
            8.0f,
            (this.height - 96 + 2).toFloat(),
            4210752
        )
    }
}
