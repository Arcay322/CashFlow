import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val icon: String, // Or Int, depending on how you store icon references
    val type: String? // "Ingreso" or "Gasto", nullable
)