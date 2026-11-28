package org.sableos.reader.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.vaachak.reader.leisure.ui.testability.Tid
import org.vaachak.reader.leisure.ui.testability.tid
import org.vaachak.reader.leisure.ui.testability.tids

/**
 * The Reader's top bar: one slim row in the Sable look of the settings screen. The title (dropped on short screens),
 * then Books, Comics, Audio and Settings as small icon-and-label pills, the selected one tinted with the accent, and the
 * profile switcher at the end in multi-user mode. About 40 dp tall instead of the 100 dp of a title row over a tab row.
 */
@Composable
fun SableTopBar(
    tabs: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    profileName: String,
    showProfile: Boolean,
    onSwitchProfileClick: () -> Unit,
    short: Boolean,
) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (!short) {
            Text(
                text = "Sable Reader",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
            Spacer(Modifier.weight(1f))
        }
        tabs.forEachIndexed { index, title ->
            val chosen = selectedIndex == index
            TabPill(
                title = title,
                icon = iconFor(index),
                selected = chosen,
                onClick = { onSelect(index) },
                modifier = when (index) {
                    0 -> Modifier.tid(Tid.Tabs.books)
                    1 -> Modifier.tid(Tid.Tabs.comics)
                    2 -> Modifier.tid(Tid.Tabs.audio)
                    else -> Modifier.tids(Tid.Bookshelf.SETTINGS_TAB, Tid.Bookshelf.OPEN_SETTINGS)
                }.then(if (short) Modifier.weight(1f) else Modifier),
            )
        }
        if (showProfile) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .tid(Tid.Vault.switchProfile)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onSwitchProfileClick)
                    .padding(horizontal = 6.dp, vertical = 4.dp)
                    .semantics(mergeDescendants = true) { contentDescription = "Switch Profile, Current: $profileName" },
            ) {
                Icon(Icons.Default.AccountCircle, contentDescription = null, tint = colors.onSurface, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text(profileName, style = MaterialTheme.typography.labelMedium, color = colors.onSurface, maxLines = 1)
            }
        }
    }
}

private fun iconFor(index: Int): ImageVector = when (index) {
    1 -> Icons.Outlined.Photo
    2 -> Icons.Default.Audiotrack
    3 -> Icons.Default.Settings
    else -> Icons.Default.Book
}

@Composable
private fun TabPill(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val tint = if (selected) colors.primary else colors.onSurfaceVariant
    Row(
        modifier = modifier
            .heightIn(min = 32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) colors.primary.copy(alpha = 0.16f) else colors.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .semantics(mergeDescendants = true) {
                role = Role.Tab
                this.selected = selected
                contentDescription = "$title Tab, ${if (selected) "Selected" else "Not Selected"}"
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = tint,
            maxLines = 1,
        )
    }
}
