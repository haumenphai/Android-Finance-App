package promax.dohaumen.financeapp.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import promax.dohaumen.financeapp.helper.getCurrentTimeStr

abstract class BaseModel {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var createTime = getCurrentTimeStr()
    var name = ""

    override fun equals(other: Any?): Boolean {
        if (other is BaseModel) {
            return this.id == other.id
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}


