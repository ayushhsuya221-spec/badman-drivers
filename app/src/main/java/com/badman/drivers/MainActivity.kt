package com.badman.drivers

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Bg = Color(0xFF070610)
private val Panel = Color(0xFF141225)
private val Purple = Color(0xFF8B35FF)
private val Cyan = Color(0xFF19D9F5)
private val Green = Color(0xFF39F5A2)
private val Muted = Color(0xFF9996B5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(7, 6, 16)
        window.navigationBarColor = android.graphics.Color.rgb(7, 6, 16)
        setContent { BadmanApp() }
    }
}

@Composable
private fun BadmanApp() {
    val context = LocalContext.current
    var splash by remember { mutableStateOf(true) }
    var busy by remember { mutableStateOf(false) }
    var unlocked by remember { mutableStateOf(false) }
    var root by remember { mutableStateOf("Checking") }
    var driver by remember { mutableStateOf("Not verified") }
    var log by remember { mutableStateOf(listOf("BADMAN DRIVER'S diagnostic console ready.")) }
    val rotation by rememberInfiniteTransition(label = "ring").animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing)), label = "rotate"
    )

    fun beep(success: Boolean = false) {
        try {
            val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 55)
            tone.startTone(if (success) ToneGenerator.TONE_PROP_ACK else ToneGenerator.TONE_PROP_BEEP, 90)
        } catch (_: Exception) { }
    }
    fun addLog(s: String) {
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        log = (log + "[$time] $s").takeLast(35)
    }

    LaunchedEffect(Unit) {
        delay(2200)
        splash = false
        beep(true)
        unlocked = true
        root = if (android.os.Process.myUid() == 0) "ROOT PROCESS" else "DENIED / NOT ROOT"
        addLog("Device diagnostics initialized.")
        addLog("Kernel: ${System.getProperty("os.version") ?: "unknown"}")
        addLog("Root status checked: $root")
    }

    MaterialTheme(colorScheme = darkColorScheme(background = Bg, surface = Panel, primary = Cyan)) {
        Box(Modifier.fillMaxSize().background(Bg)) {
            if (splash) {
                Column(Modifier.fillMaxSize().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Box(Modifier.size(270.dp), contentAlignment = Alignment.Center) {
                        Box(Modifier.fillMaxSize().rotate(rotation).border(3.dp, Brush.sweepGradient(listOf(Cyan, Purple, Color(0xFFFF3CC7), Cyan)), CircleShape))
                        Image(painterResource(R.drawable.badman_logo), contentDescription = "BADMAN logo", modifier = Modifier.size(246.dp).clip(CircleShape), contentScale = ContentScale.Crop)
                    }
                    Spacer(Modifier.height(30.dp))
                    Text("BADMAN DRIVER'S", fontSize = 27.sp, fontWeight = FontWeight.Black, color = Cyan, textAlign = TextAlign.Center)
                    Text("KERNEL DRIVER MANAGER", fontSize = 11.sp, letterSpacing = 3.sp, color = Color.White)
                    Spacer(Modifier.height(32.dp))
                    Icon(Icons.Default.LockOpen, contentDescription = "Unlocked", tint = if (unlocked) Green else Purple, modifier = Modifier.size(35.dp))
                    Spacer(Modifier.height(14.dp))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(.62f), color = Cyan, trackColor = Panel)
                    Spacer(Modifier.height(10.dp))
                    Text(if (unlocked) "ACCESS SEQUENCE COMPLETE" else "INITIALIZING SECURE CONSOLE…", color = Muted, fontSize = 11.sp)
                }
            } else {
                Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(R.drawable.badman_logo), contentDescription = "Logo", modifier = Modifier.size(58.dp).clip(CircleShape).border(1.dp, Purple, CircleShape), contentScale = ContentScale.Crop)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("BADMAN DRIVER'S", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Black)
                            Text("PAPA BOL • DRIVER MANAGER", color = Cyan, fontSize = 10.sp, letterSpacing = 1.sp)
                            Text("v1.0 • DIAGNOSTIC BUILD", color = Muted, fontSize = 10.sp)
                        }
                        Icon(Icons.Default.LockOpen, contentDescription = "Console unlocked", tint = Green, modifier = Modifier.size(28.dp))
                    }
                    Spacer(Modifier.height(22.dp))
                    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(22.dp)) {
                        Column(Modifier.padding(18.dp)) {
                            Text(Build.MODEL ?: "Android device", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text("ANDROID ${Build.VERSION.RELEASE} • SDK ${Build.VERSION.SDK_INT}", color = Cyan, fontSize = 13.sp)
                            Spacer(Modifier.height(16.dp)); HorizontalDivider(color = Color(0xFF343149)); Spacer(Modifier.height(14.dp))
                            Text("KERNEL VERSION", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(System.getProperty("os.version") ?: "Unavailable", color = Color.White, fontSize = 14.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("ARCHITECTURE  ${Build.SUPPORTED_ABIS.firstOrNull() ?: "Unknown"}", color = Muted, fontSize = 11.sp)
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatusCard("ROOT STATUS", root, Icons.Default.Security, Modifier.weight(1f), if (root == "ROOT PROCESS") Green else Color(0xFFFF6978))
                        StatusCard("DRIVER STATUS", driver, Icons.Default.Memory, Modifier.weight(1f), if (driver == "Verified loaded") Green else Muted)
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("DRIVER CONTROL", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Actions below inspect state only; no driver is loaded by this build.", color = Muted, fontSize = 11.sp)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = {
                        beep(); busy = true
                    }, enabled = !busy, modifier = Modifier.fillMaxWidth().height(54.dp), colors = ButtonDefaults.buttonColors(containerColor = Cyan, contentColor = Bg), shape = RoundedCornerShape(14.dp)) {
                        if (busy) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Bg, strokeWidth = 2.dp) else Icon(Icons.Default.Refresh, null)
                        Spacer(Modifier.width(8.dp)); Text(if (busy) "CHECKING…" else "CHECK DRIVER", fontWeight = FontWeight.Bold)
                    }
                    LaunchedEffect(busy) {
                        if (busy) {
                            addLog("Checking kernel and module inventory…")
                            delay(850)
                            driver = "Not verified"
                            addLog("No privileged module load attempted.")
                            addLog("Result: driver status cannot be confirmed by this unprivileged app.")
                            busy = false
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = { beep(); addLog("QX selected: compatibility review required; no binary executed.") }, modifier = Modifier.weight(1f).height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = Purple), shape = RoundedCornerShape(13.dp)) { Text("QX • INSPECT", fontWeight = FontWeight.Bold) }
                        Button(onClick = { beep(); addLog("RT selected: compatibility review required; no binary executed.") }, modifier = Modifier.weight(1f).height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6326A8)), shape = RoundedCornerShape(13.dp)) { Text("RT • INSPECT", fontWeight = FontWeight.Bold) }
                    }
                    Spacer(Modifier.height(20.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) { Text("TERMINAL", color = Green, fontWeight = FontWeight.Bold); Spacer(Modifier.weight(1f)); TextButton(onClick = { beep(); log = emptyList(); addLog("Console cleared.") }) { Text("CLEAR", color = Cyan) } }
                    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFF020805)).border(1.dp, Color(0xFF12472D), RoundedCornerShape(14.dp)).padding(14.dp)) {
                        log.forEach { Text(it, color = if (it.contains("Result:")) Color(0xFFFFC56D) else Green, fontSize = 11.sp, lineHeight = 17.sp) }
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("Secure diagnostic mode • no hidden process features • no automatic root commands", color = Muted, fontSize = 10.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(18.dp))
                }
            }
        }
    }
}

@Composable
private fun StatusCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier, tint: Color) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) { Icon(icon, null, tint = tint, modifier = Modifier.size(17.dp)); Spacer(Modifier.width(6.dp)); Text(title, color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(10.dp))
            Text(value, color = tint, fontSize = 13.sp, fontWeight = FontWeight.Bold, lineHeight = 17.sp)
        }
    }
}
