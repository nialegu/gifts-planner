package com.example.android.trackmysleepquality.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(primaryKeys = [
    "planId",
    "receiverId"
])
data class PlanReceiver(
    val planId: Long,
    val receiverId: Long,
)

data class PlanWithReceiver(
    @Embedded var plan: Plan,

    @Relation(parentColumn = "pId",
        entityColumn = "rId",
        entity = Receiver::class,
        associateBy = Junction(
            value = PlanReceiver::class,
            parentColumn = "planId",
            entityColumn = "receiverId"
        )
    )
    val receiver: Receiver,
)

@Entity(primaryKeys = [
    "planId",
    "giftId"
])
data class PlanGifts(
    val planId: Long,
    val giftId: Long,
)

data class PlanWithGifts(
    @Embedded var plan: Plan,

    @Relation(parentColumn = "pId",
        entityColumn = "gId",
        entity = Gift::class,
        associateBy = Junction(
            value = PlanGifts::class,
            parentColumn = "planId",
            entityColumn = "giftId"
        )
    )
    val gifts: List<Gift>,
)

data class PlanReceiverGifts(
    @Embedded var plan: Plan,

    @Relation(parentColumn = "pId",
        entityColumn = "rId",
        entity = Receiver::class,
        associateBy = Junction(
            value = PlanReceiver::class,
            parentColumn = "planId",
            entityColumn = "receiverId"
        )
    )
    val receiver: Receiver,

    @Relation(parentColumn = "pId",
        entityColumn = "gId",
        entity = Gift::class,
        associateBy = Junction(
            value = PlanGifts::class,
            parentColumn = "planId",
            entityColumn = "giftId"
        )
    )
    val gifts: List<Gift>,
)