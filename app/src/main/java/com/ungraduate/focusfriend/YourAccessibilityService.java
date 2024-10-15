package com.ungraduate.focusfriend;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

public class YourAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Ensure we are inside the YouTube app
        if ("com.google.android.youtube".equals(event.getPackageName())) {
            // Check for click events
            if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
                AccessibilityNodeInfo clickedNode = event.getSource();
                if (clickedNode != null) {
                    // Check if the clicked node is the Shorts tab
                    if (isShortsTab(clickedNode)) {
                        // Block the action by not performing it and instead redirect to Home
                        redirectToHome();
                    }
                }
            }
        }
    }

    private boolean isShortsTab(AccessibilityNodeInfo node) {
        // Check if the node contains "Shorts" in its content description
        CharSequence contentDescription = node.getContentDescription();
        return contentDescription != null && contentDescription.toString().toLowerCase().contains("shorts");
    }

    private void redirectToHome() {
        // Find the Home tab and perform a click action on it
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode != null) {
            List<AccessibilityNodeInfo> homeTabs = rootNode.findAccessibilityNodeInfosByText("Home");
            for (AccessibilityNodeInfo homeNode : homeTabs) {
                if (homeNode != null && homeNode.isClickable()) {
                    homeNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Toast.makeText(this, "Redirected to Home!", Toast.LENGTH_SHORT).show();
                    break; // Exit after redirecting
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
        // Handle when the service is interrupted or stopped
    }
}
