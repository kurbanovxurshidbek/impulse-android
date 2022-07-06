package uz.impulse.impulse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.impulse.impulse.data.remote.model.Illness

@Dao
interface IllnessDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addIllness(illness: Illness)

    @Query("DELETE FROM illness")
    fun deleteIllness()

    @Query("SELECT * FROM illness")
    fun getAllIllness(): List<Illness>

    @Query("SELECT * FROM illness WHERE id=:id")
    fun getIllnessById(id: Int): Illness
}