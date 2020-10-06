package cha.n.googlemap.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorites @JvmOverloads constructor(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = "",
    @ColumnInfo(name = "place_name") val place_name: String = "",
    @ColumnInfo(name = "place_url") val place_url: String = ""
)