package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.CenterFocusStrong
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.RestaurantMenu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.model.AppLanguage
import com.example.model.CookingStyle
import com.example.ui.components.CookingStylePicker
import com.example.ui.theme.NutracalAmber
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalCardBorder
import com.example.ui.theme.NutracalGreen
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite
import com.example.viewmodel.NutracalViewModel
import com.example.viewmodel.ScanUiState

@Composable
fun ScanScreen(
    viewModel: NutracalViewModel,
    onScanSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val cookingStyle by viewModel.cookingStyle.collectAsState()
    val scanState by viewModel.scanState.collectAsState()

    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            selectedBitmap = bitmap
            viewModel.startFoodScan(bitmap, dishHint = "Indian Thali")
        }
    }

    // Photo picker launcher (zero-permission)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                selectedBitmap = bitmap
                viewModel.startFoodScan(bitmap, dishHint = "Indian Meal")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // When scan succeeds, navigate to result screen
    if (scanState is ScanUiState.Success) {
        onScanSuccess()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NutracalWhite)
            .statusBarsPadding()
            .testTag("scan_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Localization.t("tab_scan", language),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutracalBlack
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(NutracalCardBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ICMR-NIN Flash AI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutracalGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 1: Pre-Scan Cooking Style Selector (Mandatory requirement)
            CookingStylePicker(
                selectedStyle = cookingStyle,
                onStyleSelected = { viewModel.setCookingStyle(it) },
                language = language
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Step 2: Circular Camera Viewfinder Overlay
            val infiniteTransition = rememberInfiniteTransition(label = "scan_pulse")
            val scanAngle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Restart),
                label = "angle"
            )

            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF141416))
                    .clickable { cameraLauncher.launch(null) },
                contentAlignment = Alignment.Center
            ) {
                // If user took or selected a photo, show it inside the lens
                if (selectedBitmap != null) {
                    Image(
                        bitmap = selectedBitmap!!.asImageBitmap(),
                        contentDescription = "Selected food",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Circular scanner reticle with subtle rotation
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(scanAngle)
                ) {
                    val strokeW = 3.dp.toPx()
                    drawCircle(
                        color = Color(0x33FFFFFF),
                        radius = (size.width / 2) - strokeW,
                        style = Stroke(
                            width = strokeW,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 20f), 0f)
                        )
                    )
                }

                // Inner Guide
                Canvas(modifier = Modifier.size(180.dp)) {
                    drawCircle(
                        color = NutracalGreen.copy(alpha = 0.5f),
                        radius = size.width / 2,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }

                if (selectedBitmap == null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CenterFocusStrong,
                            contentDescription = "Target",
                            tint = NutracalWhite.copy(alpha = 0.9f),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = Localization.t("camera_instructions", language),
                            fontSize = 12.sp,
                            color = NutracalWhite.copy(alpha = 0.9f),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap circle to capture",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutracalGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons: Capture & Gallery
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { cameraLauncher.launch(null) },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("scan_take_photo_btn"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NutracalBlack)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CameraAlt,
                        contentDescription = null,
                        tint = NutracalWhite,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Localization.t("take_photo", language),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutracalWhite
                    )
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clickable {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .testTag("scan_gallery_btn"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NutracalCardBorder)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Image,
                            contentDescription = null,
                            tint = NutracalBlack,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = Localization.t("pick_gallery", language),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NutracalBlack
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Test Sample Thalis (Crucial for streaming emulator without a physical camera!)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = NutracalAmber,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Localization.t("try_demo", language),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutracalSecondary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                val sampleThalis = listOf(
                    "North Indian Ghar Ka Thali (Roti, Dal, Sabzi, Dahi)",
                    "Rajasthani Dal Baati Churma with Lasun Chutney",
                    "Bajre Ki Roti & Gatte Ki Sabzi with Chaas",
                    "South Indian Masala Dosa with Sambar",
                    "Hyderabadi Chicken Biryani with Raita"
                )

                sampleThalis.forEach { thali ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                viewModel.startFoodScan(null, dishHint = thali)
                            }
                            .testTag("sample_thali_${thali.take(10).replace(" ", "_").lowercase()}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = thali,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = NutracalBlack,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = NutracalGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Friendly Animated Loading Overlay
        if (scanState is ScanUiState.Loading) {
            val loadingMsg = (scanState as ScanUiState.Loading).message
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.65f))
                    .testTag("scan_loading_overlay"),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalWhite),
                    modifier = Modifier.padding(32.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = NutracalBlack,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Analyzing Masala & Tadka...",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutracalBlack
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = Localization.t("scanning_loader", language),
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = NutracalSecondary
                        )
                    }
                }
            }
        }
    }
}
