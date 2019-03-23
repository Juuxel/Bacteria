package juuxel.bacteria.container.gui

import juuxel.bacteria.block.entity.HumidifierEntity
import juuxel.bacteria.container.HumidifierContainer
import juuxel.bacteria.lib.ModContainers
import net.minecraft.client.gui.ingame.RecipeBookProvider
import net.minecraft.client.gui.recipebook.RecipeBookGui
import net.minecraft.client.gui.widget.RecipeBookButtonWidget
import net.minecraft.container.Slot
import net.minecraft.container.SlotActionType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.util.Identifier

class HumidifierScreen(syncId: Int, private val entity: HumidifierEntity, player: PlayerEntity) :
    BacteriaContainerScreen<HumidifierContainer>(
        syncId,
        entity.getInventory(),
        player,
        ModContainers.humidifier.path,
        ::HumidifierContainer
    ), RecipeBookProvider {
    private val recipeBook = RecipeBookGui()
    private var narrow = false

    override fun onInitialized() {
        super.onInitialized()
        narrow = screenWidth < 379
        recipeBook.initialize(screenWidth, screenHeight, client, narrow, container)
        left = recipeBook.findLeftEdge(narrow, screenWidth, width)
        addButton(RecipeBookButtonWidget(left + 20, screenHeight / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE) {
            recipeBook.reset(narrow)
            recipeBook.toggleOpen()
            left = recipeBook.findLeftEdge(narrow, screenWidth, width)
            (it as RecipeBookButtonWidget).setPos(left + 20, screenHeight / 2 - 49)
        })
    }

    override fun update() {
        super.update()
        recipeBook.update()
    }

    override fun render(i1: Int, i2: Int, f: Float) {
        drawBackground()
        if (recipeBook.isOpen && this.narrow) {
            drawBackground(f, i1, i2)
            recipeBook.render(i1, i2, f)
        } else {
            recipeBook.render(i1, i2, f)
            super.render(i1, i2, f)
            recipeBook.drawGhostSlots(this.left, this.top, true, f)
        }

        drawMouseoverTooltip(i1, i2)
        recipeBook.drawTooltip(this.left, this.top, i1, i2)
    }

    // Copied from AbstractFurnaceScreen

    override fun mouseClicked(double_1: Double, double_2: Double, int_1: Int): Boolean {
        return if (this.recipeBook.mouseClicked(double_1, double_2, int_1)) {
            true
        } else {
            if (this.narrow && this.recipeBook.isOpen) true else super.mouseClicked(double_1, double_2, int_1)
        }
    }

    override fun onMouseClick(slot_1: Slot?, int_1: Int, int_2: Int, slotActionType_1: SlotActionType) {
        super.onMouseClick(slot_1, int_1, int_2, slotActionType_1)
        this.recipeBook.slotClicked(slot_1)
    }

    override fun keyPressed(int_1: Int, int_2: Int, int_3: Int): Boolean {
        return if (this.recipeBook.keyPressed(int_1, int_2, int_3)) false else super.keyPressed(int_1, int_2, int_3)
    }

    override fun isClickOutsideBounds(double_1: Double, double_2: Double, int_1: Int, int_2: Int, int_3: Int): Boolean {
        val boolean_1 =
            double_1 < int_1.toDouble() || double_2 < int_2.toDouble() || double_1 >= (int_1 + this.width).toDouble() || double_2 >= (int_2 + this.height).toDouble()
        return this.recipeBook.isClickOutsideBounds(
            double_1,
            double_2,
            this.left,
            this.top,
            this.width,
            this.height,
            int_3
        ) && boolean_1
    }

    override fun charTyped(char_1: Char, int_1: Int): Boolean {
        return if (this.recipeBook.charTyped(char_1, int_1)) true else super.charTyped(char_1, int_1)
    }

    override fun refreshRecipeBook() {
        this.recipeBook.refresh()
    }

    override fun getRecipeBookGui(): RecipeBookGui {
        return this.recipeBook
    }

    override fun onClosed() {
        this.recipeBook.close()
        super.onClosed()
    }

    // End copies from AbstractFurnaceScreen

    companion object {
        private val RECIPE_BUTTON_TEXTURE = Identifier("minecraft", "textures/gui/recipe_button.png")
    }
}