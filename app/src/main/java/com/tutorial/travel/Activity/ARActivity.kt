package com.tutorial.travel.Activity

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isGone
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.ar.core.Config
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.node.VideoNode
import android.view.MotionEvent
import com.tutorial.travel.R
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation

class ARActivity : AppCompatActivity() {

    private lateinit var sceneView: ArSceneView
    private lateinit var placeButton: ExtendedFloatingActionButton
    private lateinit var zoomInButton: ExtendedFloatingActionButton
    private lateinit var zoomOutButton: ExtendedFloatingActionButton
    private lateinit var rotateButton: ExtendedFloatingActionButton
    private lateinit var modelNode: ArModelNode
    private lateinit var videoNode: VideoNode
    private lateinit var mediaPlayer: MediaPlayer
    private var scaleFactor = 1.0f // Default scale factor
    private var rotationAngle = 0f // Default rotation angle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        sceneView = findViewById<ArSceneView?>(R.id.sceneView).apply {
            this.lightEstimationMode = Config.LightEstimationMode.DISABLED
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.videooke)

        placeButton = findViewById(R.id.place)
        zoomInButton = findViewById(R.id.zoom_in)
        zoomOutButton = findViewById(R.id.zoom_out)
        rotateButton = findViewById(R.id.rotate)

        placeButton.setOnClickListener {
            placeModel()
        }

        zoomInButton.setOnClickListener {
            scaleModel(1.1f) // Zoom in by increasing scale factor
        }

        zoomOutButton.setOnClickListener {
            scaleModel(0.9f) // Zoom out by decreasing scale factor
        }

        rotateButton.setOnClickListener {
            rotateModel(15f) // Rotate the model by 15 degrees
        }

        videoNode = VideoNode(sceneView.engine, scaleToUnits = 1f, centerOrigin = Position(y = -4f), glbFileLocation = "models/plane.glb", player = mediaPlayer, onLoaded = { _, _ ->
            mediaPlayer.start()
        })

        modelNode = ArModelNode(sceneView.engine, PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = "models/taj_mahal_3d_model.glb",
                scaleToUnits = 1f,
                centerOrigin = Position(-0.5f)
            ) {
                sceneView.planeRenderer.isVisible = true
                val materialInstance = it.materialInstances[0]
            }
            onAnchorChanged = {
                placeButton.isGone = it != null
            }
        }

        sceneView.addChild(modelNode)
        modelNode.addChild(videoNode)
    }

    private fun placeModel() {
        modelNode.anchor()
        sceneView.planeRenderer.isVisible = false
    }

    private fun scaleModel(scaleFactorChange: Float) {
        scaleFactor *= scaleFactorChange
        scaleFactor = scaleFactor.coerceIn(0.1f, 5.0f) // Limit the scale factor
        modelNode.scale = Position(scaleFactor, scaleFactor, scaleFactor) // Apply scaling
    }

    private fun rotateModel(angle: Float) {
        rotationAngle += angle
        modelNode.rotation = Rotation(0f, rotationAngle, 0f) // Apply rotation
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
