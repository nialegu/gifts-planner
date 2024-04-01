package com.example.android.trackmysleepquality.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(primaryKeys = [
    "planId",
    "receiverId",
    "giftId"
])
data class PlanReceiverGift(
    val planId: Long,
    val receiverId: Long,
    val giftId: Long,
)

data class PlanWithReceiverAndGifts(
    @Embedded var plan: Plan,

    @Relation(parentColumn = "pId",
        entityColumn = "rId",
        entity = Receiver::class,
        associateBy = Junction(
            value = PlanReceiverGift::class,
            parentColumn = "planId",
            entityColumn = "receiverId"
        )
    )
    val receiver: Receiver,

    @Relation(parentColumn = "pId",
        entityColumn = "gId",
        entity = Gift::class,
        associateBy = Junction(
            value = PlanReceiverGift::class,
            parentColumn = "planId",
            entityColumn = "giftId"
        )
    )
    val gifts: List<Gift>
)