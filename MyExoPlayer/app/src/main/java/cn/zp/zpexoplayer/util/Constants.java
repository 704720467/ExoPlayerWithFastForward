package cn.zp.zpexoplayer.util;

import android.media.AudioFormat;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;

import cn.zp.zpexoplayer.BuildConfig;


/**
 * @author guoxian
 *         <p>
 *         <p>
 *         v0.9.1 app 3.01
 *         </p>
 *         <p>
 *         V0.9.2 原生变为0.2，修复红米慢动作音频播放crash
 *         </p>
 *         <p>
 *         V0.9.3 慢放后视频进度;部分手机片尾不能渐变
 *         </p>
 *         <p>
 *         V0.9.4 chunk resize 后引起的播放问题
 *         </p>
 *         <p>
 *         V0.9.5 单视频慢放+resize后导出崩溃
 *         </p>
 *         <p>
 *         V0.9.6 原始视频最大的关键帧间隔
 *         </p>
 *         <p>
 *         V0.9.6.1 3.4版本
 *         </p>
 */
public class Constants {
    public static final String TAG = "AE_COMMON";
    public static final String TAG_EDIT = "AE_EDIT";
    public static final String TAG_PERFORMENCE = "AE_PERFORMENCE";
    public static final String TAG_LIFECYCLE_TAG = "AE_LIFECYCLE";
    public static final String TAG_GL = "AE_GL";
    public static final String TAG_AUDIO_MUTE = "AE_AUDIO_MUTE";
    public static final String TAG_EXPORT_MANAGER = "AE_EXPORT_MANAGER";
    public static final String TAG_EXPORT_ENCODER = "TAG_EXPORT_ENCODER";
    public static final String TAG_AUDIO = "AE_AUDIO";
    public static final String TAG_VIDEO = "AE_VIDEO";
    public static final String TAG_PLAYBACK = "AE_PLAYBACK";
    public static final String TAG_AUDIO_MIX = "AE_AUDIO_MIX";
    public static final String TAG_PERSISTENT = "AE_PERSISTENT";
    public static final float VERSION = 1.000f; //

    /**
     * 是否是junit模式,正式版一定要改为fasel
     */
    public static boolean TEST = false && BuildConfig.DEBUG;

    /**
     * 是否打印log
     */
    public static boolean VERBOSE = true;

    /**
     * 是否打印编解码过程中每一帧的log
     */
    public static boolean VERBOSE_CODEC_V = VERBOSE;

    public static boolean VERBOSE_LOOP_V = BuildConfig.DEBUG;

    public static boolean VERBOSE_CODEC_A = VERBOSE;

    public static boolean VERBOSE_LOOP_A = BuildConfig.DEBUG;
    public static boolean VERBOSE_TEST = BuildConfig.DEBUG;

    /**
     * 从视频提取关键帧时最大的帧数
     */
    public static final int MAX_FRAMES = 64;

    public static final float DEFAULT_FPS = 25;

    /**
     * 默认关键帧间隔，单位秒
     */
    public static final int DEFAULT_IFRAME_INTERVAL = 1;

    /**
     * 一百万。微秒的单位
     */
    public static final long US_MUTIPLE = 1000000;// 百万分之一秒的单位 微秒，
    /**
     * codec queue 超时时间
     */
    public static final long TIMEOUT_USEC = 10000;// 百分之一秒。单位是US

    /**
     * 允许添加的视频的最小长度
     */
    public static final float MIN_VIDEOFILE_DURATION = 1.8f;

    /**
     * CHUNK最小长度
     */
    public static final float MIN_CHUNK_DURATION = 1.8f;

    /**
     * 过场最大长度1.5s
     */
    public static final float MAX_TRANSITION_DURATION = 1.5f;

    /**
     * 1.6f片尾长度，单位秒
     */
    public static float TAILER_LENGTH = 1.6f;

    public static float MAX_VOLUME = 1f;

    public static int DEFAULT_AUDIO_SAMPLE_RATE = 44100;
    public static int DEFAULT_AUDIO_CHANNEL_COUNT = 1;
    public static int DEFAULT_AUDIO_CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    public static final int OUTPUT_AUDIO_BIT_RATE = 128 * 1024; // 128k
    /**
     * video/avc
     */
    public static final String VIDEO_MIME_TYPE = MediaFormat.MIMETYPE_VIDEO_AVC;
    public static final String AUDIO_MIME_TYPE = "audio/mp4a-latm";

    public static final int OUTPUT_AUDIO_AAC_PROFILE = MediaCodecInfo.CodecProfileLevel.AACObjectLC;

    public static final int DEFAULT_EXPORT_WIDTH = 960;// 1024
    public static final int DEFAULT_EXPORT_HEIGHT = 540;// 576
    /**
     * 2mbps
     */
    public static final int DEFAULT_EXPORT_BITRATE = 1024 * 1024 * 1;

    public static final float DEFAULT_SLOW_SPEED = 1.5f;

    public static final String ASSERT_FILE_PREFIX = "assert://";

    /**
     * 过场时长
     */
    public static final float TRANSITION_DURATION = 1.f;

    /**
     * 过场的最低alph
     */
    public static final float TANSITION_ALPH = 0.6f;

    public static final float ORIGINAL_SOUND_VOLUME_BY_MIXER = 1f;

    public static final String DUMMY_AUDIO_PATH = "assert://bgmusic/0_1s.aac";

    public static final String PLACEHOLDER_VIDEO = "assert://placeholder.mp4";

    public static final String DEFAULT_COVER_IMAGE = "assert://defualt_cover.png";
    /**
     * 可接受的原始视频最大的关键帧间隔
     */
    public static final int ACCECPT_VIDEO_MAX_GOP = 300;

    /**
     * 当即将开始下一个解码线程时，提前几帧启动线程
     */
    public static final int PRE_INIT_THREAD_OF_FRAME_COUNTS = 25;
    /**
     * 默认背景音乐大小
     */
    public final static float DEFOULT_BG_VOLUME = 0.7f;
    /**
     * 默认原音音量
     */
    public final static float DEFOULT_VOLUME = 0.3f;
}
