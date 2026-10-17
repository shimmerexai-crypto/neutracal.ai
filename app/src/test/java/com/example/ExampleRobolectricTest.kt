package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.IndianFoodDatabase
import com.example.model.CookingStyle
import com.example.model.FitnessGoal
import com.example.model.Gender
import com.example.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Nutracal.ai", appName)
  }

  @Test
  fun `verify user profile BMI calculation`() {
    val profile = UserProfile(
      name = "Aarav",
      gender = Gender.MALE,
      age = 26,
      heightCm = 172f,
      weightKg = 70f,
      goal = FitnessGoal.WEIGHT_LOSS
    )
    assertTrue("BMI should be around 23.6", profile.bmi in 23.0f..24.0f)
    assertEquals("Normal (Healthy)", profile.bmiCategory)
  }

  @Test
  fun `verify Indian food database contains authentic staples`() {
    val bajreRoti = IndianFoodDatabase.search("bajre")
    assertTrue("Should find Bajre Ki Roti", bajreRoti.isNotEmpty())

    val dalBaati = IndianFoodDatabase.search("baati")
    assertTrue("Should find Dal Baati Churma", dalBaati.isNotEmpty())
  }
}
