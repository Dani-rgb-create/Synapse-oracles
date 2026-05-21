package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.UserProfileEntity
import com.example.viewmodel.BackupPayload
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("NúcleoEsencia", appName)
  }

  @Test
  fun `verify backup payload serialization with moshi`() {
    val moshi = Moshi.Builder()
      .addLast(KotlinJsonAdapterFactory())
      .build()
      
    val payload = BackupPayload(
      userProfile = UserProfileEntity(
        name = "Alchemist Guest",
        birthDate = "1994-05-20",
        birthPlace = "Madrid",
        mainArchetype = "El Sabio"
      ),
      diaryEntries = emptyList(),
      dreamEntries = emptyList()
    )

    val adapter = moshi.adapter(BackupPayload::class.java)
    val json = adapter.toJson(payload)
    assertNotNull(json)

    val reconstructed = adapter.fromJson(json)
    assertNotNull(reconstructed)
    assertEquals("Alchemist Guest", reconstructed?.userProfile?.name)
    assertEquals("El Sabio", reconstructed?.userProfile?.mainArchetype)
  }
}
