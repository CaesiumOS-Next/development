/*
 * Copyright 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.commands.monkey;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.view.IWindowManager;
import android.view.Surface;

import org.junit.Test;

public class MonkeyRotationEventTest {
    @Test
    public void createRestoreEvent_restoresLockedRotation() throws Exception {
        IWindowManager windowManager = mock(IWindowManager.class);
        when(windowManager.getDefaultDisplayRotation()).thenReturn(Surface.ROTATION_90);
        when(windowManager.isRotationFrozen()).thenReturn(true);

        MonkeyRotationEvent event = MonkeyRotationEvent.createRestoreEvent(windowManager);

        assertEquals(
                MonkeyEvent.INJECT_SUCCESS,
                event.injectEvent(windowManager, /* iam= */ null, /* verbose= */ 0));
        verify(windowManager).freezeRotation(
                Surface.ROTATION_90, "MonkeyRotationEven#injectEvent");
        verify(windowManager, never()).thawRotation("MonkeyRotationEven#injectEvent");
    }

    @Test
    public void createRestoreEvent_restoresUnlockedRotation() throws Exception {
        IWindowManager windowManager = mock(IWindowManager.class);
        when(windowManager.getDefaultDisplayRotation()).thenReturn(Surface.ROTATION_270);
        when(windowManager.isRotationFrozen()).thenReturn(false);

        MonkeyRotationEvent event = MonkeyRotationEvent.createRestoreEvent(windowManager);

        assertEquals(
                MonkeyEvent.INJECT_SUCCESS,
                event.injectEvent(windowManager, /* iam= */ null, /* verbose= */ 0));
        verify(windowManager).freezeRotation(
                Surface.ROTATION_270, "MonkeyRotationEven#injectEvent");
        verify(windowManager).thawRotation("MonkeyRotationEven#injectEvent");
    }
}
