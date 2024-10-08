package com.omgodse.notally.changehistory

import com.omgodse.notally.model.ListItem
import com.omgodse.notally.recyclerview.ListManager

class ListMoveChange(
    positionFrom: Int,
    internal val positionTo: Int,
    internal var positionAfter: Int,
    internal val itemBeforeMove: ListItem,
    internal val listManager: ListManager,
) : ListChange(positionFrom) {
    override fun redo() {
        positionAfter = listManager.move(position, positionTo, pushChange = false)!!
    }

    override fun undo() {
        listManager.undoMove(positionAfter, position, itemBeforeMove)
    }

    override fun toString(): String {
        return "MoveChange from: $position to: $positionTo after: $positionAfter itemBeforeMove: $itemBeforeMove"
    }
}