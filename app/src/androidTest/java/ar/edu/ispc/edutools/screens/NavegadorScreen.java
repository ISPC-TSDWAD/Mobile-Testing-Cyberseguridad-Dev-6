package ar.edu.ispc.edutools.screens;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

/** Encapsula el selector del sistema y la navegacion externa. */
public class NavegadorScreen {
    private final UiDevice device;
    public NavegadorScreen(UiDevice device) { this.device = device; }

    public void verificarAperturaYVolver(String appPackage) throws Exception {
        assertTrue(device.wait(Until.gone(By.pkg(appPackage)), 10000));
        UiObject chrome = device.findObject(new UiSelector().textContains("Chrome"));
        if (chrome.exists()) {
            chrome.click();
            UiObject unaVez = device.findObject(new UiSelector().resourceId("android:id/button_once"));
            if (unaVez.waitForExists(1500)) unaVez.click();
        }
        device.waitForIdle();
        String destino = device.getCurrentPackageName();
        assertFalse("Debe abrir una app externa", appPackage.equals(destino));
        assertFalse("No debe quedarse en el selector", "android".equals(destino));
        device.pressBack();
        assertTrue(device.wait(Until.hasObject(By.pkg(appPackage)), 10000));
    }
}
