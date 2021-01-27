package com.craftland.engine.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.craftland.R;
import com.craftland.engine.input.InputManager;
import com.craftland.engine.input.VirtualGamepad;

public class Render extends Activity implements GLSurfaceView.Renderer
{
    public static Activity activity;
    public static Context context;
    public static GL10 gl;
    public static int zoom_width = 0;
    public static int zoom_height = 0;
    public static int display_width = 0;
    public static int display_height = 0;
    public static Camera camera = new Camera();
    public static boolean pause = false;
    public static double timeDelta;
    public static InputManager inputManager;
    public static VirtualGamepad gamepad;
    public GLSurfaceView glView;

    private Time time;
    private boolean loaded = false;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        render_init(this, R.layout.activity_opengl, R.id.glSurface, 300, 169, true);
    }

    public void render_init (Activity activity, int layout_id, int glSurface, int w, int h, boolean fullScreen)
    {
        if (fullScreen) {
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if(Build.VERSION.SDK_INT >= 19)
            {
                View decorView = activity.getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
        activity.setContentView(layout_id);
        this.activity = activity;
        context = activity.getApplicationContext();
        zoom_width = w;
        zoom_height = h;
        inputManager = new InputManager();
        time = new Time();
        timeDelta = 0;
        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        init(glSurface);
    }

    public void load(){}
    public void update() {}
    public void draw() {}
    public void draw_interface() {}

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        if(Render.gl == null)
            Render.gl = gl;
        gl.glClearColor(0, 0, 0, 1);
        gl.glClearDepthf(1.0f);
        gl.glFrontFace(GL10.GL_CCW);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0, zoom_width, zoom_height, 0, 0, 1);
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_MODELVIEW);

        if(!loaded) {
            loaded = true;
            display_width = width;
            display_height = height;
            gamepad = new VirtualGamepad();
            load();
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        time.updateDeltaTime();
        camera.update();
        update();

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(camera.X, camera.X + zoom_width,
                camera.Y + zoom_height, camera.Y, 0, 1);
        gl.glViewport(0, 0, display_width, display_height);
        gl.glMatrixMode(GL10.GL_MODELVIEW);

        draw();

        // --- draw GUI ---

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0, zoom_width, zoom_height, 0, 0, 1);
        gl.glViewport(0, 0, display_width, display_height);
        gl.glMatrixMode(GL10.GL_MODELVIEW);

        draw_interface();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(int glSurface)
    {
        glView = activity.findViewById(glSurface);
        glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glView.setRenderer(this);
        glView.requestFocus();
        glView.setFocusableInTouchMode(true);

        glView.setOnTouchListener(new android.view.View.OnTouchListener() {
            @Override
            public boolean onTouch(android.view.View v, MotionEvent motionEvent) {
                int pointerIndex = ((motionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
                int pointerId = motionEvent.getPointerId(pointerIndex);
                int action = (motionEvent.getAction() & MotionEvent.ACTION_MASK);
                int pointCnt = motionEvent.getPointerCount();
                if (pointCnt <= 2) {
                    if (pointerIndex <= 2 - 1) {
                        for (int i = 0; i < pointCnt; i++) {
                            int id = motionEvent.getPointerId(i);
                            inputManager.touch[id].X = motionEvent.getX(i) / ((float) display_width / zoom_width);
                            inputManager.touch[id].Y = motionEvent.getY(i) / ((float) display_height / zoom_height);
                        }
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                inputManager.touchPointer[pointerId] = true;
                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                inputManager.touchPointer[pointerId] = true;
                                break;
                            case MotionEvent.ACTION_MOVE:
                                inputManager.touchPointer[pointerId] = true;
                                break;
                            case MotionEvent.ACTION_UP:
                                inputManager.touchPointer[pointerId] = false;
                                break;
                            case MotionEvent.ACTION_POINTER_UP:
                                inputManager.touchPointer[pointerId] = false;
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                inputManager.touchPointer[pointerId] = false;
                                break;
                            default:
                                inputManager.touchPointer[pointerId] = false;
                                break;
                        }
                    }
                }
                return true;
            }
        });

        glView.setOnKeyListener(new android.view.View.OnKeyListener() {
            @Override
            public boolean onKey(android.view.View v, int keyCode, KeyEvent event) {
                int action = event.getAction();
                if(action == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
                        inputManager.dpadLeft = 1;
                    if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
                        inputManager.dpadRight = 1;
                    if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
                        inputManager.dpadUp = 1;
                    if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
                        inputManager.dpadDown = 1;
                    if(keyCode == KeyEvent.KEYCODE_Z)
                        inputManager.z = 1;
                    if(keyCode == KeyEvent.KEYCODE_X)
                        inputManager.x = 1;
                }
                if(action == KeyEvent.ACTION_UP){
                    if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        inputManager.dpadLeft = 0;
                    }
                    if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        inputManager.dpadRight = 0;
                    }
                    if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        inputManager.dpadUp = 0;
                    }
                    if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        inputManager.dpadDown = 0;
                    }
                }
                // volume control
                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                }
                if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                }
                return true;
            }
        });
    }
}
