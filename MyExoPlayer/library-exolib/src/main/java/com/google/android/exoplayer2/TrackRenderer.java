//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.google.android.exoplayer2;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer.ExoPlayerComponent;
import com.google.android.exoplayer2.util.Assertions;

public abstract class TrackRenderer implements ExoPlayerComponent {
    protected static final int STATE_RELEASED = -2;
    protected static final int STATE_IGNORE = -1;
    protected static final int STATE_UNPREPARED = 0;
    protected static final int STATE_PREPARED = 1;
    protected static final int STATE_ENABLED = 2;
    protected static final int STATE_STARTED = 3;
    public static final long UNKNOWN_TIME_US = -1L;
    public static final long MATCH_LONGEST_US = -2L;
    public static final long END_OF_TRACK_US = -3L;
    private int state;

    public TrackRenderer() {
    }

    protected boolean isTimeSource() {
        return false;
    }

    protected final int getState() {
        return this.state;
    }

    final int prepare(long positionUs) throws ExoPlaybackException {
        Assertions.checkState(this.state == 0);
        this.state = this.doPrepare(positionUs);
        Assertions.checkState(this.state == 0 || this.state == 1 || this.state == -1);
        return this.state;
    }

    protected abstract int doPrepare(long var1) throws ExoPlaybackException;

    final void enable(long positionUs, boolean joining) throws ExoPlaybackException {
        Assertions.checkState(this.state == 1);
        this.state = 2;
        this.onEnabled(positionUs, joining);
    }

    protected void onEnabled(long positionUs, boolean joining) throws ExoPlaybackException {
    }

    final void start() throws ExoPlaybackException {
        Assertions.checkState(this.state == 2);
        this.state = 3;
        this.onStarted();
    }

    protected void onStarted() throws ExoPlaybackException {
    }

    final void stop() throws ExoPlaybackException {
        Assertions.checkState(this.state == 3);
        this.state = 2;
        this.onStopped();
    }

    protected void onStopped() throws ExoPlaybackException {
    }

    final void disable() throws ExoPlaybackException {
        Assertions.checkState(this.state == 2);
        this.state = 1;
        this.onDisabled();
    }

    protected void onDisabled() throws ExoPlaybackException {
    }

    final void release() throws ExoPlaybackException {
        Assertions.checkState(this.state != 2 && this.state != 3 && this.state != -2);
        this.state = -2;
        this.onReleased();
    }

    protected void onReleased() throws ExoPlaybackException {
    }

    protected abstract boolean isEnded();

    protected abstract boolean isReady();

    protected abstract void doSomeWork(long var1, long var3) throws ExoPlaybackException;

    protected abstract long getDurationUs();

    protected abstract long getCurrentPositionUs();

    protected abstract long getBufferedPositionUs();

    protected abstract void seekTo(long var1) throws ExoPlaybackException;

    public void handleMessage(int what, Object object) throws ExoPlaybackException {
    }
}
